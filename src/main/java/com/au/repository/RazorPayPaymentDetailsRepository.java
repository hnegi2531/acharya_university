package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.model.RazorPayPaymentDetails;

@Repository
public interface RazorPayPaymentDetailsRepository extends JpaRepository<RazorPayPaymentDetails, Long> {

	@Query(value=" select rt from RazorPayPaymentDetails rt where rt.razorPayTransactionId=:razorPayTransactionId ")
	List<RazorPayPaymentDetails> getByRazorPayTransactionId(Long razorPayTransactionId );

	@Query(value=" select rt from RazorPayPaymentDetails rt where rt.razorPayTransactionId=:razorPayTransactionId and rt.receiptType=:receiptType and rt.amount > 0 ")
	List<RazorPayPaymentDetails> getByRazorPayTransactionIdAndReceiptType(Long razorPayTransactionId,String receiptType);

	@Query(value=" select rt from RazorPayPaymentDetails rt where rt.razorPayTransactionId=:razorPayTransactionId and rt.receiptType != :addOn and rt.receiptType != :uniform and rt.amount > 0 ")
	List<RazorPayPaymentDetails> getByRazorPayPaymentForFeeReceipt(Long razorPayTransactionId,String addOn, String uniform);

	@Query("SELECT COALESCE(SUM(rt.amount), 0) " +
			"FROM RazorPayPaymentDetails rt " +
			"WHERE rt.razorPayTransactionId = :transactionId AND rt.receiptType not in ('Add On Fee','Uniform Fee') ")
	Double getTotalAmountByRazorpayTransactionId(@Param("transactionId") Long razorPayTransactionId);

}
