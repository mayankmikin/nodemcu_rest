package com.whitehall.esp.microservices.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JsonUtils {
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	ObjectMapper objectMapper= getMapper();
	
	public DateTimeFormatter getDtf() {
		return dtf;
	}

	ObjectNode parentNode = objectMapper.createObjectNode();
	
	@Bean
	public ObjectMapper getMapper() {
		return  new ObjectMapper();
	}
	
	public  JsonNode setData(String value)
	{
		
		 parentNode.put("data", value);
		 return parentNode;
	}
	public  JsonNode setData(Boolean value)
	{
		
		 parentNode.put("data", value);
		 return parentNode;
	}
	public  JsonNode setErrorData(String value, String error)
	{
		
		 parentNode.put("data", value);
		 parentNode.put("error", error);
		 return parentNode;
	}
	
	public String print(Object value) 
	{		
		try
		{
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
		}
		catch(JsonProcessingException e)
		{
			log.error(e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public String getCurrentDate()
	{
		 LocalDateTime now = LocalDateTime.now();
		 return getDtf().format(now);
	}
	
}
