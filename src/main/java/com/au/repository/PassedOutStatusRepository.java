package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.PassedOutStatus;

@Transactional
@Repository
public interface PassedOutStatusRepository extends JpaRepository<PassedOutStatus, Integer> {
	
	@Query(value = "select p from PassedOutStatus p where p.active=true")
	public List<PassedOutStatus> findAll1();
	
	@Modifying
	@Query(value = "update PassedOutStatus p set p.active=false where p.status_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update PassedOutStatus p set p.active=true where p.status_id=?1")
	public void update1(Integer id);
	
	@Query(value = "select new map(p.status_id as id,p.status_name as status_name,"
			+ "p.status_short_name as status_short_name,p.created_by as created_by,p.modified_by as modified_by,"
			+ "p.created_date as created_date,p.modified_date as modified_date,p.active as active,"
			+ "p.created_username as created_username,p.modified_username as modified_username) "
			+ "from PassedOutStatus p "
			+ "where CONCAT(IfNull(p.status_id,''),'',IfNull(p.status_name,''),'',IfNull(p.status_short_name,''),"
			+ "'',IfNull(p.created_by,''),'',IfNull(p.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(p.status_id as id,p.status_name as status_name,"
			+ "p.status_short_name as status_short_name,p.created_by as created_by,p.modified_by as modified_by,"
			+ "p.created_date as created_date,p.modified_date as modified_date,p.active as active,"
			+ "p.created_username as created_username,p.modified_username as modified_username) "
			+ "from PassedOutStatus p")
	public Page<Object> getAllSortedData(Pageable pageable);

}
