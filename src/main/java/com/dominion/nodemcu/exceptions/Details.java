package com.dominion.nodemcu.exceptions;

public class Details {

	String field;
	String reason;
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Details() {
		super();
	}
	public Details(String field, String reason) {
		super();
		this.field = field;
		this.reason = reason;
	}
	
}
