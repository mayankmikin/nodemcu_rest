package com.whitehall.esp.microservices.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class Test {
	@Value("${roles_list}")
	public  String role_conf="ROLE_ADMIN, ROLE_CLIENT, ROLE_USER_VIEW";
	public static void main(String[] args) {

		Test t= new Test();
		//t.loadYamlFile();
		List<String> roleList= new ArrayList<String>();
		roleList=Arrays.stream(t.role_conf.split(",")).map(e->e.trim()).collect(Collectors.toList());
		Role r = new Role();
		r.setRoleName("ROLE_USER_VIEW");
		System.out.println(roleList.contains(r.getRoleName()));
	}
	public void loadYamlFile()
	{
		Yaml yaml = new Yaml(new Constructor(Permissions.class));
		InputStream inputStream = this.getClass()
		  .getClassLoader()
		  .getResourceAsStream("permissions.yml");
		/*
		 * Map<String, Object> obj = yaml.load(inputStream); System.out.println(obj);
		 */

		  Permissions perm = yaml.load(inputStream); 
		  System.out.println(perm);
		 
	}
}
