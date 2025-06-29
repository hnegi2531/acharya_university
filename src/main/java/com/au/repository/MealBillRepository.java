package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.MealBill;
import com.au.model.MealType;
import com.au.model.StoreIndentRequest;
import com.au.model.StudentDocumentAudit;


@Transactional
@Repository
public interface MealBillRepository extends JpaRepository<MealBill, Integer>  {

	@Query(value = "SELECT mb from MealBill mb where mb.active=true")
	public List<MealBill> getAllActiveMealBill();
	
	
	@Modifying
	@Query(value = "update MealBill mb set mb.active=false where mb.meal_bill_id=?1")
	public void deactivate(Integer meal_id);
	
	@Modifying
	@Query(value = "update MealBill mb set mb.active=true where mb.meal_bill_id=?1")
	public void activate(Integer meal_id);
	
	
	@Query(value ="Select new map(mb.meal_bill_id As id ,mb.dept_id AS dept_id, mb.school_id AS school_id, mb.bill_number AS bill_number, "
			+ "mb.refreshment_id AS refreshment_id, mb.lock_status AS lock_status, mb.lock_date AS lock_date,mb.total_amount As total_amount, "
			+ "mb.lock_by AS lock_by, mb.approved_by AS approved_by, mb.approved_date AS approved_date,mb.month_year As month_year, "
			+ "mb.financial_year_id AS financial_year_id, mb.created_date AS created_date, mb.modified_date AS modified_date, "
			+ "mb.created_username AS created_username, mb.modified_username AS modified_username,mb.voucher_head_new_id As voucher_head_new_id, "
			+ "mb.created_by AS created_by, mb.modified_by AS modified_by, mb.active AS active,mb.approve_status As approve_status,"
			+ "mrr.approver_remarks As approver_remarks,mrr.approved_status As approved_status,mrr.count As count,mrr.end_user_feedback_remarks As end_user_feedback_remarks,"
			+ "mrr.gross_amount As gross_amount,mrr.meal_id As meal_id,mrr.rate_per_count As rate_per_count,mrr.remarks As remarks,"
			+ "mrr.receive_status as receive_status,mrr.approved_time as approved_time,mrr.delivery_address as delivery_address,mrr.email_status as email_status,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,DAYNAME(mrr.date) as dayname,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approved_date as approved_date,"
			+ "mrr.approved_by as approved_by,mrr.approved_date as approved_date,"
			+ "ua1.username as approver_name,ua.username as mrrCreated_by,mrr.date As mealDate,"
			+ "mrr.approved_count as approved_count,mrr.created_username as mrrCreated_username,mrr.user_id as user_id,"
			+ "sc.school_id AS school_id,sc.school_name As school_name,sc.school_name_short As school_name_short,mrr.receive_date as receive_date,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "vhn.voucher_head As voucher_head,vhn.voucher_head_short_name As voucher_head_short_name) "
			+ "FROM MealBill mb "
			+ "left join MealRefreshmentRequest mrr on mrr.refreshment_id=mb.refreshment_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ " left join UserAuthentication ua on ua.id=mrr.user_id "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "where mb.bill_number=?2 And mb.active=true And CONCAT(IfNull(mb.refreshment_id,''),'',IfNull(mb.bill_number,''),'',IfNull(mrr.date,''),"
			+ "'',IfNull(mb.created_by,''),'',IfNull(mb.created_date,'')) LIKE %?1% ")
	public Page<Object> findAll2(Pageable pageable, String bill_number, Object keyword);

	@Query(value ="Select new map(mb.meal_bill_id As id ,mb.dept_id AS dept_id, mb.school_id AS school_id, mb.bill_number AS bill_number, "
			+ "mb.refreshment_id AS refreshment_id, mb.lock_status AS lock_status, mb.lock_date AS lock_date,mb.total_amount As total_amount, "
			+ "mb.lock_by AS lock_by, mb.approved_by AS approved_by, mb.approved_date AS approved_date,mb.month_year As month_year, "
			+ "mb.financial_year_id AS financial_year_id, mb.created_date AS created_date, mb.modified_date AS modified_date, "
			+ "mb.created_username AS created_username, mb.modified_username AS modified_username,mb.voucher_head_new_id As voucher_head_new_id, "
			+ "mb.created_by AS created_by, mb.modified_by AS modified_by, mb.active AS active,mb.approve_status As approve_status,"
			+ "mrr.approver_remarks As approver_remarks,mrr.approved_status As approved_status,mrr.count As count,mrr.end_user_feedback_remarks As end_user_feedback_remarks,"
			+ "mrr.gross_amount As gross_amount,mrr.meal_id As meal_id,mrr.rate_per_count As rate_per_count,mrr.remarks As remarks,"
			+ "mrr.receive_status as receive_status,mrr.approved_time as approved_time,mrr.delivery_address as delivery_address,mrr.email_status as email_status,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,DAYNAME(mrr.date) as dayname,ua1.username as approver_name,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approved_date as approved_date,mrr.date As mealDate,"
			+ "mrr.approved_by as approved_by,mrr.approved_date as approved_date,mrr.date As mealDate,"
			+ "mrr.approved_count as approved_count,ua.username as mrrCreated_by,mrr.created_username as mrrCreated_username,mrr.user_id as user_id,"
			+ "sc.school_id AS school_id,sc.school_name As school_name,sc.school_name_short As school_name_short,mrr.receive_date as receive_date,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "vhn.voucher_head As voucher_head,vhn.voucher_head_short_name As voucher_head_short_name) "
			+ "FROM MealBill mb "
			+ "left join MealRefreshmentRequest mrr on mrr.refreshment_id=mb.refreshment_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ " left join UserAuthentication ua on ua.id=mrr.user_id "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id where mb.bill_number=?1 And mb.active=true ")
	public Page<Object> findAll3(Pageable pageable, String bill_number);


	@Query(value = "select * from meal_bill ORDER BY meal_bill_id Desc LIMIT 1",nativeQuery = true)
	public MealBill getLatestMealBillNumber();


	@Query(value = "select ifNull(bill_number,0) from meal_bill ORDER BY meal_bill_id Desc LIMIT 1",nativeQuery = true)
	public String getMaxStoreIndentRequestCount();

	@Query(value="SELECT COUNT(*) FROM meal_bill mb where mb.month_year=?1 And mb.voucher_head_new_id=?2 And mb.school_id=?3 And mb.active=true",nativeQuery = true)
	public Integer getCountOfMealBill(String month_year, Integer voucher_head_new_id, Integer school_id);
	
	
	@Query(value ="Select new map(mb.meal_bill_id As id ,mb.dept_id AS dept_id, mb.school_id AS school_id, mb.bill_number AS bill_number, "
			+ "mb.refreshment_id AS refreshment_id, mb.lock_status AS lock_status, mb.lock_date AS lock_date,mb.total_amount As total_amount, "
			+ "mb.lock_by AS lock_by, mb.approved_by AS approved_by, mb.approved_date AS approved_date,mb.month_year As month_year, "
			+ "mb.financial_year_id AS financial_year_id, mb.created_date AS created_date, mb.modified_date AS modified_date, "
			+ "mb.lock_ipAddress As lock_ipAddress,mb.approved_ipAddress As Approved_ipAddress,ua1.username As lockedByUserName,"
			+ "mb.created_username AS created_username, mb.modified_username AS modified_username,mb.voucher_head_new_id As voucher_head_new_id, "
			+ "mb.created_by AS created_by, mb.modified_by AS modified_by, mb.active AS active,mb.approve_status As approve_status,"
			+ "mrr.approver_remarks As approver_remarks,mrr.approved_status As approved_status,mrr.count As count,mrr.end_user_feedback_remarks As end_user_feedback_remarks,"
			+ "mrr.gross_amount As gross_amount,mrr.meal_id As meal_id,mrr.rate_per_count As rate_per_count,mrr.remarks As remarks,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,DAYNAME(mrr.date) as dayname,"
			+ "sc.school_id AS school_id,sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "SUM(mrr.gross_amount) As total_gross_amount,SUM(mb.total_amount) As total,ua.username As approvedByUserName,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "vhn.voucher_head As voucher_head,vhn.voucher_head_short_name As voucher_head_short_name) "
			+ "FROM MealBill mb "
			+ "left join UserAuthentication ua on ua.id=mb.approved_by "
			+ "left join UserAuthentication ua1 on ua1.id=mb.lock_by "
			+ "left join MealRefreshmentRequest mrr on mrr.refreshment_id=mb.refreshment_id "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "where mrr.approved_status=1 And mrr.cancel_by is null And "
			+ "CONCAT(IfNull(mb.refreshment_id,''),'',IfNull(mb.bill_number,''),'',IfNull(mrr.date,''),"
			+ "'',IfNull(mb.created_by,''),'',IfNull(mb.created_date,'')) LIKE %?1% Group By mb.bill_number")
	public Page<Object> findAll4(Pageable pageable,Object keyword);

	@Query(value ="Select new map(mb.meal_bill_id As id ,mb.dept_id AS dept_id, mb.school_id AS school_id, mb.bill_number AS bill_number, "
			+ "mb.refreshment_id AS refreshment_id, mb.lock_status AS lock_status, mb.lock_date AS lock_date,mb.total_amount As total_amount, "
			+ "mb.lock_by AS lock_by, mb.approved_by AS approved_by, mb.approved_date AS approved_date,mb.month_year As month_year, "
			+ "mb.financial_year_id AS financial_year_id, mb.created_date AS created_date, mb.modified_date AS modified_date, "
			+ "mb.lock_ipAddress As lock_ipAddress,mb.approved_ipAddress As Approved_ipAddress,ua1.username As lockedByUserName,"
			+ "mb.created_username AS created_username, mb.modified_username AS modified_username,mb.voucher_head_new_id As voucher_head_new_id, "
			+ "mb.created_by AS created_by, mb.modified_by AS modified_by, mb.active AS active,mb.approve_status As approve_status,"
			+ "mrr.approver_remarks As approver_remarks,mrr.approved_status As approved_status,mrr.count As count,mrr.end_user_feedback_remarks As end_user_feedback_remarks,"
			+ "mrr.gross_amount As gross_amount,mrr.meal_id As meal_id,mrr.rate_per_count As rate_per_count,mrr.remarks As remarks,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,DAYNAME(mrr.date) as dayname,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "sc.school_id AS school_id,sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "SUM(mrr.gross_amount) As total_gross_amount,SUM(mb.total_amount) As total,ua.username As approvedByUserName,"
			+ "vhn.voucher_head As voucher_head,vhn.voucher_head_short_name As voucher_head_short_name) "
			+ "FROM MealBill mb "
			+ "left join UserAuthentication ua on ua.id=mb.approved_by "
			+ "left join UserAuthentication ua1 on ua1.id=mb.lock_by "
			+ "left join MealRefreshmentRequest mrr on mrr.refreshment_id=mb.refreshment_id "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "where mrr.approved_status=1 And mrr.cancel_by is null Group By mb.bill_number")
	public Page<Object> findAll5(Pageable pageable);
}
