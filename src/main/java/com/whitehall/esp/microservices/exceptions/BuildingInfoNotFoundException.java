package com.whitehall.esp.microservices.exceptions;

import java.io.Serializable;

public class BuildingInfoNotFoundException extends RuntimeException implements Serializable {

	static final long serialVersionUID = -687991492884005033L;
	
	public BuildingInfoNotFoundException(String message) {
		super(message);
	}
}
