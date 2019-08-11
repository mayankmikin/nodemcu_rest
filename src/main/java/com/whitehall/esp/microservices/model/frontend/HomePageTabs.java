package com.whitehall.esp.microservices.model.frontend;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
@NoArgsConstructor
public class HomePageTabs 
{
	private List<UtilityTab> utilityTab= new ArrayList<UtilityTab>();
	private Integer defaultTabCount;
}
