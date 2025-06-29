package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.FarmLocks;

@Repository
@Transactional
public interface FarmLocksRepository extends JpaRepository<FarmLocks, Integer> {
	
	@Query(value = "select l from FarmLocks l where l.active=true")
	public List<FarmLocks> findAll1();
	
    @Query("SELECT new map(l.lockId as id, l.empId as empId, l.month as month, "
            + "l.year as year, l.facultyLock as facultyLock, l.hodLock as hodLock, "
            + "l.principalLock as principalLock, l.challengesFaced as challengesFaced, "
            + "l.active as active) "
            + "FROM FarmLocks l "
		+ "Where CONCAT(IfNull(l.month,''),'',IfNull(l.year,''),'',IfNull(l.createdDate,''),'',IfNull(l.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

    @Query("SELECT new map(l.lockId as id, l.empId as empId, l.month as month, "
            + "l.year as year, l.facultyLock as facultyLock, l.hodLock as hodLock, "
            + "l.principalLock as principalLock, l.challengesFaced as challengesFaced, "
            + "l.active as active) "
            + "FROM FarmLocks l")
	Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update FarmLocks l set l.active=false where l.lockId=?1")
	public void updateFarmLocks(Integer researchProgressId);

	@Modifying
	@Query(value = "update FarmLocks l set l.active=true where l.lockId=?1")
	public void updateFarmLocks1(Integer researchProgressId);

}
