package com.au.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.ItemAssignment;



@Transactional
@Repository
public interface ItemAssignmentRepository extends JpaRepository<ItemAssignment, Integer> {

	
	@Query(value = "select ia from ItemAssignment ia where ia.active=true")
	public List<ItemAssignment> findAll1();
	
	
	@Query(value = "select new map(ia.item_assignment_id as id,ia.ledger_id as ledger_id,ia.opening_balance as opening_balance,"
			+ "ia.item_creation_id as item_creation_id,ia.item_description as item_description,ia.make as make,"
			+ "ia.measure_id as measure_id,ia.created_by as created_by,ia.modified_by as modified_by,"
			+ "ia.created_Date as created_Date,ia.modified_Date as modified_Date,ia.active as active,"
			+ "ic.item_names as item_names,ic.item_short_name as item_short_name,ic.item_type as item_type,"
			+ "le.ledger_name as ledger_name,me.measure_name as measure_name,me.measure_short_name as measure_short_name,"
			+ "ia.created_username as created_username,ia.modified_username as modified_username) "
			+ "from ItemAssignment ia "
			+ "left join ItemsCreation ic on ic.item_id=ia.item_creation_id "
			+ "left join Measure me on me.measure_id=ia.measure_id "
			+ "left join Ledger le on le.ledger_id=ia.ledger_id "
			+ "where CONCAT(IfNull(ia.ledger_id,''),'',IfNull(ia.item_creation_id,''),'',IfNull(ia.item_description,''),"
			+ "'',IfNull(ia.created_username,''),'',IfNull(ia.created_by,''),'',IfNull(ia.created_Date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(ia.item_assignment_id as id,ia.ledger_id as ledger_id,ia.opening_balance as opening_balance,"
			+ "ia.item_creation_id as item_creation_id,ia.item_description as item_description,ia.make as make,"
			+ "ia.measure_id as measure_id,ia.created_by as created_by,ia.modified_by as modified_by,"
			+ "ia.created_Date as created_Date,ia.modified_Date as modified_Date,ia.active as active,"
			+ "ic.item_names as item_names,ic.item_short_name as item_short_name,ic.item_type as item_type,"
			+ "le.ledger_name as ledger_name,me.measure_name as measure_name,me.measure_short_name as measure_short_name,"
			+ "ia.created_username as created_username,ia.modified_username as modified_username) "
			+ "from ItemAssignment ia "
			+ "left join ItemsCreation ic on ic.item_id=ia.item_creation_id "
			+ "left join Measure me on me.measure_id=ia.measure_id "
			+ "left join Ledger le on le.ledger_id=ia.ledger_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Modifying
	@Query(value = "update ItemAssignment ia set ia.active=false where ia.item_assignment_id=?1")
	public void updateToDeactive(Integer id);

	@Modifying
	@Query(value = "update ItemAssignment ia set ia.active=true where ia.item_assignment_id=?1")
	public void updateToActive(Integer id);
	
	
}
