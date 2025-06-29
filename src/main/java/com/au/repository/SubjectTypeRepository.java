package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.SubjectType;

@Repository
@Transactional
public interface SubjectTypeRepository extends JpaRepository<SubjectType, Integer>{

	public Boolean existsBySubjectTypeName(String subjectTypeName);
	
	@Modifying
	@Query(value = "update SubjectType r set r.active=false where r.subjectTypeId=?1")
	public void updateStd_subjects(Integer id);

	@Modifying
	@Query(value = "update SubjectType r set r.active=true where r.subjectTypeId=?1")
	public void updateStd_subjects1(Integer id);

	@Query(value = "select u from SubjectType u where u.active=true")
	public List<SubjectType> findAll1();
	
	@Query(value = "select new map(st.subjectTypeId as id,st.subjectTypeName as subjectTypeName,st.academicType as academicType,"
			+ "st.createdUsername as createdUsername,st.modifiedUsername as modifiedUsername,"
			+ "st.createdDate as createdDate,st.modifiedDate as modifiedDate,st.createdBy as createdBy,"
			+ "st.modifiedBy as modifiedBy,st.active as active) from SubjectType st "
			+ "where CONCAT(IfNull(st.subjectTypeId,''),'',IfNull(st.subjectTypeName,''),'',IfNull(st.academicType,''),"
			+ "'',IfNull(st.createdUsername,''),'',IfNull(st.createdBy,''),'',IfNull(st.createdDate,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(st.subjectTypeId as id,st.subjectTypeName as subjectTypeName,st.academicType as academicType,"
			+ "st.createdUsername as createdUsername,st.modifiedUsername as modifiedUsername,"
			+ "st.createdDate as createdDate,st.modifiedDate as modifiedDate,st.createdBy as createdBy,"
			+ "st.modifiedBy as modifiedBy,st.active as active) from SubjectType st")
	public Page<Object> getAllSortedData(Pageable pageable);

}
