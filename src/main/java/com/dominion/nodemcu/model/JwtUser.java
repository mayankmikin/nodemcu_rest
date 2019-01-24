package com.dominion.nodemcu.model;

import java.util.List;

import com.dominion.nodemcu.entity.Role;

public class JwtUser {
    private String userName;
    private long id;
    private List<Role> role;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public long getId() {
        return id;
    }

	public JwtUser(String userName, long id, List<Role> role) {
		super();
		this.userName = userName;
		this.id = id;
		this.role = role;
	}

	public List<Role> getRole() {
		return role;
	}

	public void setRole(List<Role> role) {
		this.role = role;
	}

	public JwtUser() {
		super();
	}


    
}
