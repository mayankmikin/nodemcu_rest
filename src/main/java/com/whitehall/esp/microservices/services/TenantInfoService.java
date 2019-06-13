package com.whitehall.esp.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whitehall.esp.microservices.model.TenantInfo;
import com.whitehall.esp.microservices.repositories.TenantInfoRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TenantInfoService {
	@Autowired
	private TenantInfoRepository repository;
	
	public Flux<TenantInfo> getAllTenantInfo(){
		return repository.findAll();
	}
	
	public Mono<TenantInfo> findByTenantName(String tenantName ){
		
		return repository.findByTenantName(tenantName);
	}
}
