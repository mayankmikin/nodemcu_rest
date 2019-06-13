package com.whitehall.esp.microservices.services;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.whitehall.esp.microservices.exceptions.RoleNotFoundException;
import com.whitehall.esp.microservices.model.Role;
import com.whitehall.esp.microservices.repositories.RoleRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RolesService {
	
	@Autowired
	private RoleRepository repository;
	@Value("${roles_list}")
	private String role_conf;
	
	private List<String> rolesList;
	

	public Flux<Role> findAll()
	{
		return repository.findAll();
	}
	
	public Mono<Role> create(Role entity)
	{
		if(!rolesList.stream().anyMatch(x-> x.equalsIgnoreCase(entity.getRoleName())))
		{
			log.error("role name does not exists");
			throw new RoleNotFoundException("role name does not exists");
		}
		return repository.save(entity);
	}
	
	@PostConstruct
	private void initialize()
	{
		rolesList=Arrays.stream(role_conf.split(",")).map(e->e.trim()).collect(Collectors.toList());
	}
	
	public Mono<Role> findByRoleName(String roleName)
	{
		return repository.findByRoleName(roleName);
	}
}
