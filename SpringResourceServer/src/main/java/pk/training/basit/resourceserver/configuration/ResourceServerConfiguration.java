package pk.training.basit.resourceserver.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import pk.training.basit.resourceserver.converter.jwt.CustomJwtGrantedAuthoritiesConverter;

@EnableMethodSecurity(
	prePostEnabled = true
)
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class ResourceServerConfiguration {

	@Value("${jwk.set.uri}")
    private String jwkSetUri;
	
	@Bean
	public JwtDecoder jwtDecoder() {
    	return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}
   
    JwtAuthenticationConverter jwtAuthenticationConverter() {
    	CustomJwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new CustomJwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.securityMatcher("/messages/**")
		.authorizeHttpRequests(authorizeRequests -> authorizeRequests
	        .anyRequest().authenticated()
		)
		.oauth2ResourceServer(oauth2 -> oauth2
	        .jwt(jwt -> jwt.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter()))
		);
		
		return http.build();
	}
	
}
