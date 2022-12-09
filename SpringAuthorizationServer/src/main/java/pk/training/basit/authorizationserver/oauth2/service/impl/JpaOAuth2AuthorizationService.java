package pk.training.basit.authorizationserver.oauth2.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

import pk.training.basit.authorizationserver.jackson2.mixin.AuditDeletedDateMixin;
import pk.training.basit.authorizationserver.jackson2.mixin.LongMixin;
import pk.training.basit.authorizationserver.jackson2.mixin.UserAuthorityMixin;
import pk.training.basit.authorizationserver.jackson2.mixin.UserPrincipalMixin;
import pk.training.basit.authorizationserver.jpa.audit.AuditDeletedDate;
import pk.training.basit.authorizationserver.jpa.entity.UserAuthority;
import pk.training.basit.authorizationserver.jpa.entity.UserPrincipal;
import pk.training.basit.authorizationserver.oauth2.OAuth2Util;
import pk.training.basit.authorizationserver.oauth2.entity.OAuth2AuthorizationEntity;
import pk.training.basit.authorizationserver.oauth2.repository.OAuth2AuthorizationRepository;

//This is registered as spring bean in AuthorizationServerConfiguration. No need to use @Service
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService {

	private final OAuth2AuthorizationRepository oauth2AuthorizationRepository;
	private final RegisteredClientRepository registeredClientRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public JpaOAuth2AuthorizationService(OAuth2AuthorizationRepository oauth2AuthorizationRepository,
			RegisteredClientRepository registeredClientRepository) {
		Assert.notNull(oauth2AuthorizationRepository, "authorizationRepository cannot be null");
		Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
		this.oauth2AuthorizationRepository = oauth2AuthorizationRepository;
		this.registeredClientRepository = registeredClientRepository;

		ClassLoader classLoader = JpaOAuth2AuthorizationService.class.getClassLoader();
		List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
		this.objectMapper.registerModules(securityModules);
		this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
		
		// You will need to write the Mixin for your class so Jackson can marshall it.
		this.objectMapper.addMixIn(UserAuthority.class, UserAuthorityMixin.class);
		this.objectMapper.addMixIn(UserPrincipal.class, UserPrincipalMixin.class);
		this.objectMapper.addMixIn(AuditDeletedDate.class, AuditDeletedDateMixin.class);
		this.objectMapper.addMixIn(Long.class, LongMixin.class);
		
	}

	@Override
	public void save(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		this.oauth2AuthorizationRepository.save(toEntity(authorization));
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		this.oauth2AuthorizationRepository.deleteById(authorization.getId());
	}

	@Override
	public OAuth2Authorization findById(String id) {
		Assert.hasText(id, "id cannot be empty");
		return this.oauth2AuthorizationRepository.findById(id).map(this::toObject).orElse(null);
	}
	
	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		Assert.hasText(token, "token cannot be empty");

		Optional<OAuth2AuthorizationEntity> result;
		if (tokenType == null) {
			result = this.oauth2AuthorizationRepository.findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(token);
		} else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
			result = this.oauth2AuthorizationRepository.findByState(token);
		} else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
			result = this.oauth2AuthorizationRepository.findByAuthorizationCodeValue(token);
		} else if (OAuth2ParameterNames.ACCESS_TOKEN.equals(tokenType.getValue())) {
			result = this.oauth2AuthorizationRepository.findByAccessTokenValue(token);
		} else if (OAuth2ParameterNames.REFRESH_TOKEN.equals(tokenType.getValue())) {
			result = this.oauth2AuthorizationRepository.findByRefreshTokenValue(token);
		} else {
			result = Optional.empty();
		}

		return result.map(this::toObject).orElse(null);
	}
	
	private OAuth2Authorization toObject(OAuth2AuthorizationEntity entity) {
		RegisteredClient registeredClient = this.registeredClientRepository.findById(entity.getRegisteredClientId());
		if (registeredClient == null) {
			throw new DataRetrievalFailureException(
					"The RegisteredClient with id '" + entity.getRegisteredClientId() + "' was not found in the RegisteredClientRepository.");
		}

		OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
				.id(entity.getId())
				.principalName(entity.getPrincipalName())
				.authorizationGrantType(OAuth2Util.resolveAuthorizationGrantType(entity.getAuthorizationGrantType()))
				.authorizedScopes(StringUtils.commaDelimitedListToSet(entity.getAuthorizedScopes()))
				.attributes(attributes -> attributes.putAll(parseMap(entity.getAttributes())));
		if (entity.getState() != null) {
			builder.attribute(OAuth2ParameterNames.STATE, entity.getState());
		}

		if (entity.getAuthorizationCodeValue() != null) {
			OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
					entity.getAuthorizationCodeValue(),
					entity.getAuthorizationCodeIssuedAt(),
					entity.getAuthorizationCodeExpiresAt());
			builder.token(authorizationCode, metadata -> metadata.putAll(parseMap(entity.getAuthorizationCodeMetadata())));
		}

		if (entity.getAccessTokenValue() != null) {
			OAuth2AccessToken accessToken = new OAuth2AccessToken(
					OAuth2AccessToken.TokenType.BEARER,
					entity.getAccessTokenValue(),
					entity.getAccessTokenIssuedAt(),
					entity.getAccessTokenExpiresAt(),
					StringUtils.commaDelimitedListToSet(entity.getAccessTokenScopes()));
			builder.token(accessToken, metadata -> metadata.putAll(parseMap(entity.getAccessTokenMetadata())));
		}

		if (entity.getRefreshTokenValue() != null) {
			OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
					entity.getRefreshTokenValue(),
					entity.getRefreshTokenIssuedAt(),
					entity.getRefreshTokenExpiresAt());
			builder.token(refreshToken, metadata -> metadata.putAll(parseMap(entity.getRefreshTokenMetadata())));
		}

		if (entity.getOidcIdTokenValue() != null) {
			OidcIdToken idToken = new OidcIdToken(
					entity.getOidcIdTokenValue(),
					entity.getOidcIdTokenIssuedAt(),
					entity.getOidcIdTokenExpiresAt(),
					parseMap(""));
			builder.token(idToken, metadata -> metadata.putAll(parseMap(entity.getOidcIdTokenMetadata())));
		}

		return builder.build();
	}
	
	private OAuth2AuthorizationEntity toEntity(OAuth2Authorization authorization) {
		OAuth2AuthorizationEntity entity = new OAuth2AuthorizationEntity();
		entity.setId(authorization.getId());
		entity.setRegisteredClientId(authorization.getRegisteredClientId());
		entity.setPrincipalName(authorization.getPrincipalName());
		entity.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
		entity.setAuthorizedScopes(StringUtils.collectionToDelimitedString(authorization.getAuthorizedScopes(), ","));
		entity.setAttributes(writeMap(authorization.getAttributes()));
		entity.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));

		OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
				.getToken(OAuth2AuthorizationCode.class);
		setTokenValues(authorizationCode, entity::setAuthorizationCodeValue, entity::setAuthorizationCodeIssuedAt,
				entity::setAuthorizationCodeExpiresAt, entity::setAuthorizationCodeMetadata);

		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
		setTokenValues(accessToken, entity::setAccessTokenValue, entity::setAccessTokenIssuedAt,
				entity::setAccessTokenExpiresAt, entity::setAccessTokenMetadata);
		if (accessToken != null && accessToken.getToken().getScopes() != null) {
			entity.setAccessTokenScopes(
					StringUtils.collectionToDelimitedString(accessToken.getToken().getScopes(), ","));
		}

		OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getToken(OAuth2RefreshToken.class);
		setTokenValues(refreshToken, entity::setRefreshTokenValue, entity::setRefreshTokenIssuedAt,
				entity::setRefreshTokenExpiresAt, entity::setRefreshTokenMetadata);

		OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
		setTokenValues(oidcIdToken, entity::setOidcIdTokenValue, entity::setOidcIdTokenIssuedAt,
				entity::setOidcIdTokenExpiresAt, entity::setOidcIdTokenMetadata);

		return entity;
	}

	private void setTokenValues(OAuth2Authorization.Token<?> token, Consumer<String> tokenValueConsumer,
			Consumer<Instant> issuedAtConsumer, Consumer<Instant> expiresAtConsumer,
			Consumer<String> metadataConsumer) {
		if (token != null) {
			OAuth2Token oAuth2Token = token.getToken();
			tokenValueConsumer.accept(oAuth2Token.getTokenValue());
			issuedAtConsumer.accept(oAuth2Token.getIssuedAt());
			expiresAtConsumer.accept(oAuth2Token.getExpiresAt());
			metadataConsumer.accept(writeMap(token.getMetadata()));
		}
	}

	private Map<String, Object> parseMap(String data) {
		try {
			return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

	private String writeMap(Map<String, Object> metadata) {
		try {
			return this.objectMapper.writeValueAsString(metadata);
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

}
