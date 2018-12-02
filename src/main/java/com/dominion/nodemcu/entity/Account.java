package com.dominion.nodemcu.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@Entity
@Table(name = "account")
public class Account implements Serializable{

	private static final long serialVersionUID = 8200390022234026L;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@OneToMany(mappedBy="account", fetch=FetchType.LAZY)
	private Set<User> users;
	
	@OneToMany(mappedBy="account", fetch=FetchType.LAZY)
	private Set<Device> devices;
	
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

	public Account(Long id, Set<User> users, Set<Device> devices) {
		super();
		this.id = id;
		this.users = users;
		this.devices = devices;
	}

	public Account() {
		super();
	}
	
	

}
