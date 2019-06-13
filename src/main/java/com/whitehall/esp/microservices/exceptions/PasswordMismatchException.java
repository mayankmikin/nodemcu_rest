package com.whitehall.esp.microservices.exceptions;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PasswordMismatchException extends RuntimeException implements Serializable {

	static final long serialVersionUID = -687991492884005033L;
	
	public PasswordMismatchException(String message) {
		super(message);
	}
}
