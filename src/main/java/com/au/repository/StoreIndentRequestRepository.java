package com.au.repository;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.StoreIndentRequest;

@Repository
@Transactional
public interface StoreIndentRequestRepository extends JpaRepository<StoreIndentRequest, Integer>{

	
	@Query(value = "SELECT sir from StoreIndentRequest sir where sir.active=true and sir.purchase_status=0 ")
	public List<StoreIndentRequest> findAll11();
	
	@Modifying
	@Query(value = "update StoreIndentRequest sir set sir.active=false where sir.store_indent_request_id=?1")
	public void updateExamDetail(Integer id);
	
	@Modifying
	@Query(value = "update StoreIndentRequest sir set sir.active=true where sir.store_indent_request_id=?1")
	public void updateExamDetail1(Integer id);
	
	

	@Query(value = "select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,sir.quantity as quantity,"
			+ "sir.stock_description as stock_description,sir.purpose as purpose,sir.requested_by as requested_by,sir.remarks as remarks,"
			+ "sir.requested_date as requested_date,sir.purchase_status as purchase_status,sir.approver1_id as approver1_id,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(CURRENT_TIMESTAMP, '%d/%m/20%y')) as requested_by_With_date,sir.cancel_status As cancel_status,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,sir.approver2_id as approver2_id,"
			+ "sir.approver2_remarks as approver2_remarks,sir.approver2_date as approver2_date,sir.approver1_status as approver1_status,"
			+ "sir.approver2_status as approver2_status,sir.approver1_active as approver1_active,sir.approver2_active as approver2_active,"
			+ "sir.item_id as item_id,sir.description as description,sir.others as others,sir.school_id as school_id,"
			+ "sir.dept_id as dept_id,sir.tag_id as tag_id,sir.draft_po_status as draft_po_status,sir.grn_date as grn_date,"
			+ "ua1.username as StoreIndent_approver1_name,ua2.username as StoreIndent_approver2_name,"
			+ "sir.expense_head_id as expense_head_id,sir.financial_year_id as financial_year_id,sir.purchase_request as purchase_request,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,sir.issued_status as issued_status,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,sir.measure_id as measure_id,"
			+ "sir.env_item_id as env_item_id,eiis.item_description as item_description,dt.dept_name_short as dept_name_short,"
			+ "eiis.total_issued as total_issued,eiis.item_serial_no as item_serial_no,eiis.make as make,sir.emp_id as emp_id,"
			+ "ed.employee_name as employee_name,d.designation_name as designation_name,d.designation_short_name as designation_short_name,"
			+ "ic.item_names as item_names,ic.item_short_name as item_short_name,ic.item_type as item_type,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_name as vendor_name,dt.dept_name as dept_name,"
			+ "eiis.opening_balance as opening_balance,eiis.total_available_in_stores as total_available_in_stores,"
			+ "sir.item_assignment_id as item_assignment_id,sir.ac_year_id as ac_year_id,sir.ledger_id as ledger_id,sir.received_status as received_status,"
			+ "sir.modified_by as modified_by,sir.active as active, sir.purchase_status as purchase_status) from StoreIndentRequest sir "
			+ "left join EnvItemsInStores eiis on eiis.env_item_id = sir.env_item_id "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join UserAuthentication ua1 on ed.store_indent_approver1=ua1.id "
			+ "left join UserAuthentication ua2 on ed.store_indent_approver2=ua2.id "
			+ "left join Designation d on d.designation_id=ed.designation_id "
			+ "left join Vendor v on sir.vendor_id=v.vendor_id "
			+ "left join Department dt on sir.dept_id=dt.dept_id "
			+ "left join ItemsCreation ic on sir.item_id=ic.item_id "
		    + "where (:created_by IS NULL OR sir.created_by = :created_by) "
		    + "And CONCAT(IfNull(sir.created_username,''),'',IfNull(sir.indent_ticket,''),'',IfNull(sir.stock_description,''),'',"
			+ "'',IfNull(ed.employee_name,''),'',IfNull(sir.created_date,'')) LIKE %:keyword% and  sir.active=true "
			+ "And (sir.cancel_status !=true or sir.cancel_status is null) GROUP BY sir.indent_ticket")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer created_by); 
	
	@Query(value = "select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,sir.quantity as quantity,"
			+ "sir.stock_description as stock_description,sir.purpose as purpose,sir.requested_by as requested_by,sir.remarks as remarks,"
			+ "sir.requested_date as requested_date,sir.purchase_status as purchase_status,sir.approver1_id as approver1_id,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(CURRENT_TIMESTAMP, '%d/%m/20%y')) as requested_by_With_date,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,sir.approver2_id as approver2_id,"
			+ "sir.approver2_remarks as approver2_remarks,sir.approver2_date as approver2_date,sir.approver1_status as approver1_status,"
			+ "sir.approver2_status as approver2_status,sir.approver1_active as approver1_active,sir.approver2_active as approver2_active,"
			+ "sir.item_id as item_id,sir.description as description,sir.others as others,sir.school_id as school_id,"
			+ "ua1.username as StoreIndent_approver1_name,ua2.username as StoreIndent_approver2_name,sir.cancel_status As cancel_status,"
			+ "sir.dept_id as dept_id,sir.tag_id as tag_id,sir.draft_po_status as draft_po_status,sir.grn_date as grn_date,"
			+ "sir.expense_head_id as expense_head_id,sir.financial_year_id as financial_year_id,sir.purchase_request as purchase_request,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,sir.issued_status as issued_status,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,sir.measure_id as measure_id,"
			+ "sir.env_item_id as env_item_id,eiis.item_description as item_description,dt.dept_name_short as dept_name_short,"
			+ "eiis.total_issued as total_issued,eiis.item_serial_no as item_serial_no,eiis.make as make,sir.emp_id as emp_id,"
			+ "ed.employee_name as employee_name,d.designation_name as designation_name,d.designation_short_name as designation_short_name,"
			+ "ic.item_names as item_names,ic.item_short_name as item_short_name,ic.item_type as item_type,sir.received_status as received_status,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_name as vendor_name,dt.dept_name as dept_name,"
			+ "eiis.opening_balance as opening_balance,eiis.total_available_in_stores as total_available_in_stores,"
			+ "sir.item_assignment_id as item_assignment_id,sir.ac_year_id as ac_year_id,sir.ledger_id as ledger_id,"
			+ "sir.modified_by as modified_by,sir.active as active, sir.purchase_status as purchase_status) "
			+ " from StoreIndentRequest sir "
			+ "left join EnvItemsInStores eiis on eiis.env_item_id = sir.env_item_id "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join UserAuthentication ua1 on ed.store_indent_approver1=ua1.id "
			+ "left join UserAuthentication ua2 on ed.store_indent_approver2=ua2.id "
			+ "left join Designation d on d.designation_id=ed.designation_id "
			+ "left join Vendor v on sir.vendor_id=v.vendor_id "
			+ "left join Department dt on sir.dept_id=dt.dept_id "
			+ "left join ItemsCreation ic on sir.item_id=ic.item_id "
			+ "where (:created_by IS NULL OR sir.created_by = :created_by) "
			+ "And sir.active=true And (sir.cancel_status !=true or sir.cancel_status is null) GROUP BY sir.indent_ticket")
	public Page<Object> getAllSortedData(Pageable pageable, Integer created_by);
	
	@Query(value = "select count(*) from StoreIndentRequest sir where sir.stock_description=?1 and sir.active=true")
	public Integer getcountOfStockDescription(String stock_description);
	
	
	@Query(value = "select ifNull(indent_ticket,0) from store_indent_request ORDER BY store_indent_request_id Desc LIMIT 1",nativeQuery = true)
	public String getMaxStoreIndentRequestCount( );
	
	@Query(value = "select * from store_indent_request ORDER BY store_indent_request_id Desc LIMIT 1",nativeQuery = true)
	public StoreIndentRequest getLatestIndentRequest();
	
	@Query(value = "SELECT sir.store_indent_request_id as store_indent_request_id,ed.employee_name as employee_name,DATE_FORMAT(CURRENT_TIMESTAMP, '%d/%m/20%y') as requested_date,"
			+ "concat(ed.employee_name,'-',requested_date) as requested_by_With_date from StoreIndentRequest sir "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id")
	public List<Map<String,Object>> getEmpNameConcatWithDate();
	
//	@Query(value = "select new map(sir.store_indent_request_id as store_indent_request_id,sir.indent_ticket as indent_ticket,"
//			+ "ed.employee_name as employee_name,sir.requested_date as requested_date,DATE_FORMAT(CURRENT_TIMESTAMP, '%d/%m/20%y') as "
//			+ "requested_date,concat(ed.employee_name,'-',requested_date) as requested_by_With_date,sir.emp_id as emp_id,"
//			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name,"
//			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,ed.designation_id as designation_id,"
//			+ "sir.approver1_status as approver1_status)"
//			+ " from StoreIndentRequest sir "
//			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
//			+ "left join Designation d on d.designation_id=ed.designation_id")
//	public List<Map<String,Object>> getItemApprover();

	@Query(value = "select sir.store_indent_request_id as store_indent_request_id,sir.indent_ticket as indent_ticket,"
			+ "sir.approver1_id as approver1_id,eiis.item_description as item_description,sir.env_item_id as env_item_id,ed.emp_id as emp_id,"
			+ "DATE_FORMAT(CURRENT_TIMESTAMP, '%d/%m/20%y') as requested_date,sir.cancel_status As cancel_status,"
			+ "concat(ed.employee_name,'-',requested_date) as requested_by_With_date,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,ed.employee_name as employee_name,"
			+ "sir.approver1_status as approver1_status,d.designation_name as designation_name,d.designation_short_name as designation_short_name, "
			+ "(select employee_name from employee_details ed where ed.emp_id =sir.approver1_id) as leave_app1, "
			+ "(select employee_name  from employee_details a where emp_id =a.store_indent_approver1) as reported "
			+ "from store_indent_request sir "
			+ "left join employee_details ed on sir.emp_id=ed.emp_id "
			+ "left join env_items_in_stores eiis on eiis.env_item_id = sir.env_item_id "
			+ "left join designation d on d.designation_id=ed.designation_id GROUP BY indent_ticket",nativeQuery = true)
	public List<Map<String,Object>> getItemApproverdata(Integer store_indent_request_id);
	
	
	@Query(value = "Select sir.indent_ticket as indent_ticket,sir.item_id as item_id,ic.item_names as item_names,"
			+ "sir.quantity as quantity,sir.measure_id as measure_id,m.measure_name as measure_name,ic.item_short_name as item_short_name,"
			+ "m.measure_short_name as measure_short_name,sir.remarks as remarks  from store_indent_request sir "
			+ "left join items_creation ic on sir.item_id=ic.item_id "
			+ "left join measures m on sir.measure_id=m.measure_id "
			+ "where sir.store_indent_request_id=?1 ",nativeQuery = true)
	public List<Map<String, Object>> getDataForDisplaying(Integer store_indent_request_id);
	
	
	
	
	@Query(value = "select new map(sir.store_indent_request_id as store_indent_request_id,sir.indent_ticket as indent_ticket,sir.quantity as quantity,"
			+ "sir.stock_description as stock_description,sir.purpose as purpose,sir.requested_by as requested_by,sir.remarks as remarks,"
			+ "sir.requested_date as requested_date,sir.purchase_status as purchase_status,sir.approver1_id as approver1_id,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(CURRENT_TIMESTAMP, '%d/%m/20%y')) as requested_by_With_date,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,sir.approver2_id as approver2_id,"
			+ "sir.approver2_remarks as approver2_remarks,sir.approver2_date as approver2_date,sir.approver1_status as approver1_status,"
			+ "sir.approver2_status as approver2_status,sir.approver1_active as approver1_active,sir.approver2_active as approver2_active,"
			+ "sir.item_id as item_id,sir.description as description,sir.others as others,sir.school_id as school_id,"
			+ "ua1.username as StoreIndent_approver1_name,ua2.username as StoreIndent_approver2_name,sir.cancel_status As cancel_status,"
			+ "sir.dept_id as dept_id,sir.tag_id as tag_id,sir.draft_po_status as draft_po_status,sir.grn_date as grn_date,"
			+ "sir.expense_head_id as expense_head_id,sir.financial_year_id as financial_year_id,sir.purchase_request as purchase_request,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,sir.issued_status as issued_status,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,sir.measure_id as measure_id,"
			+ "sir.env_item_id as env_item_id,eiis.item_description as item_description,dt.dept_name_short as dept_name_short,"
			+ "eiis.total_issued as total_issued,eiis.item_serial_no as item_serial_no,eiis.make as make,sir.emp_id as emp_id,"
			+ "ed.employee_name as employee_name,d.designation_name as designation_name,d.designation_short_name as designation_short_name,"
			+ "ic.item_names as item_names,ic.item_short_name as item_short_name,ic.item_type as item_type,sir.received_status as received_status,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_name as vendor_name,dt.dept_name as dept_name,"
			+ "eiis.opening_balance as opening_balance,eiis.total_available_in_stores as total_available_in_stores,"
			+ "sir.item_assignment_id as item_assignment_id,sir.ac_year_id as ac_year_id,sir.ledger_id as ledger_id,"
			+ "sir.modified_by as modified_by,sir.active as active, sir.purchase_status as purchase_status) "
			+ " from StoreIndentRequest sir "
			+ "left join EnvItemsInStores eiis on eiis.env_item_id = sir.env_item_id "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join UserAuthentication ua1 on ed.store_indent_approver1=ua1.id "
			+ "left join UserAuthentication ua2 on ed.store_indent_approver2=ua2.id "
			+ "left join Designation d on d.designation_id=ed.designation_id "
			+ "left join Vendor v on sir.vendor_id=v.vendor_id "
			+ "left join Department dt on sir.dept_id=dt.dept_id "
			+ "left join ItemsCreation ic on sir.item_id=ic.item_id "
			+ "where sir.indent_ticket=?1 and sir.approver1_status =0")
	public List<Map<String, Object>> getDataForDisplaying2(String indent_ticket);
	
	
	@Query(value = "select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,"
			+ "sir.quantity as quantity,ic.item_names as item_names,sir.issued_status as issued_status,sir.remarks as remarks,"
			+ "sir.approver1_id as approver1_id,eiis.item_description as item_description,sir.env_item_id as env_item_id,ed.emp_id as emp_id,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,"
			+ "sir.approver1_status as approver1_status,sir.approver2_status as approver2_status,sir.cancel_status As cancel_status,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(sir.created_date, '%d/%m/20%y')) as requested_by_With_date,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,ed.employee_name as employee_name,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name) "
			+ "from StoreIndentRequest sir "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join EnvItemsInStores eiis on sir.env_item_id = eiis.env_item_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Designation d on d.designation_id=ed.designation_id GROUP BY sir.indent_ticket")
	public Page<Object> getAllDataFilteredByKeyword1(Pageable pageable, Object keyword); 
	
//	@Query(value = "select sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,"
//			+ "sir.approver1_id as approver1_id,eiis.item_description as item_description,sir.env_item_id as env_item_id,ed.emp_id as emp_id,"
//			+ "sir.created_username as created_username,sir.modified_username as modified_username,"
//			+ "concat(ed.employee_name,'-',DATE_FORMAT(CURRENT_TIMESTAMP, '%d/%m/20%y')) as requested_by_With_date,"
//			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,"
//			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,ed.employee_name as employee_name,"
//			+ "sir.approver1_status as approver1_status,d.designation_name as designation_name,d.designation_short_name as designation_short_name "
//			+ "from store_indent_request sir "
//			+ "left join employee_details ed on sir.emp_id=ed.emp_id "
//			+ "left join env_items_in_stores eiis on eiis.env_item_id = sir.env_item_id "
//			+ "left join designation d on d.designation_id=ed.designation_id",nativeQuery=true)
	
	@Query(value = "select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,"
			+ "sir.quantity as quantity,ic.item_names as item_names,sir.issued_status as issued_status,sir.remarks as remarks,"
			+ "sir.approver1_id as approver1_id,eiis.item_description as item_description,sir.env_item_id as env_item_id,ed.emp_id as emp_id,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,sir.cancel_status As cancel_status,"
			+ "sir.approver1_status as approver1_status,sir.approver2_status as approver2_status,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(sir.created_date, '%d/%m/20%y')) as requested_by_With_date,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,ed.employee_name as employee_name,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name) "
			+ "from StoreIndentRequest sir "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join EnvItemsInStores eiis on sir.env_item_id = eiis.env_item_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Designation d on d.designation_id=ed.designation_id GROUP BY sir.indent_ticket")
	public Page<Object> getAllSortedData1(Pageable pageable);

//	+ "concat(ed.employee_name,'-',DATE_FORMAT(CURRENT_TIMESTAMP, '%d/%m/20%y')) as requested_by_With_date,"
//+ "(select employee_name from employee_details ed where ed.emp_id =sir.approver1_id) as leave_app1, "
//+ "(select employee_name  from employee_details a where emp_id =a.store_indent_approver1) as reported) "
	
	
	
	@Query(value = "Select (eiis.total_available_in_stores - eiis.total_issued) "
			+ "  from store_indent_request sir "
			+ "left join env_items_in_stores eiis on eiis.env_item_id = sir.env_item_id "
			+ "where sir.env_item_id=?1 ",nativeQuery = true)
	public Integer getTotalQuantityAvailable(Integer env_item_id);
	
	@Query(value = "update EnvItemsInStores set total_available_in_stores=?1 where env_item_id=?2 and active=true")
		public void updateTotalQuantity(Integer value,Integer env_item_id);
	
	
	
	@Query(value = "select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,"
			+ "sir.quantity as quantity,ic.item_names as item_names,sir.issued_status as issued_status,sir.remarks as remarks,"
			+ "sir.approver1_id as approver1_id,eiis.item_description as item_description,sir.env_item_id as env_item_id,ed.emp_id as emp_id,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,sir.cancel_status As cancel_status,"
			+ "ua1.username as StoreIndent_approver1_name,ua2.username as StoreIndent_approver2_name,eiis.item_id as item_id,"
			+ "concat(ic.item_names,'-',eiis.item_description,'-',eiis.make) as ITEM_NAME,sir.issuedBy As issuedBy,"
			+ "ed1.employee_name As issuedByName,ed1.empcode As issuedByNameEmpcode,sir.issueDate As issueDate,"
			+ "sir.approver1_status as approver1_status,sir.approver2_status as approver2_status,sir.stock_description as stock_description,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(sir.created_date, '%d/%m/20%y')) as requested_by_With_date,sir.purchase_status as purchase_status,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,sir.received_status as received_status,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,ed.employee_name as employee_name,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name,sir.description as description,"
			+ "sir.issueDate as issueDate,sir.issuedBy as issuedBy,ed.dept_id as dept_id,ed.school_id as school_id,dp.dept_name as dept_name,dp.dept_name_short as dept_name_short,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,sir.issued_quantity as issued_quantity,"
			+ "sir.others as others,ms.measure_name as measure_name,ms.measure_short_name as measure_short_name) from StoreIndentRequest sir "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join EmployeeDetails ed1 on sir.issuedBy=ed1.emp_id "
			+ "left join UserAuthentication ua1 on ed.store_indent_approver1=ua1.id "
			+ "left join UserAuthentication ua2 on ed.store_indent_approver2=ua2.id "
			+ "left join Department dp on ed.dept_id=dp.dept_id "
			+ "left join Schools sc on ed.school_id=sc.school_id "
			+ "left join EnvItemsInStores eiis on sir.env_item_id = eiis.env_item_id "
			+ "left join Measure ms on eiis.measure_id = ms.measure_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Designation d on d.designation_id=ed.designation_id "
			+ "where sir.indent_ticket=?1")
	public List<Map<String, Object>> getItemApproverDataBasedOnIndentTicket(String indent_ticket);
	
	@Query(value = "select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,eiis.total_issued as total_issued,"
			+ "eiis.total_available_in_stores as total_available_in_stores,sir.description as description,sir.cancel_status As cancel_status,"
			+ "sir.quantity as quantity,ic.item_names as item_names,sir.issued_status as issued_status,sir.remarks as remarks,"
			+ "sir.approver1_id as approver1_id,eiis.item_description as item_description,sir.env_item_id as env_item_id,ed.emp_id as emp_id,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,"
			+ "sir.approver1_status as approver1_status,sir.approver2_status as approver2_status,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(sir.created_date, '%d/%m/20%y')) as requested_by_With_date,"
			+ "concat (ic.item_names,'-',eiis.item_description) as NameAndDescription,sir.received_status as received_status,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,ed.employee_name as employee_name,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name) "
			+ "from StoreIndentRequest sir "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join EnvItemsInStores eiis on sir.env_item_id = eiis.env_item_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Designation d on d.designation_id=ed.designation_id where sir.approver1_status=1")
	public Page<Object> getApprovedDataFilteredByKeyword1(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,eiis.total_issued as total_issued,"
			+ "eiis.total_available_in_stores as total_available_in_stores,sir.description as description,sir.cancel_status As cancel_status,"
			+ "sir.quantity as quantity,ic.item_names as item_names,sir.issued_status as issued_status,sir.remarks as remarks,"
			+ "sir.approver1_id as approver1_id,eiis.item_description as item_description,sir.env_item_id as env_item_id,ed.emp_id as emp_id,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,sir.received_status as received_status,"
			+ "sir.approver1_status as approver1_status,sir.approver2_status as approver2_status,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(sir.created_date, '%d/%m/20%y')) as requested_by_With_date,"
			+ "concat (ic.item_names,'-',eiis.item_description) as NameAndDescription,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,ed.employee_name as employee_name,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name) "
			+ "from StoreIndentRequest sir "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join EnvItemsInStores eiis on sir.env_item_id = eiis.env_item_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Designation d on d.designation_id=ed.designation_id where sir.approver1_status=1")
	public Page<Object> getApprovedDataSortedData1(Pageable pageable);

		@Query(value = "select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,"
			+ "sir.quantity as quantity,ic.item_names as item_names,sir.issued_status as issued_status,sir.remarks as remarks,sir.cancel_status As cancel_status,"
			+ "sir.approver1_id as approver1_id,eiis.item_description as item_description,sir.env_item_id as env_item_id,ed.emp_id as emp_id,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,"
			+ "sir.approver1_status as approver1_status,sir.approver2_status as approver2_status,sir.received_status as received_status,"
			+ "ua1.username as StoreIndent_approver1_name,ua2.username as StoreIndent_approver2_name,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(sir.created_date, '%d/%m/20%y')) as requested_by_With_date,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,ed.employee_name as employee_name,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name) "
			+ "from StoreIndentRequest sir "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join UserAuthentication ua1 on ed.store_indent_approver1=ua1.id "
			+ "left join UserAuthentication ua2 on ed.store_indent_approver2=ua2.id "
			+ "left join EnvItemsInStores eiis on sir.env_item_id = eiis.env_item_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Designation d on d.designation_id=ed.designation_id "
			+ "where ua1.id=?1 GROUP BY sir.indent_ticket")
	public Page<Object> getItemApproverdataBasedOnUserIdByKeyword1(Pageable pageable, Integer user_id,Object keyword);

	
	
	@Query(value = "select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,"
			+ "sir.quantity as quantity,ic.item_names as item_names,sir.issued_status as issued_status,sir.remarks as remarks,"
			+ "sir.approver1_id as approver1_id,eiis.item_description as item_description,sir.env_item_id as env_item_id,ed.emp_id as emp_id,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,sir.cancel_status As cancel_status,"
			+ "sir.approver1_status as approver1_status,sir.approver2_status as approver2_status,sir.received_status as received_status,"
			+ "ua1.username as StoreIndent_approver1_name,ua2.username as StoreIndent_approver2_name,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(sir.created_date, '%d/%m/20%y')) as requested_by_With_date,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,ed.employee_name as employee_name,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name) "
			+ "from StoreIndentRequest sir "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join UserAuthentication ua1 on ed.store_indent_approver1=ua1.id "
			+ "left join UserAuthentication ua2 on ed.store_indent_approver2=ua2.id "
			+ "left join EnvItemsInStores eiis on sir.env_item_id = eiis.env_item_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Designation d on d.designation_id=ed.designation_id "
			+ "where ua1.id=?1 GROUP BY sir.indent_ticket")
	public Page<Object> getItemApproverdataBasedOnUserId(Pageable pageable1, Integer user_id);

	@Query(value = "Select sir.store_indent_request_id as store_indent_request_id,sir.item_id as item_id,ic.item_names as item_names,"
			+ "sir.issued_quantity as issued_quantity,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "sir.stock_description as stock_description,sir.description as description,sir.issued_status as issued_status,"
			+ "sir.quantity as quantity,sir.measure_id as measure_id,m.measure_name as measure_name,ic.item_short_name as item_short_name,"
			+ "ed.employee_name as employee_name,DATE_FORMAT(CURRENT_TIMESTAMP, '%d/%m/20%y') as requested_date,sir.cancel_status As cancel_status,"
			+ "concat(ed.employee_name,'-',requested_date) as End_user,ed2.employee_name as approver2_name,sir.received_status as received_status,"
			+ "sir.approver2_remarks as approver2_remarks,sir.approver2_date as approver2_date,sir.approver2_id as approver2_id,"
			+ "m.measure_short_name as measure_short_name,sir.remarks as remarks  from store_indent_request sir "
			+ "left join items_creation ic on sir.item_id=ic.item_id "
			+ "left join measures m on sir.measure_id=m.measure_id "
			+ "left join schools sc on sir.school_id=sc.school_id "
			+ "left join employee_details ed on sir.emp_id=ed.emp_id "
			+ " left join employee_details ed2 on ed.emp_id=ed2.store_indent_approver2  "
			+ "where sir.indent_ticket=?1 and sir.issued_status is null GROUP BY sir.store_indent_request_id",nativeQuery = true)
	public List<Map<String, Object>> getDataForDisplaying(String indent_ticket);

		@Modifying
	@Query(value = "select sir.store_indent_request_id from store_indent_request sir  where sir.indent_ticket in (?1) and sir.active=true",nativeQuery=true)
	public List<Integer> getStoreIndentRequest_ids(String indent_ticket);
	
	
	@Modifying
	@Query(value = "update StoreIndentRequest sir set sir.attachment_path=?2 where sir.store_indent_request_id in (?1)")
	public void updatePath(List<Integer> store_indent_request_id, String t2);
	
	
	@Query(value = "Select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,eiis.total_issued as total_issued,"
			+ "eiis.total_available_in_stores as total_available_in_stores,sir.description as description,sir.stock_description as stock_description,"
			+ "sir.quantity as quantity,ic.item_names as item_names,sir.issued_status as issued_status,sir.remarks as remarks,"
			+ "sir.approver1_id as approver1_id,eiis.item_description as item_description,sir.env_item_id as env_item_id,ed.emp_id as emp_id,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,sir.purpose as purpose,"
			+ "sir.approver1_status as approver1_status,sir.approver2_status as approver2_status,sir.requested_by as requested_by,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(sir.created_date, '%d/%m/%Y')) as requested_by_With_date,sir.purchase_status as purchase_status,"
			+ "concat (ic.item_names,'-',eiis.item_description) as NameAndDescription,sir.requested_date as requested_date,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,sir.cancel_status As cancel_status,"
			+ "ua1.username as StoreIndent_approver1_name,ua2.username as StoreIndent_approver2_name,sir.received_status as received_status,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,ed.employee_name as employee_name,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name,sir.approver2_id as approver2_id,"
			+ "sir.approver2_remarks as approver2_remarks,sir.approver2_date as approver2_date,sir.item_id as item_id,sir.others as others,"
			+ "sir.issued_status as issued_status) "
			+ "from StoreIndentRequest sir "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join UserAuthentication ua1 on ed.store_indent_approver1=ua1.id "
			+ "left join UserAuthentication ua2 on ed.store_indent_approver2=ua2.id "
			+ "left join EnvItemsInStores eiis on sir.env_item_id = eiis.env_item_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Designation d on d.designation_id=ed.designation_id "
			+ "where sir.issued_status='Approved' And sir.active=true "
			+ "And (sir.cancel_status !=true or sir.cancel_status is null) And (sir.purchase_status=0 or sir.purchase_status is null) And "
			+ "CONCAT(IfNull(sir.store_indent_request_id,''),'',IfNull(sir.indent_ticket ,''),'',IfNull(eiis.total_issued,''),'',"
			+ "IfNull(sir.issued_status,''),'',IfNull(ic.item_names,''),'',IfNull(sir.created_username,''),'',IfNull(sir.created_date,'')) LIKE %?1%  GROUP BY sir.indent_ticket")
	public Page<Object> storeIndentRequestApprovedDataByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "Select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,eiis.total_issued as total_issued,"
			+ "eiis.total_available_in_stores as total_available_in_stores,sir.description as description,sir.stock_description as stock_description,"
			+ "sir.quantity as quantity,ic.item_names as item_names,sir.issued_status as issued_status,sir.remarks as remarks,"
			+ "sir.approver1_id as approver1_id,eiis.item_description as item_description,sir.env_item_id as env_item_id,ed.emp_id as emp_id,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,sir.purpose as purpose,"
			+ "sir.approver1_status as approver1_status,sir.approver2_status as approver2_status,sir.requested_by as requested_by,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(sir.created_date, '%d/%m/%Y')) as requested_by_With_date,sir.purchase_status as purchase_status,"
			+ "concat (ic.item_names,'-',eiis.item_description) as NameAndDescription,sir.requested_date as requested_date,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,sir.received_status as received_status,"
			+ "ua1.username as StoreIndent_approver1_name,ua2.username as StoreIndent_approver2_name,sir.cancel_status As cancel_status,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,ed.employee_name as employee_name,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name,sir.approver2_id as approver2_id,"
			+ "sir.approver2_remarks as approver2_remarks,sir.approver2_date as approver2_date,sir.item_id as item_id,sir.others as others,"
			+ "sir.issued_status as issued_status) "
			+ "from StoreIndentRequest sir "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join UserAuthentication ua1 on ed.store_indent_approver1=ua1.id "
			+ "left join UserAuthentication ua2 on ed.store_indent_approver2=ua2.id "
			+ "left join EnvItemsInStores eiis on sir.env_item_id = eiis.env_item_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Designation d on d.designation_id=ed.designation_id "
			+ "where sir.issued_status='Approved' And sir.active=true "
			+ "And (sir.cancel_status !=true or sir.cancel_status is null) And (sir.purchase_status=0 or sir.purchase_status is null)  GROUP BY sir.indent_ticket ")
	public Page<Object> storeIndentRequestApprovedDataSortedData(Pageable pageable);
	
	
	@Query(value = "select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,"
			+ "sir.quantity as quantity,ic.item_names as item_names,sir.issued_status as issued_status,sir.remarks as remarks,"
			+ "sir.approver1_id as approver1_id,eiis.item_description as item_description,sir.env_item_id as env_item_id,ed.emp_id as emp_id,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,"
			+ "ua1.username as StoreIndent_approver1_name,ua2.username as StoreIndent_approver2_name,eiis.item_id as item_id,"
			+ "concat(ic.item_names,'-',eiis.item_description,'-',eiis.make) as ITEM_NAME,sir.cancel_status As cancel_status,"
			+ "sir.financial_year_id As financial_year_id,sir.requested_by As requested_by,sir.requested_date As requested_date,"
			+ "sir.approver1_status as approver1_status,sir.approver2_status as approver2_status,sir.stock_description as stock_description,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(sir.created_date, '%d/%m/20%y')) as requested_by_With_date,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,sir.received_status as received_status,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,ed.employee_name as employee_name,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name,sir.description as description,"
			+ "sir.issueDate as issueDate,sir.issuedBy as issuedBy,ed.dept_id as dept_id,ed.school_id as school_id,dp.dept_name as dept_name,dp.dept_name_short as dept_name_short,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,sir.issued_quantity as issued_quantity,"
			+ "sir.others as others,ms.measure_name as measure_name,ms.measure_short_name as measure_short_name) from StoreIndentRequest sir "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join UserAuthentication ua1 on ed.store_indent_approver1=ua1.id "
			+ "left join UserAuthentication ua2 on ed.store_indent_approver2=ua2.id "
			+ "left join Department dp on ed.dept_id=dp.dept_id "
			+ "left join Schools sc on ed.school_id=sc.school_id "
			+ "left join EnvItemsInStores eiis on sir.env_item_id = eiis.env_item_id "
			+ "left join Measure ms on eiis.measure_id = ms.measure_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Designation d on d.designation_id=ed.designation_id "
			+ "where sir.indent_ticket=?1 And sir.issued_status='Approved' And sir.active=true")
	public List<Map<String, Object>> getApprovedStoreIndentRequestByIndentTicket(String indentTicket); 

	
	@Query(value = "SELECT sir from StoreIndentRequest sir where sir.active=true And (sir.issued_status='Pending' or sir.purchase_status=0)")
	public List<StoreIndentRequest> getStoreIndentData();

	@Query(value = "SELECT sir from StoreIndentRequest sir where sir.store_indent_request_id=?1")
	public StoreIndentRequest storeIndentDetails(Integer store_indent_request_id);

	@Query(value="select "
			+ "        s.indent_ticket as indentTicket, "
			+ "        s.stock_description as indentStock, "
			+ "        s.stock_description as issuedStock, "
			+ "        s.quantity as indentedQuantity, "
			+ "        s.issued_quantity as issuedQuantity, "
			+ "        app.employee_name as approverName, "
			+ "        m.measure_short_name as uom, "
			+ "        ed.employee_name as issuedBy, "
			+ "        e.employee_name as endUser, "
			+ "        d.dept_name_short as deptName, "
			+ "        sc.school_name_short as institute "
			+ "    from "
			+ "        store_indent_request s "
			+ "    left join "
			+ "        employee_details e "
			+ "            on e.emp_id=s.created_by "
			+ "    left join "
			+ "        department d "
			+ "            on d.dept_id=e.dept_id "
			+ "    left join "
			+ "        schools sc "
			+ "            on sc.school_id=e.school_id  "
			+ "    left join "
			+ "        employee_details ed "
			+ "            on ed.emp_id=s.issued_by "
			+ "    left join "
			+ "        employee_details app "
			+ "            on app.emp_id=s.approver2_id  "
			+ "    left join "
			+ "        measures m "
			+ "            on m.measure_id=s.measure_id "
			+ "    where "
			+ "        s.env_item_id=:envItemId  and s.purchase_status=1 ", nativeQuery = true)
	public List<Map<String, Object>> getStoreIndentdetailsByStoreIndentId(Integer envItemId);	
	
	@Query("SELECT SUM(g.issued_quantity) FROM StoreIndentRequest g WHERE g.env_item_id=:envItemId")
	Double sumQuantityByItemName(@Param("envItemId") Integer envItemId);

	
	@Query(value = "select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,sir.quantity as quantity,"
			+ "sir.stock_description as stock_description,sir.purpose as purpose,sir.requested_by as requested_by,sir.remarks as remarks,"
			+ "sir.requested_date as requested_date,sir.purchase_status as purchase_status,sir.approver1_id as approver1_id,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(CURRENT_TIMESTAMP, '%d/%m/20%y')) as requested_by_With_date,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,sir.approver2_id as approver2_id,"
			+ "sir.approver2_remarks as approver2_remarks,sir.approver2_date as approver2_date,sir.approver1_status as approver1_status,"
			+ "sir.approver2_status as approver2_status,sir.approver1_active as approver1_active,sir.approver2_active as approver2_active,"
			+ "sir.item_id as item_id,sir.description as description,sir.others as others,sir.school_id as school_id,"
			+ "sir.dept_id as dept_id,sir.tag_id as tag_id,sir.draft_po_status as draft_po_status,sir.grn_date as grn_date,"
			+ "ua1.username as StoreIndent_approver1_name,ua2.username as StoreIndent_approver2_name,sir.cancel_status As cancel_status,"
			+ "sir.expense_head_id as expense_head_id,sir.financial_year_id as financial_year_id,sir.purchase_request as purchase_request,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,sir.issued_status as issued_status,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,sir.measure_id as measure_id,"
			+ "sir.env_item_id as env_item_id,eiis.item_description as item_description,dt.dept_name_short as dept_name_short,"
			+ "eiis.total_issued as total_issued,eiis.item_serial_no as item_serial_no,eiis.make as make,sir.emp_id as emp_id,"
			+ "ed.employee_name as employee_name,d.designation_name as designation_name,d.designation_short_name as designation_short_name,"
			+ "ic.item_names as item_names,ic.item_short_name as item_short_name,ic.item_type as item_type,sir.received_status as received_status,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_name as vendor_name,dt.dept_name as dept_name,"
			+ "eiis.opening_balance as opening_balance,eiis.total_available_in_stores as total_available_in_stores,"
			+ "sir.item_assignment_id as item_assignment_id,sir.ac_year_id as ac_year_id,sir.ledger_id as ledger_id,"
			+ "sir.modified_by as modified_by,sir.active as active, sir.purchase_status as purchase_status) from StoreIndentRequest sir "
			+ "left join EnvItemsInStores eiis on eiis.env_item_id = sir.env_item_id "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join UserAuthentication ua1 on ed.store_indent_approver1=ua1.id "
			+ "left join UserAuthentication ua2 on ed.store_indent_approver2=ua2.id "
			+ "left join Designation d on d.designation_id=ed.designation_id "
			+ "left join Vendor v on sir.vendor_id=v.vendor_id "
			+ "left join Department dt on sir.dept_id=dt.dept_id "
			+ "left join ItemsCreation ic on sir.item_id=ic.item_id "
		    + "where CONCAT(IfNull(sir.created_username,''),'',IfNull(sir.indent_ticket,''),'',IfNull(sir.stock_description,''),'',"
			+ "'',IfNull(sir.created_by,''),'',IfNull(sir.created_date,'')) LIKE %?1% and  sir.active=true And sir.created_by=?2")
	public Page<Object> fetchAllStoreIndentRequestBasedOnUserIdByKeyword(Pageable pageable, Object keyword,
			Integer created_by);

	@Query(value = "select new map(sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,sir.quantity as quantity,"
			+ "sir.stock_description as stock_description,sir.purpose as purpose,sir.requested_by as requested_by,sir.remarks as remarks,"
			+ "sir.requested_date as requested_date,sir.purchase_status as purchase_status,sir.approver1_id as approver1_id,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(CURRENT_TIMESTAMP, '%d/%m/20%y')) as requested_by_With_date,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,sir.approver2_id as approver2_id,"
			+ "sir.approver2_remarks as approver2_remarks,sir.approver2_date as approver2_date,sir.approver1_status as approver1_status,"
			+ "sir.approver2_status as approver2_status,sir.approver1_active as approver1_active,sir.approver2_active as approver2_active,"
			+ "sir.item_id as item_id,sir.description as description,sir.others as others,sir.school_id as school_id,"
			+ "ua1.username as StoreIndent_approver1_name,ua2.username as StoreIndent_approver2_name,sir.received_status as received_status,"
			+ "sir.dept_id as dept_id,sir.tag_id as tag_id,sir.draft_po_status as draft_po_status,sir.grn_date as grn_date,"
			+ "sir.expense_head_id as expense_head_id,sir.financial_year_id as financial_year_id,sir.purchase_request as purchase_request,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,sir.issued_status as issued_status,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,sir.measure_id as measure_id,"
			+ "sir.env_item_id as env_item_id,eiis.item_description as item_description,dt.dept_name_short as dept_name_short,"
			+ "eiis.total_issued as total_issued,eiis.item_serial_no as item_serial_no,eiis.make as make,sir.emp_id as emp_id,"
			+ "ed.employee_name as employee_name,d.designation_name as designation_name,d.designation_short_name as designation_short_name,"
			+ "ic.item_names as item_names,ic.item_short_name as item_short_name,ic.item_type as item_type,sir.cancel_status As cancel_status,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_name as vendor_name,dt.dept_name as dept_name,"
			+ "eiis.opening_balance as opening_balance,eiis.total_available_in_stores as total_available_in_stores,"
			+ "sir.item_assignment_id as item_assignment_id,sir.ac_year_id as ac_year_id,sir.ledger_id as ledger_id,"
			+ "sir.modified_by as modified_by,sir.active as active, sir.purchase_status as purchase_status) "
			+ " from StoreIndentRequest sir "
			+ "left join EnvItemsInStores eiis on eiis.env_item_id = sir.env_item_id "
			+ "left join EmployeeDetails ed on sir.emp_id=ed.emp_id "
			+ "left join UserAuthentication ua1 on ed.store_indent_approver1=ua1.id "
			+ "left join UserAuthentication ua2 on ed.store_indent_approver2=ua2.id "
			+ "left join Designation d on d.designation_id=ed.designation_id "
			+ "left join Vendor v on sir.vendor_id=v.vendor_id "
			+ "left join Department dt on sir.dept_id=dt.dept_id "
			+ "left join ItemsCreation ic on sir.item_id=ic.item_id where sir.active=true And sir.created_by=?1")
	public Page<Object> fetchAllStoreIndentRequestBasedOnUserIdSortedData(Pageable pageable1, Integer created_by);

}
