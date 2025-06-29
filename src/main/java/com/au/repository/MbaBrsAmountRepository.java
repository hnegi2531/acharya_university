package com.au.repository;

import com.au.model.MbaBrsAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author: Anjan
 * Date: 12-04-2025
 * Description: MbaBrsAmountRepository class
 */
@Repository
public interface MbaBrsAmountRepository extends JpaRepository<MbaBrsAmount, Integer> {

    @Query(value = "select * from mba_brs_amount mba where mba.transaction_date = :transactionDate", nativeQuery = true)
    MbaBrsAmount findByTransactionDate(String transactionDate);
}
