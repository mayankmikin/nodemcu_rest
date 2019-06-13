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
public class Walkthrough 
{
	
	String title;
	String description;
	//image path
	String image;
	Integer page_no;
}
