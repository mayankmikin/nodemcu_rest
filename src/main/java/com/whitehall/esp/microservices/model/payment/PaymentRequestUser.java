package com.whitehall.esp.microservices.model.payment;

import com.whitehall.esp.microservices.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestUser {
	User user;
	int amount;
}
