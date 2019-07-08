package com.whitehall.esp.microservices.model.paytm.ValidateLoginOTPRespond;

import com.whitehall.esp.microservices.model.paytm.HeadRespond;

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
public class ValidateLoginOTPRespond {
 HeadRespond head;
 Body body;
}
