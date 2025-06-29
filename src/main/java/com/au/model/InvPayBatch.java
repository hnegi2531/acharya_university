package com.au.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="inv_pay_batch")
public class InvPayBatch {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "inv_pay_batch_id")
	private Long invPayBatchId;
	
	@Column(name= "file_path")
	private String filePath;
	
	@Column(name= "file_name")
	private String fileName;
	
	@Column(name= "total_entries")
	private Long totalEntries;

	@Column(name= "error_entries")
	private Long errorEntries;
	
	@Column(name= "valid_entries")
	private Long validEntries;
	
	@Column(name= "batch_code")
	private String batchCode;

}
