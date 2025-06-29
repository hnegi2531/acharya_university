package com.au.model;

import java.util.Date;
import java.util.HashMap;

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

import com.au.dto.GrnDraftJournalVoucherDto;

import lombok.Data;

@Data
@Entity
@Table(name = "student_attachment")
public class StudentAttachments {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer attachments_id;
	private Integer student_id;
	private String attachments_file_name;

	private String attachments_file_path;
	
	private String educational_attach;
	private String educational_attach1;
	
	private String personal_attach;
	private String qualification_attach;
	private Integer attachments_subcategory_id;
	private Integer candidate_id;
	private HashMap<String, String> attachments_file_path1;
	
	@Column(name = "created_date",updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modified_date;
	
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	
	private String created_username;
	private String modified_username;

}
