package com.dominion.nodemcu.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * @author Mayank Verma
 *
 */
@Entity
@Table(name = "device")
@JsonIgnoreProperties 
public class Device implements Serializable {
	
	 private static final long serialVersionUID = 820095909012312326L;
   // sequence,auto causes problem so id 1 given to device , 2 is given to manufacturer 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	private String name;
	
	private String exporterId;
	
	private String manufacturerId;
	
	private String serialId;
	
	@Column(nullable = true, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ACCOUNT_ID")
	@JsonBackReference
	private Account account;

	private String userDefinedName;
	
	private String localIp;
	
	private String externalIp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExporterId() {
		return exporterId;
	}

	public void setExporterId(String exporterId) {
		this.exporterId = exporterId;
	}

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getUserDefinedName() {
		return userDefinedName;
	}

	public void setUserDefinedName(String userDefinedName) {
		this.userDefinedName = userDefinedName;
	}
	public Device() {
		super();
	}

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public String getExternalIp() {
		return externalIp;
	}

	public void setExternalIp(String externalIp) {
		this.externalIp = externalIp;
	}

	public Device(Long id, String name, String exporterId, String manufacturerId, String serialId, Date createdAt,
			Date updatedAt, Account account, String userDefinedName, String localIp, String externalIp) {
		super();
		this.id = id;
		this.name = name;
		this.exporterId = exporterId;
		this.manufacturerId = manufacturerId;
		this.serialId = serialId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.account = account;
		this.userDefinedName = userDefinedName;
		this.localIp = localIp;
		this.externalIp = externalIp;
	}

	@Override
	public String toString() {
		return "Device [id=" + id + ", name=" + name + ", exporterId=" + exporterId + ", manufacturerId="
				+ manufacturerId + ", serialId=" + serialId + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", account=" + account + ", userDefinedName=" + userDefinedName + ", localIp=" + localIp
				+ ", externalIp=" + externalIp + "]";
	}
	
	
}
