package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.au.model.ReceiptCounter;

public interface ReceiptCounterRepository extends JpaRepository<ReceiptCounter, Long> {

}
