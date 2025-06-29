package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.StdHostelStandardAccessories;

@Repository
@Transactional
public interface StdHostelStandardAccessoriesRepository extends JpaRepository<StdHostelStandardAccessories, Integer>{

	@Query(value = "select h from StdHostelStandardAccessories h where h.active=true")
	public List<StdHostelStandardAccessories> findAll1();
	
	@Modifying
	@Query(value = "update StdHostelStandardAccessories h set h.active=false where h.standardAccessoriesId=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update StdHostelStandardAccessories h set h.active=true where h.standardAccessoriesId=?1")
	public void update1(Integer id);
	
	@Query(value = "select new map(s.standardAccessoriesId as id,s.standardAccessories as standardAccessories,"
			+ "s.createdBy as createdBy,s.modifiedBy as modifiedBy,"
			+ "s.createdDate as createdDate,s.modifiedDate as modifiedDate,s.active as active,"
			+ "s.createdUsername as createdUsername,s.modifiedUsername as modifiedUsername) from StdHostelStandardAccessories s "
			+ "where CONCAT(IfNull(s.standardAccessoriesId,''),'',IfNull(s.standardAccessories,''),"
			+ "'',IfNull(s.createdBy,''),'',IfNull(s.createdDate,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(s.standardAccessoriesId as id,s.standardAccessories as standardAccessories,"
			+ "s.createdBy as createdBy,s.modifiedBy as modifiedBy,"
			+ "s.createdDate as createdDate,s.modifiedDate as modifiedDate,s.active as active,"
			+ "s.createdUsername as createdUsername,s.modifiedUsername as modifiedUsername) from StdHostelStandardAccessories s")
	public Page<Object> getAllSortedData(Pageable pageable);
	
}
