package com.whitehall.esp.microservices.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.whitehall.esp.microservices.model.Plan;

import reactor.core.publisher.Mono;

public interface PlanRepository extends ReactiveCrudRepository<Plan, String> {
	Mono<Plan> findByPlanId(String plainId);
}
