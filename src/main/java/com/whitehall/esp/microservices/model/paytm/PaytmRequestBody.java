package com.whitehall.esp.microservices.model.paytm;

import java.util.ArrayList;
import java.util.List;

import com.whitehall.esp.microservices.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@Setter
@Getter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaytmRequestBody {
	
	private String requestType = "Payment";
	
//	@Value("${paytm.payment.sandbox.merchantId}")
	private String mid;
	
	private String orderId = "ORDER_" + System.currentTimeMillis();;
	
	private String websiteName;
	
	private PaytmMoney txnAmount;
	
	private List<PaymentMode> enablePaymentMode= new ArrayList<PaymentMode>();
	
	private PaytmUserInfo userInfo;
	
	public PaytmRequestBody(String mid,String websiteName,String amount,User user){

//		log.info("amount= {}",new Integer(amount).toString());
		txnAmount = new PaytmMoney();
		txnAmount.setValue(amount);
		
		this.mid = mid;
		
		this.websiteName = websiteName;
		
		userInfo = new PaytmUserInfo(user.getUserId(), user.getPhone(), user.getEmail(), user.getFirstname(), user.getLastname());
		
		enablePaymentMode.add(new PaymentMode("BALANCE"));
		enablePaymentMode.add(new PaymentMode("UPI"));
		enablePaymentMode.add(new PaymentMode("PAYTM_DIGITAL_CREDIT"));
		enablePaymentMode.add(new PaymentMode("PPBL"));
		
	}
}
