package pk.training.basit.authorizationserver.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

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
             .userDetailsService(this.userPrincipalService)
                // .passwordEncoder(passwordEncoder())
         .and()
             .eraseCredentials(true);
	}
	
	//@Bean
	//public WebSecurityCustomizer webSecurityCustomizer() {
	//	return (web) -> web.ignoring().requestMatchers("/webjars/**", "/image/**");
	//}
	
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		LOGGER.debug("in configure HttpSecurity");
		
		FederatedIdentityConfigurer federatedIdentityConfigurer = new FederatedIdentityConfigurer().oauth2UserHandler(new UserRepositoryOAuth2UserHandler());
		
		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers(EndpointRequest.toAnyEndpoint(),PathRequest.toH2Console()).permitAll()
			.requestMatchers("/webjars/**", "/image/**").permitAll()
		    .anyRequest().authenticated()
		)
		.formLogin(form -> form.loginPage("/login").failureUrl("/login-error").permitAll())
		.csrf().ignoringRequestMatchers(PathRequest.toH2Console())
		.and().headers().frameOptions().sameOrigin()
		.and()
		.apply(federatedIdentityConfigurer);
		
		return http.build();
	}
	
}
