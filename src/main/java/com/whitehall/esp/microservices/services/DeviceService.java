package com.whitehall.esp.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.whitehall.esp.microservices.exceptions.EntityNotFoundException;
import com.whitehall.esp.microservices.model.Account;
import com.whitehall.esp.microservices.model.Device;
import com.whitehall.esp.microservices.repositories.DeviceRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@Slf4j
public class DeviceService {
	@Autowired
	private DeviceRepository repository;
	@Autowired
	private AccountService accountService;
	/*
	 * @Autowired private WebClient.Builder webClientBuilder;
	 * 
	 * @Value("${account.service.uri}") private String accountServiceUri;
	 */
	
	    public Mono<Device> getDevice(String deviceId) throws EntityNotFoundException {
	        Mono<Device> device = repository.findById(deviceId);
	        if (device == null) {
	            throw new EntityNotFoundException(Device.class, "DeviceId", deviceId.toString());
	        }
	        return device;
	    }

	    public Mono<Device> createDevice(Device Device) throws EntityNotFoundException {
	    	boolean exists=false;
	    	Device device=findBySerialId(Device.getSerialId()).block();
	    	if(null!=device)
			{
	    		//Device.setExternalIp(device.getExternalIp());
	    		//Device.setLocalIp(device.getLocalIp());
	    		exists=true;
	    		device.setLocalIp(Device.getLocalIp());
	    		device.setExternalIp(Device.getExternalIp());
	    		device.setUserDefinedName(Device.getUserDefinedName());
	    		log.info("updating existing device : {}",device);
	    		
			}
	    	else
	    	{
	    		log.info("creating a new  device : {}",Device);
	    		Account account=accountService.getAccount(Device.getAccount().getAccountId()).block();
	    		Device.setAccount(account);
	    	}
	        return exists?repository.save(device):repository.save(Device);
	    }

		public Flux<Device> findAll() {
			return repository.findAll();
		}
		public Flux<Device> findAllWithAccounts(String accountId) {
			return repository.findByAccountAccountId(accountId);
			//return repository.findAll();
		}
		
		public Mono <Device> findBySerialId(String serialId) {
			return repository.findBySerialId(serialId);
		}
		
		 public Flux<Device> findByExporterId(String exporterId)
		 {
			 return repository.findByExporterId(exporterId);
		 }
		public  Flux<Device> findByManufacturerId(String manufacturerId)
		{
			return repository.findByManufacturerId(manufacturerId);
		}

		public Mono<Device> changeAccountOfaDevice(String serialId, String accountId)throws EntityNotFoundException {
			 	Device device = repository.findBySerialId(serialId).block();
		        if (device == null) {
		            throw new EntityNotFoundException(Device.class, "serialId", serialId.toString());
		        }
		        device.setAccount(new Account(accountId));
			return repository.save(device);
		}
		public Mono<Device> editDevice(Device Device)throws EntityNotFoundException {
		 	Device device = repository.findBySerialId(Device.getSerialId()).block();
	        if (device == null) {
	            throw new EntityNotFoundException(Device.class, "serialId", Device.getSerialId());
	        }
	        device.setUserDefinedName(Device.getUserDefinedName());
		return repository.save(device);
	}
		
		public Mono<Boolean> existsDevice(String serialId)
		{
			return repository.existsBySerialId(serialId);
		}
		
	/*
	 * public Mono<Account> findByAccountID(String accountId) {
	 * log.info("findByAccountID: id={}",accountId); String
	 * url=accountServiceUri+"/api/v1/account/"+accountId; log.info("url: "+url);
	 * Mono<Account> accounts = webClientBuilder.build().get().uri(url) .retrieve()
	 * .bodyToMono(Account.class); return accounts;
	 * 
	 * }
	 */
}
