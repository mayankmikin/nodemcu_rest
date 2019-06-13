package com.whitehall.esp.microservices.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "groups")
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Groups {
	
	@Id
	private String groupId;
	
	private String groupName;
	
	@DBRef(lazy=true)
	private List<User> users= new ArrayList<User>();
	
	@DBRef(lazy=true)
	private List<Device>shared_devices= new ArrayList<Device>();
	
	private List<Notifications>notifications = new ArrayList<Notifications>();

}
