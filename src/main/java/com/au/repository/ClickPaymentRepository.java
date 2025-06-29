package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.ClickPayment;


@Transactional
@Repository
public interface ClickPaymentRepository extends JpaRepository<ClickPayment, Integer>{
	
	@Query(value ="Select cp From ClickPayment cp Where cp.merchant_trans_id=?1")
	public ClickPayment getClickPaymentOnMerchantTransId(String merchant_trans_id);

	@Query(value ="Select cp From ClickPayment cp Where cp.fee_head_amount_restriction_id=?1 And cp.action=1 And cp.error=0 And cp.error_note='Success'")
	public List<ClickPayment> findClickPaymentByFeeHeadAmountRestrictionId(Integer fee_head_amount_restriction_id);

}
