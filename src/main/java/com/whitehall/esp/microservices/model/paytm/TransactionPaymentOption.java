package com.whitehall.esp.microservices.model.paytm;

import com.whitehall.esp.microservices.model.paytm.FetchPaymentOptionRespond.PayOption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPaymentOption {
	String txnId;
	PayOption MerchantPayOption;
}
