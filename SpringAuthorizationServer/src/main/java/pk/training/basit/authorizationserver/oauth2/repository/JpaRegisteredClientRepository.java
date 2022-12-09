package pk.training.basit.authorizationserver.oauth2.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

import pk.training.basit.authorizationserver.oauth2.OAuth2Util;
import pk.training.basit.authorizationserver.oauth2.entity.OAuth2RegisteredClient;

// This is registered as spring bean in OAuth2RegisteredClientConfiguration. No need to use @component
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

	private final OAuth2RegisteredClientRepository oauth2RegisteredClientRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public JpaRegisteredClientRepository(OAuth2RegisteredClientRepository oauth2RegisteredClientRepository) {
		Assert.notNull(oauth2RegisteredClientRepository, "oauth2RegisteredClientRepository cannot be null");
		this.oauth2RegisteredClientRepository = oauth2RegisteredClientRepository;

		ClassLoader classLoader = JpaRegisteredClientRepository.class.getClassLoader();
		List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
		this.objectMapper.registerModules(securityModules);
		this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
	}

	@Override
	public void save(RegisteredClient registeredClient) {
		Assert.notNull(registeredClient, "registeredClient cannot be null");
		this.oauth2RegisteredClientRepository.save(toEntity(registeredClient));
	}

	@Override
	public RegisteredClient findById(String id) {
		Assert.hasText(id, "id cannot be empty");
		return this.oauth2RegisteredClientRepository.findById(id).map(this::toObject).orElse(null);
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		Assert.hasText(clientId, "clientId cannot be empty");
		return this.oauth2RegisteredClientRepository.findByClientId(clientId).map(this::toObject).orElse(null);
	}

	private RegisteredClient toObject(OAuth2RegisteredClient oauth2RegisteredClient) {
		Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(
				oauth2RegisteredClient.getClientAuthenticationMethods());
		Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(
				oauth2RegisteredClient.getAuthorizationGrantTypes());
		Set<String> redirectUris = StringUtils.commaDelimitedListToSet(
				oauth2RegisteredClient.getRedirectUris());
		Set<String> clientScopes = StringUtils.commaDelimitedListToSet(
				oauth2RegisteredClient.getScopes());

		RegisteredClient.Builder builder = RegisteredClient.withId(oauth2RegisteredClient.getId())
				.clientId(oauth2RegisteredClient.getClientId())
				.clientIdIssuedAt(oauth2RegisteredClient.getClientIdIssuedAt())
				.clientSecret(oauth2RegisteredClient.getClientSecret())
				.clientSecretExpiresAt(oauth2RegisteredClient.getClientSecretExpiresAt())
				.clientName(oauth2RegisteredClient.getClientName())
				.clientAuthenticationMethods(authenticationMethods ->
						clientAuthenticationMethods.forEach(authenticationMethod ->
								authenticationMethods.add(OAuth2Util.resolveClientAuthenticationMethod(authenticationMethod))))
				.authorizationGrantTypes((grantTypes) ->
						authorizationGrantTypes.forEach(grantType ->
								grantTypes.add(OAuth2Util.resolveAuthorizationGrantType(grantType))))
				.redirectUris((uris) -> uris.addAll(redirectUris))
				.scopes((scopes) -> scopes.addAll(clientScopes));

		Map<String, Object> clientSettingsMap = parseMap(oauth2RegisteredClient.getClientSettings());
		builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

		Map<String, Object> tokenSettingsMap = parseMap(oauth2RegisteredClient.getTokenSettings());
		builder.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build());

		return builder.build();
	}

	private OAuth2RegisteredClient toEntity(RegisteredClient registeredClient) {
		List<String> clientAuthenticationMethods = new ArrayList<>(registeredClient.getClientAuthenticationMethods().size());
		registeredClient.getClientAuthenticationMethods().forEach(clientAuthenticationMethod ->
				clientAuthenticationMethods.add(clientAuthenticationMethod.getValue()));

		List<String> authorizationGrantTypes = new ArrayList<>(registeredClient.getAuthorizationGrantTypes().size());
		registeredClient.getAuthorizationGrantTypes().forEach(authorizationGrantType ->
				authorizationGrantTypes.add(authorizationGrantType.getValue()));

		OAuth2RegisteredClient oauth2RegisteredClient = new OAuth2RegisteredClient();
		oauth2RegisteredClient.setId(registeredClient.getId());
		oauth2RegisteredClient.setClientId(registeredClient.getClientId());
		oauth2RegisteredClient.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
		oauth2RegisteredClient.setClientSecret(registeredClient.getClientSecret());
		oauth2RegisteredClient.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
		oauth2RegisteredClient.setClientName(registeredClient.getClientName());
		oauth2RegisteredClient.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
		oauth2RegisteredClient.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
		oauth2RegisteredClient.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
		oauth2RegisteredClient.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
		oauth2RegisteredClient.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
		oauth2RegisteredClient.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));

		return oauth2RegisteredClient;
	}

	private Map<String, Object> parseMap(String data) {
		try {
			return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

	private String writeMap(Map<String, Object> data) {
		try {
			return this.objectMapper.writeValueAsString(data);
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

}
