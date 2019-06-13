package com.whitehall.esp.microservices.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.whitehall.esp.microservices.model.User;
import com.whitehall.esp.microservices.repositories.UserRepository;
@Service
public class CustomUserDetails implements UserDetailsService {

	@Autowired
	  private UserRepository userRepository;

	  @Override
	  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    final User user = userRepository.findByEmail(email).block();

	    if (user == null) {
	      throw new UsernameNotFoundException("User '" + email + "' not found");
	    }

	    return org.springframework.security.core.userdetails.User//
	        .withUsername(email)//
	        .password(user.getPassword())//
	        .authorities(user.getRoles())//
	        .accountExpired(false)//
	        .accountLocked(false)//
	        .credentialsExpired(false)//
	        .disabled(false)//
	        .build();
	  }

}
