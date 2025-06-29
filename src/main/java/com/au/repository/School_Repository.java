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

import com.au.model.Offer;
import com.au.model.Schools;

@Repository
@Transactional
public interface School_Repository  extends JpaRepository<Schools, Integer>{
	
	@Query(value = "select sc.school_id as id,sc.school_name as school_name,sc.pricipals_email as pricipals_email,"
			+ "sc.school_name_short as school_name_short,sc.created_by as created_by,sc.modified_by as modified_by,sc.principal_sign As principal_sign,"
			+ "sc.created_date as created_date,sc.modified_date as modified_date,sc.active as active,sc.web_status as web_status,"
			+ "sc.created_username as created_username,sc.modified_username as modified_username,sc.job_type_id as job_type_id,"
			+ "sc.org_id as org_id,sc.week_off as week_off,sc.ref_no as ref_no,sc.academic_status as academic_status,"
			+ "sc.school_color as school_color,sc.org_name as org_name,sc.display_name as display_name,"
			+ "(select group_concat(job_type.job_short_name) from job_type where position(',' + cast(job_type.job_type_id as char) + ',' in concat(',',sc.job_type_id,',')) >0) as job_type_name,"
			+ "sc.priority as priority,ud.email as email "
			+ "from schools sc Left join user_details ud on ud.id=sc.user_id_for_email "
			+ "where CONCAT(IfNull(sc.school_id,''),'',IfNull(sc.school_name,''),'',IfNull(sc.web_status,''),'',IfNull(sc.job_type_id,''),"
			+ "'',IfNull(sc.school_name_short,''),'',IfNull(sc.created_by,''),'',IfNull(sc.created_date,''),'',IfNull(sc.org_name,''),'',IfNull(job_type_name,'')) LIKE %?1%",nativeQuery =  true)
	public List<Map<String, Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
//	@Query(value = "select sc.school_id as id,sc.school_name as school_name,"
//			+ "sc.school_name_short as school_name_short,sc.created_by as created_by,sc.modified_by as modified_by,"
//			+ "sc.created_date as created_date,sc.modified_date as modified_date,sc.active as active,sc.web_status as web_status,"
//			+ "sc.created_username as created_username,sc.modified_username as modified_username,sc.job_type as job_type,"
//			+ "sc.org_id as org_id,sc.week_off as week_off,sc.ref_no as ref_no,sc.email_id as email_id,sc.mobile_no as mobile_no,"
//			+ "sc.school_color as school_color,sc.org_name as org_name,"
//			+ "(select group_concat(jt.job_type) from job_type jt where position(','+cast(jt.job_type_id as char)+',' in concat(',',sc.job_type,',')) >0) as job_type_name,"
//			+ "sc.priority as priority,sc.job_type_name as job_type_name "
//			+ "from schools sc",nativeQuery =  true)
//	public List<Map<String, Object>> getAllSortedData(Pageable pageable);
	
	@Query(value = "select sc.school_id as id,sc.school_name as school_name,sc.pricipals_email as pricipals_email,"
			+ "sc.school_name_short as school_name_short,sc.created_by as created_by,sc.modified_by as modified_by,sc.principal_sign As principal_sign,"
			+ "sc.created_date as created_date,sc.modified_date as modified_date,sc.active as active,sc.web_status as web_status,"
			+ "sc.created_username as created_username,sc.modified_username as modified_username,sc.job_type_id as job_type_id,"
			+ "sc.org_id as org_id,sc.week_off as week_off,sc.ref_no as ref_no,sc.academic_status as academic_status,"
			+ "sc.school_color as school_color,sc.org_name as org_name,sc.display_name as display_name,"
			+ "(select group_concat(job_type.job_short_name) from job_type where position(',' + cast(job_type.job_type_id as char) + ',' in concat(',',sc.job_type_id,',')) >0) as job_short_name,"
			+ "sc.priority as priority,ud.email as email "
			+ "from schools sc Left join user_details ud on ud.id=sc.user_id_for_email",nativeQuery =  true)
	public List<Map<String, Object>> getAllSortedData(Pageable pageable);
	
	@Query(value = "select sc.school_id as id,sc.school_name as school_name,sc.pricipals_email as pricipals_email,"
			+ "sc.school_name_short as school_name_short,sc.created_by as created_by,sc.modified_by as modified_by,sc.principal_sign As principal_sign,"
			+ "sc.created_date as created_date,sc.modified_date as modified_date,sc.active as active,sc.web_status as web_status,"
			+ "sc.created_username as created_username,sc.modified_username as modified_username,sc.job_type_id as job_type_id,"
			+ "sc.org_id as org_id,sc.week_off as week_off,sc.ref_no as ref_no,sc.academic_status as academic_status,"
			+ "sc.school_color as school_color,sc.org_name as org_name,sc.display_name as display_name,o.org_type As org_type,o.org_name As orgName,"
			+ "(select group_concat(job_type.job_short_name) from job_type where position(',' + cast(job_type.job_type_id as char) + ',' in concat(',',sc.job_type_id,',')) >0) as job_short_name,"
			+ "sc.priority as priority,ud.email as email "
			+ "from schools sc "
			+ "Left join organization o on o.org_id=sc.org_id "
			+ "Left join user_details ud on ud.id=sc.user_id_for_email",nativeQuery =  true)
	public List<Map<String, Object>> getSchoolDetails();

	@Query(value = "SELECT * FROM schools where active=true",nativeQuery =  true)
	public	List<Schools>  findAll12();
	
	
	@Query(value = "SELECT school_name FROM schools where school_id=?1",nativeQuery = true)
	public String getSchoolName(Integer school_id);
	
	
	@Query(value = "SELECT count(*) FROM schools where school_name=?1 and active=true",nativeQuery = true)
	public Integer getschoolCount(String school_name);

	@Modifying
	@Query(value = "update Schools sc set sc.active=false where sc.school_id=?1")
	public void updateSchool(Integer id);
	
	@Modifying
	@Query(value = "update Schools sc set sc.active=true where sc.school_id=?1")
	public void updateSchool1(Integer id);

	@Query(value = "SELECT * FROM schools where school_name=?1 and school_name_short=?2 and active=true", nativeQuery = true)
	public List<Schools> getSchoolNames(String school_name,String school_name_short);

	@Query(value = "select * from schools where priority=?1 and active=true",nativeQuery = true)
	public List<Schools> getpriority(Integer priority);

	@Query(value = "SELECT count(*) FROM Schools s where s.school_name_short=?1 and s.active=true")
	public Integer getschoolshortCount(String school_name_short);
	
	@Query(value = "SELECT count(*) FROM Schools s where s.priority=?1 and s.active=true")
	public Integer getPriorityCount(Integer priority);
	
	@Query(value = "select case when (count(s.school_name) > 0)  then true else false end from Schools s where s.school_name =?1 and s.school_id !=?2 and s.active = true")
	public Boolean validationForSchoolName(String school_name,Integer school_id);
	
	@Query(value = "select case when (count(s.school_name_short) > 0)  then true else false end from Schools s where s.school_name_short =?1 and s.school_id !=?2 and s.active = true")
	public Boolean validationForSchoolNameShort(String school_name_short,Integer school_id);
	
	@Query(value = "select case when (count(s.ref_no) > 0)  then true else false end from Schools s where s.ref_no =?1 and s.school_id !=?2 and s.active = true")
	public Boolean validationForReferenceNumber(String ref_no,Integer school_id);
	
	@Query(value = "select s.school_name from Schools s where s.school_id=?1 and s.active=true")
	public Offer getOffer(Integer school_id);
	
	@Query(value ="Select GROUP_CONCAT(sc.school_name_short) From schools sc where sc.school_id in (:school_ids)",nativeQuery = true)
	public String getSchoolShortNameCommaSeperated(List<Integer> school_ids);
	
	@Query(value ="Select sc.school_name_short From schools sc where sc.school_id =?1 and sc.active =true",nativeQuery = true)
	public String getSchoolShortName(Integer school_id);
	
	@Query(value = "select sc.pricipals_email from Schools sc where sc.school_id=?1 and sc.active=true")
	public String getSchoolPrincipalEmail(Integer school_id);


	@Query(value="select e.school_id from EmployeeDetails e where e.contract_empcode=:empcode and e.active=true")
	Integer getSchoolId(String empcode);

	@Query(value="select e.school_id from EmployeeDetails e where e.empcode=:empcode and e.active=true")
	Integer getSchoolIdByJavaEmpcode(String empcode);

	@Query(value = "select sh.school_name_short from Schools sh "
			+ "Where sh.school_id in (?1) And sh.active=true ")
	public List<String> getAllReportedStudents(List<Integer> school_ids);
	
	
	@Query(value="select s.school_id from Schools s where s.user_id_for_email=:user_id")
	public Integer getSchoolIdByEmail(Integer user_id);

	@Query(value ="Select sc.school_name_short From schools sc "
			+ "left join acharya_erp.employee_details ed on ed.school_id=sc.school_id "
			+ "where ed.emp_id =?1 and sc.active =true And ed.active=true",nativeQuery = true)
	public String getSchoolShortNameForEmployee(Integer emp_id);

	
	@Query(value = "Select sc.school_id from schools sc where sc.school_id not in (?1) and sc.active=true",nativeQuery = true)
	List<Integer> schoolIdsForJournalVoucherCreation(List<Integer> createdSchoolIds);
	
	@Query(value="select new map(s.school_id as institute_id, s.school_name as institute_name,s.school_name_short as institute_name_short) from Schools s where s.active=true")
	public List<HashMap<String,Object>> schoolDetailsForLms();

	@Query(value = "Select sc.school_id as institute_id, sc.school_name as institute_name, sc.school_name_short as institute_short_name from schools sc ",nativeQuery = true)
	public List<Map<String, Object>> getInstitutes();

    @Query(value = "select * from schools where  school_id = ?1 and active = true",nativeQuery = true)
    Schools getSchoolBySchoolId(Integer schoolId);
}
