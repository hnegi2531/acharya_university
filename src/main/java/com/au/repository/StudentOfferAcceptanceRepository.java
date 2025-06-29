package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.StudentOfferAcceptance;

@Repository
@Transactional
public interface StudentOfferAcceptanceRepository extends JpaRepository<StudentOfferAcceptance,Integer>{
	
	@Query(value="Select stoa From StudentOfferAcceptance stoa Where stoa.active=true")
	List<StudentOfferAcceptance> activeStudentOfferAcceptanceDetails();

	@Query(value="Select * From student_offer_acceptance stoa Where stoa.candidate_id=?1 and stoa.active=true order by student_offer_acceptance_id "
			+ "desc limit 1", nativeQuery=true)
	public StudentOfferAcceptance getDetailsByCandidateId(Integer candidate_id);

}
