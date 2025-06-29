package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.dto.CandidateTransactionDetailsDTO;
import com.au.model.BulkTransaction;

@Repository
public interface BulkTransactionRepository extends JpaRepository<BulkTransaction, Long> {

	BulkTransaction findByOrderId(String razorpayOrderId);

	@Query(value=" select new com.au.dto.BulkTransactionDTO( r.orderId, r.paymentId, r.receiptId, r.transactionDate, r.status, r.amount, r.name, r.email, r.mobile  ) from BulkTransaction r where  r.status='success' order by r.created_date desc ")
	List<CandidateTransactionDetailsDTO> getBulkTransactionDetails();

	@Query(value = " select * from bulk_pay_transaction where order_id = ?1 and LOWER(status) = 'success' and UPPER(transfer_type) = 'ADDON' ", nativeQuery = true)
	BulkTransaction getBulkAddOnTransactionByOrderId(String orderId);

	@Query(value = "select * from bulk_pay_transaction bpt " +
			"where " +
			"bpt.status = 'success' " +
			"and CAST(bpt.created_date AS DATE) like concat(?1,'%') ",nativeQuery = true)
	List<BulkTransaction> getBulkFeeTransactionDetails(@Param("date") String date);

	@Query(value = " select * from bulk_pay_transaction where order_id = ?1 and LOWER(status) = 'success' and UPPER(transfer_type) != 'ADDON' ", nativeQuery = true)
	public BulkTransaction getBulkTransactionByOrderId(String orderId);
}
