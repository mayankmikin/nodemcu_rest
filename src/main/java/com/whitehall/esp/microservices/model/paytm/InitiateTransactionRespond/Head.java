package com.whitehall.esp.microservices.model.paytm.InitiateTransactionRespond;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Head{
	private String responseTimestamp;
	private String version;
	private String requestId;
	private String clientId;
	private String signature;
}