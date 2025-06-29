package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ApplicantPaidDetails;

@Transactional
@Repository
public interface ApplicantPaidDetailsRepository extends JpaRepository<ApplicantPaidDetails, Integer>{
	
	
	@Query(value = "SELECT dp from ApplicantPaidDetails dp where dp.active=true")
	public List<ApplicantPaidDetails> getAllActiveApplicantPaidDetails();

	
	@Query(value = "select new map(apd.applicant_paid_details_id AS applicant_paid_details_id,apd.lead_id AS lead_id,"
			+ "apd.opportunity_id AS opportunity_id,apd.mobile_number AS mobile_number,apd.email AS email,"
			+ "apd.amount AS amount,apd.candidate_name AS candidate_name,apd.payment_status AS payment_status,"
			+ "apd.application_no_npf AS application_no_npf,apd.order_id AS order_id,apd.razorpay_id AS razorpay_id,"
			+ "apd.created_date AS created_date, apd.modified_date AS modified_date,apd.modified_date AS modified_date,"
			+ "apd.created_by AS created_by,apd.modified_by AS modified_by,apd.active AS active,"
			+ "apd.created_username AS created_username,apd.modified_username AS modified_username ) from ApplicantPaidDetails apd "
			+ "where apd.lead_id=?1 And apd.opportunity_id=?2")
	public List<Map<String, Object>> getApplicantPaidDetailsBasedOnId(String lead_id, String opportunity_id);

}
