package com.au.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.dto.DraftOrderRequestDTO;
import com.au.model.TemporaryPurchaseOrder;

@Transactional
@Repository
public interface TemporaryPurchaseOrderRepository extends JpaRepository<TemporaryPurchaseOrder, Integer>{

	String FILTER = " (:#{#draftOrderRequestDTO.vendor} IS NULL OR tpo.vendor=:#{#draftOrderRequestDTO.vendor}) and "
	        + " (:#{#draftOrderRequestDTO.institute} IS NULL OR tpo.institute=:#{#draftOrderRequestDTO.institute}) and  "
	        + " (:#{#draftOrderRequestDTO.createdDate} IS NULL OR tpo.created_date=:#{#draftOrderRequestDTO.createdDate}) and "
	        + " (:#{#draftOrderRequestDTO.approverId} IS NULL OR tpo.purchaseApproverId=:#{#draftOrderRequestDTO.approverId}) and "
	        + " tpo.approvedStatus is null and "
	        + " tpo.active=1";
     
	String FILTER1 = " (:#{#draftOrderRequestDTO.vendor} IS NULL OR tpo.vendor = :#{#draftOrderRequestDTO.vendor}) and "
	        + " (:#{#draftOrderRequestDTO.institute} IS NULL OR tpo.institute = :#{#draftOrderRequestDTO.institute}) and  "
	        + " (:#{#draftOrderRequestDTO.createdDate} IS NULL OR tpo.created_date = :#{#draftOrderRequestDTO.createdDate}) and "
	        + " (:#{#draftOrderRequestDTO.approverId} IS NULL OR tpo.purchaseApproverId = :#{#draftOrderRequestDTO.approverId}) and "
	        + " tpo.approvedStatus is null and "
	        + " tpo.active = 0";
	
     @Query(value = "SELECT new com.au.dto.DraftOrderResponseDTO( "
    	        + "tpo.vendor as vendor, "
    	        + "tpo.institute as institute, "
    	        + "tpo.created_date as createdDate, "
    	        + "tpo.purchaseApprover as purchaseApprover, "
    	        + "tpo.temporary_purchase_order_id as temporaryPurchaseOrderId, "
    	        + "tpo.remarks as remarks, "
    	        + "tpo.billApprovedId as billApprovedId, "
    	        + "ua.username as userName, "
    	        + "tpo.createdUsername as createdUsername, "
    	        + "tpo.tpoAttachmentFilePath as tpoAttachmentFilePath, "
    	        + "tpo.billApprovedStatus as billApprovedStatus, "
    	        + "SUM(tpi.totalAmount) as totalAmount,"
    	        + "tpo.annexureText As annexureText,tpo.empId As empId  "
    	        + ") FROM TemporaryPurchaseOrder tpo "
    	        + "LEFT JOIN TemporaryPurchaseItems tpi ON tpi.temporaryPurchaseOrder.temporary_purchase_order_id = tpo.temporary_purchase_order_id "
    	        + "LEFT JOIN UserAuthentication ua ON tpo.billApprovedId = ua.id "
    	        + "WHERE " + FILTER + " "
    	        + "GROUP BY tpo.vendor, tpo.institute, tpo.created_date, tpo.purchaseApprover, "
    	        + "tpo.temporary_purchase_order_id, tpo.remarks, tpo.billApprovedId, ua.username, tpo.createdUsername, "
    	        + "tpo.tpoAttachmentFilePath, tpo.billApprovedStatus "
    	        + "ORDER BY tpo.created_date",
    	        nativeQuery = false)
	Page<Object> getDraftPurchaseOrder(@Param("draftOrderRequestDTO") DraftOrderRequestDTO draftOrderRequestDTO, Pageable pageable);
	
     @Query(value = "SELECT new com.au.dto.DraftOrderResponseDTO( "
    	        + "tpo.vendor as vendor, "
    	        + "tpo.institute as institute, "
    	        + "tpo.created_date as createdDate, "
    	        + "tpo.purchaseApprover as purchaseApprover, "
    	        + "tpo.temporary_purchase_order_id as temporaryPurchaseOrderId, "
    	        + "tpo.remarks as remarks, "
    	        + "tpo.billApprovedId as billApprovedId, "
    	        + "ua.username as userName, "
    	        + "tpo.createdUsername as createdUsername, "
    	        + "tpo.tpoAttachmentFilePath as tpoAttachmentFilePath, "
    	        + "tpo.billApprovedStatus as billApprovedStatus, "
    	        + "SUM(tpi.totalAmount) as totalAmount,"
    	        + "tpo.annexureText As annexureText ,tpo.empId As empId "
    	        + ") FROM TemporaryPurchaseOrder tpo "
    	        + "LEFT JOIN TemporaryPurchaseItems tpi ON tpi.temporaryPurchaseOrder.temporary_purchase_order_id = tpo.temporary_purchase_order_id "
    	        + "LEFT JOIN UserAuthentication ua ON tpo.billApprovedId = ua.id "
    	        + "WHERE " + FILTER1 + " "
    	        + "GROUP BY tpo.vendor, tpo.institute, tpo.created_date, tpo.purchaseApprover, "
    	        + "tpo.temporary_purchase_order_id, tpo.remarks, tpo.billApprovedId, ua.username, tpo.createdUsername, "
    	        + "tpo.tpoAttachmentFilePath, tpo.billApprovedStatus "
    	        + "ORDER BY tpo.created_date",
    	        nativeQuery = false)
	Page<Object> getCancelledDraftPurchaseOrder(@Param("draftOrderRequestDTO") DraftOrderRequestDTO draftOrderRequestDTO, Pageable pageable);

	@Query(value=" select tpo from TemporaryPurchaseOrder tpo left join TemporaryPurchaseItems tpi on tpi.temporaryPurchaseOrder=tpo.temporary_purchase_order_id where tpo.temporary_purchase_order_id=:id ")
	TemporaryPurchaseOrder getDraftPurchaseOrderById(@Param("id") Integer id);

	@Modifying
	@Query(value="Update temp_purchase_order tpo Set tpo.tpo_attachment_file_path=?1 Where tpo.temporary_purchase_order_id=?2",nativeQuery = true)
	void updationOfTemporaryPurchaseOrderFilePath(String purchaseOrderFilePath, Integer temporary_purchase_order_id);

	@Modifying
	@Query(value="Update purhcase_grn grn Set grn.active=false Where grn.purchase_order_id=?1",nativeQuery = true)
	void deactivateGRN(Integer purchaseOrderId);

	@Modifying
	@Query(value="Update temp_purchase_order tpo Set tpo.active=false Where tpo.temporary_purchase_order_id=(select po.temporary_purchase_order_id from purchase_order po where po.purchase_order_id=?1)",nativeQuery = true)
	void deactivateTemporaryPurchaseOrder(Integer purchaseOrderId);
	
	@Modifying
	@Query(value="Update temp_purchase_items tpi Set tpi.active=false Where tpi.temporary_purchase_order_id=(select po.temporary_purchase_order_id from purchase_order po where po.purchase_order_id=?1)",nativeQuery = true)
	void deactivateTemporaryPurchaseItems(Integer purchaseOrderId);

}
