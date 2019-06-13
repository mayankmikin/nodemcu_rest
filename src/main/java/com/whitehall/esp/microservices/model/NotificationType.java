package com.whitehall.esp.microservices.model;

public enum NotificationType {

	 	CONTACT_REQUEST("Conatact Request"),
	 	CONTACT_ADDED("Contact Added"),
	 	CONTACT_REQUEST_REJECTED("Contact Request Rejected"),
	    ADDED_TO_GROUP("Conatct Added to group"),
		REMOVED_TO_GROUP("Removed From Group"),
		LEFT_GROUP("Left Group");
		
		private String message;
		private  NotificationType(String message) {
			this.message=message;
		}
	    
	    public String getValue()
	    {
	    	return this.message;
	    }
	    
}
