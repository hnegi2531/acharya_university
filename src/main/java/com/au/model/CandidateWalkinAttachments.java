package com.au.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "candidate_walkin_attachments")
public class CandidateWalkinAttachments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer candidate_attachment_id;
	private Integer candidate_id;
	private String attachment_path;
	private String attachment_file_name;
	private String attachment_type;
	private String attachment_purpose;
	private Boolean active;
}
