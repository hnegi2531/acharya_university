package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.au.model.StoresStock;

import com.au.dto.StoreDTO;

@Transactional
@Repository
public interface StoresStockRepository extends JpaRepository<StoresStock,Integer>{
	
	@Query(value = "select ss from StoresStock ss where ss.active=true")
	public List<StoresStock> findAll1();
	
	@Modifying
	@Query(value = "update StoresStock ss set ss.active=false where ss.stock_type_id=?1")
	public void update(Integer stock_type_id);
	
	@Modifying
	@Query(value = "update StoresStock ss set ss.active=true where ss.stock_type_id=?1")
	public void update1(Integer stock_type_id);
	
	@Query(value = "Select new map(ss.stock_type_id as id,ss.stock_type_name as stock_type_name,"
			+ "ss.stock_type_short_name as stock_type_short_name,ss.created_username as created_username,"
			+ "ss.modified_username as modified_username,ss.created_date as created_date,ss.modified_date as modified_date,"
			+ "ss.created_by as created_by,ss.modified_by as modified_by,ss.active as active) From StoresStock ss "
			+ "Where CONCAT(IfNull(ss.stock_type_name,''),'',IfNull(ss.stock_type_short_name,''),'',IfNull(ss.created_username,''),'',"
			+ "IfNull(ss.created_date,''),'',IfNull(ss.created_by,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(stock_type_id as id,ss.stock_type_name as stock_type_name,"
			+ "ss.stock_type_short_name as stock_type_short_name,ss.created_username as created_username,"
			+ "ss.modified_username as modified_username,ss.created_date as created_date,ss.modified_date as modified_date,"
			+ "ss.created_by as created_by,ss.modified_by as modified_by,ss.active as active) From StoresStock ss")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "Select count(*) from StoresStock ss where ss.stock_type_name=?1 and ss.active=true")
	public Integer getCountStockTypename(String stock_type_name);
	
	@Query(value = "Select count(*) from StoresStock ss where ss.stock_type_short_name=?1 and ss.active=true")
	public Integer getCountStockTypeShortName(String stock_type_short_name);
	
	@Query(value = "Select ss.stock_type_short_name from StoresStock ss where ss.stock_type_id=?1 and ss.active=true")
	public String getStockTypeShortName(Integer stock_type_id);
	
	@Query(value="select new com.au.dto.StoreDTO(ss.stock_type_id as storeId,ss.stock_type_name as storeName) from StoresStock ss where  ss.active=true",nativeQuery = false)
	List<StoreDTO> getStores();
	
	@Query(value = "Select new map(ss.stock_type_id as id,ss.stock_type_name as stock_type_name,"
			+ "ss.stock_type_short_name as stock_type_short_name,ss.created_username as created_username,"
			+ "ss.modified_username as modified_username,ss.created_date as created_date,ss.modified_date as modified_date,"
			+ "ss.created_by as created_by,ss.modified_by as modified_by,ss.active as active) From StoresStock ss "
			+ "Where CONCAT(IfNull(ss.stock_type_name,''),'',IfNull(ss.stock_type_short_name,''),'',IfNull(ss.created_username,''),'',"
			+ "IfNull(ss.created_date,''),'',IfNull(ss.created_by,'')) LIKE %?1%")
	public Page<Object> allStoresStockDetails1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(ss.stock_type_id as id,ss.stock_type_name as stock_type_name,"
			+ "ss.stock_type_short_name as stock_type_short_name,ss.created_username as created_username,"
			+ "ss.modified_username as modified_username,ss.created_date as created_date,ss.modified_date as modified_date,"
			+ "ss.created_by as created_by,ss.modified_by as modified_by,ss.active as active) From StoresStock ss ")
	public Page<Object> allStoresStockDetails2(Pageable pageable);
}
