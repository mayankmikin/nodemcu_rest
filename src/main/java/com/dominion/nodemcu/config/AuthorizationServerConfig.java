package com.dominion.nodemcu.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
@ComponentScan
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Value("${security.jwt.client-id}")
	   private String clientId;

	   @Value("${security.jwt.client-secret}")
	   private String clientSecret;

	   @Value("${security.jwt.grant-type}")
	   private String grantType;

	   @Value("${security.jwt.scope-read}")
	   private String scopeRead;

	   @Value("${security.jwt.scope-write}")
	   private String scopeWrite = "write";

	   @Value("${security.jwt.resource-ids}")
	   private String resourceIds;

	   @Autowired
	   private TokenStore tokenStore;

	   @Autowired
	   private JwtAccessTokenConverter accessTokenConverter;

	   @Autowired
	   private AuthenticationManager authenticationManager;

	   @Override
	   public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
	      configurer
	              .inMemory()
	              .withClient(clientId)
	              .secret(clientSecret)
	              .authorizedGrantTypes(grantType)
	              .scopes(scopeRead, scopeWrite)
	              .resourceIds(resourceIds);
	   }

	   @Override
	   public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	      TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
	      enhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
	      endpoints.tokenStore(tokenStore)
	              .accessTokenConverter(accessTokenConverter)
	              .tokenEnhancer(enhancerChain)
	              .authenticationManager(authenticationManager);
	   }
	   @Override
	    public void configure(
	      AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
	        oauthServer.tokenKeyAccess("permitAll()")
	          .checkTokenAccess("isAuthenticated()");
	    }
}

/*· @EnableAuthorizationServer: Enables an authorization server

· AuthorizationServerConfig which is our authorization server configuration class extends AuthorizationServerConfigurerAdapter which in turn is an implementation of AuthorizationServerConfigurer. The presence of a bean of type AuthorizationServerConfigurer simply tells Spring Boot to switch off auto-configuration and use the custom configuration. Also the AuthorizationServerConfig like any other configuration class has its definition automatically scanned,wired and applied by Spring Boot because of the @Configuration annotation.

· Client id: defines the id of the client application that is authorized to authenticate, the client application provides this in order to be allowed to send request to the server.

Client secret: is the client application’s password. In a non-trivial implementation client ids and passwords will be securely stored in a database and retrievable through a separate API that clients applications access during deployment. These pieces of information can also be shared and stored in environment variables although that would not be my preferred option.

· Grant type: we define grant type password here because it’s not enabled by default

· The scope: read, write defines the level of access we are allowing to resources

· Resource Id: The resource Id specified here must be specified on the resource server as well

· AuthenticationManager: Spring’s authentication manager takes care checking user credential validity

· TokenEnhancerChain: We define a token enhancer that enables chaining multiple types of claims containing different information*/
