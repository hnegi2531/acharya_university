package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.FarmOtherPertinentActivities;

@Repository
@Transactional
public interface FarmOtherPertinentActivitiesRepository extends JpaRepository<FarmOtherPertinentActivities, Integer> {
	
	@Query(value = "select op from FarmOtherPertinentActivities op where op.active=true")
	public List<FarmOtherPertinentActivities> findAll1();
	
    @Query("SELECT new map(op.otherPertinentId as id, op.empId as empId, op.month as month, "
            + "op.year as year, op.activity as activity, op.purpose as purpose, "
            + "op.maxMarks as maxMarks, op.selfMarks as selfMarks, op.hodMarks as hodMarks, "
            + "op.active as active) "
            + "FROM FarmOtherPertinentActivities op "
		+ "Where CONCAT(IfNull(op.month,''),'',IfNull(op.year,''),'',IfNull(op.createdDate,''),'',IfNull(op.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

    @Query("SELECT new map(op.otherPertinentId as id, op.empId as empId, op.month as month, "
            + "op.year as year, op.activity as activity, op.purpose as purpose, "
            + "op.maxMarks as maxMarks, op.selfMarks as selfMarks, op.hodMarks as hodMarks, "
            + "op.active as active) "
            + "FROM FarmOtherPertinentActivities op")
	Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update FarmOtherPertinentActivities op set op.active=false where op.otherPertinentId=?1")
	public void updateFarmOtherPertinentActivities(Integer otherPertinentId);

	@Modifying
	@Query(value = "update FarmOtherPertinentActivities op set op.active=true where op.otherPertinentId=?1")
	public void updateFarmOtherPertinentActivities1(Integer otherPertinentId);

	@Modifying
	@Query(value = "update FarmOtherPertinentActivities op set op.attachmentPath=?2 where op.otherPertinentId IN ?1")
	public void updatePath(List<Integer> otherPertinentId, String t2);
	
}
