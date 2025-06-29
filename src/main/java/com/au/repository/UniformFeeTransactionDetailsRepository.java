package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.dto.CandidateTransactionDetailsDTO;
import com.au.model.UniformFeeTransactionDetails;

@Repository
public interface UniformFeeTransactionDetailsRepository extends JpaRepository<UniformFeeTransactionDetails, Integer>{

	@Query(value="select u from UniformFeeTransactionDetails u where u.uniformTransactionId=:uniformTransactionId")
	List<UniformFeeTransactionDetails> getAllByUniformTransactionId(Integer uniformTransactionId);

	@Query(value=" select new com.au.dto.CandidateTransactionDetailsDTO( r.orderId, r.paymentId, r.receiptId, r.transactionDate, r.status, r.amount  ) from UniformTransaction r where r.studentId=:studentId and r.status='success' order by r.created_date desc ")
	List<CandidateTransactionDetailsDTO> getUniformTransactionDetails(Integer studentId);

	
}
