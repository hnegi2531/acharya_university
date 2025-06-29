package com.au.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.model.TemporaryPurchaseItems;

@Repository
@Transactional
public interface TemporaryPurchaseItemsRepository extends JpaRepository<TemporaryPurchaseItems, Integer> {


    @Modifying
    @Query("DELETE FROM TemporaryPurchaseItems t WHERE t.temporaryPurchaseOrder.temporary_purchase_order_id = :orderId")
    void deleteByTemporaryPurchaseOrderId(@Param("orderId") Integer orderId);
}
