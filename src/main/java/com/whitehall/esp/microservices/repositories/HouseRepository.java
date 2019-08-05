package com.whitehall.esp.microservices.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.whitehall.esp.microservices.model.BuildingInfo;
import com.whitehall.esp.microservices.model.House;

import reactor.core.publisher.Mono;

@Repository
public interface HouseRepository extends ReactiveCrudRepository<House, String> {
//	Mono<BuildingInfo> findByAccountAccountId(String accountId);
//	Mono<BuildingInfo> findByAccountAccountName(String accountName);
}

