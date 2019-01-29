package com.dominion.nodemcu.jwtsecurity;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.dominion.nodemcu.model.JwtAuthenticationToken;
import com.dominion.nodemcu.model.JwtUser;
import com.dominion.nodemcu.model.JwtUserDetails;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationProvider.class);
    @Autowired
    private JwtValidator validator;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
    	 JwtUser jwtUser=new JwtUser();
    	try {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken;
        String token = jwtAuthenticationToken.getToken();
        
        jwtUser= validator.validate(token);

        if (jwtUser == null) {
            throw new RuntimeException("JWT Token is incorrect");
        }
        String role="";
        jwtUser.getRole()
		.stream()
		.map(e->role+e.getRoleName()+",");
        /*To remove the ", " part which is immediately followed by end of string, you can do:

        	str = str.replaceAll(", $", "");*/
        role.replaceAll(", $", "");
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(role);
        return new JwtUserDetails(jwtUser.getUserName(), jwtUser.getId(),
                token,
                grantedAuthorities);
    	}
    	 catch (ExpiredJwtException e) {
             LOG.error(e.getMessage());
             return new JwtUserDetails(jwtUser.getUserName(), jwtUser.getId(),
                     null,
                     null);	
         }
         catch (Exception e) {
         	  LOG.error(e.getMessage());
         	 return new JwtUserDetails(jwtUser.getUserName(), jwtUser.getId(),
                     null,
                     null);
         }
    	
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
    }
}