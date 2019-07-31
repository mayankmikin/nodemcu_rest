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

@Document
@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Floor implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2141306256051333899L;

	@Id
	private String floorId;
	
	@Indexed
	private String floorName;
	private Set<Room> rooms = new HashSet<Room>();
}
