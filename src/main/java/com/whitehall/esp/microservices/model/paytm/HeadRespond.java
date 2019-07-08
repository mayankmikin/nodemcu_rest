package com.whitehall.esp.microservices.model.paytm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class HeadRespond {
	private String requestId;
	
	private String version;

	private String responseTimestamp;
}
