package com.au.repository;

import com.au.model.BoardTagAmount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

/**
 * Author: Anjan
 * Date: 19-12-2024
 * Description: AcerpStdTagBoardAmountRepository class
 */
public interface BoardTagAmountRepository extends JpaRepository<BoardTagAmount, Integer> {

    @Query(value ="Select sum(bta.paid) as paid,sum(bta.remaining_balance) as remaining_balance From board_tag_amount bta " +
            "Where bta.student_id=?1 and bta.fee_template_id=?2 and bta.year_sem=?3 and bta.active=true",nativeQuery = true)
    Map<String, Object> paidAmount(Integer studentId, Integer feeTemplateId, Integer yearOrSem);

    @Query(value = "SELECT new map(" +
            "bta.tag_auid_id as tag_auid_id, bta.student_id as student_id, " +
            "bta.institute_id as institute_id, bta.ac_year_id as ac_year_id, bta.fc_year_id as fc_year_id, " +
            "bta.course_branch_assignment_id as course_branch_assignment_id, bta.fee_template_id as fee_template_id, " +
            "bta.board_id as board_id, bta.board_receivable_id as board_receivable_id, " +
            "bta.to_pay_from_board as to_pay_from_board, bta.received_from_board as received_from_board, " +
            "bta.paid as paid, bta.remaining_balance as remaining_balance, bta.remarks as remarks, " +
            "bta.created_by as created_by, bta.modified_by as modified_by, bta.created_date as created_date, " +
            "bta.modified_date as modified_date, bta.active as active, bta.year_sem as year_sem, " +
            "bta.paid_receipt_no as paid_receipt_no,sd.student_name as student_name,sd.auid as auid,ac.ac_year as ac_year," +
            "ps.program_specialization_name as program_specialization_name,fy.financial_year as financial_year," +
            "ft.fee_template_name as fee_template_name,bo.board_unique_name as board_unique_name,sch.school_name as school_name) " +
            "FROM BoardTagAmount bta " +
            "Left join Student_Details sd on sd.student_id = bta.student_id " +
            "Left join Schools sch on sch.school_id = bta.institute_id " +
            "Left join Academic_year ac on ac.ac_year_id = bta.ac_year_id " +
            "Left Join FinancialYear fy on fy.financial_year_id = bta.fc_year_id " +
            "Left Join ProgramSpecilization ps on ps.program_specialization_id = bta.course_branch_assignment_id " +
            "Left Join FeeTemplate ft on ft.fee_template_id = bta.fee_template_id " +
            "Left Join Board bo on bo.board_unique_id = bta.board_id " +
            "WHERE bta.active = true AND CONCAT(IFNULL(bta.auid, ''), '', IFNULL(bta.student_id, ''), '', IFNULL(bta.institute_id, ''), '', " +
            "IFNULL(bta.ac_year_id, ''), '', IFNULL(bta.fee_template_id, ''), '', IFNULL(bta.board_id, ''), '', " +
            "IFNULL(bta.remarks, ''), '', IFNULL(bta.year_sem, '')) LIKE %:keyword%")
    Page<Object> listAll1(Pageable pageable, Object keyword);

    @Query(value = "SELECT new map(bta.tag_auid_id as tag_auid_id, bta.auid as auid, bta.student_id as student_id, " +
            "bta.institute_id as institute_id, bta.ac_year_id as ac_year_id, bta.fc_year_id as fc_year_id, " +
            "bta.course_branch_assignment_id as course_branch_assignment_id, bta.fee_template_id as fee_template_id, " +
            "bta.board_id as board_id, bta.board_receivable_id as board_receivable_id, " +
            "bta.to_pay_from_board as to_pay_from_board, bta.received_from_board as received_from_board, " +
            "bta.paid as paid, bta.remaining_balance as remaining_balance, bta.remarks as remarks, " +
            "bta.created_by as created_by, bta.modified_by as modified_by, bta.created_date as created_date, " +
            "bta.modified_date as modified_date, bta.active as active, bta.year_sem as year_sem, " +
            "bta.paid_receipt_no as paid_receipt_no,sd.student_name as student_name,sd.auid as auid,ac.ac_year as ac_year," +
            "ps.program_specialization_name as program_specialization_name,fy.financial_year as financial_year," +
            "ft.fee_template_name as fee_template_name,bo.board_unique_name as board_unique_name,sch.school_name as school_name) " +
            "FROM BoardTagAmount bta " +
            "Left join Student_Details sd on sd.student_id = bta.student_id " +
            "Left join Schools sch on sch.school_id = bta.institute_id " +
            "Left join Academic_year ac on ac.ac_year_id = bta.ac_year_id " +
            "Left Join FinancialYear fy on fy.financial_year_id = bta.fc_year_id " +
            "Left Join ProgramSpecilization ps on ps.program_specialization_id = bta.course_branch_assignment_id " +
            "Left Join FeeTemplate ft on ft.fee_template_id = bta.fee_template_id " +
            "Left Join Board bo on bo.board_unique_id = bta.board_id ")
    Page<Object> listAll2(Pageable pageable);
}
