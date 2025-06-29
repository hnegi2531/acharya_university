package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.au.model.UniformTransaction;

@Repository
public interface UniformTransactionRepository extends JpaRepository<UniformTransaction, Integer> {

	UniformTransaction findByOrderId(String razorpayOrderId);

}
