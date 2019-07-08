package com.whitehall.esp.microservices.model.paytm.FetchPaymentOptionRespond;


import com.whitehall.esp.microservices.model.paytm.HeadRespond;

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
public class FetchPaymentOptionRespond {
	HeadRespond head;
	Body body;
}