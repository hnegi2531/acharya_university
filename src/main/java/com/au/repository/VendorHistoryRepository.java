package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Vendor;
import com.au.model.VendorHistory;

@Repository
@Transactional
public interface VendorHistoryRepository extends JpaRepository<VendorHistory , Integer> {

	
	@Query(value ="Select vh From VendorHistory vh Where vh.active=true")
	public List<VendorHistory> findAll1();
	
	
	@Query(value = "Select new map(vh.VendorHistory_id as id,vh.vendor_id as vendor_id,vh.vendor_name as vendor_name,vh.vendor_email as vendor_email,"
			+ "vh.vendor_contact_no as vendor_contact_no,vh.vendor_gst_no as vendor_gst_no,vh.vendor_bank_account_holder_name as vendor_bank_account_holder_name,"
			+ "vh.vendor_bank_name as vendor_bank_name,vh.bank_branch as bank_branch,vh.vendor_bank_ifsc_code as vendor_bank_ifsc_code,"
			+ "vh.pin_code as pin_code,vh.pan_number as pan_number,vh.street_name as street_name,vh.account_no as account_no,"
			+ "vh.area as area,vh.vendor_type as vendor_type,vh.created_by as created_by,vh.modified_by as modified_by,"
			+ "vh.created_date as created_date,vh.modified_date as modified_date,vh.active as active,"
			+ "vh.nature_of_business as nature_of_business,vh.credit_period as credit_period,vh.vendor_attachment_id as vendor_attachment_id,"
			+ "vh.vendor_attachment_path as vendor_attachment_path,vh.vendor_attachment_file_name as vendor_attachment_file_name,"
			+ "vh.vendor_attachement_type as vendor_attachement_type,"
			+ "vh.vendor_address as vendor_address,vh.created_username as created_username,le.ledger_name as ledger_name,"
			+ "vh.modified_username as modified_username,st.name as state_name,ct.name as city_name,"
			+ "vh.account_verifier_date as account_verifier_date,vh.verifier_user_id as verifier_user_id,"
			+ "vh.account_verification_status as account_verification_status) From VendorHistory vh "
			+ "Left join State st on st.id = vh.state_id "
			+ "Left join City ct on ct.id = vh.vendor_city_id "
			+ "Left join Ledger le on le.ledger_id = vh.ledger_id "
			+ "Where CONCAT(IfNull(vh.vendor_id,''),'',IfNull(vh.vendor_name,''),'',IfNull(vh.vendor_gst_no,''),'',"
			+ "IfNull(vh.vendor_bank_account_holder_name,''),'',IfNull(vh.vendor_bank_name,''),'',"
			+ "IfNull(vh.vendor_type,''),'',IfNull(vh.created_by,''),'',IfNull(vh.created_date,''),'',"
			+ "IfNull(vh.nature_of_business,''),'',IfNull(vh.credit_period,''),'',IfNull(vh.created_username,''),'',"
			+ "IfNull(le.ledger_name,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(vh.VendorHistory_id as id,vh.vendor_id as vendor_id,vh.vendor_name as vendor_name,vh.vendor_email as vendor_email,"
			+ "vh.vendor_contact_no as vendor_contact_no,vh.vendor_gst_no as vendor_gst_no,vh.vendor_bank_account_holder_name as vendor_bank_account_holder_name,"
			+ "vh.vendor_bank_name as vendor_bank_name,vh.bank_branch as bank_branch,vh.vendor_bank_ifsc_code as vendor_bank_ifsc_code,"
			+ "vh.pin_code as pin_code,vh.pan_number as pan_number,vh.street_name as street_name,vh.account_no as account_no,"
			+ "vh.area as area,vh.vendor_type as vendor_type,vh.created_by as created_by,vh.modified_by as modified_by,"
			+ "vh.created_date as created_date,vh.modified_date as modified_date,vh.active as active,"
			+ "vh.nature_of_business as nature_of_business,vh.credit_period as credit_period,vh.vendor_attachment_id as vendor_attachment_id,"
			+ "vh.vendor_attachment_path as vendor_attachment_path,vh.vendor_attachment_file_name as vendor_attachment_file_name,"
			+ "vh.vendor_attachement_type as vendor_attachement_type,"
			+ "vh.vendor_address as vendor_address,vh.created_username as created_username,le.ledger_name as ledger_name,"
			+ "vh.modified_username as modified_username,st.name as state_name,ct.name as city_name,"
			+ "vh.account_verifier_date as account_verifier_date,vh.verifier_user_id as verifier_user_id,"
			+ "vh.account_verification_status as account_verification_status) From VendorHistory vh "
			+ "Left join State st on st.id = vh.state_id "
			+ "Left join City ct on ct.id = vh.vendor_city_id "
			+ "Left join Ledger le on le.ledger_id = vh.ledger_id ")
	public Page<Object> findAll3(Pageable pageable);
}
