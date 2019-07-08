package com.whitehall.esp.microservices.model.paytm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaytmMoney {
	String currency="INR";
	String value;
}
