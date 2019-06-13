package com.whitehall.esp.microservices.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AppSetting {
	
	private String country;
	
	private String appLanguage;
	
	private String currency;
	
	@DBRef(lazy = true)
	private PaymentMethod paymentMethod;
	
	private Boolean enableNotification;
	
	private Boolean enablePromotion;
	
	private Boolean enableHistory;
}
