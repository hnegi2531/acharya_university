package com.au.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.dto.LockDateRequestDTO;
import com.au.dto.LockDateResponsseDTO;
import com.au.model.LockDates;



@Repository
public interface LockDateRepository extends JpaRepository<LockDates, Integer>{

	String LOCK_DATE_FILTERS=" (:#{#lockDateRequestDTO.lock_month} IS NULL OR l.lock_month=:#{#lockDateRequestDTO.lock_month}) and"
			+ " (:#{#lockDateRequestDTO.lock_year} IS NULL OR l.lock_year=:#{#lockDateRequestDTO.lock_year}) and "
			+ " (:#{#lockDateRequestDTO.leave_lock_date} IS NULL OR l.leave_lock_date=:#{#lockDateRequestDTO.leave_lock_date}) and "
			+ " (:#{#lockDateRequestDTO.payroll_lock_date} IS NULL OR l.payroll_lock_date=:#{#lockDateRequestDTO.payroll_lock_date}) and"
			+ " (:#{#lockDateRequestDTO.searchtext} IS NULL OR l.payroll_lock_date like :#{#lockDateRequestDTO.searchtext}% OR l.leave_lock_date like :#{#lockDateRequestDTO.searchtext}% OR l.lock_year like :#{#lockDateRequestDTO.searchtext}% OR  l.lock_month like :#{#lockDateRequestDTO.searchtext}% )   and"
			+ " l.active=1" ;
	
	@Query(value="select new com.au.dto.LockDateResponsseDTO( l.lock_month as lock_month, l.lock_year as lock_year, l.leave_lock_date as leave_lock_date, l.payroll_lock_date as payroll_lock_date,l.created_by as created_by,l.created_date as created_date,l.remarks as remarks,l.modified_by as modified_by ) from LockDates l where "
			+ LOCK_DATE_FILTERS ,nativeQuery=false)
	Page<LockDateResponsseDTO> getLockDatesList(LockDateRequestDTO lockDateRequestDTO, Pageable pageable);

	@Query(value="select l from LockDates l where l.lock_id=:lockId ",nativeQuery=false)
	LockDates getLockDateById(@Param("lockId") Integer lockId);

	@Query(value=" select new map(l.lock_id as id, l.lock_month as lock_month, l.lock_year as lock_year,"
			+ " l.leave_lock_date as leave_lock_date, l.payroll_lock_date as payroll_lock_date,ua.username as created_name,"
			+ "l.created_by as created_by, l.created_date as created_date, l.active as active) from LockDates l "
			+ "left join UserAuthentication ua on ua.id=l.created_by "
			+ " where CONCAT( IfNull(l.lock_month,''),'',IfNull(l.lock_year,''),'',"
			+ "IfNull(l.leave_lock_date,''),'',IfNull(l.payroll_lock_date,''),'' ) LIKE %?1% ")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query(value=" select new map(l.lock_id as id, l.lock_month as lock_month, l.lock_year as lock_year,"
			+ " l.leave_lock_date as leave_lock_date, l.payroll_lock_date as payroll_lock_date,ua.username as created_name,"
			+ " l.created_by as created_by, l.created_date as created_date, l.active as active) from LockDates l "
			+ "left join UserAuthentication ua on ua.id=l.created_by ")
	Page<Object> getAllSortedData(Pageable pageable);

	@Query(value = "Select count(*) From lock_dates l where l.lock_month=?1 And l.lock_year=?2 ",nativeQuery = true)
	public Integer getCountOfMonthAndYear(Integer lock_month, Integer lock_year);
	
	@Query(value="select * from lock_dates where lock_month=:month and lock_year=:year and active=1 order by created_date desc limit 1 ",nativeQuery = true)
	public LockDates getFirstByMonthandYear(@Param("month") Integer month,@Param("year") Integer year);

	@Query(value=" select new map(l.lock_id as id, l.lock_month as lock_month, l.lock_year as lock_year,"
			+ " l.leave_lock_date as leave_lock_date, l.payroll_lock_date as payroll_lock_date,ua.username as created_name,"
			+ " l.created_by as created_by, l.created_date as created_date, l.active as active) from LockDates l "
			+ "left join UserAuthentication ua on ua.id=l.created_by "
			+ " where l.lock_month=?1 And l.lock_year=?2 And l.active=1 ")
	public List<HashMap<String, Object>> getLockDateDetailsData(Integer lock_month, Integer lock_year);

	@Query(value = "select ld.leave_lock_date from lock_dates ld where ld.lock_month=month(?1) and ld.lock_year=year(?1) and ld.active=true",nativeQuery = true)
	public Date checkMonthAndYearInLockDateTable(LocalDate minusMonths);
	
	@Query(value="select * from lock_dates where lock_month=:month and lock_year=:year and active=1 order by created_date desc limit 1 ",nativeQuery = true)
	public LockDates getLatestDataByMonthandYear(@Param("month") Integer month,@Param("year") Integer year);
	
}
