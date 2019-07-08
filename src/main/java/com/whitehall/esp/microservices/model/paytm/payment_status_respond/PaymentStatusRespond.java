package com.whitehall.esp.microservices.model.paytm.payment_status_respond;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PaymentStatusRespond {
	
	@JsonProperty("MID")
	private String MID;
	@JsonProperty("ORDERID")
	private String ORDERID;
	@JsonProperty("BANKTXNID")
	private String BANKTXNID;
	@JsonProperty("TXN_TYPE")
	private String TXN_TYPE;
	@JsonProperty("TXNAMOUNT")
	private String TXNAMOUNT;
	@JsonProperty("STATUS")
	private String STATUS;
	@JsonProperty("RESPCODE")
	private String RESPCODE;
	@JsonProperty("RESPMSG")
	private String RESPMSG;
	@JsonProperty("TXNDATE")
	private String TXNDATE;
	@JsonProperty("GATEWAYNAME")
	private String GATEWAYNAME;
	@JsonProperty("BANKNAME")
	private String BANKNAME;
	@JsonProperty("PAYMENTMODE")
	private String PAYMENTMODE;
	@JsonProperty("TXNTYPE")
	private String TXNTYPE;
	@JsonProperty("REFUNDAMT")
	private String REFUNDAMT;
	
}
