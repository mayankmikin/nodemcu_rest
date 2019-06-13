package com.whitehall.esp.microservices.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Tenant //extends Auditable<String> 
{
	@Id
	private String tenantId;
	@NotNull
	private String tenantName;
	@NotNull
	private String code;
	private String domainName;
	private String natureOfIndustry;
	private Boolean isActivated=false;
	
	
	
}
