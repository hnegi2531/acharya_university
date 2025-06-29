package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.HostelFeeTemplateSlots;

@Repository
@Transactional
public interface HostelFeeTemplateSlotsRepository extends JpaRepository<HostelFeeTemplateSlots	, Integer>{
	
	@Query(value ="Select new map(hfts.hostel_fee_template_slot_id as id,hfts.minimum_amount as minimum_amount,"
			+ "hfts.due_date as due_date,hfts.hostel_fee_template_id as hostel_fee_template_id,hfts.createdDate as createdDate,"
			+ "hfts.modifiedDate as modifiedDate,hfts.createdBy as createdBy,hfts.modifiedBy as modifiedBy,hfts.createdUsername as createdUsername,"
			+ "hfts.modifiedUsername as modifiedUsername,hfts.active as active,hft.template_name as template_name) From HostelFeeTemplateSlots hfts "
			+ "Left join HostelFeeTemplate hft On hft.hostel_fee_template_id=hfts.hostel_fee_template_id "
			+ "Where CONCAT(IfNull(hfts.hostel_fee_template_slot_id,''),'',IfNull(hfts.minimum_amount,''),'',IfNull(hfts.createdDate,''),'',IfNull(hfts.createdBy,''),'',IfNull(hfts.createdUsername,''),'',IfNull(hft.template_name,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(hfts.hostel_fee_template_slot_id as id,hfts.minimum_amount as minimum_amount,"
			+ "hfts.due_date as due_date,hfts.hostel_fee_template_id as hostel_fee_template_id,hfts.createdDate as createdDate,"
			+ "hfts.modifiedDate as modifiedDate,hfts.createdBy as createdBy,hfts.modifiedBy as modifiedBy,hfts.createdUsername as createdUsername,"
			+ "hfts.modifiedUsername as modifiedUsername,hfts.active as active) From HostelFeeTemplateSlots hfts "
			+ "Left join HostelFeeTemplate hft On hft.hostel_fee_template_id=hfts.hostel_fee_template_id")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value ="Select hfts From HostelFeeTemplateSlots hfts where hfts.hostel_fee_template_id =?1 and hfts.active=true")
	public List<HostelFeeTemplateSlots> getHostelFeeTemplateSlots(Integer hostel_fee_template_id);

}
