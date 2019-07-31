package com.whitehall.esp.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whitehall.esp.microservices.model.BuildingInfo;
import com.whitehall.esp.microservices.repositories.BuildingInfoRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BuildingInfoService 
{
	@Autowired
	private BuildingInfoRepository repository;
	
	 public Mono<BuildingInfo> createBuildingInfo(BuildingInfo building) {
	        return repository.save(building);
	    }
	 
	 public Flux<BuildingInfo> findAll() {
	        return repository.findAll();
	    }
	 
	 public Mono<BuildingInfo> findById(String id) {
	        return repository.findById(id);
	    }
	 
	 public Mono<BuildingInfo>  findByAccountAccountName(String accountName){
	        return repository.findByAccountAccountName(accountName);
	    }
	 public Mono<BuildingInfo>  findByAccountAccountId(String accountId){
	        return repository.findByAccountAccountId(accountId);
	    }
}
