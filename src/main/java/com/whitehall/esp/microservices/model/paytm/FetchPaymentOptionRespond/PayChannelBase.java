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
public class PayChannelBase {
	
	private StatusInfo isDisabled;
	
	private StatusInfo hasLowSuccess;
	
	private StatusInfo iconUrl;
	
	private BalanceInfo balanceInfo;
	
	private Boolean isHybridDisabled;
}
