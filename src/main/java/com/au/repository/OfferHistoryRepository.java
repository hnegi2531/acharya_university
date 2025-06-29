package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.OfferHistoryResponse;
import com.au.model.OfferHistory;
import com.au.model.OfferHistoryRequest;


@Transactional
@Repository
public interface OfferHistoryRepository extends JpaRepository<OfferHistory,Integer> {
	
	@Query(value ="Select oh From OfferHistory oh Where oh.job_id=?1 And oh.active=true")
	public List<OfferHistory>  offerHistoryByJobId(Integer job_id);
	
	
	@Query(value ="Select oh From OfferHistory oh Where oh.emp_id=?1 And oh.active=true")
	public List<OfferHistory>  offerHistoryByEmployeeId(Integer emp_id);


	public OfferHistoryResponse save(OfferHistoryRequest offerHistoryRequest);

}
