package pk.training.basit.authorizationserver.oauth2.entity;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * oauth2_registered_client is a table define internally by spring. The location of this table can be find in database.properties 
 * file.
 * 
 * @author basit
 */
@Entity
@Table(name = "oauth2_registered_client")
public class OAuth2RegisteredClient implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "client_id")
	private String clientId;

	@Column(name = "client_id_issued_at")
	private Instant clientIdIssuedAt;

	@Column(name = "client_secret")
	private String clientSecret;

	@Column(name = "client_secret_expires_at")
	private Instant clientSecretExpiresAt;

	@Column(name = "client_name")
	private String clientName;

	@Column(name = "client_authentication_methods", length = 1000)
	private String clientAuthenticationMethods;

	@Column(name = "authorization_grant_types", length = 1000)
	private String authorizationGrantTypes;

	@Column(name = "redirect_uris", length = 1000)
	private String redirectUris;

	@Column(length = 1000)
	private String scopes;

	@Column(name = "client_settings", length = 2000)
	private String clientSettings;

	@Column(name = "token_settings", length = 2000)
	private String tokenSettings;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Instant getClientIdIssuedAt() {
		return clientIdIssuedAt;
	}

	public void setClientIdIssuedAt(Instant clientIdIssuedAt) {
		this.clientIdIssuedAt = clientIdIssuedAt;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public Instant getClientSecretExpiresAt() {
		return clientSecretExpiresAt;
	}

	public void setClientSecretExpiresAt(Instant clientSecretExpiresAt) {
		this.clientSecretExpiresAt = clientSecretExpiresAt;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientAuthenticationMethods() {
		return clientAuthenticationMethods;
	}

	public void setClientAuthenticationMethods(String clientAuthenticationMethods) {
		this.clientAuthenticationMethods = clientAuthenticationMethods;
	}

	public String getAuthorizationGrantTypes() {
		return authorizationGrantTypes;
	}

	public void setAuthorizationGrantTypes(String authorizationGrantTypes) {
		this.authorizationGrantTypes = authorizationGrantTypes;
	}

	public String getRedirectUris() {
		return redirectUris;
	}

	public void setRedirectUris(String redirectUris) {
		this.redirectUris = redirectUris;
	}

	public String getScopes() {
		return scopes;
	}

	public void setScopes(String scopes) {
		this.scopes = scopes;
	}

	public String getClientSettings() {
		return clientSettings;
	}

	public void setClientSettings(String clientSettings) {
		this.clientSettings = clientSettings;
	}

	public String getTokenSettings() {
		return tokenSettings;
	}

	public void setTokenSettings(String tokenSettings) {
		this.tokenSettings = tokenSettings;
	}

}
