package com.whitehall.esp.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whitehall.esp.microservices.exceptions.EntityNotFoundException;
import com.whitehall.esp.microservices.model.Account;
import com.whitehall.esp.microservices.repositories.AccountRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class AccountService {
	@Autowired
	private AccountRepository repository;
	
	    public Mono<Account> getAccount(String id) throws EntityNotFoundException {
	        Mono<Account> device = repository.findById(id);
	        if (device == null) {
	            throw new EntityNotFoundException(Account.class, "AccountId", id.toString());
	        }
	        return device;
	    }

	    public Mono<Account> createAccount(Account Account) {
	        return repository.save(Account);
	    }

		public Flux<Account> findAll() {
			return repository.findAll();
		}
		
		public Mono<Boolean> existsByAccountName(String accountName)
		{
			return repository.existsByAccountName(accountName);
		}
		
		public Mono<Account> findByAccountName(String accountName) {
	        return repository.findByAccountName(accountName);
	    }

}
