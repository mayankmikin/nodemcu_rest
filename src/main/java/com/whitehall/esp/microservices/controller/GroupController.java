package com.whitehall.esp.microservices.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitehall.esp.microservices.exceptions.CustomException;
import com.whitehall.esp.microservices.exceptions.DeviceAlreadyExistException;
import com.whitehall.esp.microservices.exceptions.DeviceNotFoundException;
import com.whitehall.esp.microservices.exceptions.EntityNotFoundException;
import com.whitehall.esp.microservices.exceptions.GroupCreationLimitExceedsException;
import com.whitehall.esp.microservices.exceptions.GroupNotFoundException;
import com.whitehall.esp.microservices.exceptions.UserAlreadyExistException;
import com.whitehall.esp.microservices.exceptions.UserNotFoundException;
import com.whitehall.esp.microservices.model.Device;
import com.whitehall.esp.microservices.model.Groups;
import com.whitehall.esp.microservices.model.MultiDevices;
import com.whitehall.esp.microservices.model.MultipleUsers;
import com.whitehall.esp.microservices.model.NotificationType;
import com.whitehall.esp.microservices.model.Notifications;
import com.whitehall.esp.microservices.model.User;
import com.whitehall.esp.microservices.services.DeviceService;
import com.whitehall.esp.microservices.services.GroupsService;
import com.whitehall.esp.microservices.services.UserService;
import com.whitehall.esp.microservices.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/group")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class GroupController extends GenericController {

	@Autowired
	GroupsService groupService;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private JsonUtils mapper;
	
	@Autowired
	private ObjectMapper objectmapper;
	
	
	@PostMapping("/")
	public Groups create(@Valid @RequestBody Groups group) {
		
		String email = getEmailFromToken();
		User u= userService.findByEmail(email).block();
		if(null==u)
		{
			throw new UserNotFoundException("User Not Found");
		}
		
		if(u.getGroups().size()>=3)
		{
			throw new GroupCreationLimitExceedsException("You can create only upto 3 groups");
		}
		Groups createdGroup=groupService.create(group).block();
		u.getGroups().add(createdGroup);
		userService.updateUser(u).block();
		return createdGroup;
	}
	
	@DeleteMapping("/delete/{groupId}")
	public User delete(@PathVariable String groupId) {
		String email = getEmailFromToken();
		log.info("delete mapping for email: {} , group Id: {}",email,groupId);
		User u= userService.findByEmail(email).block();
		if(null==u)
		{
			throw new UserNotFoundException("User Not Found");
		}
		
		Groups grp = groupService.findByGroupId(groupId).block();
		if(null==grp)
		{
			throw new GroupNotFoundException("Group Not Found");
		}
		
		groupService.deleteGroup(grp).block();
		u.getGroups().remove(grp);
		userService.updateUser(u).block();
		return u;
	}

	
	@GetMapping("/")
	public Flux<Groups> getAll(Groups group) {
		
		return groupService.findAll();
	}
	
	@GetMapping("/owned")
	public List<Groups> getAllOwnedGroupsOfAUser() {
		String email = getEmailFromToken();
		User u= userService.findByEmail(email).block();
		
		return u.getGroups();
	}
	@GetMapping("/shared")
	public List<Groups> getAllSharedGroupsOfAUser() {
		
		String email = getEmailFromToken();
		User u= userService.findByEmail(email).block();
		List<Groups> shrGrp = new ArrayList<Groups>();
		u.getShared_groups_id().forEach(e->{
			shrGrp.add(groupService.findByGroupId(e).block());
		});
		log.info("getAllSharedGroupsOfAUser");
		log.info(mapper.print(shrGrp));
		return shrGrp;
	}
	@PatchMapping("/add/user/{groupId}/{userToAddEmail}")
	public Mono<Groups> addUser(@PathVariable String groupId,@PathVariable String userToAddEmail) throws JsonProcessingException	{
		
		log.info("addUsers=> groupId:{}, userToAddEmail: {}",groupId,userToAddEmail);

		String email = getEmailFromToken();
		User owner = userService.findByEmail(email).block();
		if(null==owner)
		{
			throw new UserNotFoundException("User with email: "+email+" doesnot exist");  
		}
		
		Groups grp=groupService.findByGroupId(groupId).block();
		if(grp==null) {
			throw new GroupNotFoundException("Group with group id"+groupId+" doesn't exist");
		}
		
		if(!owner.getGroups().contains(grp)) {
			throw new CustomException("Given user is not authorised", HttpStatus.FORBIDDEN);
		}
		User userToAdd=userService.findByEmail(userToAddEmail).block();
		if(null==userToAdd)
		{
			throw new UserNotFoundException("User with email: "+userToAddEmail+" doesnot exist");  
		}
		
		List<User> uList = grp.getUsers();
		if(uList.contains(userToAdd)) {
			throw new UserAlreadyExistException("User with email: "+userToAddEmail+" already exist");
		}
		// if contact is not already added to group
		uList.add(userToAdd);
		userToAdd.getShared_groups_id().add(groupId);
		
		Notifications notification=new Notifications();
		notification.setNotification_content(userToAdd.getFirstname()+" has been added to the group");
		notification.setNotification_type(NotificationType.ADDED_TO_GROUP);
		notification.setStatus("unread");
		grp.getNotifications().add(notification);
		return userService.updateUser(userToAdd).then(groupService.editGroup(grp));			
	}
	
	@PatchMapping("/add/users/{groupId}")
	public List<String> addMultipleUsers(@PathVariable String groupId,@RequestBody MultipleUsers users) throws JsonProcessingException{

		String email = getEmailFromToken();
		User owner = userService.findByEmail(email).block();
		if(null==owner)
		{
			throw new UserNotFoundException("User with email: "+email+" doesnot exist");  
		}
		
		Groups grp=groupService.findByGroupId(groupId).block();
		if(grp==null) {
			throw new GroupNotFoundException("Group with group id"+groupId+" doesn't exist");
		}
		
		if(!owner.getGroups().contains(grp)) {
			throw new CustomException("Given user is not authorised", HttpStatus.FORBIDDEN);
		}
				
		if(users.getUsersEmail().isEmpty()){
			throw new CustomException("Users To add Cannot be empty", HttpStatus.NOT_ACCEPTABLE);
		}

		List<User> UsersInGroup = grp.getUsers();	
		List<String> InCorrectUserEmail =  new ArrayList<String>();
		List<String> contactsEmail = (List<String>) owner.getContacts_email();
		for(String userToAddEmail : users.getUsersEmail()) {
			User userToAdd=userService.findByEmail(userToAddEmail).block();
			
			if(!(userToAdd==null||UsersInGroup.contains(userToAdd))&&contactsEmail.contains(userToAddEmail)) {
				UsersInGroup.add(userToAdd);
				Notifications notification=new Notifications();
				notification.setNotification_content(userToAdd.getFirstname()+" has been added to the group");
				notification.setNotification_type(NotificationType.ADDED_TO_GROUP);
				notification.setStatus("unread");
				grp.getNotifications().add(notification);
				userToAdd.getShared_groups_id().add(groupId);
				userService.updateUser(userToAdd);		
			}
			else
				InCorrectUserEmail.add(userToAddEmail);			
		}

		groupService.editGroup(grp);
		return InCorrectUserEmail;
	}
	
	@PatchMapping("/remove/user/{groupId}/{userToRemoveEmail}")
	public Mono<Groups> removeUser(@PathVariable String groupId,@PathVariable String userToRemoveEmail){
		
		log.info("removeUsers=> groupId:{}, userToAddEmail: {}",groupId,userToRemoveEmail);
		
		String email = getEmailFromToken();
		User owner = userService.findByEmail(email).block();
		if(null==owner)
		{
			throw new UserNotFoundException("User with email: "+email+" doesnot exist");  
		}
		
		Groups grp=groupService.findByGroupId(groupId).block();
		if(grp==null) {
			throw new GroupNotFoundException("Group with group id"+groupId+" doesn't exist");
		}
		
		if(!owner.getGroups().contains(grp)) {
			throw new CustomException("Given user is not authorised", HttpStatus.FORBIDDEN);
		}
		
		User userToRemove=userService.findByEmail(userToRemoveEmail).block();		
		if(null== userToRemove)
		{
			throw new UserNotFoundException("User with email: "+userToRemove+" doesnot exist");  
		}
		grp.getUsers().remove(userToRemove);
		
		Notifications notification=new Notifications();
		notification.setNotification_content(userToRemove.getFirstname()+" has been removed from the group");
		notification.setNotification_type(NotificationType.REMOVED_TO_GROUP);
		notification.setStatus("unread");
		grp.getNotifications().add(notification);
		
		userToRemove.getShared_groups_id().remove(groupId);
		
		return userService.updateUser(userToRemove).then(groupService.editGroup(grp));
	}	
	
	@PatchMapping("/remove/users/{groupId}")
	public List<String> removeMultipleUsers(@PathVariable String groupId,MultipleUsers users) throws JsonProcessingException{

		String email = getEmailFromToken();
		User owner = userService.findByEmail(email).block();
		if(null==owner)
		{
			throw new UserNotFoundException("User with email: "+email+" doesnot exist");  
		}
		
		Groups grp=groupService.findByGroupId(groupId).block();
		if(grp==null) {
			throw new GroupNotFoundException("Group with group id"+groupId+" doesn't exist");
		}
		
		if(!owner.getGroups().contains(grp)) {
			throw new CustomException("Given user is not authorised", HttpStatus.FORBIDDEN);
		}
		
		if(users.getUsersEmail().isEmpty())
		{
			throw new CustomException("Users To add Cannot be empty", HttpStatus.NOT_ACCEPTABLE);
		}
		
		List<User> UsersInGroup = grp.getUsers();
		List<String> InCorrectUserEmail =  new ArrayList<String>();
		for(String userToRemoveEmail : users.getUsersEmail()) {
			User userToRemove=userService.findByEmail(userToRemoveEmail).block();
			
			if(UsersInGroup.contains(userToRemove)) {
				UsersInGroup.remove(userToRemove);
				Notifications notification=new Notifications();
				notification.setNotification_content(userToRemove.getFirstname()+" has been removed from the group");
				notification.setNotification_type(NotificationType.REMOVED_TO_GROUP);
				notification.setStatus("unread");
				grp.getNotifications().add(notification);
				
				userToRemove.getShared_groups_id().remove(groupId);
				userService.updateUser(userToRemove);	
			}
			else
				InCorrectUserEmail.add(userToRemoveEmail);			
		}
		
		groupService.editGroup(grp);
		return InCorrectUserEmail;
	}
	
	@PatchMapping("/leave/{groupId}/")
	public Mono<Groups> userLeave(@PathVariable String groupId){
		String userEmail = getEmailFromToken();
		User user=userService.findByEmail(userEmail).block();		
		if(null== user)
		{
			throw new UserNotFoundException("User with email: "+userEmail+" doesnot exist");  
		}
		
		Groups grp=groupService.findByGroupId(groupId).block();
		if(grp==null) {
			throw new GroupNotFoundException("Group with group id"+groupId+" doesn't exist");
		}
		
		if(!user.getGroups().contains(grp)) {
			throw new CustomException("Given user is not authorised", HttpStatus.FORBIDDEN);
		}
		log.info("removeUsers=> groupId:{}, userEmail: {}",groupId,userEmail);
		
		grp.getUsers().remove(user);
		user.getShared_groups_id().remove(groupId);
		
		Notifications notification=new Notifications();
		notification.setNotification_content(user.getFirstname()+" has left the group");
		notification.setNotification_type(NotificationType.LEFT_GROUP);
		notification.setStatus("unread");
		grp.getNotifications().add(notification);
		
		
		return userService.updateUser(user).then(groupService.editGroup(grp));
	}	
	
	@PatchMapping("add/device/{groupId}/{deviceToAddserialId}")
	public Mono<Groups> addDevice(@PathVariable String groupId,@PathVariable String deviceToAddserialId) throws JsonProcessingException, EntityNotFoundException{
		
		log.info("addDevices=> groupId:{}, deviceToAddId: {}",groupId,deviceToAddserialId);
		
		String email = getEmailFromToken();
		User owner = userService.findByEmail(email).block();
		if(null==owner)
		{
			throw new UserNotFoundException("User with email: "+email+" doesnot exist");  
		}
		
		Groups grp=groupService.findByGroupId(groupId).block();
		if(grp==null) {
			throw new GroupNotFoundException("Group with group id"+groupId+" doesn't exist");
		}

		Device devToAdd=deviceService.getDevice(deviceToAddserialId).block();
		if(null== devToAdd)
		{
			throw new DeviceNotFoundException("Device with id: "+deviceToAddserialId+" doesnot exist");  
		}

		if(!owner.getGroups().contains(grp)&& !owner.getAccount().getAccountId().equals(devToAdd.getAccount().getAccountId())) {
			throw new CustomException("Given user is not authorised", HttpStatus.FORBIDDEN);
		}
		
		List<Device> dList = grp.getShared_devices();
		if(dList.contains(devToAdd)) {
			throw new DeviceAlreadyExistException("Device with id: "+devToAdd+" already exist");
		}
		
		dList.add(devToAdd);
		return groupService.editGroup(grp);
	}
	
	@PatchMapping("/add/devices/{groupId}")
	public List<String> addMultipleDevices(@PathVariable String groupId,@RequestBody MultiDevices devices) throws JsonProcessingException, EntityNotFoundException{
		
		String email = getEmailFromToken();
		User owner = userService.findByEmail(email).block();
		if(null==owner)
		{
			throw new UserNotFoundException("User with email: "+email+" doesnot exist");  
		}
		
		Groups grp=groupService.findByGroupId(groupId).block();
		if(grp==null) {
			throw new GroupNotFoundException("Group with group id"+groupId+" doesn't exist");
		}

		if(!owner.getGroups().contains(grp)) {
			throw new CustomException("Given user is not authorised", HttpStatus.FORBIDDEN);
		}
		
		if(devices.getDeviceId().isEmpty())
		{
			throw new CustomException("Users To add Cannot be empty", HttpStatus.NOT_ACCEPTABLE);
		}
		
		List<Device> DeviceInGroup = grp.getShared_devices();
		List<String> InCorrectDeviceId =  new ArrayList<String>();	
		String ownerAccountId = owner.getAccount().getAccountId();
		for(String deviceToAddId : devices.getDeviceId()) {
			Device devToAdd=deviceService.getDevice(deviceToAddId).block();
			if(!(devToAdd==null || DeviceInGroup.contains(devToAdd)) && ownerAccountId.equals(devToAdd.getAccount().getAccountId()))
				DeviceInGroup.add(devToAdd);
			else
				InCorrectDeviceId.add(deviceToAddId);			
		}
		
		return InCorrectDeviceId;
	}
		
	@PatchMapping("/remove/device/{groupId}/{deviceToRemoveserialId}")
	public Mono<Groups> removeDevice(@PathVariable String groupId,@PathVariable String deviceToRemoveserialId) throws JsonProcessingException, EntityNotFoundException
	{
		log.info("removeDevice=> groupId:{}, deviceToAddId: {}",groupId,deviceToRemoveserialId);
		
		String email = getEmailFromToken();
		User owner = userService.findByEmail(email).block();
		if(null==owner)
		{
			throw new UserNotFoundException("User with email: "+email+" doesnot exist");  
		}
		
		Groups grp=groupService.findByGroupId(groupId).block();
		if(grp==null) {
			throw new GroupNotFoundException("Group with group id"+groupId+" doesn't exist");
		}

		Device devToRemove=deviceService.getDevice(deviceToRemoveserialId).block();
		if(null== devToRemove)
		{
			throw new DeviceNotFoundException("Device with id: "+deviceToRemoveserialId+" doesnot exist");  
		}

		if(!owner.getGroups().contains(grp)) {
			throw new CustomException("Given user is not authorised", HttpStatus.FORBIDDEN);
		}
		
		grp.getShared_devices().remove(devToRemove);
		return groupService.editGroup(grp);
		
		
	}
	
	@PatchMapping("/remove/devices/{groupId}")
	public List<String> removeMultipleDevices(@PathVariable String groupId,@RequestBody MultiDevices devices) throws JsonProcessingException, EntityNotFoundException{

		String email = getEmailFromToken();
		User owner = userService.findByEmail(email).block();
		if(null==owner)
		{
			throw new UserNotFoundException("User with email: "+email+" doesnot exist");  
		}
		
		Groups grp=groupService.findByGroupId(groupId).block();
		if(grp==null) {
			throw new GroupNotFoundException("Group with group id"+groupId+" doesn't exist");
		}

		if(!owner.getGroups().contains(grp)) {
			throw new CustomException("Given user is not authorised", HttpStatus.FORBIDDEN);
		}
		
		
		List<Device> DeviceInGroup = grp.getShared_devices();
		List<String> InCorrectDeviceId =  new ArrayList<String>();;
		for(String deviceToAddSerialId : devices.getDeviceId()) {
			Device devToAdd=deviceService.getDevice(deviceToAddSerialId).block();
			if(DeviceInGroup.contains(devToAdd))
				DeviceInGroup.remove(devToAdd);
			else
				InCorrectDeviceId.add(deviceToAddSerialId);			
		}
		groupService.editGroup(grp);
		return InCorrectDeviceId;
	}
}
