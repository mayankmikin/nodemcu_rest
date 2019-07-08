package com.whitehall.esp.microservices.model.paytm.ValidateLoginOTPRespond;

import java.util.Map;

import com.whitehall.esp.microservices.model.paytm.ResultInfo;

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
public class Body {
	private ResultInfo resultInfo;
	
	private Map<String, Object> extraParamsMap;
	
	private Boolean authenticated;
}
