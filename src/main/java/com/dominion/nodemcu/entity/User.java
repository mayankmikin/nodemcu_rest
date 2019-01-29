package com.dominion.nodemcu.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 8200959090022234026L;

	/*
	 * @Id
	 * 
	 * @GeneratedValue(strategy = GenerationType.IDENTITY)
	 * 
	 * @Column(updatable=false, unique=true, nullable=false) private Long id;
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	private String firstname;

	private String lastname;

	private String address;

	private Boolean isactive;

	@NotNull
	private String phone;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACCOUNT_ID")
	private Account account;

	@Column(nullable = true, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;

	@Column(nullable = false, unique = true)
	@Email(message = "Please provide a valid e-mail")
	@NotEmpty(message = "Please provide an e-mail")
	private String email;

	//@Transient
	private String password;

	private boolean enabled;

	private String confirmationToken;

	private Boolean isAccountOwner;

	/**
	 * Roles are being eagerly loaded here because they are a fairly small
	 * collection of items for this example.
	 */
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private List<Role> roles;

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

	public User() {
		super();
	}

	public Boolean getIsAccountOwner() {
		return isAccountOwner;
	}

	public void setIsAccountOwner(Boolean isAccountOwner) {
		this.isAccountOwner = isAccountOwner;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public void addRoleToExistingRoles(Role role) {
		this.roles.add(role);
	}
	public void addRoleToNewRolesList(Role role) {
		this.roles=new ArrayList<>();
		this.roles.add(role);
	}
	public User( String firstname, String lastname, String address, Boolean isactive, @NotNull String phone,
			Account account, Date createdAt, Date updatedAt,
			@Email(message = "Please provide a valid e-mail") @NotEmpty(message = "Please provide an e-mail") String email,
			String password, boolean enabled, String confirmationToken, Boolean isAccountOwner, List<Role> roles) {
		super();
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

}
