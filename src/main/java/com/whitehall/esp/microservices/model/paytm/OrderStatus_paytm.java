package com.whitehall.esp.microservices.model.paytm;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class OrderStatus_paytm {
	@JsonProperty("MID")
	private String MID;
	@JsonProperty("ORDERID")
	private String ORDERID;
	@JsonProperty("CHECKSUMHASH")
	private String CHECKSUMHASH;
}
