package com.au.repository;


import java.util.HashMap;
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

import com.au.model.Vendor;
@Repository
@Transactional
public interface VendorRepository extends JpaRepository<Vendor , Integer> {
	
	@Query(value ="Select ve From Vendor ve Where ve.active=true")
	public List<Vendor> findAll1();
	
	@Modifying
	@Query(value = "Update Vendor ve set ve.active=false Where ve.vendor_id=?1")
	public void updateVendor(Integer vendor_id);

	@Modifying
	@Query(value = "Update Vendor ve set ve.active=true where ve.vendor_id=?1")
	public void updateVendor1(Integer vendor_id);
	
	@Query(value = "Select COALESCE(count(ve.vendor_email),0)  From vendor ve where  ve.vendor_email=?1 and ve.active=true",nativeQuery=true)
	public Integer vendorCountOnVendorEmail( String vendor_email );
	
	@Query(value = "Select COALESCE(count(ve.vendor_name),0)  From vendor ve where ve.vendor_name=?1  and ve.active=true",nativeQuery=true)
	public Integer vendorCountOnVendorName( String vendor_name);
	
	@Query(value = "Select COALESCE(count(ve.vendor_gst_no),0)  From vendor ve where ve.vendor_gst_no=?1  and ve.active=true",nativeQuery=true)
	public Integer vendorCountOnVendorGstNo( String vendor_gst_no);
	
	@Query(value = "Select COALESCE(count(ve.pan_number),0)  From vendor ve where ve.pan_number=?1  and ve.active=true",nativeQuery=true)
	public Integer vendorCountOnVendorPanNumber( String pan_number);
	
	@Query(value = "Select COALESCE(count(ve.ctea_number),0)  From vendor ve where ve.ctea_number=?1  and ve.active=true",nativeQuery=true)
	public Integer vendorCountOnVendorCteaNumber( String ctea_number);
	
	@Query(value = "Select COALESCE(count(ve.account_no),0)  From vendor ve where ve.account_no=?1  and ve.active=true",nativeQuery=true)
	public Integer vendorCountOnVendorAccountNo( String account_no);
	
	
	@Query(value = "Select new map(ve.vendor_id as id,ve.vendor_name as vendor_name,ve.vendor_email as vendor_email,"
			+ "ve.vendor_contact_no as vendor_contact_no,ve.vendor_gst_no as vendor_gst_no,ve.vendor_bank_account_holder_name as vendor_bank_account_holder_name,"
			+ "ve.vendor_bank_name as vendor_bank_name,ve.bank_branch as bank_branch,ve.vendor_bank_ifsc_code as vendor_bank_ifsc_code,"
			+ "ve.pin_code as pin_code,ve.pan_number as pan_number,ve.street_name as street_name,ve.account_no as account_no,"
			+ "ve.area as area,ve.vendor_type as vendor_type,ve.created_by as created_by,ve.modified_by as modified_by,ve.state_id as state_id,"
			+ "ve.created_date as created_date,ve.modified_date as modified_date,ve.active as active,ve.country_id as country_id,"
			+ "ve.nature_of_business as nature_of_business,ve.credit_period as credit_period,ve.vendor_city_id as vendor_city_id,"
			+ "ve.vendor_address as vendor_address,ve.created_username as created_username,le.ledger_name as ledger_name,le.ledger_id as ledger_id,"
			+ "vhn.voucher_head_new_id as voucher_head_new_id,vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "ve.modified_username as modified_username,st.name as state_name,ct.name as city_name,co.name as country_name,"
			+ "ve.account_verifier_date as account_verifier_date,ve.verifier_user_id as verifier_user_id,"
			+ "ve.account_verification_status as account_verification_status) From Vendor ve "
			+ "Left join State st on st.id = ve.state_id "
			+ "Left join Country co on co.id = ve.country_id "
			+ "Left join City ct on ct.id = ve.vendor_city_id "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id = ve.voucher_head_new_id "
			+ "Left join Ledger le on le.ledger_id = ve.ledger_id "
			+ "Where CONCAT(IfNull(ve.vendor_id,''),'',IfNull(ve.vendor_name,''),'',IfNull(ve.vendor_gst_no,''),'',"
			+ "IfNull(ve.vendor_bank_account_holder_name,''),'',IfNull(ve.vendor_bank_name,''),'',"
			+ "IfNull(ve.vendor_type,''),'',IfNull(ve.created_by,''),'',IfNull(ve.created_date,''),'',"
			+ "IfNull(ve.nature_of_business,''),'',IfNull(ve.credit_period,''),'',IfNull(ve.created_username,''),'',"
			+ "IfNull(le.ledger_name,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(ve.vendor_id as id,ve.vendor_name as vendor_name,ve.vendor_email as vendor_email,"
			+ "ve.vendor_contact_no as vendor_contact_no,ve.vendor_gst_no as vendor_gst_no,ve.vendor_bank_account_holder_name as vendor_bank_account_holder_name,"
			+ "ve.vendor_bank_name as vendor_bank_name,ve.bank_branch as bank_branch,ve.vendor_bank_ifsc_code as vendor_bank_ifsc_code,"
			+ "ve.pin_code as pin_code,ve.pan_number as pan_number,ve.street_name as street_name,ve.account_no as account_no,"
			+ "ve.area as area,ve.vendor_type as vendor_type,ve.created_by as created_by,ve.modified_by as modified_by,ve.state_id as state_id,"
			+ "ve.created_date as created_date,ve.modified_date as modified_date,ve.active as active,ve.country_id as country_id,"
			+ "ve.nature_of_business as nature_of_business,ve.credit_period as credit_period,ve.vendor_city_id as vendor_city_id,"
			+ "ve.vendor_address as vendor_address,ve.created_username as created_username,le.ledger_name as ledger_name,le.ledger_id as ledger_id,"
			+ "vhn.voucher_head_new_id as voucher_head_new_id,vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "ve.modified_username as modified_username,st.name as state_name,ct.name as city_name,co.name as country_name,"
			+ "ve.account_verifier_date as account_verifier_date,ve.verifier_user_id as verifier_user_id,"
			+ "ve.account_verification_status as account_verification_status) From Vendor ve "
			+ "Left join State st on st.id = ve.state_id "
			+ "Left join City ct on ct.id = ve.vendor_city_id "
			+ "Left join Country co on co.id = ve.country_id "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id = ve.voucher_head_new_id "
			+ "Left join Ledger le on le.ledger_id = ve.ledger_id")
	public Page<Object> findAll3(Pageable pageable);
	
//	@Query(value = "Select s.school_id as school_id,s.school_name as school_name,s.school_name_short as school_name_short from schools s "
//			+ "where s.school_id NOT IN (Select veo.school_id from vendor_opening_balance veo where veo.vendor_id=?1) and s.active=true",nativeQuery=true)
//	public List<Map<String, Object>> fetchUnassignedSchoolDetails(Integer vendor_id);
	
	@Query(value = "Select vh.voucher_head  From vendor ve  left join voucher_head_new vh on vh.voucher_head_new_id=ve.voucher_head_new_id where ve.vendor_id=?1  and ve.active=true",nativeQuery=true)
	public String getvendorIdByVendorName(Integer vendor_id);

	@Query(value=" select v from Vendor v where v.vendor_id=:vendorId ")
	public Vendor getVendorById(@Param("vendorId") Integer vendorId);
	
	@Query(value = "Select ve.vendor_id as id, ve.vendor_name as vendor_name, ve.vendor_email as vendor_email,"
			+ "ve.vendor_contact_no as vendor_contact_no,"
			+ "ve.vendor_bank_account_holder_name as vendor_bank_account_holder_name, ve.country_id as country_id,"
			+ "ve.vendor_bank_name as vendor_bank_name, ve.bank_branch as bank_branch, co.name as country_name,"
			+ "ve.voucher_head_id as voucher_head_id,"
			+ "ve.pin_code as pin_code, ve.street_name as street_name, ve.account_no as account_no,"
			+ "ve.area as area, ve.vendor_type as vendor_type, ve.created_by as created_by, ve.modified_by as modified_by,"
			+ "ve.created_date as created_date, ve.modified_date as modified_date, ve.active as active,"
			+ "ve.nature_of_business as nature_of_business, ve.credit_period as credit_period, ve.voucher_head_new_id as voucher_head_new_id,"
			+ "vhn.voucher_head as voucher_head, vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "ve.vendor_address as vendor_address, ve.created_username as created_username, le.ledger_name as ledger_name,"
			+ "ve.modified_username as modified_username, st.name as state_name, ct.name as city_name,"
			+ "ve.account_verifier_date as account_verifier_date, ve.verifier_user_id as verifier_user_id,"
			+ "ve.account_verification_status as account_verification_status, ve.vendor_gst_no as vendor_gst_no,"
			+ "ve.pan_number as pan_number,ve.vendor_gst_no as vendor_gst_no,ve.vendor_bank_ifsc_code as vendor_bank_ifsc_code From Vendor ve "
			+ "Left join State st on st.id = ve.state_id " 
			+ "Left join Country co on co.id = ve.country_id "
			+ "Left join City ct on ct.id = ve.vendor_city_id " 
			+ "Left join Ledger le on le.ledger_id = ve.ledger_id "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id = ve.voucher_head_new_id Where ve.vendor_id=:vendorId ")
	public Map<String, Object> getFullDetailOfVendorById(@Param("vendorId") Integer vendorId);

	@Query(value = "Select new map(v.voucher_head_new_id as voucherHeadNewId,v.vendor_name as vendor_name) from Vendor v "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id = v.voucher_head_new_id "
			+ " Where vhn.is_vendor=true And vhn.active=true And v.active=true ")
	public List<HashMap<String, Object>> getVoucherHeadNewDataFromVendor();

	@Query(value ="Select vendor_name From Vendor ve Where ve.active=true And ve.vendor_id=?1")
	public String getVendorName(Integer vendorId);

	@Query(value = "Select vendor_email From Vendor ve Where ve.active=true And ve.vendor_id=?1")
	String getVendorEmail(Integer vendorId);
}
