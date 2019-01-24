package com.dominion.nodemcu.exceptions;

import java.util.ArrayList;
import java.util.List;

public class UserNotFoundException extends BaseException
{

	private static final long serialVersionUID = -8908188243480172809L;
	String message;
	String reason;
	String status;
	List<Details>details= new ArrayList<Details>();
	
	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message, String reason, String status, List<Details> details) {
		super(message, reason, status, details);
		this.message = message;
		this.reason = reason;
		this.status = status;
		this.details = details;
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

	@Override
	public String toString() {
		return "UserNotFoundException [message=" + message + ", reason=" + reason + ", status=" + status + ", details="
				+ details + "]";
	}
	
	
}
