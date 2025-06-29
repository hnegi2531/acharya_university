package com.au.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.OtherFeeTemplate;

@Repository
public interface OtherFeeTemplateRepository extends JpaRepository<OtherFeeTemplate, Integer> {

	boolean existsByUniformNumberAndFeetypeAndAcYearIdAndSchoolIdAndProgramId(String uniformNumber, String feetype,
			Integer acYearId, Integer schoolId, Integer programId);

	@Query(value = "SELECT "
	        + "    oft.other_fee_template_id AS otherFeeTemplateId, "
	        + "    oft.feetype AS feetype, "
	        + "    oft.uniform_number AS uniformNumber, "
	        + "    ac.ac_year AS acYear, "
	        + "    s.school_name_short AS institute, "
	        + "    p.program_short_name AS program, "
	        + "    ps.program_specialization_short_name AS programSpecilization, "
	        + "    oft.created_date AS createdDate, "
	        + "    oft.created_by AS createdBy, "
	        + "    oft.modified_by AS modifiedBy, "
	        + "    oft.modified_date AS modifiedDate, "
	        + "    ofd.total AS total, "
	        + "    pa.number_of_semester AS numberOfSemester, "
	        + "    oft.active AS active "
	        + "FROM "
	        + "    other_fee_template oft "
	        + "LEFT JOIN "
	        + "    academic_year ac ON ac.ac_year_id = oft.ac_year_id "
	        + "LEFT JOIN "
	        + "    schools s ON s.school_id = oft.school_id "
	        + "LEFT JOIN "
	        + "    program p ON p.program_id = oft.program_id "
	        + "LEFT JOIN "
	        + "    program_specialization ps ON ps.program_specialization_id = oft.program_specialization_id "
	        + "LEFT JOIN other_fee_details ofd ON ofd.template_id = oft.other_fee_template_id "
	        + "LEFT JOIN "
	        + "    program_assignment pa ON pa.program_id = p.program_id "
	      , nativeQuery = true)
	Page<Map<String, Object>> getOtherFeeDetails(Pageable pageable);


	
	@Query("SELECT COUNT(o) FROM OtherFeeTemplate o WHERE o.uniformNumber = ?1 AND o.feetype = ?2 AND o.acYearId = ?3 AND o.schoolId = ?4 AND o.programId = ?5 AND o.active =true")
	Integer existsByUniformNumberAndFeetypeAndAcYearIdAndSchoolIdAndProgramIdAndActive(
	    String uniformNumber,
	    String feetype,
	    Integer acYearId,
	    Integer schoolId,
	    Integer programId,
	    boolean active
	);

	@Query("SELECT COUNT(o) FROM OtherFeeTemplate o WHERE  o.feetype =?1 AND o.acYearId =?2 AND o.schoolId =?3 AND o.programId =?4 AND o.active =true")
	Integer existsByUniformNumberAndFeetypeAndAcYearIdAndSchoolIdAndProgramId1(
	    String feetype,
	    Integer acYearId,
	    Integer schoolId,
	    Integer programId,
	    boolean active
	);


	@Query("SELECT o.programId FROM OtherFeeTemplate o WHERE o.active =true AND o.otherFeeTemplateId=?1")
    Integer getProgramIdByOtherFeeTemplateId(Integer otherFeeTemplateId);
}
