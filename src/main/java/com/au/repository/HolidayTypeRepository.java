package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.HolidayType;

@Repository
@Transactional
public interface HolidayTypeRepository extends JpaRepository<HolidayType, Integer> {
	public Boolean existsByholidayType(String holidayType);
	public Boolean existsByholidayTypeShort(String holidayTypeShort);
	
//	@Query(value = "SELECT * FROM holiday_type where holiday_type=\"Declared Holiday\" and active=true",nativeQuery = true)
//	public HolidayType fetchHolidayType();
	
	@Query(value = "SELECT * FROM holiday_type where leave_id=?1 and active=true",nativeQuery = true)
	public HolidayType fetchHolidayType(Integer leave_id);	
	
	@Query(value = "select h from HolidayType h where h.active=true")
	public List<HolidayType> findAll1();

	@Modifying
	@Query(value = "update HolidayType h set h.active=false where h.holidayTypeId=?1")
	public void updateHolidayType(Integer id);

	@Modifying
	@Query(value = "update HolidayType h set h.active=true where h.holidayTypeId=?1")
	public void updateHolidayType1(Integer id);

	
	
	@Query(value = "select new map(hd.holidayTypeId as id,hd.holidayType as holidayType,hd.holidayTypeShort as holidayTypeShort,"
			+ "hd.holiday as holiday,hd.createdBy as createdBy,hd.modifiedBy as modifiedBy,"
			+ "hd.createdDate as createdDate,hd.modifiedDate as modifiedDate,hd.active as active,"
			+ "hd.createdUsername as createdUsername,hd.modifiedUsername as modifiedUsername) "
			+ "from HolidayType hd "
			+ "where CONCAT(IfNull(hd.holidayTypeId,''),'',IfNull(hd.holidayType,''),'',IfNull(hd.holidayTypeShort,''),"
			+ "'',IfNull(hd.holiday,''),'',IfNull(hd.createdBy,''),'',IfNull(hd.createdDate,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(hd.holidayTypeId as id,hd.holidayType as holidayType,hd.holidayTypeShort as holidayTypeShort,"
			+ "hd.holiday as holiday,hd.createdBy as createdBy,hd.modifiedBy as modifiedBy,"
			+ "hd.createdDate as createdDate,hd.modifiedDate as modifiedDate,hd.active as active,"
			+ "hd.createdUsername as createdUsername,hd.modifiedUsername as modifiedUsername) "
			+ "from HolidayType hd")
	public Page<Object> getAllSortedData(Pageable pageable);
}
