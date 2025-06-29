package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ScholarshipVocherHeadWiseAmountDetails;

@Repository
@Transactional
public interface ScholarshipVocherHeadWiseAmountDetailsRepository extends JpaRepository<ScholarshipVocherHeadWiseAmountDetails,Integer>{
	
	
	@Query(value ="Select new map(svhwad.sch_vocher_head_wise_amount_id as sch_vocher_head_wise_amount_id,"
			+ "svhwad.scholarship_id as scholarship_id,svhwad.voucher_head_new_id as voucher_head_new_id,svhwad.amount as amount,"
			+ "svhwad.scholarship_year as scholarship_year,svhwad.created_date as created_date,svhwad.created_by as created_by,"
			+ "svhwad.active as active,svhwad.created_username as created_username,vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name) From ScholarshipVocherHeadWiseAmountDetails svhwad "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id = svhwad.voucher_head_new_id where svhwad.scholarship_id=?1 and svhwad.active=true")
	public List<HashMap<String,Object>> scholarshipHeadWiseAmountDetailsOnScholarshipId(Integer scholarship_id);
	
	@Query(value ="Select new map(svhwad.sch_vocher_head_wise_amount_id as sch_vocher_head_wise_amount_id,"
			+ "svhwad.scholarship_id as scholarship_id,svhwad.voucher_head_new_id as voucher_head_new_id,svhwad.amount as amount,"
			+ "svhwad.scholarship_year as scholarship_year,svhwad.created_date as created_date,svhwad.created_by as created_by,"
			+ "svhwad.active as active,svhwad.created_username as created_username,vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name) From ScholarshipVocherHeadWiseAmountDetails svhwad "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id = svhwad.voucher_head_new_id where svhwad.scholarship_id=?1 and svhwad.voucher_head_new_id=?2 and svhwad.active=true")
	public List<HashMap<String,Object>> scholarshipHeadWiseAmountDetailsOnVocherHeadNewId(Integer scholarship_id,Integer voucher_head_new_id);

	@Modifying
	@Query(value = "update ScholarshipVocherHeadWiseAmountDetails d set d.active=false where d.sch_vocher_head_wise_amount_id=?1")
	public void update(Integer id);
	
	@Modifying
	@Query(value = "update ScholarshipVocherHeadWiseAmountDetails d set d.active=true where d.sch_vocher_head_wise_amount_id=?1")
	public void update1(Integer id);
	
	@Query(value ="Select ifNull(Sum(svhwad.amount),0) From scholarship_vocher_head_wise_amount_details svhwad "
			+ "Where svhwad.scholarship_id in ?1 And svhwad.voucher_head_new_id=?2 And svhwad.scholarship_year=?3 And svhwad.active=true",nativeQuery=true)
	public Integer getScholarshipAmountOnVocherHeadWise(List<Integer> scholarship_id,Integer voucher_head_new_id,Integer scholarship_year);
	
	@Query(value ="Select new map(svhwad.sch_vocher_head_wise_amount_id as sch_vocher_head_wise_amount_id,"
			+ "svhwad.scholarship_id as scholarship_id,svhwad.voucher_head_new_id as voucher_head_new_id,svhwad.amount as amount,"
			+ "svhwad.scholarship_year as scholarship_year,svhwad.created_date as created_date,svhwad.created_by as created_by,"
			+ "svhwad.active as active,svhwad.created_username as created_username,vhn.voucher_head as voucher_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name) From ScholarshipVocherHeadWiseAmountDetails svhwad "
			+ "Left join Scholarship sch on sch.scholarship_id = svhwad.scholarship_id "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id = svhwad.voucher_head_new_id where sch.student_id=?1 and svhwad.active=true")
	public List<HashMap<String,Object>> scholarshipHeadWiseAmountDetailsOnStudentId(Integer student_id);
	
}
