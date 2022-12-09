package pk.training.basit.authorizationserver.oauth2.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import pk.training.basit.authorizationserver.oauth2.OAuth2Util;
import pk.training.basit.authorizationserver.oauth2.entity.OAuth2Client;
import pk.training.basit.authorizationserver.oauth2.entity.OAuth2ClientSetting;
import pk.training.basit.authorizationserver.oauth2.entity.OAuth2ClientTokenSetting;
import pk.training.basit.authorizationserver.oauth2.repository.OAuth2ClientRepository;
import pk.training.basit.authorizationserver.oauth2.service.OAuth2ClientSettingsService;
import pk.training.basit.authorizationserver.oauth2.service.OAuth2RegisteredClientService;
import pk.training.basit.authorizationserver.oauth2.service.OAuth2TokenSettingsService;

@Service
public class OAuth2RegisteredClientServiceImpl implements OAuth2RegisteredClientService {

	private final OAuth2TokenSettingsService oauth2TokenSettingsService;
	private final OAuth2ClientSettingsService oauth2ClientSettingsService;
	private final OAuth2ClientRepository oauth2ClientRepository;
	
	
	public OAuth2RegisteredClientServiceImpl(OAuth2TokenSettingsService oauth2TokenSettingsService, 
			OAuth2ClientSettingsService oauth2ClientSettingsService,
			OAuth2ClientRepository oauth2ClientRepository) {
		this.oauth2TokenSettingsService = oauth2TokenSettingsService;
		this.oauth2ClientSettingsService = oauth2ClientSettingsService;
		this.oauth2ClientRepository = oauth2ClientRepository;
	}

	@Override
	public List<RegisteredClient> getOAuth2RegisteredClient() {
		
		List<RegisteredClient> registeredClients = Collections.emptyList();
		List<OAuth2Client> registeredOAuth2Clients = Collections.emptyList();
		List<OAuth2Client> oauth2Clients = oauth2ClientRepository.findByRegisteredFalse();
		if (!CollectionUtils.isEmpty(oauth2Clients)) {
			registeredClients = new ArrayList<>();
			registeredOAuth2Clients = new ArrayList<>();
			for (OAuth2Client client : oauth2Clients) {	
				
				RegisteredClient registeredClient = getRegisteredClient(client);
				registeredClients.add(registeredClient);
				
				client.setRegistered(true);
				registeredOAuth2Clients.add(client);
				
			}
		}
		
		if (!CollectionUtils.isEmpty(registeredOAuth2Clients)) {
			oauth2ClientRepository.saveAll(registeredOAuth2Clients);
		}
		
		return registeredClients;
	}
	
	private RegisteredClient getRegisteredClient(OAuth2Client client) {
		
		OAuth2ClientTokenSetting clientTokenSetting = client.getTokenSetting();
		TokenSettings tokenSettings = oauth2TokenSettingsService.getTokenSettings(clientTokenSetting);
		
		OAuth2ClientSetting clientSetting = client.getClientSetting();
		ClientSettings clientSettings = oauth2ClientSettingsService.getClientSettings(clientSetting);
		
		Set<ClientAuthenticationMethod> clientAuthenticationMethods = convertClientAuthenticationMethods(client.getClientAuthenticationMethods());
		Set<AuthorizationGrantType> authorizationGrantTypes = convertAuthorizationGrantTypes(client.getAuthorizationGrantTypes());
		Set<String> redirectUris = StringUtils.commaDelimitedListToSet(client.getRedirectUris());
		Set<String> scopes = StringUtils.commaDelimitedListToSet(client.getScopes());
		
		Long id = client.getId();
		RegisteredClient.Builder registeredClientBuilder = RegisteredClient.withId(String.valueOf(id))
				.clientId(client.getClientId())
				.clientIdIssuedAt(client.getClientIdIssuedAt())
				.clientName(client.getClientName())
				.clientSecret(client.getClientSecret())
				.clientSecretExpiresAt(client.getClientSecretExpiresAt())
				.tokenSettings(tokenSettings)
				.clientAuthenticationMethods(set -> set.addAll(clientAuthenticationMethods))
				.authorizationGrantTypes(set -> set.addAll(authorizationGrantTypes))
				.redirectUris(set -> set.addAll(redirectUris))
				.scopes(set -> set.addAll(scopes))
				.clientSettings(clientSettings);
		
		RegisteredClient registeredClient = registeredClientBuilder.build();
		return registeredClient;
		
	}
	
	private Set<AuthorizationGrantType> convertAuthorizationGrantTypes(String authorizationGrantType) {
		
		Set<AuthorizationGrantType> authorizationGrantTypes = Stream.of(authorizationGrantType.split(",", -1))
				.map(type -> OAuth2Util.resolveAuthorizationGrantType(type))
				.collect(Collectors.toSet());
		
		return authorizationGrantTypes;
		
	}
	
	private Set<ClientAuthenticationMethod> convertClientAuthenticationMethods(String clientAuthenticationMethod) {
		
		Set<ClientAuthenticationMethod> clientAuthenticationMethods = Stream.of(clientAuthenticationMethod.split(",", -1))
				.map(type -> OAuth2Util.resolveClientAuthenticationMethod(type))
				.collect(Collectors.toSet());
		
		return clientAuthenticationMethods;
		
	}
	
}
