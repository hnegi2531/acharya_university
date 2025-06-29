package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.ScholarshipApprovalStatus;
import com.au.model.ScholarshipApprovalStatusHistory;

@Repository
@Transactional
public interface ScholarshipApprovalStatusHistoryRepository extends JpaRepository<ScholarshipApprovalStatusHistory, Integer> {
	
	@Query(value = "Select new map(sash.scholarship_approved_status_history_id as id, sash.scholarship_approved_status_id as scholarship_approved_status_id,sash.student_id as student_id,"
			+ " sash.counselor_id as counselor_id,sash.verifier_remarks as verifier_remarks,sash.pre_approver_remarks as pre_approver_remarks,"
			+ " sash.approval as approval, sash.applied_date as applied_date, sash.approved_amount as approved_amount,sash.is_verified as is_verified,"
			+ " sash.candidate_id as candidate_id, sash.is_approved as is_approved, sash.prev_approved_amount as prev_approved_amount,"
			+ " sash.scholarship_id as scholarship_id, sash.verified_amount as verified_amount, sash.year10_amount as year10_amount,"
			+ " sash.year11_amount as year11_amount, sash.year12_amount as year12_amount, sash.year1_amount as year1_amount,"
			+ " sash.year2_amount as year2_amount, sash.year3_amount as year3_amount, sash.year4_amount as year4_amount,"
			+ " sash.year5_amount as year5_amount, sash.year6_amount as year6_amount, sash.year7_amount as year7_amount,"
			+ " sash.year8_amount as year8_amount, sash.year9_amount as year9_amount,sash.verified_date as verified_date,"
			+ " sash.approved_by as approved_by,sash.comments as comments,sash.verified_by as verified_by,"
			+ " sash.updated_approved_amount_date as updated_approved_amount_date,sash.cancel_remarks as cancel_remarks,"
			+ " sash.cancel_date as cancel_date,sash.created_username as created_username,sash.modified_username as modified_username,"
			+ " sash.created_date as created_date,sash.modified_date as modified_date,sash.created_by as created_by,sash.active as active) "
			+ " From ScholarshipApprovalStatusHistory sash "
			+ "Where CONCAT(IfNull(sash.scholarship_approved_status_id,''),'',IfNull(sash.applied_date ,''),'',IfNull(sash.approval,''),'',"
			+ "IfNull(sash.is_verified,''),'',IfNull(sash.is_approved,''),'',IfNull(sash.verified_amount,''),'',IfNull(sash.verified_amount,''),'',"
			+ "IfNull(sash.updated_approved_amount_date,''),'',IfNull(sash.cancel_remarks,''),'',IfNull(sash.cancel_date,''),'',"
			+ "IfNull(sash.created_username,''),'',IfNull(sash.created_date,''),'',IfNull(sash.created_by,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(sash.scholarship_approved_status_history_id as id,sash.scholarship_approved_status_id as scholarship_approved_status_id,sash.student_id as student_id,"
			+ " sash.counselor_id as counselor_id,sash.verifier_remarks as verifier_remarks,sash.pre_approver_remarks as pre_approver_remarks,"
			+ " sash.approval as approval, sash.applied_date as applied_date, sash.approved_amount as approved_amount,sash.is_verified as is_verified,"
			+ " sash.candidate_id as candidate_id, sash.is_approved as is_approved, sash.prev_approved_amount as prev_approved_amount,"
			+ " sash.scholarship_id as scholarship_id, sash.verified_amount as verified_amount, sash.year10_amount as year10_amount,"
			+ " sash.year11_amount as year11_amount, sash.year12_amount as year12_amount, sash.year1_amount as year1_amount,"
			+ " sash.year2_amount as year2_amount, sash.year3_amount as year3_amount, sash.year4_amount as year4_amount,"
			+ " sash.year5_amount as year5_amount, sash.year6_amount as year6_amount, sash.year7_amount as year7_amount,"
			+ " sash.year8_amount as year8_amount, sash.year9_amount as year9_amount,sash.verified_date as verified_date,"
			+ " sash.approved_by as approved_by,sash.comments as comments,sash.verified_by as verified_by,"
			+ " sash.updated_approved_amount_date as updated_approved_amount_date,sash.cancel_remarks as cancel_remarks,"
			+ " sash.cancel_date as cancel_date,sash.created_username as created_username,sash.modified_username as modified_username,"
			+ " sash.created_date as created_date,sash.modified_date as modified_date,sash.created_by as created_by,sash.active as active) "
			+ " From ScholarshipApprovalStatusHistory sash ")
	public Page<Object> findAll2(Pageable pageable);

	

	@Query(value=" select * from scholarship_approved_status_history where scholarship_id=?1", nativeQuery = true)
	public List<ScholarshipApprovalStatusHistory> getScholarshipApprovalStatusHistoryData(Integer scholarship_id);

}
