package com.whitehall.esp.microservices.exceptions;

import java.io.Serializable;

public class PlanNotFoundException extends RuntimeException implements Serializable {

	static final long serialVersionUID = -687991492885005033L;
	
	public PlanNotFoundException(String message) {
		super(message);
	}
}
