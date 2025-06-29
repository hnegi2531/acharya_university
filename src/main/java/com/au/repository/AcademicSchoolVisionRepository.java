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
import com.au.model.Department;

@Repository
@Transactional
public interface AcademicSchoolVisionRepository extends JpaRepository<AcademicSchoolVision, Integer>{

	@Modifying
	@Query(value = "update AcademicSchoolVision asv set asv.active=false where asv.academicSchoolVisionId=?1")
	public void updateStd_subjects(Integer id);

	@Modifying
	@Query(value = "update AcademicSchoolVision asv set asv.active=true where asv.academicSchoolVisionId=?1")
	public void updateStd_subjects1(Integer id);
	
	@Query(value = "Select new map(asv.academicSchoolVisionId as id,asv.active as active,"
			+ "asv.asvDescription as asvDescription, asv.asvMission as asvMission, asv.asvVision as asvVision,"
			+ "asv.createdBy as createdBy,asv.createdDate as createdDate,asv.createdUsername as createdUsername,"
			+ "asv.schoolId as schoolId, sc.school_name_short as school_name_short) From AcademicSchoolVision asv "
			+ "left join Schools sc on asv.schoolId=sc.school_id "
			+ "Where CONCAT(IfNull(asv.academicSchoolVisionId,''),'',IfNull(asv.createdBy,''),'',IfNull(asv.createdDate,''),'',IfNull(asv.asvMission,''),'',IfNull(asv.asvVision,''),'',IfNull(asv.createdUsername,''),'',IfNull(sc.school_name_short,'')) LIKE %?1% ")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(asv.academicSchoolVisionId as id,asv.active as active,"
			+ "asv.asvDescription as asvDescription, asv.asvMission as asvMission, asv.asvVision as asvVision,"
			+ "asv.createdBy as createdBy,asv.createdDate as createdDate,asv.createdUsername as createdUsername,"
			+ "asv.schoolId as schoolId, sc.school_name_short as school_name_short) From AcademicSchoolVision asv "
			+ "left join Schools sc on asv.schoolId=sc.school_id ")
	public Page<Object> findAll2(Pageable pageable);
	
	
	@Query(value = "SELECT asv from AcademicSchoolVision asv  where asv.active=true")
	public List<AcademicSchoolVision> findAll11();

	
}
