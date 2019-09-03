package com.whitehall.esp.microservices.model.googleSignIn;

import java.util.List;

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
public class UserInfoDetails {


	  /// The provider identifier.
	  private String providerId;

	  /// The provider’s user ID for the user.
	  private String uid;

	  /// The name of the user.
	  private String displayName;

	  /// The URL of the user’s profile photo.
	  private String photoUrl;

	  /// The user’s email address.
	  private String email;
	  
	  
}
