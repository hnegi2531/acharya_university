package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.CancelledHostelFeeReceipts;

@Transactional
@Repository
public interface CancelledHostelFeeReceiptsRepository  extends JpaRepository<CancelledHostelFeeReceipts, Integer>{

}
