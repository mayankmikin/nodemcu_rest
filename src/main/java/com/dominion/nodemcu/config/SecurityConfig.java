package com.dominion.nodemcu.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${security.signing-key}")
	private String signingKey;

	@Value("${security.encoding-strength}")
	private Integer encodingStrength;

	@Value("${security.security-realm}")
	private String securityRealm;

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		        /*.sessionManagement()
		        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		        .and()
		        .httpBasic()
		        .realmName(securityRealm)
		        .and()*/
		        .csrf()
		        .disable()
		        
		        // now below configuration allows us to see h2 console
				.authorizeRequests().antMatchers("/","/h2/**").permitAll().and()
				.formLogin().loginPage("/login").permitAll()
				.and()
				.logout().permitAll();
		http.exceptionHandling().accessDeniedPage("/403");
		
		http.headers().frameOptions().disable();
		/*http.headers().addHeaderWriter(
	            new XFrameOptionsHeaderWriter(
	            		XFrameOptionsMode.SAMEORIGIN));*/

	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(signingKey);
		return converter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	@Primary //Making this primary to avoid any accidental duplication with another token service instance of the same name
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}
}


/*
To enable access to the H2 database console under Spring Security you need to change three things:

Allow all access to the url path /console.
Disable CRSF (Cross-Site Request Forgery). By default, Spring Security will protect against CRSF attacks.
Since the H2 database console runs inside a frame, you need to enable this in in Spring Security.
The following Spring Security Configuration will:

Allow all requests to the root url (“/”) (Line 12)
Allow all requests to the H2 database console url (“/console/*”) (Line 13)
Disable CSRF protection (Line 15)
Disable X-Frame-Options in Spring Security (Line 16)
CAUTION: This is not a Spring Security Configuration that you would want to use for a production website.
 These settings are only to support development of a Spring Boot web application and enable access to the 
 H2 database console. I cannot think of an example where you’d actually want the H2 database console exposed 
 on a production database.
*/