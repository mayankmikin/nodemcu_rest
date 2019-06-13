package com.whitehall.esp.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.whitehall.esp.microservices.exceptions.RoleNotFoundException;
import com.whitehall.esp.microservices.model.Account;
import com.whitehall.esp.microservices.model.Device;
import com.whitehall.esp.microservices.model.Groups;
import com.whitehall.esp.microservices.model.Role;
import com.whitehall.esp.microservices.repositories.GroupsRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GroupsService {
	
	@Autowired
	private GroupsRepository repository;
//	
//	@Autowired
//	private WebClient.Builder webClientBuilder;
//	
//	@Value("${device.service.uri}")
//	private String deviceServiceUri;
	
	public Flux<Groups> findAll()
	{
		return repository.findAll();
	}
	
	public Mono<Groups> create(Groups entity)
	{
		
	
		return repository.save(entity);
	}
	
	/*
	 * public Mono<Device> findByserialId(String serialId) {
	 * log.info("findByserialId: id={}",serialId); String
	 * url=deviceServiceUri+"/api/v1/device/serialId/"+serialId;
	 * log.info("url: "+url); Mono<Device> devices =
	 * webClientBuilder.build().get().uri(url) .retrieve()
	 * .bodyToMono(Device.class); return devices;
	 * 
	 * }
	 */
	public Mono<Groups> findByGroupId(String groupId)
	{
		
	
		return repository.findByGroupId(groupId);
	}
	
	public Mono<Groups> editGroup(Groups entity)
	{
		
		return repository.save(entity);
	}
	public Mono<Void> deleteGroup(Groups entity)
	{

		return repository.delete(entity);
	}
}
