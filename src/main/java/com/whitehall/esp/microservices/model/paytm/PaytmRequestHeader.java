package com.whitehall.esp.microservices.model.paytm;

import com.paytm.pg.merchant.CheckSumServiceHelper;

import lombok.Getter;

@Getter
public class PaytmRequestHeader {
	private String clientId;
	
	private String requestTimestamp = new Long(System.currentTimeMillis()).toString();;
	
	private String version;
	
	private String signature;
	
	private String channelId;
		
	public PaytmRequestHeader(String clientId,String version,String channelId,String MERCHANT_KEY, String paytmParams_body) throws Exception {
		this.clientId= clientId;
		
		this.version=version;
		
		this.channelId = channelId;
		
		signature = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(MERCHANT_KEY, paytmParams_body);
	}
}

