package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Batch;

@Repository
@Transactional
public interface BatchRepository extends JpaRepository<Batch, Integer>{

	@Query(value = "select ba from Batch ba where ba.active=true")
	public List<Batch> findAll1();
	
	
	@Modifying
	@Query(value = "update Batch ba set ba.active=false where ba.batch_id=?1")
	public void updateBatch(Integer id);

	@Modifying
	@Query(value = "update Batch ba set ba.active=true where ba.batch_id=?1")
	public void updateBatch1(Integer id);
	
	@Query(value ="Select new map(ba.batch_id as id,ba.batch_name as batch_name,ba.batch_short_name as batch_short_name,"
			+ "ba.remarks as remarks,ba.created_date as created_date,ba.modified_date as modified_date,ba.created_by as created_by,"
			+ "ba.modified_by as modified_by,ba.created_username as created_username,ba.modified_username as modified_username,"
			+ "ba.active as active) From Batch ba "
			+ "Where CONCAT(IfNull(ba.batch_id,''),'',IfNull(ba.batch_name,''),'',IfNull(ba.batch_short_name,''),'',IfNull(ba.created_date,''),'',IfNull(ba.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(ba.batch_id as id,ba.batch_name as batch_name,ba.batch_short_name as batch_short_name,"
			+ "ba.remarks as remarks,ba.created_date as created_date,ba.modified_date as modified_date,ba.created_by as created_by,"
			+ "ba.modified_by as modified_by,ba.created_username as created_username,ba.modified_username as modified_username,"
			+ "ba.active as active) From Batch ba")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM Batch ba where ba.batch_name=?1 and ba.active=true")
	public Integer countOfBatchName(String batch_name);
	
	@Query(value = "SELECT count(*) FROM Batch ba where ba.batch_short_name=?1 and ba.active=true")
	public Integer countOfBatchShortName(String batch_short_name);

	@Query(value = "SELECT ba FROM Batch ba where ba.batch_short_name=:batchShortName ")
	public Batch findByBatchShortName(String batchShortName);


	
}
