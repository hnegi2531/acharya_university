package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.TimeIntervalTypes;

@Repository
@Transactional
public interface TimeIntervalTypesRepository extends JpaRepository<TimeIntervalTypes, Integer>{

	public Boolean existsByIntervalTypeName(String intervalTypeName);
	
	public Boolean existsByIntervalTypeShort(String intervalTypeShort);
	
	@Query(value = "Select tit From TimeIntervalTypes tit  where tit.active=true")
	public List<TimeIntervalTypes> findAll1();
	
	@Modifying
	@Query(value = "update TimeIntervalTypes r set r.active=false where r.intervalTypeId=?1")
	public void updateStd_subjects(Integer id);

	@Modifying
	@Query(value = "update TimeIntervalTypes r set r.active=true where r.intervalTypeId=?1")
	public void updateStd_subjects1(Integer id);
	
	@Query(value ="Select new map(tit.intervalTypeId as id,tit.intervalTypeName as intervalTypeName,tit.intervalTypeShort as intervalTypeShort,"
			+ "tit.remarks as remarks,tit.showBatch as showBatch,tit.allowMultipleStaff as allowMultipleStaff,tit.outside as outside,"
			+ "tit.showSubject as showSubject,tit.showAttendance as showAttendance,tit.createdDate as createdDate,tit.modifiedDate as modifiedDate,"
			+ "tit.createdBy as createdBy,tit.modifiedBy as modifiedBy,tit.active as active,tit.createdUsername as createdUsername,"
			+ "tit.modifiedUsername as modifiedUsername) From TimeIntervalTypes tit "
			+ "Where CONCAT(IfNull(tit.intervalTypeId,''),'',IfNull(tit.intervalTypeName,''),'',IfNull(tit.intervalTypeShort,''),'',"
			+ "IfNull(tit.allowMultipleStaff ,''),'',IfNull(tit.outside,''),'',IfNull(tit.showSubject,''),'',IfNull(tit.showAttendance,''),'',"
			+ "IfNull(tit.createdDate,''),'',IfNull(tit.createdBy,''),'',IfNull(tit.createdUsername,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(tit.intervalTypeId as id,tit.intervalTypeName as intervalTypeName,tit.intervalTypeShort as intervalTypeShort,"
			+ "tit.remarks as remarks,tit.showBatch as showBatch,tit.allowMultipleStaff as allowMultipleStaff,tit.outside as outside,"
			+ "tit.showSubject as showSubject,tit.showAttendance as showAttendance,tit.createdDate as createdDate,tit.modifiedDate as modifiedDate,"
			+ "tit.createdBy as createdBy,tit.modifiedBy as modifiedBy,tit.active as active,tit.createdUsername as createdUsername,"
			+ "tit.modifiedUsername as modifiedUsername) From TimeIntervalTypes tit")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "select tt from TimeIntervalTypes tt where tt.showBatch='no' and tt.active=true")
	public List<TimeIntervalTypes> getTimeIntervalTypesInSectionDropdown();
	
	@Query(value = "select tt from TimeIntervalTypes tt where tt.showBatch='yes' and tt.active=true")
	public List<TimeIntervalTypes> getTimeIntervalTypesInBatchDropdown();
}
