package com.dominion.nodemcu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
    private ResourceServerTokenServices tokenServices;

    @Value("${security.jwt.resource-ids}")
    private String resourceIds;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceIds).tokenServices(tokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
                http
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/**", "/api-docs/**").permitAll()
                .antMatchers("/springjwt/**" ).authenticated();
    }
}

/*@EnableResourceServer: Enables a resource server. By default this annotation creates a security filter which authenticates requests via an incoming OAuth2 token. The filter is an instance of WebSecurityConfigurerAdapter which has an hard-coded order of 3 (Due to some limitations of Spring Framework). You need to tell Spring Boot to set OAuth2 request filter order to 3 to align with the hardcoded value. You do that by adding security.oauth2.resource.filter-order = 3 in the application.properties file. Hopefully this will be fixed in future releases.

The resource server has the authority to define the permission for any endpoint. The the endpoint permission is defined with: */
//.antMatchers(“/actuator/**”, “/api-docs/**”).permitAll()
 //.antMatchers(“/springjwt/**”).authenticated()

//Here notice that the resource and the authorization servers both use the same token service. That is because they are in the same code base so we are reusing the same bean.