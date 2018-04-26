package com.thedeanda.ajaxproxy.config.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntVariable {
	private String originalValue;
	private int value;

	public IntVariable(String originalValue, int intValue) {
		this.originalValue = originalValue;
		this.value = intValue;
	}

	public VariableValue(String originalValue, String stringValue) {
		this.originalValue = originalValue;
		this.stringValue = stringValue;
	}
}
