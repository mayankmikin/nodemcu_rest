package com.whitehall.esp.microservices.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeviceStatusChangeRequest {
	private String deviceId;
	
	private String port;
	
	private String action;
}
