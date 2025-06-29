package com.au.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.BoardReceivedAmount;

@Repository
@Transactional
public interface BoardReceivedAmountRepository extends JpaRepository<BoardReceivedAmount, Integer> {
	
	@Query(value = "SELECT new map(" +
            "bra.boardReceivedAmountId as id, " +
            "bra.receivedYear as receivedYear, " +
            "bra.acYearId as acYearId, " +
            "bra.boardId as boardId, " +
            "bra.yearSem as yearSem, " +
            "bra.neftNo as neftNo, " +
            "bra.amount as amount, " +
            "bra.bulkReceiptNo as bulkReceiptNo, " +
            "bra.remarks as remarks, " +
            "bra.paidByStudent as paidByStudent, " +
            "bra.receivedDate as receivedDate, " +
            "bra.schoolId as schoolId, " +
            "bra.taggedAmount as taggedAmount, " +
            "bra.lockStatus as lockStatus, " +
            "bra.feeTemplateId as feeTemplateId, " +
            "bra.createdUsername as createdUsername, " +
            "bra.modifiedUsername as modifiedUsername, " +
            "bra.createdDate as createdDate, " +
            "bra.modifiedDate as modifiedDate, " +
            "bra.createdBy as createdBy, " +
            "bra.modifiedBy as modifiedBy, " +
            "bra.active as active,ft.fee_template_name as feeTemplateName, " +
            "brd.board_unique_name as boardUniqueName,brd.board_unique_short_name as boardUniqueShortName, " +
            "ay.ac_year as acYear, sch.school_name as schoolName, sch.school_name_short as schoolNameShort) " +
            "FROM BoardReceivedAmount bra " +
            "Left Join FeeTemplate ft On ft.fee_template_id=bra.feeTemplateId " +
            "Left Join Board brd On brd.board_unique_id=bra.boardId " +
            "Left Join Academic_year ay On ay.ac_year_id=bra.acYearId " +
            "Left Join Schools sch On sch.school_id=bra.schoolId " +
            "WHERE CONCAT(IFNULL(ft.fee_template_name, ''), '', IFNULL(brd.board_unique_name, ''), '', " +
            "IFNULL(brd.board_unique_short_name, ''), '', IFNULL(ay.ac_year, ''), '', IFNULL(ay.ac_year, ''), '', IFNULL(sch.school_name, '')) LIKE %?1%")
	Page<Object> sortedAndSearchedDetails(Pageable pageable, Object keyword);
	
	@Query(value = "SELECT new map(" +
            "bra.boardReceivedAmountId as id, " +
            "bra.receivedYear as receivedYear, " +
            "bra.acYearId as acYearId, " +
            "bra.boardId as boardId, " +
            "bra.yearSem as yearSem, " +
            "bra.neftNo as neftNo, " +
            "bra.amount as amount, " +
            "bra.bulkReceiptNo as bulkReceiptNo, " +
            "bra.remarks as remarks, " +
            "bra.paidByStudent as paidByStudent, " +
            "bra.receivedDate as receivedDate, " +
            "bra.schoolId as schoolId, " +
            "bra.taggedAmount as taggedAmount, " +
            "bra.lockStatus as lockStatus, " +
            "bra.feeTemplateId as feeTemplateId, " +
            "bra.createdUsername as createdUsername, " +
            "bra.modifiedUsername as modifiedUsername, " +
            "bra.createdDate as createdDate, " +
            "bra.modifiedDate as modifiedDate, " +
            "bra.createdBy as createdBy, " +
            "bra.modifiedBy as modifiedBy, " +
            "bra.active as active,ft.fee_template_name as feeTemplateName, " +
            "brd.board_unique_name as boardUniqueName,brd.board_unique_short_name as boardUniqueShortName, " +
            "ay.ac_year as acYear, sch.school_name as schoolName, sch.school_name_short as schoolNameShort) " +
            "FROM BoardReceivedAmount bra " +
            "Left Join FeeTemplate ft On ft.fee_template_id=bra.feeTemplateId " +
            "Left Join Board brd On brd.board_unique_id=bra.boardId " +
            "Left Join Academic_year ay On ay.ac_year_id=bra.acYearId " +
            "Left Join Schools sch On sch.school_id=bra.schoolId ")
	Page<Object> sortedDetails(Pageable pageable);

}
