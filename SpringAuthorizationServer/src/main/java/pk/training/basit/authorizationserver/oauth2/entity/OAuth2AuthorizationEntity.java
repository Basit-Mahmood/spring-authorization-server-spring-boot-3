package pk.training.basit.authorizationserver.oauth2.entity;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Naming this entity OAuth2AuthorizationEntity instead of OAuth2Authorization so we can identify it with the 
 * OAuth2Authorization internal spring class. oauth2_authorization is a table define internally by spring. 
 * The location of this table can be find in database.properties file.
 * 
 * @author basit
 */
@Entity
@Table(name = "oauth2_authorization")
public class OAuth2AuthorizationEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "registered_client_id")
	private String registeredClientId;

	@Column(name = "principal_name")
	private String principalName;

	@Column(name = "authorization_grant_type")
	private String authorizationGrantType;

	@Column(name = "authorized_scopes", length = 1000)
	private String authorizedScopes;

	@Column(length = 4000)
	private String attributes;

	@Column(length = 500)
	private String state;

	@Column(name = "authorization_code_value", length = 4000)
	private String authorizationCodeValue;

	@Column(name = "authorization_code_issued_at")
	private Instant authorizationCodeIssuedAt;

	@Column(name = "authorization_code_expires_at")
	private Instant authorizationCodeExpiresAt;

	@Column(name = "authorization_code_metadata")
	private String authorizationCodeMetadata;

	@Column(name = "access_token_value", length = 4000)
	private String accessTokenValue;

	@Column(name = "access_token_issued_at")
	private Instant accessTokenIssuedAt;

	@Column(name = "access_token_expires_at")
	private Instant accessTokenExpiresAt;

	@Column(name = "access_token_metadata", length = 2000)
	private String accessTokenMetadata;

	@Column(name = "access_token_type")
	private String accessTokenType;

	@Column(name = "access_token_scopes", length = 1000)
	private String accessTokenScopes;

	@Column(name = "refresh_token_value", length = 4000)
	private String refreshTokenValue;

	@Column(name = "refresh_token_issued_at")
	private Instant refreshTokenIssuedAt;

	@Column(name = "refresh_token_expires_at")
	private Instant refreshTokenExpiresAt;

	@Column(name = "refresh_token_metadata", length = 2000)
	private String refreshTokenMetadata;

	@Column(name = "oidc_id_token_value", length = 4000)
	private String oidcIdTokenValue;

	@Column(name = "oidc_id_token_issued_at")
	private Instant oidcIdTokenIssuedAt;

	@Column(name = "oidc_id_token_expires_at")
	private Instant oidcIdTokenExpiresAt;

	@Column(name = "oidc_id_token_metadata", length = 2000)
	private String oidcIdTokenMetadata;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegisteredClientId() {
		return registeredClientId;
	}

	public void setRegisteredClientId(String registeredClientId) {
		this.registeredClientId = registeredClientId;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getAuthorizationGrantType() {
		return authorizationGrantType;
	}

	public void setAuthorizationGrantType(String authorizationGrantType) {
		this.authorizationGrantType = authorizationGrantType;
	}

	public String getAuthorizedScopes() {
		return authorizedScopes;
	}

	public void setAuthorizedScopes(String authorizedScopes) {
		this.authorizedScopes = authorizedScopes;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAuthorizationCodeValue() {
		return authorizationCodeValue;
	}

	public void setAuthorizationCodeValue(String authorizationCodeValue) {
		this.authorizationCodeValue = authorizationCodeValue;
	}

	public Instant getAuthorizationCodeIssuedAt() {
		return authorizationCodeIssuedAt;
	}

	public void setAuthorizationCodeIssuedAt(Instant authorizationCodeIssuedAt) {
		this.authorizationCodeIssuedAt = authorizationCodeIssuedAt;
	}

	public Instant getAuthorizationCodeExpiresAt() {
		return authorizationCodeExpiresAt;
	}

	public void setAuthorizationCodeExpiresAt(Instant authorizationCodeExpiresAt) {
		this.authorizationCodeExpiresAt = authorizationCodeExpiresAt;
	}

	public String getAuthorizationCodeMetadata() {
		return authorizationCodeMetadata;
	}

	public void setAuthorizationCodeMetadata(String authorizationCodeMetadata) {
		this.authorizationCodeMetadata = authorizationCodeMetadata;
	}

	public String getAccessTokenValue() {
		return accessTokenValue;
	}

	public void setAccessTokenValue(String accessTokenValue) {
		this.accessTokenValue = accessTokenValue;
	}

	public Instant getAccessTokenIssuedAt() {
		return accessTokenIssuedAt;
	}

	public void setAccessTokenIssuedAt(Instant accessTokenIssuedAt) {
		this.accessTokenIssuedAt = accessTokenIssuedAt;
	}

	public Instant getAccessTokenExpiresAt() {
		return accessTokenExpiresAt;
	}

	public void setAccessTokenExpiresAt(Instant accessTokenExpiresAt) {
		this.accessTokenExpiresAt = accessTokenExpiresAt;
	}

	public String getAccessTokenMetadata() {
		return accessTokenMetadata;
	}

	public void setAccessTokenMetadata(String accessTokenMetadata) {
		this.accessTokenMetadata = accessTokenMetadata;
	}

	public String getAccessTokenType() {
		return accessTokenType;
	}

	public void setAccessTokenType(String accessTokenType) {
		this.accessTokenType = accessTokenType;
	}

	public String getAccessTokenScopes() {
		return accessTokenScopes;
	}

	public void setAccessTokenScopes(String accessTokenScopes) {
		this.accessTokenScopes = accessTokenScopes;
	}

	public String getRefreshTokenValue() {
		return refreshTokenValue;
	}

	public void setRefreshTokenValue(String refreshTokenValue) {
		this.refreshTokenValue = refreshTokenValue;
	}

	public Instant getRefreshTokenIssuedAt() {
		return refreshTokenIssuedAt;
	}

	public void setRefreshTokenIssuedAt(Instant refreshTokenIssuedAt) {
		this.refreshTokenIssuedAt = refreshTokenIssuedAt;
	}

	public Instant getRefreshTokenExpiresAt() {
		return refreshTokenExpiresAt;
	}

	public void setRefreshTokenExpiresAt(Instant refreshTokenExpiresAt) {
		this.refreshTokenExpiresAt = refreshTokenExpiresAt;
	}

	public String getRefreshTokenMetadata() {
		return refreshTokenMetadata;
	}

	public void setRefreshTokenMetadata(String refreshTokenMetadata) {
		this.refreshTokenMetadata = refreshTokenMetadata;
	}

	public String getOidcIdTokenValue() {
		return oidcIdTokenValue;
	}

	public void setOidcIdTokenValue(String oidcIdTokenValue) {
		this.oidcIdTokenValue = oidcIdTokenValue;
	}

	public Instant getOidcIdTokenIssuedAt() {
		return oidcIdTokenIssuedAt;
	}

	public void setOidcIdTokenIssuedAt(Instant oidcIdTokenIssuedAt) {
		this.oidcIdTokenIssuedAt = oidcIdTokenIssuedAt;
	}

	public Instant getOidcIdTokenExpiresAt() {
		return oidcIdTokenExpiresAt;
	}

	public void setOidcIdTokenExpiresAt(Instant oidcIdTokenExpiresAt) {
		this.oidcIdTokenExpiresAt = oidcIdTokenExpiresAt;
	}

	public String getOidcIdTokenMetadata() {
		return oidcIdTokenMetadata;
	}

	public void setOidcIdTokenMetadata(String oidcIdTokenMetadata) {
		this.oidcIdTokenMetadata = oidcIdTokenMetadata;
	}

}
