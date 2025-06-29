package com.au.model;

import org.apache.commons.lang.builder.ToStringBuilder;

public enum TypeEnum {

	TEXT("text"), FILE("file"), CAPTION("caption");

	String type;

	TypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("type", type).toString();
	}
}
