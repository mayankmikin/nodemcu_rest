package com.whitehall.esp.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whitehall.esp.microservices.model.Plan;
import com.whitehall.esp.microservices.repositories.PlanRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PlanService {
	@Autowired
	PlanRepository repository;
	
	public Mono<Plan> findPlanByID(String id){
		return repository.findByPlanId(id);
	}
	
	public Flux<Plan> getAllPlan(){
		return repository.findAll();
	}
	
	public Mono<Plan> addPlan(Plan p){
		return repository.save(p);
	}
	
	public Flux<Plan> addMultiplePlan(Iterable<Plan> p){
		return repository.saveAll(p);
	}
	
	public void deletePlan(Plan p){
		repository.delete(p); 
	}
	
	public void deleteMultiplePlan(Iterable<Plan> p){
		repository.deleteAll(p);
	}
	public Mono<Plan> updatePlan(Plan p){
		return repository.save(p);
	}
	
	public Flux<Plan> updateMultiplePlan(Iterable<Plan> p){
		return repository.saveAll(p);
	}
}
