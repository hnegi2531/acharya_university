package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.dto.FineSlabDTO;
import com.au.model.FineSlab;

@Repository
public interface FineSlabRepository  extends JpaRepository<FineSlab, Integer>{
	
	@Query(value="select fb from FineSlab fb where fb.schoolId=:schoolId and fb.week=:week")
	FineSlab getFineSlabBySchoolIdAndWeek(Integer schoolId,Integer week);

	@Query(value="select new com.au.dto.FineSlabDTO( fb.fineSlabId, fb.week, fb.percentage, fb.schoolId ,sc.school_name, fb.fromDate,fb.tillDate )  from FineSlab fb left join Schools sc on sc.school_id=fb.schoolId   ")
	List<FineSlabDTO> getAllFines();

	@Query(value="select new com.au.dto.FineSlabDTO( fb.fineSlabId, fb.week, fb.percentage, fb.schoolId, sc.school_name, fb.fromDate,fb.tillDate )  from FineSlab fb left join Schools sc on sc.school_id=fb.schoolId where fb.fineSlabId=:fineSlabId ")
	FineSlabDTO getFineSlabById(Integer fineSlabId);

	@Query(value="select * from fine_slab fb where fb.week=:week ORDER BY fb.created_date DESC limit 1 ", nativeQuery = true)
	FineSlab getFineSlabByWeek(Integer week);

}
