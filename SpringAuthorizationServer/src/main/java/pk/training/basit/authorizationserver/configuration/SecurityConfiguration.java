package pk.training.basit.authorizationserver.configuration;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

	@Autowired 
	private UserPrincipalService userPrincipalService;
	
	// If no passwordEncoder bean is defined then you have to prefix password like {noop}secret1, or {bcrypt}password
	// if not static spring boot 2.6.x gives bean currently in creation error at line .passwordEncoder(passwordEncoder()) in configureGlobal() method
	/**
	@Bean
    public static PasswordEncoder passwordEncoder() {		
		LOGGER.debug("in passwordEncoder");
        return new BCryptPasswordEncoder();
    };
    */
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
		LOGGER.debug("in configureGlobal");
		 builder
		 	.eraseCredentials(true)
            .userDetailsService(this.userPrincipalService);
	}
	
	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}
	
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
		LOGGER.debug("in configure HttpSecurity");
		
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
			.requestMatchers(antMatcher("/h2-console/**")).permitAll()
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
		    .ignoringRequestMatchers(antMatcher("/h2-console/**"))
         )
		.headers(headers -> headers
			.frameOptions(options -> options.sameOrigin())
		)
		.apply(federatedIdentityConfigurer);
		
		return http.build();
	}
	
}
