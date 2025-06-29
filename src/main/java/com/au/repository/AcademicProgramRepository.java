package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.AcademicProgram;

@Transactional
@Repository
public interface AcademicProgramRepository extends JpaRepository<AcademicProgram, Integer>{
/*
	@Query(value = "select * from academic_program where ac_year_id=?1 and program_id=?2 and active=1",nativeQuery = true)
	public List<AcademicProgram> getNumOfSemAndYearByProgram_IdAndAcYear_Id(Integer ac_year_id,Integer program_id);
	*/
	
	@Query(value = "SELECT count(*) FROM academic_program where ac_year_id=?1 and program_id=?2",nativeQuery = true)
	public Integer getProgram(Integer ac_year_id,Integer  program_id);

    @Query(value="select ap from AcademicProgram ap where ap.active=true")
	public List<AcademicProgram> findAll1();

   @Query(value="select new map(ap.academic_program_id as id,ap.ac_year_id as ac_year_id,"
   		+ "ap.program_id as program_id,ap.created_by as created_by,ap.modified_by as modified_by,"
   		+ "ap.created_username as created_username,ap.modified_username as modified_username,"
   		+ "ap.created_date as created_date,ap.modified_date as modified_date,ap.number_of_years as number_of_years,"
   		+ "ap.number_of_semester as number_of_semester,ap.active as active,p.program_name as program_name,ac.ac_year as ac_year) "
   		+ "from AcademicProgram ap left join Program p on ap.program_id=p.program_id "
   		+ "left join Academic_year ac on ap.ac_year_id=ac.ac_year_id "
   		+ "Where CONCAT(IfNull(ac.ac_year_id,''),'',IfNull(ac.created_by,''),'',IfNull(ac.created_date,''),'',IfNull(p.program_name,'')) LIKE %?1% ")
	public Page<Object> findAll11(Pageable pageable, Object keyword);
   
   @Query(value="select new map(ap.academic_program_id as id,ap.ac_year_id as ac_year_id,"
	   		+ "ap.program_id as program_id,ap.created_by as created_by,ap.modified_by as modified_by,"
	   		+ "ap.created_username as created_username,ap.modified_username as modified_username,"
	   		+ "ap.created_date as created_date,ap.modified_date as modified_date,ap.number_of_years as number_of_years,"
	   		+ "ap.number_of_semester as number_of_semester,ap.active as active,p.program_name as program_name,ac.ac_year as ac_year) "
	   		+ "from AcademicProgram ap left join Program p on ap.program_id=p.program_id left join Academic_year ac on ap.ac_year_id=ac.ac_year_id")
		public Page<Object> findAll12(Pageable pageable);
   
    @Modifying
	@Query(value = "update AcademicProgram ap set ap.active=false where ap.academic_program_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update AcademicProgram ap set ap.active=true where ap.academic_program_id=?1")
	public void update1(Integer id);
	
}
