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

import lombok.Data;

@Entity
@Table(name="change_of_course_program_attachment")
@Data
public class ChangeOfCourseProgramAttachment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer changeOfCourseProgramAttachmentId;
	private Integer newStudentId;
	private String changeOfCourseProgramAttachmentPath;
	private String changeOfCourseProgramAttachmentFileName;
	private String changeOfCourseProgramAttachmentType;
	private Integer amount;
	private String remarks;
	private Boolean approvalStatus;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date created_date;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified_date;
	
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Boolean active;

}
