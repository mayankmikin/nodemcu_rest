package com.dominion.nodemcu.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dominion.nodemcu.entity.Account;
import com.dominion.nodemcu.entity.Device;
import com.dominion.nodemcu.model.DeviceRequest;
import com.dominion.nodemcu.repository.AccountRepository;
import com.dominion.nodemcu.repository.DeviceRepository;
import com.dominion.nodemcu.utils.WebUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/internal")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DeviceDataController 
{	
	private static final Logger log = LoggerFactory.getLogger(DeviceDataController.class);
	 @Autowired
	 WebUtils utils;
	 @Autowired
	 DeviceRepository deviceRepo;
	 @Autowired
	 AccountRepository accountRepository;
	 
	 public static ObjectMapper mapper = new ObjectMapper();
	 @PostMapping("/espdata")
		public ResponseEntity<?> getidaddr(@RequestBody DeviceRequest datarequest,HttpServletRequest request) throws JsonProcessingException
		{
		 
		 log.debug("ipaddress is"+datarequest.getLocalIp());
			 log.debug("serial id is"+datarequest.getSerialId());
			 log.debug("external ip is"+request.getRemoteAddr());
			 log.debug("host is"+request.getRemoteHost());
			 utils.setRequest(request);
			 log.debug(utils.getClientIp());
			 log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(datarequest));
			 return new ResponseEntity<String>("",HttpStatus.OK);
		}
	 
		@RequestMapping("/serialid/{id}")
	    public ResponseEntity<Device> findDeviceBySerialId(@PathVariable(value = "id") String serialId){
			
     	   	log.info("findDeviceBySerialId called with serial id {}",serialId);
	           Device response=deviceRepo.findBySerialId(serialId).get();
	           
	           if(null!=response)
	           {
	        	   log.info("device found {}",response);
	           }
	           
	        return new ResponseEntity<Device>(response,HttpStatus.OK);
	    }
		  @PostMapping("/addDeviceInAccountByDeviceId/{id}/account/{accountid}")
		  @Transactional  
		  public ResponseEntity<Account> addDeviceInAccountByDeviceId(@PathVariable(value = "id") String serialId,@PathVariable(value = "accountid") String accountid){
	            
	                log.info("findDeviceBySerialId called with serial id {}",serialId);
	               Device response=deviceRepo.findBySerialId(serialId).get();
	               
	               if(null!=response)
	               {
	                   log.info("device found {}",response);
	                   
	               }
	               Account account =accountRepository.findById(Long.valueOf(accountid)).get();
	               if(null!=account)
	               {
	                   log.info("device found {} adding device into account{}",account);
	                /*   Set<Device> devices= new HashSet<>(account.getDevices());
	                   devices.add(response);
	                   account.setDevices(devices);*/
	                   account.addDevices(response);
	                   account=accountRepository.save(account);
	                   response.setAccount(account);
	                   deviceRepo.save(response);
	                   
	                  
	               }
	            return new ResponseEntity<Account>(account,HttpStatus.OK);
	        }
}
