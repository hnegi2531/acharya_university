package com.au.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.StdSubjects;

@Repository
@Transactional
public interface StdSubjectsRepository extends JpaRepository<StdSubjects, Integer> {
	
	@Query(value = "select * from subjects s where s.active=true",nativeQuery = true)
	public List<StdSubjects> findAll1();
	
	@Modifying
	@Query(value = "update StdSubjects r set r.active=false where r.subjectId=?1")
	public void updateStd_subjects(Integer id);

	@Modifying
	@Query(value = "update StdSubjects r set r.active=true where r.subjectId=?1")
	public void updateStd_subjects1(Integer id);

	public Boolean existsBySubjectName(String subjectName);
	public Boolean existsBySubjectNameShort(String subjectNameShort);
	public Boolean existsBySubjectCode(String subjectCode);

	 List<StdSubjects> findBysubjectCodeNotIn(Collection<Integer> subjectCode);

	@Query(value = "SELECT * FROM subjects where subject_code NOT IN (?1)",nativeQuery = true)
	public List<StdSubjects> findBysubjectCodeNotIn(String subjectCode);
	
	@Query(value = "select new map(ss.subjectId as id,ss.subjectName as subjectName,"
			+ "ss.subjectNameShort as subjectNameShort,ss.subjectCode as subjectCode,ss.createdBy as createdBy,ss.modifiedBy as modifiedBy,"
			+ "ss.createdDate as createdDate,ss.modifiedDate as modifiedDate,ss.active as active,"
			+ "ss.createdUsername as createdUsername,ss.modifiedUsername as modifiedUsername) "
			+ "from StdSubjects ss "
			+ "where CONCAT(IfNull(ss.subjectId,''),'',IfNull(ss.subjectName,''),'',IfNull(ss.subjectNameShort,''),"
			+ "'',IfNull(ss.subjectCode,''),'',IfNull(ss.createdBy,''),'',IfNull(ss.createdDate,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(ss.subjectId as id,ss.subjectName as subjectName,"
			+ "ss.subjectNameShort as subjectNameShort,ss.subjectCode as subjectCode,ss.createdBy as createdBy,ss.modifiedBy as modifiedBy,"
			+ "ss.createdDate as createdDate,ss.modifiedDate as modifiedDate,ss.active as active,"
			+ "ss.createdUsername as createdUsername,ss.modifiedUsername as modifiedUsername) "
			+ "from StdSubjects ss")
	public Page<Object> getAllSortedData(Pageable pageable);
	
}
