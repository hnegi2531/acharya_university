package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.DraftPaymentVoucher;


@Repository
@Transactional
public interface DraftPaymentVoucherRepository extends JpaRepository<DraftPaymentVoucher, Integer> {

	
	@Query(value = "SELECT pvd from DraftPaymentVoucher pvd where pvd.active=true")
	public List<DraftPaymentVoucher> findAll11();
	
	@Modifying
	@Query(value = "update DraftPaymentVoucher pvd set pvd.active=false where pvd.draft_payment_voucher_id=?1")
	public void updateExamDetail(Integer id);
	
	@Modifying
	@Query(value = "update DraftPaymentVoucher pvd set pvd.active=true where pvd.draft_payment_voucher_id=?1")
	public void updateExamDetail1(Integer id);
	
	
	@Query(value = "select new map(pvd.draft_payment_voucher_id as id,pvd.school_id as school_id,pvd.date as date,"
			+ "pvd.created_username as created_username,pvd.modified_username as modified_username,pvd.type As type,"
			+ "pvd.voucher_head_id as voucher_head_id,pvd.jv_school_id As jv_school_id,"
			+ "pvd.inter_school_id As inter_school_id,pvd.inter_bank_id As inter_bank_id,"
			+ "sc1.school_name as interschool_name,sc1.school_name_short as interschool_name_short,"
			+ "ba1.bank_name As Inter_bank_name,ba1.bank_short_name As inter_bank_short_name,"
			+ "sc2.school_name as jvschool_name,sc2.school_name_short as jvschool_name_short,pvd.env_bill_details_id As env_bill_details_id,"
			+ "pvd.created_date as created_date,pvd.modified_date as modified_date,pvd.created_by as created_by,"
			+ "pvd.bank_id as bank_id,pvd.pay_to as pay_to,pvd.cheque_dd_no as cheque_dd_no,pvd.remarks as remarks,"
			+ "pvd.purchase_ref_no as purchase_ref_no,pvd.nature_type as nature_type,pvd.expense_head_id as expense_head_id,"
			+ "pvd.financial_year_id as financial_year_id,pvd.voucher_no as voucher_no,pvd.reference_number as reference_number,"
			+ "pvd.voucher_head_new_id as voucher_head_new_id,pvd.invoice_number as invoice_number,pvd.debit_total as debit_total,"
			+ "pvd.debit as debit,pvd.payment_mode as payment_mode,pvd.approved_status as approved_status,pvd.approved_date as approved_date,"
			+ "pvd.verified_status as verified_status,pvd.vendor_id as vendor_id,pvd.dept_id as dept_id,pvd.verified_date as verified_date,"
			+ "pvd.verifier_id as verifier_id,pvd.approver_id as approver_id,pvd.po_reference as po_reference,"
			+ "pvd.online as online,pvd.po_bill_id as po_bill_id,pvd.online as online,pvd.actual_date as actual_date,"
			+ "pvd.attachment_name as attachment_name,pvd.attachment_path as attachment_path,ba.bank_short_name as bank_short_name,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,vhn.voucher_head as voucher_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,v.vendor_name as vendor_name,v.vendor_email as vendor_email,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "pvd.modified_by as modified_by,pvd.active as active) from DraftPaymentVoucher pvd "
			+ "left join Schools sc on sc.school_id = pvd.school_id "
			+ "left join Schools sc1 on sc1.school_id = pvd.inter_school_id "
			+ "left join Bank ba1 on ba1.bank_id = pvd.inter_bank_id "
			+ "left join Schools sc2 on sc2.school_id = pvd.jv_school_id "
			+ "left join Bank ba on ba.bank_id = pvd.bank_id "
			+ "left join Department de on de.dept_id = pvd.dept_id "
			+ "left join VoucherHead vh on vh.voucher_head_id = pvd.voucher_head_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = vh.voucher_head_new_id "
			+ "left join Vendor v on v.vendor_id = pvd.vendor_id "
		    + "where CONCAT(IfNull(pvd.created_username,''),'',IfNull(pvd.cheque_dd_no,''),'',IfNull(pvd.pay_to,''),'',"
			+ "'',IfNull(pvd.created_by,''),'',IfNull(pvd.created_date,'')) LIKE %?1% AND pvd.approved_status is null group by pvd.voucher_no")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(pvd.draft_payment_voucher_id as id,pvd.school_id as school_id,pvd.date as date,"
			+ "pvd.created_username as created_username,pvd.modified_username as modified_username,pvd.type As type,"
			+ "pvd.voucher_head_id as voucher_head_id,pvd.jv_school_id As jv_school_id,"
			+ "pvd.inter_school_id As inter_school_id,pvd.inter_bank_id As inter_bank_id,"
			+ "sc1.school_name as interschool_name,sc1.school_name_short as interschool_name_short,"
			+ "ba1.bank_name As Inter_bank_name,ba1.bank_short_name As inter_bank_short_name,"
			+ "sc2.school_name as jvschool_name,sc2.school_name_short as jvschool_name_short,pvd.env_bill_details_id As env_bill_details_id,"
			+ "pvd.created_date as created_date,pvd.modified_date as modified_date,pvd.created_by as created_by,"
			+ "pvd.bank_id as bank_id,pvd.pay_to as pay_to,pvd.cheque_dd_no as cheque_dd_no,pvd.remarks as remarks,"
			+ "pvd.purchase_ref_no as purchase_ref_no,pvd.nature_type as nature_type,pvd.expense_head_id as expense_head_id,"
			+ "pvd.financial_year_id as financial_year_id,pvd.voucher_no as voucher_no,pvd.reference_number as reference_number,"
			+ "pvd.voucher_head_new_id as voucher_head_new_id,pvd.invoice_number as invoice_number,pvd.debit_total as debit_total,"
			+ "pvd.debit as debit,pvd.payment_mode as payment_mode,pvd.approved_status as approved_status,pvd.approved_date as approved_date,"
			+ "pvd.verified_status as verified_status,pvd.vendor_id as vendor_id,pvd.dept_id as dept_id,pvd.verified_date as verified_date,"
			+ "pvd.verifier_id as verifier_id,pvd.approver_id as approver_id,pvd.po_reference as po_reference,"
			+ "pvd.online as online,pvd.po_bill_id as po_bill_id,pvd.online as online,pvd.actual_date as actual_date,"
			+ "pvd.attachment_name as attachment_name,pvd.attachment_path as attachment_path,ba.bank_short_name as bank_short_name,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,vhn.voucher_head as voucher_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,v.vendor_name as vendor_name,v.vendor_email as vendor_email,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "pvd.modified_by as modified_by,pvd.active as active) from DraftPaymentVoucher pvd "
			+ "left join Schools sc on sc.school_id = pvd.school_id "
			+ "left join Schools sc1 on sc1.school_id = pvd.inter_school_id "
			+ "left join Bank ba1 on ba1.bank_id = pvd.inter_bank_id "
			+ "left join Schools sc2 on sc2.school_id = pvd.jv_school_id "
			+ "left join Bank ba on ba.bank_id = pvd.bank_id "
			+ "left join Department de on de.dept_id = pvd.dept_id "
			+ "left join VoucherHead vh on vh.voucher_head_id = pvd.voucher_head_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = vh.voucher_head_new_id "
			+ "left join Vendor v on v.vendor_id = pvd.vendor_id "
			+ "where pvd.approved_status is null group by pvd.voucher_no")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Query(value = "select new map(pvd.draft_payment_voucher_id as id,pvd.school_id as school_id,pvd.date as date,"
			+ "pvd.created_username as created_username,pvd.modified_username as modified_username,pvd.type As type,"
			+ "pvd.voucher_head_id as voucher_head_id,pvd.jv_school_id As jv_school_id,"
			+ "pvd.inter_school_id As inter_school_id,pvd.inter_bank_id As inter_bank_id,"
			+ "sc1.school_name as interschool_name,sc1.school_name_short as interschool_name_short,"
			+ "ba1.bank_name As Inter_bank_name,ba1.bank_short_name As inter_bank_short_name,"
			+ "sc2.school_name as jvschool_name,sc2.school_name_short as jvschool_name_short,pvd.env_bill_details_id As env_bill_details_id,"
			+ "pvd.created_date as created_date,pvd.modified_date as modified_date,pvd.created_by as created_by,"
			+ "pvd.bank_id as bank_id,pvd.pay_to as pay_to,pvd.cheque_dd_no as cheque_dd_no,pvd.remarks as remarks,"
			+ "pvd.purchase_ref_no as purchase_ref_no,pvd.nature_type as nature_type,pvd.expense_head_id as expense_head_id,"
			+ "pvd.financial_year_id as financial_year_id,pvd.voucher_no as voucher_no,pvd.reference_number as reference_number,"
			+ "pvd.voucher_head_new_id as voucher_head_new_id,pvd.invoice_number as invoice_number,pvd.debit_total as debit_total,"
			+ "pvd.debit as debit,pvd.payment_mode as payment_mode,pvd.approved_status as approved_status,pvd.approved_date as approved_date,"
			+ "pvd.verified_status as verified_status,pvd.vendor_id as vendor_id,pvd.dept_id as dept_id,pvd.verified_date as verified_date,"
			+ "pvd.verifier_id as verifier_id,pvd.approver_id as approver_id,pvd.po_reference as po_reference,pvd.actual_date as actual_date,"
			+ "pvd.online as online,pvd.po_bill_id as po_bill_id,pvd.online as online,fy.financial_year as financial_year,"
			+ "pvd.attachment_name as attachment_name,pvd.attachment_path as attachment_path,"
			+ "ba.bank_name As bank_name,ba.bank_short_name As bank_short_name,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,vhn.voucher_head as voucher_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,v.vendor_name as vendor_name,v.vendor_email as vendor_email,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "v.vendor_bank_ifsc_code As vendor_bank_ifsc_code,v.account_no As vendoe_account_no,"
			+ "pv.payment_voucher_id as payment_voucher_id,pv.voucher_no as pv_voucher_no,"
			+ "pvd.jv_financial_year_id As jv_financial_year_id,fy1.financial_year As jv_financial_year,"
			+ "pvd.modified_by as modified_by,pvd.active as active) from DraftPaymentVoucher pvd "
			+ "left join Schools sc on sc.school_id = pvd.school_id "
			+ "left join Schools sc1 on sc1.school_id = pvd.inter_school_id "
			+ "left join Bank ba1 on ba1.bank_id = pvd.inter_bank_id "
			+ "left join Schools sc2 on sc2.school_id = pvd.jv_school_id "
			+ "left join FinancialYear fy on fy.financial_year_id = pvd.financial_year_id "
			+ "left join Bank ba on ba.bank_id = pvd.bank_id "
			+ "left join PaymentVoucher pv on pv.payment_voucher_id = pvd.payment_voucher_id "
			+ "left join FinancialYear fy1 on pvd.jv_financial_year_id = fy1.financial_year_id "
			+ "left join Department de on de.dept_id = pvd.dept_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = pvd.voucher_head_id "
			+ "left join Vendor v on v.voucher_head_new_id = pvd.voucher_head_id "
			+ "where pvd.voucher_no=?1 And pvd.school_id=?2 And pvd.financial_year_id=?3 And pvd.active=true")
	public List<HashMap<String, Object>> getDraftPaymentVoucherData(Integer voucher_no,Integer school_id,Integer financial_year_id);
	

	
	@Modifying
	@Query(value = "update DraftPaymentVoucher pvd set pvd.attachment_path=?2 where pvd.draft_payment_voucher_id in (?1)")
	public void updatePath(List<Integer> draft_payment_voucher_id, String t2);

	

	@Modifying
	@Query(value = "select draft_payment_voucher_id from draft_payment_voucher pvd  where pvd.voucher_no in (?1) and pvd.active=true",nativeQuery=true)
	public List<Integer> getDraftPaymentVoucher_ids(Integer voucher_no);

	@Query(value = "SELECT pvd.voucher_no from draft_payment_voucher pvd "
			+ " where pvd.active=true And pvd.financial_year_id=?1 And pvd.school_id=?2 "
			+ "ORDER BY pvd.draft_payment_voucher_id DESC LIMIT 1",nativeQuery=true)
	public Integer getLatestData(Integer financial_year_id, Integer school_id);
	
	@Query(value = "select * from draft_payment_voucher "
			+ " where active=true And financial_year_id=?1 And school_id=?2 "
			+ "ORDER BY draft_payment_voucher_id Desc LIMIT 1",nativeQuery = true)
	public DraftPaymentVoucher getLatestDraftPaymentVoucher(Integer financial_year_id, Integer school_id);
	
	
	@Modifying
	@Query(value = "DELETE From DraftPaymentVoucher dpv where dpv.voucher_no=?1")
	public void deleteDraftPaymentVoucher(Integer voucher_no);

	@Query(value = "select * from draft_payment_voucher where payment_voucher_id=:voucherId",nativeQuery = true)
	public DraftPaymentVoucher findByVoucherId(@Param("voucherId") Integer voucherId);
	
	
	@Query(value = "select new map(pvd.draft_payment_voucher_id as id,pvd.school_id as school_id,pvd.date as date,"
			+ "pvd.created_username as created_username,pvd.modified_username as modified_username,pvd.type As type,"
			+ "pvd.voucher_head_id as voucher_head_id,pvd.jv_school_id As jv_school_id,"
			+ "pvd.inter_school_id As inter_school_id,pvd.inter_bank_id As inter_bank_id,"
			+ "sc1.school_name as interschool_name,sc1.school_name_short as interschool_name_short,"
			+ "ba1.bank_name As Inter_bank_name,ba1.bank_short_name As inter_bank_short_name,"
			+ "sc2.school_name as jvschool_name,sc2.school_name_short as jvschool_name_short,pvd.env_bill_details_id As env_bill_details_id,"
			+ "pvd.created_date as created_date,pvd.modified_date as modified_date,pvd.created_by as created_by,"
			+ "pvd.bank_id as bank_id,pvd.pay_to as pay_to,pvd.cheque_dd_no as cheque_dd_no,pvd.remarks as remarks,"
			+ "pvd.purchase_ref_no as purchase_ref_no,pvd.nature_type as nature_type,pvd.expense_head_id as expense_head_id,"
			+ "pvd.financial_year_id as financial_year_id,pvd.voucher_no as voucher_no,pvd.reference_number as reference_number,"
			+ "pvd.voucher_head_new_id as voucher_head_new_id,pvd.invoice_number as invoice_number,pvd.debit_total as debit_total,"
			+ "pvd.debit as debit,pvd.payment_mode as payment_mode,pvd.approved_status as approved_status,pvd.approved_date as approved_date,"
			+ "pvd.verified_status as verified_status,pvd.vendor_id as vendor_id,pvd.dept_id as dept_id,pvd.verified_date as verified_date,"
			+ "pvd.verifier_id as verifier_id,pvd.approver_id as approver_id,pvd.po_reference as po_reference,pvd.actual_date as actual_date,"
			+ "pvd.online as online,pvd.po_bill_id as po_bill_id,pvd.online as online,fy.financial_year as financial_year,"
			+ "pvd.attachment_name as attachment_name,pvd.attachment_path as attachment_path,ba.bank_short_name as bank_short_name,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,vhn.voucher_head as voucher_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,v.vendor_name as vendor_name,v.vendor_email as vendor_email,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "pv.payment_voucher_id as payment_voucher_id,pv.voucher_no as pv_voucher_no,"
			+ "pvd.modified_by as modified_by,pvd.active as active) from DraftPaymentVoucher pvd "
			+ "left join Schools sc on sc.school_id = pvd.school_id "
			+ "left join Schools sc1 on sc1.school_id = pvd.inter_school_id "
			+ "left join Bank ba1 on ba1.bank_id = pvd.inter_bank_id "
			+ "left join Schools sc2 on sc2.school_id = pvd.jv_school_id "
			+ "left join FinancialYear fy on fy.financial_year_id = pvd.financial_year_id "
			+ "left join Bank ba on ba.bank_id = pvd.bank_id "
			+ "left join PaymentVoucher pv on pv.payment_voucher_id = pvd.payment_voucher_id "
			+ "left join Department de on de.dept_id = pvd.dept_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = pvd.voucher_head_id "
			+ "left join Vendor v on v.voucher_head_new_id = pvd.voucher_head_id "
		    + "where (:verifier_id IS NULL OR pvd.verifier_id = :verifier_id) And (:verified_status IS NULL OR pvd.verified_status = :verified_status)  "
		    + "And CONCAT(IfNull(pvd.created_username,''),'',IfNull(pvd.cheque_dd_no,''),'',IfNull(pvd.pay_to,''),'',"
			+ "'',IfNull(pvd.created_by,''),'',IfNull(pvd.created_date,'')) LIKE %:keyword%  And pvd.approved_status is null And pvd.active=true "
			+ "group by pvd.voucher_no,pvd.financial_year_id,pvd.school_id")
	public Page<Object> getAllDataFilteredByKeywordStatus(Pageable pageable,  Integer verifier_id, Integer verified_status ,Object keyword); 
	
	@Query(value = "select new map(pvd.draft_payment_voucher_id as id,pvd.school_id as school_id,pvd.date as date,"
			+ "pvd.created_username as created_username,pvd.modified_username as modified_username,pvd.type As type,"
			+ "pvd.voucher_head_id as voucher_head_id,pvd.jv_school_id As jv_school_id,"
			+ "pvd.inter_school_id As inter_school_id,pvd.inter_bank_id As inter_bank_id,"
			+ "sc1.school_name as interschool_name,sc1.school_name_short as interschool_name_short,"
			+ "ba1.bank_name As Inter_bank_name,ba1.bank_short_name As inter_bank_short_name,"
			+ "sc2.school_name as jvschool_name,sc2.school_name_short as jvschool_name_short,pvd.env_bill_details_id As env_bill_details_id,"
			+ "pvd.created_date as created_date,pvd.modified_date as modified_date,pvd.created_by as created_by,"
			+ "pvd.bank_id as bank_id,pvd.pay_to as pay_to,pvd.cheque_dd_no as cheque_dd_no,pvd.remarks as remarks,"
			+ "pvd.purchase_ref_no as purchase_ref_no,pvd.nature_type as nature_type,pvd.expense_head_id as expense_head_id,"
			+ "pvd.financial_year_id as financial_year_id,pvd.voucher_no as voucher_no,pvd.reference_number as reference_number,"
			+ "pvd.voucher_head_new_id as voucher_head_new_id,pvd.invoice_number as invoice_number,pvd.debit_total as debit_total,"
			+ "pvd.debit as debit,pvd.payment_mode as payment_mode,pvd.approved_status as approved_status,pvd.approved_date as approved_date,"
			+ "pvd.verified_status as verified_status,pvd.vendor_id as vendor_id,pvd.dept_id as dept_id,pvd.verified_date as verified_date,"
			+ "pvd.verifier_id as verifier_id,pvd.approver_id as approver_id,pvd.po_reference as po_reference,pvd.actual_date as actual_date,"
			+ "pvd.online as online,pvd.po_bill_id as po_bill_id,pvd.online as online,fy.financial_year as financial_year,"
			+ "pvd.attachment_name as attachment_name,pvd.attachment_path as attachment_path,ba.bank_short_name as bank_short_name,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ba.bank_name as bank_name,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,vhn.voucher_head as voucher_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,v.vendor_name as vendor_name,v.vendor_email as vendor_email,"
			+ "v.vendor_contact_no as vendor_contact_no,v.vendor_tin_no as vendor_tin_no,v.vendor_bank_name as vendor_bank_name,"
			+ "pv.payment_voucher_id as payment_voucher_id,pv.voucher_no as pv_voucher_no,"
			+ "pvd.modified_by as modified_by,pvd.active as active) from DraftPaymentVoucher pvd "
			+ "left join Schools sc on sc.school_id = pvd.school_id "
			+ "left join Schools sc1 on sc1.school_id = pvd.inter_school_id "
			+ "left join Bank ba1 on ba1.bank_id = pvd.inter_bank_id "
			+ "left join Schools sc2 on sc2.school_id = pvd.jv_school_id "
			+ "left join FinancialYear fy on fy.financial_year_id = pvd.financial_year_id "
			+ "left join Bank ba on ba.bank_id = pvd.bank_id "
			+ "left join PaymentVoucher pv on pv.payment_voucher_id = pvd.payment_voucher_id "
			+ "left join Department de on de.dept_id = pvd.dept_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id = pvd.voucher_head_id "
			+ "left join Vendor v on v.voucher_head_new_id = pvd.voucher_head_id "
			+ "where (:verifier_id IS NULL OR pvd.verifier_id = :verifier_id) And (:verified_status IS NULL OR pvd.verified_status = :verified_status) "
			+ "And pvd.approved_status is null And pvd.active=true "
			+ "group by pvd.voucher_no,pvd.financial_year_id,pvd.school_id")
	public Page<Object> getAllSortedDataStatus(Pageable pageable, Integer verifier_id, Integer verified_status);
	
	
	
	@Modifying
	@Query(value = "update DraftPaymentVoucher pvd set pvd.active=false where pvd.voucher_no=?1 And pvd.financial_year_id=?2")
	public void deactiveDraftPaymentVoucher(Integer voucher_no,Integer financial_year_id);


}
