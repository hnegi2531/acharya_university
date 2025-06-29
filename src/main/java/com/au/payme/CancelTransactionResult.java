package com.au.payme;

import java.util.Date;

public class CancelTransactionResult {
	
	private String transaction;
	private Long cancel_time;
	private Integer state;
	
	public CancelTransactionResult() {
		super();
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public Long getCancel_time() {
		return cancel_time;
	}

	public void setCancel_time(Long cancel_time) {
		this.cancel_time = cancel_time;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	

}
