package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.AcademicsProgramVision;

@Repository
@Transactional
public interface AcademicsProgramVisionRepository extends JpaRepository<AcademicsProgramVision,Integer>{

	@Modifying
	@Query(value = "update AcademicsProgramVision apv set apv.active=false where apv.academicsProgramVisionId=?1")
	public void updateStd_subjects(Integer id);

	@Modifying
	@Query(value = "update AcademicsProgramVision apv set apv.active=true where apv.academicsProgramVisionId=?1")
	public void updateStd_subjects1(Integer id);
	
	@Query(value = "Select new map(apv.academicsProgramVisionId as id,apv.apvMission as apvMission,apv.apvVision as apvVision,"
			+ "apv.apvDescription as apvDescription,apv.createdBy as createdBy,apv.createdDate as createdDate,"
			+ "apv.createdUsername as createdUsername,apv.active as active,sc.school_name_short as school_name_short,sc.school_name as school_name,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short ) From AcademicsProgramVision apv "
			+ "Left join Schools sc on apv.schoolId = sc.school_id "
			+ "Left join Department d on apv.dept_id = d.dept_id "
			+ "Where CONCAT(IfNull(apv.academicsProgramVisionId,''),'',IfNull(apv.createdBy,''),'',IfNull(apv.createdDate,''),'',IfNull(d.dept_name_short,''),'',IfNull(apv.apvVision,''),'',IfNull(sc.school_name_short,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value= "Select new map(apv.academicsProgramVisionId as id,apv.apvMission as apvMission,apv.apvVision as apvVision,"
			+ "apv.apvDescription as apvDescription,apv.createdBy as createdBy,apv.createdDate as createdDate,"
			+ "apv.createdUsername as createdUsername,apv.active as active,sc.school_name_short as school_name_short,sc.school_name as school_name,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short ) From AcademicsProgramVision apv "
			+ "Left join Schools sc on apv.schoolId = sc.school_id "
			+ "Left join Department d on apv.dept_id = d.dept_id ")
	public Page<Object> findAll2(Pageable pageable);
	
	
	@Query(value = "SELECT apv from AcademicsProgramVision apv  where apv.active=true")
	public List<AcademicsProgramVision> findAll11();

	
	
}
