package com.whitehall.esp.microservices.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class MultiPort implements Serializable {
	 private static final long serialVersionUID = 820095909012313226L;
		    private String portName;
		    private String value;
		    private String dt;
		    private String range;
		    private String name;

		    @JsonProperty("port_name")
		    public String getPortName() { return portName; }
		    @JsonProperty("port_name")
		    public void setPortName(String value) { this.portName = value; }

		    @JsonProperty("value")
		    public String getValue() { return value; }
		    @JsonProperty("value")
		    public void setValue(String value) { this.value = value; }

		    @JsonProperty("DT")
		    public String getDt() { return dt; }
		    @JsonProperty("DT")
		    public void setDt(String value) { this.dt = value; }

		    @JsonProperty("range")
		    public String getRange() { return range; }
		    @JsonProperty("range")
		    public void setRange(String value) { this.range = value; }

		    @JsonProperty("name")
		    public String getName() { return name; }
		    @JsonProperty("name")
		    public void setName(String value) { this.name = value; }
		}
