package com.whitehall.esp.microservices.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Address 
{
	   	private String houseNumber;
	    private String streetAddress;
	    private String city;
	    private String state;
	    private String zipCode;

}
