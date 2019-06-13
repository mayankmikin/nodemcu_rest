package com.whitehall.esp.microservices.exceptions;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
@AllArgsConstructor
public class TenantNotFoundException extends RuntimeException implements Serializable {

	static final long serialVersionUID = -687991492884005033L;
	
	public TenantNotFoundException(String message) {
		super(message);
	}
}
