package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.FeeHead;

@Repository
@Transactional
public interface FeeHeadRepository extends JpaRepository<FeeHead, Integer>{
	@Query(value = "select h from FeeHead h where h.active=true")
	public List<FeeHead> findAll1();

	@Modifying
	@Query(value = "update FeeHead h set h.active=false where h.fee_head_id=?1")
	public void updateFeeHead(Integer id);

	@Modifying
	@Query(value = "update FeeHead h set h.active=true where h.fee_head_id=?1")
	public void updateFeeHead1(Integer id);
	
	@Query(value = "Select new map(fh.fee_head_id as id,fh.fee_head as fee_head,fh.school_id as school_id,"
			+ "fh.charged_type_id as charged_type_id,fh.repeat_fee_head as fee_head_category,fh.tally_id as tally_id,"
			+ "fh.vendor_id as vendor_id,fh.created_date as created_date,fh.modified_date as modified_date,"
			+ "fh.created_by as created_by,fh.modified_by as modified_by,fh.created_username as created_username,"
			+ "fh.modified_username as modified_username,fh.active as active) From FeeHead fh "
			+ "Where CONCAT(IfNull(fh.fee_head_id,''),'',IfNull(fh.fee_head,''),'',IfNull(fh.charged_type_id,''),'',IfNull(fh.repeat_fee_head,''),'',IfNull(fh.created_date,''),'',IfNull(fh.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(fh.fee_head_id as id,fh.fee_head as fee_head,fh.school_id as school_id,"
			+ "fh.charged_type_id as charged_type_id,fh.repeat_fee_head as fee_head_category,fh.tally_id as tally_id,"
			+ "fh.vendor_id as vendor_id,fh.created_date as created_date,fh.modified_date as modified_date,"
			+ "fh.created_by as created_by,fh.modified_by as modified_by,fh.created_username as created_username,"
			+ "fh.modified_username as modified_username,fh.active as active) From FeeHead fh")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM FeeHead fh where fh.fee_head=?1 and fh.active=true")
	public Integer countOfFeeHead(String fee_head);
}
