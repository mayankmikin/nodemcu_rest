package com.whitehall.esp.microservices.model.paytm;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public  class ResultInfo{
	String resultStatus;
	String resultCode;
	String resultMsg;
	String bankRetry;
	String retry;
}