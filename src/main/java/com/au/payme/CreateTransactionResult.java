package com.au.payme;

import java.util.Date;

public class CreateTransactionResult {

	private Long create_time;
	private String transaction;
	private Integer state;
	
	public CreateTransactionResult() {
		super();

	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	
	
}
