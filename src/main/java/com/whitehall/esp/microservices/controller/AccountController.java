package com.whitehall.esp.microservices.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whitehall.esp.microservices.exceptions.AccountAlreadyExistException;
import com.whitehall.esp.microservices.exceptions.EntityNotFoundException;
import com.whitehall.esp.microservices.model.Account;
import com.whitehall.esp.microservices.services.AccountService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {

	//private static final Logger log=LoggerFactory.getLogger(AccountController.class);
	/*
	 * @Autowired private AccountRepository AccountRepo;
	 */
//	@Autowired
//	private DiscoveryClient discoveryClient;
	
	@Autowired
	private AccountService accountService;
	
	/*
	 * @GetMapping("/account/code/{Account}") public Mono<Account>
	 * findByAccountcode(@PathVariable("Account") String code) {
	 * log.info("findByAccount: AccountCode={}", code); return
	 * accountService.findByAccountCode(code); }
	 */

//	@GetMapping("/service-instances/{applicationName}")
//	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName)
//	{
//		return this.discoveryClient.getInstances(applicationName);
//	}
	
	
	@GetMapping("/account")
	public Flux<Account> findAll() {
		log.info("findAll");
		return accountService.findAll();
	}

	@GetMapping("/account/{id}")
	public Mono<Account> findById(@PathVariable("id") String id) throws EntityNotFoundException {
		log.info("findById: id={}", id);
		return accountService.getAccount(id);
	}

	@PostMapping("/account")
	public Mono<Account> create(@Valid @RequestBody Account account) {
		
		if(existsByAccountName(account.getAccountName())
				.block())
		{
			throw new AccountAlreadyExistException("Account already exists");
		}
		Account acc= new Account();
		acc.setAccountName(account.getAccountName());
		return accountService.createAccount(acc);
	}
	
	public Mono<Boolean> existsByAccountName(String accountName)
	{
		return accountService.existsByAccountName(accountName);
	}
	
	@GetMapping("/account/AccountName/{accountName}")
	public Mono<Account> findByAccountName(@PathVariable String accountName) {
        return accountService.findByAccountName(accountName);
    }
}
