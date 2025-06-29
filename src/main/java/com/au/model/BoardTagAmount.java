package com.au.model;

import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Author: Anjan
 * Date: 17-12-2024
 * Description: AcerpStdTagBoardAmount class
 */

@Data
@Entity
public class BoardTagAmount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer tag_auid_id;
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
    Integer created_by;
    Integer modified_by;
    LocalDateTime created_date;
    LocalDateTime modified_date;
    Boolean active;
    String year_sem;
    Integer paid_receipt_no;

}
