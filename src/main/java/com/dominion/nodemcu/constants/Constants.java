package com.dominion.nodemcu.constants;

import java.util.ArrayList;
import java.util.List;

import com.dominion.nodemcu.exceptions.Details;

public class Constants 
{
	
	public static class UserConstants
	{
		public static final String USER_NOT_FOUND="User Not Found";
		
		
	}
	public static class PublicConstants
	{
		/*public static final String GET_IMAGE="/api/image";
		public static final String SAVE_IMAGE="static/images/";*/
		public static final String reason="Invalid Data";
		public static final String status="Invalid Request";
		
	}
	
	public static List<Details> createDetailsForException(String field, String reason)
	{
		List<Details> details= new ArrayList<Details>();
		details.add(new Details(field, reason));
		return details;
	}
	public static List<Details> addtoDetailsForException(String field, String reason,List<Details>details)
	{
		details.add(new Details(field, reason));
		return details;
	}

}
