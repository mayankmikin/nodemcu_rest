package com.dominion.nodemcu.model;

public class DeviceRequest 
{
	private String localIp;
	private String serialId;
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	public String getSerialId() {
		return serialId;
	}
	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}
	public DeviceRequest(String localIp, String serialId) {
		super();
		this.localIp = localIp;
		this.serialId = serialId;
	}
	public DeviceRequest() {
		super();
	}
	
	
}
