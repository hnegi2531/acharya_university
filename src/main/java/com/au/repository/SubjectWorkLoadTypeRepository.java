package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.SubjectWorkLoadType;

@Transactional
@Repository
public interface SubjectWorkLoadTypeRepository extends JpaRepository<SubjectWorkLoadType, Integer>{

	@Query(value = "SELECT * FROM subject_workload_type where subject_id=?1 and  subject_work_load_type_id=?2",nativeQuery = true)
	public SubjectWorkLoadType getSubjectWorkLoadTypeBySubjectIdAndworkLoadId(Integer subjectId,Integer subject_work_load_type_id);

	
	@Query(value = "SELECT * FROM subject_workload_type where subject_id=?1",nativeQuery = true)
	public List<SubjectWorkLoadType> getSubjectWorkLoadTypeBySubjectId(Integer subjectId);
	
	@Query(value = "select new map(swlt.subjectWorkLoadTypeId as id,swlt.academicWorkLoadTypeId as academicWorkLoadTypeId,"
			+ "swlt.subjectId as subjectId,swlt.subjetAssignId as subjetAssignId,swlt.createdBy as createdBy,swlt.modifiedBy as modifiedBy,"
			+ "swlt.createdDate as createdDate,swlt.modifiedDate as modifiedDate,swlt.active as active,"
			+ "swlt.createdUsername as createdUsername,swlt.modifiedUsername as modifiedUsername,swlt.hours as hours) "
			+ "from SubjectWorkLoadType swlt "
			+ "where CONCAT(IfNull(swlt.subjectWorkLoadTypeId,''),'',IfNull(swlt.academicWorkLoadTypeId,''),'',IfNull(swlt.subjectId,''),"
			+ "'',IfNull(swlt.subjetAssignId,''),'',IfNull(swlt.hours,''),'',IfNull(swlt.createdBy,''),'',IfNull(swlt.createdDate,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(swlt.subjectWorkLoadTypeId as id,swlt.academicWorkLoadTypeId as academicWorkLoadTypeId,"
			+ "swlt.subjectId as subjectId,swlt.subjetAssignId as subjetAssignId,swlt.createdBy as createdBy,swlt.modifiedBy as modifiedBy,"
			+ "swlt.createdDate as createdDate,swlt.modifiedDate as modifiedDate,swlt.active as active,"
			+ "swlt.createdUsername as createdUsername,swlt.modifiedUsername as modifiedUsername,swlt.hours as hours) "
			+ "from SubjectWorkLoadType swlt")
	public Page<Object> getAllSortedData(Pageable pageable);
}
