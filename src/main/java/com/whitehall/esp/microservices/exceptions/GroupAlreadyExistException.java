package com.whitehall.esp.microservices.exceptions;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@ResponseStatus(HttpStatus.CONFLICT)
@Getter
@AllArgsConstructor
public class GroupAlreadyExistException extends RuntimeException implements Serializable {

static final long serialVersionUID = -687991492884005033L;
	
	public GroupAlreadyExistException(String message) {
		super(message);
	}
	
}
