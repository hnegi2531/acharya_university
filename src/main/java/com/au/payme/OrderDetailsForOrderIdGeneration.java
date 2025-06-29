package com.au.payme;

import java.util.HashMap;

public class OrderDetailsForOrderIdGeneration {
	
	private HashMap<String,Object> order_details;
	private String prefix_suffix_word;
	private Integer length_without_prefix;

	public OrderDetailsForOrderIdGeneration() {
		super();
	}

	public HashMap<String, Object> getOrder_details() {
		return order_details;
	}

	public void setOrder_details(HashMap<String, Object> order_details) {
		this.order_details = order_details;
	}

	public String getPrefix_suffix_word() {
		return prefix_suffix_word;
	}

	public void setPrefix_suffix_word(String prefix_suffix_word) {
		this.prefix_suffix_word = prefix_suffix_word;
	}

	public Integer getLength_without_prefix() {
		return length_without_prefix;
	}

	public void setLength_without_prefix(Integer length_without_prefix) {
		this.length_without_prefix = length_without_prefix;
	}
	
	

}
