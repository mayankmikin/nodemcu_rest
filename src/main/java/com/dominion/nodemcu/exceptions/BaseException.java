package com.dominion.nodemcu.exceptions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BaseException extends Exception
{

	private static final long serialVersionUID = 1L;
	String submitTimeUtc= Instant.now().toString();
	String message;
	String reason;
	String status;
	List<Details>details= new ArrayList<Details>();
	
	public String getSubmitTimeUtc() {
		return submitTimeUtc;
	}
	public void setSubmitTimeUtc(String submitTimeUtc) {
		this.submitTimeUtc = submitTimeUtc;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Details> getDetails() {
		return details;
	}
	public void setDetails(List<Details> details) {
		this.details = details;
	}
	public BaseException() {
		super();
	}
	public BaseException( String message, String reason, String status, List<Details> details) {
		super();
		this.submitTimeUtc = Instant.now().toString();
		this.message = message;
		this.reason = reason;
		this.status = status;
		this.details = details;
	}
	
	
}
