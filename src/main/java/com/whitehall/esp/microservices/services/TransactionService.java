package com.whitehall.esp.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whitehall.esp.microservices.model.Transaction;
import com.whitehall.esp.microservices.repositories.TransactionRepositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepositories repositories;
	
	public Mono<Transaction> addTransaction(Transaction t){
		return repositories.save(t);
	}
	
	public Mono<Transaction> findTransactionById(String id){
		return repositories.findByTransactionId(id);
	}
	
	
	public Flux<Transaction> getAllTransaction(String id){
		return repositories.findAll();
	}
}
