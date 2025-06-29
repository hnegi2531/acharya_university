package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.UniformFeePaid;

@Transactional
@Repository
public interface UniformFeePaidRepository extends JpaRepository<UniformFeePaid, Integer> {
	
	@Query(value = "select u from UniformFeePaid u where u.active=true")
	public List<UniformFeePaid> findAll1();

    @Query("SELECT new map(u.uniformFeePaidId as id, u.studentId as studentId, u.paidYear as paidYear, "
            + "u.amount as amount, u.voucherHeadNewId as voucherHeadNewId, u.otherFeeTemplateId as otherFeeTemplateId, "
            + "u.uniformReceiptNo as uniformReceiptNo, u.createdBy as createdBy, u.createdDate as createdDate, "
            + "u.modifiedBy as modifiedBy, u.modifiedDate as modifiedDate, "
            + "u.createdUsername as createdUsername, u.modifiedUsername as modifiedUsername, "
            + "u.fcYearId as fcYearId, u.transactionDate as transactionDate) "
            + "FROM UniformFeePaid u "
            + "Where CONCAT(IfNull(u.studentId,''),'',IfNull(u.paidYear,''),'',IfNull(u.createdDate,''),'',IfNull(u.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

    @Query("SELECT new map(u.uniformFeePaidId as id, u.studentId as studentId, u.paidYear as paidYear, "
            + "u.amount as amount, u.voucherHeadNewId as voucherHeadNewId, u.otherFeeTemplateId as otherFeeTemplateId, "
            + "u.uniformReceiptNo as uniformReceiptNo, u.createdBy as createdBy, u.createdDate as createdDate, "
            + "u.modifiedBy as modifiedBy, u.modifiedDate as modifiedDate, "
            + "u.createdUsername as createdUsername, u.modifiedUsername as modifiedUsername, "
            + "u.fcYearId as fcYearId, u.transactionDate as transactionDate) "
            + "FROM UniformFeePaid u ")
    Page<Object> getAllSortedData(Pageable pageable);


	@Modifying
	@Query(value = "update UniformFeePaid u set u.active=false where u.uniformFeePaidId=?1")
	public void updateUniformFeePaid(Integer uniformFeePaidId);

	@Modifying
	@Query(value = "update UniformFeePaid u set u.active=true where u.uniformFeePaidId=?1")
	public void updateUniformFeePaid1(Integer uniformFeePaidId);

}
