package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.dto.PurchaseIndentDTO;
import com.au.model.PurchaseIndent;

@Repository
public interface PurchaseIndentRepository extends JpaRepository<PurchaseIndent, Integer> {

	@Query(value = "select new com.au.dto.PurchaseIndentDTO(" + " p.purchaseIndentId , " + "p.envItemId, "
			+ "p.ledgerId, " + "p.itemDescription, " + "p.quantity, " + "p.approxRate, " + "p.budgetAvaialable, "
			+ "p.balanceQuantity, " + "p.vendorName, " + "p.vendorContactNo, " + "p.attachmantPathType, "
			+ "p.remark, ua.username, p.created_date, p.totalValue, p.status,u.username,p.approverId, p.indentNo, p.createdBy, p.approvedDate  ) " + "from PurchaseIndent p "
			+ " left join UserAuthentication ua on ua.id=p.createdBy  "
			+ " left join UserAuthentication u on u.id=p.approverId " + "where p.active = 1", nativeQuery = false)
	List<PurchaseIndentDTO> getAllPurchaseIndent();

	@Query(value = "select new com.au.dto.PurchaseIndentDTO(" + "  p.purchaseIndentId , " + "p.envItemId, "
			+ "p.ledgerId, " + "p.itemDescription, " + "p.quantity, " + "p.approxRate, " + "p.budgetAvaialable, "
			+ "p.balanceQuantity, " + "p.vendorName, " + "p.vendorContactNo, " + "p.attachmantPathType, "
			+ "p.remark, ua.username, p.created_date, p.totalValue, p.status,u.username,p.approverId, p.indentNo, p.createdBy, p.approvedDate ) " + "from PurchaseIndent p "
			+ " left join UserAuthentication ua on ua.id=p.createdBy "
			+ "  left join UserAuthentication u on u.id=p.approverId "
			+ "where p.active = 1 and p.purchaseIndentId=:purchaseIndentId ", nativeQuery = false)
	PurchaseIndentDTO getAllPurchaseIndentById(Integer purchaseIndentId);

	@Query(value = "select new com.au.dto.PurchaseIndentDTO(" + "  p.purchaseIndentId , " + "p.envItemId, "
			+ "p.ledgerId, " + "p.itemDescription, " + "p.quantity, " + "p.approxRate, " + "p.budgetAvaialable, "
			+ "p.balanceQuantity, " + "p.vendorName, " + "p.vendorContactNo, " + "p.attachmantPathType, "
			+ "p.remark, ua.username, p.created_date, p.totalValue, p.status,u.username, p.approverId, p.indentNo, p.createdBy, p.approvedDate ) " + "from PurchaseIndent p "
			+ " left join UserAuthentication ua on ua.id=p.createdBy "
			+ "  left join UserAuthentication u on u.id=p.approverId "
			+ "where p.active = 1 and p.approverId=:approverId and p.status='Pending' ", nativeQuery = false)
	List<PurchaseIndentDTO> getAllPurchaseIndentForApproval(Integer approverId);

	@Query(value = "select new com.au.dto.PurchaseIndentDTO(" + "  p.purchaseIndentId , " + "p.envItemId, "
			+ "p.ledgerId, " + "p.itemDescription, " + "p.quantity, " + "p.approxRate, " + "p.budgetAvaialable, "
			+ "p.balanceQuantity, " + "p.vendorName, " + "p.vendorContactNo, " + "p.attachmantPathType, "
			+ "p.remark, ua.username, p.created_date, p.totalValue, p.status,u.username,p.approverId, p.indentNo, p.createdBy, p.approvedDate ) " + "from PurchaseIndent p "
			+ " left join UserAuthentication ua on ua.id=p.createdBy "
			+ "  left join UserAuthentication u on u.id=p.approverId "
			+ "where p.active = 1 and  p.status=:status ", nativeQuery = false)
	List<PurchaseIndentDTO> getAllPurchaseIndentbyStatus(String status);
	
	@Query(value = "select ifNull(indent_no,0) from purchase_indent ORDER BY purchase_indent_id Desc LIMIT 1",nativeQuery = true)
	public String getMaxPurchaseIndentRequestCount( );
	
	
	@Query(value = "select * from purchase_indent ORDER BY purchase_indent_id Desc LIMIT 1",nativeQuery = true)
	public PurchaseIndent getLatestPurchaseIndentRequest();

	@Query(value = "select new com.au.dto.PurchaseIndentDTO(" + "  p.purchaseIndentId , " + "p.envItemId, "
			+ "p.ledgerId, " + "p.itemDescription, " + "p.quantity, " + "p.approxRate, " + "p.budgetAvaialable, "
			+ "p.balanceQuantity, " + "p.vendorName, " + "p.vendorContactNo, " + "p.attachmantPathType, "
			+ "p.remark, ua.username, p.created_date, p.totalValue, p.status,u.username,p.approverId, p.indentNo, p.createdBy, p.approvedDate  ) " + "from PurchaseIndent p "
			+ " left join UserAuthentication ua on ua.id=p.createdBy "
			+ "  left join UserAuthentication u on u.id=p.approverId "
			+ "where p.active = 1 and p.createdBy=:userId  ", nativeQuery = false)
	List<PurchaseIndentDTO> getAllPurchaseIndentByUserId(Integer userId);

	@Query(value=" select e.store_indent_approver1 as storeIndentApproverId from EmployeeDetails e where e.emp_id=:userId ",nativeQuery = false)
	Map<String, Object> getApproverId(Integer userId);

	@Query(value = "select new com.au.dto.PurchaseIndentDTO(" + "  p.purchaseIndentId , " + "p.envItemId, "
			+ "p.ledgerId, " + "p.itemDescription, " + "p.quantity, " + "p.approxRate, " + "p.budgetAvaialable, "
			+ "p.balanceQuantity, " + "p.vendorName, " + "p.vendorContactNo, " + "p.attachmantPathType, "
			+ "p.remark, ua.username, p.created_date, p.totalValue, p.status,u.username,p.approverId, p.indentNo, p.createdBy, p.approvedDate  ) " + "from PurchaseIndent p "
			+ " left join UserAuthentication ua on ua.id=p.createdBy "
			+ "  left join UserAuthentication u on u.id=p.approverId "
			+ "where p.active=1 and p.status in ('Goods received','Order placed','Rejected')  ", nativeQuery = false)
	List<PurchaseIndentDTO> getPurchaseIndentHistory();

}
