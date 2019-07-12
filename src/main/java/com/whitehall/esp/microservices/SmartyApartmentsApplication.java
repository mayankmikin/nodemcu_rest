package com.whitehall.esp.microservices;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
//@EnableDiscoveryClient
public class SmartyApartmentsApplication {

	private static final Log LOGGER = LogFactory.getLog(SmartyApartmentsApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SmartyApartmentsApplication.class, args);
	}
}
