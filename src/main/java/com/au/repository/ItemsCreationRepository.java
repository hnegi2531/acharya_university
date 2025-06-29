package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.au.model.ItemsCreation;
import com.au.model.Vendor;
import com.au.dto.ActiveitemsDetailsResponseDto;

@Transactional
@Repository
public interface ItemsCreationRepository extends JpaRepository<ItemsCreation,Integer> {
	
	@Query(value = "select ic from ItemsCreation ic where ic.active=true")
	public List<ItemsCreation> findAll1();
	
	@Modifying
	@Query(value = "update ItemsCreation ic set ic.active=false where ic.item_id=?1")
	public void update(Integer itemId);
	
	
	@Modifying
	@Query(value = "update ItemsCreation ic set ic.active=true where ic.item_id=?1")
	public void update1(Integer itemId);
	
	@Query(value = "select new map(ic.item_id as id,ic.item_names as item_names,ic.item_type as item_type,ic.voucher_head_new_id As voucher_head_new_id,"
			+ "vhn.voucher_head As voucher_head,vhn.voucher_head_short_name As voucher_head_short_name,vhn.voucher_type As voucher_type,"
			+ "ic.item_short_name as item_short_name,ic.created_by as created_by,ic.modified_by as modified_by,"
			+ "ic.created_date as created_date,ic.modified_date as modified_date,ic.active as active,ic.measure_id as measure_id,"
			+ "ic.created_username as created_username,ic.modified_username as modified_username,le.ledger_name as ledger_name) "
			+ "from ItemsCreation ic "
			+ "left join Ledger le on ic.ledger_id=le.ledger_id "
			+ "left join VoucherHeadNew vhn on ic.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "where CONCAT(IfNull(ic.item_id,''),'',IfNull(ic.item_names,''),'',IfNull(ic.item_type,''),"
			+ "'',IfNull(ic.item_short_name,''),'',IfNull(ic.created_by,''),'',IfNull(ic.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(ic.item_id as id,ic.item_names as item_names,ic.item_type as item_type,ic.voucher_head_new_id As voucher_head_new_id,"
			+ "vhn.voucher_head As voucher_head,vhn.voucher_head_short_name As voucher_head_short_name,vhn.voucher_type As voucher_type,"
			+ "ic.item_short_name as item_short_name,ic.created_by as created_by,ic.modified_by as modified_by,"
			+ "ic.created_date as created_date,ic.modified_date as modified_date,ic.active as active,ic.measure_id as measure_id,"
			+ "ic.created_username as created_username,ic.modified_username as modified_username,le.ledger_name as ledger_name) "
			+ "from ItemsCreation ic "
			+ "left join Ledger le on ic.ledger_id=le.ledger_id "
			+ "left join VoucherHeadNew vhn on ic.voucher_head_new_id=vhn.voucher_head_new_id ")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "select count(*) from ItemsCreation ic where ic.item_names=?1 and ic.active=true")
	public Integer getCountItemName(String itemNames);

	@Query(value = "select SUBSTRING(ic.item_type ,1 ,1) as itemType from items_creation ic "
			+ "where ic.item_id=?1",nativeQuery=true)
    public String getItemType(Integer itemId);
	
	@Query(value="select ic.item_names from ItemsCreation ic where ic.item_id=:itemId ")
	public String getItemNameByItemId(@Param("itemId") Integer itemId);
	
	@Query(value = "Select new com.au.dto.ActiveitemsDetailsResponseDto(ic.item_id as itemId,m.measure_short_name as measure_name,m.measure_short_name as measure_short_name,"
			+ "CONCAT(ifNull(ic.item_names,' '),' - ',ifNull(eiis.item_description,' '),"
			+ "CASE WHEN eiis.make IS NULL THEN '' ELSE CONCAT(' - ', eiis.make) END) AS  itemNamesWithDiscriprtionAndMake,"
			+ "ic.active as active,ic.library_book_status as libraryBookStatus,ic.item_nature as itemNature,"
			+ "ic.is_accession as isAccession,ic.item_type as itemType,ic.item_names as itemNames,eiis.env_item_id as envItemId) from ItemsCreation ic "
			+ "Inner join EnvItemsInStores eiis on eiis.item_id=ic.item_id "
			+ "Left join Measure m On m.measure_id=eiis.measure_id Where ic.active=true",nativeQuery = false)
	public List<ActiveitemsDetailsResponseDto> allActiveitemsDetails();

	
	
	@Query(value=" select ic from ItemsCreation ic where ic.item_id=:itemId And ic.active=true")
	public ItemsCreation getItemCreationById(@Param("itemId") Integer itemId);

}
