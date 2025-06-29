package com.au.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.DollarToInrConversion;

@Transactional
@Repository
public interface DollarToInrConversionRepository extends JpaRepository<DollarToInrConversion, Integer> {

	

	@Query(value = "select * from dollar_to_inr_conversion where active=true",nativeQuery = true)
	public List<DollarToInrConversion> findAll1();
	
	
	@Query(value = "Select new map(dtucc.dollar_to_inr_id as id,dtucc.dollar_value as dollar_value,dtucc.inr as inr,"
			+ "dtucc.created_username as created_username,dtucc.created_date as created_date,dtucc.created_by as created_by,"
			+ "dtucc.active as active,dtucc.date as date) From DollarToInrConversion dtucc "
			+ "Where CONCAT(IfNull(dtucc.dollar_to_inr_id,''),'',IfNull(dtucc.dollar_value,''),'',IfNull(dtucc.inr,''),'',IfNull(dtucc.created_username,''),'',"
			+ "IfNull(dtucc.date,''),'',IfNull(dtucc.created_by,'')) LIKE %?1%")
	public Page<Object> findAll11(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(dtucc.dollar_to_inr_id as id,dtucc.dollar_value as dollar_value,dtucc.inr as inr,"
			+ "dtucc.created_username as created_username,dtucc.created_date as created_date,dtucc.created_by as created_by,"
			+ "dtucc.active as active,dtucc.date as date) From DollarToInrConversion dtucc ")
	public Page<Object> findAll12(Pageable pageable);
	
	@Modifying
	@Query(value = "update DollarToInrConversion dtucc set dtucc.active=false where dtucc.dollar_to_inr_id=?1")
	public void deActivate(Integer id);

	@Modifying
	@Query(value = "update DollarToInrConversion dtucc set dtucc.active=true where dtucc.dollar_to_inr_id=?1")
	public void activate(Integer id);
	
	@Query(value = "Select * From  dollar_to_inr_conversion dtucc where dtucc.active=true Order by dollar_to_inr_id DESC Limit 1",nativeQuery=true)
	public DollarToInrConversion getDollarToUzbekistaniCurrencyConversion();
	
	@Query(value = "Select * From  dollar_to_inr_conversion dtucc where dtucc.active=true Order by dollar_to_inr_id DESC Limit 1",nativeQuery=true)
	public DollarToInrConversion getDollarToINRCurrencyConversion();
	
	@Query(value ="select * from dollar_to_inr_conversion d where month(d.date)=:month or year(d.date)=:year order by d.date desc limit 1", nativeQuery=true )
	DollarToInrConversion findByDateAndActive(@Param("month") int month,@Param("year") int year);
	
}
