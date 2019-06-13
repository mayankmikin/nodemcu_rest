package com.whitehall.esp.microservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitehall.esp.microservices.model.Role;
import com.whitehall.esp.microservices.services.RolesService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class RoleController {
	
	
	@Autowired
	private RolesService roleService;
	
	public static ObjectMapper mapper = new ObjectMapper();
	
	@PostMapping("/role")
	public Mono<Role> createRole(@RequestBody Role role)
	{
		
		return roleService.create(role);
	}
	
	@GetMapping("/role")
	public Flux<Role> getRoles()
	{
		
		return roleService.findAll();
	}

}
