package com.dominion.nodemcu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Component;

import com.dominion.nodemcu.entity.User;
import com.dominion.nodemcu.model.Account;
import com.dominion.nodemcu.model.Device;

@SpringBootApplication
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}
	
	/*Spring Data Rest hides the ID by default, in order to have it in the JSON you have to manually configure that for your entity. 
	Depending on your spring version you can either provide your own configuration (old):*/
	@Component
	public class ExposeEntityIdRestMvcConfiguration extends RepositoryRestConfigurerAdapter {

	  @Override
	  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
	    config.exposeIdsFor(Account.class);
	    config.exposeIdsFor(Device.class);
	    config.exposeIdsFor(User.class);
	  }
	}
	
}

