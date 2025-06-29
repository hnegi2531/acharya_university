package com.au.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.au.model.ReadmissionAmountPaid;

@Transactional
@Repository
public interface ReadmissionAmountPaidRepository extends JpaRepository<ReadmissionAmountPaid, Integer> {

}
