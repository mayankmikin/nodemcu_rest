package com.whitehall.esp.microservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whitehall.esp.microservices.model.Plan;
import com.whitehall.esp.microservices.services.PlanService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/plan")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class PlanController {
	
	@Autowired
	PlanService planService;
	
	@GetMapping("/")
	Flux<Plan> getAllPlan() {
		return planService.getAllPlan();
	}
	
	@PostMapping("add")
	Mono<Plan> addPlan(@RequestBody Plan plan) {
		log.info("addPlan plan:{}",plan);
		return planService.addPlan(plan);
	}
	
	@GetMapping("byplanid/{planId}")
	Mono<Plan> getPlanById(@PathVariable String planId) {
		return planService.findPlanByID(planId);
	}
}
