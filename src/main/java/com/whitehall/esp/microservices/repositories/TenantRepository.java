package com.whitehall.esp.microservices.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.whitehall.esp.microservices.model.Tenant;

import reactor.core.publisher.Mono;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@RepositoryRestResource(collectionResourceRel = "tenant", path = "tenant")
@Repository
public interface TenantRepository extends ReactiveCrudRepository<Tenant, String> {

	Mono<Tenant> findByTenantId(String tenantId);
	//@RestResource(rel = "tenant", path="/findbycode/{code}")
	Mono<Tenant> findByCode(String code);
	Mono<Boolean> existsByTenantName(String tenantName);
}
