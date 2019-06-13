package com.whitehall.esp.microservices.repositories;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.whitehall.esp.microservices.model.PaymentMethod;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentMethodRepository extends ReactiveCrudRepository<PaymentMethod, String>{
	Mono<PaymentMethod> findByPaymentMethodId(String paymentMethodId);
	
	@Query("{ 'enablePaymentMethod' : { $regex: ?0 } }")
	Flux<PaymentMethod> findAllActive(Boolean enablePaymentMethod);
	
}
