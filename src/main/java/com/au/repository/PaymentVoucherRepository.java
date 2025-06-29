package com.au.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.DraftPaymentVoucher;
import com.au.model.PaymentVoucher;


@Repository
@Transactional
public interface PaymentVoucherRepository extends JpaRepository<PaymentVoucher, Integer>{
	
	
	@Query(value="SELECT pv from payment_voucher pv where pv.voucher_no=:paymentVoucherNo", nativeQuery = true)
	public List<PaymentVoucher> findByVoucher_no(@Param("paymentVoucherNo") Integer paymentVoucherNo);
	
	@Query(value = "SELECT pv from PaymentVoucher pv where pv.active=true")
	public List<PaymentVoucher> findAll11();
	

	@Modifying
	@Query(value = "update PaymentVoucher pv set pv.active=false where pv.payment_voucher_id=?1")
	public void updateExamDetail(Integer id);
	
	@Modifying
	@Query(value = "update PaymentVoucher pv set pv.active=true where pv.payment_voucher_id=?1")
	public void updateExamDetail1(Integer id);
	

	@Query(value = "select new map(pv.payment_voucher_id as id,pv.school_id as school_id,pv.date as date,pv.type AS type,"
			+ "pv.created_username as created_username,pv.modified_username as modified_username,pv.created_name As created_name,"
			+ "pv.voucher_head_id as voucher_head_id,pv.jv_joucher_number As jv_joucher_number,pv.jv_school_id As jv_school_id,"
			+ "pv.jv_financial_year_id As jv_financial_year_id,pv.cancelled_remarks As cancelled_remarks,"
			+ "pv.inter_school_id As inter_school_id,pv.inter_bank_id As inter_bank_id,"
			+ "sc1.school_name as interschool_name,sc1.school_name_short as interschool_name_short,"
			+ "ba1.bank_name As Inter_bank_name,ba1.bank_short_name As inter_bank_short_name,"
			+ "pv.created_date as created_date,pv.modified_date as modified_date,pv.created_by as created_by,pvd.verified_date As verified_date,"
			+ "pv.bank_id as bank_id,pv.paying_now as paying_now,pv.cheque_dd_no as cheque_dd_no,pv.remarks as remarks,"
			+ "pv.pay_to as pay_to,pv.expensense_lead as expensense_lead,pv.purchase_order_id as purchase_order_id,"
			+ "pv.financial_year_id as financial_year_id,pv.journal_voucher_id as journal_voucher_id,pv.voucher_no as voucher_no,"
			+ "pv.voucher_head_new_id as voucher_head_new_id,pv.fee_head_id as fee_head_id,pv.credit_total as credit_total,"
			+ "pv.debit_total as debit_total,pv.credit as credit,pv.debit as debit,pv.nature_id as nature_id,pv.note as note,"
			+ "pv.payment_status as payment_status,pv.vendor_id as vendor_id,pv.dept_id as dept_id,pv.status as status,"
			+ "pv.inter_institute_id as inter_institute_id,pv.vendor_active as vendor_active,pv.clearingDate as clearingDate,"
			+ "pv.lockstatus as lockstatus,pv.cancel_voucher as cancel_voucher,pv.voucher_remarks as voucher_remarks,"
			+ "pv.cancelled_by as cancelled_by,pv.cancelled_date as cancelled_date,pv.online as online,pv.env_bill_details_id As env_bill_details_id,"
			+ "pv.payment_voucher_id as payment_voucher_id,pv.advanced_po_status as advanced_po_status,pv.actual_date as actual_date,"
			+ "pv.approver_id as approver_id,pv.approved_status as approved_status,pv.approved_date as approved_date,"
			+ "pv.attachment_name as attachment_name,pv.attachment_path as attachment_path,ba.bank_short_name as bank_short_name,"
			+ "pvd.approver_id as draft_approver_id,pvd.approved_status as draft_approved_status,pvd.approved_date as draft_approved_date,"
			+ "pvd.draft_payment_voucher_id as draft_payment_voucher_id,pvd.modified_by as draft_modified_by,"
			+ "pvd.created_username as draft_created_username,pvd.modified_username as draft_modified_username,"
			+ "pvd.created_date as draft_created_date,pvd.modified_date as draft_modified_date,pvd.created_by as draft_created_by,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,vhn.voucher_head as voucher_head,fh.fee_head as fee_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,v.vendor_name as vendor_name,"
			+ "ua.username as username,ua.usercode as usercode,ua.id as user_id,"
			+ "ebd.env_bill_details_id AS env_bill_details_id,ebd.attachment_path As envAttachment_path,"
			+ "grn.grnId AS grnId,grn.attachmentPath As grnAttachment_path,"
			+ "pv.modified_by as modified_by,pv.active as active) from PaymentVoucher pv "
			+ "left join Schools sc on sc.school_id = pv.school_id "
			+ "left join Bank ba on ba.bank_id = pv.bank_id "
			+ "left join Schools sc1 on sc1.school_id = pv.inter_school_id "
			+ "left join Bank ba1 on ba1.bank_id = pv.inter_bank_id "
			+ "left join DraftPaymentVoucher pvd on pvd.payment_voucher_id = pv.payment_voucher_id "
			+ "left join UserAuthentication ua on ua.id = pvd.approver_id "
			+ "left join Department de on de.dept_id = pv.dept_id "
			+ "left join VoucherHead vh on vh.voucher_head_id = pv.voucher_head_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = vh.voucher_head_new_id "
			+ "left join Vendor v on v.vendor_id = pv.vendor_id "
			+ "left join FeeHead fh on fh.fee_head_id = pv.fee_head_id "
			+ "left join EnvBillDetails ebd on ebd.payment_voucher_id = pv.payment_voucher_id "
			+ "left join GRN grn on grn.paymentVoucherId = pv.payment_voucher_id "
			+ "where (DATE(pv.created_date) >= :minDate) "
	 	    + "AND (:start IS NULL OR DATE(pv.created_date) >= :start) "
		        + "AND (:end IS NULL OR DATE(pv.created_date) <= :end) And "
		    + "CONCAT(IfNull(pv.created_username,''),'',IfNull(pv.cheque_dd_no,''),'',IfNull(pv.voucher_remarks,''),'',"
			+ "'',IfNull(pv.created_by,''),'',IfNull(pv.created_date,'')) LIKE %:keyword% "
			+ "group by pv.voucher_no,pv.financial_year_id,pv.school_id")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable,
            @Param("keyword") Object keyword,
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("minDate") Date minDate);
	
	@Query(value = "select new map(pv.payment_voucher_id as id,pv.school_id as school_id,pv.date as date,pv.type AS type,"
			+ "pv.created_username as created_username,pv.modified_username as modified_username,pv.created_name As created_name,"
			+ "pv.voucher_head_id as voucher_head_id,pv.jv_joucher_number As jv_joucher_number,pv.jv_school_id As jv_school_id,"
			+ "pv.jv_financial_year_id As jv_financial_year_id,pv.cancelled_remarks As cancelled_remarks,"
			+ "pv.inter_school_id As inter_school_id,pv.inter_bank_id As inter_bank_id,"
			+ "sc1.school_name as interschool_name,sc1.school_name_short as interschool_name_short,"
			+ "ba1.bank_name As Inter_bank_name,ba1.bank_short_name As inter_bank_short_name,"
			+ "pv.created_date as created_date,pv.modified_date as modified_date,pv.created_by as created_by,pvd.verified_date As verified_date,"
			+ "pv.bank_id as bank_id,pv.paying_now as paying_now,pv.cheque_dd_no as cheque_dd_no,pv.remarks as remarks,"
			+ "pv.pay_to as pay_to,pv.expensense_lead as expensense_lead,pv.purchase_order_id as purchase_order_id,"
			+ "pv.financial_year_id as financial_year_id,pv.journal_voucher_id as journal_voucher_id,pv.voucher_no as voucher_no,"
			+ "pv.voucher_head_new_id as voucher_head_new_id,pv.fee_head_id as fee_head_id,pv.credit_total as credit_total,"
			+ "pv.debit_total as debit_total,pv.credit as credit,pv.debit as debit,pv.nature_id as nature_id,pv.note as note,"
			+ "pv.payment_status as payment_status,pv.vendor_id as vendor_id,pv.dept_id as dept_id,pv.status as status,"
			+ "pv.inter_institute_id as inter_institute_id,pv.vendor_active as vendor_active,pv.clearingDate as clearingDate,"
			+ "pv.lockstatus as lockstatus,pv.cancel_voucher as cancel_voucher,pv.voucher_remarks as voucher_remarks,"
			+ "pv.cancelled_by as cancelled_by,pv.cancelled_date as cancelled_date,pv.online as online,pv.env_bill_details_id As env_bill_details_id,"
			+ "pv.payment_voucher_id as payment_voucher_id,pv.advanced_po_status as advanced_po_status,pv.actual_date as actual_date,"
			+ "pv.approver_id as approver_id,pv.approved_status as approved_status,pv.approved_date as approved_date,"
			+ "pv.attachment_name as attachment_name,pv.attachment_path as attachment_path,ba.bank_short_name as bank_short_name,"
			+ "pvd.approver_id as draft_approver_id,pvd.approved_status as draft_approved_status,pvd.approved_date as draft_approved_date,"
			+ "pvd.draft_payment_voucher_id as draft_payment_voucher_id,pvd.modified_by as draft_modified_by,"
			+ "pvd.created_username as draft_created_username,pvd.modified_username as draft_modified_username,"
			+ "pvd.created_date as draft_created_date,pvd.modified_date as draft_modified_date,pvd.created_by as draft_created_by,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,vhn.voucher_head as voucher_head,fh.fee_head as fee_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,v.vendor_name as vendor_name,"
			+ "ua.username as username,ua.usercode as usercode,ua.id as user_id,"
			+ "ebd.env_bill_details_id AS env_bill_details_id,ebd.attachment_path As envAttachment_path,"
			+ "grn.grnId AS grnId,grn.attachmentPath As grnAttachment_path,"
			+ "pv.modified_by as modified_by,pv.active as active) from PaymentVoucher pv "
			+ "left join Schools sc on sc.school_id = pv.school_id "
			+ "left join Bank ba on ba.bank_id = pv.bank_id "
			+ "left join Schools sc1 on sc1.school_id = pv.inter_school_id "
			+ "left join Bank ba1 on ba1.bank_id = pv.inter_bank_id "
			+ "left join DraftPaymentVoucher pvd on pvd.payment_voucher_id = pv.payment_voucher_id "
			+ "left join UserAuthentication ua on ua.id = pvd.approver_id "
			+ "left join Department de on de.dept_id = pv.dept_id "
			+ "left join VoucherHead vh on vh.voucher_head_id = pv.voucher_head_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = vh.voucher_head_new_id "
			+ "left join Vendor v on v.vendor_id = pv.vendor_id "
			+ "left join FeeHead fh on fh.fee_head_id = pv.fee_head_id "
			+ "left join EnvBillDetails ebd on ebd.payment_voucher_id = pv.payment_voucher_id "
			+ "left join GRN grn on grn.paymentVoucherId = pv.payment_voucher_id "
			+ "where (DATE(pv.created_date) >= :minDate) "
	 	    + "AND (:start IS NULL OR DATE(pv.created_date) >= :start) "
		        + "AND (:end IS NULL OR DATE(pv.created_date) <= :end) "
			+ "group by pv.voucher_no,pv.financial_year_id,pv.school_id")
	public Page<Object> getAllSortedData(Pageable pageable,
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("minDate") Date minDate);
	
	@Query("SELECT MIN(pv.created_date) FROM PaymentVoucher pv")
	Optional<Date> findMinCreatedDate();
	
	
	@Query(value = "SELECT pv.voucher_no from payment_voucher pv "
			+ " where pv.active=true And pv.financial_year_id=?1 And pv.school_id=?2 "
			+ "ORDER BY pv.payment_voucher_id DESC LIMIT 1",nativeQuery=true)
	public Integer getLatestData(Integer financial_year_id, Integer school_id);
	
	@Query(value = "select * from payment_voucher "
			+ " where active=true And financial_year_id=?1 And school_id=?2 "
			+ "ORDER BY payment_voucher_id Desc LIMIT 1",nativeQuery = true)
	public PaymentVoucher getLatestDraftPaymentVoucher(Integer financial_year_id, Integer school_id);
	
	@Query(value = "select new map(pv.payment_voucher_id as id,pv.school_id as school_id,pv.date as date,"
			+ "pv.created_username as created_username,pv.modified_username as modified_username,pv.created_name As created_name,"
			+ "pv.voucher_head_id as voucher_head_id,pv.jv_joucher_number As jv_joucher_number,pv.jv_school_id As jv_school_id,"
			+ "pv.jv_financial_year_id As jv_financial_year_id,pv.cancelled_remarks As cancelled_remarks,"
			+ "pv.inter_school_id As inter_school_id,pv.inter_bank_id As inter_bank_id,"
			+ "sc1.school_name as interschool_name,sc1.school_name_short as interschool_name_short,"
			+ "ba1.bank_name As Inter_bank_name,ba1.bank_short_name As inter_bank_short_name,"
			+ "pv.created_date as created_date,pv.modified_date as modified_date,pv.created_by as created_by,pvd.verified_date As verified_date,"
			+ "pv.bank_id as bank_id,pv.paying_now as paying_now,pv.cheque_dd_no as cheque_dd_no,pv.remarks as remarks,"
			+ "pv.pay_to as pay_to,pv.expensense_lead as expensense_lead,pv.purchase_order_id as purchase_order_id,"
			+ "pv.financial_year_id as financial_year_id,pv.journal_voucher_id as journal_voucher_id,pv.voucher_no as voucher_no,"
			+ "pv.voucher_head_new_id as voucher_head_new_id,pv.fee_head_id as fee_head_id,pv.credit_total as credit_total,"
			+ "pv.debit_total as debit_total,pv.credit as credit,pv.debit as debit,pv.nature_id as nature_id,pv.note as note,"
			+ "pv.payment_status as payment_status,pv.vendor_id as vendor_id,pv.dept_id as dept_id,pv.status as status,"
			+ "pv.inter_institute_id as inter_institute_id,pv.vendor_active as vendor_active,pv.clearingDate as clearingDate,"
			+ "pv.lockstatus as lockstatus,pv.cancel_voucher as cancel_voucher,pv.voucher_remarks as voucher_remarks,"
			+ "pv.cancelled_by as cancelled_by,pv.cancelled_date as cancelled_date,pv.online as online,"
			+ "pv.payment_voucher_id as payment_voucher_id,pv.advanced_po_status as advanced_po_status,pv.actual_date as actual_date,"
			+ "pv.approver_id as approver_id,pv.approved_status as approved_status,pv.approved_date as approved_date,"
			+ "pv.attachment_name as attachment_name,pv.attachment_path as attachment_path,pv.env_bill_details_id As env_bill_details_id,"
			+ "pvd.approver_id as draft_approver_id,pvd.approved_status as draft_approved_status,pvd.approved_date as draft_approved_date,"
			+ "pvd.draft_payment_voucher_id as draft_payment_voucher_id,pvd.modified_by as draft_modified_by,"
			+ "pvd.created_username as draft_created_username,pvd.modified_username as draft_modified_username,"
			+ "pvd.created_date as draft_created_date,pvd.modified_date as draft_modified_date,pvd.created_by as draft_created_by,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,pv.type AS type,"
			+ "ba.bank_name As bank_name,ba.bank_short_name As bank_short_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,vhn.voucher_head as voucher_head,fh.fee_head as fee_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,v.vendor_gst_no As vendor_gst_no,"
			+ "v.vendor_name as vendor_name,v.vendor_email as vendor_email,fy.financial_year as financial_year,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "v.account_no As vendoe_account_no,v.nature_of_business As nature_of_business,v.pan_number As vendor_pan_number,"
			+ "v.vendor_bank_account_holder_name As vendor_bank_account_holder_name,v.vendor_bank_ifsc_code As vendor_bank_ifsc_code,"	
			+ "ed1.employee_name as approverName,ed1.empcode as appeoverCode,ed1.emp_id as approverId,"
			+ "ed.employee_name as verifyName,ed.empcode as verifyCode,ed.emp_id as verifyId,"
			+ "fy.financial_year_id as fy_financial_year_id,fy.financial_year as financial_year,fy.year as year,"
			+ "pv.modified_by as modified_by,pv.active as active) from PaymentVoucher pv "
			+ "left join Schools sc on sc.school_id = pv.school_id "
			+ "left join Schools sc1 on sc1.school_id = pv.inter_school_id "
			+ "left join Bank ba1 on ba1.bank_id = pv.inter_bank_id "
			+ "left join Bank ba on ba.bank_id = pv.bank_id "
			+ "left join DraftPaymentVoucher pvd on pvd.draft_payment_voucher_id = pv.draft_payment_voucher_id "
			+ "left join UserAuthentication ua on ua.id = pvd.approver_id "
			+ "left join UserAuthentication ua1 on ua1.id = pvd.verifier_id "
			+ "left join EmployeeDetails ed on ua1.email = ed.email "
			+ "left join EmployeeDetails ed1 on ua.email = ed1.email "
			+ "left join Department de on de.dept_id = pv.dept_id "
			+ "left join FinancialYear fy on fy.financial_year_id = pv.financial_year_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = pv.voucher_head_id "
			+ "left join Vendor v on v.voucher_head_new_id = pv.voucher_head_id "
			+ "left join FeeHead fh on fh.fee_head_id = pv.fee_head_id where pv.voucher_no=?1 And pv.school_id=?2 And "
			+ "pv.financial_year_id=?3 And pv.active=true")
	public List<Map<String, Object>> getPaymentVoucherData(Integer voucher_no,Integer school_id,Integer financial_year_id);	
	
	
	@Query(value = "select distinct b.bank_name from payment_voucher pv "
			+ "left join bank b on b.bank_id =pv.bank_id "
			+ " where pv.active=true",nativeQuery=true)
	public List<String> getAllDistinctBankForOutflow();
	
	
	@Query(value = "select new map(pv.payment_voucher_id as id,pv.school_id as school_id,pv.date as date,pv.type AS type,"
			+ "pv.created_username as created_username,pv.modified_username as modified_username,pv.created_name As created_name,"
			+ "pv.voucher_head_id as voucher_head_id,pv.jv_joucher_number As jv_joucher_number,pv.jv_school_id As jv_school_id,"
			+ "pv.jv_financial_year_id As jv_financial_year_id,pv.cancelled_remarks As cancelled_remarks,"
			+ "pv.inter_school_id As inter_school_id,pv.inter_bank_id As inter_bank_id,"
			+ "sc1.school_name as interschool_name,sc1.school_name_short as interschool_name_short,"
			+ "ba1.bank_name As Inter_bank_name,ba1.bank_short_name As inter_bank_short_name,"
			+ "pv.created_date as created_date,pv.modified_date as modified_date,pv.created_by as created_by,pvd.verified_date As verified_date,"
			+ "pv.bank_id as bank_id,pv.paying_now as paying_now,pv.cheque_dd_no as cheque_dd_no,pv.remarks as remarks,"
			+ "pv.pay_to as pay_to,pv.expensense_lead as expensense_lead,pv.purchase_order_id as purchase_order_id,"
			+ "pv.financial_year_id as financial_year_id,pv.journal_voucher_id as journal_voucher_id,pv.voucher_no as voucher_no,"
			+ "pv.voucher_head_new_id as voucher_head_new_id,pv.fee_head_id as fee_head_id,pv.credit_total as credit_total,"
			+ "pv.debit_total as debit_total,pv.credit as credit,pv.debit as debit,pv.nature_id as nature_id,pv.note as note,"
			+ "pv.payment_status as payment_status,pv.vendor_id as vendor_id,pv.dept_id as dept_id,pv.status as status,"
			+ "pv.inter_institute_id as inter_institute_id,pv.vendor_active as vendor_active,pv.env_bill_details_id As env_bill_details_id,"
			+ "pv.lockstatus as lockstatus,pv.cancel_voucher as cancel_voucher,pv.voucher_remarks as voucher_remarks,"
			+ "pv.cancelled_by as cancelled_by,pv.cancelled_date as cancelled_date,pv.online as online,"
			+ "pv.payment_voucher_id as payment_voucher_id,pv.advanced_po_status as advanced_po_status,pv.actual_date as actual_date,"
			+ "pv.approver_id as approver_id,pv.approved_status as approved_status,pv.approved_date as approved_date,"
			+ "pv.attachment_name as attachment_name,pv.attachment_path as attachment_path,ba.bank_short_name as bank_short_name,"
			+ "pvd.approver_id as draft_approver_id,pvd.approved_status as draft_approved_status,pvd.approved_date as draft_approved_date,"
			+ "pvd.draft_payment_voucher_id as draft_payment_voucher_id,pvd.modified_by as draft_modified_by,"
			+ "pvd.created_username as draft_created_username,pvd.modified_username as draft_modified_username,"
			+ "pvd.created_date as draft_created_date,pvd.modified_date as draft_modified_date,pvd.created_by as draft_created_by,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,vhn.voucher_head as voucher_head,fh.fee_head as fee_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,v.vendor_name as vendor_name,"
			+ "ua.username as username,ua.usercode as usercode,ua.id as user_id,"
			+ "pv.modified_by as modified_by,pv.active as active) from PaymentVoucher pv "
			+ "left join Schools sc on sc.school_id = pv.school_id "
			+ "left join Bank ba on ba.bank_id = pv.bank_id "
			+ "left join Schools sc1 on sc1.school_id = pv.inter_school_id "
			+ "left join Bank ba1 on ba1.bank_id = pv.inter_bank_id "
			+ "left join DraftPaymentVoucher pvd on pvd.draft_payment_voucher_id = pv.draft_payment_voucher_id "
			+ "left join UserAuthentication ua on ua.id = pvd.approver_id "
			+ "left join Department de on de.dept_id = pv.dept_id "
			+ "left join VoucherHead vh on vh.voucher_head_id = pv.voucher_head_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = vh.voucher_head_new_id "
			+ "left join Vendor v on v.vendor_id = pv.vendor_id "
			+ "left join FeeHead fh on fh.fee_head_id = pv.fee_head_id where pv.bank_id=?1 And pv.financial_year_id=?2 And "
			+ "date(pv.date)=?3 And pv.active=true")
	public List<HashMap<String, Object>> getTotalDebitOfDay(Integer bank_id, Integer financial_year_id, Date fromdate);

//	@Query(value = "SELECT sum(debit) FROM payment_voucher where voucher_head_id=?1 and financial_year_id=?2 and active=true",nativeQuery=true)
//	public Double getSumOfDebitFromPaymentVoucher(Integer voucher_head_id, Integer financial_year_id);
//
	@Query(value = "select (SELECT round(COALESCE(SUM(debit), 0),2) FROM payment_voucher where voucher_head_id=?1 and financial_year_id=?2 and school_id=?3 "
			+ "and active=true) + "
			+ "(SELECT round(COALESCE(SUM(debit), 0),2) FROM journal_voucher where voucher_head_id=?1 and financial_year_id=?2 and school_id=?3 "
			+ "and active=true)",nativeQuery=true)
	public Double getSumOfDebitFromAllTables(Integer voucher_head_id, Integer financial_year_id, Integer school_id);

	@Query(value = "select pv.payment_voucher_id as id,pv.school_id as school_id,pv.date as date,pv.type AS type,"
			+ "pv.created_username as created_username,pv.modified_username as modified_username,pv.created_name As created_name,"
			+ "pv.voucher_head_id as voucher_head_id,pv.jv_joucher_number As jv_joucher_number,pv.jv_school_id As jv_school_id,"
			+ "pv.jv_financial_year_id As jv_financial_year_id,pv.cancelled_remarks As cancelled_remarks,"
			+ "pv.inter_school_id As inter_school_id,pv.inter_bank_id As inter_bank_id,"
			+ "sc1.school_name as interschool_name,sc1.school_name_short as interschool_name_short,"
			+ "ba1.bank_name As Inter_bank_name,ba1.bank_short_name As inter_bank_short_name,"
			+ "pv.created_date as created_date,pv.modified_date as modified_date,pv.created_by as created_by,"
			+ "pv.bank_id as bank_id,pv.paying_now as paying_now,pv.cheque_dd_no as cheque_dd_no,pv.remarks as remarks,"
			+ "pv.pay_to as pay_to,pv.expensense_lead as expensense_lead,pv.purchase_order_id as purchase_order_id,"
			+ "pv.financial_year_id as financial_year_id,pv.journal_voucher_id as journal_voucher_id,pv.voucher_no as voucher_no,"
			+ "pv.voucher_head_new_id as voucher_head_new_id,pv.fee_head_id as fee_head_id,pv.credit_total as credit_total,"
			+ "pv.debit_total as debit_total,pv.credit as credit,pv.debit as debit,pv.nature_id as nature_id,pv.note as note,"
			+ "pv.payment_status as payment_status,pv.vendor_id as vendor_id,pv.dept_id as dept_id,pv.status as status,"
			+ "pv.inter_institute_id as inter_institute_id,pv.vendor_active as vendor_active,pvd.verified_date As verified_date,"
			+ "pv.lockstatus as lockstatus,pv.cancel_voucher as cancel_voucher,pv.voucher_remarks as voucher_remarks,"
			+ "pv.cancelled_by as cancelled_by,pv.cancelled_date as cancelled_date,pv.online as online,pv.env_bill_details_id As env_bill_details_id,"
			+ "pv.payment_voucher_id as payment_voucher_id,pv.advanced_po_status as advanced_po_status,pv.actual_date as actual_date,"
			+ "pv.approver_id as approver_id,pv.approved_status as approved_status,pv.approved_date as approved_date,"
			+ "pv.attachment_name as attachment_name,pv.attachment_path as attachment_path,ba.bank_short_name as bank_short_name,"
			+ "pvd.approver_id as draft_approver_id,pvd.approved_status as draft_approved_status,pvd.approved_date as draft_approved_date,"
			+ "pvd.draft_payment_voucher_id as draft_payment_voucher_id,pvd.modified_by as draft_modified_by,"
			+ "pvd.created_username as draft_created_username,pvd.modified_username as draft_modified_username,"
			+ "pvd.created_date as draft_created_date,pvd.modified_date as draft_modified_date,pvd.created_by as draft_created_by,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,vhn.voucher_head as voucher_head,fh.fee_head as fee_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,v.vendor_name as vendor_name,"
			+ "ua.username as username,ua.usercode as usercode,ua.id as user_id,"
			+ "pv.modified_by as modified_by,pv.active as active from payment_voucher pv "
			+ "left join schools sc on sc.school_id = pv.school_id "
			+ "left join bank ba on ba.bank_id = pv.bank_id "
			+ "left join schools sc1 on sc1.school_id = pv.inter_school_id "
			+ "left join bank ba1 on ba1.bank_id = pv.inter_bank_id "
			+ "left join draft_payment_voucher pvd on pvd.draft_payment_voucher_id = pv.draft_payment_voucher_id "
			+ "left join user_details ua on ua.id = pvd.approver_id "
			+ "left join department de on de.dept_id = pv.dept_id "
			+ "left join voucher_head vh on vh.voucher_head_id = pv.voucher_head_id "
			+ "left join voucher_head_new vhn on vhn.voucher_head_new_id = vh.voucher_head_new_id "
			+ "left join vendor v on v.vendor_id = pv.vendor_id "
			+ "left join fee_head fh on fh.fee_head_id = pv.fee_head_id where pv.school_id=?1 And pv.voucher_head_id=?2 and pv.financial_year_id=?3 And "
			+ "date(pv.date)=date(?4) And pv.active=true",nativeQuery=true)
	public List<Map<String, Object>> getTotalDebitDetailsFromPaymentVoucher(Integer school_id, Integer voucher_head_new_id,
			Integer financial_year_id, Date fromdate);
	
	
	@Query(value = "SELECT jv.voucher_no from PaymentVoucher jv where jv.active=true And jv.payment_voucher_id=?1")
	public Integer getPaymentVoucherData(Integer payment_voucher_id);

	@Query(value = "SELECT jv.school_id from PaymentVoucher jv where jv.active=true And jv.payment_voucher_id=?1")
	public Integer getPaymentVoucherSclId(Integer payment_voucher_id);
	
	@Query(value = "SELECT jv.financial_year_id from PaymentVoucher jv where jv.active=true And jv.payment_voucher_id=?1")
	public Integer getPaymentVoucherFyId(Integer payment_voucher_id);

	
	
	@Query(value = "select new map(pv.payment_voucher_id as id,pv.school_id as school_id,pv.date as date,"
			+ "pv.created_username as created_username,pv.modified_username as modified_username,pv.created_name As created_name,"
			+ "pv.voucher_head_id as voucher_head_id,pv.jv_joucher_number As jv_joucher_number,pv.jv_school_id As jv_school_id,"
			+ "pv.jv_financial_year_id As jv_financial_year_id,pv.cancelled_remarks As cancelled_remarks,"
			+ "pv.inter_school_id As inter_school_id,pv.inter_bank_id As inter_bank_id,"
			+ "sc1.school_name as interschool_name,sc1.school_name_short as interschool_name_short,"
			+ "ba1.bank_name As Inter_bank_name,ba1.bank_short_name As inter_bank_short_name,"
			+ "pv.created_date as created_date,pv.modified_date as modified_date,pv.created_by as created_by,"
			+ "pv.bank_id as bank_id,pv.paying_now as paying_now,pv.cheque_dd_no as cheque_dd_no,pv.remarks as remarks,"
			+ "pv.pay_to as pay_to,pv.expensense_lead as expensense_lead,pv.purchase_order_id as purchase_order_id,"
			+ "pv.financial_year_id as financial_year_id,pv.journal_voucher_id as journal_voucher_id,pv.voucher_no as voucher_no,"
			+ "pv.voucher_head_new_id as voucher_head_new_id,pv.fee_head_id as fee_head_id,pv.credit_total as credit_total,"
			+ "pv.debit_total as debit_total,pv.credit as credit,pv.debit as debit,pv.nature_id as nature_id,pv.note as note,"
			+ "pv.payment_status as payment_status,pv.vendor_id as vendor_id,pv.dept_id as dept_id,pv.status as status,"
			+ "pv.inter_institute_id as inter_institute_id,pv.vendor_active as vendor_active,pv.clearingDate as clearingDate,"
			+ "pv.lockstatus as lockstatus,pv.cancel_voucher as cancel_voucher,pv.voucher_remarks as voucher_remarks,"
			+ "pv.cancelled_by as cancelled_by,pv.cancelled_date as cancelled_date,pv.online as online,pvd.verified_date As verified_date,"
			+ "pv.payment_voucher_id as payment_voucher_id,pv.advanced_po_status as advanced_po_status,pv.actual_date as actual_date,"
			+ "pv.approver_id as approver_id,pv.approved_status as approved_status,pv.approved_date as approved_date,"
			+ "pv.attachment_name as attachment_name,pv.attachment_path as attachment_path,ba.bank_short_name as bank_short_name,"
			+ "pvd.approver_id as draft_approver_id,pvd.approved_status as draft_approved_status,pvd.approved_date as draft_approved_date,"
			+ "pvd.draft_payment_voucher_id as draft_payment_voucher_id,pvd.modified_by as draft_modified_by,pv.type AS type,"
			+ "pvd.created_username as draft_created_username,pvd.modified_username as draft_modified_username,"
			+ "pvd.created_date as draft_created_date,pvd.modified_date as draft_modified_date,pvd.created_by as draft_created_by,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,vhn.voucher_head as voucher_head,fh.fee_head as fee_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,v.vendor_gst_no As vendor_gst_no,pv.env_bill_details_id As env_bill_details_id,"
			+ "v.vendor_name as vendor_name,v.vendor_email as vendor_email,fy.financial_year as financial_year,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "v.account_no As vendoe_account_no,v.nature_of_business As nature_of_business,v.pan_number As vendor_pan_number,"
			+ "v.vendor_bank_account_holder_name As vendor_bank_account_holder_name,v.vendor_bank_ifsc_code As vendor_bank_ifsc_code,"			+ ""
			+ "ed1.employee_name as approverName,ed1.empcode as appeoverCode,ed1.emp_id as approverId,"
			+ "ed.employee_name as verifyName,ed.empcode as verifyCode,ed.emp_id as verifyId,"
			+ "fy.financial_year_id as fy_financial_year_id,fy.financial_year as financial_year,fy.year as year,"
			+ "pv.modified_by as modified_by,pv.active as active) from PaymentVoucher pv "
			+ "left join Schools sc on sc.school_id = pv.school_id "
			+ "left join Bank ba on ba.bank_id = pv.bank_id "
			+ "left join Schools sc1 on sc1.school_id = pv.inter_school_id "
			+ "left join Bank ba1 on ba1.bank_id = pv.inter_bank_id "
			+ "left join DraftPaymentVoucher pvd on pvd.draft_payment_voucher_id = pv.draft_payment_voucher_id "
			+ "left join UserAuthentication ua on ua.id = pvd.approver_id "
			+ "left join UserAuthentication ua1 on ua1.id = pvd.verifier_id "
			+ "left join EmployeeDetails ed on ua1.email = ed.email "
			+ "left join EmployeeDetails ed1 on ua.email = ed1.email "
			+ "left join Department de on de.dept_id = pv.dept_id "
			+ "left join FinancialYear fy on fy.financial_year_id = pv.financial_year_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = pv.voucher_head_id "
			+ "left join Vendor v on v.voucher_head_new_id = pv.voucher_head_id "
			+ "left join FeeHead fh on fh.fee_head_id = pv.fee_head_id "
			+ "where pv.payment_voucher_id=?1 And pv.active=true")
	public Map<String, Object> getPaymentVoucherDataById(Integer payment_voucher_id);

	
	@Query(value = "select new map(pv.payment_voucher_id as payment_voucher_id,"
			+ "ba.voucher_head As bank_name,ba.voucher_head_short_name As bank_short_name) from PaymentVoucher pv "
			+ "left join VoucherHeadNew ba on ba.voucher_head_new_id = pv.bank_id "
			+ "where pv.bank_id=?1 And pv.active=true")
	public List<Map<String, Object>> getVoucherHeadBankDetails(Integer bankId);
	
	
	
//	@Modifying
//	@Query(value = "update PaymentVoucher pv set pv.attachment_path=?2 where pv.payment_voucher_id=?1")
//	public void updatePath(Integer payment_voucher_id, String t2);
//
//
//	@Modifying
//	@Query(value = "update PaymentVoucher pv set pv.attachment_path=?2 where pv.payment_voucher_id=?1")
//	public List<Integer> getDraftPaymentVoucher(Integer voucher_no);
//
}
