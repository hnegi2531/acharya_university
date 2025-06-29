package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "infrastructure_blocks")
public class InfrastructureBlocks {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer block_id;
	@Column(unique = true)
	private String block_name;
	@Column(unique = true)
	private String block_short_name;
	private Integer school_id; //institute_id
	private String blockcode;
	private Integer total_no_of_floor;
	private Float total_built_up_area;
	private String survey_number;
	private String document_number;
	private Integer facility_type_id;
	private String remarks;
	private Integer basement;
	private Boolean show_in_event;
	private Boolean active;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;

}
