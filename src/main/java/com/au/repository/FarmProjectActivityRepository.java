package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.FarmProjectActivity;

@Repository
@Transactional
public interface FarmProjectActivityRepository  extends JpaRepository<FarmProjectActivity, Integer> {
	
	@Query(value = "select pa from FarmProjectActivity pa where pa.active=true")
	public List<FarmProjectActivity> findAll1();
	
    @Query("SELECT new map(pa.projectActivityId as id, pa.empId as empId, pa.month as month, "
            + "pa.year as year, pa.activity as activity, pa.purpose as purpose, "
            + "pa.initiationDate as initiationDate, "
            + "pa.active as active) "
            + "FROM FarmProjectActivity pa "
		+ "Where CONCAT(IfNull(pa.month,''),'',IfNull(pa.year,''),'',IfNull(pa.createdDate,''),'',IfNull(pa.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

    @Query("SELECT new map(pa.projectActivityId as id, pa.empId as empId, pa.month as month, "
            + "pa.year as year, pa.activity as activity, pa.purpose as purpose, "
            + "pa.initiationDate as initiationDate, "
            + "pa.active as active) "
            + "FROM FarmProjectActivity pa")
	Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update FarmProjectActivity pa set pa.active=false where pa.projectActivityId=?1")
	public void updateFarmProjectActivity(Integer projectActivityId);

	@Modifying
	@Query(value = "update FarmProjectActivity pa set pa.active=true where pa.projectActivityId=?1")
	public void updateFarmProjectActivity1(Integer projectActivityId);

}
