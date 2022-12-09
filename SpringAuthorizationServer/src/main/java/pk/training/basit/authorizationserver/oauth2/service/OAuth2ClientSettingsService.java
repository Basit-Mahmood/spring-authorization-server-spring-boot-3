package pk.training.basit.authorizationserver.oauth2.service;

import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import pk.training.basit.authorizationserver.oauth2.entity.OAuth2ClientSetting;

public interface OAuth2ClientSettingsService {

	ClientSettings getClientSettings(OAuth2ClientSetting clientSetting);
	
}
