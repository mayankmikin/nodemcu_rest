package com.assesment.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.dominion.entity.User;
import com.dominion.repository.UserRepository;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ConfigurationServiceTest extends BaseMockitoTest {



   @Mock
   public UserRepository configurationRepository;

  

   public User entity;

   @Before
   public void before() {

      entity = new User();
      entity.setFirstname("sahithi");
      entity.setLastname("bandari");
      entity.setId(new Long(1));
      entity.setAddress("USA");
      entity.setIsActive(true);
      entity.setPhone("917 822 2651");
   }


   @Test
   public void should_save_configuration() {

	   when(configurationRepository.save(entity)).thenReturn(entity);
	   
      User foundedConfiguration = createSampleConfigurationEntity();

      assertThat(foundedConfiguration).isNotNull();
      assertThat(foundedConfiguration.getFirstname()).isEqualTo(entity.getFirstname());
      assertThat(foundedConfiguration.getId()).isNotNull();
      assertThat(foundedConfiguration.getLastname()).isEqualTo(entity.getLastname());
      assertThat(foundedConfiguration.getAddress()).isEqualTo(entity.getAddress());
      assertThat(foundedConfiguration.getPhone()).isEqualTo(entity.getPhone());
      assertThat(foundedConfiguration.getIsActive()).isEqualTo(entity.getIsActive());
   }
   
   @Test
   public void negativeTestCase() {

	   when(configurationRepository.save(entity)).thenReturn(entity);
	   
      User foundedConfiguration = createdifferentConfigurationEntity();

      assertThat(foundedConfiguration).isNotNull();
      assertThat(foundedConfiguration.getFirstname()).isNotEqualTo(entity.getFirstname());
      assertThat(foundedConfiguration.getId()).isNotNull();
      assertThat(foundedConfiguration.getLastname()).isNotEqualTo(entity.getLastname());
      assertThat(foundedConfiguration.getAddress()).isNotEqualTo(entity.getAddress());
      assertThat(foundedConfiguration.getPhone()).isNotEqualTo(entity.getPhone());
      assertThat(foundedConfiguration.getIsActive()).isNotEqualTo(entity.getIsActive());
   }



   @Test
   public void should_find_all_configurations() {

      User entity_I = createSampleConfigurationEntity();
      entity_I.setFirstname("X");

      User entity_II = createSampleConfigurationEntity();
      entity_II.setFirstname("Y");

      List<User> configurationList = new ArrayList<>();
      configurationList.add(entity_I);
      configurationList.add(entity_II);

      when(configurationRepository.findAll()).thenReturn(configurationList);

      List<User> returnedList = (List<User>) configurationRepository.findAll();
      assertThat(returnedList).isNotNull();
      assertThat(returnedList.size()).isEqualTo(2);

   }


   private User createSampleConfigurationEntity() {

	   User user = new User();
		user.setFirstname("sahithi");
		user.setLastname("bandari");
		user.setId(new Long(1));
		user.setAddress("USA");
		user.setIsActive(true);
		user.setPhone("917 822 2651");
		return user;
   }
   private User createdifferentConfigurationEntity() {

	   User user = new User();
		user.setFirstname("Not sahithi");
		user.setLastname("Not bandari");
		user.setId(new Long(2));
		user.setAddress("NOT USA");
		user.setIsActive(false);
		user.setPhone("2917 822 2651");
		return user;
   }
}
