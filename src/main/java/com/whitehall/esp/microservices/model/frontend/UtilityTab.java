package com.whitehall.esp.microservices.model.frontend;

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
public class UtilityTab {
	private String tabname;
    private String url;
}
