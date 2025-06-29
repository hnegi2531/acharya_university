package com.au.payme;

import java.util.Date;

public class PerformTransactionResult {
	
	private String transaction;
	private Long perform_time;
	private Integer state;
	
	
	public PerformTransactionResult() {
		super();
	}


	public String getTransaction() {
		return transaction;
	}


	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}


	public Long getPerform_time() {
		return perform_time;
	}


	public void setPerform_time(Long perform_time) {
		this.perform_time = perform_time;
	}


	public Integer getState() {
		return state;
	}


	public void setState(Integer state) {
		this.state = state;
	}
	
	
}
