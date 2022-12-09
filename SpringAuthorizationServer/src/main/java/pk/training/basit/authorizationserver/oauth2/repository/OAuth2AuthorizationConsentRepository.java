package pk.training.basit.authorizationserver.oauth2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pk.training.basit.authorizationserver.oauth2.entity.OAuth2AuthorizationConsentEntity;

public interface OAuth2AuthorizationConsentRepository extends JpaRepository<OAuth2AuthorizationConsentEntity, OAuth2AuthorizationConsentEntity.AuthorizationConsentId> {

	Optional<OAuth2AuthorizationConsentEntity> findByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
	void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
	
}
