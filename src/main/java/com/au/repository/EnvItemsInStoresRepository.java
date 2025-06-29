package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.EnvItemsInStores;

@Transactional
@Repository
public interface EnvItemsInStoresRepository extends JpaRepository<EnvItemsInStores , Integer> {
	
	@Query(value = " select eiis from EnvItemsInStores eiis where eiis.active = true ")
	public List<EnvItemsInStores>  findAll1();
	
//	@Query(value = "select new map(eiis.env_item_id as env_item_id, eiis.active as active,"
//			+ "eiis.created_by as created_by, eiis.created_date as created_date, eiis.created_username as created_username,"
//			+ "eiis.item_description as item_description, itm.item_names as item_names, eiis.item_serial_no as item_serial_no,"
//			+ "le.ledger_name as ledger_name,me.measure_name as measure_name, eiis.modified_by as modified_by,"
//			+ "eiis.modified_by as modified_by, eiis.modified_date as modified_date, eiis.modified_username as modified_username,"
//			+ "ss.stock_type_name as stock_type_name, eiis.total_available_in_stores as total_available_in_stores,"
//			+ "eiis.total_issued as total_issued, eiis.make as make)"
//			+ "from EnvItemsInStores eiis left join ItemsCreation itm on eiis.item_id=itm.item_id "
//			+ "left join Ledger le on eiis.ledger_id=le.ledger_id "
//			+ "left join Measure me on eiis.measure_id=me.measure_id "
//			+ "left join StoresStock ss on eiis.stock_type_id=ss.stock_type_id ")
//	public List<HashMap<String, Object>> findAll2();
	
	@Query(value = "select new map(eiis.env_item_id as id, eiis.active as active,eiis.opening_balance as opening_balance,"
			+ "eiis.created_by as created_by, eiis.created_date as created_date, eiis.created_username as created_username,"
			+ "eiis.item_description as item_description, itm.item_names as item_names, eiis.item_serial_no as item_serial_no,"
			+ "le.ledger_name as ledger_name,me.measure_name as measure_name, eiis.modified_by as modified_by,"
			+ "eiis.modified_by as modified_by, eiis.modified_date as modified_date, eiis.modified_username as modified_username,"
			+ "ss.stock_type_name as stock_type_name, eiis.total_available_in_stores as total_available_in_stores,"
			+ "ic.measure_id as measure_id,me.measure_name as measure_name,me.measure_short_name as measure_short_name,ic.item_id as item_id,"
			+ "ic.item_names as item_names,ic.item_short_name as item_short_name,ic.item_type as item_type,"
			+ "eiis.total_issued as total_issued, eiis.make as make) "
			+ "from EnvItemsInStores eiis left join ItemsCreation itm on eiis.item_id=itm.item_id "
			+ "left join Ledger le on eiis.ledger_id=le.ledger_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Measure me on ic.measure_id=me.measure_id "
			+ "left join StoresStock ss on eiis.stock_type_id=ss.stock_type_id "
			+ "where CONCAT(IfNull(eiis.env_item_id,''),'',IfNull(eiis.item_description,''),'',IfNull(eiis.total_issued,''),"
			+ "'',IfNull(eiis.total_available_in_stores,''),'',IfNull(eiis.created_by,''),'',IfNull(eiis.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(eiis.env_item_id as id, eiis.active as active,eiis.opening_balance as opening_balance,"
			+ "eiis.created_by as created_by, eiis.created_date as created_date, eiis.created_username as created_username,"
			+ "eiis.item_description as item_description,eiis.item_serial_no as item_serial_no,"
			+ "le.ledger_name as ledger_name,me.measure_name as measure_name, eiis.modified_by as modified_by,"
			+ "eiis.modified_by as modified_by, eiis.modified_date as modified_date, eiis.modified_username as modified_username,"
			+ "ss.stock_type_name as stock_type_name, eiis.total_available_in_stores as total_available_in_stores,"
			+ "ic.item_names as item_names,ic.item_short_name as item_short_name,ic.item_type as item_type,"
			+ "ic.measure_id as measure_id,me.measure_name as measure_name,me.measure_short_name as measure_short_name,ic.item_id as item_id,"
			+ "eiis.total_issued as total_issued, eiis.make as make) "
			+ "from EnvItemsInStores eiis "
			+ "left join Ledger le on eiis.ledger_id=le.ledger_id "
			+ "left join ItemsCreation ic on eiis.item_id=ic.item_id "
			+ "left join Measure me on ic.measure_id=me.measure_id "
			+ "left join StoresStock ss on eiis.stock_type_id=ss.stock_type_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = " update EnvItemsInStores eiis set eiis.active=false where eiis.env_item_id=?1")
	public void update(Integer env_item_id);
	
	@Modifying
	@Query(value = " update EnvItemsInStores eiis set eiis.active=true where eiis.env_item_id=?1")
	public void update1(Integer env_item_id);
	
	
	@Query(value = "select max(cast(SUBSTRING(item_serial_no ,5 ,10) as UNSIGNED)) AS number2 from env_items_in_stores "
			+ "where item_serial_no is not null;",nativeQuery=true)
	public String getSerialNumber();
	

	@Query(value = "WITH grn_summary AS ( "
			+ "    SELECT "
			+ "        env_item_id, "
			+ "        COALESCE(SUM(enter_qty), 0) AS grnQuantity "
			+ "    FROM "
			+ "        purhcase_grn "
			+ "    GROUP BY "
			+ "        env_item_id "
			+ "), "
			+ "issue_summary AS ( "
			+ "    SELECT "
			+ "        item_id, "
			+ "        COALESCE(SUM(issue_quantity), 0) AS issueQuantity "
			+ "    FROM "
			+ "        stock_issue "
			+ "    GROUP BY "
			+ "        item_id "
			+ "),  "
			+ "indent_summary AS ( "
			+ "    SELECT "
			+ "        env_item_id, "
			+ "        COALESCE(SUM(CASE  "
			+ "            WHEN purchase_status = 1 AND active = 1 THEN issued_quantity ELSE 0  "
			+ "        END), 0) AS indent_quantity "
			+ "    FROM "
			+ "        store_indent_request "
			+ "    GROUP BY "
			+ "        env_item_id "
			+ "), "
			+ "request_indent_summary AS ( "
			+ "    SELECT "
			+ "        env_item_id, "
			+ "        COALESCE(SUM(CASE  "
			+ "            WHEN purchase_status = 0 AND active = 1 THEN quantity ELSE 0  "
			+ "        END), 0) AS request_indent_quantity "
			+ "    FROM "
			+ "        store_indent_request "
			+ "    GROUP BY "
			+ "        env_item_id "
			+ ") "
			+ "SELECT "
			+ "    eiis.env_item_id AS env_item_id, "
			+ "    ic.item_id AS item_id, "
			+ "    eiis.item_description AS description, "
			+ "    eiis.make AS make, "
			+ "    CONCAT(ic.item_names, '-', eiis.item_description, '-', eiis.make) AS ITEM_NAME, "
			+ "    eiis.ledger_id AS ledger_id, "
			+ "    eiis.measure_id AS measure_id, "
			+ "    COALESCE(eiis.opening_balance, 0) AS opening_balance, "
			+ "    COALESCE(grn.grnQuantity, 0) AS grnQuantity, "
			+ "    COALESCE(issue.issueQuantity, 0) AS issueQuantity, "
			+ "    COALESCE(eiis.scrap, 0) AS scrap, "
			+ "    COALESCE(indent.indent_quantity, 0) AS indent_quantity, "
			+ "    COALESCE(request_indent.request_indent_quantity, 0) AS request_indent_quantity, "
			+ "    (COALESCE(eiis.opening_balance, 0)  "
			+ "     + COALESCE(grn.grnQuantity, 0)  "
			+ "     - COALESCE(issue.issueQuantity, 0)  "
			+ "     - COALESCE(eiis.scrap, 0)  "
			+ "     - COALESCE(indent.indent_quantity, 0)  "
			+ "     - COALESCE(request_indent.request_indent_quantity, 0)) AS closingStock "
			+ "FROM "
			+ "    env_items_in_stores eiis "
			+ "LEFT JOIN "
			+ "    items_creation ic ON ic.item_id = eiis.item_id "
			+ "LEFT JOIN "
			+ "    grn_summary grn ON grn.env_item_id = eiis.env_item_id "
			+ "LEFT JOIN "
			+ "    issue_summary issue ON issue.item_id = eiis.env_item_id "
			+ "LEFT JOIN "
			+ "    indent_summary indent ON indent.env_item_id = eiis.env_item_id "
			+ "LEFT JOIN "
			+ "    request_indent_summary request_indent ON request_indent.env_item_id = eiis.env_item_id group by  eiis.env_item_id "
		   , nativeQuery = true)
	public List<Map<String,Object>> getItemNameConcatWithdescriptionAndMake();
	
	
	@Query(value="select m.measure_short_name from EnvItemsInStores es left join Measure m on es.measure_id=m.measure_id where es.env_item_id=:envItemId ",nativeQuery = false)
	public String getUomForGRN(@Param("envItemId") Integer envItemId);
	
	@Query(value="select m.measure_short_name from EnvItemsInStores es left join Measure m on es.measure_id=m.measure_id where es.env_item_id=:envItemId ",nativeQuery = false)
	public String getUomShortNameForGRN(@Param("envItemId") Integer envItemId);
	
	@Query(value="select ic from EnvItemsInStores ic where ic.active=true",nativeQuery = false)
	public Page<EnvItemsInStores> getItemAssigment(Pageable pageable);
	
	@Query(value="select env from EnvItemsInStores env where env.stock_type_id=:storeId ", nativeQuery = false)
	public List<EnvItemsInStores> getListOfStockRegisterByStoreId(@Param("storeId") Integer storeId);
	
	@Query(value="select env from EnvItemsInStores env where env.env_item_id=:itemAssignmentId")
	public EnvItemsInStores getEnvItemsbyId(@Param("itemAssignmentId") Integer itemAssignmentId);

	@Query(value =  "    WITH grn_summary AS (     SELECT "
			+ "        env_item_id, "
			+ "        COALESCE(SUM(enter_qty), "
			+ "        0) AS grnQuantity           "
			+ "    FROM "
			+ "        purhcase_grn           "
			+ "    GROUP BY "
			+ "        env_item_id ), issue_summary AS (     SELECT "
			+ "        item_id, "
			+ "        COALESCE(SUM(issue_quantity), "
			+ "        0) AS issueQuantity           "
			+ "    FROM "
			+ "        stock_issue           "
			+ "    GROUP BY "
			+ "        item_id ),  indent_summary AS (     SELECT "
			+ "        env_item_id, "
			+ "        COALESCE(SUM(CASE               "
			+ "            WHEN purchase_status = 1  "
			+ "            AND active = 1 THEN issued_quantity  "
			+ "            ELSE 0           "
			+ "        END), "
			+ "        0) AS indent_quantity      "
			+ "    FROM "
			+ "        store_indent_request      "
			+ "    GROUP BY "
			+ "        env_item_id ) "
			+ "	select "
			+ "        eiis.env_item_id as itemAssignmentId, "
			+ "        eiis.item_assigment_name as itemAssigmentName, "
			+ "        eiis.item_id as itemId, "
			+ "        ic.item_names as itemName, "
			+ "        eiis.item_description as itemDescription, "
			+ "        eiis.ledger_id as legderId  , "
			+ "        COALESCE(eiis.opening_balance, "
			+ "        0) AS opening_balance, "
			+ "        COALESCE(grn.grnQuantity, "
			+ "        0) AS grnQuantity, "
			+ "        COALESCE(indent.indent_quantity, "
			+ "        0) AS issueQuantity, "
			+ "        COALESCE(eiis.scrap, "
			+ "        0) AS scrap, "
			+ "        (COALESCE(eiis.opening_balance, "
			+ "        0) + COALESCE(grn.grnQuantity, "
			+ "        0) - COALESCE(indent.indent_quantity, "
			+ "        0) - COALESCE(eiis.scrap, "
			+ "        0)  ) AS closingStock , "
			+ "        m.measure_name as uomName ,      "
			+ "        l.ledger_name as ledgerName       "
			+ "    from "
			+ "        env_items_in_stores eiis        "
			+ "    left join "
			+ "        items_creation ic  "
			+ "            on ic.item_id=eiis.item_id      "
			+ "    LEFT JOIN "
			+ "        grn_summary grn               "
			+ "            ON grn.env_item_id = eiis.env_item_id       "
			+ "    LEFT JOIN "
			+ "        issue_summary issue               "
			+ "            ON issue.item_id = eiis.env_item_id       "
			+ "    LEFT JOIN "
			+ "        indent_summary indent               "
			+ "            ON indent.env_item_id = eiis.env_item_id      "
			+ "    LEFT JOIN "
			+ "        measures m  "
			+ "            on m.measure_id=eiis.measure_id        "
			+ "    LEFT JOIN "
			+ "        ledger l  "
			+ "            on l.ledger_id = COALESCE(:ledgerId, ic.ledger_id)        "
			+ "    where "
			+ "        (:ledgerId IS NULL OR ic.ledger_id = :ledgerId) "
			+ "        and ic.item_type='Goods'  "
			+ "        and ic.is_accession=0  "
			+ "    group by "
			+ "        eiis.env_item_id", nativeQuery = true)
public List<Map<String, Object>> getListOfStockRegisterByLedgerId(Integer ledgerId);

	@Query(value = " select eiis from EnvItemsInStores eiis where eiis.item_id=?1 And eiis.active = true ")
	public List<EnvItemsInStores> getData(Integer itemId);


	
}
