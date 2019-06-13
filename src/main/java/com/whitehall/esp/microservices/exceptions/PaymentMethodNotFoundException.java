package com.whitehall.esp.microservices.exceptions;

import java.io.Serializable;

public class PaymentMethodNotFoundException  extends RuntimeException implements Serializable {

	static final long serialVersionUID = -687991492884005033L;
	
	public PaymentMethodNotFoundException(String message) {
		super(message);
	}
}
