package com.whitehall.esp.microservices.model.paytm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class PaymentOptionRequestHead {
 String version;
 String requestTimestamp ;
 String channelId;
 String txnToken;
 
 public PaymentOptionRequestHead(String version,String channelId,String txnToken) {
	 requestTimestamp = new Long(System.currentTimeMillis()).toString();
	 this.version = version;
	 this.channelId = channelId;
	 this.txnToken = txnToken;
}
}
