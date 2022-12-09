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
@Table(name = "oauth2_client_token_setting")
public class OAuth2ClientTokenSetting implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "access_token_time")
	private Integer accessTokenTime;

	@Column(name = "access_token_time_unit")
	private String accessTokenTimeUnit;

	@Column(name = "refresh_token_time")
	private Integer refreshTokenTime;

	@Column(name = "refresh_token_time_unit")
	private String refreshTokenTimeUnit;

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

	public Integer getAccessTokenTime() {
		return accessTokenTime;
	}

	public void setAccessTokenTime(Integer accessTokenTime) {
		this.accessTokenTime = accessTokenTime;
	}

	public String getAccessTokenTimeUnit() {
		return accessTokenTimeUnit;
	}

	public void setAccessTokenTimeUnit(String accessTokenTimeUnit) {
		this.accessTokenTimeUnit = accessTokenTimeUnit;
	}

	public Integer getRefreshTokenTime() {
		return refreshTokenTime;
	}

	public void setRefreshTokenTime(Integer refreshTokenTime) {
		this.refreshTokenTime = refreshTokenTime;
	}

	public String getRefreshTokenTimeUnit() {
		return refreshTokenTimeUnit;
	}

	public void setRefreshTokenTimeUnit(String refreshTokenTimeUnit) {
		this.refreshTokenTimeUnit = refreshTokenTimeUnit;
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
