package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.BankGroup;
import com.au.model.CourseObjective;


@Transactional
@Repository
public interface BankGroupRepository extends JpaRepository<BankGroup, Integer>{

	@Query(value = "SELECT bg from BankGroup bg where bg.active=true")
	public List<BankGroup> listAll();

	@Modifying
	@Query(value = "update BankGroup bg set bg.active=false where bg.bank_group_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update BankGroup bg set bg.active=true where bg.bank_group_id=?1")
	public void activate(Integer id);
	
	
	
	@Query(value = "select new map(bg.bank_group_id as id,bg.bank_group_name as bank_group_name,"
			+ "bg.created_username as created_username,bg.modified_username as modified_username,bg.created_date as created_date,bg.modified_date as modified_date,bg.created_by as created_by,"
			+ "bg.modified_by as modified_by,bg.active as active) from BankGroup bg "
			+ "where CONCAT(IfNull(bg.created_username,''),'',IfNull(bg.bank_group_name,''),"
			+ "'',IfNull(bg.created_by,''),'',IfNull(bg.created_date,'',IfNull(bg.bank_group_id,''),'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	@Query(value = "select new map(bg.bank_group_id as id,bg.bank_group_name as bank_group_name,"
			+ "bg.created_username as created_username,bg.modified_username as modified_username,bg.created_date as created_date,bg.modified_date as modified_date,bg.created_by as created_by,"
			+ "bg.modified_by as modified_by,bg.active as active) from BankGroup bg ")
	public Page<Object> getAllSortedData(Pageable pageable);

}
