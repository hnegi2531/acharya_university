package com.au.repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.FineConcession;

@Repository
public interface FineConcessionRepository extends JpaRepository<FineConcession, Integer> {

	@Query(value=" select f from FineConcession f where f.auid=:auid ",nativeQuery = false)
	public FineConcession getFineConcessionByAuid(String auid);
	
	
	@Query(value=" select f.concession_amount from fine_concession f where str_to_date(f.till_date,'%Y-%m-%d')>=curdate() and f.auid=:auid ",nativeQuery = true)
	public Integer getFineConcessionAmountTillDate(String auid);

	@Transactional
	@Modifying
    @Query(value=" update FineConcession f set f.concessionAmount=:amount where f.auid=:auid ",nativeQuery = false)
	public void updateConcessionAmount(int amount, String auid);

	@Query("SELECT new map(f.fineConcessionId as fineConcessionId, f.auid as auid, f.currentSem as currentSem, f.currentYear as currentYear, " +
			"f.totalDue as totalDue, f.concessionAmount as concessionAmount, f.tillDate as tillDate, f.remarks as remarks, " +
			"f.file as file, f.createdDate as createdDate, f.modifiedDate as modifiedDate, "+
			" s.student_name as student_name, s.created_username as created_username, s.modified_username as modified_username, " +
			"s.active as active) " +
			"FROM FineConcession f " +
			"JOIN Student_Details s ON f.auid = s.auid " +
			"ORDER BY f.createdDate DESC")
	public List<Map<String, Object>> getAllConcessions();
}
