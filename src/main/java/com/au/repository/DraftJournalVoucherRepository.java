package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.DraftJournalVoucher;

@Repository
@Transactional
public interface DraftJournalVoucherRepository extends JpaRepository< DraftJournalVoucher ,Integer> {
	
	@Query(value = "SELECT djv from DraftJournalVoucher djv where djv.active=true")
	public List<DraftJournalVoucher> findAll11();
	
	@Modifying
	@Query(value = "update DraftJournalVoucher djv set djv.active=false where djv.draft_journal_voucher_id=?1")
	public void updateDraftJournalVoucher(Integer id);
	
	@Modifying
	@Query(value = "update DraftJournalVoucher djv set djv.active=true where djv.draft_journal_voucher_id=?1")
	public void updateDraftJournalVoucher1(Integer id);
	
	@Query(value = "select new map(djv.draft_journal_voucher_id as id,djv.school_id as school_id,djv.date as date,"
			+ "djv.created_username as created_username,djv.modified_username as modified_username,"
			+ "djv.created_date as created_date,djv.modified_date as modified_date,djv.created_by as created_by,"
			+ "djv.fee_head_id as fee_head_id,djv.journal_voucher_number as journal_voucher_number,djv.paying_now as paying_now,"
			+ "djv.expensense_head as expensense_head,djv.credit as credit,djv.credit_total as credit_total,"
			+ "djv.bank_id as bank_id,djv.pay_to as pay_to,djv.cheque_dd_no as cheque_dd_no,djv.remarks as remarks,"
			+ "djv.financial_year_id as financial_year_id,djv.vendor_active as vendor_active,djv.dept_id as dept_id,"
			+ "djv.vendor_id as vendor_id,djv.salary_structure_head_id as salary_structure_head_id,djv.salary_status as salary_status,"
			+ "djv.month as month,djv.year as year,djv.nature_id as nature_id,djv.cancel_voucher as cancel_voucher, "
			+ "djv.cancelled_by as cancelled_by,djv.cancelled_date as cancelled_date,djv.verified_status as verified_status,"
			+ "djv.approved_status as approved_status,djv.verified_date as verified_date,djv.approved_date as approved_date,"
			+ "djv.verifier_id as verifier_id,djv.approver_id as approver_id,djv.payment_mode as payment_mode, "
			+ "djv.purchase_ref_number as purchase_ref_number,djv.voucher_remarks as voucher_remarks,djv.actual_date as actual_date,"
			+ "djv.ledger_id as ledger_id,djv.voucher_head_id as voucher_head_id,djv.contract_number as contract_number,"
			+ "djv.debit as debit,djv.debit_total as debit_total,djv.journal_voucher_id as journal_voucher_id,"
			+ "djv.reference_number as reference_number,djv.po_bill_id as po_bill_id,ba.bank_short_name as bank_short_name,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,djv.attachment_path as attachment_path,"
			+ "v.vendor_name as vendor_name,v.vendor_email as vendor_email,djv.env_bill_details_id As env_bill_details_id,"
			+ "vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "fy.financial_year as financial_year,vhn.voucher_head_new_id as voucher_head_new_id, djv.type as type,"
			+ "grn.attachmentPath As grnAttachmentPath,env.attachment_path As envAttachment_path,"
			+ "djv.modified_by as modified_by,djv.active as active) from DraftJournalVoucher djv "
			+ "left join Schools sc on sc.school_id = djv.school_id "
			+ "left join Bank ba on ba.bank_id = djv.bank_id "
			+ "left join Department de on de.dept_id = djv.dept_id "
			+ "left join FinancialYear fy on fy.financial_year_id = djv.financial_year_id "
			+ "left join Vendor v on v.vendor_id = djv.vendor_id "
			+ "left join VoucherHead vh on vh.voucher_head_id = djv.voucher_head_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = vh.voucher_head_new_id "
			+ "left join GRN grn on grn.draft_journal_voucher_id = djv.draft_journal_voucher_id "
			+ "left join EnvBillDetails env on env.env_bill_details_id = djv.env_bill_details_id "
		    + "where djv.active=true And CONCAT(IfNull(djv.created_username,''),'',IfNull(djv.cheque_dd_no,''),'',IfNull(djv.pay_to,''),'',"
			+ "IfNull(djv.created_by,''),'',IfNull(djv.created_date,'')) LIKE %?1% "
			+ "And djv.approved_status is null group by djv.journal_voucher_number,djv.financial_year_id,djv.school_id")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(djv.draft_journal_voucher_id as id,djv.school_id as school_id,djv.date as date,"
			+ "djv.created_username as created_username,djv.modified_username as modified_username,djv.env_bill_details_id As env_bill_details_id,"
			+ "djv.created_date as created_date,djv.modified_date as modified_date,djv.created_by as created_by,"
			+ "djv.fee_head_id as fee_head_id,djv.journal_voucher_number as journal_voucher_number,djv.paying_now as paying_now,"
			+ "djv.expensense_head as expensense_head,djv.credit as credit,djv.credit_total as credit_total,"
			+ "djv.bank_id as bank_id,djv.pay_to as pay_to,djv.cheque_dd_no as cheque_dd_no,djv.remarks as remarks,"
			+ "djv.financial_year_id as financial_year_id,djv.vendor_active as vendor_active,djv.dept_id as dept_id,"
			+ "djv.vendor_id as vendor_id,djv.salary_structure_head_id as salary_structure_head_id,djv.salary_status as salary_status,"
			+ "djv.month as month,djv.year as year,djv.nature_id as nature_id,djv.cancel_voucher as cancel_voucher, "
			+ "djv.cancelled_by as cancelled_by,djv.cancelled_date as cancelled_date,djv.verified_status as verified_status,"
			+ "djv.approved_status as approved_status,djv.verified_date as verified_date,djv.approved_date as approved_date,"
			+ "djv.verifier_id as verifier_id,djv.approver_id as approver_id,djv.payment_mode as payment_mode, "
			+ "djv.purchase_ref_number as purchase_ref_number,djv.voucher_remarks as voucher_remarks,djv.actual_date as actual_date,"
			+ "djv.ledger_id as ledger_id,djv.voucher_head_id as voucher_head_id,djv.contract_number as contract_number,"
			+ "djv.debit as debit,djv.debit_total as debit_total,djv.journal_voucher_id as journal_voucher_id,"
			+ "djv.reference_number as reference_number,djv.po_bill_id as po_bill_id,ba.bank_short_name as bank_short_name,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,djv.attachment_path as attachment_path,"
			+ "v.vendor_name as vendor_name,v.vendor_email as vendor_email,vhn.voucher_head_new_id as voucher_head_new_id,"
			+ "vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "fy.financial_year as financial_year,djv.type as type,"
			+ "grn.attachmentPath As grnAttachmentPath,env.attachment_path As envAttachment_path,"
			+ "djv.modified_by as modified_by,djv.active as active) from DraftJournalVoucher djv "
			+ "left join Schools sc on sc.school_id = djv.school_id "
			+ "left join Bank ba on ba.bank_id = djv.bank_id "
			+ "left join Department de on de.dept_id = djv.dept_id "
			+ "left join FinancialYear fy on fy.financial_year_id = djv.financial_year_id "
			+ "left join Vendor v on v.vendor_id = djv.vendor_id "
			+ "left join VoucherHead vh on vh.voucher_head_id = djv.voucher_head_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = vh.voucher_head_new_id "
			+ "left join GRN grn on grn.draft_journal_voucher_id = djv.draft_journal_voucher_id "
			+ "left join EnvBillDetails env on env.env_bill_details_id = djv.env_bill_details_id "
			+ "where djv.active=true And djv.approved_status is null group by djv.journal_voucher_number,djv.financial_year_id,djv.school_id")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Query(value = "SELECT djv.journal_voucher_number from draft_journal_voucher djv "
			+ " where djv.active=true And djv.financial_year_id=?1 And djv.school_id=?2 "
			+ "ORDER BY djv.draft_journal_voucher_id DESC LIMIT 1",nativeQuery=true)
	public Integer getLatestData(Integer integer, Integer integer2);
	
	@Query(value = "select * from draft_journal_voucher  "
			+ " where active=true And financial_year_id=?1 And school_id=?2 "
			+ "ORDER BY draft_journal_voucher_id Desc LIMIT 1",nativeQuery = true)
	public DraftJournalVoucher getLatestDraftPaymentVoucher(Integer integer, Integer integer2);
	
	
	@Query(value = "select new map(djv.draft_journal_voucher_id as id,djv.school_id as school_id,djv.date as date,"
			+ "djv.created_username as created_username,djv.modified_username as modified_username,djv.env_bill_details_id As env_bill_details_id,"
			+ "djv.created_date as created_date,djv.modified_date as modified_date,djv.created_by as created_by,"
			+ "djv.fee_head_id as fee_head_id,djv.journal_voucher_number as journal_voucher_number,djv.paying_now as paying_now,"
			+ "djv.expensense_head as expensense_head,djv.credit as credit,djv.credit_total as credit_total,"
			+ "djv.bank_id as bank_id,djv.pay_to as pay_to,djv.cheque_dd_no as cheque_dd_no,djv.remarks as remarks,"
			+ "djv.financial_year_id as financial_year_id,djv.vendor_active as vendor_active,djv.dept_id as dept_id,"
			+ "djv.vendor_id as vendor_id,djv.salary_structure_head_id as salary_structure_head_id,djv.salary_status as salary_status,"
			+ "djv.month as month,djv.year as year,djv.nature_id as nature_id,djv.cancel_voucher as cancel_voucher, "
			+ "djv.cancelled_by as cancelled_by,djv.cancelled_date as cancelled_date,djv.verified_status as verified_status,"
			+ "djv.approved_status as approved_status,djv.verified_date as verified_date,djv.approved_date as approved_date,"
			+ "djv.verifier_id as verifier_id,djv.approver_id as approver_id,djv.payment_mode as payment_mode, "
			+ "djv.purchase_ref_number as purchase_ref_number,djv.voucher_remarks as voucher_remarks,djv.actual_date as actual_date,"
			+ "djv.ledger_id as ledger_id,djv.voucher_head_id as voucher_head_id,djv.contract_number as contract_number,"
			+ "djv.debit as debit,djv.debit_total as debit_total,djv.journal_voucher_id as journal_voucher_id,"
			+ "djv.reference_number as reference_number,djv.po_bill_id as po_bill_id,ba.bank_short_name as bank_short_name,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,djv.attachment_path as attachment_path,"
			+ "v.vendor_name as vendor_name,v.vendor_email as vendor_email,vhn.voucher_head_new_id as voucher_head_new_id,"
			+ "vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name, djv.type as type,"
			+ "fy.financial_year as financial_year,"
			+ "djv.modified_by as modified_by,djv.active as active) from DraftJournalVoucher djv "
			+ "left join Schools sc on sc.school_id = djv.school_id "
			+ "left join Bank ba on ba.bank_id = djv.bank_id "
			+ "left join Department de on de.dept_id = djv.dept_id "
			+ "left join FinancialYear fy on fy.financial_year_id = djv.financial_year_id "
			+ "left join Vendor v on v.vendor_id = djv.vendor_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = djv.voucher_head_id "
			+ "where djv.journal_voucher_number=?1 And djv.school_id=?2 And "
			+ "djv.financial_year_id=?3 and djv.active=true")
	public List<Map<String, Object>> getDraftJournalVoucherData(Integer journal_voucher_number,Integer school_id,
			Integer financial_year_id);
	
	@Modifying
	@Query(value = "select djv.draft_journal_voucher_id from draft_journal_voucher djv "
			+ "where djv.journal_voucher_number in (?1) and djv.active=true",nativeQuery=true)
	public List<Integer> getDraftJournal_ids(Integer journal_voucher_number);
	
	
	@Modifying
	@Query(value = "update DraftJournalVoucher djv set djv.attachment_path=?2 where djv.draft_journal_voucher_id in (?1)")
	public void updatePath(List<Integer> draft_journal_voucher_id, String t2);

	
	@Modifying
	@Query(value = "DELETE From DraftJournalVoucher djv where djv.journal_voucher_number=?1")
	public void deleteDraftJournalVoucher(Integer journal_voucher_number);	
	
	@Query(value = "SELECT new map(djv.draft_journal_voucher_id as draft_journal_voucher_id,"
			+ "djv.journal_voucher_number as journal_voucher_number,"
			+ "djv.attachment_name as attachment_name,djv.attachment_path as attachment_path) from DraftJournalVoucher djv "
			+ "where djv.active=true And djv.journal_voucher_number=?1 And djv.financial_year_id=?2 group by djv.journal_voucher_number")
	public List<HashMap<String, Object>> findByPaymentVoucherNo(Integer journal_voucher_number, Integer financial_year_id);

	@Query(value = "SELECT djv.journal_voucher_number from DraftJournalVoucher djv where djv.active=true And djv.draft_journal_voucher_id=?1")
	public Integer getDraftJournalVoucherData(Integer draft_journal_voucher_id);

	@Query(value = "SELECT djv.school_id from DraftJournalVoucher djv where djv.active=true And djv.draft_journal_voucher_id=?1")
	public Integer getDraftJournalVoucherSclId(Integer draft_journal_voucher_id);
	
	@Query(value = "SELECT djv.financial_year_id from DraftJournalVoucher djv where djv.active=true And djv.draft_journal_voucher_id=?1")
	public Integer getDraftJournalVoucherFyId(Integer draft_journal_voucher_id);	


}
