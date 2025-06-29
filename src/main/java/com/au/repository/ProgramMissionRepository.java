package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.AcademicSchoolVision;
import com.au.model.ProgramMission;

@Transactional
@Repository
public interface ProgramMissionRepository extends JpaRepository<ProgramMission, Integer>{

	
	@Modifying
	@Query(value = "update ProgramMission pm set pm.active=true where pm.programMissionId=?1")
	public void updateprogram_mission(Integer id);
	
	
	@Modifying
	@Query(value = "update ProgramMission pm set pm.active=true where pm.programMissionId=?1")
	public void updateprogram_mission1(Integer id);
	
	
	@Query(value = "SELECT pm from ProgramMission pm  where pm.active=true")
	public List<ProgramMission> findAll1();

	
	@Query(value = "Select new map(pm.programMissionId as id,pm.active as active,"
			+ "pm.programMissionName as programMissionName, pm.schoolId as schoolId, pm.programId as programId,"
			+ "pm.createdBy as createdBy,pm.createdDate as createdDate,pm.createdUsername as createdUsername,"
			+ "pm.programSpecializationId as programSpecializationId, sc.school_name_short as school_name_short,"
			+ "prs.program_specialization_short_name as program_specialization_short_name,pro.program_short_name as program_short_name) "
			+ "From ProgramMission pm "
			+ "Left join Schools sc on pm.schoolId = sc.school_id "
			+ "Left join Program pro on pm.programId = pro.program_id "
			+ "Left join ProgramSpecilization prs on pm.programSpecializationId = prs.program_specialization_id "
			+ "Where CONCAT(IfNull(pm.programMissionId,''),'',IfNull(pm.createdBy,''),'',IfNull(pm.createdDate,''),'',"
			+ "IfNull(pro.program_short_name,''),'',IfNull(prs.program_specialization_short_name,''),'',IfNull(sc.school_name_short,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	
	@Query(value = "Select new map(pm.programMissionId as id,pm.active as active,"
			+ "pm.programMissionName as programMissionName, pm.schoolId as schoolId, pm.programId as programId,"
			+ "pm.createdBy as createdBy,pm.createdDate as createdDate,pm.createdUsername as createdUsername,"
			+ "pm.programSpecializationId as programSpecializationId, sc.school_name_short as school_name_short,"
			+ "prs.program_specialization_short_name as program_specialization_short_name,pro.program_short_name as program_short_name) "
			+ "From ProgramMission pm "
			+ "Left join Schools sc on pm.schoolId = sc.school_id "
			+ "Left join Program pro on pm.programId = pro.program_id "
			+ "Left join ProgramSpecilization prs on pm.programSpecializationId = prs.program_specialization_id ")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	
	
	
}
