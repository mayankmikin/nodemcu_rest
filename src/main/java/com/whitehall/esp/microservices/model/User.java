package com.whitehall.esp.microservices.model;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.relation.RoleInfoNotFoundException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "user")
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties({"contacts"})
public class User implements Serializable {
	
	@Id
	private String userId;
	
	private String firstname;

	private String lastname;

	private Address address;

	private Boolean isactive;

	@NotNull
	@Indexed
	private String phone;
	
	//@DBRef(lazy=true)
	private Account account;

	@CreatedDate
	private Date createdAt;

	@LastModifiedDate
	private Date updatedAt;

	@Email(message = "Please provide a valid e-mail")
	@NotEmpty(message = "Please provide an e-mail")
	@Indexed
	private String email;

	//@Transient
	private String password;

	private boolean enabled;

	private String confirmationToken;

	private Boolean isAccountOwner;
	
	private Boolean first_visited=true;
	
	private String language_preffered="en";
	
	//@DBRef(db="role")
	private List<Role> roles;
	
	private Permissions permissions;
	
	@DBRef(lazy=true)
	private List<Groups>groups =new ArrayList<Groups>();
	
	//@DBRef(lazy=true)
	private List<String>shared_groups_id =new ArrayList<String>();// won't it have a cyclic dependency ??
	
	private String user_type="standard"; 
	
	private AppSetting appSetting;
	
//	//@DBRef(lazy=true)
//	private Set<User>contacts =new HashSet<User>();
//	
//	//@DBRef(lazy=true)
//	private Set<User>pending_requests=new HashSet<User>(); 
	
	private Set<String>contacts_email=new HashSet<String>();
	
	private Set<String>pending_requests_email=new HashSet<String>(); 
	
	private List<Notifications>notifications =new ArrayList<Notifications>();
	
	private List<String>blocked_by_user_email = new ArrayList<String>();// user that blocked the given user
	
	private List<String>blocked_user_email = new ArrayList<String>();// users who the given user blocked
	
	public  Permissions loadPermissions()
	{
		Yaml yaml = new Yaml(new Constructor(Permissions.class));
		InputStream inputStream = this.getClass()
		  .getClassLoader()
		  .getResourceAsStream("permissions.yml");
		return yaml.load(inputStream); 
	}
	public Permissions setPermissionsFromFile() throws RoleInfoNotFoundException
	{
		this.permissions=loadPermissions();
		if(!this.roles.isEmpty())
		{
			
		
		this.roles.forEach(e->{
			Field[] fields = this.permissions.getClass().getDeclaredFields();
			for(Field f:fields)
			{
				f.setAccessible(true);
				if(!f.getName().equalsIgnoreCase(e.getRoleName().toString())&& !Modifier.isStatic(f.getModifiers()))
				{
					try {
						f.set(this.permissions, new HashMap<String, String>());
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
					
				}
			}
		});
		}
		else
		{
			throw new RoleInfoNotFoundException("Roles not Found");
		}
		return this.permissions;
	}
}
