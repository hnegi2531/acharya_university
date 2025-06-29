package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.AssignUserResponseDTO;
import com.au.model.Ledger;


@Transactional
@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Integer> {

	@Query(value = "SELECT * FROM ledger where group_id=?1", nativeQuery = true)
	public List<Ledger> getLedgerByGroupId(Integer grp_id);

	@Query(value = "SELECT count(ledger_id)  FROM ledger where group_id=?1", nativeQuery = true)
	public Integer CountLedgerByGroupId(Integer grp_id);

	
	@Query(value = "select new map(l.ledger_id as id,l.group_id as group_id,l.ledger_name as ledger_name,"
			+ "l.remarks as remarks,l.room_status as room_status,l.ledger_short_name as ledger_short_name,"
			+ "l.created_date as created_date,l.modified_date as modified_date,l.created_by as created_by,l.modified_by as modified_by,"
			+ "l.active as active,l.created_username as created_username,l.modified_username as modified_username,"
			+ "l.type_of_expenses as type_of_expenses,l.capex as capex,l.opex as opex,l.balance_sheet_row_code as balance_sheet_row_code,"
			+ "l.name_in_russia as name_in_russia,l.name_in_english as name_in_english,l.financial_report_status as financial_report_status,"
			+ "l.priority as priority,gr.group_name as group_name,th.tally_fee_head as tally_fee_head) "
			+ "from Ledger l "
			+ "left join Group gr on l.group_id=gr.group_id "
			+ "left join TallyHead th on th.tally_id=l.tally_id "
			+ "Where CONCAT(IfNull(l.ledger_id,''),'',IfNull(gr.group_name,''),'',IfNull(l.ledger_name,''),'',"
			+ "IfNull(l.ledger_short_name,''),'',IfNull(l.created_date,''),'',IfNull(l.created_by,''),'',"
			+ "IfNull(l.created_username,''),'',IfNull(l.priority,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "select new map(l.ledger_id as id,l.group_id as group_id,l.ledger_name as ledger_name,"
			+ "l.remarks as remarks,l.room_status as room_status,l.ledger_short_name as ledger_short_name,"
			+ "l.created_date as created_date,l.modified_date as modified_date,l.created_by as created_by,l.modified_by as modified_by,"
			+ "l.active as active,l.created_username as created_username,l.modified_username as modified_username,"
			+ "l.type_of_expenses as type_of_expenses,l.capex as capex,l.opex as opex,l.balance_sheet_row_code as balance_sheet_row_code,"
			+ "l.name_in_russia as name_in_russia,l.name_in_english as name_in_english,l.financial_report_status as financial_report_status,"
			+ "l.priority as priority,gr.group_name as group_name,th.tally_fee_head as tally_fee_head) "
			+ "from Ledger l "
			+ "left join Group gr on l.group_id=gr.group_id "
			+ "left join TallyHead th on th.tally_id=l.tally_id ")
	public Page<Object> findAll2(Pageable pageable);

	@Query(value = "select l from Ledger l where l.active=true")
	public List<Ledger> findAll11();
	
	@Modifying
	@Query(value = "update Ledger l set l.active=false where l.ledger_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update Ledger l set l.active=true where l.ledger_id=?1")
	public void update1(Integer id);
	
	@Query(value = "select count(*) from Ledger l where l.ledger_name=?1 and l.active=true")
	public Integer getCountLedgerName(String ledger_name);
	
	@Query(value = "select count(*) from Ledger l where l.ledger_short_name=?1 and l.active=true")
	public Integer getCountLedgerShortName(String ledger_short_name);

	
	@Query(value = "select SUBSTRING(l.ledger_short_name ,1 ,3) as short_name "
			+ "from ledger l where l.ledger_id=?1 ",nativeQuery=true)
    public String getLedgerShortName(Integer ledger_id);
	
	@Query(value = "select SUBSTRING(l.type_of_expenses ,1 ,1) as Expenses  "
			+ "from ledger l where l.ledger_id=?1 ",nativeQuery=true)
    public String getLedgerExpense(Integer ledger_id);

	@Query(value = "select l.ledger_name as ledgerName,l.ledger_id as ledgerId from ledger l where l.group_id=:groupId and l.active=1 ", nativeQuery = true)
	public List<Map<String, Object>> getLegderbyGroupId(Integer groupId);
	
	
}
