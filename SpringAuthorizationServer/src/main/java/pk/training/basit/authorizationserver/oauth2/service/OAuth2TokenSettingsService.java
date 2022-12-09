package pk.training.basit.authorizationserver.oauth2.service;

import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import pk.training.basit.authorizationserver.oauth2.entity.OAuth2ClientTokenSetting;

public interface OAuth2TokenSettingsService {

	TokenSettings getTokenSettings(OAuth2ClientTokenSetting clientTokenSetting);
	
}
