package com.whitehall.esp.microservices.config;


import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.whitehall.esp.microservices.exceptions.CustomException;
import com.whitehall.esp.microservices.model.Role;
import com.whitehall.esp.microservices.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

	 /**
	   * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
	   * microservices environment, this key would be kept on a config-server.
	   */
	  @Value("${security.jwt.token.secret-key:secret-key}")
	  private String secretKey;

	  @Value("${security.jwt.token.expire-length:3600000}")
	  private long validityInMilliseconds = 3600000; // 1h

	  @Autowired
	  private CustomUserDetails myUserDetails;

	  @PostConstruct
	  protected void init() {
	    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	  }

	  public String createToken(String username, User u) {

	    Claims claims = Jwts.claims().setSubject(username);
	   // claims.put("roles", u.getRoles().stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));
	    claims.put("details",u);
	    Date now = new Date();
	    Date validity = new Date(now.getTime() + validityInMilliseconds);

	    return Jwts.builder()//
	        .setClaims(claims)//
	        .setIssuedAt(now)//
	        .setExpiration(validity)//
	        .signWith(SignatureAlgorithm.HS256, secretKey)//
	        .compact();
	  }

	  public Authentication getAuthentication(String token) {
	    UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
	    log.info("userDetails: "+userDetails);
	    Authentication auth=new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	    log.info("auth is: "+auth);
	    return auth;
	  }

	  public String getUsername(String token) {
	    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	  }

	  public String resolveToken(HttpServletRequest req) {
	    String bearerToken = req.getHeader("Authorization");
	  //  log.info("resolving Token");
	    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
	    	log.info(bearerToken);
	      return bearerToken.substring(7);
	    }
	    return null;
	  }

	  public boolean validateToken(String token) {
	    try {
	    	log.info("validateToken seckret key is: "+secretKey);
	      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
	      return true;
	    } catch (JwtException | IllegalArgumentException e) {
	      throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
}
