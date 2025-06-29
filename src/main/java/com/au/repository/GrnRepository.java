package com.au.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.dto.DirectGrnListDTO;
import com.au.dto.DraftOrderRequestDTO;
import com.au.dto.GrnByItemAssigmentDTO;
import com.au.dto.GrnListDTO;
import com.au.model.GRN;

@Repository
public interface GrnRepository extends JpaRepository<GRN, Integer> {

	boolean existsByPurchaseRefNo(String poNo);

	boolean existsByInvoiceNumber(String invoiceNumber);

	@Query(value = "select new com.au.dto.DirectGrnListDTO( gr.grnId as grnId, gr.itemName as itemName, gr.quantity as quantity, gr.balanceQty as balanceQuantity, gr.total as value, gr.enterQty as enterQuantity, gr.invoiceDescription as itemDescription, "
			+ " gr.attachmentPath as attachment, gr.invoiceNumber as invoiceNo, gr.invoiceDate as invoiceDate, gr.vendorId as vendorId,gr.vendorName as vendorName, gr.instituteId as instituteId,"
			+ " gr.aquistion as aquistion, gr.remarks as remarks , gr.storeId as storeId, gr.storeName as storeName , "
			+ " gr.createdUserName as createdUserName,gr.grnNo as grnNumber, gr.createdDate as createdDate,gr.uom as uom,gr.uomShortName as uomShortName,"
			+ " po.poTotalAmount as poTotalAmount, pi.poReferenceNo as poReferenceNo, po.approvedDate as approvedDate ) "
			+ " from  GRN gr "
			+ " Left join PurchaseOrder as po on po.poReferenceNo = gr.purchaseRefNo "
			+ " Left join PurchaseItems pi on pi.purchase_item_id = gr.purchaseItemId Where gr.grnNo=:grnNo ")
	List<DirectGrnListDTO> getListofDirectGRNById(String grnNo);

	@Query(value = "select new com.au.dto.DirectGrnListDTO( gr.grnId as grnId, gr.itemName as itemName, gr.qunatity as quantity, gr.balanceQty as balanceQuantity, gr.total as value, gr.enterQty as enterQuantity, gr.invoiceDescription as itemDescription, "
			+ " gr.attachmentPath as attachment, gr.invoiceNumber as invoiceNo, gr.invoiceDate as invoiceDate, gr.vendorId as vendorId,gr.vendorName as vendorName, gr.instituteId as instituteId,"
			+ " gr.aquistion as aquistion, gr.remarks as remarks, gr.storeId as storeId, gr.storeName as storeName , "
			+ " gr.createdUserName as createdUserName,gr.grnNo as grnNumber, gr.createdDate as createdDate,gr.uom as uom,gr.uomShortName as uomShortName,"
			+ " po.poTotalAmount as poTotalAmount, pi.poReferenceNo as poReferenceNo, po.approvedDate as approvedDate ) "
			+ " from  GRN gr "
			+ " Left join PurchaseOrder as po on po.poReferenceNo = gr.purchaseRefNo "
			+ " Left join PurchaseItems pi on pi.purchase_item_id = gr.purchaseItemId ")
	Page<DirectGrnListDTO> getListofDirectGRN(DraftOrderRequestDTO draftOrderRequestDTO, Pageable pageable);

	@Query("SELECT SUM(g.enterQty) FROM GRN g WHERE g.envItemId=:envItemId")
	Double sumQuantityByItemName(@Param("envItemId") Integer envItemId);

	@Query(value=" select new  com.au.dto.GrnByItemAssigmentDTO( g.grnNo as grnNumber, g.createdDate as grnDate, g.enterQty as quantity, g.createdUserName as createdBy, g.uom as uom,e.item_description as  itemDescription,e.make as make,i.item_names as itemName  ) from GRN g left join EnvItemsInStores e on e.env_item_id=g.itemId left join ItemsCreation i  on i.item_id=e.item_id where g.envItemId=:item_assigment_id ", nativeQuery = false)
	List<GrnByItemAssigmentDTO> getGrnByItemAssigmentId(@Param("item_assigment_id") Integer item_assigment_id);
	
	List<GRN> findByGrnNo(String grnNo);

	GRN findFirstByGrnNo(String grnNO);

	@Query(value=" select gr.uom from GRN gr where gr.itemId=:itemId", nativeQuery = false)
	String getGrnUom(@Param("itemId") Integer itemId);
	
	@Query(value = "select new com.au.dto.GrnListDTO( gr.grnId as grnId, "
			+ "  CASE  "
			+ "    WHEN gr.invoiceDescription IS NOT NULL THEN CONCAT(gr.itemName, '-', gr.invoiceDescription) "
			+ "    ELSE gr.itemName "
			+ "  END AS itemName, "
			+ " gr.quantity as quantity, gr.balanceQty as balanceQuantity, gr.total as value, gr.enterQty as enterQuantity, gr.invoiceDescription as itemDescription, "
			+ " gr.attachmentPath as attachment, gr.invoiceNumber as invoiceNo, gr.invoiceDate as invoiceDate, "
			+ " gr.vendorId as vendorId,gr.vendorName as vendorName, gr.instituteId as instituteId,"
			+ " gr.aquistion as aquistion, gr.remarks as remarks , gr.storeId as storeId, gr.storeName as storeName ,"
			+ " gr.createdUserName as createdUserName,gr.grnNo as grnNumber, gr.createdDate as createdDate, gr.uom as uom,"
			+ " gr.uomShortName as uomShortName, sch.school_name as schoolName, gr.gstTotal as gstTotal,"
			+ " gr.discountTotal as discountTotal, gr.costTotal as costTotal, pi.rate as rate, gr.gst as gst,"
			+ " gr.discount as discount, gr.totalAmount as totalAmount, pi.poReferenceNo as poReferenceNo, po.approvedDate as approvedDate) from  GRN gr "
			+ " Left join Schools sch on sch.school_id = gr.instituteId "
			+ " Left join PurchaseOrder as po on po.poReferenceNo = gr.purchaseRefNo "
			+ " Left join PurchaseItems pi on pi.purchase_item_id = gr.purchaseItemId Where gr.grnNo=:grnNo ", nativeQuery = false)
	List<GrnListDTO> getListofGRNForPdf(String grnNo);

	@Query(value="select gr from GRN gr  where gr.voucherHeadNewId in (:vouchheadIds) ")
	List<GRN> getGrnByVoucherHeadIds(List<Integer> vouchheadIds);



//	 @Query("SELECT new map("
//	            + "MAX(grn.createdBy) as createdBy, "
//	            + "MAX(grn.instituteId) as instituteId, "
//	            + "MAX(grn.createdDate) as createdDate, "
//	            + "MAX(grn.invoiceNumber) as invoiceNumber, "
//	            + "MAX(grn.grnReferenceNumber) as grnReferenceNumber, "
//	            + "MAX(grn.referenceNo) as referenceNo, "
//	            + "MAX(grn.purchaseRefNo) as purchaseRefNo, "
//	            + "MAX(grn.finalTotal) as finalTotal, "
//	            + "MAX(grn.vendorName) as vendorName, "
//	            + "MAX(grn.vendorId) as vendorId, "
//	            + "MAX(grn.envItemId) as envItemId, "
//	            + "MAX(grn.grnId) as grnId, "
//	            + "MAX(grn.year) as year, "
//	            + "MAX(grn.journalVoucherId) as journalVoucherId, "
//	            + "MAX(grn.cancelGrnRemarks) as cancelGrnRemarks, "
//	            + "MAX(grn.approver1Remarks) as approver1Remarks, "
//	            + "MAX(grn.approver1Status) as approver1Status, "
//	            + "MAX(grn.approver1Id) as approver1Id, "
//	            + "MAX(grn.approver1Date) as approver1Date, "
//	            + "MAX(grn.approver2Remarks) as approver2Remarks, "
//	            + "MAX(grn.approver2Status) as approver2Status, "
//	            + "MAX(grn.approver2Id) as approver2Id, "
//	            + "MAX(grn.approver2Date) as approver2Date) "
//	            + "FROM GRN grn "
//	            + "WHERE grn.active = TRUE AND grn.cancelEpaymentGrn = TRUE "
//	            + "AND grn.paymentVoucherId IS NULL "
//	            + "GROUP BY grn.referenceNo, grn.instituteId, grn.purchaseRefNo, grn.year "
//	            + "ORDER BY grn.grnId DESC")
//	List<Map<String, Object>> indexPageForGrn();

	 @Query("SELECT new map(env.grnId AS id, env.invoiceNumber AS invoice_number,env.purchaseRefNo AS purchase_ref_no,env.attachmentPath As attachmentPath, "
		        + "   env.instituteId AS institute_id,env.active AS active,env.invoiceDate AS invoice_date,env.vendorName AS vendor_name,env.total AS total, "
		        + "   env.remarks AS remarks,env.itemName AS item_name,env.grnNo AS grn_no, env.createdBy AS created_by,env.itemId AS item_id,"
		        + "  env.draft_journal_voucher_id As draft_journal_voucher_id,env.journal_voucher_id As journal_voucher_id,"
		        + "	djv.created_by As djvcreated_by,djv.created_date As djvcreated_date,jv.created_by As jvcreated_by,jv.created_date As jvcreated_date,"
		        + "djv.created_username As created_username,jv.created_username As created_username,env.paymentVoucherId As paymentVoucherId, "
			 	+ " jv.journal_voucher_number as journalVoucherNumber, jv.financial_year_id as financialYearId,"
			 	+ "po.purchase_order_id As purchase_order_id,po.poTotalAmount As poTotalAmount,"
			 	+ "env.draft_payment_voucher_id As draft_payment_voucher_id,env.vendorId As vendorId,"
		        + "   env.createdDate AS created_date,env.gstTotal AS gst_total,env.costTotal AS cost_total,env.totalAmount AS total_amount, "
		        + "   sc.school_name_short AS school_name_short,sc.school_name AS school_name,po.accountPaymentType as accountPaymentType ) FROM GRN env "
		        + "LEFT JOIN Schools sc ON env.instituteId = sc.school_id "
			 + "LEFT JOIN PurchaseOrder po ON po.poReferenceNo = env.purchaseRefNo "
		        + "LEFT JOIN DraftJournalVoucher djv ON env.draft_journal_voucher_id = djv.draft_journal_voucher_id "
		        + "LEFT JOIN JournalVoucher jv ON env.journal_voucher_id = jv.journal_voucher_id "
		        + "where (DATE(env.createdDate) >= :minDate) "
		 	    + "AND (:start IS NULL OR DATE(env.createdDate) >= :start) "
			        + "AND (:end IS NULL OR DATE(env.createdDate) <= :end) And "
			+ " CONCAT(IfNull(env.invoiceNumber,''),'',IfNull(sc.school_name_short,''),'',IfNull(env.purchaseRefNo,''),'',IfNull(env.vendorName,''),'',"
			+ "IfNull(env.itemName,''),'',IfNull(env.totalAmount,'')) LIKE %:keyword% GROUP BY env.instituteId, env.purchaseRefNo ORDER BY env.grnId DESC ")
	 public Page<Object> getAllDataFilteredByKeyword(Pageable pageable,
	            @Param("keyword") Object keyword,
	            @Param("start") Date start,
	            @Param("end") Date end,
	            @Param("minDate") Date minDate);
	
	 @Query("SELECT new map(env.grnId AS id, env.invoiceNumber AS invoice_number,env.purchaseRefNo AS purchase_ref_no,env.attachmentPath As attachmentPath, "
		        + "   env.instituteId AS institute_id,env.active AS active,env.invoiceDate AS invoice_date,env.vendorName AS vendor_name,env.total AS total, "
		        + "   env.remarks AS remarks,env.itemName AS item_name,env.grnNo AS grn_no, env.createdBy AS created_by,env.itemId AS item_id,"
		        + "  env.draft_journal_voucher_id As draft_journal_voucher_id,env.journal_voucher_id As journal_voucher_id, "
		        + "	djv.created_by As djvcreated_by,djv.created_date As djvcreated_date,jv.created_by As jvcreated_by,jv.created_date As jvcreated_date, "
			 	+ " jv.journal_voucher_number as journalVoucherNumber, jv.financial_year_id as financialYearId, "
			 	+ "po.purchase_order_id As purchase_order_id,po.poTotalAmount As poTotalAmount, "
			 	+ "env.draft_payment_voucher_id As draft_payment_voucher_id,env.vendorId As vendorId,"
		        + "djv.created_username As djvcreated_username,jv.created_username As jvcreated_username,env.paymentVoucherId As paymentVoucherId, "
		        + "   env.createdDate AS created_date,env.gstTotal AS gst_total,env.costTotal AS cost_total,env.totalAmount AS total_amount, "
		        + "   sc.school_name_short AS school_name_short,sc.school_name AS school_name,po.accountPaymentType as accountPaymentType ) FROM GRN env "
		        + "LEFT JOIN Schools sc ON env.instituteId = sc.school_id "
		        + "LEFT JOIN PurchaseOrder po ON po.poReferenceNo = env.purchaseRefNo "
		        + "LEFT JOIN DraftJournalVoucher djv ON env.draft_journal_voucher_id = djv.draft_journal_voucher_id "
		        + "LEFT JOIN JournalVoucher jv ON env.journal_voucher_id = jv.journal_voucher_id "
		        + "where (DATE(env.createdDate) >= :minDate) "
		 	    + "AND (:start IS NULL OR DATE(env.createdDate) >= :start) "
			        + "AND (:end IS NULL OR DATE(env.createdDate) <= :end) "
		        + "GROUP BY env.instituteId, env.purchaseRefNo ORDER BY env.grnId DESC")
		public Page<Object> getAllSortedData(Pageable pageable,
	            @Param("start") Date start,
	            @Param("end") Date end,
	            @Param("minDate") Date minDate);

	 @Query(value="select gr.* from purhcase_grn gr  where gr.grn_no =?1 And gr.active=true" ,nativeQuery = true)
	 public List<GRN> getGrnDetailBasedOnGrnNo(String grn_no);

	 @Query(value = "select * from purhcase_grn where purchase_order_id = :poId", nativeQuery = true)
	List<GRN> getGRNByPOId(Integer poId);
	 
		@Query("SELECT MIN(pv.createdDate) FROM GRN pv")
		Optional<Date> findMinCreatedDate();
}
