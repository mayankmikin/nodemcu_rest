package com.whitehall.esp.microservices.model.tenant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "tenantinfo_user")
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TenantInfo {
	
	String tenantInfoId;
	@Indexed
	String tenantName;
	ConfigurationProperties configuration;
}
