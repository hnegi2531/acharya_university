package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.FeeAdmissionCategory;

@Repository
@Transactional
public interface FeeAdmissionCategoryRepository extends JpaRepository<FeeAdmissionCategory, Integer> {

	@Query(value = "select fac from FeeAdmissionCategory fac where fac.active=true")
	public List<FeeAdmissionCategory> findAll1();

	@Modifying
	@Query(value = "update FeeAdmissionCategory fac set fac.active=false where fac.fee_admission_category_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update FeeAdmissionCategory fac set fac.active=true where fac.fee_admission_category_id=?1")
	public void update1(Integer id);

	@Query(value = "SELECT COUNT(*) FROM fee_admission_category where fee_admission_category_type=?1 and active=true", nativeQuery = true)
	public Integer getFeeAdmissionCategoryCount(String fee_admission_category_type);

	@Query(value = "SELECT COUNT(*) FROM fee_admission_category where fee_admission_category_short_name=?1 and active=true", nativeQuery = true)
	public Integer getFeeAdmissionCategoryShortNameCount(String fee_admission_category_short_name);

	@Query(value = "Select new map(fac.fee_admission_category_id as id,fac.year_sem as year_sem,"
			+ "fac.fee_admission_category_type as fee_admission_category_type,fac.is_regular as is_regular,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,"
			+ "fac.created_date as created_date,fac.created_by as created_by,fac.active as active,fac.is_check as is_check,"
			+ "fac.is_sub_category_applicable as is_sub_category_applicable,fac.created_username as created_username) From FeeAdmissionCategory fac "
			+ "Where CONCAT(IfNull(fac.fee_admission_category_id,''),'',IfNull(fac.fee_admission_category_type,''),'',IfNull(fac.fee_admission_category_short_name,''),'',IfNull(fac.created_date,''),'',IfNull(fac.created_by,''),'',IfNull(fac.created_username,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);

	@Query(value = "Select new map(fac.fee_admission_category_id as id,fac.year_sem as year_sem,"
			+ "fac.fee_admission_category_type as fee_admission_category_type,fac.is_regular as is_regular,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,"
			+ "fac.created_date as created_date,fac.created_by as created_by,fac.active as active,fac.is_check as is_check,"
			+ "fac.is_sub_category_applicable as is_sub_category_applicable,fac.created_username as created_username) From FeeAdmissionCategory fac ")
	public Page<Object> findAll2(Pageable pageable);

	@Query(value = "select (select count(*) from student_details where active=1 and auid is not null and fee_admission_category_id=a.fee_admission_category_id and ac_year_id=b.ac_year_id) as admitted,"
			+ "a.fee_admission_category_id,sum(intake_permit) As intake,"
			+ "sum(intake_permit) - (select count(*) from student_details where active=1 and auid is not null and fee_admission_category_id=a.fee_admission_category_id and ac_year_id=b.ac_year_id) As vacant,"
			+ "fc.fee_admission_category_id as feeAdmissionId,fc.fee_admission_category_type as feeAdmissionType, b.ac_year_id as acYearId  from intake_permit a  "
			+ " inner join intake_assignment b on a.intake_id=b.intake_id  "
			+ "inner join fee_admission_category fc on fc.fee_admission_category_id=a.fee_admission_category_id  "
			+ "where b.ac_year_id=?1 and a.active=true and b.active=true  group by a.fee_admission_category_id", nativeQuery = true)
	public List<Map<String, Object>> getAdmissionCategoryReportAcademicYearWise(Integer acYearId);

	@Query(value = " select sd.fee_admission_category_id as feeAdmissionId, fc.fee_admission_category_short_name  as feeAdmissionType ,s.school_id as schoolId ,s.school_name_short as schoolName, COALESCE(COUNT(sd.student_id), 0) AS admitted, "
			+ "    COALESCE(i.actual_intake, 0) AS intake, "
			+ "    COALESCE(i.actual_intake, 0) - COALESCE(COUNT(sd.student_id), 0) AS vacant "
			+ " from  student_details sd left join schools s on s.school_id=sd.school_id "
			+ " left join intake_assignment i on sd.school_id=i.school_id and sd.program_id=i.program_id and sd.ac_year_id=i.ac_year_id and sd.program_specialization_id=i.program_specialization_id "
			+ " left join fee_admission_category fc on fc.fee_admission_category_id=sd.fee_admission_category_id "
			+ " where sd.fee_admission_category_id=:feeAdmissionId group by s.school_id   ", nativeQuery = true)
	public List<Map<String, Object>> getAdmissionCategoryReportInstituteWiseWithCategory(Integer feeAdmissionId);
	
	@Query(value = "select (select count(*) from student_details where active=1 and auid is not null "
				+ "and fee_admission_category_id=a.fee_admission_category_id And school_id=b.school_id and ac_year_id=b.ac_year_id ) as admitted,"
			+ "a.fee_admission_category_id,sum(intake_permit) As intake, "
			+ "sum(intake_permit) - (select count(*) from student_details where active=1 and auid is not null "
				+ "and fee_admission_category_id=a.fee_admission_category_id And school_id=b.school_id and ac_year_id=b.ac_year_id ) As vacant,"
				+ "fc.fee_admission_category_id As feeAdmissionId,fc.fee_admission_category_type As feeAdmissionType,"
			+ "s.school_id as schoolId ,s.school_name_short as schoolName "
			+ "from intake_permit a   "
			+ "inner join intake_assignment b on a.intake_id=b.intake_id   "
			+ " left join schools s on s.school_id=b.school_id   "
			+ "inner join fee_admission_category fc on fc.fee_admission_category_id=a.fee_admission_category_id   "
			+ "where b.ac_year_id=?1  And a.fee_admission_category_id=?2 and a.active=true and b.active=true "
			+ "group by s.school_id ,fc.fee_admission_category_type , b.ac_year_id,a.fee_admission_category_id", nativeQuery = true)
	public List<Map<String, Object>> getAdmissionCategoryReportSchoolWiseWithCategory(Integer acYearId, Integer feeAdmissionId);
	
	@Query(value = " select s.school_id as schoolId ,s.school_name_short as schoolName,sd.ac_year_id as acYearId, COALESCE(COUNT(sd.student_id), 0) AS admitted, "
			+ "    COALESCE(i.actual_intake, 0) AS intake, "
			+ "    COALESCE(i.actual_intake, 0) - COALESCE(COUNT(sd.student_id), 0) AS vacant from student_details sd  "
			+ " left join  intake_assignment i on sd.school_id=i.school_id and sd.program_id=i.program_id and sd.ac_year_id=i.ac_year_id and sd.program_specialization_id=i.program_specialization_id "
			+ " left join schools s on s.school_id=sd.school_id " + " where sd.ac_year_id=:acYearId "
			+ "  group by s.school_id order by  s.school_id ", nativeQuery = true)
	public List<Map<String, Object>> getAdmissionCategoryReportInstituteWise(Integer acYearId);

	@Query(value = "select (select count(*) from student_details where active=1 and auid is not null  "
			+ "and fee_admission_category_id=a.fee_admission_category_id And school_id=b.school_id and ac_year_id=b.ac_year_id  "
			+ "And program_id = b.program_id And program_specialization_id = b.program_specialization_id) as admitted, "
			+ "a.fee_admission_category_id,sum(intake_permit) As intake,  "
			+ "sum(intake_permit) - (select count(*) from student_details where active=1 and auid is not null  "
			+ "and fee_admission_category_id=a.fee_admission_category_id And school_id=b.school_id and ac_year_id=b.ac_year_id  "
			+ "And program_id = b.program_id And program_specialization_id = b.program_specialization_id) As vacant, "
			+ "s.school_id as schoolId ,s.school_name_short as schoolName , p.program_short_name As program_short_name, "
			+ "fc.fee_admission_category_short_name As fee_admission_category_short_name, "
			+ "p.program_id As program_id,ps.program_specialization_id As program_specialization_id,ps.program_specialization_short_name As program_specialization_short_name "
			+ "from intake_permit a "
			+ "inner join intake_assignment b on a.intake_id=b.intake_id "
			+ " left join program p on p.program_id=b.program_id "
			+ "  left join program_specialization ps on ps.program_specialization_id=b.program_specialization_id "
			+ " left join schools s on s.school_id=b.school_id "
			+ "inner join fee_admission_category fc on fc.fee_admission_category_id=a.fee_admission_category_id    "
			+ "where b.ac_year_id=?2 And a.fee_admission_category_id=?1 And s.school_id=?3 and a.active=true and b.active=true "
			+ "group by s.school_id ,fc.fee_admission_category_type , b.ac_year_id,a.fee_admission_category_id,p.program_id,ps.program_specialization_id ", nativeQuery = true)
	public List<Map<String, Object>> getAdmissionCategoryReportSpecializationWise(Integer feeAdmissionId, Integer acYearId, Integer schoolId);

	
	@Query(value = " SELECT (SELECT COUNT(*)  FROM student_details WHERE active = 1 AND auid IS NOT NULL AND ac_year_id = b.ac_year_id AND school_id = s.school_id) AS admitted,"
			+ "SUM(intake_permit) AS intake,s.school_id As schoolId,"
			+ "SUM(intake_permit) - (SELECT COUNT(*) FROM student_details WHERE active = 1 AND auid IS NOT NULL AND ac_year_id = b.ac_year_id AND school_id = s.school_id) AS vacant,"
			+ "b.ac_year_id AS acYearId,s.school_name_short AS schoolName "
			+ "FROM intake_permit a "
			+ "INNER JOIN intake_assignment b ON a.intake_id = b.intake_id "
			+ "LEFT JOIN schools s ON s.school_id = b.school_id "
			+ "WHERE b.ac_year_id = ?1 "
			+ "  AND a.active = TRUE "
			+ "  AND b.active = TRUE "
			+ "GROUP BY s.school_id, b.ac_year_id ", nativeQuery = true)
	public List<Map<String, Object>> getAdmissionCategoryTotalReportAcademicYearWise(Integer acYearId);

	
	@Query(value = " SELECT (SELECT COUNT(*)  FROM student_details WHERE active = 1 AND auid IS NOT NULL AND ac_year_id = b.ac_year_id AND school_id = s.school_id "
			+ "And program_id = b.program_id And program_specialization_id = b.program_specialization_id And fee_admission_category_id=a.fee_admission_category_id) AS admitted,"
			+ "SUM(intake_permit) AS intake,"
			+ "SUM(intake_permit) - (SELECT COUNT(*) FROM student_details WHERE active = 1 AND auid IS NOT NULL AND ac_year_id = b.ac_year_id AND school_id = s.school_id "
			+ "And program_id = b.program_id And program_specialization_id = b.program_specialization_id And fee_admission_category_id=a.fee_admission_category_id) AS vacant,"
			+ "b.ac_year_id AS acYearId,s.school_id AS schoolId,s.school_name_short AS schoolName ,"
			+ " p.program_short_name As program_short_name,"
			+ "fc.fee_admission_category_short_name As fee_admission_category_short_name,"
			+ "p.program_id As program_id,ps.program_specialization_id As program_specialization_id,ps.program_specialization_short_name As program_specialization_short_name "
			+ "FROM intake_permit a "
			+ "INNER JOIN intake_assignment b ON a.intake_id = b.intake_id "
			+ " left join program p on p.program_id=b.program_id "
			+ "  left join program_specialization ps on ps.program_specialization_id=b.program_specialization_id "
			+ " left join schools s on s.school_id=b.school_id "
			+ "inner join fee_admission_category fc on fc.fee_admission_category_id=a.fee_admission_category_id "
			+ "WHERE b.ac_year_id =?1 "
			+ "  AND a.active = TRUE "
			+ "  AND b.active = TRUE "
			+ "	 AND intake_permit !=0 "
			+ "GROUP BY s.school_id, b.ac_year_id,p.program_id,ps.program_specialization_id , a.fee_admission_category_id ", nativeQuery = true)
	public List<Map<String, Object>> getAdmissionCategoryProgramWiseTotalReportAcademicYearWise(Integer acYearId);

	@Query(value = " SELECT (SELECT COUNT(*)  FROM student_details WHERE active = 1 AND auid IS NOT NULL AND ac_year_id = b.ac_year_id AND school_id = s.school_id "
			+ "And program_id = b.program_id And program_specialization_id = b.program_specialization_id And fee_admission_category_id=a.fee_admission_category_id ) AS admitted,"
			+ "SUM(intake_permit) AS intake,"
			+ "SUM(intake_permit) - (SELECT COUNT(*) FROM student_details WHERE active = 1 AND auid IS NOT NULL AND ac_year_id = b.ac_year_id AND school_id = s.school_id "
			+ "And program_id = b.program_id And program_specialization_id = b.program_specialization_id And fee_admission_category_id=a.fee_admission_category_id) AS vacant,"
			+ "b.ac_year_id AS acYearId,s.school_id AS schoolId,s.school_name_short AS schoolName ,"
			+ "p.program_short_name As program_short_name,p.program_name As program_name,"
			+ "fc.fee_admission_category_short_name As fee_admission_category_short_name,fc.fee_admission_category_type As fee_admission_category_type,"
			+ "p.program_id As program_id,ps.program_specialization_id As program_specialization_id,ps.program_specialization_short_name As program_specialization_short_name "
			+ "FROM intake_permit a "
			+ "INNER JOIN intake_assignment b ON a.intake_id = b.intake_id "
			+ "left join program p on p.program_id=b.program_id "
			+ "left join program_specialization ps on ps.program_specialization_id=b.program_specialization_id "
			+ "left join schools s on s.school_id=b.school_id "
			+ "inner join fee_admission_category fc on fc.fee_admission_category_id=a.fee_admission_category_id "
			+ "WHERE b.ac_year_id =?1 And s.school_id=?2 "
			+ "AND a.active = TRUE "
			+ "AND b.active = TRUE "
			+ "GROUP BY s.school_id, b.ac_year_id,p.program_id,ps.program_specialization_id,a.fee_admission_category_id ", nativeQuery = true)
	public List<Map<String, Object>> getAdmissionCategoryTotalReportAcademicYearAndSchoolWise(Integer acYearId, Integer schoolId);
}
