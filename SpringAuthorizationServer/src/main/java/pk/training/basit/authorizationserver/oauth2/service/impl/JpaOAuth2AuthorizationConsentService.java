package pk.training.basit.authorizationserver.oauth2.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import pk.training.basit.authorizationserver.oauth2.entity.OAuth2AuthorizationConsentEntity;
import pk.training.basit.authorizationserver.oauth2.repository.OAuth2AuthorizationConsentRepository;

//This is registered as spring bean in AuthorizationServerConfiguration. No need to use @Service
public class JpaOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

	private final OAuth2AuthorizationConsentRepository oauth2AuthorizationConsentRepository;
	private final RegisteredClientRepository registeredClientRepository;

	public JpaOAuth2AuthorizationConsentService(
			OAuth2AuthorizationConsentRepository oauth2AuthorizationConsentRepository,
			RegisteredClientRepository registeredClientRepository) {
		Assert.notNull(oauth2AuthorizationConsentRepository, "oauth2AuthorizationConsentRepository cannot be null");
		Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
		this.oauth2AuthorizationConsentRepository = oauth2AuthorizationConsentRepository;
		this.registeredClientRepository = registeredClientRepository;
	}
	
	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		this.oauth2AuthorizationConsentRepository.save(toEntity(authorizationConsent));
	}

	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		this.oauth2AuthorizationConsentRepository.deleteByRegisteredClientIdAndPrincipalName(
				authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
	}

	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
		return this.oauth2AuthorizationConsentRepository.findByRegisteredClientIdAndPrincipalName(
				registeredClientId, principalName).map(this::toObject).orElse(null);
	}

	private OAuth2AuthorizationConsent toObject(OAuth2AuthorizationConsentEntity authorizationConsent) {
		String registeredClientId = authorizationConsent.getRegisteredClientId();
		RegisteredClient registeredClient = this.registeredClientRepository.findById(registeredClientId);
		if (registeredClient == null) {
			throw new DataRetrievalFailureException("The RegisteredClient with id '" + registeredClientId
					+ "' was not found in the RegisteredClientRepository.");
		}

		OAuth2AuthorizationConsent.Builder builder = OAuth2AuthorizationConsent.withId(registeredClientId,
				authorizationConsent.getPrincipalName());
		if (authorizationConsent.getAuthorities() != null) {
			for (String authority : StringUtils.commaDelimitedListToSet(authorizationConsent.getAuthorities())) {
				builder.authority(new SimpleGrantedAuthority(authority));
			}
		}

		return builder.build();
	}

	private OAuth2AuthorizationConsentEntity toEntity(OAuth2AuthorizationConsent authorizationConsent) {
		OAuth2AuthorizationConsentEntity entity = new OAuth2AuthorizationConsentEntity();
		entity.setRegisteredClientId(authorizationConsent.getRegisteredClientId());
		entity.setPrincipalName(authorizationConsent.getPrincipalName());

		Set<String> authorities = new HashSet<>();
		for (GrantedAuthority authority : authorizationConsent.getAuthorities()) {
			authorities.add(authority.getAuthority());
		}
		entity.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorities));

		return entity;
	}

}
