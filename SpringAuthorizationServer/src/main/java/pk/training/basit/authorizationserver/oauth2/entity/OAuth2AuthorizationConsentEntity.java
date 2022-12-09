package pk.training.basit.authorizationserver.oauth2.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

/**
 * Naming this entity OAuth2AuthorizationConsentEntity instead of OAuth2AuthorizationConsent so we can identify it with the 
 * OAuth2AuthorizationConsent internal spring class. oauth2_authorization_consent is a table define internally by spring. 
 * The location of this table can be find in database.properties file.
 * 
 * @author basit
 */
@Entity
@Table(name = "oauth2_authorization_consent")
@IdClass(OAuth2AuthorizationConsentEntity.AuthorizationConsentId.class)
public class OAuth2AuthorizationConsentEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "registered_client_id")
	private String registeredClientId;

	@Id
	@Column(name = "principal_name")
	private String principalName;

	@Column(length = 1000)
	private String authorities;

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

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public static class AuthorizationConsentId implements Serializable {

		private static final long serialVersionUID = 1L;
		private String registeredClientId;
		private String principalName;

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			AuthorizationConsentId that = (AuthorizationConsentId) o;
			return registeredClientId.equals(that.registeredClientId) && principalName.equals(that.principalName);
		}

		@Override
		public int hashCode() {
			return Objects.hash(registeredClientId, principalName);
		}
	}

}
