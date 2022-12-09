package pk.training.basit.authorizationserver.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;

import pk.training.basit.authorizationserver.jpa.entity.UserPrincipal;

@Validated
public interface UserPrincipalService extends UserDetailsService {

	@Override
    UserPrincipal loadUserByUsername(String username);
	
}
