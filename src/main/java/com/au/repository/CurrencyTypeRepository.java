package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Currency_Type;

@Transactional
@Repository
public interface CurrencyTypeRepository extends JpaRepository<Currency_Type, Integer> {

	@Query(value = "SELECT * FROM currency_type where active=1", nativeQuery = true)
	public List<Currency_Type> findAll1();

	@Modifying
	@Query(value = "update Currency_Type ct set ct.active=false where ct.currency_type_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update Currency_Type ct set ct.active=true where ct.currency_type_id=?1")
	public void update1(Integer id);

	@Query(value = "select currency_type_short_name from currency_type "
			+ "where currency_type_id=?1",nativeQuery = true)
	public String fetchCurrencyShortName(Integer currency_type_id);
	
	@Query(value ="Select new map(ct.currency_type_id as id,ct.currency_type_name as currency_type_name,"
			+ "ct.currency_type_short_name as currency_type_short_name,ct.created_date as created_date,"
			+ "ct.modified_date as modified_date,ct.created_by as created_by,ct.modified_by as modified_by,"
			+ "ct.created_username as created_username,ct.modified_username as modified_username,ct.active as active) From Currency_Type ct "
			+ "Where CONCAT(IfNull(ct.currency_type_id,''),'',IfNull(ct.currency_type_name,''),'',"
			+ "IfNull(ct.currency_type_short_name,''),'',IfNull(ct.created_date,''),'',IfNull(ct.created_by,''),'',"
			+ "IfNull(ct.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(ct.currency_type_id as id,ct.currency_type_name as currency_type_name,"
			+ "ct.currency_type_short_name as currency_type_short_name,ct.created_date as created_date,"
			+ "ct.modified_date as modified_date,ct.created_by as created_by,ct.modified_by as modified_by,"
			+ "ct.created_username as created_username,ct.modified_username as modified_username,ct.active as active) From Currency_Type ct")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM Currency_Type ct where ct.currency_type_name=?1 and ct.active=true")
	public Integer countOfCurrencyTypeName(String currency_type_name);
	
	@Query(value = "SELECT count(*) FROM Currency_Type ct where ct.currency_type_short_name=?1 and ct.active=true")
	public Integer countOfCurrencyTypeShortName(String currency_type_short_name);

	@Query(value = "select ct.currency_type_id from currency_type ct "
			+ "where ct.currency_type_name ='USD' And ct.active=true",nativeQuery = true)
	public Integer getCurrencyTypeIdOfUSD();
}
