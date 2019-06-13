package com.whitehall.esp.microservices.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.whitehall.esp.microservices.model.TenantInfo;

import reactor.core.publisher.Mono;

//@RepositoryRestResource(collectionResourceRel = "tenantInfo", path = "tenantInfo")
@Repository
public interface TenantInfoRepository extends ReactiveCrudRepository<TenantInfo, String> {

	Mono<TenantInfo> findByTenantName(String tenantName);
}
