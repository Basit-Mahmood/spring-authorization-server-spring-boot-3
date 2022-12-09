package pk.training.basit.authorizationserver.configuration;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import pk.training.basit.authorizationserver.oauth2.repository.JpaRegisteredClientRepository;
import pk.training.basit.authorizationserver.oauth2.repository.OAuth2RegisteredClientRepository;
import pk.training.basit.authorizationserver.oauth2.service.OAuth2RegisteredClientService;

@Configuration
public class OAuth2RegisteredClientConfiguration {

	private static final Logger LOGGER = LogManager.getLogger();
	
	private final OAuth2RegisteredClientService oauth2RegisteredClientService;
	
	public OAuth2RegisteredClientConfiguration(OAuth2RegisteredClientService oauth2RegisteredClientService) {
		this.oauth2RegisteredClientService = oauth2RegisteredClientService;
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository(OAuth2RegisteredClientRepository oauth2RegisteredClientRepository) {
		
		RegisteredClientRepository registeredClientRepository = new JpaRegisteredClientRepository(oauth2RegisteredClientRepository);
		
		LOGGER.debug("in registeredClientRepository");

		List<RegisteredClient> registeredClients = oauth2RegisteredClientService.getOAuth2RegisteredClient();
		registeredClients.forEach(registeredClient -> {
			registeredClientRepository.save(registeredClient);
		});
		
		return registeredClientRepository;
	}
	
}
