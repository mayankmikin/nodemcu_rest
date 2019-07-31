package com.whitehall.esp.microservices.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitehall.esp.microservices.model.frontend.HomePageTabs;

@Configuration
public class ResourceFileReader 
{
//	@Bean
//	public  HomePageTabs loadUtility()
//	{
//		Yaml yaml = new Yaml(new Constructor(HomePageTabs.class));
//		InputStream inputStream = this.getClass()
//		  .getClassLoader()
//		  .getResourceAsStream("homePageTabs.yml");
//		return yaml.load(inputStream); 
//	}

	@Bean
	public HomePageTabs loadTabs()
	{
		ObjectMapper mapper = new ObjectMapper();
		HomePageTabs hometab= new HomePageTabs();
		TypeReference<HomePageTabs> typeReference = new TypeReference<HomePageTabs>() {
		};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/json/homePageTabs.json");
		try {
			hometab = mapper.readValue(inputStream, typeReference);
		}
		catch (IOException e) {
			System.out.println("Unable to load homePageTabs: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Unable to load homePageTabs: " + e.getMessage());
		}
		return hometab;
	}
	
}
