package com.whitehall.esp.microservices.model.paytm.InitiateTransactionRespond;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.whitehall.esp.microservices.model.paytm.ResultInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Body{
	ResultInfo resultInfo;	
	String txnToken;
	Boolean isPromoCodeValid;
	Boolean authenticated;
//	@JsonIgnore
	Map<String, Object> extraParamsMap;
	
}