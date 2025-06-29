package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ScholarshipApprovalStatus;

@Repository
@Transactional
public interface ScholarshipApprovalStatusRepository extends JpaRepository<ScholarshipApprovalStatus, Integer> {
/*
	@Modifying(clearAutomatically = true)
	@Query(value = "update ScholarshipApprovalStatus s set s.doc1= :doc1 where s.scholarship_id= :scholarship_id")
	public void updateScholarship(@Param(value = "scholarship_id") Integer scholarship_id);
*/
	@Query(value="SELECT * FROM scholarship_approved_status where scholarship_id=?1",nativeQuery = true)
	public ScholarshipApprovalStatus findByScholarshipId(Integer id);

	@Query(value = "SELECT * FROM scholarship_approved_status where candidate_id=?1 and active=true",nativeQuery = true)
	public ScholarshipApprovalStatus getScholarshipByCandidateId(Integer candidate_id);
	
	@Query(value = "Select new map(sas.scholarship_approved_status_id as scholarship_approved_status_id,"
			+ " sas.verifier_remarks as verifier_remarks,sas.pre_approver_remarks as pre_approver_remarks,"
			+ " sas.approval as approval, sas.applied_date as applied_date, sas.approved_amount as approved_amount,"
			+ " sas.candidate_id as candidate_id, sas.is_approved as is_approved, sas.prev_approved_amount as prev_approved_amount,"
			+ " sas.scholarship_id as scholarship_id, sas.verified_amount as verified_amount, sas.year10_amount as year10_amount,"
			+ " sas.year11_amount as year11_amount, sas.year12_amount as year12_amount, sas.year1_amount as year1_amount,"
			+ " sas.year2_amount as year2_amount, sas.year3_amount as year3_amount, sas.year4_amount as year4_amount,"
			+ " sas.year5_amount as year5_amount, sas.year6_amount as year6_amount, sas.year7_amount as year7_amount,"
			+ " sas.year8_amount as year8_amount, sas.year9_amount as year9_amount)"
			+ " From ScholarshipApprovalStatus sas Where sas.student_id=?1 and sas.is_approved='yes' and sas.cancelBy is null and sas.active=true")
	public List<HashMap<String, Object>> scholarshipApprovalStatusForFeeReceipt(Integer student_id);
	
	@Query(value = "Select new map(sas.scholarship_approved_status_id as id,sas.student_id as student_id,"
			+ " sas.counselor_id as counselor_id,sas.verifier_remarks as verifier_remarks,sas.pre_approver_remarks as pre_approver_remarks,"
			+ " sas.approval as approval, sas.applied_date as applied_date, sas.approved_amount as approved_amount,sas.is_verified as is_verified,"
			+ " sas.candidate_id as candidate_id, sas.is_approved as is_approved, sas.prev_approved_amount as prev_approved_amount,"
			+ " sas.scholarship_id as scholarship_id, sas.verified_amount as verified_amount, sas.year10_amount as year10_amount,"
			+ " sas.year11_amount as year11_amount, sas.year12_amount as year12_amount, sas.year1_amount as year1_amount,"
			+ " sas.year2_amount as year2_amount, sas.year3_amount as year3_amount, sas.year4_amount as year4_amount,"
			+ " sas.year5_amount as year5_amount, sas.year6_amount as year6_amount, sas.year7_amount as year7_amount,"
			+ " sas.year8_amount as year8_amount, sas.year9_amount as year9_amount,sas.verified_date as verified_date,"
			+ " sas.approved_by as approved_by,sas.comments as comments,sas.verified_by as verified_by,"
			+ " sas.updated_approved_amount_date as updated_approved_amount_date,sas.cancel_remarks as cancel_remarks,"
			+ " sas.cancel_date as cancel_date,sas.created_username as created_username,sas.modified_username as modified_username,"
			+ " sas.created_date as created_date,sas.modified_date as modified_date,sas.created_by as created_by,sas.active as active) "
			+ " From ScholarshipApprovalStatus sas "
			+ "Where CONCAT(IfNull(sas.scholarship_approved_status_id,''),'',IfNull(sas.applied_date ,''),'',IfNull(sas.approval,''),'',"
			+ "IfNull(sas.is_verified,''),'',IfNull(sas.is_approved,''),'',IfNull(sas.verified_amount,''),'',IfNull(sas.verified_amount,''),'',"
			+ "IfNull(sas.updated_approved_amount_date,''),'',IfNull(sas.cancel_remarks,''),'',IfNull(sas.cancel_date,''),'',"
			+ "IfNull(sas.created_username,''),'',IfNull(sas.created_date,''),'',IfNull(sas.created_by,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(sas.scholarship_approved_status_id as id,sas.student_id as student_id,"
			+ " sas.counselor_id as counselor_id,sas.verifier_remarks as verifier_remarks,sas.pre_approver_remarks as pre_approver_remarks,"
			+ " sas.approval as approval, sas.applied_date as applied_date, sas.approved_amount as approved_amount,sas.is_verified as is_verified,"
			+ " sas.candidate_id as candidate_id, sas.is_approved as is_approved, sas.prev_approved_amount as prev_approved_amount,"
			+ " sas.scholarship_id as scholarship_id, sas.verified_amount as verified_amount, sas.year10_amount as year10_amount,"
			+ " sas.year11_amount as year11_amount, sas.year12_amount as year12_amount, sas.year1_amount as year1_amount,"
			+ " sas.year2_amount as year2_amount, sas.year3_amount as year3_amount, sas.year4_amount as year4_amount,"
			+ " sas.year5_amount as year5_amount, sas.year6_amount as year6_amount, sas.year7_amount as year7_amount,"
			+ " sas.year8_amount as year8_amount, sas.year9_amount as year9_amount,sas.verified_date as verified_date,"
			+ " sas.approved_by as approved_by,sas.comments as comments,sas.verified_by as verified_by,"
			+ " sas.updated_approved_amount_date as updated_approved_amount_date,sas.cancel_remarks as cancel_remarks,"
			+ " sas.cancel_date as cancel_date,sas.created_username as created_username,sas.modified_username as modified_username,"
			+ " sas.created_date as created_date,sas.modified_date as modified_date,sas.created_by as created_by,sas.active as active) "
			+ " From ScholarshipApprovalStatus sas ")
	public Page<Object> findAll2(Pageable pageable);

	@Modifying
	@Query(value = "update ScholarshipApprovalStatus d set d.active=false where d.candidate_id=?1")
	public void update(Integer id);
	
	@Modifying
	@Query(value = "update ScholarshipApprovalStatus d set d.active=true where d.scholarship_approved_status_id=?1")
	public void update1(Integer id);
	
	@Query(value = "Select new map(sas.scholarship_approved_status_id as id,sas.verifier_remarks as verifier_remarks,sas.pre_approver_remarks as pre_approver_remarks,"
			+ " sas.created_by as created_by, sas.verified_amount as verified_amount,sas.is_approved as is_approved,"
			+ " sas.candidate_id as candidate_id, sas.is_verified as is_verified,sd.student_id as student_id,sas.comments as comments,"
			+ "sa.scholarship_attachment_path as scholarship_attachment_path,sa.scholarship_attachment_file_name as scholarship_attachment_file_name,"
			+ " sas.scholarship_id as scholarship_id, sas.verified_amount as verified_amount,s.requested_scholarship as requested_scholarship,"
			+ "sas.created_username as requested_by,sas.created_date as requested_date,sas.pre_approval_date as pre_approval_date,"
			+ "ua.username as pre_approval_byName,sas.verified_date as verified_date,ua1.id as verified_by,ua1.username as verified_name,"
			+ " sd.auid as auid,sd.usn as usn,sd.student_name as student_name, sas.requestedByRemarks as requestedByRemarks)"
			+ " From ScholarshipApprovalStatus sas "
			+ " left join Student_Details sd on sd.candidate_id=sas.candidate_id"
			+ " left join Scholarship s on s.scholarship_id=sas.scholarship_id "
			+ "left join ScholarshipAttachment sa  on s.candidate_id= sa.candidate_id "
			+ "left join UserAuthentication ua1 on ua1.id=sas.verified_by "
			+ " left join UserAuthentication ua on ua.id=sas.pre_approver_by where sas.is_verified='yes' And sas.is_approved is NULL and sd.auid IS NOT null "
			+ "ORDER BY sas.created_date desc")
	public List<HashMap<String,Object>> getIsVerifiedDataForIndex ();
	
	@Query(value = "Select new map(sas.scholarship_approved_status_id as id,sas.verifier_remarks as verifier_remarks,sas.pre_approver_remarks as pre_approver_remarks,"
			+ " sas.created_by as created_by, sas.created_date as created_date, sas.verified_amount as verified_amount,"
			+ " sas.is_approved as is_approved,sas.approved_by as approved_by,sas.approved_date as approved_date,"
			+ " ua.username as approved_by_name,ua.id as userId,sas.comments as comments,sas.approved_amount as approved_amount,"
			+ "sa.scholarship_attachment_path as scholarship_attachment_path,sa.scholarship_attachment_file_name as scholarship_attachment_file_name,"
			+ " sas.candidate_id as candidate_id, sas.is_verified as is_verified,ua.username as username,sd.student_id as student_id,"
			+ " sas.scholarship_id as scholarship_id, sas.verified_amount as verified_amount,s.requested_scholarship as requested_scholarship,"
			+ "sas.created_username as requested_by,sas.created_date as requested_date,sas.pre_approval_date as pre_approval_date,"
			+ "ua2.username as pre_approval_byName,sas.verified_date as verified_date,ua1.id as verified_by,ua1.username as verified_name,"
			+ "sd.auid as auid,sd.usn as usn,sd.student_name as student_name, sas.requestedByRemarks as requestedByRemarks,"
			+ "sas.cancel_remarks as cancel_remarks, sas.cancel_date as cancel_date, sas.cancelBy as cancelBy, ua3.username as cancelByUsername)"
			+ " From ScholarshipApprovalStatus sas "
			+ " left join Student_Details sd on sd.candidate_id=sas.candidate_id"
			+ " left join Scholarship s on s.scholarship_id=sas.scholarship_id "
			+ "left join ScholarshipAttachment sa  on s.candidate_id= sa.candidate_id "
			+ "left join UserAuthentication ua on ua.id=sas.approved_by "
			+ "left join UserAuthentication ua1 on ua1.id=sas.verified_by "
			+ " left join UserAuthentication ua2 on ua2.id=sas.pre_approver_by "
			+ "left join UserAuthentication ua3 on ua3.id=sas.cancelBy "
			+ "where sd.ac_year_id=?1 and sas.is_approved Is Not NULL ORDER BY sas.created_date desc")
	public List<HashMap<String,Object>> getIsApprovedDataForIndex (Integer ac_year_id);

	@Query(value=" select * from scholarship_approved_status where student_id=:studentId and is_approved='yes' and active=true ", nativeQuery = true)
	public ScholarshipApprovalStatus getApprovedScholarShipbyYearAndStudentId(@Param("studentId") Integer studentId);
	
	@Query(value=" select * from scholarship_approved_status where student_id=?1 and is_approved='yes' and active=true ", nativeQuery = true)
	public List<ScholarshipApprovalStatus> getYearWiseDataByStudentId(Integer student_id);

	@Query(value = "SELECT * FROM scholarship_approved_status where candidate_id=?1 and is_verified='yes' and active=true",nativeQuery = true)
	public ScholarshipApprovalStatus getScholarshipDetailsByCandidateId(Integer candidate_id);

	@Query(value = " select COALESCE(sum(year1_amount),0) as year1 ,COALESCE(sum(year2_amount),0) as year2,COALESCE(sum(year3_amount),0) as year3 ,COALESCE(sum(year4_amount),0) as year4, " +
			"     COALESCE(sum(year5_amount),0) as year5,COALESCE(sum(year6_amount),0)as year6,COALESCE(sum(year7_amount),0) as year7,COALESCE(sum(year8_amount),0) as year8,COALESCE(sum(year9_amount),0) as year9, " +
			"     COALESCE(sum(year10_amount),0) as year10,COALESCE(sum(year11_amount),0) as year11,COALESCE(sum(year12_amount),0) as year12 " +
			"     from scholarship_approved_status where student_id=?1 and is_approved='yes' and active=true ", nativeQuery = true)
	Map<String, Object> getApprovedScholarShipbyStudentId(Integer studentId);
}
