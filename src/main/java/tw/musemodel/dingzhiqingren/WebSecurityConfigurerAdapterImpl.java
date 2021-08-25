package tw.musemodel.dingzhiqingren;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tw.musemodel.dingzhiqingren.service.UserDetailsServiceImpl;

/**
 * @author p@musemodel.tw
 */
@Configuration
@EnableGlobalMethodSecurity(
	jsr250Enabled = true,
	prePostEnabled = true,
	securedEnabled = true
)
@EnableWebSecurity
public class WebSecurityConfigurerAdapterImpl extends WebSecurityConfigurerAdapter {

	private final static Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfigurerAdapterImpl.class);

	@Autowired
	DataSource dataSource;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	/**
	 * Override this method to configure the {@link HttpSecurity}. Typically
	 * subclasses should not invoke this method by calling super as it may
	 * override their configuration. The default configuration is:
	 *
	 * <pre>http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();</pre>
	 *
	 * Any endpoint that requires defense against common vulnerabilities can
	 * be specified here, including public ones. See
	 * {@link HttpSecurity#authorizeRequests} and the `permitAll()`
	 * authorization rule for more details on public endpoints.
	 *
	 * @param httpSecurity the {@link HttpSecurity} to modify
	 * @throws Exception if an error occurs
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.
			authorizeRequests(
				authorizeRequests -> authorizeRequests.
					/*
					 无须认证但需取得识别
					 */
					antMatchers(
						"/",
						"/activate.asp",
						"/activate.json",
						"/activated.asp",
						"/authentication.json",
						"/notify-bot.line.me/**",
						"/reactivate.asp",
						"/reactivate.json",
						"/resetPassword/**",
						"/signIn.asp",
						"/signUp.asp",
						"/*.png"
					).permitAll().
					anyRequest().authenticated()
			).
			formLogin(
				formLoginCustomizer -> {
					try {
						formLoginCustomizer.
							loginPage("/signIn.asp").
							loginProcessingUrl("/signIn.asp").
							failureUrl("/signIn.asp").
							and().
							logout(
								logoutCustomizer -> logoutCustomizer.
									logoutUrl("/signOut.asp").
									logoutSuccessUrl("/")
							);
					} catch (Exception exception) {
						LOGGER.debug(
							String.format(
								"%s#configure(\n\tHttpSecurity = {}\n);//任何需要防御常见漏洞的端点都可以在此处指定，包括公开漏洞。",
								getClass().getName()
							),
							httpSecurity,
							exception
						);
					}
				}
			).
			csrf().disable();
		LOGGER.debug(
			String.format(
				"%s#configure(\n\tHttpSecurity = {}\n);//任何需要防御常见漏洞的端点都可以在此处指定，包括公开漏洞。",
				getClass().getName()
			),
			httpSecurity
		);
	}

	/**
	 * Override this method to configure {@link WebSecurity}.For example, if
	 * you wish to ignore certain requests.Endpoints specified in this
	 * method will be ignored by Spring Security, meaning it will not
	 * protect them from CSRF, XSS, Clickjacking, and so on. Instead, if you
	 * want to protect endpoints against common vulnerabilities, then see
	 * {@link #configure(HttpSecurity)} and the
	 * {@link HttpSecurity#authorizeRequests} configuration method.
	 *
	 * @param webSecurity
	 * @throws Exception if an error occurs
	 */
	@Override
	public void configure(WebSecurity webSecurity) throws Exception {
		/*
		 无须认证且无需取得识别
		 */
		webSecurity.
			ignoring().
			antMatchers(
				"/*.html",
				"/IMAGE/**",
				"/SCRIPT/**",
				"/STYLE/**",
				"/error",
				"/inpay2/**",
				"/manifest.json",
				"/poc/**",
				"/privacy.asp",
				"/terms.asp",
				"/webhook/**",
				"/ICON/**"
			);
		LOGGER.debug(
			String.format(
				"%s#configure(\n\tWebSecurity = {}\n);//Spring Security 将忽略此方法中指定的端点。",
				getClass().getName()
			),
			webSecurity
		);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * Used by the default implementation of
	 * {@link #authenticationManager()} to attempt to obtain an
	 * {@link AuthenticationManager}. If overridden, the
	 * {@link AuthenticationManagerBuilder} should be used to specify the
	 * {@link AuthenticationManager}.
	 *
	 * <p>
	 * The {@link #authenticationManagerBean()} method can be used to expose
	 * the resulting {@link AuthenticationManager} as a Bean. The
	 * {@link #userDetailsServiceBean()} can be used to expose the last
	 * populated {@link UserDetailsService} that is created with the
	 * {@link AuthenticationManagerBuilder} as a Bean. The
	 * {@link UserDetailsService} will also automatically be populated on
	 * {@link HttpSecurity#getSharedObject(Class)} for use with other
	 * {@link SecurityContextConfigurer} (i.e. RememberMeConfigurer)</p>
	 *
	 * <p>
	 * For example, the following configuration could be used to register in
	 * memory authentication that exposes an in memory
	 * {@link UserDetailsService}:</p>
	 *
	 * <pre>&#064;Override
	 * protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
	 * 	authenticationManagerBuilder.
	 * 	// enable in memory based authentication with a user named
	 * 	// &quot;user&quot; and &quot;admin&quot;
	 * 	inMemoryAuthentication().
	 *		withUser(&quot;user&quot;).password(&quot;password&quot;).roles(&quot;USER&quot;).
	 *		and().
	 * 		withUser(&quot;admin&quot;).password(&quot;password&quot;).roles(&quot;USER&quot;, &quot;ADMIN&quot;);
	 * }
	 *
	 * // Expose the UserDetailsService as a Bean
	 * &#064;Bean
	 * &#064;Override
	 * public UserDetailsService userDetailsServiceBean() throws Exception {
	 * 	return super.userDetailsServiceBean();
	 * }</pre>
	 *
	 * @param authenticationManagerBuilder the
	 * {@link AuthenticationManagerBuilder} to use
	 * @throws Exception if an error occurs
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.
			jdbcAuthentication().
			dataSource(dataSource);
		LOGGER.debug(
			String.format(
				"%s#configure(\n\tAuthenticationManagerBuilder = {}\n);//由 authenticationManager() 的默认实现使用以尝试获取 AuthenticationManager；如果被覆写，则应使用 AuthenticationManagerBuilder 来指定 AuthenticationManager。",
				getClass().getName()
			),
			authenticationManagerBuilder
		);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
