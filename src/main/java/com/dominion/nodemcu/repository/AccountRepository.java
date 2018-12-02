package com.dominion.nodemcu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.dominion.nodemcu.entity.Account;
import com.dominion.nodemcu.entity.Device;
import com.dominion.nodemcu.entity.User;

/*rest repository will work only if your model class implements Serializable*/
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RepositoryRestResource(collectionResourceRel = "account", path = "account")
public interface AccountRepository extends CrudRepository<Account, Long> {

	Optional<Account> findById(Long id);
	//List<Device> findDevices(Long id);
	//List<User> findUsers(Long id);
}
