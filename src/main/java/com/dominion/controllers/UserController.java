package com.dominion.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dominion.entity.User;
import com.dominion.model.ApiResponseMessage;
import com.dominion.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class UserController 
{	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	UserRepository repository;
	ObjectMapper mapper= new ObjectMapper();
	@RequestMapping("/findall")
    public ResponseEntity<List<User>> findAllUsers(){
           List<User> allusers=new ArrayList<User>();
           repository.findAll().forEach(allusers::add);
          // log.info(allusers.toString());
           allusers.forEach(s -> log.info(s.toString()));
		/*ApiResponseMessage response=new ApiResponseMessage(4,Thread.currentThread().getStackTrace()[1].getMethodName(), mapper.convertValue(allusers, JsonNode.class));*/
        return new ResponseEntity<List<User>>(allusers,HttpStatus.OK);
    }
	@PostMapping("/save")
    public ResponseEntity<ApiResponseMessage> saveUser(@RequestBody User user){
		user.setCreatedAt(Calendar.getInstance().getTime());
		ApiResponseMessage response=new ApiResponseMessage(4,Thread.currentThread().getStackTrace()[1].getMethodName(), mapper.convertValue(repository.save(user), JsonNode.class));
        return new ResponseEntity<ApiResponseMessage>(response,HttpStatus.CREATED);
    }

}
