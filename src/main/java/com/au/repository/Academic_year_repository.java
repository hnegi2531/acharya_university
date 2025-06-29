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
import com.au.model.Academic_year;


@Transactional
@Repository
public interface Academic_year_repository extends JpaRepository<Academic_year, Integer>  {
	public static final String FIND_PROJECTS = "SELECT * FROM academic_year where current_year>=year(now())-1 and active=1";
	
	

	@Query(value = FIND_PROJECTS, nativeQuery = true)
	public List<Academic_year> findByAcYearId();

	@Query(value = "select count(*) from academic_year where active=1 ", nativeQuery = true)
	public Integer countRecords();

	@Query(value = "SELECT * FROM academic_year where current_year>=year(now())-1 and active=1", nativeQuery = true)
	public List<Academic_year> findByAcYearId1();

	@Query(value = "SELECT ac_year FROM academic_year where current_year=year(now()) and active=1", nativeQuery = true)
	public String findByAcYearId2();

	@Modifying
	@Query(value = "update Academic_year ay set ay.active=false where ay.ac_year_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update Academic_year ay set ay.active=true where ay.ac_year_id=?1")
	public void update1(Integer id);

	@Query(value = "select * from academic_year where active=true ORDER BY ac_year DESC",nativeQuery = true)
	public List<Academic_year> findAll1();
	
	@Query(value = "select * from academic_year where active=true And ac_year_id >=6 "
			+ "ORDER BY ac_year DESC",nativeQuery = true)
	public List<Academic_year> academicYearGT();
	
	@Query(value = "SELECT ac_year FROM academic_year where ac_year_id=?1",nativeQuery = true)
	public String fetchAcademicYear(Integer ac_year_id);
	
	@Query(value = "Select new map(ay.ac_year_id as id,ay.ac_year as ac_year,ay.ac_year_code as ac_year_code,"
			+ "ay.created_username as created_username,ay.created_date as created_date,ay.created_by as created_by,"
			+ "ay.active as active,ay.current_year as current_year) From Academic_year ay "
			+ "Where CONCAT(IfNull(ay.ac_year_id,''),'',IfNull(ay.created_by,''),'',IfNull(ay.created_date,''),'',IfNull(ay.created_username,''),'',IfNull(ay.ac_year,''),'',IfNull(ay.current_year,'')) LIKE %?1%")
	public Page<Object> findAll11(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(ay.ac_year_id as id,ay.ac_year as ac_year,ay.ac_year_code as ac_year_code,"
			+ "ay.created_username as created_username,ay.created_date as created_date,ay.created_by as created_by,"
			+ "ay.active as active,ay.current_year as current_year) From Academic_year ay ")
	public Page<Object> findAll12(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM Academic_year ay where ay.ac_year=?1 and ay.active=true")
	public Integer countOfAcYear(String ac_year);
	
	@Query(value = "SELECT count(*) FROM Academic_year ay where ay.current_year=?1 and ay.active=true")
	public Integer countOfCurrentYear(Integer current_year);
	
	@Query(value = "SELECT ay.ac_year FROM Academic_year ay where ay.ac_year_id=?1 and ay.active=true")
	public String getAcademicYear(Integer ac_year_id);
	
	@Query(value = "SELECT ay.current_year FROM Academic_year ay where ay.ac_year_id=?1 and ay.active=true")
	public Integer getCurrentYear(Integer ac_year_id);
	
	@Query(value ="Select ay1.ac_year_id From academic_year ay1 "
			+ "where ay1.current_year =(Select ay2.current_year + 1 From academic_year ay2 where ay2.ac_year_id=?1) and ay1.active=true",nativeQuery=true)
	public  Integer getNextAcademicYearId(Integer ac_year_id);
	
	@Query(value ="SELECT ay.ac_year_id FROM academic_year ay where ay.current_year = year(now()) and active=true",nativeQuery = true)
	public Integer getCurrentAcademicYearId();

	@Query(value = "SELECT * FROM academic_year ac where ac.ac_year_id=?1 and ac.active = true ", nativeQuery = true)
	public Academic_year findByAcademicId(Integer academicId);

	@Query(value = "SELECT ac.current_year FROM academic_year ac where ac.ac_year_id=?1 ", nativeQuery = true)
	public Integer findAcademicYearById(Integer academicId);

	@Query(value = "SELECT current_year FROM academic_year where current_year=year(now()) and active=1", nativeQuery = true)
	public Integer getCurrentYear();
	
	@Query(value = "Select ay.ac_year_id as ac_year_id,ay.ac_year as ac_year,ay.current_year as current_year from academic_year ay where active=true ",nativeQuery = true)
	public List<Map<String, Object>> activeAcademicYear();
	
	@Query(value = "Select ay.ac_year_id as ac_year_id,ay.ac_year as ac_year,ay.current_year as current_year from academic_year ay where ay.active=true Order By ay.ac_year_id Desc Limit 5 ",nativeQuery = true)
	public List<Map<String, Object>> latestAcademicYear();

	@Query(value = "Select ay.ac_year_id as ac_year_id,ay.ac_year as ac_year,ay.current_year as current_year from academic_year ay where ay.active=true Order By ay.ac_year_id Desc ",nativeQuery = true)
	public List<Map<String, Object>> getAcYears();

	@Query(value = "SELECT ac_year_id FROM academic_year where current_year=year(DATE_FORMAT(STR_TO_DATE(?1,'%d-%m-%Y'), '%Y-%m-%d')) and active=1", nativeQuery = true)
	public Integer getCurrentAcademicYearId(String date_of_admission);

	@Query(value = "select ac_year_id  FROM academic_year where active=1 order by ac_year_id desc limit 1 ", nativeQuery = true)
	public Integer getLatestAcYearId();

}
