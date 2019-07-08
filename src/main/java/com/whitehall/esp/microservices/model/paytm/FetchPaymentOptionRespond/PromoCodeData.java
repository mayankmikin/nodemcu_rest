package com.whitehall.esp.microservices.model.paytm.FetchPaymentOptionRespond;

import com.whitehall.esp.microservices.model.paytm.InitiateTransactionRespond.Body;
import com.whitehall.esp.microservices.model.paytm.InitiateTransactionRespond.Head;
import com.whitehall.esp.microservices.model.paytm.InitiateTransactionRespond.InitiateTransactionRespond;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PromoCodeData {
	
	private String promoCodeMsg;
	
	private String promoCodeTypeName;
	
	private String promoCodeValid;
	
	private String promoMsg;
	
	private String promoCode;
}
