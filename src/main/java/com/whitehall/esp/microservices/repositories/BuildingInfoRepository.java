package com.whitehall.esp.microservices.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.whitehall.esp.microservices.model.BuildingInfo;

import reactor.core.publisher.Mono;

@Repository
public interface BuildingInfoRepository extends ReactiveCrudRepository<BuildingInfo, String> {
	Mono<BuildingInfo> findByAccountAccountId(String accountId);
	Mono<BuildingInfo> findByAccountAccountName(String accountName);
}

