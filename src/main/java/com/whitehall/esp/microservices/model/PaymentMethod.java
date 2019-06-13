package com.whitehall.esp.microservices.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "PaymentMethod")
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaymentMethod {
	
	private String paymentMethodId;
	
	private String name;
	
	private Boolean enablePaymentMethod;
}
