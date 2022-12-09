package pk.training.basit.oauthclient.config.oauth2.client.impl;

import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;

@Component
public class ClientCredentialsOAuth2Client extends AbstractOAuth2Client {

	public ClientCredentialsOAuth2Client(Environment env) {
		super(env);
	}
	
	@Override
	public ClientRegistration getClientRegistration() {
		 return ClientRegistration.withRegistrationId("client-credentials-client-name")
            .clientId("client-credentials-client-id")
            .clientSecret("secret2")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .scope("message.read", "message.write")
            .authorizationUri(oauth2AuthorizationUri)
            .tokenUri(oauth2TokenUri)
            .build();
	}

}
