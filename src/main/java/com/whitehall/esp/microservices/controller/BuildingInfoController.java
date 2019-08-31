package com.whitehall.esp.microservices.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

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
import com.whitehall.esp.microservices.exceptions.EntityNotFoundException;
import com.whitehall.esp.microservices.model.Account;
import com.whitehall.esp.microservices.model.BuildingInfo;
import com.whitehall.esp.microservices.model.Device;
import com.whitehall.esp.microservices.model.frontend.MoveDeviceIntoRoom;
import com.whitehall.esp.microservices.services.AccountService;
import com.whitehall.esp.microservices.services.BuildingInfoService;
import com.whitehall.esp.microservices.services.DeviceService;
import com.whitehall.esp.microservices.utils.JsonUtils;

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
	private DeviceService deviceService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private JsonUtils mapper;
	
	@PostMapping("/")
	public Mono<BuildingInfo> saveBuildingInfo(@RequestBody BuildingInfo buildingInfo) throws AccountNotFoundException 
	{
		
		Account acc=accountService.findByAccountName(buildingInfo.getAccount().getAccountName()).block();
		BuildingInfo alreadyExisting=buildingInfoService.findByAccountAccountName(acc.getAccountName()).block();
		if(null!=alreadyExisting)
		{
			log.info("building Info already existsing for user: {}",acc.getAccountName());
			buildingInfo.setAccount(acc);
			alreadyExisting=getAllBuildingParamsRight(alreadyExisting,buildingInfo);
			return buildingInfoService.createBuildingInfo(alreadyExisting);
		}
		if(null!=acc)
		{
			buildingInfo.setAccount(acc);
			buildingInfo=getAllBuildingParamsRight(new BuildingInfo(),buildingInfo);
		}
		else
		{
			throw new AccountNotFoundException("Account Not Found with account Name: "+buildingInfo.getAccount().getAccountName());
		}
		return buildingInfoService.createBuildingInfo(buildingInfo);
	}
	
	private BuildingInfo getAllBuildingParamsRight(BuildingInfo b,BuildingInfo buildingInfo) {
		//set account
		
		b.setAccount(buildingInfo.getAccount());
		// set houses
		buildingInfo.getHouses().forEach((h)->{
			h.setHouseId(UUID.randomUUID().toString());
			h.getFloor().forEach((f)->{
				f.setFloorId(UUID.randomUUID().toString());
				f.getRooms().forEach((r)->{
					r.setRoomId(UUID.randomUUID().toString());
					r.setRoomConfigs(new HashMap<String, String>());
					r.setStatus(false);
					r.setDevices(new HashSet<Device>());
					
				});
			});
		});
		b.setHouses(buildingInfo.getHouses());
		return b;
	}

	@GetMapping("/")
	public Flux<BuildingInfo> getAllBuildingInfo() 
	{
		return buildingInfoService.findAll();
	}
	
	@PatchMapping("/{buildingInfoId}")
	public Mono<BuildingInfo> editBuildingInfo(@RequestBody BuildingInfo buildingInfo, @PathVariable String buildingInfoId) throws AccountNotFoundException 
	{
		log.info("editBuildingInfo ");
		log.info(mapper.print(buildingInfo));
		mapper.print(buildingInfo);
		Mono<BuildingInfo> existingBuildinInfo=buildingInfoService.findById(buildingInfoId);
		if(null==existingBuildinInfo)
		{
			throw new BuildingInfoNotFoundException("Building with Id"+buildingInfoId+" not found");
		}
		Mono<Account> acc=accountService.findByAccountName(buildingInfo.getAccount().getAccountName());
		if(null==acc)
		{
			throw new AccountNotFoundException("Account Not Found with account Name: "+buildingInfo.getAccount().getAccountName());
		}
		return buildingInfoService.createBuildingInfo(buildingInfo);
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
	
	@PatchMapping("/moveDevice/operation/{operationName}/accountId/{accountId}")
	public Mono<BuildingInfo> moveDeviceIntoRoom(@RequestBody MoveDeviceIntoRoom devices,@PathVariable String accountId,
			@PathVariable String operationName) 
	{
		log.info("moveDeviceIntoRoom accountId{}",accountId);
		BuildingInfo buildingInfo=buildingInfoService.findById(devices.getBuildingInfoId()).block();
		if(null== buildingInfo)
		{
			throw new BuildingInfoNotFoundException("invalid buildingInfo Id");
		}
		else
		{
			
			if(operationName.equalsIgnoreCase("add"))
			{
				buildingInfo.getHouses().stream()
				.flatMap(h->h.getFloor().stream()
						).anyMatch(f-> f.getRooms().stream()
								.filter(r-> r.getRoomId().equalsIgnoreCase(devices.getRoomId()))
								.anyMatch(p-> 
								
										p.getDevices().addAll(devices.getDeviceList())
									
								)
							);
				
				List<Device>deviceToedit=devices.getDeviceList();
				deviceToedit.forEach(d->{
					d.setOccupied(true);
					try {
						deviceService.editDevice(d).block();
					} catch (EntityNotFoundException e) {
						e.printStackTrace();
					}
				});
			}
			if(operationName.equalsIgnoreCase("remove"))
			{
				buildingInfo.getHouses().stream()
				.flatMap(h->h.getFloor().stream()
						).anyMatch(f-> f.getRooms().stream()
								.filter(r-> r.getRoomId().equalsIgnoreCase(devices.getRoomId()))
								.anyMatch(p-> p.getDevices().removeAll(devices.getDeviceList()))
								);
				List<Device>deviceToedit=devices.getDeviceList();
				deviceToedit.forEach(d->{
					d.setOccupied(false);
					try {
						deviceService.editDevice(d).block();
					} catch (EntityNotFoundException e) {
						e.printStackTrace();
					}
				});
			}
			
		}
	
		return buildingInfoService.createBuildingInfo(buildingInfo);
	}
	

}
