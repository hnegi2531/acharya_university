package com.au.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.au.model.AdvanceMonthlyEmiDeduction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeeTemplateRequest {
	private String fee_template_name;
	private Integer ac_year_id;
	private String ac_year; // na
	private Integer school_id; // Fk
	private Integer program_id; // Fk
	private String program_sht; // na
	private String program_specialization_id;
	private Integer currency_type_id;
	private String currency_short; // na
	private Integer fee_admission_category_id;
	private String fee_admission_category; // na
	private Integer fee_admission_sub_category_id;
	private Boolean Is_paid_at_board; // na
	private String nationality;
	private Boolean Is_nri; // na
	private Integer program_type_id;
	private Integer approved_by; // na
	private Boolean approved_status; // na
	private Date approved_date;
	private Boolean active; // na
	private String remarks;
	private String is_saarc;
	private Integer lat_year_sem; 
	private String program_specialization;
	private Boolean uniform_status;
	private Boolean laptop_status;

}