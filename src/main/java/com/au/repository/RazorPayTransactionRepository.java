package com.au.repository;

import java.util.List;
import java.util.Map;

import com.au.dto.RazorPayTransactionDetails;
import com.au.model.BulkTransaction;
import com.au.model.RegistrationFeeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.model.RazorPayTransaction;

@Repository
public interface RazorPayTransactionRepository extends JpaRepository<RazorPayTransaction, Long> {

	RazorPayTransaction findByOrderId(String orderId);

	@Query(value = " select r.transaction_type as transactionType, r.order_id as orderId,  r.transaction_date as transactionDate, r.amount as amount,r.payment_id as paymentId, r.remarks as remarks from razor_pay_transaction r where r.student_id=:studentId and r.status='success' ", nativeQuery = true)
	List<Map<String, Object>> getTransactionDetails(Integer studentId);

	@Query(value = " select r from RazorPayTransaction r where r.orderId=:orderId ")
	RazorPayTransaction getByOrderId(String orderId);

	@Query(value = "SELECT ifNull(sum(rd.amount),0) FROM razor_pay_transaction razor "
			+ "left join razor_pay_payment_details rd on razor.razor_pay_transaction_id = rd.razor_pay_transaction_id "
			+ "where razor.student_id=?1 and razor.ac_year_id=?2 and (rd.payment_type = 'Add On Fee' or rd.payment_type = 'Uniform and Stationary Fee' ) "
			+ "and razor.current_year=?3", nativeQuery = true)
	public Double getDataYearly(Integer studentId, Integer ac_year_id, Integer current_year);

	@Query(value = "SELECT ifNull(sum(rd.amount),0) FROM razor_pay_transaction razor "
			+ "left join razor_pay_payment_details rd on razor.razor_pay_transaction_id = rd.razor_pay_transaction_id "
			+ "where razor.student_id=?1 and razor.ac_year_id=?2 and (rd.payment_type = 'Add On Fee' or rd.payment_type = 'Uniform and Stationary Fee' ) "
			+ "and razor.current_sem=?3", nativeQuery = true)
	public Double getDataSemester(Integer studentId, Integer ac_year_id, Integer current_sem);

	@Query(value = " select r from RazorPayTransaction r where r.orderId=:orderId  and r.transactionType=:transactionType ")
	RazorPayTransaction getByOrderIdAndTransactionType(String orderId, String transactionType);

	@Query(value = " select r.amount as amount, r.order_id as orderId, s.auid as auid, r.payment_id as paymentId, r.transaction_date as transactionDate, rp.amount as transferAmount,rp.receipt_type as type  from  razor_pay_payment_details rp left join  razor_pay_transaction r on r.razor_pay_transaction_id=rp.razor_pay_transaction_id left join student_details s on r.student_id=s.student_id where r.status='success' and month(r.transaction_date)=:month and year(r.transaction_date)=:year ", nativeQuery = true)
	List<Map<String, Object>> getTransactionDetailsForPhp(Integer month, Integer year);

	@Query(value = " select r.amount, r.payment_id, r.order_id, r.transaction_date, r.candidate_id, c.npf_status,c.candidate_name from registration_fee_transaction r left join candidate_walkin c on c.candidate_id=r.candidate_id where status='success' "
			+ " and month(r.transaction_date)=:month and year(r.transaction_date)=:year ", nativeQuery = true)
	List<Map<String, Object>> getRegistrationTransactionDetailsForPhp(Integer month, Integer year);


	@Query(value = " SELECT r.razor_pay_transaction_id AS razor_pay_transaction_id,r.amount AS amount,r.created_date AS created_date,"
			+ "r.description AS description,r.email AS email,r.mobile AS mobile,r.name AS name,r.order_id AS order_id,"
			+ "r.paid_year AS paid_year,r.payment_id AS payment_id,r.payment_type AS payment_type,r.receipt_id AS receipt_id,"
			+ "r.remarks AS remarks,r.school_id AS school_id,r.signature AS signature,r.status AS status,"
			+ "r.transaction_date AS transaction_date,r.transaction_type AS transaction_type,s.school_name As school_name,s.school_name_short As school_name_short "
			+ "from bulk_pay_transaction r "
			+ "left join schools s on s.school_id = r.school_id "
			+ "where r.status='success' And r.name is not null", nativeQuery = true)
	List<Map<String, Object>> getTransactionDetailsData();


	@Query(value = "select * from registration_fee_transaction rft  " +
			"where rft.status = 'success' " +
			"and CAST(rft.created_date AS DATE) like concat(?1,'%') ", nativeQuery = true)
	List<Map<String, Object>> getRegistrationFeeTransactionDetails(@Param("date") String date);


	@Query(value = "select * from razor_pay_transaction " +
			"where CAST(created_date AS DATE) like concat(?1,'%')" +
			"and transaction_type = ?2 ", nativeQuery = true)
	List<RazorPayTransaction> getRazorPayTransactionByDateAndType(String date, String type);
}