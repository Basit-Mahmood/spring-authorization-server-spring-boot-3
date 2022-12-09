package pk.training.basit.authorizationserver.oauth2.entity;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import pk.training.basit.authorizationserver.jpa.audit.AuditDeletedDate;

@Entity
@Table(name = "oauth2_client")
public class OAuth2Client implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "client_id")
	private String clientId;
	
	@Column(name = "client_id_issued_at")
	private Instant clientIdIssuedAt;

	@Column(name = "client_name")
	private String clientName;

	@Column(name = "client_secret")
	private String clientSecret;
	
	@Column(name = "client_secret_expires_at")
	private Instant clientSecretExpiresAt;

	@Column(name = "authentication_method")
	private String clientAuthenticationMethods;

	@Column(name = "authorization_grant_type")
	private String authorizationGrantTypes;

	@Column(name = "redirect_uris")
	private String redirectUris;

	private String scopes;

	private boolean registered;

	@OneToOne(mappedBy = "oauth2Client", cascade = CascadeType.ALL, optional = false)
	private OAuth2ClientTokenSetting tokenSetting;

	@OneToOne(mappedBy = "oauth2Client", cascade = CascadeType.ALL)
	private OAuth2ClientSetting clientSetting;
	
	@Embedded
    private AuditDeletedDate audit = new AuditDeletedDate();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
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

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public OAuth2ClientTokenSetting getTokenSetting() {
		return tokenSetting;
	}

	public void setTokenSetting(OAuth2ClientTokenSetting tokenSetting) {
		this.tokenSetting = tokenSetting;
	}

	public OAuth2ClientSetting getClientSetting() {
		return clientSetting;
	}

	public void setClientSetting(OAuth2ClientSetting clientSetting) {
		this.clientSetting = clientSetting;
	}

	public AuditDeletedDate getAudit() {
		return audit;
	}

	public void setAudit(AuditDeletedDate audit) {
		this.audit = audit;
	}
	
}
