package pk.training.basit.authorizationserver.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;

import pk.training.basit.authorizationserver.jpa.entity.User;

@Validated
public interface UserPrincipalService extends UserDetailsService {

	@Override
    User loadUserByUsername(String username);
	
}
