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
public class StatusInfo {
	
	private String status;
	
	private String msg;
	
	private String userAccountExist;
	
	private String merchantAccept;
}
