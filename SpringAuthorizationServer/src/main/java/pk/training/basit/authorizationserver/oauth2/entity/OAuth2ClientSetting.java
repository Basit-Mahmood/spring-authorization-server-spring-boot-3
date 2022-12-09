package pk.training.basit.authorizationserver.oauth2.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import pk.training.basit.authorizationserver.jpa.audit.AuditDeletedDate;

@Entity
@Table(name = "oauth2_client_setting")
public class OAuth2ClientSetting implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "require_authorization_consent")
	private boolean requireAuthorizationConsent;

	@OneToOne
	@JoinColumn(name = "client_id", referencedColumnName = "client_id", nullable = false)
	private OAuth2Client oauth2Client;

	@Embedded
	private AuditDeletedDate audit = new AuditDeletedDate();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isRequireAuthorizationConsent() {
		return requireAuthorizationConsent;
	}

	public void setRequireAuthorizationConsent(boolean requireAuthorizationConsent) {
		this.requireAuthorizationConsent = requireAuthorizationConsent;
	}

	public OAuth2Client getOauth2Client() {
		return oauth2Client;
	}

	public void setOauth2Client(OAuth2Client oauth2Client) {
		this.oauth2Client = oauth2Client;
	}

	public AuditDeletedDate getAudit() {
		return audit;
	}

	public void setAudit(AuditDeletedDate audit) {
		this.audit = audit;
	}

}
