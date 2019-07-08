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
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PayMethod {
	private String displayName;
	
	private StatusInfo isDisabled;

	private PayChannelBase payChannelOptions[];
	
	private String paymentMode;
	
	private String feeAmount;
	
	private String taxAmount;
	
	private String totalTransactionAmount;
	
	private String priority;
	
	private Boolean onboarding;
	
	private Boolean isHybridDisabled;
	
}
