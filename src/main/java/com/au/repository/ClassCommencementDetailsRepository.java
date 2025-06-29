package com.au.repository;

import java.util.Date;
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

import com.au.model.ClassCommencementDetails;


@Repository
@Transactional
public interface ClassCommencementDetailsRepository  extends JpaRepository<ClassCommencementDetails, Integer> {

	@Query(value = "select new map(ccd.class_commencement_details_id as id, ccd.ac_year_id as ac_year_id, ccd.school_id as school_id, "
			+ "ccd.program_specialization_id as program_specialization_id, ccd.year_sem as year_sem, ccd.commencement_id as commencement_id, "
			+ "ccd.remarks as remarks, ccd.to_date as to_date, ccd.from_date as from_date, ccd.created_username as created_username, "
			+ "ccd.modified_username as modified_username, p.program_name as program_name, p.program_short_name as program_short_name, "
			+ "ccd.fromDate_for_fronted_use as fromDate_for_fronted_use, ccd.toDate_for_fronted_use as toDate_for_fronted_use, "
			+ "ps.program_specialization_name as program_specialization_name, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ac.ac_year as ac_year, ac.ac_year_code as ac_year_code, s.school_name as school_name, ctd.commencement_type as commencement_type, "
			+ "s.school_name_short as school_name_short, ccd.created_date as created_date, ccd.modified_date as modified_date, ccd.created_by as created_by, "
			+ "ccd.modified_by as modified_by, ccd.active as active) from ClassCommencementDetails ccd "
			+ "left join CommencementType ctd on ctd.commencement_id=ccd.commencement_id "
			+ "left join Schools s on s.school_id=ccd.school_id "
			+ "left join ProgramSpecilization ps on ps.program_specialization_id=ccd.program_specialization_id "
			+ "left join Program p on p.program_id=ps.program_id "
			+ "left join Academic_year ac on ac.ac_year_id=ccd.ac_year_id "
			+ "where (:ac_year_id is null or ccd.ac_year_id = :ac_year_id) "
			+ "and (:school_id is null or ccd.school_id = :school_id) "
			+ "and (:program_assignment_id is null or ccd.program_assignment_id = :program_assignment_id) "
			+ "and (:program_specialization_id is null or ccd.program_specialization_id = :program_specialization_id) "
			+ "and (:year_sem is null or ccd.year_sem = :year_sem) "
			+ "and CONCAT(IfNull(ccd.class_commencement_details_id,''),'',IfNull(ccd.ac_year_id,''),'',IfNull(ccd.school_id,''),'',IfNull(ccd.program_specialization_id,''),"
			+ "'',IfNull(ccd.created_by,''),'',IfNull(ccd.created_date,'')) LIKE %:keyword%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, @Param("keyword") Object keyword,
													@Param("ac_year_id") Integer ac_year_id, @Param("school_id") Integer school_id,
													@Param("program_assignment_id") Integer program_assignment_id, @Param("program_specialization_id") Integer program_specialization_id, @Param("year_sem") Integer year_sem);

	@Query(value = "select new map(ccd.class_commencement_details_id as id, ccd.ac_year_id as ac_year_id, ccd.school_id as school_id, "
			+ "ccd.program_specialization_id as program_specialization_id, ccd.year_sem as year_sem, ccd.commencement_id as commencement_id, "
			+ "ccd.remarks as remarks, ccd.to_date as to_date, ccd.from_date as from_date, ccd.created_username as created_username, "
			+ "ccd.modified_username as modified_username, p.program_name as program_name, p.program_short_name as program_short_name, "
			+ "ccd.fromDate_for_fronted_use as fromDate_for_fronted_use, ccd.toDate_for_fronted_use as toDate_for_fronted_use, "
			+ "ps.program_specialization_name as program_specialization_name, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ac.ac_year as ac_year, ac.ac_year_code as ac_year_code, s.school_name as school_name, ctd.commencement_type as commencement_type, "
			+ "s.school_name_short as school_name_short, ccd.created_date as created_date, ccd.modified_date as modified_date, ccd.created_by as created_by, "
			+ "ccd.modified_by as modified_by, ccd.active as active) from ClassCommencementDetails ccd "
			+ "left join CommencementType ctd on ctd.commencement_id=ccd.commencement_id "
			+ "left join Schools s on s.school_id=ccd.school_id "
			+ "left join ProgramSpecilization ps on ps.program_specialization_id=ccd.program_specialization_id "
			+ "left join Program p on p.program_id=ps.program_id "
			+ "left join Academic_year ac on ac.ac_year_id=ccd.ac_year_id "
			+ "where (:ac_year_id is null or ccd.ac_year_id = :ac_year_id) "
			+ "and (:school_id is null or ccd.school_id = :school_id) "
			+ "and (:program_assignment_id is null or ccd.program_assignment_id = :program_assignment_id) "
			+ "and (:program_specialization_id is null or ccd.program_specialization_id = :program_specialization_id) "
			+ "and (:year_sem is null or ccd.year_sem = :year_sem) ")
	public Page<Object> getAllSortedData(Pageable pageable, @Param("ac_year_id") Integer ac_year_id, @Param("school_id") Integer school_id,
										 @Param("program_assignment_id") Integer program_assignment_id, @Param("program_specialization_id") Integer program_specialization_id, @Param("year_sem") Integer year_sem);


	@Query(value = "SELECT ccd from ClassCommencementDetails ccd where ccd.active=true")
	public List<ClassCommencementDetails> findAll11();
	
	@Modifying
	@Query(value = "update ClassCommencementDetails ccd set ccd.active=false where ccd.class_commencement_details_id=?1")
	public void updateClassCommencementDetails(Integer id);
	
	@Modifying
	@Query(value = "update ClassCommencementDetails ccd set ccd.active=true where ccd.class_commencement_details_id=?1")
	public void updateClassCommencementDetails1(Integer id);
	
	
	@Query(value = "SELECT ccd.class_commencement_details_id as class_commencement_details_id,"
			+ "date_format(ccd.to_date, '%d-%m-%Y') as to_date,ct.commencement_type as commencement_type,"
			+ "ps.program_specialization_name as program_specialization_name,ccd.year_sem as year_sem,"
			+ "date_format(ccd.from_date, '%d-%m-%Y') as from_date,"
			+ "ps.program_specialization_short_name as program_specialization_short_name from class_commencement_details ccd "
			+ "left join commencement_type ct on ct.commencement_id=ccd.commencement_id "
			+ "left join program_specialization ps on ps.program_specialization_id=ccd.program_specialization_id "
			+ "where ccd.school_id=?1 and ccd.program_specialization_id=?2 and ccd.year_sem=?3 and ccd.active=true",nativeQuery=true)
	public List<Map<String ,Object>> getClassCommencementDetails(Integer school_id, Integer program_specialization_id, Integer year_sem);
	
	
	@Query(value = "SELECT ccd.to_date as to_date,ct.commencement_type as commencement_type,"
			+ "ccd.year_sem as year_sem,"
			+ "ccd.from_date as from_date,ccd.remarks as remarks from class_commencement_details ccd "
			+ "left join commencement_type ct on ct.commencement_id=ccd.commencement_id "
			+ "left join program_specialization ps on ps.program_specialization_id=ccd.program_specialization_id "
			+ "where ccd.school_id=?1 and ccd.program_specialization_id=?2 and ccd.year_sem=?3 and ccd.active=true",nativeQuery=true)
	public List<Map<String ,Object>> getClassCommencementDetailsForStudentTimetable(Integer school_id, Integer program_specialization_id, Integer year_sem);

	@Query(value=" select * from commencement_type c left join  class_commencement_details cc on cc.commencement_id=c.commencement_id where c.commencement_type='Last Date to Pay'  and cc.school_id=:schoolId "
			+ "and cc.program_assignment_id=:programAssignmentId and cc.program_specialization_id=:programSpecializationId and cc.year_sem=:sem  and cc.active=1 order by cc.class_commencement_details_id desc limit 1 ", nativeQuery = true)
	public ClassCommencementDetails getClassCommencementDetailsForStudentLateFee(Integer schoolId, Integer programAssignmentId,Integer programSpecializationId,Integer sem);
	
	
	@Query(value = "SELECT ccd.from_date from ClassCommencementDetails ccd where ccd.ac_year_id=?1 And ccd.program_specialization_id=?2 And ccd.year_sem=?3 And ccd.commencement_id=2 And ccd.active=true")
	public Date getCommencementDateByAcademicYearIdSpecializationAndYearSem(Integer ac_year_id,Integer program_specialization_id,Integer current_year_sem);

	@Query(value = "SELECT ccd from ClassCommencementDetails ccd where ccd.ac_year_id=?1 And ccd.school_id=?2 And ccd.year_sem=?3 And"
			+ " ccd.commencement_id=?4 And ccd.program_specialization_id=?5 And ccd.active=true")
	public ClassCommencementDetails getClassCommencementDetailsForValidatingTimeTable(Integer ac_year, Integer school_id,
			Integer year_sem, Integer commencement_id, Integer program_specialization_id);

	@Query(value = "SELECT Count(ccd) from ClassCommencementDetails ccd where ccd.commencement_id=?1 And ccd.school_id=?2 And ccd.program_specialization_id=?3 And ccd.program_assignment_id=?4 And ccd.ac_year_id=?5 And ccd.year_sem=?6 And ccd.active=true")
	public Integer countOfCommenceDetailsOnSem( Integer commencementId, Integer schoolId ,Integer specializationId, Integer programAssignmentID, Integer acYearId, Integer yearSem);
	
	@Query(value = "SELECT Count(ccd) from ClassCommencementDetails ccd where ccd.commencement_id=?1 And ccd.school_id=?2 And ccd.program_specialization_id=?3 And ccd.program_assignment_id=?4 And ccd.ac_year_id=?5 And ccd.current_year=?6 And ccd.active=true")
	public Integer countOfCommenceDetailsOnYear( Integer commencementId, Integer schoolId,Integer specializationId, Integer programAssignmentID, Integer acYearId, Integer currentYear);

	@Query(value=" select cc.from_date from commencement_type c left join  class_commencement_details cc on cc.commencement_id=c.commencement_id where c.commencement_type='Last Date to Pay'  and cc.school_id=:schoolId "
			+ "and cc.program_assignment_id=:programAssignmentId and cc.program_specialization_id=:programSpecializationId and cc.year_sem=:sem  and cc.active=1 and STR_TO_DATE(cc.from_date, '%Y-%m-%d') >= CURDATE() order by cc.class_commencement_details_id desc limit 1 ", nativeQuery = true)
	public String  getFromDateFromClassCommencementDetails(Integer schoolId, Integer programAssignmentId,Integer programSpecializationId,Integer sem);

}
