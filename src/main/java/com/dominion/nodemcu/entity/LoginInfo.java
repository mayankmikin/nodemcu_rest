package com.dominion.nodemcu.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.LastModifiedDate;

import com.dominion.nodemcu.model.Auditable;

@Entity
@Table(name = "loginInfo")
public class LoginInfo extends Auditable<String>
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	private String username;
	
	@LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastLogindDate;
	
	private String tokenIssued;

	public LoginInfo() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getLastLogindDate() {
		return lastLogindDate;
	}

	public void setLastLogindDate(Date lastLogindDate) {
		this.lastLogindDate = lastLogindDate;
	}

	public String getTokenIssued() {
		return tokenIssued;
	}

	public void setTokenIssued(String tokenIssued) {
		this.tokenIssued = tokenIssued;
	}

	@Override
	public String toString() {
		return "LoginInfo [username=" + username + ", lastLogindDate=" + lastLogindDate + ", tokenIssued=" + tokenIssued
				+ "]";
	}

	public LoginInfo(String username, Date lastLogindDate, String tokenIssued) {
		super();
		this.username = username;
		this.lastLogindDate = lastLogindDate;
		this.tokenIssued = tokenIssued;
	}
	
	
	

}
