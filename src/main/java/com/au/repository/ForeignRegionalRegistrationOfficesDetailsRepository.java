package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.ForeignRegionalRegistrationOfficesDetails;

@Repository
@Transactional
public interface ForeignRegionalRegistrationOfficesDetailsRepository extends JpaRepository<ForeignRegionalRegistrationOfficesDetails , Integer>{
	
	@Query(value =  "select frrod from ForeignRegionalRegistrationOfficesDetails frrod where frrod.active=true" )
	public List<ForeignRegionalRegistrationOfficesDetails> findAll1();
	
	@Query(value ="Select new map(frrod.frrod_id as id,frrod.student_id as student_id,frrod.surname as surname,frrod.passport_no as passport_no,"
			+ "frrod.place_of_birth as place_of_birth,frrod.passport_issue_place as passport_issue_place,frrod.passport_issue_date as passport_issue_date,"
			+ "frrod.passport_expiry_date as passport_expiry_date,frrod.visa_no as visa_no,frrod.visa_type as visa_type,frrod.type_of_entry as type_of_entry,"
			+ "frrod.place_of_visa_issue as place_of_visa_issue,frrod.visa_issue_date as visa_issue_date,frrod.visa_expiry_date as visa_expiry_date,"
			+ "frrod.port_of_departure as port_of_departure,frrod.port_of_arrival as port_of_arrival,frrod.fsis_no as fsis_no,frrod.immigration_date as immigration_date,"
			+ "frrod.rp_no as rp_no,frrod.rp_issue_date as rp_issue_date,frrod.rp_expiry_date as rp_expiry_date,frrod.issue_by as issue_by,frrod.reported_to_india as reported_to_india,"
			+ "frrod.reported_on as reported_on,frrod.passport_copy_document_path as passport_copy_document_path,frrod.visa_copy_document_path as visa_copy_document_path,"
			+ "frrod.residential_permit_copy_document_path as residential_permit_copy_document_path,frrod.aiu_equivalence_document_path as aiu_equivalence_document_path,"
			+ "frrod.remarks as remarks,frrod.joining_year_id as joining_year_id,frrod.attachment_path as attachment_path,frrod.p_v_remarks as p_v_remarks,"
			+ "frrod.p_v_active as p_v_active,frrod.created_date as created_date,frrod.modified_date as modified_date,frrod.created_by as created_by,"
			+ "frrod.created_username as created_username,frrod.active as active ) From ForeignRegionalRegistrationOfficesDetails frrod "
			+ "Where CONCAT(IfNull(frrod.frrod_id,''),'',IfNull(frrod.passport_issue_place,''),'',IfNull(frrod.visa_no,''),'',IfNull(frrod.visa_type,'')) LIKE %?1% ")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(frrod.frrod_id as id,frrod.student_id as student_id,frrod.surname as surname,frrod.passport_no as passport_no,"
			+ "frrod.place_of_birth as place_of_birth,frrod.passport_issue_place as passport_issue_place,frrod.passport_issue_date as passport_issue_date,"
			+ "frrod.passport_expiry_date as passport_expiry_date,frrod.visa_no as visa_no,frrod.visa_type as visa_type,frrod.type_of_entry as type_of_entry,"
			+ "frrod.place_of_visa_issue as place_of_visa_issue,frrod.visa_issue_date as visa_issue_date,frrod.visa_expiry_date as visa_expiry_date,"
			+ "frrod.port_of_departure as port_of_departure,frrod.port_of_arrival as port_of_arrival,frrod.fsis_no as fsis_no,frrod.immigration_date as immigration_date,"
			+ "frrod.rp_no as rp_no,frrod.rp_issue_date as rp_issue_date,frrod.rp_expiry_date as rp_expiry_date,frrod.issue_by as issue_by,frrod.reported_to_india as reported_to_india,"
			+ "frrod.reported_on as reported_on,frrod.passport_copy_document_path as passport_copy_document_path,frrod.visa_copy_document_path as visa_copy_document_path,"
			+ "frrod.residential_permit_copy_document_path as residential_permit_copy_document_path,frrod.aiu_equivalence_document_path as aiu_equivalence_document_path,"
			+ "frrod.remarks as remarks,frrod.joining_year_id as joining_year_id,frrod.attachment_path as attachment_path,frrod.p_v_remarks as p_v_remarks,"
			+ "frrod.p_v_active as p_v_active,frrod.created_date as created_date,frrod.modified_date as modified_date,frrod.created_by as created_by,"
			+ "frrod.created_username as created_username,frrod.active as active) From ForeignRegionalRegistrationOfficesDetails frrod ")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = "update ForeignRegionalRegistrationOfficesDetails frrod set frrod.active=false where frrod.frrod_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update ForeignRegionalRegistrationOfficesDetails frrod set frrod.active=true where frrod.frrod_id=?1")
	public void activate(Integer id);

	@Query(value ="Select frrod From ForeignRegionalRegistrationOfficesDetails frrod "
			+ "where frrod.passport_expiry_date = ?1 "
			+ "OR "
			+ "frrod.visa_expiry_date = ?1 ")
	public List<ForeignRegionalRegistrationOfficesDetails> getFrroData(String expectedExpiryDate);
	
	@Query(value ="Select frrod.frrod_id as id,frrod.student_id as student_id,frrod.surname as surname,"
			+ "frrod.place_of_birth as place_of_birth,frrod.passport_issue_place as passport_issue_place,"
			+ "frrod.visa_type as visa_type,frrod.type_of_entry as type_of_entry, frrod.place_of_visa_issue as place_of_visa_issue,"
			+ "frrod.port_of_departure as port_of_departure,frrod.port_of_arrival as port_of_arrival,frrod.fsis_no as fsis_no,frrod.immigration_date as immigration_date,"
			+ "frrod.rp_no as rp_no,frrod.rp_issue_date as rp_issue_date,frrod.rp_expiry_date as rp_expiry_date,frrod.issue_by as issue_by,frrod.reported_to_india as reported_to_india,"
			+ "frrod.reported_on as reported_on,frrod.passport_copy_document_path as passport_copy_document_path,frrod.visa_copy_document_path as visa_copy_document_path,"
			+ "frrod.residential_permit_copy_document_path as residential_permit_copy_document_path,frrod.aiu_equivalence_document_path as aiu_equivalence_document_path,"
			+ "frrod.remarks as remarks,frrod.joining_year_id as joining_year_id,frrod.attachment_path as attachment_path,frrod.p_v_remarks as p_v_remarks,"
			+ "frrod.p_v_active as p_v_active,frrod.created_date as created_date,frrod.modified_date as modified_date,frrod.created_by as created_by,"
			+ "frrod.created_username as created_username,frrod.active as active,"
			+ "frrod.passport_no as passport_no, frrod.passport_issue_date as passport_issue_date, frrod.passport_expiry_date as passport_expiry_date,"
			+ "frrod.visa_no as visa_no, frrod.visa_issue_date as visa_issue_date, frrod.visa_expiry_date as visa_expiry_date,"
			+ "sd.student_name as student_name, sd.auid as auid, sd.usn as usn,sd.mobile as mobile, sd.acharya_email as studentAcharyaEmail, sc.school_id as school_id, "
			+ "ua.email as principalEmail, admin.email as administratorEmail, ed.email as proctorEmail "
			+ "From foreign_regional_registration_offices_details frrod "
			+ "left join student_details sd on sd.student_id=frrod.student_id "
			+ "left join proctor_student_assignment psa on psa.student_id=sd.student_id "
			+ "left join employee_details ed on ed.emp_id=psa.emp_id "
			+ "left join schools sc on sc.school_id=sd.school_id "
			+ "left join administration_email_ids admin on admin.school_id=sc.school_id "
			+ "left join user_details ua on ua.id=sc.user_id_for_email " 
			+ "where frrod.passport_expiry_date = ?1 "
			+ "OR "
			+ "frrod.visa_expiry_date = ?1 ",nativeQuery=true)
	public List<Map<String, Object>> getFrroStudentData(String expectedExpiryDate);

	
	@Query(value ="Select frrod.frrod_id as id,frrod.student_id as student_id,frrod.surname as surname,"
			+ "frrod.place_of_birth as place_of_birth,frrod.passport_issue_place as passport_issue_place,"
			+ "frrod.visa_type as visa_type,frrod.type_of_entry as type_of_entry, frrod.place_of_visa_issue as place_of_visa_issue,"
			+ "frrod.port_of_departure as port_of_departure,frrod.port_of_arrival as port_of_arrival,frrod.fsis_no as fsis_no,frrod.immigration_date as immigration_date,"
			+ "frrod.rp_no as rp_no,frrod.rp_issue_date as rp_issue_date,frrod.rp_expiry_date as rp_expiry_date,frrod.issue_by as issue_by,frrod.reported_to_india as reported_to_india,"
			+ "frrod.reported_on as reported_on,frrod.passport_copy_document_path as passport_copy_document_path,frrod.visa_copy_document_path as visa_copy_document_path,"
			+ "frrod.residential_permit_copy_document_path as residential_permit_copy_document_path,frrod.aiu_equivalence_document_path as aiu_equivalence_document_path,"
			+ "frrod.remarks as remarks,frrod.joining_year_id as joining_year_id,frrod.attachment_path as attachment_path,frrod.p_v_remarks as p_v_remarks,"
			+ "frrod.p_v_active as p_v_active,frrod.created_date as created_date,frrod.modified_date as modified_date,frrod.created_by as created_by,"
			+ "frrod.created_username as created_username,frrod.active as active,"
			+ "frrod.passport_no as passport_no, frrod.passport_issue_date as passport_issue_date, frrod.passport_expiry_date as passport_expiry_date,"
			+ "frrod.visa_no as visa_no, frrod.visa_issue_date as visa_issue_date, frrod.visa_expiry_date as visa_expiry_date,"
			+ "sd.student_name as student_name, sd.auid as auid, sd.usn as usn,sd.mobile as mobile, sd.acharya_email as studentAcharyaEmail, sc.school_id as school_id, "
			+ "ua.email as principalEmail, admin.email as administratorEmail, ed.email as proctorEmail "
			+ "From foreign_regional_registration_offices_details frrod "
			+ "left join student_details sd on sd.student_id=frrod.student_id "
			+ "left join proctor_student_assignment psa on psa.student_id=sd.student_id "
			+ "left join employee_details ed on ed.emp_id=psa.emp_id "
			+ "left join schools sc on sc.school_id=sd.school_id "
			+ "left join administration_email_ids admin on admin.school_id=sc.school_id "
			+ "left join user_details ua on ua.id=sc.user_id_for_email "
			+ "where psa.emp_id=?1 and frrod.student_id IN ?2",nativeQuery=true )
	public List<Map<String, Object>> getFrroStudentDataByStudentIds(Integer emp_id, Set<Integer> listStudentIds);

	
//	public List<HashMap<String, Object>> getDataByExpiryDate(String expiryDate);
}
