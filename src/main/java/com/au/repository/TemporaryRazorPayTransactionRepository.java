package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.au.model.RazorPayTransaction;
import com.au.model.TemporaryRazorPayTransaction;

@Repository
public interface TemporaryRazorPayTransactionRepository extends JpaRepository<TemporaryRazorPayTransaction, Long>{

	TemporaryRazorPayTransaction findByOrderId(String orderId);

}
