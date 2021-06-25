package tw.musemodel.dingzhiqingren.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
	private MessageSource messageSource;

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

	/**
	 * 激活
	 *
	 * @param inputString 激活码
	 * @param locale 语言环境
	 * @return 杰森对象
	 */
	@Transactional
	public JSONObject activate(String inputString, Locale locale) {
		Activation activation = activationRepository.
			findOneByString(inputString).
			orElseThrow();
		if (Objects.isNull(activation.getString()) || Objects.nonNull(activation.getOccurred())) {
			throw new RuntimeException("activate.activated");
		}

		activation.setString(null);
		activation.setOccurred(new Date(System.currentTimeMillis()));
		activation = activationRepository.saveAndFlush(activation);
		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"activate.done",
				null,
				locale
			)).
			withRedirect(String.format(
				"/activated.asp?id=%s",
				activation.getLover().getIdentifier().toString()
			)).
			withResponse(true).
			toJSONObject();
	}

	@Transactional
	public JSONObject activated(Activated activated, HttpServletRequest request, Locale locale) {
		Lover lover = loadByIdentifier(activated.getIdentifier());
		if (Objects.isNull(lover)) {
			LOGGER.debug("初始化密码时找不到情人");
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"activated.mustntBeAuthenticated",
					null,
					locale
				)).
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
			withReason(messageSource.getMessage(
				"activated.done",
				null,
				locale
			)).
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
	public JSONObject reactivate(String username, HttpServletRequest request, Locale locale) {
		Lover lover = loadByUsername(username);
		if (Objects.isNull(lover)) {
			LOGGER.debug("重新激活时找不到情人");
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"reactivate.notFound",
					null,
					locale
				)).
				withResponse(false).
				toJSONObject();
		}

		Activation activation = activationRepository.findById(
			lover.getId()
		).orElseThrow();
		if (Objects.isNull(activation.getString()) || Objects.nonNull(activation.getOccurred())) {
			return new JavaScriptObjectNotation().
				withReason(messageSource.getMessage(
					"reactivate.bringOwlsToAthens",
					null,
					locale
				)).
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
				withMessage(messageSource.getMessage(
					"reactivate.bringOwlsToAthens",
					new String[]{
						activation.getString(),
						String.format(
							"https://%s/activate.asp",
							request.getServerName()
						)
					},
					locale
				)).
				withPhoneNumber(String.format(
					"+%s%s",
					lover.getCountry().getCallingCode(),
					lover.getLogin()
				))
		);

		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"reactivate.done",
				new String[]{
					activation.getString(),
					String.format(
						"https://%s/activate.asp",
						request.getServerName()
					)
				},
				locale
			)).
			withRedirect("/activate.asp").
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
	public JSONObject signUp(SignUp signUp, HttpServletRequest request, Locale locale) {
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
		lover.setGender(signUp.getGender());
		lover = loverRepository.saveAndFlush(lover);

		applicationEventPublisher.publishEvent(new SignedUpEvent(
			lover,
			request.getServerName(),
			request.getLocale()
		));
		return new JavaScriptObjectNotation().
			withReason(messageSource.getMessage(
				"signUp.done",
				null,
				locale
			)).
			withRedirect("/activate.asp").
			withResponse(true).
			toJSONObject();
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

	public Element loverElement(Element loverElement, Lover lover, Locale locale) {

		if (!Objects.isNull(lover.getLocation())) {
			loverElement.setAttribute(
				"location",
				messageSource.getMessage(
					lover.getLocation().getCity(),
					null,
					locale
				));
		}

		if (!Objects.isNull(lover.getNickname())) {
			loverElement.setAttribute(
				"nickname",
				lover.getNickname()
			);
		}

		if (!Objects.isNull(lover.getBirthday())) {
			loverElement.setAttribute(
				"age",
				servant.getAgeByBirth(
					lover.getBirthday()).toString()
			);
			loverElement.setAttribute(
				"birthday",
				DateTimeFormatter.ofPattern("yyyy-MM-dd").format(
					servant.toTaipeiZonedDateTime(
						lover.getBirthday()
					).withZoneSameInstant(Servant.ZONE_ID_TAIPEI)
				)
			);
		}

		if (!Objects.isNull(lover.getGender())) {
			loverElement.setAttribute(
				"gender",
				lover.getGender() ? messageSource.getMessage(
				"gender.male",
				null,
				locale
			) : messageSource.getMessage(
				"gender.female",
				null,
				locale
			));
		}

		if (!Objects.isNull(lover.getPhoto())) {
			loverElement.setAttribute(
				"photo",
				lover.getPhoto()
			);
		}

		if (!Objects.isNull(lover.getIntroduction())) {
			String introduction = lover.getIntroduction();
			loverElement.setAttribute(
				"intro",
				introduction
			);
		}

		if (!Objects.isNull(lover.getBodyType())) {
			loverElement.setAttribute(
				"bodyType",
				messageSource.getMessage(
					lover.getBodyType().toString(),
					null,
					locale
				));
		}

		if (!Objects.isNull(lover.getHeight())) {
			loverElement.setAttribute(
				"height",
				lover.getHeight().toString()
			);
		}

		if (!Objects.isNull(lover.getWeight())) {
			loverElement.setAttribute(
				"weight",
				lover.getWeight().toString()
			);
		}

		if (!Objects.isNull(lover.getEducation())) {
			loverElement.setAttribute(
				"education",
				messageSource.getMessage(
					lover.getEducation().toString(),
					null,
					locale
				));
		}

		if (!Objects.isNull(lover.getMarriage())) {
			loverElement.setAttribute(
				"marriage",
				messageSource.getMessage(
					lover.getMarriage().toString(),
					null,
					locale
				));
		}

		if (!Objects.isNull(lover.getOccupation())) {
			loverElement.setAttribute(
				"occupation",
				lover.getOccupation()
			);
		}

		if (!Objects.isNull(lover.getSmoking())) {
			loverElement.setAttribute(
				"smoking",
				messageSource.getMessage(
					lover.getSmoking().toString(),
					null,
					locale
				));
		}

		if (!Objects.isNull(lover.getDrinking())) {
			loverElement.setAttribute(
				"drinking",
				messageSource.getMessage(
					lover.getDrinking().toString(),
					null,
					locale
				));
		}

		if (!Objects.isNull(lover.getIdealType())) {
			loverElement.setAttribute(
				"idealType",
				lover.getIdealType()
			);
		}

		if (!Objects.isNull(lover.getLineID())) {
			loverElement.setAttribute(
				"lineLink",
				lover.getLineID()
			);
		}

		if (!Objects.isNull(lover.getHello())) {
			loverElement.setAttribute(
				"hello",
				lover.getHello()
			);
		}

		if (!Objects.isNull(lover.getActive())) {
			loverElement.setAttribute(
				"active",
				Servant.ZHONG_HUA_MIN_ZU.format(
					servant.toTaipeiZonedDateTime(
						lover.getActive()
					).withZoneSameInstant(Servant.ZONE_ID_TAIPEI)
				).replaceAll("\\+\\d{2}$", "")
			);
		}

		return loverElement;
	}
}
