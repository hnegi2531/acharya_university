package com.au.repository;

import java.time.LocalDate;
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

import com.au.model.JournalVoucher;



@Repository
@Transactional
public interface JournalVoucherRepository extends JpaRepository<JournalVoucher, Integer> {

	@Query(value = "SELECT jv from JournalVoucher jv where jv.active=true")
	public List<JournalVoucher> findAll11();
	
	@Modifying
	@Query(value = "update JournalVoucher jv set jv.active=false where jv.journal_voucher_id=?1")
	public void updateJournalVoucher(Integer id);
	
	@Modifying
	@Query(value = "update JournalVoucher jv set jv.active=true where jv.journal_voucher_id=?1")
	public void updateJournalVoucher1(Integer id);
	
	@Query(value = "select new map(jv.journal_voucher_id as id,jv.journal_voucher_number as journal_voucher_number,jv.date as date,"
			+ "jv.created_username as created_username,jv.modified_username as modified_username,jv.school_id as school_id,"
			+ "jv.created_date as created_date,jv.modified_date as modified_date,jv.created_by as created_by,"
			+ "jv.env_bill_details_id As env_bill_details_id,jv.cancelled_remarks As cancelled_remarks,"
			+ "jv.bank_id as bank_id,jv.pay_to as pay_to,jv.cheque_dd_no as cheque_dd_no,jv.remarks as remarks,"
			+ "jv.purchase_ref_number as purchase_ref_number,jv.salary_structure_head_id as salary_structure_head_id,jv.year as year,"
			+ "jv.financial_year_id as financial_year_id,jv.reference_number as reference_number,jv.month as month,"
			+ "jv.expensense_head as expensense_head,jv.debit_total as debit_total,jv.credit_total as credit_total,"
			+ "jv.debit as debit,jv.credit as credit,jv.vendor_active as vendor_active,jv.salary_status as salary_status,"
			+ "jv.nature_id as nature_id,jv.cancel_voucher as cancel_voucher,jv.voucher_remarks as voucher_remarks,"
			+ "jv.cancelled_by as cancelled_by,jv.vendor_id as vendor_id,jv.dept_id as dept_id,jv.fee_head_id as fee_head_id,"
			+ "jv.cancelled_date as cancelled_date,jv.po_bill_id as po_bill_id,jv.paying_now as paying_now,"
			+ "jv.attachment_path as attachment_path,jv.fileType as fileType,jv.contract_number as contract_number,"
			+ "jv.payment_mode as payment_mode,jv.voucher_head_id as voucher_head_id,jv.actual_date as actual_date,"
			+ "vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "jv.approver_id as approver_id,jv.approved_status as approved_status,jv.approved_date as approved_date,"
			+ "djv.approver_id as draft_approver_id,djv.approved_status as draft_approved_status,"
			+ "djv.approved_date as draft_approved_date,djv.modified_by as draft_modified_by,djv.created_date as draft_created_date,"
			+ "djv.created_username as draft_created_username,djv.modified_username as draft_modified_username,"
			+ "djv.verified_date As verified_date,djv.verifier_id As verifier_id,ua1.username As verifierName,"
			+ "ba.bank_short_name as bank_short_name,jv.draft_journal_voucher_id as draft_journal_voucher_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,jv.ledger_id as ledger_id,"
			+ "v.vendor_name as vendor_name,v.vendor_email as vendor_email,fy.financial_year as financial_year,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "ua.username as cancelled_username,ua.usercode as usercode,ua.id as user_id,"
			+ "ebd.env_bill_details_id AS env_bill_details_id,ebd.attachment_path As envAttachment_path,"
			+ "grn.grnId AS grnId,grn.attachmentPath As grnAttachment_path,"
			+ "jv.modified_by as modified_by,jv.active as active) from JournalVoucher jv "
			+ "left join Schools sc on sc.school_id = jv.school_id "
			+ "left join Bank ba on ba.bank_id = jv.bank_id "
			+ "left join Department de on de.dept_id = jv.dept_id "
			+ "left join DraftJournalVoucher djv on djv.draft_journal_voucher_id = jv.draft_journal_voucher_id "
			+ "left join UserAuthentication ua on ua.id = djv.approver_id "
			+ "left join UserAuthentication ua1 on ua1.id = djv.verifier_id "
			+ "left join FinancialYear fy on fy.financial_year_id = jv.financial_year_id "
			+ "left join Vendor v on v.vendor_id = jv.vendor_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = jv.voucher_head_id "
			+ "left join EnvBillDetails ebd on ebd.journal_voucher_id = jv.journal_voucher_id "
			+ "left join GRN grn on grn.journal_voucher_id = jv.journal_voucher_id "
			+ "where (DATE(jv.created_date) >= :minDate) "
	 	    + "AND (:start IS NULL OR DATE(jv.created_date) >= :start) "
		        + "AND (:end IS NULL OR DATE(jv.created_date) <= :end) And "
		    + "CONCAT(IfNull(jv.created_username,''),'',IfNull(jv.cheque_dd_no,''),'',IfNull(jv.pay_to,''),'',"
			+ "'',IfNull(jv.created_by,''),'',IfNull(jv.created_date,'')) LIKE %:keyword% "
			+ "group by jv.journal_voucher_number,jv.financial_year_id,jv.school_id ")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable,
            @Param("keyword") Object keyword,
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("minDate") Date minDate);
	
	@Query(value = "select new map(jv.journal_voucher_id as id,jv.journal_voucher_number as journal_voucher_number,jv.date as date,"
			+ "jv.created_username as created_username,jv.modified_username as modified_username,jv.school_id as school_id,"
			+ "jv.created_date as created_date,jv.modified_date as modified_date,jv.created_by as created_by,"
			+ "jv.bank_id as bank_id,jv.pay_to as pay_to,jv.cheque_dd_no as cheque_dd_no,jv.remarks as remarks,"
			+ "jv.purchase_ref_number as purchase_ref_number,jv.salary_structure_head_id as salary_structure_head_id,jv.year as year,"
			+ "jv.financial_year_id as financial_year_id,jv.reference_number as reference_number,jv.month as month,"
			+ "jv.env_bill_details_id As env_bill_details_id,jv.cancelled_remarks As cancelled_remarks,"
			+ "jv.expensense_head as expensense_head,jv.debit_total as debit_total,jv.credit_total as credit_total,"
			+ "jv.debit as debit,jv.credit as credit,jv.vendor_active as vendor_active,jv.salary_status as salary_status,"
			+ "jv.nature_id as nature_id,jv.cancel_voucher as cancel_voucher,jv.voucher_remarks as voucher_remarks,"
			+ "jv.cancelled_by as cancelled_by,jv.vendor_id as vendor_id,jv.dept_id as dept_id,jv.fee_head_id as fee_head_id,"
			+ "jv.cancelled_date as cancelled_date,jv.po_bill_id as po_bill_id,jv.paying_now as paying_now,"
			+ "jv.payment_mode as payment_mode,jv.voucher_head_id as voucher_head_id,jv.actual_date as actual_date,"
			+ "vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "jv.attachment_path as attachment_path,jv.fileType as fileType,jv.contract_number as contract_number,"
			+ "jv.approver_id as approver_id,jv.approved_status as approved_status,jv.approved_date as approved_date,"
			+ "djv.approver_id as draft_approver_id,djv.approved_status as draft_approved_status,"
			+ "djv.approved_date as draft_approved_date,djv.modified_by as draft_modified_by,djv.created_date as draft_created_date,"
			+ "djv.created_username as draft_created_username,djv.modified_username as draft_modified_username,"
			+ "djv.verified_date As verified_date,djv.verifier_id As verifier_id,ua1.username As verifierName,"
			+ "ba.bank_short_name as bank_short_name,jv.draft_journal_voucher_id as draft_journal_voucher_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,jv.ledger_id as ledger_id,"
			+ "v.vendor_name as vendor_name,v.vendor_email as vendor_email,fy.financial_year as financial_year,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "ua.username as cancelled_username,ua.usercode as usercode,ua.id as user_id,"
			+ "ebd.env_bill_details_id AS env_bill_details_id,ebd.attachment_path As envAttachment_path,"
			+ "grn.grnId AS grnId,grn.attachmentPath As grnAttachment_path,"
			+ "jv.modified_by as modified_by,jv.active as active) from JournalVoucher jv "
			+ "left join Schools sc on sc.school_id = jv.school_id "
			+ "left join Bank ba on ba.bank_id = jv.bank_id "
			+ "left join DraftJournalVoucher djv on djv.draft_journal_voucher_id = jv.draft_journal_voucher_id "
			+ "left join UserAuthentication ua on ua.id = djv.approver_id "
			+ "left join UserAuthentication ua1 on ua1.id = djv.verifier_id "
			+ "left join Department de on de.dept_id = jv.dept_id "
			+ "left join FinancialYear fy on fy.financial_year_id = jv.financial_year_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = jv.voucher_head_id "
			+ "left join Vendor v on v.vendor_id = jv.vendor_id "
			+ "left join EnvBillDetails ebd on ebd.journal_voucher_id = jv.journal_voucher_id "
			+ "left join GRN grn on grn.journal_voucher_id = jv.journal_voucher_id "
			+ "where (DATE(jv.created_date) >= :minDate) "
	 	    + "AND (:start IS NULL OR DATE(jv.created_date) >= :start) "
		        + "AND (:end IS NULL OR DATE(jv.created_date) <= :end) "
			+ "group by jv.journal_voucher_number,jv.financial_year_id,jv.school_id ")
	public Page<Object> getAllSortedData(Pageable pageable,
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("minDate") Date minDate);
	
	@Query("SELECT MIN(jv.created_date) FROM JournalVoucher jv")
	Optional<Date> findMinCreatedDate();
	
	@Query(value = "SELECT jv.journal_voucher_number from journal_voucher jv "
			+ " where jv.active=true And jv.financial_year_id=?1 And jv.school_id=?2 "
			+ "ORDER BY jv.journal_voucher_id DESC LIMIT 1",nativeQuery=true)
	public Integer getLatestData(Integer financial_year_id, Integer school_id);
	
	@Query(value = "select * from journal_voucher "
			+ " where active=true And financial_year_id=?1 And school_id=?2 "
			+ "ORDER BY journal_voucher_id Desc LIMIT 1",nativeQuery = true)
	public JournalVoucher getLatestJournalVoucher(Integer financial_year_id, Integer school_id);
	
	@Query(value = "select new map(jv.journal_voucher_id as id,jv.journal_voucher_number as journal_voucher_number,jv.date as date,"
			+ "jv.created_username as created_username,jv.modified_username as modified_username,jv.school_id as school_id,"
			+ "jv.created_date as created_date,jv.modified_date as modified_date,jv.created_by as created_by,"
			+ "jv.env_bill_details_id As env_bill_details_id,jv.cancelled_remarks As cancelled_remarks,"
			+ "jv.bank_id as bank_id,jv.pay_to as pay_to,jv.cheque_dd_no as cheque_dd_no,jv.remarks as remarks,"
			+ "jv.purchase_ref_number as purchase_ref_number,jv.salary_structure_head_id as salary_structure_head_id,jv.year as year,"
			+ "jv.financial_year_id as financial_year_id,jv.reference_number as reference_number,jv.month as month,"
			+ "jv.expensense_head as expensense_head,jv.debit_total as debit_total,jv.credit_total as credit_total,"
			+ "jv.debit as debit,jv.credit as credit,jv.vendor_active as vendor_active,jv.salary_status as salary_status,"
			+ "jv.nature_id as nature_id,jv.cancel_voucher as cancel_voucher,jv.voucher_remarks as voucher_remarks,"
			+ "jv.cancelled_by as cancelled_by,jv.vendor_id as vendor_id,jv.dept_id as dept_id,jv.fee_head_id as fee_head_id,"
			+ "jv.cancelled_date as cancelled_date,jv.po_bill_id as po_bill_id,jv.paying_now as paying_now,jv.actual_date as actual_date,"
			+ "jv.payment_mode as payment_mode,jv.ledger_id as ledger_id,jv.voucher_head_id as voucher_head_id,"
			+ "jv.approver_id as approver_id,jv.approved_status as approved_status,jv.approved_date as approved_date,"
			+ "jv.attachment_path as attachment_path,jv.fileType as fileType,jv.contract_number as contract_number,"
			+ "ba.bank_short_name as bank_short_name,jv.draft_journal_voucher_id as draft_journal_voucher_id,"
			+ "vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,djv.modified_by as draft_modified_by,"
			+ "djv.created_username as draft_created_username,djv.modified_username as draft_modified_username,"
			+ "djv.created_date as draft_created_date,djv.modified_date as draft_modified_date,djv.created_by as draft_created_by,"
			+ "djv.approver_id as draft_approver_id,djv.approved_status as draft_approved_status,djv.approved_date as draft_approved_date,"
			+ "v.vendor_name as vendor_name,v.vendor_email as vendor_email,fy.financial_year as financial_year,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "ua.username as username,ua.usercode as usercode,ua.id as user_id,"
			+ "jv.modified_by as modified_by,jv.active as active) from JournalVoucher jv "
			+ "left join Schools sc on sc.school_id = jv.school_id "
			+ "left join DraftJournalVoucher djv on djv.draft_journal_voucher_id = jv.draft_journal_voucher_id "
			+ "left join UserAuthentication ua on ua.id = djv.approver_id "
			+ "left join Bank ba on ba.bank_id = jv.bank_id "
			+ "left join Department de on de.dept_id = jv.dept_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = jv.voucher_head_id "
			+ "left join FinancialYear fy on fy.financial_year_id = jv.financial_year_id "
			+ "left join Vendor v on v.vendor_id = jv.vendor_id where jv.journal_voucher_number=?1 And jv.school_id=?2 And "
			+ "jv.financial_year_id=?3 and jv.active=true")
	public List<HashMap<String, Object>> getJournalVoucherData(Integer journal_voucher_number,Integer school_id,
			Integer financial_year_id);

	@Modifying
	@Query(value = "update JournalVoucher jv set jv.attachment_path=?1,jv.fileType=?2 where jv.journal_voucher_number=?3")
	public void updateJournalVoucherFilePath(String attachmentPath ,String attachmentFileType,Integer journal_voucher_number);

	
	@Query(value = "SELECT new map(jv.journal_voucher_id as journal_voucher_id,jv.journal_voucher_number as journal_voucher_number,"
			+ "jv.attachment_path as attachment_path,jv.fileType as fileType) from JournalVoucher jv "
			+ "where jv.active=true And jv.journal_voucher_number=?1 And jv.financial_year_id=?2 group by jv.journal_voucher_number")
	public List<HashMap<String, Object>> findByPaymentVoucherNo(Integer journal_voucher_number, Integer financial_year_id);

	
	@Query(value = "select new map(jv.journal_voucher_id as id,jv.journal_voucher_number as journal_voucher_number,jv.date as date,"
			+ "jv.created_username as created_username,jv.modified_username as modified_username,jv.school_id as school_id,"
			+ "jv.created_date as created_date,jv.modified_date as modified_date,jv.created_by as created_by,"
			+ "jv.env_bill_details_id As env_bill_details_id,jv.cancelled_remarks As cancelled_remarks,"
			+ "jv.bank_id as bank_id,jv.pay_to as pay_to,jv.cheque_dd_no as cheque_dd_no,jv.remarks as remarks,"
			+ "jv.purchase_ref_number as purchase_ref_number,jv.salary_structure_head_id as salary_structure_head_id,jv.year as year,"
			+ "jv.financial_year_id as financial_year_id,jv.reference_number as reference_number,jv.month as month,"
			+ "jv.expensense_head as expensense_head,jv.debit_total as debit_total,jv.credit_total as credit_total,"
			+ "jv.debit as debit,jv.credit as credit,jv.vendor_active as vendor_active,jv.salary_status as salary_status,"
			+ "jv.nature_id as nature_id,jv.cancel_voucher as cancel_voucher,jv.voucher_remarks as voucher_remarks,"
			+ "jv.cancelled_by as cancelled_by,jv.vendor_id as vendor_id,jv.dept_id as dept_id,jv.fee_head_id as fee_head_id,"
			+ "jv.cancelled_date as cancelled_date,jv.po_bill_id as po_bill_id,jv.paying_now as paying_now,"
			+ "jv.attachment_path as attachment_path,jv.fileType as fileType,jv.contract_number as contract_number,"
			+ "jv.payment_mode as payment_mode,jv.voucher_head_id as voucher_head_id,jv.actual_date as actual_date,"
			+ "vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "jv.approver_id as approver_id,jv.approved_status as approved_status,jv.approved_date as approved_date,"
			+ "djv.approver_id as draft_approver_id,djv.approved_status as draft_approved_status,"
			+ "djv.verifier_id As verifier_id,ed.employee_name As verifier_name,ed1.employee_name As approver_name,"
			+ "djv.approved_date as draft_approved_date,djv.modified_by as draft_modified_by,djv.created_date as draft_created_date,"
			+ "djv.created_username as draft_created_username,djv.modified_username as draft_modified_username,"
			+ "ba.bank_short_name as bank_short_name,jv.draft_journal_voucher_id as draft_journal_voucher_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,jv.ledger_id as ledger_id,"
			+ "v.vendor_name as vendor_name,v.vendor_email as vendor_email,fy.financial_year as financial_year,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "v.account_no As vendoe_account_no,v.nature_of_business As nature_of_business,v.pan_number As vendor_pan_number,"
			+ "v.vendor_bank_account_holder_name As vendor_bank_account_holder_name,v.vendor_bank_ifsc_code As vendor_bank_ifsc_code,"
			+ "v.vendor_gst_no As vendor_gst_no,"
			+ "ua.username as cancelled_username,ua.usercode as usercode,ua.id as user_id,"
			+ "ua1.username as cancelled_username,ua1.usercode as usercode,ua1.id as user_id,jv.type as type, jv.draftCreatedName as draftCreatedName,"
			+ "jv.modified_by as modified_by,jv.active as active) from JournalVoucher jv "
			+ "left join Schools sc on sc.school_id = jv.school_id "
			+ "left join Bank ba on ba.bank_id = jv.bank_id "
			+ "left join Department de on de.dept_id = jv.dept_id "
			+ "left join DraftJournalVoucher djv on djv.draft_journal_voucher_id = jv.draft_journal_voucher_id "
			+ "left join UserAuthentication ua on ua.id = djv.approver_id "
			+ "left join UserAuthentication ua1 on ua1.id = djv.verifier_id "
			+ "left join EmployeeDetails ed on ua1.email = ed.email "
			+ "left join EmployeeDetails ed1 on ua.email = ed1.email "
			+ "left join FinancialYear fy on fy.financial_year_id = jv.financial_year_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = jv.voucher_head_id "
			+ "left join Vendor v on v.voucher_head_new_id = jv.voucher_head_id "
			+ "where jv.journal_voucher_number=?1 And jv.school_id=?2 And jv.financial_year_id=?3 and jv.active=true")
	public  List<Map<String, Object>> getJournalVoucherByVoucherNumber(Integer journal_voucher_number, Integer school_id,
			Integer financial_year_id);
	
	@Query(value = "select new map(jv.journal_voucher_id as id,jv.journal_voucher_number as journal_voucher_number,jv.date as date,"
			+ "jv.created_username as created_username,jv.modified_username as modified_username,jv.school_id as school_id,"
			+ "jv.created_date as created_date,jv.modified_date as modified_date,jv.created_by as created_by,"
			+ "jv.env_bill_details_id As env_bill_details_id,jv.cancelled_remarks As cancelled_remarks,"
			+ "jv.bank_id as bank_id,jv.pay_to as pay_to,jv.cheque_dd_no as cheque_dd_no,jv.remarks as remarks,"
			+ "jv.purchase_ref_number as purchase_ref_number,jv.salary_structure_head_id as salary_structure_head_id,jv.year as year,"
			+ "jv.financial_year_id as financial_year_id,jv.reference_number as reference_number,jv.month as month,"
			+ "jv.expensense_head as expensense_head,jv.debit_total as debit_total,jv.credit_total as credit_total,"
			+ "jv.debit as debit,jv.credit as credit,jv.vendor_active as vendor_active,jv.salary_status as salary_status,"
			+ "jv.nature_id as nature_id,jv.cancel_voucher as cancel_voucher,jv.voucher_remarks as voucher_remarks,"
			+ "jv.cancelled_by as cancelled_by,jv.vendor_id as vendor_id,jv.dept_id as dept_id,jv.fee_head_id as fee_head_id,"
			+ "jv.cancelled_date as cancelled_date,jv.po_bill_id as po_bill_id,jv.paying_now as paying_now,"
			+ "jv.attachment_path as attachment_path,jv.fileType as fileType,jv.contract_number as contract_number,"
			+ "jv.payment_mode as payment_mode,jv.voucher_head_id as voucher_head_id,jv.actual_date as actual_date,"
			+ "vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "jv.approver_id as approver_id,jv.approved_status as approved_status,jv.approved_date as approved_date,"
			+ "djv.approver_id as draft_approver_id,djv.approved_status as draft_approved_status,"
			+ "djv.verifier_id As verifier_id,ed.employee_name As verifier_name,ed1.employee_name As approver_name,"
			+ "djv.approved_date as draft_approved_date,djv.modified_by as draft_modified_by,djv.created_date as draft_created_date,"
			+ "djv.created_username as draft_created_username,djv.modified_username as draft_modified_username,"
			+ "ba.bank_short_name as bank_short_name,jv.draft_journal_voucher_id as draft_journal_voucher_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,jv.ledger_id as ledger_id,"
			+ "v.vendor_name as vendor_name,v.vendor_email as vendor_email,fy.financial_year as financial_year,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "v.account_no As vendoe_account_no,v.nature_of_business As nature_of_business,v.pan_number As vendor_pan_number,"
			+ "v.vendor_bank_account_holder_name As vendor_bank_account_holder_name,v.vendor_bank_ifsc_code As vendor_bank_ifsc_code,"
			+ "v.vendor_gst_no As vendor_gst_no,"
			+ "ua.username as cancelled_username,ua.usercode as usercode,ua.id as user_id,"
			+ "ua1.username as cancelled_username,ua1.usercode as usercode,ua1.id as user_id,"
			+ "jv.modified_by as modified_by,jv.active as active) from JournalVoucher jv "
			+ "left join Schools sc on sc.school_id = jv.school_id "
			+ "left join Bank ba on ba.bank_id = jv.bank_id "
			+ "left join Department de on de.dept_id = jv.dept_id "
			+ "left join DraftJournalVoucher djv on djv.draft_journal_voucher_id = jv.draft_journal_voucher_id "
			+ "left join UserAuthentication ua on ua.id = djv.approver_id "
			+ "left join UserAuthentication ua1 on ua1.id = djv.verifier_id "
			+ "left join EmployeeDetails ed on ua1.email = ed.email "
			+ "left join EmployeeDetails ed1 on ua.email = ed1.email "
			+ "left join FinancialYear fy on fy.financial_year_id = jv.financial_year_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = jv.voucher_head_id "
			+ "left join Vendor v on v.voucher_head_new_id = jv.voucher_head_id "
			+ "where jv.journal_voucher_id=?1 And jv.active=true")
	public  Map<String, Object> getJournalVoucherDataById(Integer journal_voucher_id);

	@Query(value ="Select sum(jv.debit) "
			+ "from journal_voucher jv "
			+ "Where jv.financial_year_id=?1 And jv.active=true",nativeQuery=true)
	public String getTotalAmount(Integer financial_year_id);

	@Query(value = "SELECT sum(debit) FROM journal_voucher where voucher_head_id=?1 and financial_year_id=?2 and active=true",nativeQuery=true)
	public Double getSumOfDebitFromJournalVoucher(Integer voucher_head_id, Integer financial_year_id);

	@Query(value = "select jv.journal_voucher_id as journal_voucher_id,jv.journal_voucher_number as journal_voucher_number,jv.date as date,"
			+ "jv.created_username as created_username,jv.modified_username as modified_username,jv.school_id as school_id,"
			+ "jv.created_date as created_date,jv.modified_date as modified_date,jv.created_by as created_by,"
			+ "jv.env_bill_details_id As env_bill_details_id,jv.cancelled_remarks As cancelled_remarks,"
			+ "jv.bank_id as bank_id,jv.pay_to as pay_to,jv.cheque_dd_no as cheque_dd_no,jv.remarks as remarks,"
			+ "jv.purchase_ref_number as purchase_ref_number,jv.salary_structure_head_id as salary_structure_head_id,jv.year as year,"
			+ "jv.financial_year_id as financial_year_id,jv.reference_number as reference_number,jv.month as month,"
			+ "jv.expensense_head as expensense_head,jv.debit_total as debit_total,jv.credit_total as credit_total,"
			+ "jv.debit as debit,jv.credit as credit,jv.vendor_active as vendor_active,jv.salary_status as salary_status,"
			+ "jv.nature_id as nature_id,jv.cancel_voucher as cancel_voucher,jv.voucher_remarks as voucher_remarks,"
			+ "jv.cancelled_by as cancelled_by,jv.vendor_id as vendor_id,jv.dept_id as dept_id,jv.fee_head_id as fee_head_id,"
			+ "jv.cancelled_date as cancelled_date,jv.po_bill_id as po_bill_id,jv.paying_now as paying_now,"
			+ "jv.payment_mode as payment_mode,jv.voucher_head_id as voucher_head_id,jv.actual_date as actual_date,"
			+ "jv.approver_id as approver_id,jv.approved_status as approved_status,jv.approved_date as approved_date,"
			+ "jv.draft_journal_voucher_id as draft_journal_voucher_id,jv.ledger_id as ledger_id,"
			+ "ua.username as username,ua.usercode as usercode,ua.id as user_id,"
			+ "jv.modified_by as modified_by,jv.active as active from journal_voucher jv "
			+ "left join user_details ua on ua.id = jv.cancelled_by "
			+ "where jv.school_id=?1 And jv.voucher_head_id=?2 And jv.financial_year_id=?3 and date(jv.date)=date(?4) and jv.active=true",nativeQuery=true)
	public List<Map<String, Object>> getTotalDebitDetailsFromJournalVoucher(Integer school_id, Integer voucher_head_id,
			Integer financial_year_id, Date fromdate);
	
	@Query(value = "Select jv.school_id from journal_voucher jv where jv.month=?1 And jv.year=?2 And jv.active=true",nativeQuery=true)
	List<Integer> journalVoucherCreatedSchoolIds(Integer month,Integer year);
	
	@Query(value = "Select IfNull(Max(jv.journal_voucher_number),0) from journal_voucher jv where jv.school_id=?1 And jv.financial_year_id=?2 And jv.active=true",nativeQuery=true)
	public Integer getMaxJournalVoucherNumber(Integer schoolId,Integer financial_year_id);


	@Query(value = "SELECT jv.journal_voucher_number from JournalVoucher jv where jv.active=true And jv.journal_voucher_id=?1")
	public Integer getJournalVoucherData(Integer journal_voucher_id);

	@Query(value = "SELECT jv.school_id from JournalVoucher jv where jv.active=true And jv.journal_voucher_id=?1")
	public Integer getJournalVoucherSclId(Integer journal_voucher_id);
	
	@Query(value = "SELECT jv.financial_year_id from JournalVoucher jv where jv.active=true And jv.journal_voucher_id=?1")
	public Integer getJournalVoucherFyId(Integer journal_voucher_id);
	
	@Query(value = "select jv.journal_voucher_id from journal_voucher jv "
			+ "where jv.journal_voucher_number=?1 And jv.school_id=?2 And jv.financial_year_id=?3 and jv.active=true "
			+ "ORDER BY jv.journal_voucher_id Desc LIMIT 1",nativeQuery = true)
	public Integer getJournalVoucherId(Integer journal_voucher_number, Integer school_id,
			Integer financial_year_id);
//
//	@Query("SELECT MIN(jv.created_date) FROM JournalVoucher jv")
//	public Date findMinCreatedDate();

}

