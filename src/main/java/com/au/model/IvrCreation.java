package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "ivr_creation")
public class IvrCreation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ivr_creation_id;
	
		private String callid;
	    private String starttime;
	    private String endtime;
	    private String filename;
	    private String answeredtime;
	    
	    // Unique fields in the first JSON
	    private String emp_phone;
	    private String clicktocalldid;
	    private String callto;
	    private String dialstatus;
	    private String direction;
	    private String disconnectedby;
	    private String groupname;
	    private String agentname;

	    // Unique fields in the second JSON
	    private String executive;
	    private String customer;
	    private String status;
	    private String duration;
	    private String callType;
	    
	    private String exenumber;
	    private String url;
	    private String apikey;
	    
	    private Integer student_id;
	    private Integer proctor_id;
		@Lob
	    private String text_file;
		private String summarize;
	    
	    
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
