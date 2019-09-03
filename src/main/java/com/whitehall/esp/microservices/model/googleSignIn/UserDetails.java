package com.whitehall.esp.microservices.model.googleSignIn;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.whitehall.esp.microservices.model.Account;
import com.whitehall.esp.microservices.model.Address;
import com.whitehall.esp.microservices.model.AppSetting;
import com.whitehall.esp.microservices.model.Groups;
import com.whitehall.esp.microservices.model.Notifications;
import com.whitehall.esp.microservices.model.Permissions;
import com.whitehall.esp.microservices.model.Role;
import com.whitehall.esp.microservices.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDetails {
	  private  String providerId;

	  private String uid;

	  private String displayName;

	  private String photoUrl;

	  private String email;

	  private Boolean isAnonymous;

	  private Boolean isEmailVerified;

	  private List<UserInfoDetails> providerData;

	
}
