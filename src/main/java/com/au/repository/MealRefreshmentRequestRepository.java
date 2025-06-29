package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.dto.MealRefreshmentDTO;
import com.au.model.MealRefreshmentRequest;


@Transactional
@Repository
public interface MealRefreshmentRequestRepository extends JpaRepository<MealRefreshmentRequest,Integer>{
	
	@Query(value = "select ml from MealRefreshmentRequest ml where ml.active=true")
	public List<MealRefreshmentRequest> findAll1();
	
	/*
	 * Pending -> 0, Approved -> 1,  rejected -> 2
	 * food received -> 1 (It becomes 1 ,after food being delivered.)
	 * email_status = 1 ----> email sent
	 */

	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,"
			+ "mrr.time as time,mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,mrr.approved_count as approved_count,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,DAYNAME(mrr.date) as dayname,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.time_for_frontend as time_for_frontend,mrr.voucher_head_new_id as voucher_head_new_id,mrr.gross_amount as gross_amount,mrr.rate_per_count as rate_per_count,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,mrr.user_id as user_id,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "mrr.dept_id as dept_id,mrr.school_id as school_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id "
			+ "where CONCAT(IfNull(mrr.refreshment_id,''),'',IfNull(mrr.meal_id,''),'',IfNull(mrr.date,''),"
			+ "'',IfNull(mrr.created_by,''),'',IfNull(mrr.created_date,'')) LIKE %?1% And mrr.user_id=?2 Group by mrr.refreshment_id")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer user_id); 
	
	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,mrr.approved_count as approved_count,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,DAYNAME(mrr.date) as dayname,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.time_for_frontend as time_for_frontend,mrr.voucher_head_new_id as voucher_head_new_id,mrr.gross_amount as gross_amount,mrr.rate_per_count as rate_per_count,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,mrr.user_id as user_id,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "mrr.dept_id as dept_id,mrr.school_id as school_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id "
			+ "where mrr.user_id=?1 Group by mrr.refreshment_id")
	public Page<Object> getAllSortedData(Pageable pageable, Integer user_id);
	
	@Modifying
	@Query(value = "update MealRefreshmentRequest ml set ml.active=false where ml.refreshment_id=?1")
	public void update(Integer refreshment_id);
	
	
	@Modifying
	@Query(value = "update MealRefreshmentRequest ml set ml.active=true where ml.refreshment_id=?1")
	public void update1(Integer refreshment_id);

	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,DAYNAME(mrr.date) as dayname,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,mrr.approved_count as approved_count,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "mrr.dept_id as dept_id,mrr.school_id as school_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id "
			+ "where CONCAT(IfNull(mrr.refreshment_id,''),'',IfNull(mrr.meal_id,''),'',IfNull(mrr.date,''),"
			+ "'',IfNull(mrr.created_by,''),'',IfNull(mrr.created_date,'')) LIKE %?1% And mrr.approved_status=1 And "
			+ "mt.for_end_user=true And SUBSTRING(mrr.date,4,2)=?2 And SUBSTRING(mrr.date,7,4)=?3 group by mrr.refreshment_id ")
	public Page<Object> getAllDataFilteredByKeyword1(Pageable pageable,Object keyword,String month,String year,  Integer approved_status); 
	
	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,DAYNAME(mrr.date) as dayname,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,mrr.approved_count as approved_count,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "mrr.dept_id as dept_id,mrr.school_id as school_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id "
			+ "where mrr.approved_status=1 And mt.for_end_user=true And SUBSTRING(mrr.date,4,2)=?1 And SUBSTRING(mrr.date,7,4)=?2 group by mrr.refreshment_id ")
	public Page<Object> getAllSortedData1(Pageable pageable, String month, String year, Integer approved_status);
	
	
	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,DAYNAME(mrr.date) as dayname,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,mrr.approved_count as approved_count,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "mrr.dept_id as dept_id,mrr.school_id as school_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id "
			+ "where CONCAT(IfNull(mrr.refreshment_id,''),'',IfNull(mrr.meal_id,''),'',IfNull(mrr.date,''),"
			+ "'',IfNull(mrr.created_by,''),'',IfNull(mrr.created_date,'')) LIKE %?1% And mrr.approved_status=2 And"
			+ " mt.for_end_user=true And SUBSTRING(mrr.date,4,2)=?2 And SUBSTRING(mrr.date,7,4)=?3 group by mrr.refreshment_id")
	public Page<Object> getAllDataFilteredByKeyword1pending(Pageable pageable ,Object keyword,String month,String year,  Integer approved_status); 
	
	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,DAYNAME(mrr.date) as dayname,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.approved_count as approved_count,"
			+ "mrr.receive_status as receive_status,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "mrr.dept_id as dept_id,mrr.school_id as school_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id "
			+ "where mrr.approved_status=2 And mt.for_end_user=true And SUBSTRING(mrr.date,4,2)=?1 And SUBSTRING(mrr.date,7,4)=?2 group by mrr.refreshment_id")
	public Page<Object> getAllSortedData1Pending(Pageable pageable,String month, String year,  Integer approved_status);

	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,DAYNAME(mrr.date) as dayname,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.approved_count as approved_count,"
			+ "mrr.receive_status as receive_status,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "mrr.dept_id as dept_id,mrr.school_id as school_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id "
			+ "where CONCAT(IfNull(mrr.refreshment_id,''),'',IfNull(mrr.meal_id,''),'',IfNull(mrr.date,''),"
			+ "'',IfNull(mrr.created_by,''),'',IfNull(mrr.created_date,'')) LIKE %?1% and mt.for_mess=true")
	public Page<Object> getAllDataFilteredByKeyword2(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,DAYNAME(mrr.date) as dayname,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,mrr.approved_count as approved_count,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,mrr.school_id as school_id,"
			+ "mrr.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id where mt.for_mess=true")
	public Page<Object> getAllSortedData2(Pageable pageable);
	
	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,DAYNAME(mrr.date) as dayname,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,mrr.approved_count as approved_count,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,mrr.school_id as school_id,"
			+ "mrr.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id where mt.for_end_user=true and mrr.approved_status=1 group by mrr.refreshment_id")
	public List<HashMap<String, Object>> fetchAllMealRefreshmentRequestDetailsForEmailIndex();

	@Query(value = "select DISTINCT(ml.created_by) from MealRefreshmentRequest ml where ml.date=?1 and ml.active=true And ml.approved_status=1")  
	public List<Integer> getApprovedDataByDateForSendingEmail1(String meal_date);

	@Query(value = "select mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,SUBSTRING(mrr.time,12,8) as time,"
			+ "mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,ed.email as email,ed.employee_name as employee_name,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mrr.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as username,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,ed1.mobile as mobile,"
			+ "ed1.email as approverEmail,ed1.employee_name as approver_name,"
			+ "ed1.designation_id as designation_id,d.designation_name as designation_name,dep1.dept_name as dept_name,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,mrr.time_for_frontend as time_for_frontend,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,mrr.approved_count as approved_count,mrr.approved_date as approved_date,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status,sc.school_name_short as school_name_short,dep.dept_name_short as dept_name_short "
			+ "from meal_refreshment_request mrr "
			+ "left join meal_type mt on mrr.meal_id=mt.meal_id "
			+ "left join vendor ve on ve.voucher_head_new_id=mrr.voucher_head_new_id "
			+ "left join user_details ua1 on mrr.approved_by=ua1.id "
			+ "left join user_details ua2 on mrr.cancel_by=ua2.id "
			+ "left join user_details ua3 on mrr.created_by=ua3.id "
			+ "left join employee_details ed on ua3.email=ed.email "
			+ "left join schools sc on sc.school_id=ed.school_id "
			+ "left join department dep on dep.dept_id=ed.dept_id "
			+ "left join employee_details ed1 on ua1.email=ed1.email "
			+ "left join designation d on d.designation_id=ed1.designation_id "
			+ "left join department dep1 on dep1.dept_id=ed1.dept_id "
			+ "where mrr.created_by=?1 and mrr.date=?2 and mrr.active=true And mrr.approved_status=1",nativeQuery=true)
	public List<Map<String, Object>> getApprovedDataByDateForSendingEmail(Integer d, String meal_date);
	
	@Query(value = "select DISTINCT(ml.voucher_head_new_id) from MealRefreshmentRequest ml where ml.date=?1 and ml.active=true And ml.approved_status=1")
	public List<Integer> getApprovedMealIdsByDateForSendingEmailToVendor(String meal_date);
	
	@Query(value = "select mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,SUBSTRING(mrr.time,12,8) as time,"
			+ "mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,ed.email as email,ed.employee_name as employee_name,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mrr.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,ve.vendor_email as vendor_email,mt.meal_type as meal_type,mt.for_end_user as for_end_user,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as username,ua2.username as cancel_user_name,ed1.mobile as mobile,"
			+ "ed1.email as approverEmail,ed1.employee_name as approver_name,"
			+ "ed1.designation_id as designation_id,d.designation_name as designation_name,dep1.dept_name as dept_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,mrr.voucher_head_new_id as voucher_head_new_id,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,mrr.time_for_frontend as time_for_frontend,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,mrr.approved_count as approved_count,mrr.approved_date as approved_date,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status,sc.school_name_short as school_name_short,dep.dept_name_short as dept_name_short "
			+ "from meal_refreshment_request mrr "
			+ "left join meal_type mt on mrr.meal_id=mt.meal_id "
			+ "left join vendor ve on ve.voucher_head_new_id=mrr.voucher_head_new_id "
			+ "left join user_details ua1 on mrr.approved_by=ua1.id "
			+ "left join user_details ua2 on mrr.cancel_by=ua2.id "
			+ "left join user_details ua3 on mrr.created_by=ua3.id "
			+ "left join employee_details ed on ua3.email=ed.email "
			+ "left join employee_details ed1 on ua1.email=ed1.email "
			+ "left join designation d on d.designation_id=ed1.designation_id "
			+ "left join department dep1 on dep1.dept_id=ed1.dept_id "
			+ "left join schools sc on sc.school_id=ed.school_id "
			+ "left join department dep on dep.dept_id=ed.dept_id "
			+ "where mrr.voucher_head_new_id=?1 and mrr.date=?2 and mrr.active=true And mrr.approved_status=1",nativeQuery=true)
	public List<Map<String, Object>> getApprovedDataByDateForSendingEmailToVendor(Integer voucher_head_new_id, String meal_date);

	
	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.date as date,"
			+ "mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,mt.meal_type as meal_type,"
			+ "mt.menu_contents as menu_contents) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id where mrr.refreshment_id=?1")
	public HashMap<String, Object> getMealRefreshmentRequestByRefreshmentId(Integer refreshment_id);
	
//	@Query(value = "select mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
//			+ "mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,"
//			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,"
//			+ "mt.meal_type as meal_type,mt.for_end_user as for_end_user,"
//			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,"
//			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
//			+ "mrr.created_username as created_username,mrr.modified_username as modified_username "
//			+ "from meal_refreshment_request mrr "
//			+ "left join meal_type mt on mrr.meal_id=mt.meal_id "
//			+ "where month(STR_TO_DATE(mrr.date, '%d-%m-%Y'))=month(STR_TO_DATE(:date1, '%d-%m-%Y')) and mt.for_mess=true and mrr.active=true",nativeQuery = true)
//	public List<Map<String, Object>> messDataForCalendarView(String date1);
//	where month(STR_TO_DATE(date, '%d-%m-%Y'))=month(STR_TO_DATE('13-01-2024', '%d-%m-%Y')) and mt.for_mess=true and mrr.active=true
	
	@Query(value = "select mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,"
			+ "mt.meal_type as meal_type,mt.for_end_user as for_end_user,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username "
			+ "from meal_refreshment_request mrr "
			+ "left join meal_type mt on mrr.meal_id=mt.meal_id "
			+ "where month(mrr.date)=month(:date1) and mt.for_mess=true and mrr.active=true",nativeQuery = true)
	public List<Map<String, Object>> messDataForCalendarView(String date1);
	
	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mrr.remarks as mealRefreshmentRemarks,mrr.delivery_address as delivery_address,mrr.approved_by as approved_by,"
			+ "mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks) from MealRefreshmentRequest mrr "
			+ " where mrr.refreshment_id=?1")
	public HashMap<String, Object> getMealRefreshmentRequestById(Integer refreshment_id);	
	
	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,DAYNAME(mrr.date) as dayname,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,mrr.approved_count as approved_count,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,mrr.school_id as school_id,"
			+ "mrr.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id "
			+ "where mt.for_end_user=true and mrr.approved_status=1 and mrr.date=?1 And mrr.email_status Is Null group by mrr.refreshment_id")
	public List<HashMap<String, Object>> getFilteredEndUserData(String date);

	@Query(value = "select ml.rate_per_count from MealVendorAssignment ml where ml.meal_id=?1 and ml.voucher_head_new_id=?2 and ml.active=true")
	public Integer getMealRefreshmentRequestById(Integer meal_id, Integer voucher_head_new_id);
	
	
	
	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,DAYNAME(mrr.date) as dayname,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,mrr.approved_count as approved_count,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,mrr.school_id as school_id,"
			+ "mrr.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id "
			+ "where CONCAT(IfNull(mrr.refreshment_id,''),'',IfNull(mrr.meal_id,''),'',IfNull(mrr.date,''),"
			+ "'',IfNull(mrr.created_by,''),'',IfNull(mrr.created_date,'')) LIKE %?1% "
			+ "and mt.for_end_user=true And mrr.approved_status=1 or mrr.approved_status=2 group by mrr.refreshment_id ")
	public List<HashMap<String, Object>> fetchAllMealRefreshmentRequestDetailsForEmailIndexReport();

	
	
	@Query(value = "Select new map(v.vendor_id as id,v.vendor_name as vendor_name,v.voucher_head_new_id as voucher_head_new_id) from MealVendorAssignment mva "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor v on vhn.voucher_head_new_id=v.voucher_head_new_id "
			+ "where v.active=true And mva.meal_id=?1")
	public List<HashMap<String, Object>> getVendorData(Integer meal_id);
	
//	@Query(value = "Select new map(v.vendor_id as id,v.vendor_name as vendor_name) from Vendor v "
//			+ "left join MealVendorAssignment mva on mva.vendor_id=v.vendor_id "
//			+ "where v.voucher_head_new_id IN "
//			+ "(select vhn.voucher_head_new_id from VoucherHeadNew vhn where vhn.is_vendor=true And vhn.active=true) "
//			+ "And v.active=true And mva.meal_id=?1")

	@Modifying
	@Query(value = "update MealRefreshmentRequest ml set ml.email_status=true where ml.active=true And ml.date=?1 And ml.approved_status=1")
	public void updateMailStatus(String approved_date);

	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,DAYNAME(mrr.date) as dayname,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,mrr.approved_count as approved_count,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,mrr.school_id as school_id,"
			+ "mrr.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id "
			+ "where CONCAT(IfNull(mrr.refreshment_id,''),'',IfNull(mrr.meal_id,''),'',IfNull(mrr.date,''),"
			+ "'',IfNull(mrr.created_by,''),'',IfNull(mrr.created_date,'')) LIKE %?1% "
			+ "and mt.for_end_user=true And mrr.approved_status=1 or mrr.approved_status=2 group by mrr.refreshment_id ")
	public Page<Object> getAllDataFilteredByKeyword3(Pageable pageable, Object keyword);

	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,DAYNAME(mrr.date) as dayname,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,mrr.approved_count as approved_count,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,mrr.school_id as school_id,"
			+ "mrr.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id "
			+ "where mt.for_end_user=true And (mrr.approved_status=1 or mrr.approved_status=2) group by mrr.refreshment_id ")
	public Page<Object> getAllSortedData3(Pageable pageable);

	
	@Query(value = "select count(*) from MealRefreshmentRequest ml where ml.active=true And ml.meal_id=?1 And ml.date=?2 And ml.user_id=?3 And ml.count=?4")
	public Integer getCountOfMealTypeAndDate(Integer meal_id, String date, Integer user_id, Integer count);	
	
	
	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,DAYNAME(mrr.date) as dayname,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,mrr.approved_count as approved_count,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,mrr.school_id as school_id,"
			+ "mrr.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id "
			+ "where CONCAT(IfNull(mrr.refreshment_id,''),'',IfNull(mrr.meal_id,''),'',IfNull(mrr.date,''),"
			+ "'',IfNull(mrr.created_by,''),'',IfNull(mrr.created_date,'')) LIKE %?1% "
			+ "and mt.for_end_user=true And mrr.email_status Is Null And mrr.cancel_by Is Null group by mrr.refreshment_id")
	public Page<Object> getAllDataFilteredByKeyword11(Pageable pageable,Object keyword); 
	
	@Query(value = "select new map(mrr.refreshment_id as id,mrr.meal_id as meal_id,mrr.count as count,mrr.date as date,mrr.time as time,"
			+ "mrr.created_by as created_by,mrr.modified_by as modified_by,mrr.remarks as remarks,mva.rate_per_count as rate_per_count,"
			+ "ve.vendor_name as vendor_name,mt.meal_type as meal_type,mt.for_end_user as for_end_user,DAYNAME(mrr.date) as dayname,"
			+ "mt.menu_contents as menu_contents,mt.remarks as meal_remarks,ua1.username as approver_name,ua2.username as cancel_user_name,"
			+ "mrr.created_date as created_date,mrr.modified_date as modified_date,mrr.active as active,"
			+ "mrr.created_username as created_username,mrr.modified_username as modified_username,"
			+ "mrr.end_user_feedback_remarks as end_user_feedback_remarks,mrr.cancel_date as cancel_date,mrr.receive_date as receive_date,"
			+ "mrr.receive_status as receive_status,mrr.approved_count as approved_count,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,mrr.school_id as school_id,"
			+ "mrr.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "mrr.approved_by as approved_by,mrr.approved_status as approved_status,mrr.approver_remarks as approver_remarks,mrr.approved_date as approved_date,"
			+ "mrr.approved_time as approved_time,mrr.cancel_by as cancel_by,mrr.cancel_remarks as cancel_remarks,mrr.delivery_address as delivery_address,"
			+ "mrr.email_status as email_status) "
			+ "from MealRefreshmentRequest mrr "
			+ "left join MealType mt on mrr.meal_id=mt.meal_id "
			+ "left join Department dept on mrr.dept_id=dept.dept_id "
			+ "left join Schools sc on mrr.school_id=sc.school_id "
			+ "left join MealVendorAssignment mva on mrr.meal_id=mva.meal_id "
			+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "left join UserAuthentication ua1 on mrr.approved_by=ua1.id "
			+ "left join UserAuthentication ua2 on mrr.cancel_by=ua2.id "
			+ "where mt.for_end_user=true And mrr.email_status Is Null And mrr.cancel_by Is Null group by mrr.refreshment_id")
	public Page<Object> getAllSortedData11(Pageable pageable);

	@Query(value = "Select new map(edh.dept_id as dept_id,edh.school_id as school_id ) from UserAuthentication ua "
			+ "left join EmployeeDetails edh on edh.email=ua.email "
			+ "where ua.active=true And ua.id=?1")
	public Map<String, Object> getDeptAndSchool_ids(Integer created_by);

	@Query(value="select new  com.au.dto.MealRefreshmentDTO( mr.refreshment_id ,s.school_name, d.dept_name, ua.username"
			+ ", mr.end_user_feedback_remarks, ut.username, mr.approved_status, mr.date, mt.meal_type, mr.count, mr.rate_per_count,"
			+ " mr.gross_amount,mr.remarks, mr.approved_count,s.school_name_short, mr.dept_id ,mr.school_id ) from MealRefreshmentRequest mr "
			+ " left join UserAuthentication ua on ua.id=mr.user_id "
			+ " left join UserAuthentication ut on ut.id=mr.approved_by "
			+ " left join Department d on d.dept_id=mr.dept_id "
			+ " left join Schools s on s.school_id=mr.school_id "
			+ " left join MealType mt on mt.meal_id=mr.meal_id"
			+ " left join Vendor v on v.voucher_head_new_id=mr.voucher_head_new_id "
			+ " where mr.voucher_head_new_id=:voucher_head_new_id "
			+ " and FUNCTION('YEAR', STR_TO_DATE(mr.date, '%d-%m-%Y'))=:year "
			+ " and FUNCTION('MONTH', STR_TO_DATE(mr.date, '%d-%m-%Y'))=:month "
			+ " and mr.approved_status=1 And mr.cancel_by is null")
	public List<MealRefreshmentDTO> getMealRefreshmentRequests(Integer voucher_head_new_id, Integer month, Integer year);
	

}
