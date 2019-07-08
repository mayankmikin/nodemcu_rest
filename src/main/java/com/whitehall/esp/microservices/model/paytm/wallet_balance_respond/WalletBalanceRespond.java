package com.whitehall.esp.microservices.model.paytm.wallet_balance_respond;

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
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WalletBalanceRespond {
	HeadRespond head;
	Body body;
}
