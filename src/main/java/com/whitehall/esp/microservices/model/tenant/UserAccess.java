package com.whitehall.esp.microservices.model.tenant;

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
public class UserAccess {

	private Integer noOfGroups=3;
	private Integer noOfUserPerGroup=50;
	private Integer noOfTotalUsers=150;
	private Integer noOfContactEmail=150;
	private Integer noOfPendingEmail=100;
}
