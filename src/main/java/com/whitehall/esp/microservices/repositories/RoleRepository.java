package com.whitehall.esp.microservices.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.whitehall.esp.microservices.model.Role;
import com.whitehall.esp.microservices.model.User;

import reactor.core.publisher.Mono;

public interface RoleRepository  extends ReactiveCrudRepository<Role, String>{

	public Mono<Role> findByRoleName(String roleName);
}
