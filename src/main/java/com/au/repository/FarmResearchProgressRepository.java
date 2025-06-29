package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.FarmResearchProgress;

@Repository
@Transactional
public interface FarmResearchProgressRepository extends JpaRepository<FarmResearchProgress, Integer> {
	
	@Query(value = "select rp from FarmResearchProgress rp where rp.active=true")
	public List<FarmResearchProgress> findAll1();
	
    @Query("SELECT new map(rp.researchProgressId as id, rp.empId as empId, rp.month as month, "
            + "rp.year as year, rp.activityName as activityName, rp.title as title, "
            + "rp.fromDate as fromDate, rp.toDate as toDate, rp.plannedWork as plannedWork, "
            + "rp.status as status, rp.activityDetails as activityDetails, rp.progressSummary as progressSummary, "
            + "rp.active as active) "
            + "FROM FarmResearchProgress rp "
		+ "Where CONCAT(IfNull(rp.month,''),'',IfNull(rp.year,''),'',IfNull(rp.createdDate,''),'',IfNull(rp.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

    @Query("SELECT new map(rp.researchProgressId as id, rp.empId as empId, rp.month as month, "
            + "rp.year as year, rp.activityName as activityName, rp.title as title, "
            + "rp.fromDate as fromDate, rp.toDate as toDate, rp.plannedWork as plannedWork, "
            + "rp.status as status, rp.activityDetails as activityDetails, rp.progressSummary as progressSummary, "
            + "rp.active as active) "
            + "FROM FarmResearchProgress rp")
	Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update FarmResearchProgress rp set rp.active=false where rp.researchProgressId=?1")
	public void updateFarmResearchProgress(Integer researchProgressId);

	@Modifying
	@Query(value = "update FarmResearchProgress rp set rp.active=true where rp.researchProgressId=?1")
	public void updateFarmResearchProgress1(Integer researchProgressId);

}
