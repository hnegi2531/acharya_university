package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.dto.StockIssueByItemAssigmentDTO;
import com.au.dto.StockIssuesByStockNo;
import com.au.model.StockIssue;

@Repository
public interface StockIssueRepository extends JpaRepository<StockIssue, Long> {

	@Query("SELECT new com.au.dto.StockIssuesByStockNo("
	        + " MAX(st.storeId) as storeId,"
	        + " MAX(st.storeName) as storeName,"
	        + " MAX(st.description) as description,"
	        + " SUM(st.availableQuantity) as availableQuantity,"
	        + " SUM(st.stocks) as stocks,"
	        + " MAX(st.purpose) as purpose,"
	        + " MAX(st.createdBy) as createdBy,"
	        + " MAX(st.createdByUserName) as createdByUserName,"
	        + " MAX(st.endUserName) as endUserName,"
	        + " MAX(st.endUserId) as endUserId,"
	        + " MAX(st.stockNo) as stockNo,"
	        + " SUM(st.issueQuantity) as issueQuantity,"
	        + " MAX(st.createdDate) as createdDate,"
	        + " MAX(st.uom) as uom)"
	        + " FROM StockIssue st GROUP BY st.stockNo")
	Page<StockIssuesByStockNo> getStockIssue(Pageable pageable);
	
	@Query("SELECT SUM(g.issueQuantity) FROM StockIssue g WHERE g.itemId=:envItemId")
	Double sumQuantityByItemName(@Param("envItemId") Integer envItemId);

	@Query(value = " select new com.au.dto.StockIssueByItemAssigmentDTO( s.indent_ticket as stockNumber, s.requested_date as requestedDate,  " +
			"s.quantity as requestedQuantity,s.issued_quantity as issuedQuantity, m.measure_short_name as uom," +
			"e.item_description as  itemDescription,i.item_names as itemName,e.item_assigment_name as itemAssignmentName, " +
			"e.make as make, s.issueDate as issueDate, s.created_username as issueTo ) " +
			"from StoreIndentRequest s  left join EnvItemsInStores e on e.env_item_id=s.env_item_id " +
			"left join ItemsCreation i  on i.item_id=e.item_id " +
			"left join Measure m on m.measure_id=s.measure_id " +
			"where s.env_item_id=:item_assigment_id  and s.active = 1 and s.issueDate is not null")
	List<StockIssueByItemAssigmentDTO> getStockIssueByItemAssigmentId(
			@Param("item_assigment_id") Integer item_assigment_id);

	List<StockIssue> findByStockNo(String stockNumber);
	
	@Query(value=" select gr.uom from StockIssue gr where gr.itemId=:itemId", nativeQuery = false)
	String getStockUom(@Param("itemId") Integer itemId);

	
}
