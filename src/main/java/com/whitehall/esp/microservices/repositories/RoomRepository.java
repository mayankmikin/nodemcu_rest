package com.whitehall.esp.microservices.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.whitehall.esp.microservices.model.Room;

@Repository
public interface RoomRepository extends ReactiveCrudRepository<Room, String> {
//	Mono<BuildingInfo> findByAccountAccountId(String accountId);
//	Mono<BuildingInfo> findByAccountAccountName(String accountName);
}

