package com.whitehall.esp.microservices.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whitehall.esp.microservices.config.ResourceFileReader;
import com.whitehall.esp.microservices.model.frontend.HomePageTabs;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/data")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class DataController 
{
	@Autowired
	ResourceFileReader jsonreader;
	
	@GetMapping("/homePageTabs")
	public ResponseEntity<HomePageTabs> gethomePageTabs() {
		log.info("gethomePageTabs");
		return new ResponseEntity<HomePageTabs>(jsonreader.loadTabs(),HttpStatus.OK);
	}

}
