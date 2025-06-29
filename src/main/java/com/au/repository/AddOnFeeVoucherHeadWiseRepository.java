package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.AddOnFeeVoucherHeadWise;


@Transactional
@Repository
public interface AddOnFeeVoucherHeadWiseRepository extends JpaRepository<AddOnFeeVoucherHeadWise, Integer> {
	
	@Query(value = "select af from AddOnFeeVoucherHeadWise af where af.active=true")
	public List<AddOnFeeVoucherHeadWise> findAll1();

	  @Query("SELECT new map(af.addOnFeeVoucherHeadWiseId as addOnFeeVoucherHeadWiseId, "
	    		+ "af.studentId as studentId, "
	            + "af.voucherHeadNewId as voucherHeadNewId, "
	            + "af.voucherHeadTotalAmt as voucherHeadTotalAmt, "
	            + "af.totalPayingNow as totalPayingNow, "
	            + "af.payingNow as payingNow, "
	            + "af.addOnReceiptId as addOnReceiptId, "
	            + "af.paidYear as paidYear, "
	            + "af.active as active, "
	            + "af.fcYearId as fcYearId, "
	            + "af.schoolId as schoolId, "
	            + "af.createdDate as createdDate, "
	            + "af.modifiedDate as modifiedDate, "
	            + "af.createdBy as createdBy, "
	            + "af.modifiedBy as modifiedBy, "
	            + "af.createdUsername as createdUsername, "
	            + "af.modifiedUsername as modifiedUsername) "
	            + "FROM AddOnFeeVoucherHeadWise af "
            + "Where CONCAT(IfNull(af.studentId,''),'',IfNull(af.voucherHeadNewId,''),'',IfNull(af.createdDate,''),'',IfNull(af.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

    @Query("SELECT new map(af.addOnFeeVoucherHeadWiseId as addOnFeeVoucherHeadWiseId, "
    		+ "af.studentId as studentId, "
            + "af.voucherHeadNewId as voucherHeadNewId, "
            + "af.voucherHeadTotalAmt as voucherHeadTotalAmt, "
            + "af.totalPayingNow as totalPayingNow, "
            + "af.payingNow as payingNow, "
            + "af.addOnReceiptId as addOnReceiptId, "
            + "af.paidYear as paidYear, "
            + "af.active as active, "
            + "af.fcYearId as fcYearId, "
            + "af.schoolId as schoolId, "
            + "af.createdDate as createdDate, "
            + "af.modifiedDate as modifiedDate, "
            + "af.createdBy as createdBy, "
            + "af.modifiedBy as modifiedBy, "
            + "af.createdUsername as createdUsername, "
            + "af.modifiedUsername as modifiedUsername) "
            + "FROM AddOnFeeVoucherHeadWise af ")
    Page<Object> getAllSortedData(Pageable pageable);


	@Modifying
	@Query(value = "update AddOnFeeVoucherHeadWise af set af.active=false where af.addOnFeeVoucherHeadWiseId=?1")
	public void updateAddOnFeeVoucherHeadWise(Integer addOnFeeVoucherHeadWiseId);

	@Modifying
	@Query(value = "update AddOnFeeVoucherHeadWise af set af.active=true where af.addOnFeeVoucherHeadWiseId=?1")
	public void updateAddOnFeeVoucherHeadWise1(Integer addOnFeeVoucherHeadWiseId);

}
