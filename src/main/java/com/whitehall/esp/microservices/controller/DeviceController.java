package com.whitehall.esp.microservices.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitehall.esp.microservices.config.MQTTConfig;
//import com.whitehall.esp.microservices.config.MqttOutboundConfig;
import com.whitehall.esp.microservices.exceptions.EntityNotFoundException;
import com.whitehall.esp.microservices.model.Device;
import com.whitehall.esp.microservices.model.DeviceStatusChangeRequest;
import com.whitehall.esp.microservices.model.User;
import com.whitehall.esp.microservices.services.DeviceService;
import com.whitehall.esp.microservices.services.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/device")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DeviceController extends GenericController{

	//private static final Logger log=LoggerFactory.getLogger(DeviceController.class);
	/*
	 * @Autowired private DeviceRepository DeviceRepo;
	 */
	public static ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private IMqttClient mqttPublisher;
	
	@Autowired
	private IMqttClient subscriber;
	
	
	@Autowired
	private UserService userService;
	/*
	 * @GetMapping("/code/{Device}") public Mono<Device>
	 * findByDevicecode(@PathVariable("Device") String code) {
	 * log.info("findByDevice: DeviceCode={}", code); return
	 * deviceService.findByDeviceCode(code); }
	 */

	
	@GetMapping("")
	public Flux<Device> findAll() {
		log.info("findAll");
		return deviceService.findAll();
	}
	@GetMapping("/account/{accountId}")
	public Flux<Device> findAllWithAccounts(@PathVariable String accountId) {
		log.info("findAllWithAccount id : {}",accountId);
		return deviceService.findAllWithAccounts(accountId);
	}

	@GetMapping("/{id}")
	public Mono<Device> findById(@PathVariable("id") String id) throws EntityNotFoundException {
		log.info("findById: id={}", id);
		return deviceService.getDevice(id);
	}

	@PostMapping("")
	public Mono<Device> create(@Valid @RequestBody Device Device) throws JsonProcessingException, EntityNotFoundException {
		log.info("create device : {}",mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Device));
		return deviceService.createDevice(Device);
	}
	
	@GetMapping("/serialId/{serialId}")
	public Mono <Device> findBySerialId(@PathVariable String serialId) {
		return deviceService.findBySerialId(serialId);
	}
	@GetMapping("/exporterId/{exporterId}")
	public Flux<Device> findByExporterId(@PathVariable String exporterId)
	 {
		 return deviceService.findByExporterId(exporterId);
	 }
	@GetMapping("/manufacturerId/{manufacturerId}")
	public  Flux<Device> findByManufacturerId(@PathVariable String manufacturerId)
	{
		log.info("manufacturerId : {}",manufacturerId);
		return deviceService.findByManufacturerId(manufacturerId);
	}
	
	@PatchMapping("/{serialId}/account/{accountId}")
	public Mono<Device> changeAccountOfaDevice(@PathVariable String serialId,@PathVariable String accountId)throws EntityNotFoundException {
		log.info("changeAccountOfaDevice :deviceId= {},accountId: {} ",serialId,accountId);
		
		return deviceService.changeAccountOfaDevice(serialId, accountId);
	}
	
	@PatchMapping("")
	public Mono<Device> editDevice(@Valid @RequestBody Device Device)throws EntityNotFoundException {
		log.info("editDevice :deviceId= {},accountId: {} ",Device.getDeviceId(),Device.getAccount().getAccountId());
		
		return deviceService.editDevice(Device);
	}
	
	@PatchMapping("/mqtt")
	public ResponseEntity sentToMqtt(@RequestBody DeviceStatusChangeRequest deviceRequest) throws MqttPersistenceException, MqttException {
		String userEmail = getEmailFromToken();
		User user = userService.findByEmail(userEmail).block();
		
		Device device =  deviceService.findBySerialId(deviceRequest.getDeviceId()).block();
		
		if(!device.getAccount().equals(user.getAccount()))
			return new ResponseEntity("You dont have access of this device", HttpStatus.UNAUTHORIZED);
		
		String topic = "device/"+deviceRequest.getDeviceId();
		String msg = deviceRequest.getAction()+"-"+deviceRequest.getPort();
		log.info("Topic={}, msg={}",topic,msg);
		mqttPublisher.publish(topic, new MqttMessage(msg.getBytes()));	
		
		return new ResponseEntity(HttpStatus.OK);
	}
	@PatchMapping("/mqtt/publish")
	public ResponseEntity publish(@RequestBody String deviceId) throws MqttPersistenceException, MqttException
	{
		log.info("device id is: {}",deviceId);
		String publishTopicName = "device/"+deviceId;
		mqttPublisher.publish(publishTopicName, new MqttMessage("hello".getBytes()));
		return new ResponseEntity(HttpStatus.OK);
	}
	@PatchMapping("/mqtt/status")
	public ResponseEntity<List<String>> getDeviceStatus(@RequestBody String deviceId) throws MqttPersistenceException, MqttException, InterruptedException {
		String userEmail = getEmailFromToken();
		User user = userService.findByEmail(userEmail).block();
		String payloadRespone="";
		log.info("device id is: {}",deviceId);
		Device device =  deviceService.findBySerialId(deviceId).block();
		
//		if(!device.getAccount().equals(user.getAccount()))
//			return new ResponseEntity("You dont have access of this device", HttpStatus.UNAUTHORIZED);
		
		String publishTopicName = "device/"+deviceId;
		String subsribeTopicName = "device/"+deviceId+"/in";
		
		CountDownLatch receivedSignal = new CountDownLatch(10);
		List<String> responsemessage=new ArrayList<String>();
		subscriber.subscribe(subsribeTopicName, (topic, msg) -> {
		    byte[] payload = msg.getPayload();
		    // ... payload handling omitted
		    log.info("message recived from topic is:{}",msg.toString());
		    responsemessage.add(msg.toString());
		    receivedSignal.countDown();
		   
		});  
		mqttPublisher.publish(publishTopicName, new MqttMessage("3".toString().getBytes()));
		receivedSignal.await(3, TimeUnit.SECONDS);
		return new ResponseEntity<List<String>>(responsemessage,HttpStatus.OK);
	}
//	@FunctionalInterface
//	interface  IFunc{
//	    String print();
//	}
}
