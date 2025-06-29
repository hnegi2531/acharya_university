package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.HostelHeadWiseAmt;

@Transactional
@Repository
public interface HostelHeadWiseAmtRepository extends JpaRepository<HostelHeadWiseAmt, Integer>{
	
	@Query(value = "Select new map(hha.hostel_head_wise_amt_id as id,hha.voucher_head_new_id as voucher_head_new_id,"
			+ "hha.amount as amount,hha.hostel_fee_template_id as hostel_fee_template_id,hha.createdDate as createdDate,"
			+ "hha.modifiedDate as modifiedDate,hha.createdBy as createdBy,hha.modifiedBy as modifiedBy,hha.createdUsername as createdUsername,"
			+ "hha.modifiedUsername as modifiedUsername,hha.active as active,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "hft.template_name as template_name) From HostelHeadWiseAmt hha "
			+ "Left join VoucherHeadNew vhn On vhn.voucher_head_new_id=hha.voucher_head_new_id "
			+ "Left join HostelFeeTemplate hft On hft.hostel_fee_template_id=hha.hostel_fee_template_id "
			+ "Where CONCAT(IfNull(hha.hostel_head_wise_amt_id,''),'',IfNull(hha.amount,''),'',IfNull(hha.createdDate,''),'',IfNull(vhn.voucher_head_short_name,''),'',IfNull(hha.createdUsername,''),'',IfNull(hft.template_name,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(hha.hostel_head_wise_amt_id as id,hha.voucher_head_new_id as voucher_head_new_id,"
			+ "hha.amount as amount,hha.hostel_fee_template_id as hostel_fee_template_id,hha.createdDate as createdDate,"
			+ "hha.modifiedDate as modifiedDate,hha.createdBy as createdBy,hha.modifiedBy as modifiedBy,hha.createdUsername as createdUsername,"
			+ "hha.modifiedUsername as modifiedUsername,hha.active as active,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "hft.template_name as template_name) From HostelHeadWiseAmt hha "
			+ "Left join VoucherHeadNew vhn On vhn.voucher_head_new_id=hha.voucher_head_new_id "
			+ "Left join HostelFeeTemplate hft On hft.hostel_fee_template_id=hha.hostel_fee_template_id ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "Select new map(hha.hostel_head_wise_amt_id as hostel_head_wise_amt_id,hha.voucher_head_new_id as voucher_head_new_id,"
			+ "hha.amount as amount,hha.hostel_fee_template_id as hostel_fee_template_id,hha.createdDate as createdDate,"
			+ "hha.modifiedDate as modifiedDate,hha.createdBy as createdBy,hha.modifiedBy as modifiedBy,hha.createdUsername as createdUsername,"
			+ "hha.modifiedUsername as modifiedUsername,hha.active as active,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "hft.template_name as template_name) From HostelHeadWiseAmt hha "
			+ "Left join VoucherHeadNew vhn On vhn.voucher_head_new_id=hha.voucher_head_new_id "
			+ "Left join HostelFeeTemplate hft On hft.hostel_fee_template_id=hha.hostel_fee_template_id Where hha.hostel_fee_template_id=?1 and hha.active=true")
	public List<HashMap<String, Object>> hostelHeadWiseAmtOnFeeTemplateId(Integer hostel_fee_template_id);

}
