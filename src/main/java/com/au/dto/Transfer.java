package com.au.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transfer {
	private int amount;
    private String recipient;
    private String currency;
    private boolean on_hold;
    private Long on_hold_until;
    private List<String> linked_account_notes;
    private String source;
    private Map<String, Object> notes;
    private String id;
	   
}
