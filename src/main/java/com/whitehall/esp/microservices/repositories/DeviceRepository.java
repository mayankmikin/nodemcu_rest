package com.whitehall.esp.microservices.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.whitehall.esp.microservices.model.Device;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@RepositoryRestResource(collectionResourceRel = "tenant", path = "tenant")
@Repository
public interface DeviceRepository extends ReactiveCrudRepository<Device, String> {

	
	  //@Query("{ 'account': {'$ref': 'account', '$id': { '$id': ?0 } } }")
	//@Query("{ 'account': {'$ref': 'account', '$id': ?0 } }")
	Flux<Device> findByAccountAccountId(String accountId);
	 
	Mono<Device> findBySerialId(String serialId);
	
	Flux<Device> findByExporterId(String exporterId);
	
	Flux<Device> findByManufacturerId(String manufacturerId);
	Mono<Boolean> existsBySerialId(String serialId);
	
	
}
