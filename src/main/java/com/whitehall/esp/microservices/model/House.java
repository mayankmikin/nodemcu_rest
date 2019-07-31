package com.whitehall.esp.microservices.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
public class House implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7889365340996802000L;

	@Id
	private String houseId;
	
	@Indexed
	private String houseName;
	
	private Set<Floor> floor = new HashSet<Floor>();
	
	
}
