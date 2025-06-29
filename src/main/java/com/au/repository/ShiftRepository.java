package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Shift;

@Transactional
@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {

	@Query(value = "select s.shift_category_id as shift_category_id,s.frontend_use_start_time as frontend_use_start_time,s.frontend_use_end_time as frontend_use_end_time,"
			+ "concat(s.shift_name,'(',s.shift_start_time,'-',s.shift_end_time,')') as shiftName,s.grace_time  as grace_time, s.actual_start_time as actual_start_time from shift s where s.active=true",nativeQuery=true)
	public List<Map<String,Object>> findAll1();

	@Modifying
	@Query(value = "update Shift s set s.active=false where s.shiftCategoryId=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update Shift s set s.active=true where s.shiftCategoryId=?1")
	public void update1(Integer id);
	
	public Boolean existsByShiftName(String shiftName);
	@Query(value = "select case when (count(sh) > 0)  then true else false end from Shift sh where sh.shiftStartTime = :shiftStartTime and sh.shiftEndTime = :shiftEndTime and sh.active = true")
	public Boolean existsByshiftStartTime(String shiftStartTime,String shiftEndTime);
	public Boolean existsByshiftEndTime(String shiftEndTime);
	
	@Query(value = "select new map(sh.shiftCategoryId as id,sh.shiftName as shiftName,sh.shiftStartTime as shiftStartTime,"
			+ "sh.fhPunchIn as fhPunchIn, sh.fhPunchOut as fhPunchOut, sh.shPunchIn as shPunchIn, sh.shPunchOut as shPunchOut,"
			+ "sh.shiftEndTime as shiftEndTime,sh.createdBy as createdBy,sh.modifiedBy as modifiedBy,sh.school_id as school_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "sh.createdDate as createdDate,sh.modifiedDate as modifiedDate,sh.active as active,sh.is_saturday as is_saturday,"
			+ "sh.createdUsername as createdUsername,sh.modifiedUsername as modifiedUsername,sh.active as active, sh.grace_time  as grace_time, sh.actual_start_time as actual_start_time) "
			+ "from Shift sh "
			+ "left join Schools sc on sc.school_id = sh.school_id "
			+ "where CONCAT(IfNull(sh.shiftCategoryId,''),'',IfNull(sh.shiftName,''),'',IfNull(sh.shiftStartTime,''),"
			+ "'',IfNull(sh.shiftEndTime,''),'',IfNull(sh.createdBy,''),'',IfNull(sh.createdDate,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(sh.shiftCategoryId as id,sh.shiftName as shiftName,sh.shiftStartTime as shiftStartTime,"
			+ "sh.fhPunchIn as fhPunchIn, sh.fhPunchOut as fhPunchOut, sh.shPunchIn as shPunchIn, sh.shPunchOut as shPunchOut,"
			+ "sh.shiftEndTime as shiftEndTime,sh.createdBy as createdBy,sh.modifiedBy as modifiedBy,"
			+ "sh.frontend_use_start_time as frontend_use_start_time,sh.is_saturday as is_saturday,sh.school_id as school_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "sh.createdDate as createdDate,sh.modifiedDate as modifiedDate,sh.active as active,sh.frontend_use_end_time as frontend_use_end_time,"
			+ "sh.createdUsername as createdUsername,sh.modifiedUsername as modifiedUsername,sh.active as active, sh.grace_time  as grace_time, sh.actual_start_time as actual_start_time ) "
			+ "from Shift sh left join Schools sc on sc.school_id = sh.school_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "select case when (count(sh) > 0)  then true else false end from Shift sh where sh.shiftCategoryId != ?1 and sh.shiftName = ?2 and sh.active = true")
	public Boolean checkShiftNameForUpdate(Integer shiftCategoryId,String shiftName);
	
	@Query(value = "select case when (count(sh) > 0)  then true else false end from Shift sh where sh.shiftCategoryId != ?1 and sh.shiftStartTime = ?2 and sh.shiftEndTime = ?3 and sh.active = true")
	public Boolean checkshiftTimeForUpdate(Integer shiftCategoryId,String shiftStartTime,String shiftEndTime);
	
	@Query(value = "select s from Shift s where s.shiftCategoryId=?1")
	public Shift getShiftData(Integer shift_category_id);
	
	@Query(" select s from Shift s where s.is_saturday=true")
	public Shift getShiftIdForSaturday();

	@Query(value = "select sh.shift_category_id as id,sh.shift_name as shiftName,sh.shift_start_time as shiftStartTime,"
			+ "sh.shift_end_time as shiftEndTime,sh.created_by as createdBy,sh.modified_by as modifiedBy,"
			+ "sh.frontend_use_start_time as frontend_use_start_time,sh.is_saturday as is_saturday,sh.school_id as school_id,"
			+ "(select group_concat(schools.school_name_short) from schools where position(',' + cast(schools.school_id as char) + ',' in concat(',',sh.school_id,',')) >0) as school_short_name,"
			+ "sh.created_Date as createdDate,sh.modified_Date as modifiedDate,sh.frontend_use_end_time as frontend_use_end_time,"
			+ "sh.created_username as createdUsername,sh.modified_username as modifiedUsername,sh.active as active,  sh.grace_time  as grace_time,s.actual_start_time as actual_start_time "
			+ "from shift sh left join schools sc on sc.school_id = sh.school_id",nativeQuery =  true)
	public List<Map<String, Object>> getShiftDetailData();

	@Query(value = "select case when (count(sh) > 0)  then true else false end from Shift sh where sh.shiftStartTime = :shiftStartTime and sh.shiftEndTime = :shiftEndTime and sh.active = true and sh.school_id=:schoolId")
	public Boolean existsByShiftStartTimeAndSchoolId(String shiftStartTime,String shiftEndTime,String schoolId);
	
	@Query(value = "select case when (count(sh) > 0)  then true else false end from Shift sh where sh.shiftName=:shiftName and sh.school_id=:schoolId")
	public Boolean existsByShiftNameAndSchoolId(String shiftName,String schoolId);

	@Query(value="SELECT COUNT(*) FROM Shift  where shiftCategoryId=?1 And school_id in (?2) and active=true")
	public Integer checkShiftNameWithSchoolId(Integer shiftCategoryId, String school_id);


	@Query(value = "select new map(sh.shiftCategoryId as id,sh.shiftName as shiftName,sh.shiftStartTime as shiftStartTime,"
			+ "sh.shiftEndTime as shiftEndTime,sh.createdBy as createdBy,sh.modifiedBy as modifiedBy,"
			+ "sh.frontend_use_start_time as frontend_use_start_time,sh.is_saturday as is_saturday,sh.school_id as school_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "CONCAT(sh.shiftName, '-' ,substring(sh.shiftStartTime,1,5),'-',substring(sh.shiftEndTime,1,5)) as concateShiftName,"
			+ "sh.createdDate as createdDate,sh.modifiedDate as modifiedDate,sh.active as active,sh.frontend_use_end_time as frontend_use_end_time,"
			+ "sh.createdUsername as createdUsername,sh.modifiedUsername as modifiedUsername,sh.active as active, sh.grace_time  as grace_time, sh.actual_start_time as actual_start_time ) "
			+ "from Shift sh "
			+ "left join Schools sc on sc.school_id = sh.school_id "
			+ "where sh.school_id=?1 And sh.active=true")
	public List<Map<String, Object>> shiftDetailsBasedOnSchoolId(String school_id);

	
}
