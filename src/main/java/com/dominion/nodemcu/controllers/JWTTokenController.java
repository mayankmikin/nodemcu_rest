package com.dominion.nodemcu.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dominion.nodemcu.jwtsecurity.JwtGenerator;

@RestController
@RequestMapping("/token")
public class JWTTokenController 
{
	private static final Logger LOG = LoggerFactory.getLogger(JWTTokenController.class);
	@PostMapping("/{userName}")
	public ResponseEntity<String> tokenshowrole(@PathVariable final String username)
	{
		 LOG.info("token show role");
		 JwtGenerator jwtUser=new JwtGenerator();
		 
		 
		 return new ResponseEntity<String>("Role is",HttpStatus.OK);
	}
	
	@GetMapping(value = "/rest/authentication")
	/*no need to use preauthorize as we have done pattern based authorization 
	.authorizeRequests().antMatchers(pattern).authenticated()*/
	 //@PreAuthorize("hasAuthority('ADMIN_USER')")
	// @PreAuthorize("hasAnyRole('STANDARD_USER', 'ADMIN_USER')")
	//@PreAuthorize("hasRole('STANDARD_USER')")
	public ResponseEntity<String> showrole()
	{
		 LOG.info("showrole");
		 
		 return new ResponseEntity<String>("Role is",HttpStatus.OK);
	}
}
