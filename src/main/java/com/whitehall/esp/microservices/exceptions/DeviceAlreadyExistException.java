package com.whitehall.esp.microservices.exceptions;

import java.io.Serializable;

public class DeviceAlreadyExistException extends RuntimeException implements Serializable {

static final long serialVersionUID = -687991492884005033L;
	
	public DeviceAlreadyExistException(String message) {
		super(message);
	}
	
}