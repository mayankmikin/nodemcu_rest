package com.dominion.nodemcu.jwtsecurity;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dominion.nodemcu.controllers.JWTTokenController;
import com.dominion.nodemcu.entity.Role;
import com.dominion.nodemcu.model.JwtUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtValidator {

	private static final Logger LOG = LoggerFactory.getLogger(JwtValidator.class);
    private String secret = "youtube";

    public JwtUser validate(String token) {

        JwtUser jwtUser = null;
     
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            jwtUser = new JwtUser();
            LOG.info("body is : "+(String) body.get("userId"));
            LOG.info("body is : "+(List<Role>) body.get("role"));
            jwtUser.setUserName(body.getSubject());
            jwtUser.setId(Long.parseLong((String) body.get("userId")));
            List<Role>roles=(List<Role>) body.get("role");
           /* StringBuilder roleName="";
            roles.stream().map(role->
            {
            	roleName.append(role.getRoleName()).append(",");
            });*/
            jwtUser.setRole((List<Role>) body.get("role"));
        
       

        return jwtUser;
    }
}