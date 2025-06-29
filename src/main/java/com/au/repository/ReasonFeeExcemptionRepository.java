package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ProgramType;
import com.au.model.ReasonFeeExcemption;

@Repository
@Transactional
public interface ReasonFeeExcemptionRepository extends JpaRepository<ReasonFeeExcemption, Integer>{
	
	
	@Query(value = "SELECT r FROM ReasonFeeExcemption r where active=true")
	public List<ReasonFeeExcemption> listAllActiveDetails();

	@Modifying
	@Query(value = "update ReasonFeeExcemption r set r.active=false where r.fee_exemption_id=?1")	
	public void updateReasonFeeExcemption(Integer fee_exemption_id);

	
	@Modifying
	@Query(value = "update ReasonFeeExcemption r set r.active=true where r.fee_exemption_id=?1")	
	public void updateReasonFeeExcemption1(Integer fee_exemption_id);
	
	@Query(value = "Select new map(re.fee_exemption_id as id,re.reasion_for_fee_exemption as reasion_for_fee_exemption,"
			+ "re.created_date as created_date,re.modified_date as modified_date,re.created_by as created_by,re.exemption_status as exemption_status,"
			+ "re.modified_by as modified_by,re.active as active,re.created_username as created_username,re.modified_username as modified_username) From ReasonFeeExcemption re "
			+ "Where CONCAT(IfNull(re.fee_exemption_id,''),'',IfNull(re.reasion_for_fee_exemption,''),'',IfNull(re.created_date,''),'',IfNull(re.created_username,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(re.fee_exemption_id as id,re.reasion_for_fee_exemption as reasion_for_fee_exemption,"
			+ "re.created_date as created_date,re.modified_date as modified_date,re.created_by as created_by,re.exemption_status as exemption_status,"
			+ "re.modified_by as modified_by,re.active as active,re.created_username as created_username,re.modified_username as modified_username) From ReasonFeeExcemption re")
	public Page<Object> findAll2(Pageable pageable);
	
	@Query(value = "select count(*) from ReasonFeeExcemption r where r.reasion_for_fee_exemption=?1 and r.active=true")
	public Integer countReasonFeeExcemption(String reasion_for_fee_exemption);
	
	@Query(value = "select count(*) from ReasonFeeExcemption r where r.fee_exemption_id != ?1 and r.reasion_for_fee_exemption=?2 and r.active=true")
	public Integer countReasonFeeExcemptionForUpdate(Integer fee_exemption_id,String reasion_for_fee_exemption);

}
