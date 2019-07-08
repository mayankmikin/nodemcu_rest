package com.whitehall.esp.microservices.model.paytm;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class PaytmUserInfo {

	private String custId;
	private String mobile;
	private String email;
	private String firstName;
	private String lastName;
	
	public void setAll(String custId, String mobile, String email, String firstName, String lastName){
		this.custId = custId;
		this.mobile = mobile;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
}
