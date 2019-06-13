package com.whitehall.esp.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whitehall.esp.microservices.exceptions.AccountAlreadyExistException;
import com.whitehall.esp.microservices.exceptions.EntityNotFoundException;
import com.whitehall.esp.microservices.model.Tenant;
import com.whitehall.esp.microservices.repositories.TenantRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class TenantService {
	@Autowired
	private TenantRepository repository;
	
	    public Mono<Tenant> getTenant(String tenantId) throws EntityNotFoundException {
	        Mono<Tenant> Tenant = repository.findById(tenantId);
	        if (Tenant == null) {
	            throw new EntityNotFoundException(Tenant.class, "tenantId", tenantId.toString());
	        }
	        return Tenant;
	    }

	    public Mono<Tenant> createTenant(Tenant tenant) {
	    	if(existsByTenantName(tenant.getTenantName()).block())
			{
				throw new AccountAlreadyExistException("Tenant already exists");
			}
	        return repository.save(tenant);
	    }

		public Flux<Tenant> findAll() {
			return repository.findAll();
		}
		
		public Mono<Tenant> findByTenantCode(String tenantCode) {
			return repository.findByCode(tenantCode);
		}
		public Mono<Boolean> existsByTenantName(String tenantName)
		{
			return repository.existsByTenantName(tenantName);
		}
		public Mono<Void> deleteId(String id)
		{
			return repository.deleteById(id);
		}
		public Mono<Tenant> deactivateTenant(String id) throws EntityNotFoundException 
		{

			Tenant tenant=getTenant(id).block();
			tenant.setIsActivated(false);
			return repository.save(tenant);
		}
		
		public Mono<Tenant> editTenant(Tenant tenant) throws EntityNotFoundException 
		{

			return repository.save(tenant);
		}

}
