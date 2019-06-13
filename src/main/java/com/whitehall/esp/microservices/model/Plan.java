package com.whitehall.esp.microservices.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "plan")
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Plan {
	@Id
	private String planId;
	
	private String name;
	
	private String  benifits;
	
	private int validity;//in form days
	
	private int amount;
	
	private boolean active = true;
}
