package pk.training.basit.authorizationserver.oauth2.service.impl;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import pk.training.basit.authorizationserver.oauth2.entity.OAuth2ClientTokenSetting;
import pk.training.basit.authorizationserver.oauth2.service.OAuth2TokenSettingsService;

@Service
public class OAuth2TokenSettingsServiceImpl implements OAuth2TokenSettingsService {

	@Override
	public TokenSettings getTokenSettings(OAuth2ClientTokenSetting clientTokenSetting) {
		
		final int accessTokenTime = clientTokenSetting.getAccessTokenTime();
		final String accessTokenTimeUnit = clientTokenSetting.getAccessTokenTimeUnit();
		final int refreshTokenTime = clientTokenSetting.getRefreshTokenTime();
		final String refreshTokenTimeUnit = clientTokenSetting.getRefreshTokenTimeUnit();
		
		Duration accessTokenDuration = setTokenTime(accessTokenTimeUnit, accessTokenTime, 5);
		Duration refreshTokenDuration = setTokenTime(refreshTokenTimeUnit, refreshTokenTime, 60);

		TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder().accessTokenTimeToLive(accessTokenDuration)
				.refreshTokenTimeToLive(refreshTokenDuration);
		TokenSettings tokenSetting = tokenSettingsBuilder.build();
		return tokenSetting;
		
	}
	
	private Duration setTokenTime(String tokenTimeUnit, long tokenTime, long durationInMinutes) {

		Duration duration = Duration.ofMinutes(durationInMinutes);

		if (StringUtils.hasText(tokenTimeUnit)) {

			switch (tokenTimeUnit.toUpperCase()) {
			case "M":
			case "MINUTE":
			case "MINUTES":
				duration = Duration.ofMinutes(tokenTime);
				break;
			case "H":
			case "HOUR":
			case "HOURS":
				duration = Duration.ofHours(tokenTime);
				break;
			case "D":
			case "DAY":
			case "DAYS":
				duration = Duration.ofDays(tokenTime);
				break;
			case "W":
			case "WEEK":
			case "WEEKS":
				duration = Duration.of(tokenTime, ChronoUnit.WEEKS);
				break;
			}
		}

		return duration;
	}
	
}
