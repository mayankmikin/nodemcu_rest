package com.whitehall.esp.microservices.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.whitehall.esp.microservices.model.Account;

import reactor.core.publisher.Mono;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@RepositoryRestResource(collectionResourceRel = "tenant", path = "tenant")
@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, String> {

	Mono<Boolean> existsByAccountName(String accountName);
	

	Mono<Account> findByAccountName(String accountName);
}
