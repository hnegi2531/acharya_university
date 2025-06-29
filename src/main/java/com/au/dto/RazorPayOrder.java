package com.au.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RazorPayOrder {
	  private int amount;
	    private int amount_paid;
	    private Map<String, Object> notes; 
	    private long created_at;
	    private int amount_due;
	    private String currency;
	    private String receipt;
	    private String id;
	    private String entity;
	    private String offer_id;
	    private int attempts;
	    private String status;
	    private List<Transfer> transfers;

}
