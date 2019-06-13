package com.whitehall.esp.microservices.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.whitehall.esp.microservices.model.Groups;
import com.whitehall.esp.microservices.model.User;

import reactor.core.publisher.Mono;

public interface GroupsRepository  extends ReactiveCrudRepository<Groups, String>{

	Mono<Groups> findByGroupId(String groupId);
}
