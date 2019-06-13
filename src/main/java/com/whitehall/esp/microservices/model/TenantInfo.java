package com.whitehall.esp.microservices.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "tenantinfo_tenant")
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TenantInfo {
	@Id
	String tenantInfoId;
	String tenantName;
	ConfigurationProperties configuration;
}
