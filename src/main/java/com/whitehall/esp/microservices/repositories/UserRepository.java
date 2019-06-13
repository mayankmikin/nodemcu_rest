package com.whitehall.esp.microservices.repositories;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.whitehall.esp.microservices.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@RepositoryRestResource(collectionResourceRel = "tenant", path = "tenant")
@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String> {

	Mono<User> findByPhone(String phone);
	Mono<User> findByEmail(String email);
	Flux<User> findByAccountAccountId(String accountId);
	Mono<User> findByConfirmationToken(String confirmationToken);
	Mono<Boolean> existsByEmail(String email);
	Mono<Boolean> existsByPhone(String phone);
	 //@Query("{ 'account': {'$ref': 'account', '$id': { '$id': ?0 } } }")
		//@Query("{ 'account': {'$ref': 'account', '$id': ?0 } }")
	@Query("{ 'email' : { $regex: ?0 } }")
	Flux<User> findUsersByRegexpName(String regexp);
}
