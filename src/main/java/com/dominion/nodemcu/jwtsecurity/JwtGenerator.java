package com.dominion.nodemcu.jwtsecurity;

import java.time.Instant;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.dominion.nodemcu.model.JwtUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtGenerator {


    public String generate(UserDetails jwtUser) {

        Claims claims = Jwts.claims()
                .setSubject(jwtUser.getUsername());
        claims.put("username", String.valueOf(jwtUser.getUsername()));
        claims.put("authorities", jwtUser.getAuthorities());
       

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "youtube")
                .setExpiration(new Date().from(Instant.now().plusMillis(30000)))
                .compact();
    }
}