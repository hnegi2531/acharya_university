package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.CancelledTallyHostelReceipt;

@Transactional
@Repository
public interface CancelledTallyHostelReceiptRepository  extends JpaRepository<CancelledTallyHostelReceipt, Integer>{

}
