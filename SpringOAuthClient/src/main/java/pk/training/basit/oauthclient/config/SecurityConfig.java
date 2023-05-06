package pk.training.basit.oauthclient.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	    auth.eraseCredentials(false);
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/webjars/**").permitAll()
				.anyRequest().authenticated())
			.formLogin(form -> form.loginPage("/login").failureUrl("/login-error").permitAll())
			.oauth2Client(withDefaults());
		
		return http.build();
	}
	
	@Bean
	UserDetailsService users() {
		
		UserDetails user = User.withDefaultPasswordEncoder()
				.username("user1")
				.password("password")
				.authorities("USER")
				.build();
		
		return new InMemoryUserDetailsManager(user);
	}
	
}
