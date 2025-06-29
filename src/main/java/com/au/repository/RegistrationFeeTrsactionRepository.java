package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.au.dto.CandidateTransactionDetailsDTO;
import com.au.model.RegistrationFeeTransaction;
import org.springframework.transaction.annotation.Transactional;

public interface RegistrationFeeTrsactionRepository extends JpaRepository<RegistrationFeeTransaction, Long> {

	RegistrationFeeTransaction findByOrderId(String razorpayOrderId);

	@Query(value=" select new com.au.dto.CandidateTransactionDetailsDTO( r.orderId, r.paymentId, r.receiptId, r.transactionDate, r.status, r.amount  ) from RegistrationFeeTransaction r where r.candidateId=:candidateId order by r.created_date desc ")
	List<CandidateTransactionDetailsDTO> getCandidateTransactionDetails(Integer candidateId);

	@Query(value="select r from RegistrationFeeTransaction r where r.candidateId=:candidateId and r.status='success' ")
	RegistrationFeeTransaction getTransactionDetailsByCandidateId(Integer candidateId);

	@Query(value="select r from RegistrationFeeTransaction r where r.receiptStatus = 'P' and r.status='success' ")
	List<RegistrationFeeTransaction> findAllPendingReceipt();

	@Modifying
	@Transactional
	@Query(value = " UPDATE RegistrationFeeTransaction r SET r.receiptStatus = 'P' WHERE r.orderId =:orderId ")
	void updateRegistrationFeeTransactionByBankImportTransactionId(String orderId);
}
