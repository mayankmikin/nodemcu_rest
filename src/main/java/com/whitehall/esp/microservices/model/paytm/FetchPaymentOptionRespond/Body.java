package com.whitehall.esp.microservices.model.paytm.FetchPaymentOptionRespond;

import java.util.Map;

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
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Body {
	private String paymentFlow;
	
	private PayOption merchantPayOption;
	
	private PayOption addMoneyPayOption;
	
	private MerchantDetails merchantDetails;
	
	private String merchantOfferMessage;
	
	private String nativeJsonRequestSupported;
	
	private PromoCodeData promoCodeData;
	
	private ResultInfo resultInfo;
	
	private Map<String, Object> extraParamsMap;
	
	private String promoCode;
	
	private Boolean walletOnly;
	
	private Boolean zeroCostEmi;
	
	private Boolean pcfEnabled;
	
	private Boolean onTheFlyKYCRequired;
}
