package com.thedeanda.ajaxproxy.config.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MergeConfig {
	StringVariable filePath;
	StringVariable path;
	boolean minify;

	@Builder.Default
	MergeMode mode = MergeMode.PLAIN;
	// "mode":"CSS",
	// MergeMode mode = obj.hasKey(MODE) ? MergeMode.valueOf(obj.getString(MODE)) :
	// MergeMode.PLAIN;
}
