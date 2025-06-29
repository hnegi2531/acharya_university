package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.au.model.PurchaseItems;

@Transactional
@Repository
public interface PurchaseItemsRepository extends JpaRepository<PurchaseItems,Integer> {

	@Query(value=" select p from PurchaseItems p  where p.poReferenceNo=:poNo ",nativeQuery=false)
	List<PurchaseItems> getPOForGRNByPoNo(@Param("poNo") String poNo);

	@Query(value=" select p from PurchaseItems p where p.purchase_item_id=:id",nativeQuery = false)
	PurchaseItems getPurchaseItemById(@Param("id") Integer Id);
	
	@Modifying
	@Query(value=" Update PurchaseItems p set p.balanceQuantity=?1 where p.purchase_item_id=?2",nativeQuery = false)
	void balancedQuantityUpdate(Float balancedQuantity,Integer purchaseItemId);

	@Query(value = "select sum(pi.balance_quantity) from purchase_items pi where purchase_order_id = :purchaseOrderId", nativeQuery = true)
	Integer getBalanceByPurchaseOrderId(Integer purchaseOrderId);
}
