package com.au.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.PettyCash;

@Transactional
@Repository
public interface PettyCashRepository extends JpaRepository<PettyCash, Integer>{

	@Query(value = "SELECT pc from PettyCash pc where pc.active=true")
	public List<PettyCash> getAllPettyCash();
	
	
	@Modifying
	@Query(value = "update PettyCash co set co.active=false where co.petty_cash_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update PettyCash co set co.active=true where co.petty_cash_id=?1")
	public void activate(Integer id);
	
	
	@Query(value = "select new map(pc.petty_cash_id as id,pc.school_id as school_id,pc.amount as amount,pc.remark as remark,"
			+ "pc.created_username as created_username,pc.modified_username as modified_username,pc.modified_by as modified_by,pc.active as active,"
			+ "pc.created_date as created_date,pc.modified_date as modified_date,pc.created_by as created_by,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short ) from PettyCash pc "
			+ "left join Schools sc on sc.school_id=pc.school_id "
			+ "where CONCAT(IfNull(sc.school_name_short,''),'',IfNull(pc.petty_cash_id,''),"
			+ "'',IfNull(pc.created_by,''),'',IfNull(pc.created_date,'',IfNull(pc.amount,''),'')) LIKE %?1% ")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	@Query(value = "select new map(pc.petty_cash_id as id,pc.school_id as school_id,pc.amount as amount,pc.remark as remark,"
			+ "pc.created_username as created_username,pc.modified_username as modified_username,pc.modified_by as modified_by,pc.active as active,"
			+ "pc.created_date as created_date,pc.modified_date as modified_date,pc.created_by as created_by,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short ) from PettyCash pc "
			+ "left join Schools sc on sc.school_id=pc.school_id ")
	public Page<Object> getAllSortedData(Pageable pageable);


	@Query(value = "select count(*) from petty_cash where school_id=?1 And DATE(created_date)=Date(?2) And active=true",nativeQuery = true)
	public int getCount(Integer school_id, Date created_date);

	@Query(value = "select new map(pc.petty_cash_id as id,pc.school_id as school_id,pc.amount as amount,pc.remark as remark,"
			+ "pc.created_username as created_username,pc.modified_username as modified_username,pc.modified_by as modified_by,pc.active as active,"
			+ "pc.created_date as created_date,pc.modified_date as modified_date,pc.created_by as created_by,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short ) from PettyCash pc "
			+ "left join Schools sc on sc.school_id=pc.school_id  where pc.school_id=?1 ")
	public List<Map<String, Object>> getPettyCashDetailsBasedOnSchoolId(Integer school_id);


	@Query(value = "select CAST(COALESCE(sum(pc.amount),0) as double) as payment from petty_cash pc " +
			"where pc.created_username = :createdUsername and DATE(pc.created_date) between DATE(:fromDate) and DATE(:toDate)", nativeQuery = true)
    Double fetchByCreatedUsernameAndDates(String createdUsername, String fromDate, String toDate);

	@Query(value = "select sum(pc.amount) from petty_cash pc where pc.school_id = :schoolId " +
			"and (:fromDate is null or DATE(pc.created_date) >= DATE(:fromDate)) " +
			"and (:toDate is null or DATE(pc.created_date) <= DATE(:toDate)) ",
			nativeQuery = true)
	Double fetchBasedOnSchools(Integer schoolId, String fromDate, String toDate);

	@Query(value = "select COALESCE(SUM(pc.amount), 0) as totalAmount FROM petty_cash pc "
			+ "where DATE(pc.created_date) = DATE(?1) And pc.active=true",nativeQuery = true)
	public Double getTotalAmount(String selected_date);

	@Query(value = "select COALESCE(SUM(pc.amount), 0) as totalAmount FROM petty_cash pc "
			+ "where DATE(pc.created_date) = DATE(?1) And pc.active=true And pc.school_id=13 ",nativeQuery = true)
	public Double getTotalAmountHos(String selected_date);

	@Query(value = "select COALESCE(SUM(pc.amount), 0) as totalAmount,pc.school_id As school_id ,sc.school_name_short As school_name_short "
			+ "FROM acharya_erp.petty_cash pc "
			+ "left join schools sc on sc.school_id = pc.school_id "
			+ "where DATE(pc.created_date) = DATE(?1) And pc.active=true group by pc.school_id",nativeQuery = true)
	public List<Map<String, Object>> getTotalAmountGroupBy(String selected_date);
}
