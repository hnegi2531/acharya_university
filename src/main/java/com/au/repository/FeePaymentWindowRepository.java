package com.au.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.FeePaymentWindow;
import com.au.model.Schools;
import com.au.model.VoucherHeadNew;


@Repository
@Transactional
public interface FeePaymentWindowRepository extends JpaRepository<FeePaymentWindow,Integer>{
	
	@Query(value="Select new map(fpw.from_date as from_date,fpw.to_date as to_date,fpw.amount as amount,fpw.fixed as fixed,fpw.external_status as external_status,"
			+ "fpw.remarks as remarks,fpw.window_type as window_type,sch.school_name as school_name,sch.school_id as school_id,fpw.transfer_type As transfer_type,"
			+ "fpw.voucher_head_new_id as voucher_head_new_id,fpw.fee_payment_window_id as fee_payment_window_id) From FeePaymentWindow fpw "
			+ "Left Join Schools sch On sch.school_id= fpw.school_id "
			+ "Left Join UserAuthentication usr On fpw.user_id=usr.id Where fpw.school_id=?1 "
			+ "And (date(?2) Between DATE_FORMAT(STR_TO_DATE(fpw.from_date, '%Y-%m-%d'),'%Y-%m-%d') And DATE_FORMAT(STR_TO_DATE(fpw.to_date, '%Y-%m-%d'),'%Y-%m-%d')) "
			+ "And FIND_IN_SET(?3,fpw.program_id) > 0 And fpw.active=true",nativeQuery=false)
	List<HashMap<String, Object>> feePaymentWindowsDetails(Integer schoolId, Date currentDate,String programId);
	
	@Query(value="Select new map(fpw.from_date as from_date,fpw.to_date as to_date,fpw.amount as amount,fpw.fixed as fixed,fpw.external_status as external_status,"
			+ "fpw.remarks as remarks,fpw.window_type as window_type,sch.school_name as school_name,sch.school_id as school_id,"
			+ "fpw.voucher_head_new_id as voucher_head_new_id,fpw.transfer_type As transfer_type,"
			+ "fpw.fee_payment_window_id as fee_payment_window_id) From FeePaymentWindow fpw "
			+ "Left Join Schools sch On sch.school_id= fpw.school_id "
			+ "Left Join UserAuthentication usr On fpw.user_id=usr.id Where fpw.school_id=?1 "
			+ "And (date(?2) Between DATE_FORMAT(STR_TO_DATE(fpw.from_date, '%Y-%m-%d'),'%Y-%m-%d') And DATE_FORMAT(STR_TO_DATE(fpw.to_date, '%Y-%m-%d'),'%Y-%m-%d')) "
			+ "And FIND_IN_SET(?3,fpw.program_id) > 0 And fpw.window_type='EXAM' And fpw.active=true",nativeQuery=false)
	List<HashMap<String, Object>> feePaymentWindowsDetailsForExam(Integer schoolId, Date currentDate,String programId);

	@Query(value="Select fpw.voucher_head_new_id From fee_payment_window fpw Where fpw.school_id=:school_id "
			+ "And (:formattedDate Between fpw.from_date And fpw.to_date) And fpw.active=true",nativeQuery=true)
	List<String> getVoucherHeadId(Integer school_id, String formattedDate);

	@Query(value = "SELECT count(*) FROM fee_payment_window fpw " +
            "WHERE fpw.school_id = ?1 " +
            "AND FIND_IN_SET(?2,fpw.program_id) > 0 " +
            "AND ((STR_TO_DATE(fpw.from_date, '%Y-%m-%d') BETWEEN STR_TO_DATE(?3, '%Y-%m-%d') AND STR_TO_DATE(?4, '%Y-%m-%d')) " +
            "     OR (STR_TO_DATE(fpw.to_date, '%Y-%m-%d') BETWEEN STR_TO_DATE(?3, '%Y-%m-%d') AND STR_TO_DATE(?4, '%Y-%m-%d')) " +
            "     OR (STR_TO_DATE(?3, '%Y-%m-%d') BETWEEN STR_TO_DATE(fpw.from_date, '%Y-%m-%d') AND STR_TO_DATE(fpw.to_date, '%Y-%m-%d')) " +
            "     OR (STR_TO_DATE(?4, '%Y-%m-%d') BETWEEN STR_TO_DATE(fpw.from_date, '%Y-%m-%d') AND STR_TO_DATE(fpw.to_date, '%Y-%m-%d'))) " +
            "AND fpw.active = true", nativeQuery = true)
	Integer getcount(Integer school_id, Integer program_id, String from_date, String to_date);

	@Query(value = "Select new map(fpw.fee_payment_window_id as id,bpt.razorPayTransactionId As razorPayTransactionId, fpw.active as active, "
			+ "fpw.school_id as school_id, fpw.voucher_head_new_id as voucher_head_new_id,fpw.transfer_type As transfer_type,"
			+ " fpw.user_id as user_id, fpw.amount as amount,fpw.created_by as created_by, fpw.created_date as created_date,bpt.remarks As TransactionRemarks,"
			+ " fpw.created_username as created_username, fpw.fixed as fixed, fpw.external_status as external_status, fpw.remarks as remarks,"
			+ " fpw.attachment_path as attachment_path, fpw.attachment_file as attachment_file, fpw.window_type as window_type,sum(bpt.amount) As totalAmount,"
			+ " sc.school_name as school_name,sc.school_name_short as school_name_short,vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ " ua.username as username,ua.email as email,fpw.voucher_head As commavoucher_head,fpw.program As commaprogram,fpw.userName As commauserName,"
			+ " fpw.modified_date as modified_date, fpw.modified_username as modified_username, fpw.from_date as from_date,fpw.to_date as to_date,"
			+ " fpw.program_id as program_id, bpt.status as Status )"
			+ " From FeePaymentWindow fpw "
			+ "Left Join BulkTransaction bpt On (fpw.fee_payment_window_id=bpt.transactionId and bpt.status ='success') "
			+ "Left Join Schools sc On fpw.school_id=sc.school_id "
			+ "Left Join VoucherHeadNew vhn On vhn.voucher_head_new_id=fpw.voucher_head_new_id "
			+ "Left Join UserAuthentication ua On fpw.user_id=ua.id "
			+ "Where (:user_id IS NULL OR FIND_IN_SET(:user_id, fpw.user_id) > 0) "
			+ "And CONCAT(IfNull(fpw.voucher_head_new_id,''),'',IfNull(fpw.school_id,''),'',IfNull(fpw.amount,''),"
			+ "'',IfNull(fpw.created_by,''),'',IfNull(fpw.created_date,''),'',IfNull(fpw.user_id,'')) LIKE %:keyword%  group by fpw.fee_payment_window_id")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable,String user_id, Object keyword);
	
	@Query(value = "Select new map(fpw.fee_payment_window_id as id,bpt.razorPayTransactionId As razorPayTransactionId, fpw.active as active, "
			+ "fpw.school_id as school_id, fpw.voucher_head_new_id as voucher_head_new_id,fpw.transfer_type As transfer_type,"
			+ " fpw.user_id as user_id, fpw.amount as amount,fpw.created_by as created_by, fpw.created_date as created_date,bpt.remarks As TransactionRemarks,"
			+ " fpw.created_username as created_username, fpw.fixed as fixed, fpw.external_status as external_status, fpw.remarks as remarks,"
			+ " fpw.attachment_path as attachment_path, fpw.attachment_file as attachment_file, fpw.window_type as window_type,sum(bpt.amount) As totalAmount,"
			+ " sc.school_name as school_name,sc.school_name_short as school_name_short,vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ " ua.username as username,ua.email as email,fpw.voucher_head As commavoucher_head,fpw.program As commaprogram,fpw.userName As commauserName,"
			+ " fpw.modified_date as modified_date, fpw.modified_username as modified_username, fpw.from_date as from_date,fpw.to_date as to_date,"
			+ " fpw.program_id as program_id, bpt.status as Status )"
			+ " From FeePaymentWindow fpw "
			+ "Left Join BulkTransaction bpt On (fpw.fee_payment_window_id=bpt.transactionId and bpt.status ='success') "
			+ "Left Join Schools sc On fpw.school_id=sc.school_id "
			+ "Left Join VoucherHeadNew vhn On vhn.voucher_head_new_id=fpw.voucher_head_new_id "
			+ "Left Join UserAuthentication ua On fpw.user_id=ua.id "
			+ "where (:user_id IS NULL OR FIND_IN_SET(:user_id, fpw.user_id) > 0) And fpw.active=true group by fpw.fee_payment_window_id")
	public Page<Object> getAllSortedData(Pageable pageable,String user_id);
	
	@Query(value="Select new map(fpw.from_date as from_date,fpw.to_date as to_date,fpw.amount as amount,fpw.fixed as fixed,fpw.external_status as external_status,"
			+ "fpw.remarks as remarks,fpw.window_type as window_type,sch.school_name as school_name,sch.school_id as school_id,fpw.voucher_head_new_id as voucher_head_new_id,fpw.fee_payment_window_id as fee_payment_window_id) From FeePaymentWindow fpw "
			+ "Left Join Schools sch On sch.school_id= fpw.school_id "
			+ "Left Join UserAuthentication usr On fpw.user_id=usr.id Where fpw.school_id=?1 "
			+ "And (date(?2) Between DATE_FORMAT(STR_TO_DATE(fpw.from_date, '%Y-%m-%d'),'%Y-%m-%d') And DATE_FORMAT(STR_TO_DATE(fpw.to_date, '%Y-%m-%d'),'%Y-%m-%d')) And fpw.active=true",nativeQuery=false)
	List<HashMap<String, Object>> feePaymentWindowsDetailsForBulk(Integer school_id, Date currentDate);

	
	@Query(value = "Select fpw.fee_payment_window_id as fee_payment_window_id, fpw.active as active, fpw.voucher_head_new_id as voucher_head_new_id,"
			+ " fpw.user_id as user_id, fpw.amount as amount,fpw.created_by as created_by, fpw.created_date as created_date,"
			+ " fpw.created_username as created_username, fpw.fixed as fixed, fpw.external_status as external_status, fpw.remarks as remarks,"
			+ " fpw.attachment_path as attachment_path, fpw.attachment_file as attachment_file, fpw.window_type as window_type,"
			+ " sc.school_name as school_name,sc.school_name_short as school_name_short,vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ " ua.username as username,ua.email as email,fpw.voucher_head As commavoucher_head,fpw.program As commaprogram,fpw.user_name As commauserName,"
			+ " fpw.modified_date as modified_date, fpw.modified_username as modified_username, fpw.from_date as from_date,fpw.to_date as to_date,"
			+ " bpt.razor_pay_transaction_id AS razor_pay_transaction_id,bpt.amount AS transaction_amount,bpt.code AS code,"
			+ "bpt.description AS description,bpt.email AS transaction_email,bpt.transaction_id AS transaction_id,"
			+ "bpt.mobile AS mobile,bpt.name AS name,fpw.transfer_type As transfer_type,"
			+ "bpt.order_id AS order_id,bpt.paid_year AS paid_year,bpt.payment_id AS payment_id,"
			+ "bpt.payment_type AS payment_type,bpt.reason AS reason,bpt.receipt_id AS receipt_id,bpt.remarks AS transaction_remarks,"
			+ "bpt.school_id AS school_id,bpt.signature AS signature,bpt.source AS source,bpt.status AS bulkStatus,"
			+ "bpt.step AS step,bpt.transaction_date AS transaction_date,bpt.transaction_type AS transaction_type,"
			+ " fpw.program_id as program_id, fpw.Status as Status "
			+ " From fee_payment_window fpw "
			+ "Left Join bulk_pay_transaction bpt On fpw.fee_payment_window_id=bpt.transaction_id "
			+ "Left Join schools sc On fpw.school_id=sc.school_id "
			+ "Left Join voucher_head_new vhn On vhn.voucher_head_new_id=fpw.voucher_head_new_id "
			+ "Left Join user_details ua On fpw.user_id=ua.id "
			+ "where fpw.user_id=?1 And fpw.active=true group by fpw.fee_payment_window_id " ,nativeQuery=true)
	List<Map<String, Object>> getFeePaymentWindowBasedOnUserId(Integer user_id);

	
	@Query(value = "Select fpw.fee_payment_window_id as fee_payment_window_id, fpw.active as active, fpw.voucher_head_new_id as voucher_head_new_id,"
			+ " fpw.user_id as user_id, fpw.amount as amount,fpw.created_by as created_by, fpw.created_date as created_date,"
			+ " fpw.created_username as created_username, fpw.fixed as fixed, fpw.external_status as external_status, fpw.remarks as remarks,"
			+ " fpw.attachment_path as attachment_path, fpw.attachment_file as attachment_file, fpw.window_type as window_type,"
			+ " sc.school_name as school_name,sc.school_name_short as school_name_short,vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ " ua.username as username,ua.email as email,fpw.voucher_head As commavoucher_head,fpw.program As commaprogram,fpw.user_name As commauserName,"
			+ " fpw.modified_date as modified_date, fpw.modified_username as modified_username, fpw.from_date as from_date,fpw.to_date as to_date,"
			+ " bpt.razor_pay_transaction_id AS razor_pay_transaction_id,bpt.amount AS transaction_amount,bpt.code AS code,"
			+ "bpt.description AS description,bpt.email AS transaction_email,bpt.transaction_id AS transaction_id,"
			+ "bpt.mobile AS mobile,bpt.name AS name,fpw.transfer_type As transfer_type,"
			+ "bpt.order_id AS order_id,bpt.paid_year AS paid_year,bpt.payment_id AS payment_id,"
			+ "bpt.payment_type AS payment_type,bpt.reason AS reason,bpt.receipt_id AS receipt_id,bpt.remarks AS transaction_remarks,"
			+ "bpt.school_id AS school_id,bpt.signature AS signature,bpt.source AS source,bpt.status AS bulkStatus,"
			+ "bpt.step AS step,bpt.transaction_date AS transaction_date,bpt.transaction_type AS transaction_type,"
			+ " fpw.program_id as program_id "
			+ " From fee_payment_window fpw "
			+ "Left Join bulk_pay_transaction bpt On (fpw.fee_payment_window_id=bpt.transaction_id and bpt.status ='success') "
			+ "Left Join schools sc On fpw.school_id=sc.school_id "
			+ "Left Join voucher_head_new vhn On vhn.voucher_head_new_id=fpw.voucher_head_new_id "
			+ "Left Join user_details ua On fpw.user_id=ua.id "
			+ "where bpt.transaction_id=?1 And fpw.active=true And bpt.status='success' " ,nativeQuery=true)
	List<Map<String, Object>> getBulkPayTransaction(Integer fee_payment_window_id);
	
 
}
