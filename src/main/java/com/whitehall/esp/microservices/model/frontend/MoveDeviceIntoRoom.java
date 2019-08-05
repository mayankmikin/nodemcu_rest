package com.whitehall.esp.microservices.model.frontend;

import java.util.List;

import com.whitehall.esp.microservices.model.Device;
import com.whitehall.esp.microservices.model.Floor;
import com.whitehall.esp.microservices.model.House;
import com.whitehall.esp.microservices.model.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
@NoArgsConstructor
public class MoveDeviceIntoRoom 
{
	List<Device>deviceList;
	String buildingInfoId;
	String houseId;
	String floorId;
	String roomId;
}
