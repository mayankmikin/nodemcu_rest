package com.whitehall.esp.microservices.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
public class Device implements Serializable {
	 private static final long serialVersionUID = 820095909012312326L;
	   // sequence,auto causes problem so id 1 given to device , 2 is given to manufacturer 
	 	@Id
		private String deviceId;
		
		private String name;
		
		private String exporterId;
		
		private String manufacturerId;
		
		@Indexed
		private String serialId;
		
		@CreatedDate
		private Date createdAt;

		@LastModifiedDate
		private Date updatedAt;
	
		//@DBRef(db="account", lazy = true) 
	  	private Account account;

		private String userDefinedName;
		
		private String localIp;
		
		private String externalIp;
		
		private Set<Map<String,Integer>> ports= new HashSet<Map<String,Integer>>();
	
		private String deviceType;
		
		private  boolean isOccupied=false;

}
