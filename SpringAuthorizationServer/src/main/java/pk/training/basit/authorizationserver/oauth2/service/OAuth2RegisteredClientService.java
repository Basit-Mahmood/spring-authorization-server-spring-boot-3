package pk.training.basit.authorizationserver.oauth2.service;

import java.util.List;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

public interface OAuth2RegisteredClientService {

	List<RegisteredClient> getOAuth2RegisteredClient();
	
}
