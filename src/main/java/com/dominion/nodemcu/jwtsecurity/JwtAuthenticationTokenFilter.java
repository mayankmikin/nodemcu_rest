package com.dominion.nodemcu.jwtsecurity;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.dominion.nodemcu.controllers.JWTTokenController;
import com.dominion.nodemcu.model.JwtAuthenticationToken;

public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {
	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    public JwtAuthenticationTokenFilter() {
        super("/**");
    }
    public JwtAuthenticationTokenFilter(String pattern) {
        super(pattern);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        String header = httpServletRequest.getHeader("Authorization");

        
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("JWT Token is missing");
        }

        String authenticationToken = header.substring(6);
        LOG.info("token is : "+authenticationToken);
        JwtAuthenticationToken token = new JwtAuthenticationToken(authenticationToken);
        return getAuthenticationManager().authenticate(token);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}