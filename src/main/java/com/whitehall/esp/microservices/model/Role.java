package com.whitehall.esp.microservices.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "role")
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties({"authority"})
public class Role implements Serializable,GrantedAuthority {
	
	@Id
	private String roleId;
	
	private String roleName;

    private String description;
    
    public String getAuthority() {
        return roleName;
      }

	@PersistenceConstructor
	public Role(String roleName) {
		super();
		this.roleName=roleName;
	}
}
