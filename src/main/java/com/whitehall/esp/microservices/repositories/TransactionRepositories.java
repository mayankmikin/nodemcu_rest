package com.whitehall.esp.microservices.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.whitehall.esp.microservices.model.Transaction;

import reactor.core.publisher.Mono;

public interface TransactionRepositories extends ReactiveCrudRepository<Transaction, String> {
	Mono<Transaction> findByTransactionId(String transactionId);
	
}
