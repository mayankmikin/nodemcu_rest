package com.whitehall.esp.microservices.model.paytm.FetchPaymentOptionRespond;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BalanceInfo {
	float Value;
	String Currency;
}
