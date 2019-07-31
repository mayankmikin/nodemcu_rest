package com.whitehall.esp.microservices.model;
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Document
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"createdDate"})
public class Account implements Serializable {
	 private static final long serialVersionUID = 820095909015312356L;
	@Id
	private String accountId;
	/*Note:
	 * 
	 * jackson.databind.ser.std.CollectionSerializer.serialize
	 * if saving something goes to infinite recursion due to collection not able to serialize 
	 * then use json ignore 
	 * 
	 * */
	private String accountName;
	
	/*
	 * @DBRef private Set<User> users=new HashSet<>();
	 */
	
	/*
	 * @DBRef(db="device") private Set<Device> devices=new HashSet<>();
	 */
	
	@PersistenceConstructor
	public Account(String accountId) {
		super();
		this.accountId = accountId;
	}
	
	
	//private Instant createdDate=Instant.now();

	public Account() {
		super();
	}
}
