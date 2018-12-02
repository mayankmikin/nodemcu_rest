package com.dominion.nodemcu.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dominion.nodemcu.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

		List<User> findByFirstnameAndIsActive(String firstname, Boolean isActive);
		List<User> findByLastname(String lastName);
	
}
