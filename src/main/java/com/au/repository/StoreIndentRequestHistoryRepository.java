package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.StoreIndentRequestHistory;

@Repository
@Transactional
public interface StoreIndentRequestHistoryRepository extends JpaRepository<StoreIndentRequestHistory, Integer>{

	
	
	
	@Query(value = "select new map(sirh.store_indent_request_history_id as id,sirh.store_indent_request_id as store_indent_request_id,"
			+ "sirh.quantity as quantity,ic.item_names as item_names,sirh.issued_status as issued_status,sirh.remarks as remarks,"
			+ "sirh.approver1_id as approver1_id,eiis.item_description as item_description,sirh.env_item_id as env_item_id,"
			+ "sirh.created_username as created_username,sirh.modified_username as modified_username,ed.emp_id as emp_id,"
			+ "sirh.approver1_status as approver1_status,sirh.approver2_status as approver2_status,sirh.indent_ticket as indent_ticket,"
			+ "ua1.username as StoreIndent_approver1_name,ua2.username as StoreIndent_approver2_name,sir.cancel_status As cancel_status,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(sirh.created_date, '%d/%m/20%y')) as requested_by_With_date,"
			+ "sirh.created_date as created_date,sirh.modified_date as modified_date,sirh.created_by as created_by,"
			+ "sirh.approver1_remarks as approver1_remarks,ed.employee_name as employee_name,sirh.description as description,"
			+ "sirh.requested_by as requested_by,sirh.requested_date as requested_date,sirh.financial_year_id as financial_year_id,"
			+ "sirh.purpose as purpose,sirh.stock_description as stock_description,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name) "
			+ "from StoreIndentRequestHistory sirh "
			+ "left join StoreIndentRequest sir on sir.store_indent_request_id=sirh.store_indent_request_id "
			+ "left join EmployeeDetails ed on sirh.emp_id=ed.emp_id "
			+ "left join UserAuthentication ua1 on ed.store_indent_approver1=ua1.id "
			+ "left join UserAuthentication ua2 on ed.store_indent_approver2=ua2.id "
			+ "left join EnvItemsInStores eiis on sirh.env_item_id = eiis.env_item_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Designation d on d.designation_id=ed.designation_id "
		    + "where (:requested_by IS NULL OR sirh.requested_by = :requested_by) And "
		    + "CONCAT(IfNull(sir.created_username,''),'',IfNull(sir.indent_ticket,''),'',IfNull(sir.store_indent_request_id,''),'',"
			+ "'',IfNull(sir.created_by,''),'',IfNull(sir.created_date,'')) LIKE %:keyword% GROUP BY sirh.indent_ticket")
	public Page<Object> getAllDataFilteredByKeyword1(Pageable pageable, Object keyword, Integer requested_by); 
	
	
	@Query(value = "select new map(sirh.store_indent_request_history_id as id,sirh.store_indent_request_id as store_indent_request_id,"
			+ "sirh.quantity as quantity,ic.item_names as item_names,sirh.issued_status as issued_status,sirh.remarks as remarks,"
			+ "sirh.approver1_id as approver1_id,eiis.item_description as item_description,sirh.env_item_id as env_item_id,"
			+ "sirh.created_username as created_username,sirh.modified_username as modified_username,ed.emp_id as emp_id,"
			+ "sirh.approver1_status as approver1_status,sirh.approver2_status as approver2_status,sirh.indent_ticket as indent_ticket,"
			+ "ua1.username as StoreIndent_approver1_name,ua2.username as StoreIndent_approver2_name,sir.cancel_status As cancel_status,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(sirh.created_date, '%d/%m/20%y')) as requested_by_With_date,"
			+ "sirh.created_date as created_date,sirh.modified_date as modified_date,sirh.created_by as created_by,"
			+ "sirh.approver1_remarks as approver1_remarks,ed.employee_name as employee_name,sirh.description as description,"
			+ "sirh.requested_by as requested_by,sirh.requested_date as requested_date,sirh.financial_year_id as financial_year_id,"
			+ "sirh.purpose as purpose,sirh.stock_description as stock_description,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name) "
			+ "from StoreIndentRequestHistory sirh "
			+ "left join StoreIndentRequest sir on sir.store_indent_request_id=sirh.store_indent_request_id "
			+ "left join EmployeeDetails ed on sirh.emp_id=ed.emp_id "
			+ "left join UserAuthentication ua1 on ed.store_indent_approver1=ua1.id "
			+ "left join UserAuthentication ua2 on ed.store_indent_approver2=ua2.id "
			+ "left join EnvItemsInStores eiis on sirh.env_item_id = eiis.env_item_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Designation d on d.designation_id=ed.designation_id "
			+ "where (:requested_by IS NULL OR sirh.requested_by = :requested_by) "
			+ "GROUP BY sirh.indent_ticket")
	public Page<Object> getAllSortedData1(Pageable pageable, Integer requested_by);	
}
