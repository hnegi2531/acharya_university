package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.au.model.CancelledBulkFeeReceipt;

public interface CancelledBulkFeeReceiptRepository extends JpaRepository<CancelledBulkFeeReceipt, Integer> {

}
