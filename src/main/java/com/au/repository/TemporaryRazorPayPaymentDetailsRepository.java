package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.TemporaryRazorPayPaymentDetails;

@Repository
public interface TemporaryRazorPayPaymentDetailsRepository  extends JpaRepository<TemporaryRazorPayPaymentDetails, Long>{

	@Query(value=" select rt from TemporaryRazorPayPaymentDetails rt where rt.razorPayTransactionId=:razorPayTransactionId ")
	List<TemporaryRazorPayPaymentDetails> getByRazorPayTransactionId(Long razorPayTransactionId );

	
}
