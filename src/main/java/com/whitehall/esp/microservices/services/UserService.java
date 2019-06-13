package com.whitehall.esp.microservices.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.whitehall.esp.microservices.config.JwtTokenProvider;
import com.whitehall.esp.microservices.exceptions.CustomException;
import com.whitehall.esp.microservices.exceptions.EntityNotFoundException;
import com.whitehall.esp.microservices.exceptions.UserAlreadyExistException;
import com.whitehall.esp.microservices.model.Account;
import com.whitehall.esp.microservices.model.Role;
import com.whitehall.esp.microservices.model.User;
import com.whitehall.esp.microservices.repositories.RoleRepository;
import com.whitehall.esp.microservices.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
@Slf4j
public class UserService {
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepo;
	
//	@Autowired
//	private WebClient.Builder webClientBuilder;
	@Autowired
	  private JwtTokenProvider jwtTokenProvider;

	  @Autowired
	  private AuthenticationManager authenticationManager;
	  
	  @Autowired
	  private PasswordEncoder passwordEncoder;
	  
//	@Value("${account.service.uri}")
//	private String accountServiceUri;
	
	    public Mono<User> getUser(String userId) throws EntityNotFoundException {
	        Mono<User> user = repository.findById(userId);
	        if (user == null) {
	            throw new EntityNotFoundException(User.class, "UserId", userId.toString());
	        }
	        return user;
	    }

	    public Mono<User> createUser(User User)throws UserAlreadyExistException {
	    	if(existsByEmailId(User.getEmail()).block())
			{
				throw new UserAlreadyExistException("User already exists with this email Id: "+User.getEmail());
			}
	    	
	    	if(existsByPhone(User.getPhone()).block())
			{
				throw new UserAlreadyExistException("User already exists with this phone Id: "+User.getPhone());
			}
	    	//Account account= findByAccountID(User.getAccount().getAccountId()).block();
	    	//User.setAccount(account);
	    	User.setConfirmationToken(UUID.randomUUID().toString());
	    	User.setPassword(passwordEncoder.encode(User.getPassword()));
	    	if(User.getRoles().isEmpty())
	    	{
	    		List<Role>roles=(List<Role>)roleRepo.findAll().blockLast();
	    		roles.forEach(r->{
	    			if(r.getRoleName().equalsIgnoreCase("ROLE_USER_VIEW"))
	    			{
	    				User.getRoles().add(r);
	    			}
	    			
	    		});
	    	}
	        return repository.save(User);
	    }
	    public Mono<User> updateUser(User User)throws UserAlreadyExistException {
	        return repository.save(User);
	    }
	    public Mono<User> verifyUser(User User)throws UserAlreadyExistException {
	    	User.setEnabled(true);
	        return repository.save(User);
	    }

		public Flux<User> findAll() {
			return repository.findAll();
		}
		
		public Mono<User> findByPhone(String phone)
		{
			return repository.findByPhone(phone);
		}
		
		public Mono<User> findByEmail(String email)
		{
			return repository.findByEmail(email);
		}
		public Mono<User> findByConfirmationToken(String confirmationToken)
		{
			return repository.findByConfirmationToken(confirmationToken);
		}
		public Flux<User> findByAccountAccountId(String accountId) {
			return repository.findByAccountAccountId(accountId);
			//return repository.findAll();
		}
		public Mono<Boolean> existsByEmailId(String email)
		{
			return  repository.existsByEmail(email);
		}
		public Mono<Boolean> existsByPhone(String phone)
		{
			return  repository.existsByPhone(phone);
		}
//		public Flux<Account> findByAccounts() {
//		    log.info("findByAccounts");
//		    String url=accountServiceUri+"/api/v1/account";
//		    log.info("url: "+url);
//		    Flux<Account> accounts = webClientBuilder.build().get().uri(url)
//		    		.retrieve()
//		    		.bodyToFlux(Account.class);
//		    return accounts;
//		          
//		}
//		public Mono<Account> findByAccountID(String accountId) {
//		    log.info("findByAccountID: id={}",accountId);
//		    String url=accountServiceUri+"/api/v1/account/"+accountId;
//		    log.info("url: "+url);
//		    Mono<Account> accounts = a
//		    return accounts;
//		          
//		}
//		
//		public Mono<Account> createAccount(Account account) {
//		    log.info("creating Account from User Service");	
//		    WebClient webClient = webClientBuilder
//		            //.baseUrl("http://account-service/api/v1")
//		           // .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.github.v3+json")
//		            //.defaultHeader(HttpHeaders.USER_AGENT, "Spring 5 WebClient")
//		            .build();
//		    String url=accountServiceUri+"/api/v1/account";
//		    log.info("url: "+url);
//		    Mono<Account> createdAccount = webClient
//		    		.post()
//		    		.uri(url)
//		    		.contentType(MediaType.APPLICATION_JSON)
//		    		.syncBody(account)
//		    		//.body(BodyInserters.fromObject(account))
//		    		.retrieve()
//		    		.bodyToMono(Account.class);
//		    return createdAccount;
//		          
//		}
//		
		public String signin(String email, String password) {
		    try {
		      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		      User u=repository.findByEmail(email).block();
		      u.setPassword("");
		      return jwtTokenProvider.createToken(email,u);
		    } catch (AuthenticationException e) {
		      throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
		    }
		  }
		
		public Flux<User> findUsersByRegexpName(String regexp) {
			return repository.findUsersByRegexpName(regexp);
		}
		
		
}
