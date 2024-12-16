package pk.training.basit.authorizationserver.configuration;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import pk.training.basit.authorizationserver.configuration.federated.identity.FederatedIdentityConfigurer;
import pk.training.basit.authorizationserver.configuration.federated.identity.UserRepositoryOAuth2UserHandler;
import pk.training.basit.authorizationserver.service.UserPrincipalService;

@EnableMethodSecurity(
    prePostEnabled = true,  
    mode = AdviceMode.PROXY,
    proxyTargetClass = false
)
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfiguration {

	private static final Logger LOGGER = LogManager.getLogger(SecurityConfiguration.class);
	private static final String H2_CONSOLE_URL = "/h2-console/**";

	@Autowired 
	private UserPrincipalService userPrincipalService;
	
	// If no passwordEncoder bean is defined then you have to prefix password like {noop}secret1, or {bcrypt}password
	// if not static password encoder is define spring boot gives cyclic dependency error on UserserviceImpl bean as it is using PasswordEncoder.
	// If you remove static from method then while saving plain password will save in database
	/**
	 * As we are using prefixes in our test scripts for passwords in src/main/resources/database/scripts/user-principla-test-data.sql and 
	 * src/main/resources/database/scripts/oauth2-client-test-data.sql.
	 * 
	 * That's why we are not specifying specific password encoder. In user-principla-test-data.sql we are using {bcrypt} prefix. So delegating 
	 * password encoder will automatically validate it with BCrypt password encoder.
	 * 
	 * Similarly in oauth2-client-test-data.sql we are using {noop}. So delegating password encoder will automatically validate it with Plain 
	 * password encoder.
	 * 
	 * This is a sample project. Just for demonstration we are doing it in this way.
	 * 
	 */
	@Bean
    public static PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    };
    
	@Bean
	public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userPrincipalService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(daoAuthenticationProvider);
	}
	
	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}
	
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
		LOGGER.debug("in configure HttpSecurity");
		
		Customizer <FederatedIdentityConfigurer>  federatedIdentityConfigurerCustomizer = Customizer.withDefaults();
		FederatedIdentityConfigurer federatedIdentityConfigurer = new FederatedIdentityConfigurer().oauth2UserHandler(new UserRepositoryOAuth2UserHandler());
		
		/**
		 * To enable access to the H2 database console under Spring Security you need to change three things:
		 * 
		 * 		Allow all access to the url path /h2-console/*.
		 * 		Disable CRSF (Cross-Site Request Forgery). By default, Spring Security will protect against CRSF attacks.
		 * 		Since the H2 database console runs inside a frame, you need to enable this in in Spring Security.
		 * 
		 * The following Spring Security Configuration will:
		 * 
		 * 		Allow all requests to the root url ("/")
		 * 		Allow all requests to the H2 database console url ("/h2-console/*")
		 * 		Disable CSRF protection
		 * 		Disable X-Frame-Options in Spring Security
		 */
		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
			.requestMatchers(antMatcher("/")).permitAll()
			.requestMatchers(antMatcher(H2_CONSOLE_URL)).permitAll()
			.requestMatchers(mvc.pattern("/webjars/**")).permitAll()
			.requestMatchers(mvc.pattern("/image/**")).permitAll()
		    .anyRequest().authenticated()
		)
		.formLogin(form -> form
				.loginPage("/login")
				.failureUrl("/login-error")
				.permitAll()
		)
		.csrf(csrf -> csrf
		    .ignoringRequestMatchers(antMatcher(H2_CONSOLE_URL))
         )
		.headers(headers -> headers
			.frameOptions(options -> options.sameOrigin())
		)
		.with(federatedIdentityConfigurer, federatedIdentityConfigurerCustomizer);
		
		return http.build();
	}
	
}
