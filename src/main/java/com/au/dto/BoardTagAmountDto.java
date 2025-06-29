package com.au.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Author: Anjan
 * Date: 19-12-2024
 * Description: AcerpStdTagBoardAmountDto class
 */
@Data
public class BoardTagAmountDto {
    String auid;
    Integer student_id;
    Integer institute_id;
    Integer ac_year_id;
    Integer fc_year_id;
    Integer course_branch_assignment_id;
    Integer fee_template_id;
    Integer board_id;
    Integer board_receivable_id;
    Float to_pay_from_board;
    Float received_from_board;
    Float paid;
    Float remaining_balance;
    String remarks;
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
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
    Boolean active;
    String year_sem;
    Integer paid_receipt_no;
}
