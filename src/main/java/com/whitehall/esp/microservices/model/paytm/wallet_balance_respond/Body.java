package com.whitehall.esp.microservices.model.paytm.wallet_balance_respond;

import java.util.Map;

import com.whitehall.esp.microservices.model.paytm.FetchPaymentOptionRespond.BalanceInfo;
import com.whitehall.esp.microservices.model.paytm.HeadRespond;
import com.whitehall.esp.microservices.model.paytm.ResultInfo;

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
public class Body {
	private ResultInfo resultInfo;
	
	private Map<String, Object> extraParamsMap;
	
	private BalanceInfo balanceInfo;
	
}
