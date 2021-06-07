package tw.musemodel.dingzhiqingren.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.musemodel.dingzhiqingren.entity.Activation;
import tw.musemodel.dingzhiqingren.entity.Country;
import tw.musemodel.dingzhiqingren.entity.LineUserProfile;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.entity.Role;
import tw.musemodel.dingzhiqingren.entity.User;
import tw.musemodel.dingzhiqingren.event.SignedUpEvent;
import tw.musemodel.dingzhiqingren.model.Activated;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.model.SignUp;
import tw.musemodel.dingzhiqingren.repository.ActivationRepository;
import tw.musemodel.dingzhiqingren.repository.CountryRepository;
import tw.musemodel.dingzhiqingren.repository.LineUserProfileRepository;
import tw.musemodel.dingzhiqingren.repository.LoverRepository;
import tw.musemodel.dingzhiqingren.repository.UserRepository;

/**
 * 服务层：情人
 *
 * @author p@musemodel.tw
 */
@Service
public class LoverService {

	private final static Logger LOGGER = LoggerFactory.getLogger(LoverService.class);

	private static final AmazonSNS AMAZON_SNS = AmazonSNSClientBuilder.defaultClient();

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Servant servant;

	@Autowired
	private ActivationRepository activationRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private LineUserProfileRepository lineUserProfileRepository;

	@Autowired
	private LoverRepository loverRepository;

	@Autowired
	private UserRepository userRepository;

	public List<Lover> loadLovers() {
		return loverRepository.findAll();
	}

	@Transactional
	public JSONObject activate(String inputString) {
		Activation activation = activationRepository.
			findOneByString(inputString).
			orElseThrow();
		if (Objects.isNull(activation.getString()) || Objects.nonNull(activation.getOccurred())) {
			throw new RuntimeException("activation.activated");
		}

		activation.setString(null);
		activation.setOccurred(new Date(System.currentTimeMillis()));
		activation = activationRepository.saveAndFlush(activation);
		return new JavaScriptObjectNotation().
			withReason("activation.done").
			withRedirect(String.format(
				"/activated.asp?id=%s",
				activation.getLover().getIdentifier().toString()
			)).
			withResponse(true).
			toJSONObject();
	}

	@Transactional
	public JSONObject activated(Activated activated, HttpServletRequest request) {
		Lover lover = loadByIdentifier(activated.getIdentifier());
		if (Objects.isNull(lover)) {
			LOGGER.debug(
				String.format(
					"%s.activated(\n\tActivated = {},\n\tHttpServletRequest = {}\n);//初始化密码时找不到情人",
					getClass().getName()
				),
				activated,
				request
			);
			return new JavaScriptObjectNotation().
				withReason("activated.cannotBeAuthenticated").
				withResponse(false).
				toJSONObject();
		}
		String shadow = activated.getShadow();

		lover.setShadow(
			passwordEncoder.encode(shadow)
		);
		Collection<Role> roles = new HashSet<>();
		roles.add(servant.getRole(Servant.ROLE_ADVENTURER));
		lover.setRoles(roles);
		lover = loverRepository.saveAndFlush(lover);

		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					String.format(
						"%s%s",
						lover.getCountry().getCallingCode(),
						lover.getLogin()
					),
					shadow
				)
			)
		);
		HttpSession session = request.getSession(true);
		session.setAttribute(
			SPRING_SECURITY_CONTEXT_KEY,
			securityContext
		);

		return new JavaScriptObjectNotation().
			withReason("activated.done").
			withRedirect("/me.asp").
			withResponse(true).
			toJSONObject();
	}

	@Transactional(readOnly = true)
	public Lover loadByIdentifier(UUID identifier) {
		return loverRepository.findOneByIdentifier(identifier);
	}

	@Transactional(readOnly = true)
	public Lover loadByUsername(String username) {
		User user = userRepository.findOneByUsername(username);
		if (Objects.isNull(user)) {
			return null;
		}
		return loverRepository.findById(user.getId()).orElseThrow();
	}

	@Transactional
	public JSONObject reactivate(String username, HttpServletRequest request) {
		Lover lover = loadByUsername(username);
		if (Objects.isNull(lover)) {
			LOGGER.debug(
				String.format(
					"%s.reactivate(\n\tString = {}\n);//重新激活时找不到用户",
					getClass().getName()
				),
				username
			);
			return new JavaScriptObjectNotation().
				withReason("reactivate.cannotBeAuthenticated").
				withResponse(false).
				toJSONObject();
		}

		Activation activation = activationRepository.findById(
			lover.getId()
		).orElseThrow();
		if (Objects.isNull(activation.getString()) || Objects.nonNull(activation.getOccurred())) {
			return new JavaScriptObjectNotation().
				withReason("reactivate.bringOwlsToAthens").
				withResponse(false).
				toJSONObject();
		}

		String string = RandomStringUtils.randomAlphanumeric(8);
		while (activationRepository.countByString(string) > 0) {
			string = RandomStringUtils.randomAlphanumeric(8);
		}
		activation.setString(string);

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.HOUR_OF_DAY, 1);
		Date expiry = calendar.getTime();
		activation.setExpiry(expiry);

		activation = activationRepository.saveAndFlush(activation);
		PublishResult publishResult = AMAZON_SNS.publish(
			new PublishRequest().
				withMessage(String.format(
					"請造訪 https://%s/activation.asp 並輸入激活碼：%s。",
					request.getServerName(),
					activation.getString()
				)).
				withPhoneNumber(String.format(
					"+%s%s",
					lover.getCountry().getCallingCode(),
					lover.getLogin()
				))
		);

		return new JavaScriptObjectNotation().
			withReason("reactivate.done").
			withRedirect("/activation.asp").
			withResponse(true).
			withResult(publishResult).
			toJSONObject();
	}

	@Transactional
	public Lover saveLover() {
		UUID uuid = UUID.randomUUID();

		Lover lover = new Lover();
		lover.setCountry(
			countryRepository.findById((short) 1).orElseThrow()
		);
		lover.setIdentifier(uuid);
		lover.setLogin(uuid.toString().replaceAll("\\-", ""));
		lover = loverRepository.saveAndFlush(lover);

		LineUserProfile lineUserProfile = new LineUserProfile();
		lineUserProfile.setId(lover.getId());
		lineUserProfile.setLover(lover);
		lineUserProfile.setDisplayName(uuid.toString().replaceAll("\\-", ""));
		lineUserProfileRepository.saveAndFlush(lineUserProfile);

		return lover;
	}

	@Transactional
	public Lover signUp(SignUp signUp, HttpServletRequest request) {
		Country country = countryRepository.
			findById(signUp.getCountry()).
			orElseThrow();
		String login = signUp.getLogin();

		if (loverRepository.countByCountryAndLogin(country, login) > 0) {
			throw new RuntimeException("signUp.exists");
		}

		Lover lover = new Lover();
		lover.setIdentifier(UUID.randomUUID());
		lover.setCountry(country);
		lover.setLogin(login);
		lover = loverRepository.saveAndFlush(lover);

		applicationEventPublisher.publishEvent(new SignedUpEvent(
			lover,
			request.getServerName(),
			request.getLocale()
		));
		return lover;
	}

	@Transactional
	public Activation signedUp(Lover lover) {
		String string = RandomStringUtils.randomAlphanumeric(8);
		while (activationRepository.countByString(string) > 0) {
			string = RandomStringUtils.randomAlphanumeric(8);
		}

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.HOUR_OF_DAY, 1);
		Date expiry = calendar.getTime();

		return activationRepository.saveAndFlush(new Activation(
			lover.getId(),
			lover,
			string,
			expiry
		));
	}
}
