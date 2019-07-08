package com.whitehall.esp.microservices.model.paytm.InitiateTransactionRespond;

import java.util.Map;

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
public class InitiateTransactionRespond {
	private Head head;
	private Body body;
}

