package com.whitehall.esp.microservices.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

	  @Autowired
	  private JwtTokenProvider jwtTokenProvider;

	  @Override
	  protected void configure(HttpSecurity http) throws Exception {

	    // Disable CSRF (cross site request forgery)
	    http.csrf().disable();
	    http.cors(); // to enable the cors filter

	    // No session will be created or used by spring security
	    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	    // Entry points
	    http.authorizeRequests()//
	        .antMatchers("/api/v1/user/authenticate").permitAll()//
	        .antMatchers("/api/v1/user/register").permitAll()//	
	        .antMatchers("/api/v1/user/invite/register").permitAll()//	
	        .antMatchers("/api/v1/user/sendemail/**").permitAll()//
	        .antMatchers("/api/v1/user/verify/**").permitAll()//
	        .antMatchers("/api/v1/user/getRoles").permitAll()//
	        .antMatchers("/api/v1/role").permitAll()//
	        .antMatchers("/api/v1/device").permitAll()
	        .antMatchers("/browser/**").permitAll()//
	        .antMatchers("/error/**").permitAll()//
	        .antMatchers("/").permitAll()//
	        //.antMatchers("/h2-console/**/**").permitAll()
	        // Disallow everything else..
	        .anyRequest().authenticated();

	    // If a user try to access a resource without having enough permissions
	    http.exceptionHandling().accessDeniedPage("/login");

	    // Apply JWT
	    http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

	    // Optional, if you want to test the API from a browser
	    // http.httpBasic();
	  }

	  @Override
	  public void configure(WebSecurity web) throws Exception {
	    // Allow swagger to be accessed without authentication
	    web.ignoring().antMatchers("/v2/api-docs")//
	    	.antMatchers("/v1/api/**")//
	        .antMatchers("/swagger-resources/**")//
	        .antMatchers("/swagger-ui.html")//
	        .antMatchers("/configuration/**")//
	        .antMatchers("/webjars/**")//
	        .antMatchers("/public")
	        
	        // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
	        .and()
	        .ignoring()
	        .antMatchers("/h2-console/**/**");
	  }

	  @Bean
	  public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder(12);
	  }
	  
	  @Bean
	  public AuthenticationManager customAuthenticationManager() throws Exception {
	    return authenticationManager();
	  }
}
