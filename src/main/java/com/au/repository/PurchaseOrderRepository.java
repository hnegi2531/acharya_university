package com.au.repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.dto.DraftOrderRequestDTO;
import com.au.model.PurchaseOrder;

@Transactional
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {

    PurchaseOrder findByTemporaryPurchaseOrderId(Integer temporaryPurchaseOrderId);

    boolean existsByTemporaryPurchaseOrderId(Integer temporaryPurchaseOrderId);


    String FILTER = "(:#{#draftOrderRequestDTO.vendor} IS NULL OR po.vendor = :#{#draftOrderRequestDTO.vendor}) AND " +
            "(:#{#draftOrderRequestDTO.institute} IS NULL OR po.institute = :#{#draftOrderRequestDTO.institute}) AND " +
            "(:#{#draftOrderRequestDTO.instituteId} IS NULL OR po.instituteId = :#{#draftOrderRequestDTO.instituteId}) AND " +
            "(:#{#draftOrderRequestDTO.requestType} IS NULL OR po.requestType = :#{#draftOrderRequestDTO.requestType}) AND " +
            "( " +
            "    (:#{#draftOrderRequestDTO.fromDate} IS NULL OR :#{#draftOrderRequestDTO.toDate} IS NULL) OR " +
            "    (DATE(po.created_date) BETWEEN STR_TO_DATE(:#{#draftOrderRequestDTO.fromDate}, '%Y-%m-%d') AND STR_TO_DATE(:#{#draftOrderRequestDTO.toDate}, '%Y-%m-%d'))\n" +
            ") AND " +
            "(:#{#draftOrderRequestDTO.poNo} IS NULL OR po.poReferenceNo = :#{#draftOrderRequestDTO.poNo}) AND " +
            "(:#{#draftOrderRequestDTO.amount} IS NULL OR po.poTotalAmount = :#{#draftOrderRequestDTO.amount}) AND " +
            "(:#{#draftOrderRequestDTO.approverId} IS NULL OR po.approverId = :#{#draftOrderRequestDTO.approverId}) AND " +
            "po.active = 1 ORDER BY po.created_date ";

    @Query(value = "select new com.au.dto.PurchaseOrderResponseDTO( po.vendor as vendor, po.institute as institute, po.poReferenceNo as poNo,"
            + " po.poTotalAmount as amount, po.accountPaymentType as accountPaymentType, po.purchase_order_id as purchaseOrderId,"
            + " po.approverId as approverId,po.remarks as remarks,po.createdUsername as createdUsername, po.grnCreationStatus as grnCreationStatus, "
            + " tpo.tpoAttachmentFilePath as tpoAttachmentFilePath, po.created_date as created_date, po.approvedDate as approvedDate, "
            + " po.requestType as requestType, ed.employee_name as approverName) from  PurchaseOrder po "
            + " left join TemporaryPurchaseOrder tpo on tpo.temporary_purchase_order_id=po.temporaryPurchaseOrderId "
            + " left join EmployeeDetails ed on ed.emp_id = po.approverId "
            + " where " + FILTER)
    Page<Object> getPurchaseOrder(@Param("draftOrderRequestDTO") DraftOrderRequestDTO draftOrderRequestDTO, Pageable pageable);

    @Query(value = " select po from PurchaseOrder po left join PurchaseItems pi on pi.purchaseOrder=po.purchase_order_id where po.purchase_order_id=:id ")
    PurchaseOrder getPurchaseOrderById(@Param("id") Integer id);

    @Query(value = "select new com.au.dto.PurchaseOrderResponseDTO( po.vendor as vendor, po.poReferenceNo as poNo ) from  PurchaseOrder po")
    Page<Object> getListOfPOForGRN(DraftOrderRequestDTO draftOrderRequestDTO, Pageable pageable);

    @Modifying
    @Query(value = "Update PurchaseOrder po Set po.purchaseOrderFilePath=?1 Where po.purchase_order_id=?2")
    void updationOfPurchaseOrderFilePath(String purchaseOrderFilePath, Integer purchaseOrderId);

    String FILTER_FOR_DEACTIVATE = "( :#{#draftOrderRequestDTO.vendor} IS NULL OR po.vendor=:#{#draftOrderRequestDTO.vendor}) And"
            + " ( :#{#draftOrderRequestDTO.institute} IS NULL OR po.institute=:#{#draftOrderRequestDTO.institute}) And"
            + " ( :#{#draftOrderRequestDTO.poNo} IS NULL OR po.po_reference_no=:#{#draftOrderRequestDTO.poNo}) And"
            + " ( :#{#draftOrderRequestDTO.createdDate} IS NULL OR po.created_date=:#{#draftOrderRequestDTO.createdDate}) And"
            + " ( :#{#draftOrderRequestDTO.amount} IS NULL OR po.po_total_amount=:#{#draftOrderRequestDTO.amount}) And"
            + " ( :#{#draftOrderRequestDTO.approverId} IS NULL OR po.approver_id=:#{#draftOrderRequestDTO.approverId}) And po.active=false";

    @Query(value = "select po.vendor as vendor, po.institute as institute, po.po_Reference_no as poNo, " +
            "po.po_total_amount as amount, po.account_payment_type as accountPaymentType, po.purchase_order_id as purchaseOrderId, " +
            "po.approver_id as approverId,po.remarks as remarks,po.created_username as createdUsername, po.cancel_by_id, po.cancel_comments,  " +
            "po.request_type, po.created_date, ed1.employee_name as createdBy,ed2.employee_name as cancelleBy " +
            "from  purchase_order po  " +
            "left join employee_details ed1 on ed1.emp_id = po.created_by " +
            "left join employee_details ed2 on ed2.emp_id = po.cancel_by_id " +
            "where " + FILTER_FOR_DEACTIVATE, nativeQuery = true)
    List<Map<String, Object>> getDeactivateApprovedPurchaseOrder(@Param("draftOrderRequestDTO") DraftOrderRequestDTO draftOrderRequestDTO, Pageable pageable);


    @Query(value = " Select po from PurchaseOrder po "
//				+ "left join PurchaseItems pi on pi.purchaseOrder=po.purchase_order_id "
            + "where po.purchase_order_id Not in (Select grn.purchaseOrderId From GRN grn Where grn.active=true) ")
    List<PurchaseOrder> getApprovedPurchaseOrder();

    @Modifying
    @Query(value = "Update PurchaseOrder po Set po.grnCreationStatus=true Where po.purchase_order_id=?1")
    void updateGrnCreationStatus(Integer purchaseOrderId);

    @Query(value = " Select po from PurchaseOrder po "
//				+ "left join PurchaseItems pi on pi.purchaseOrder=po.purchase_order_id "
            + "where po.voucherHeadNewId=?1 and po.instituteId=?2 and po.active=true ")
    List<PurchaseOrder> getPurchaseOrderOnVoucherHeadNewIdAndSchoolId(Integer voucher_head_new_id, Integer school_id);

    @Query(value = " select grn.*, vhn.ledger_id,l.ledger_name from purhcase_grn grn " +
            "left join voucher_head_new vhn " +
            "on grn.voucher_head_new_id = vhn.voucher_head_new_id " +
            "left join ledger l " +
            "on l.ledger_id = vhn.ledger_id " +
            "where grn.grn_no = ?1 ", nativeQuery = true)
    List<Map<String, Object>> getJournalVoucher(String grnNo);


    @Query(value = "select  purchase_order_id as purchaseOrderId, po_reference_no as poReferenceNo " +
            "from purchase_order po where active = 1", nativeQuery = true)
    List<Map<String, Object>> getActivePOIdAndRefNum();


	
	@Query(value = "select po from PurchaseOrder po where po.active=true")
	List<PurchaseOrder> getPurchaseOrderData();

	@Query(value = "select po from PurchaseOrder po where po.active=true And po.purchase_order_id=?1")
	PurchaseOrder getPurchaseOrder(Integer purchase_order_id);
}
