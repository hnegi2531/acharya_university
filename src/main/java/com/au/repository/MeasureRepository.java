package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.au.model.Measure;

import com.au.dto.UomDTO;

@Transactional
@Repository
public interface MeasureRepository extends JpaRepository<Measure , Integer>{
	
	
	@Query(value = "select ms from Measure ms where ms.active=1")
	public List<Measure> findAll1();
	
	@Modifying
	@Query(value = "update Measure ms set ms.active=0 where ms.measure_id=?1 ")
	public void update(Integer measure_id);
	
	@Modifying
	@Query(value = "update Measure ms set ms.active=1 where ms.measure_id=?1 ")
	public void update1(Integer measure_id);
	
	@Query(value = "select new map(m.measure_id as id,m.measure_name as measure_name,m.measure_short_name as measure_short_name,"
			+ "m.created_by as created_by,m.modified_by as modified_by,"
			+ "m.created_date as created_date,m.modified_date as modified_date,m.active as active,"
			+ "m.created_username as created_username,m.modified_username as modified_username) from Measure m "
			+ "where CONCAT(IfNull(m.measure_id,''),'',IfNull(m.measure_name,''),'',IfNull(m.measure_short_name,''),"
			+ "'',IfNull(m.created_by,''),'',IfNull(m.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(m.measure_id as id,m.measure_name as measure_name,m.measure_short_name as measure_short_name,"
			+ "m.created_by as created_by,m.modified_by as modified_by,"
			+ "m.created_date as created_date,m.modified_date as modified_date,m.active as active,"
			+ "m.created_username as created_username,m.modified_username as modified_username) from Measure m")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "select count(*) from Measure m where m.measure_name=?1")
	public Integer getCountMeasureName(String measure_name);
	
	@Query(value = "select count(*) from Measure m where m.measure_short_name=?1")
	public Integer getCountMeasureShortName(String measure_name);
	
	@Query(value=" select new com.au.dto.UomDTO( m.measure_name as uomName, m.measure_short_name as uomName) from Measure m where m.measure_id=:measureId  ",nativeQuery = false)
	public UomDTO getUomByMeasureId(@Param("measureId") Integer measureId);

	@Query(value = "select m.measure_short_name from Measure m where m.active=1 and m.measure_id = :measureId")
	String findByMeasureShortNameMeasureId(Integer measureId);
}
