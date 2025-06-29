package com.au.clickpay;

public enum ClickError {
	
	SUCESS("0"),
	SIGN_CHECK_FAILED("-1"),
	INCORRECT_PARAMETER_AMOUNT("-2"),
	ACTION_NOT_FOUND("-3"),
	ALREADY_PAID("-4"),
	USER_DOES_NOT_EXIST("-5"),
	TRANSACTION_DOES_NOT_EXIST("-6"),
	FAILED_TO_UPDATE_USER("-7"),
	ERROR_IN_REQUES_FROM_CLICK("-8"),
	TRANSACTION_CANCELLED("-9"),;

	private String string;

	private ClickError(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}
	
	
}
