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
import com.au.model.Section;

@Repository
@Transactional
public interface SectionRepository extends JpaRepository<Section, Integer> {
	
	

	@Query(value = "select h from Section h where h.active=true")
	public List<Section> findAll1();

	@Modifying
	@Query(value = "update Section r set r.active=false where r.section_id=?1")
	public void updateSection(Integer id);

	@Modifying
	@Query(value = "update Section r set r.active=true where r.section_id=?1")
	public void updateSection1(Integer id);

	@Query(value = "SELECT s1.school_name,s1.school_name_short,s.section_id,s.section_name,"
			+ "s.active,s.created_date,s.created_username,s.remarks,s.volume,s.school_id FROM section s"
			+ " left join schools s1 on s.school_id = s1.school_id",nativeQuery = true)
	public List<Map<String, Object>> fetchAllSectionDetails();

	@Query(value = "select * from section where school_id=?1", nativeQuery = true)
	public List<Section> fetchSectionBySchool(Integer school_id);
	
	@Query(value = "select new map(sec.section_id as id,sec.section_name as section_name,"
			+ "sec.school_id as school_id,sec.volume as volume,sec.remarks as remarks,sec.created_by as created_by,sec.modified_by as modified_by,"
			+ "sec.created_date as created_date,sec.modified_date as modified_date,sec.active as active,sc.school_name as school_name,"
			+ "sec.created_username as created_username,sec.modified_username as modified_username,sc.school_name_short as school_name_short) "
			+ "from Section sec left join Schools sc on sec.school_id=sc.school_id "
			+ "where CONCAT(IfNull(sec.section_id,''),'',IfNull(sec.section_name,''),'',"
			+ "'',IfNull(sec.volume,''),'',IfNull(sc.school_name_short,''),'',IfNull(sc.school_name,''),"
			+ "'',IfNull(sec.remarks,''),'',IfNull(sec.school_id,''),'',IfNull(sec.created_by,''),'',IfNull(sec.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(sec.section_id as id,sec.section_name as section_name,"
			+ "sec.school_id as school_id,sec.volume as volume,sec.remarks as remarks,sec.created_by as created_by,sec.modified_by as modified_by,"
			+ "sec.created_date as created_date,sec.modified_date as modified_date,sec.active as active,sc.school_name as school_name,"
			+ "sec.created_username as created_username,sec.modified_username as modified_username,sc.school_name_short as school_name_short) "
			+ "from Section sec left join Schools sc on sec.school_id=sc.school_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Query(value = "SELECT count(*) FROM Section sec where sec.section_name=?1 and sec.school_id=?2 and sec.active=true")
	public Integer countSectionName(String section_name,Integer school_id);
	
	@Query(value = "SELECT count(*) FROM Section sec where sec.section_id != ?1 and sec.section_name=?2 and sec.school_id = ?3 and sec.active=true")
	public Integer countOfSectionNameOnSchoolId(Integer section_id,String section_name,Integer school_id);

	@Query(value = "select s.section_name from section s where s.section_id=?1", nativeQuery = true)
	public String getSectionNamebySectionId(Integer section_id);
	
	@Query(value = "select s from section s where s.section_name=:sectionName ", nativeQuery = true)
	public Section findBySectionName(String sectionName);
	
}
