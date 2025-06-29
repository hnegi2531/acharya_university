package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.exception.ResourceNotFoundException;
import com.au.model.ScholarshipVoucherHeadWiseAmountDetailsHistory;
import com.au.model.ScholarshipVoucherHeadWiseAmountDetailsHistory;

@Repository
@Transactional
public interface ScholarshipVoucherHeadWiseAmountDetailsHistoryRepository extends JpaRepository<ScholarshipVoucherHeadWiseAmountDetailsHistory, Integer> {

	@Query(value ="Select new map(svhwad.sch_voucher_head_wise_amount_history_id as id,svhwad.sch_voucher_head_wise_amount_id as sch_voucher_head_wise_amount_id,"
			+ "svhwad.scholarship_id as scholarship_id,svhwad.voucher_head_new_id as voucher_head_new_id,svhwad.amount as amount,"
			+ "svhwad.scholarship_year as scholarship_year,svhwad.created_date as created_date,svhwad.created_by as created_by,"
			+ "svhwad.active as active,svhwad.created_username as created_username,vhn.voucher_head as voucher_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name) From ScholarshipVoucherHeadWiseAmountDetailsHistory svhwad "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id = svhwad.voucher_head_new_id "
			+ "Where svhwad.active=true And CONCAT(IfNull(svhwad.amount,''),'',IfNull(svhwad.scholarship_year,'')"
			+ ",'',IfNull(svhwad.created_date,''),'',IfNull(svhwad.created_by,''),'',IfNull(svhwad.created_username,''))"
			+ " LIKE %?1%")
		public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(svhwad.sch_voucher_head_wise_amount_history_id as id,svhwad.sch_voucher_head_wise_amount_id as sch_voucher_head_wise_amount_id,"
			+ "svhwad.scholarship_id as scholarship_id,svhwad.voucher_head_new_id as voucher_head_new_id,svhwad.amount as amount,"
			+ "svhwad.scholarship_year as scholarship_year,svhwad.created_date as created_date,svhwad.created_by as created_by,"
			+ "svhwad.active as active,svhwad.created_username as created_username,vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name) From ScholarshipVoucherHeadWiseAmountDetailsHistory svhwad "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id = svhwad.voucher_head_new_id")
	public Page<Object> findAll2(Pageable pageable);
	
	@Query(value ="Select new map(svhwad.sch_voucher_head_wise_amount_history_id as id,svhwad.sch_voucher_head_wise_amount_id as sch_voucher_head_wise_amount_id,"
			+ "svhwad.scholarship_id as scholarship_id,svhwad.voucher_head_new_id as voucher_head_new_id,svhwad.amount as amount,"
			+ "svhwad.scholarship_year as scholarship_year,svhwad.created_date as created_date,svhwad.created_by as created_by,"
			+ "svhwad.active as active,svhwad.created_username as created_username,vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name) From ScholarshipVoucherHeadWiseAmountDetailsHistory svhwad "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id = svhwad.voucher_head_new_id where svhwad.scholarship_id=?1 and svhwad.active=true")
	public List<HashMap<String,Object>> scholarshipHeadWiseAmountDetailsOnScholarshipId(Integer scholarship_id);
	
	@Query(value ="Select new map(svhwad.sch_voucher_head_wise_amount_history_id as id,svhwad.sch_voucher_head_wise_amount_id as sch_voucher_head_wise_amount_id,"
			+ "svhwad.scholarship_id as scholarship_id,svhwad.voucher_head_new_id as voucher_head_new_id,svhwad.amount as amount,"
			+ "svhwad.scholarship_year as scholarship_year,svhwad.created_date as created_date,svhwad.created_by as created_by,"
			+ "svhwad.active as active,svhwad.created_username as created_username,vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name) From ScholarshipVoucherHeadWiseAmountDetailsHistory svhwad "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id = svhwad.voucher_head_new_id where svhwad.scholarship_id=?1 and svhwad.voucher_head_new_id=?2 and svhwad.active=true")
	public List<HashMap<String,Object>> scholarshipHeadWiseAmountDetailsOnVocherHeadNewId(Integer scholarship_id,Integer voucher_head_new_id);

	@Modifying
	@Query(value = "update ScholarshipVoucherHeadWiseAmountDetailsHistory d set d.active=false where d.sch_voucher_head_wise_amount_history_id=?1")
	public void update(Integer id);
	
	@Modifying
	@Query(value = "update ScholarshipVoucherHeadWiseAmountDetailsHistory d set d.active=true where d.sch_voucher_head_wise_amount_history_id=?1")
	public void update1(Integer id);
	
	@Query(value ="Select ifNull(Sum(svhwad.amount),0) From scholarship_vocher_head_wise_amount_details svhwad "
			+ "Where svhwad.scholarship_id in ?1 And svhwad.voucher_head_new_id=?2 And svhwad.scholarship_year=?3 And svhwad.active=true",nativeQuery=true)
	public Integer getScholarshipAmountOnVocherHeadWise(List<Integer> scholarship_id,Integer voucher_head_new_id,Integer scholarship_year);

	
	
}
