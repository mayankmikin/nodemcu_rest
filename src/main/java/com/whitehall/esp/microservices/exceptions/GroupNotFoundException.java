package com.whitehall.esp.microservices.exceptions;

import java.io.Serializable;

public class GroupNotFoundException extends RuntimeException implements Serializable {

static final long serialVersionUID = -687991492884005033L;
	
	public GroupNotFoundException(String message) {
		super(message);
	}
	
}
