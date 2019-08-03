package com.whitehall.esp.microservices.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whitehall.esp.microservices.exceptions.EntityNotFoundException;
import com.whitehall.esp.microservices.model.Tenant;
import com.whitehall.esp.microservices.services.TenantService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TenantController {

	//private static final Logger log=LoggerFactory.getLogger(TenantController.class);
	/*
	 * @Autowired private TenantRepository tenantRepo;
	 */
	
	@Autowired
	private TenantService tenantService;
	
	@GetMapping("/tenant/code/{tenant}")
	public Mono<Tenant> findByTenantcode(@PathVariable("tenant") String code) {
		log.info("findByTenant: tenantCode={}", code);
		return tenantService.findByTenantCode(code);
	}

	@GetMapping("/tenant")
	public Flux<Tenant> findAll() {
		log.info("findAll");
		return tenantService.findAll();
	}

	@GetMapping("/tenant/{id}")
	public Mono<Tenant> findById(@PathVariable("id") String id) throws EntityNotFoundException {
		log.info("findById: id={}", id);
		return tenantService.getTenant(id);
	}

	@PostMapping("/tenant")
	public Mono<Tenant> create(@Valid @RequestBody Tenant tenant) {
		return tenantService.createTenant(tenant);
	}
	
	/*
	 * @PutMapping("/tenant/{id}") public Mono<Tenant> create(@Valid @RequestBody
	 * Tenant tenant, @PathVariable String id) { tenant.setTenantId(id); return
	 * tenantService.createTenant(tenant); }
	 */
	
	@GetMapping("/tenant/tenantName/{tenantName}")
	public Mono<Boolean> existsByTenantName(String tenantName)
	{
		return tenantService.existsByTenantName(tenantName);
	}
	
	@PatchMapping("/tenant/{id}")
	public Mono<Tenant> deactivateTenant(@PathVariable String id,@Valid @RequestBody Tenant tenant) throws EntityNotFoundException
	{
		return tenantService.deactivateTenant(id);
	}
	
	@PutMapping("/tenant/{id}")
	public Mono<Tenant> editTenant(@PathVariable String id,@Valid @RequestBody Tenant tenant) throws EntityNotFoundException
	{
		return tenantService.editTenant(tenant);
	}
	
	@DeleteMapping("/tenant/{id}")
	public Mono<Void> deleteId(@PathVariable String id)
	{
		return tenantService.deleteId(id);
	}
}
