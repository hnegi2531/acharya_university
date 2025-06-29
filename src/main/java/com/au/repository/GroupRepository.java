package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.Group;

@Transactional
@Repository
public interface GroupRepository extends JpaRepository<Group, Integer>{
	
	@Query(value = "select gp from Group gp where gp.active=true")
	public List<Group> findAll11();
	
	@Modifying
	@Query(value = "update Group gp set gp.active=false where gp.group_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update Group gp set gp.active=true where gp.group_id=?1")
	public void update1(Integer id);
	
	@Query(value ="Select new map(gp.group_id as id,gp.group_name as group_name,gp.group_short_name as group_short_name,gp.balance_sheet_group as balance_sheet_group,"
			+ "gp.created_date as created_date,gp.modified_date as modified_date,gp.created_by as created_by,gp.financials as financials,"
			+ "gp.modified_by as modified_by,gp.active as active,gp.remarks as remarks,gp.group_priority as group_priority,"
			+ "gp.created_username as created_username,gp.modified_username as modified_username) From Group gp "
			+ "Where CONCAT(IfNull(gp.group_id,''),'',IfNull(gp.group_name,''),'',IfNull(gp.group_short_name,''),'',"
			+ "IfNull(gp.created_date,''),'',IfNull(gp.created_by,''),'',IfNull(gp.remarks,''),'',IfNull(gp.created_username,''),'',IfNull(gp.group_priority,'')) LIKE %?1%")
	public Page<Object> findAll12(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(gp.group_id as id,gp.group_name as group_name,gp.group_short_name as group_short_name,gp.balance_sheet_group as balance_sheet_group,"
			+ "gp.created_date as created_date,gp.modified_date as modified_date,gp.created_by as created_by,gp.financials as financials,"
			+ "gp.modified_by as modified_by,gp.active as active,gp.remarks as remarks,gp.group_priority as group_priority,"
			+ "gp.created_username as created_username,gp.modified_username as modified_username) From Group gp ")
	public Page<Object> findAll13(Pageable pageable);
	
	@Query(value = "select count(*) from Group gp where gp.group_name=?1 and gp.active=true")
	public Integer getCountGroupName(String group_name);
	
	@Query(value = "select count(*) from Group gp where gp.group_short_name=?1 and gp.active=true")
	public Integer getCountGroupShortName(String group_short_name);

	@Query(value = "select g.group_name as groupName, g.group_id as groupId from group_table g "
			+ " left join ledger l on l.group_id=g.group_id "
			+ " left join items_creation i on i.ledger_id=l.ledger_id "
			+ "  where i.item_type='Goods' "
			+ " group by  g.group_name, g.group_id  ", nativeQuery = true)
	public List<Map<String, Object>> getGroupsForStockRegister();
	
}
