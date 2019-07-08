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
public class PayOption {
	
	private PayMethod paymentModes[]; 
	
	private PayChannelBase savedInstruments[];
	
	private String userProfileSarvatra;

	private Boolean activeSubscriptions;
}
