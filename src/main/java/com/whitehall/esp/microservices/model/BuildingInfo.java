package com.whitehall.esp.microservices.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Document
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BuildingInfo implements Serializable
{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = -3388134344369204521L;

	@Id
	private String buildingInfoId;
	
	private Set<House> houses= new HashSet<House>();
	
	private Account account;

}
