package com.assesment.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dominion.entity.User;
import com.dominion.repository.UserRepository;

public class ConfigurationControllerIT extends BaseIT {

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	UserRepository confgurationRepository;

	@Test
	public void should_create_configuration() throws Exception {
		// Given
		User request = this.createSampleUser();

		// When
		ResponseEntity<User> responseEntity = testRestTemplate.postForEntity("/api/users/save",
				request, User.class);

		// Then
		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		User configurationResponse = responseEntity.getBody();
		assertThat(configurationResponse).isNotNull();
		assertThat(configurationResponse.getId()).isNotNull();
	}

	@Test
	public void should_return_all_configurations() {

		confgurationRepository.save(new User(new Long(1),"abc","xyz","USA",true,"909090",Calendar.getInstance().getTime(),Calendar.getInstance().getTime()));
		confgurationRepository.save(new User(new Long(1),"def","xyz","CANADA",true,"909091",Calendar.getInstance().getTime(),Calendar.getInstance().getTime()));
		confgurationRepository.save(new User(new Long(1),"ghi","xyz","INDIA",true,"909092",Calendar.getInstance().getTime(),Calendar.getInstance().getTime()));

		ResponseEntity<List> entityList = testRestTemplate.getForEntity("/api/ang/configurations", List.class);

		assertThat(entityList).isNotNull();
		assertThat(entityList.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entityList.getBody().size()).isGreaterThan(2);
	}



/*	@Test
	public void should_get_return_404_for_invalid_application_name() throws Exception {

		// given
		final String WRONG_APP_NAME = "Invalid App";

		// when
		ResponseEntity<Response> responseEntity = testRestTemplate
				.getForEntity("/api/ang/configuration/appname/" + WRONG_APP_NAME, Response.class);

		// then
		assertThat(responseEntity.getBody().getStatus(), equalTo(RequestStatus.FAILURE));
		assertThat(responseEntity.getBody().getErrorCode(), equalTo("1"));
		assertThat(responseEntity.getBody().getErrorMessage(), equalTo("Belirtilen nesne sistemde bulunmuyor"));
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		MatcherAssert.assertThat(responseEntity.getStatusCode(), Matchers.equalTo(HttpStatus.NOT_FOUND));

	}*/

	private final User createSampleUser() {

		User user = new User();
		user.setFirstname("sahithi");
		user.setLastname("bandari");
		user.setId(new Long(1));
		user.setAddress("USA");
		user.setIsActive(true);
		user.setPhone("917 822 2651");
		return user;
	}

}
