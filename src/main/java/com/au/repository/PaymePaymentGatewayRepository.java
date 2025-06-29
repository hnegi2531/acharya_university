package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.PaymeTransactions;

@Transactional
@Repository
public interface PaymePaymentGatewayRepository extends JpaRepository<PaymeTransactions , Integer> {
	
	
	@Query(value = "Select pt From PaymeTransactions pt Where pt.paycom_transaction_id=?1")
	public PaymeTransactions checkingPaymeTransactions(String paycom_transaction_id);
	
	@Query(value = "Select pt From PaymeTransactions pt Where pt.order_id=?1")
	public PaymeTransactions checkingPaymeTransactionsOnOrderId(String order_id);
	
	@Modifying
	@Query(value = "Update PaymeTransactions pt set order_id=?2 Where pt.transaction_id=?1")
	public void updateOrderId(Integer transaction_id,String order_id);
	
	@Query(value = "Select pt.paycom_transaction_id as paycom_transaction_id,pt.paycom_time as paycom_time,"
			+ "pt.amount as amount,pt.order_id as order_id,pt.create_time as create_time,pt.perform_time as perform_time,"
			+ "pt.cancel_time as cancel_time,pt.state as state,pt.reason as reason,pt.receivers as receivers From payme_transaction pt "
			+ "Where (pt.paycom_time between  ?1 And ?2) And pt.state > 0 ",nativeQuery=true)
	public List<Map<String, Object>> getPaymeTransactions(Long from,Long to);

	@Query(value ="Select pt From PaymeTransactions pt Where pt.fee_head_amount_restriction_id=?1 and pt.state=2 ")
	public List<PaymeTransactions> findPaymeTransactionsByFeeHeadAmountRestrictionId(Integer fee_head_amount_restriction_id);

	@Query(value = "Select pt From PaymeTransactions pt Where pt.order_id=?1 And pt.state=2")
	public PaymeTransactions getPaymeTransactionDetails(String order_id);

}
