package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.TutionFeeWaiverAttachment;




@Transactional
@Repository
public interface TutionFeeWaiverAttachmentRepository extends JpaRepository<TutionFeeWaiverAttachment , Integer>{
	
	@Query(value = "select tut_fee_atch from TutionFeeWaiverAttachment tut_fee_atch where tut_fee_atch.tution_fee_waiver_id=?1")
	public TutionFeeWaiverAttachment findById1(Integer tution_fee_waiver_id);
	

}
