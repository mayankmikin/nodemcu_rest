package com.dominion.nodemcu.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dominion.nodemcu.entity.User;
import com.dominion.nodemcu.repository.UserRepository;

/*in order to retrieve user details from the database.
 * this user detail service is provided by spring */
@Service
public class AppUserDetailsService implements UserDetailsService {

	  @Autowired
	   private UserRepository userRepository;
	  
	  @Override
	    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
	        User user = userRepository.findByEmail(s).get();

	        if(user == null) {
	            throw new UsernameNotFoundException(String.format("The username or email id %s doesn't exist", s));
	        }

	        List<GrantedAuthority> authorities = new ArrayList<>();
	        user.getRoles().forEach(role -> {
	            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
	        });

	        UserDetails userDetails = new org.springframework.security.core.userdetails.
	                User(user.getEmail(), user.getPassword(), authorities);

	        return userDetails;
	    }

}
