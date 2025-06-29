package com.au.repository;

import java.util.Date;
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

import com.au.model.EnvBillDetails;

@Transactional
@Repository
public interface EnvBillDetailsRepository extends JpaRepository<EnvBillDetails, Integer>{

	
	@Query(value = "SELECT ebd from EnvBillDetails ebd where ebd.active=true")
	public List<EnvBillDetails> getAllActiveEnvBillDetails();
	
	@Modifying
	@Query(value = "update EnvBillDetails ebd set ebd.active=false where ebd.env_bill_details_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update EnvBillDetails ebd set ebd.active=true where ebd.env_bill_details_id=?1")
	public void activate(Integer id);
	
	
	@Query(value = "select new map(ebd.env_bill_details_id AS id,ebd.category_details_id AS category_details_id,ebd.date AS date,ebd.remarks AS remarks,"
			+ "ebd.requested_amount AS requested_amount,ebd.attachment_path AS attachment_path,ebd.attachment_name AS attachment_name,"
			+ "ebd.journal_voucher_id AS journal_voucher_id,ebd.aprover1_id AS aprover1_id,ebd.aprover1_date AS aprover1_date,"
			+ "ebd.aprover1_remarks AS aprover1_remarks,ebd.aprover1_status AS aprover1_status,ebd.aprover2_id AS aprover2_id,"
			+ "ebd.aprover2_remarks AS aprover2_remarks,ebd.aprover2_status AS aprover2_status,ebd.aprover2_date AS aprover2_date,"
			+ "ebd.payment_voucher_id AS payment_voucher_id,ebd.cancel_epayment_grn AS cancel_epayment_grn,"
			+ "ebd.epayment_cancel_remark AS epayment_cancel_remark,ebd.env_cancelled_by AS env_cancelled_by,ebd.env_cancelled_date AS env_cancelled_date,"
			+ "ebd.created_by AS created_by,ebd.modified_by AS modified_by,ebd.created_date AS created_date,ebd.modified_date AS modified_date,"
			+ "ctd.category_detail As category_detail,"
			+ "ebd.active AS active,ebd.created_username AS created_username,ebd.modified_username AS modified_username ) from EnvBillDetails ebd "
			+ "left join CategoryTypeDetails ctd on ebd.category_details_id=ctd.category_details_id "
			+ "where (DATE(ebd.created_date) >= :minDate) "
	 	    + "AND (:start IS NULL OR DATE(ebd.created_date) >= :start) "
		        + "AND (:end IS NULL OR DATE(ebd.created_date) <= :end) And "
			+ "(:created_by IS NULL OR ebd.created_by = :created_by) "
			+ "And CONCAT(IfNull(ebd.date,''),'',IfNull(ebd.remarks,''),'',IfNull(ebd.category_details_id,''),"
			+ "'',IfNull(ebd.created_by,''),'',IfNull(ebd.created_date,'',IfNull(ebd.env_bill_details_id,''),'')) LIKE %:keyword%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable,
            @Param("keyword") Object keyword,
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("minDate") Date minDate,
            @Param("created_by") Integer created_by);
	
	
	@Query(value = "select new map(ebd.env_bill_details_id AS id,ebd.category_details_id AS category_details_id,ebd.date AS date,ebd.remarks AS remarks,"
			+ "ebd.requested_amount AS requested_amount,ebd.attachment_path AS attachment_path,ebd.attachment_name AS attachment_name,"
			+ "ebd.journal_voucher_id AS journal_voucher_id,ebd.aprover1_id AS aprover1_id,ebd.aprover1_date AS aprover1_date,"
			+ "ebd.aprover1_remarks AS aprover1_remarks,ebd.aprover1_status AS aprover1_status,ebd.aprover2_id AS aprover2_id,"
			+ "ebd.aprover2_remarks AS aprover2_remarks,ebd.aprover2_status AS aprover2_status,ebd.aprover2_date AS aprover2_date,"
			+ "ebd.payment_voucher_id AS payment_voucher_id,ebd.cancel_epayment_grn AS cancel_epayment_grn,"
			+ "ctd.category_detail As category_detail,"
			+ "ebd.epayment_cancel_remark AS epayment_cancel_remark,ebd.env_cancelled_by AS env_cancelled_by,ebd.env_cancelled_date AS env_cancelled_date,"
			+ "ebd.created_by AS created_by,ebd.modified_by AS modified_by,ebd.created_date AS created_date,ebd.modified_date AS modified_date,"
			+ "ebd.active AS active,ebd.created_username AS created_username,ebd.modified_username AS modified_username ) from EnvBillDetails ebd "
			+ "left join CategoryTypeDetails ctd on ebd.category_details_id=ctd.category_details_id "
			+ "where (DATE(ebd.created_date) >= :minDate) "
	 	    + "AND (:start IS NULL OR DATE(ebd.created_date) >= :start) "
		        + "AND (:end IS NULL OR DATE(ebd.created_date) <= :end) And "
			+ "(:created_by IS NULL OR ebd.created_by = :created_by) ")
	public Page<Object> getAllSortedData(Pageable pageable,
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("minDate") Date minDate,
            @Param("created_by") Integer created_by);

	@Query(value = "select new map(ebd.env_bill_details_id AS id,ebd.category_details_id AS category_details_id,ebd.date AS date,ebd.remarks AS remarks,"
			+ "ebd.requested_amount AS requested_amount,ebd.attachment_path AS attachment_path,ebd.attachment_name AS attachment_name,"
			+ "ebd.journal_voucher_id AS journal_voucher_id,ebd.aprover1_id AS aprover1_id,ebd.aprover1_date AS aprover1_date,"
			+ "ebd.aprover1_remarks AS aprover1_remarks,ebd.aprover1_status AS aprover1_status,ebd.aprover2_id AS aprover2_id,"
			+ "ebd.aprover2_remarks AS aprover2_remarks,ebd.aprover2_status AS aprover2_status,ebd.aprover2_date AS aprover2_date,"
			+ "ebd.payment_voucher_id AS payment_voucher_id,ebd.cancel_epayment_grn AS cancel_epayment_grn,"
			+ "ctd.category_detail As category_detail,"
			+ "ebd.epayment_cancel_remark AS epayment_cancel_remark,ebd.env_cancelled_by AS env_cancelled_by,ebd.env_cancelled_date AS env_cancelled_date,"
			+ "ebd.created_by AS created_by,ebd.modified_by AS modified_by,ebd.created_date AS created_date,ebd.modified_date AS modified_date,"
			+ "ebd.active AS active,ebd.created_username AS created_username,ebd.modified_username AS modified_username ) from EnvBillDetails ebd "
			+ "left join CategoryTypeDetails ctd on ebd.category_details_id=ctd.category_details_id where ebd.active=true")
	public List<Map<String, Object>> getEnvBillDetailsdata();

	
	@Query(value = "select ebd.env_bill_details_id AS env_bill_details_id from env_bill_details ebd "
			+ "where ebd.active=true "
			 + "And (:journal_voucher_id is null or ebd.journal_voucher_id = :journal_voucher_id) "
			 + "And (:payment_voucher_id is null or ebd.payment_voucher_id = :payment_voucher_id) ",nativeQuery=true)
	public List<Map<String, Object>> getEnvBillDetailsId(Integer journal_voucher_id, Integer payment_voucher_id);
	
	
	@Query("SELECT MIN(pv.created_date) FROM EnvBillDetails pv")
	Optional<Date> findMinCreatedDate();
}
