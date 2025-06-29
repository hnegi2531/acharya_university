package com.au.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="debug_log")
public class DebugLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer debug_log_id;
	private Integer student_id;
	private Integer ac_year_id;
	private Integer school_id;
	private Integer hostel_fee_template_id;
	private Double total_amount;
	private Double paid;
	private Double waiver;
	private Double due;
	private String operation;

}
