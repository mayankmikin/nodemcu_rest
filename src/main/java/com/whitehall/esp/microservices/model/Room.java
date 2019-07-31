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
public class Room implements Serializable{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6974778517995664248L;

	@Id
	private String roomId;
	
	  @Indexed
	  private String roomName;
	  
	  private Set<Device> devices = new HashSet<Device>();
}
