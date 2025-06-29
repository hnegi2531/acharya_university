package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.FeeTemplateRemarks;

@Transactional
@Repository
public interface FeeTemplateRemarksRepository  extends JpaRepository<FeeTemplateRemarks, Integer>{

	@Query(value = "SELECT co from FeeTemplateRemarks co where co.active=true")
	public List<FeeTemplateRemarks> findAll11();
	
	
	@Modifying
	@Query(value = "update FeeTemplateRemarks c set c.active=false where c.fee_template_remarks_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update FeeTemplateRemarks c set c.active=true where c.fee_template_remarks_id=?1")
	public void activate(Integer id);

	@Query(value = "select new map(ftr.fee_template_remarks_id as fee_template_remarks_id,ftr.fee_template_id as fee_template_id,ftr.remarks as remarks,"
			+ "ftr.created_username as created_username,ftr.modified_username as modified_username,"
			+ "ftr.created_date as created_date,ftr.modified_date as modified_date,ftr.created_by as created_by,"
			+ "ftr.modified_by as modified_by,ftr.active as active) from FeeTemplateRemarks ftr "
			+ "where ftr.fee_template_id=?1 And ftr.active=true")
	public List<Map<String, Object>> getFeeTemplateRemarksDetails(Integer fee_template);

}
