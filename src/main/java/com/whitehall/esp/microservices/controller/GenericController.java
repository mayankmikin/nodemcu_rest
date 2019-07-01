package com.whitehall.esp.microservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.whitehall.esp.microservices.config.IAuthenticationFacade;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class GenericController {
//	@Autowired
//	private ExternalConfig externalConfig;
////	@PostMapping("/Tenant/add")
////	public <Mono>
	 @Autowired
	public IAuthenticationFacade authenticationFacade;
//	 
//	@PatchMapping("/tenant/update/{tenantName}") 
//	public Flux<TenantInfo> tenantUpdate(@PathVariable String tenantName) throws JsonProcessingException{
//		log.info("inside tenantUpdate, tenantId={}",tenantName);
//		return externalConfig.fallback(tenantName);
//	}
	
	public String getEmailFromToken()
	{
		Authentication authentication = authenticationFacade.getAuthentication();
		return authentication.getName();
	}
}
