package com.dominion.nodemcu.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
@Entity
@Table(name = "account")
@JsonIgnoreProperties
public class Account implements Serializable{

	private static final long serialVersionUID = 8200390022234026L;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	/*Note:
	 * 
	 * jackson.databind.ser.std.CollectionSerializer.serialize
	 * if saving something goes to infinite recursion due to collection not able to serialize 
	 * then use json ignore 
	 * 
	 * */
	//@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy="account", cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	private Set<User> users=new HashSet<>();
	
	//@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy="account",cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private Set<Device> devices=new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Device> getDevices() {
		return devices;
	}

	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}
	
	
	public void addDevices(Device devices) {
		//this.devices = new HashSet<>();
		this.devices.add(devices);
	}

	public void addUsers(User users) {
		//this.users = getUsers();
		this.users.add(users);
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", users=" + users + ", devices=" + devices + "]";
	}

	
}
