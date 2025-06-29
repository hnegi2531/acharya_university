package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.IntakePermit;

@Transactional
@Repository
public interface IntakePermitRepository extends JpaRepository<IntakePermit , Integer>{
	
	@Query(value = "SELECT ip FROM IntakePermit ip where ip.active=true")
	public List<IntakePermit> findAll1();
	
	@Query(value ="Select new map(ip.intake_permit_id as id,ip.intake_id as intake_id,ip.fee_admission_sub_category_id as fee_admission_sub_category_id,"
			+ "ip.intake_permit as intake_permit,ip.created_date as created_date,ip.created_by as created_by,"
			+ "ip.created_username as created_username,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name,fac.fee_admission_category_type as fee_admission_category_type,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_id as fee_admission_category_id) From IntakePermit ip "
			+ "Inner Join IntakeAssignment ia on ia.intake_id=ip.intake_id "
			+ "Left Join ProgramSpecilization ps On ps.program_specialization_id=ia.program_specialization_id "
			+ "Left Join FeeAdmissionCategory fac on fac.fee_admission_category_id=ip.fee_admission_category_id "
			+ "Left Join FeeAdmissionSubCategory fasc on fasc.fee_admission_sub_category_id=ip.fee_admission_sub_category_id "
			+ "Where CONCAT(IfNull(ip.intake_permit_id,''),'',IfNull(ip.intake_permit,''),'',IfNull(ip.created_date,''),'',"
			+ "IfNull(ip.created_username,''),'',IfNull(ps.program_specialization_short_name,''),'',IfNull(fasc.fee_admission_sub_category_short_name,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(ip.intake_permit_id as id,ip.intake_id as intake_id,ip.fee_admission_sub_category_id as fee_admission_sub_category_id,"
			+ "ip.intake_permit as intake_permit,ip.created_date as created_date,ip.created_by as created_by,"
			+ "ip.created_username as created_username,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name,fac.fee_admission_category_type as fee_admission_category_type,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_id as fee_admission_category_id) From IntakePermit ip "
			+ "Inner Join IntakeAssignment ia on ia.intake_id=ip.intake_id "
			+ "Left Join ProgramSpecilization ps On ps.program_specialization_id=ia.program_specialization_id "
			+ "Left Join FeeAdmissionCategory fac on fac.fee_admission_category_id=ip.fee_admission_category_id "
			+ "Left Join FeeAdmissionSubCategory fasc on fasc.fee_admission_sub_category_id=ip.fee_admission_sub_category_id ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value ="Select new map(ip.intake_permit_id as id,ip.intake_id as intake_id,ip.fee_admission_sub_category_id as fee_admission_sub_category_id,"
			+ "ip.intake_permit as intake_permit,ip.created_date as created_date,ip.created_by as created_by,fac.year_sem As year_sem,"
			+ "ip.created_username as created_username,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name,fac.fee_admission_category_type as fee_admission_category_type,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_id as fee_admission_category_id) From IntakePermit ip "
			+ "Inner Join IntakeAssignment ia on ia.intake_id=ip.intake_id "
			+ "Left Join ProgramSpecilization ps On ps.program_specialization_id=ia.program_specialization_id "
			+ "Left Join FeeAdmissionCategory fac on fac.fee_admission_category_id=ip.fee_admission_category_id "
			+ "Left Join FeeAdmissionSubCategory fasc on fasc.fee_admission_sub_category_id=ip.fee_admission_sub_category_id "
			+ "where ip.intake_id=?1")
	public List<HashMap<String, Object>> getIntakePermitByIntakeId(Integer intake_id);
	
	@Query(value ="Select new map(ip.intake_permit_id as intake_permit_id,ip.intake_id as intake_id,ip.fee_admission_sub_category_id as fee_admission_sub_category_id,"
			+ "ip.intake_permit as intake_permit,ip.created_date as created_date,ip.created_by as created_by,"
			+ "ip.created_username as created_username,"
			+ "fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name,fac.fee_admission_category_type as fee_admission_category_type,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_id as fee_admission_category_id) From IntakePermit ip "
			+ "Left Join FeeAdmissionCategory fac on fac.fee_admission_category_id=ip.fee_admission_category_id "
			+ "Left Join FeeAdmissionSubCategory fasc on fasc.fee_admission_sub_category_id=ip.fee_admission_sub_category_id "
			+ "where ip.intake_id in ?1 and ip.active=true")
	public List<HashMap<String, Object>> getIntakePermitDetailsForGridView(List<Integer> intake_id);
}
