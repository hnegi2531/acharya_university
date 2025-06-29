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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transport_maintenance")
public class TransportMaintenance {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer transport_maintenance_id;
	
	private Integer service_type_id; 
	private Integer maintenance_id;
	private Integer dept_id;
	private Integer school_id;
	private String type_of_vehicle;
	private String reporting_date_time;
    private String duration;
    private String reporting_place;
    private String report_to_person;
    private String report_to_person_number;
    private String requesting_from_datetime;
    private String requesting_to_datetime; 
    private String place_of_visit;  
    private String purpose;
	private String request_stage;
	private String request_status;
	private String alloted_person_name;    // Driver Name
	private String alloted_person_number;  // Driver Number
	private String attending_remarks;      // Driver Remarks
	private Integer attend_status;
	private String dateOfAttended;
	private String dateOfClosed;
    private String username;
    private String cancelled_status;
    private Integer user_id;
    private String complaintStatus;
    
    @Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	@Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;
	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	

}
