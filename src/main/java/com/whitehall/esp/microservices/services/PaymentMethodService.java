package com.whitehall.esp.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whitehall.esp.microservices.model.PaymentMethod;
import com.whitehall.esp.microservices.repositories.PaymentMethodRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PaymentMethodService {
	
	@Autowired
	PaymentMethodRepository repository;
	
	public Mono<PaymentMethod> updatePaymentMethod(PaymentMethod payM) {
		return repository.save(payM);
	}
	
	public Flux<PaymentMethod> getAllEnabledPaymentMethods(){
		return repository.findAllActive(true);
	}
	
	public Flux<PaymentMethod> getAll(){
		return repository.findAll();
	}
	
	public Mono<PaymentMethod> findbyPaymentMethodId(String id){
		return repository.findByPaymentMethodId(id);
	}
	
	
	public Mono<PaymentMethod> addPaymentMethod(PaymentMethod payM) {
		return repository.save(payM);
	}
	
	public Flux<PaymentMethod> deletePaymentMethod(PaymentMethod payM) {
		repository.delete(payM);
		return getAll();
		
	}
	
}
