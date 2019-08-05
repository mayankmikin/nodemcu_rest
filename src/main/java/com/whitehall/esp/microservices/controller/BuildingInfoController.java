package com.whitehall.esp.microservices.controller;

import java.util.stream.Collectors;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whitehall.esp.microservices.exceptions.BuildingInfoNotFoundException;
import com.whitehall.esp.microservices.model.Account;
import com.whitehall.esp.microservices.model.BuildingInfo;
import com.whitehall.esp.microservices.model.frontend.MoveDeviceIntoRoom;
import com.whitehall.esp.microservices.services.AccountService;
import com.whitehall.esp.microservices.services.BuildingInfoService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/buildingInfo")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class BuildingInfoController 
{
	@Autowired
	private BuildingInfoService buildingInfoService;
	
	@Autowired
	private AccountService accountService;
	
	@PostMapping("/")
	public Mono<BuildingInfo> saveBuildingInfo(@RequestBody BuildingInfo buildingInfo) throws AccountNotFoundException 
	{
		
		Mono<Account> acc=accountService.findByAccountName(buildingInfo.getAccount().getAccountName());
		if(null!=acc)
		{
			buildingInfo.setAccount(acc.block());
		}
		else
		{
			throw new AccountNotFoundException("Account Not Found with account Name: "+buildingInfo.getAccount().getAccountName());
		}
		return buildingInfoService.createBuildingInfo(buildingInfo);
	}
	
	@GetMapping("/")
	public Flux<BuildingInfo> getAllBuildingInfo() 
	{
		return buildingInfoService.findAll();
	}
	
	@GetMapping("/{id}")
	public Mono<BuildingInfo> getBuildingInfo(@PathVariable String id) 
	{
		return buildingInfoService.findById(id);
	}
	
	@GetMapping("/accountName/{accountName}")
	public Mono<BuildingInfo> getBuildingInfoByAccountAccountName(@PathVariable String accountName) 
	{
		log.info("getBuildingInfoByAccountAccountName {}",accountName);
		return buildingInfoService.findByAccountAccountName(accountName);
	}
	
	@GetMapping("/accountId/{accountId}")
	public Mono<BuildingInfo> getBuildingInfoByAccountAccountId(@PathVariable String accountId) 
	{
		log.info("getBuildingInfoByAccountAccountId {}",accountId);
		return buildingInfoService.findByAccountAccountId(accountId);
	}
	
	@PatchMapping("/moveDevice/accountId/{accountId}")
	public Mono<BuildingInfo> moveDeviceIntoRoom(@RequestBody MoveDeviceIntoRoom devices,@PathVariable String accountId) 
	{
		log.info("moveDeviceIntoRoom accountId{}",accountId);
		BuildingInfo buildingInfo=buildingInfoService.findById(devices.getBuildingInfoId()).block();
		if(null== buildingInfo)
		{
			throw new BuildingInfoNotFoundException("invalid buildingInfo Id");
		}
		else
		{
			buildingInfo.getHouses().stream()
			.flatMap(h->h.getFloor().stream()
					).anyMatch(f-> f.getRooms().stream()
							.filter(r-> r.getRoomId().equalsIgnoreCase(devices.getRoomId()))
							.anyMatch(p-> p.getDevices().addAll(devices.getDeviceList()))
							);
			
		}
	
		return buildingInfoService.createBuildingInfo(buildingInfo);
	}
	

}
