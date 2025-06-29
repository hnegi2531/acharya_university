package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.FeeHeadAmountRestriction;



@Transactional
@Repository
public interface FeeHeadAmountRestrictionRepository extends JpaRepository<FeeHeadAmountRestriction, Integer>{
	
	@Query(value="Select fhar From FeeHeadAmountRestriction fhar Where fhar.active=true")
	public List<FeeHeadAmountRestriction> getActiveFeeHeadAmountRestriction();
	
	@Query(value ="Select distinct new com.au.dto.FeeHeadAmountRestrictionResponse(fhar.feeHeadAmountRestrictionId as id,sch.school_id as schoolId,"
			+ "fhar.fromDate as fromDate,fhar.toDate as toDate,fhar.amount as amount,vh.voucher_head_id as voucherHeadId,fhar.fixed as fixed,"
			+ "fhar.externalStatus as externalStatus,fhar.remarks as remarks,fhar.userId as userId,fhar.createdBy as createdBy,fhar.createdDate as createdDate,"
			+ "fhar.active as active,fhar.createdUsername as createdUsername,fhar.modifiedBy as modifiedBy,fhar.modifiedUsername as modifiedUsername,"
			+ "sch.school_name_short as schoolShortName,vhn.voucher_head as voucherHead,"
			+ "vhn.voucher_head_short_name as voucherHeadShortName,fhar.attachmentPath as attachmentPath,fhar.attachmentFileName as attachmentFileName,"
			+ "fhar.attachmentType as attachmentType) From FeeHeadAmountRestriction fhar "
			+ "Left Join Schools sch on sch.school_id=fhar.schoolId "
			+ "Inner Join VoucherHead vh on vh.voucher_head_id=fhar.voucherHeadId "
			+ "Left Join VoucherHeadNew vhn on vhn.voucher_head_new_id=vh.voucher_head_new_id "
			+ "Where CONCAT(IfNull(fhar.feeHeadAmountRestrictionId,''),'',IfNull(fhar.fromDate,''),'',IfNull(fhar.toDate,''),'',"
			+ "IfNull(fhar.fixed,''),'',IfNull(fhar.externalStatus,''),'',IfNull(fhar.createdDate,''),'',"
			+ "IfNull(fhar.amount,''),'',IfNull(sch.school_name_short,''),'',IfNull(fhar.createdUsername,''),'',"
			+ "IfNull(vhn.voucher_head_short_name,''),'','',IfNull(fhar.active,''),'',"
			+ "IfNull(vhn.voucher_head,'')) LIKE %?1% And fhar.userId LIKE ?2 Or fhar.userId LIKE ?3 Or fhar.userId LIKE ?4 Or fhar.userId LIKE ?5 ")
	public Page<Object> fetchAllFeeHeadAmountRestrictionDetailsByKeywordAndUserId(Pageable pageable, Object keyword,String userIdCase1,String userIdCase2,String userIdCase3, String userIdCase4);
	
	@Query(value ="Select distinct new com.au.dto.FeeHeadAmountRestrictionResponse(fhar.feeHeadAmountRestrictionId as id,sch.school_id as schoolId,"
			+ "fhar.fromDate as fromDate,fhar.toDate as toDate,fhar.amount as amount,vh.voucher_head_id as voucherHeadId,fhar.fixed as fixed,"
			+ "fhar.externalStatus as externalStatus,fhar.remarks as remarks,fhar.userId as userId,fhar.createdBy as createdBy,fhar.createdDate as createdDate,"
			+ "fhar.active as active,fhar.createdUsername as createdUsername,fhar.modifiedBy as modifiedBy,fhar.modifiedUsername as modifiedUsername,"
			+ "sch.school_name_short as schoolShortName,vhn.voucher_head as voucherHead,"
			+ "vhn.voucher_head_short_name as voucherHeadShortName,fhar.attachmentPath as attachmentPath,fhar.attachmentFileName as attachmentFileName,"
			+ "fhar.attachmentType as attachmentType) From FeeHeadAmountRestriction fhar "
			+ "Left Join Schools sch on sch.school_id=fhar.schoolId "
			+ "Inner Join VoucherHead vh on vh.voucher_head_id=fhar.voucherHeadId "
			+ "Left Join VoucherHeadNew vhn on vhn.voucher_head_new_id=vh.voucher_head_new_id "
			+ "Where CONCAT(IfNull(fhar.feeHeadAmountRestrictionId,''),'',IfNull(fhar.fromDate,''),'',IfNull(fhar.toDate,''),'',"
			+ "IfNull(fhar.fixed,''),'',IfNull(fhar.externalStatus,''),'',IfNull(fhar.createdDate,''),'',"
			+ "IfNull(fhar.amount,''),'',IfNull(sch.school_name_short,''),'',IfNull(fhar.createdUsername,''),'',"
			+ "IfNull(vhn.voucher_head_short_name,''),'','',IfNull(fhar.active,''),'',"
			+ "IfNull(vhn.voucher_head,'')) LIKE %?1%")
	public Page<Object> fetchAllFeeHeadAmountRestrictionDetailsByKeyword(Pageable pageable, Object keyword);
	
	
	@Query(value ="Select distinct new com.au.dto.FeeHeadAmountRestrictionResponse(fhar.feeHeadAmountRestrictionId as id,sch.school_id as schoolId,"
			+ "fhar.fromDate as fromDate,fhar.toDate as toDate,fhar.amount as amount,vh.voucher_head_id as voucherHeadId,fhar.fixed as fixed,"
			+ "fhar.externalStatus as externalStatus,fhar.remarks as remarks,fhar.userId as userId,fhar.createdBy as createdBy,fhar.createdDate as createdDate,"
			+ "fhar.active as active,fhar.createdUsername as createdUsername,fhar.modifiedBy as modifiedBy,fhar.modifiedUsername as modifiedUsername,"
			+ "sch.school_name_short as schoolShortName,vhn.voucher_head as voucherHead,"
			+ "vhn.voucher_head_short_name as voucherHeadShortName,fhar.attachmentPath as attachmentPath,fhar.attachmentFileName as attachmentFileName,"
			+ "fhar.attachmentType as attachmentType) From FeeHeadAmountRestriction fhar "
			+ "Left Join Schools sch on sch.school_id=fhar.schoolId "
			+ "Inner Join VoucherHead vh on vh.voucher_head_id=fhar.voucherHeadId "
			+ "Left Join VoucherHeadNew vhn on vhn.voucher_head_new_id=vh.voucher_head_new_id "
			+ "Where fhar.userId LIKE ?1 Or fhar.userId LIKE ?2 Or fhar.userId LIKE ?3 Or fhar.userId LIKE ?4 ")
	public Page<Object> fetchAllSortedFeeHeadAmountRestrictionDetailsAndUserId(Pageable pageable,String userIdCase1,String userIdCase2,String userIdCase3, String userIdCase4);
	
	
	@Query(value ="Select distinct new com.au.dto.FeeHeadAmountRestrictionResponse(fhar.feeHeadAmountRestrictionId as id,sch.school_id as schoolId,"
			+ "fhar.fromDate as fromDate,fhar.toDate as toDate,fhar.amount as amount,vh.voucher_head_id as voucherHeadId,fhar.fixed as fixed,"
			+ "fhar.externalStatus as externalStatus,fhar.remarks as remarks,fhar.userId as userId,fhar.createdBy as createdBy,fhar.createdDate as createdDate,"
			+ "fhar.active as active,fhar.createdUsername as createdUsername,fhar.modifiedBy as modifiedBy,fhar.modifiedUsername as modifiedUsername,"
			+ "sch.school_name_short as schoolShortName,vhn.voucher_head as voucherHead,"
			+ "vhn.voucher_head_short_name as voucherHeadShortName,fhar.attachmentPath as attachmentPath,fhar.attachmentFileName as attachmentFileName,"
			+ "fhar.attachmentType as attachmentType) From FeeHeadAmountRestriction fhar "
			+ "Left Join Schools sch on sch.school_id=fhar.schoolId "
			+ "Inner Join VoucherHead vh on vh.voucher_head_id=fhar.voucherHeadId "
			+ "Left Join VoucherHeadNew vhn on vhn.voucher_head_new_id=vh.voucher_head_new_id ")
	public Page<Object> fetchAllSortedFeeHeadAmountRestrictionDetails(Pageable pageable);
	
	@Modifying
	@Query(value = "Update FeeHeadAmountRestriction fhar set fhar.active=false where fhar.feeHeadAmountRestrictionId=?1")
	public void deactivateFeeHeadAmountRestrictionDetail(Integer feeHeadAmountRestrictionId);
	
	@Modifying
	@Query(value = "Update FeeHeadAmountRestriction fhar set fhar.active=false where fhar.feeHeadAmountRestrictionId=?1")
	public void activateFeeHeadAmountRestrictionDetail(Integer feeHeadAmountRestrictionId);
	
	@Query(value ="Select new map(fhar.feeHeadAmountRestrictionId as feeHeadAmountRestrictionId,sch.school_id as schoolId,"
			+ "fhar.fromDate as fromDate,fhar.toDate as toDate,fhar.amount as amount,vh.voucher_head_id as voucherHeadId,fhar.fixed as fixed,"
			+ "fhar.externalStatus as externalStatus,fhar.remarks as remarks,fhar.userId as userId,fhar.createdBy as createdBy,fhar.createdDate as createdDate,"
			+ "fhar.active as active,fhar.createdUsername as createdUsername,fhar.modifiedBy as modifiedBy,fhar.modifiedUsername as modifiedUsername,"
			+ "sch.school_name_short as schoolShortName,vhn.voucher_head as voucherHead,"
			+ "vhn.voucher_head_short_name as voucherHeadShortName) From FeeHeadAmountRestriction fhar "
			+ "Left Join Schools sch on sch.school_id=fhar.schoolId "
			+ "Inner Join VoucherHead vh on vh.voucher_head_id=fhar.voucherHeadId "
			+ "Left Join VoucherHeadNew vhn on vhn.voucher_head_new_id=vh.voucher_head_new_id Where fhar.feeHeadAmountRestrictionId=?1")
	public List<HashMap<String,Object>> feeHeadAmountRestrictionDetailsForPayment(Integer feeHeadAmountRestrictionId);

}