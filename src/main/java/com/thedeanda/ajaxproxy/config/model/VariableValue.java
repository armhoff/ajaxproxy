package com.thedeanda.ajaxproxy.config.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VariableValue {
	private String originalValue;
	private int intValue;
}
