package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.AddOnFee;

@Transactional
@Repository
public interface AddOnFeeRepository extends JpaRepository<AddOnFee, Integer> {
	
	@Query(value = "select a from AddOnFee a where a.active=true")
	public List<AddOnFee> findAll1();

    @Query("SELECT new map(a.addOnFeeId as addOnFeeId, a.addOnFeeReceiptId as addOnFeeReceiptId, " +
 	       "a.transactionType as transactionType, a.amount as amount, a.studentId as studentId, " +
 	       "a.schoolId as schoolId, a.fcYearId as fcYearId, a.paidYear as paidYear, a.active as active, " +
 	       "a.transactionDate as transactionDate, a.bankTransactionId as bankTransactionId, " +
 	       "a.remarks as remarks, a.orderId as orderId, a.createdDate as createdDate, " +
 	       "a.modifiedDate as modifiedDate, a.createdBy as createdBy, a.modifiedBy as modifiedBy, " +
 	       "a.createdUsername as createdUsername, a.modifiedUsername as modifiedUsername) " +
 	       "FROM AddOnFee a "
            + "Where CONCAT(IfNull(a.studentId,''),'',IfNull(a.amount,''),'',IfNull(a.createdDate,''),'',IfNull(a.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

    @Query("SELECT new map(a.addOnFeeId as addOnFeeId, a.addOnFeeReceiptId as addOnFeeReceiptId, " +
    	       "a.transactionType as transactionType, a.amount as amount, a.studentId as studentId, " +
    	       "a.schoolId as schoolId, a.fcYearId as fcYearId, a.paidYear as paidYear, a.active as active, " +
    	       "a.transactionDate as transactionDate, a.bankTransactionId as bankTransactionId, " +
    	       "a.remarks as remarks, a.orderId as orderId, a.createdDate as createdDate, " +
    	       "a.modifiedDate as modifiedDate, a.createdBy as createdBy, a.modifiedBy as modifiedBy, " +
    	       "a.createdUsername as createdUsername, a.modifiedUsername as modifiedUsername) " +
    	       "FROM AddOnFee a ")
    Page<Object> getAllSortedData(Pageable pageable);


	@Modifying
	@Query(value = "update AddOnFee a set a.active=false where a.addOnFeeId=?1")
	public void updateAddOnFee(Integer addOnFeeId);

	@Modifying
	@Query(value = "update AddOnFee a set a.active=true where a.addOnFeeId=?1")
	public void updateAddOnFee1(Integer addOnFeeId);

}
