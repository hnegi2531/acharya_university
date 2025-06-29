package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.IntakePermitHistory;


@Transactional
@Repository
public interface IntakePermitHistoryRepository extends JpaRepository<IntakePermitHistory, Integer>
{

	@Query(value ="Select new map(iph.intake_permit_history_id as intake_permit_history_id,iph.intake_permit_id as intake_permit_id,iph.intake_id as intake_id,iph.fee_admission_sub_category_id as fee_admission_sub_category_id,"
			+ "iph.intake_permit as intake_permit,iph.created_date as created_date,iph.created_by as created_by,iph.intake_history_id as intake_history_id,"
			+ "iph.created_username as created_username,fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name) From IntakePermitHistory iph "
			+ "Left Join FeeAdmissionSubCategory fasc on fasc.fee_admission_sub_category_id=iph.fee_admission_sub_category_id where iph.intake_history_id in ?1")
	public List<HashMap<String, Object>> getIntakePermitHistoryDetails(List<Integer> intake_history_ids);
	
	
}




