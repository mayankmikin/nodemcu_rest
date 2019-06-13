package com.whitehall.esp.microservices.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
public class Permissions 
{
    public Permissions() {
		super();
	}


	private Map<String, String> ROLE_ADMIN;
	
    private Map<String, String> ROLE_CLIENT;
	
    private Map<String, String> ROLE_USER_VIEW;
	
    private Map<String, String> ROLE_USER_OWNER;
    
}
