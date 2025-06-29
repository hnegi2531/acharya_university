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
import javax.validation.constraints.Size;

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
@Table(name = "hostel_waiver")
public class HostelWaiver {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer hostel_waiver_id;
	private Integer student_id;
	private Integer ac_year_id;
	private Integer paid_amount;
	private Integer total_amount;
	private String remarks;
	private Boolean approve_status;
	private Boolean active;
	
	private Integer hw_attachment_id; 
	private String hw_attachment_path;
	private String hw_attachment_file_name;
	private String hw_attachement_type;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	private Integer modified_by;
	
	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;
	private Integer hostel_bed_id;
	private Integer hostel_bed_assignment_id;
	@Size(max = 50)
	private String type;
	
}
