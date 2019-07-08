package com.whitehall.esp.microservices.model.paytm;

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
public class LoginOTPRequestHead {
	private String clientId;

	private String version;

	private String requestTimestamp;

	private String channelId;

	private String txnToken;
	
}
