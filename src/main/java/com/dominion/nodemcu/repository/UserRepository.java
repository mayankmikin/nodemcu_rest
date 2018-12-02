package com.dominion.nodemcu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.dominion.nodemcu.entity.User;
/*rest repository will work only if your model class implements Serializable*/
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends CrudRepository<User, Long> {

		List<User> findByFirstnameAndIsactive(String firstname, Boolean isactive);
		List<User> findByLastname(String lastName);
		Optional<User> findById(Long id);
		Optional<User> findByAccount(Long id);
		
}
