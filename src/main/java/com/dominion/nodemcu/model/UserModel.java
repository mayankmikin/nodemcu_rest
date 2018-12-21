package com.dominion.nodemcu.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.dominion.nodemcu.entity.Account;

public class UserModel implements Serializable {

	private static final long serialVersionUID = 8200912390022234026L;



	private Long id;

	private String firstname;

	private String lastname;

	private String address;

	private Boolean isactive;

	private String phone;
	

	private Account account;


	private Date createdAt;


	private Date updatedAt;


	private String email;


	private String password;

	private boolean enabled;

	private String confirmationToken;

	private Boolean isAccountOwner;

	/**
	 * Roles are being eagerly loaded here because they are a fairly small
	 * collection of items for this example.
	 */
	
	private List<Long> roles;

	// getter setters and constructors

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getIsactive() {
		return isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	

	public Boolean getIsAccountOwner() {
		return isAccountOwner;
	}

	public void setIsAccountOwner(Boolean isAccountOwner) {
		this.isAccountOwner = isAccountOwner;
	}

	public List<Long> getRoles() {
		return roles;
	}

	public void setRoles(List<Long> roles) {
		this.roles = roles;
	}

	public UserModel(Long id, String firstname, String lastname, String address, Boolean isactive, String phone,
			Account account, Date createdAt, Date updatedAt, String email, String password, boolean enabled,
			String confirmationToken, Boolean isAccountOwner, List<Long> roles) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.address = address;
		this.isactive = isactive;
		this.phone = phone;
		this.account = account;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.confirmationToken = confirmationToken;
		this.isAccountOwner = isAccountOwner;
		this.roles = roles;
	}

	public UserModel() {
		super();
	}




}
