package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.TdsDeduction;

@Repository
public interface TdsDeductionRepository extends JpaRepository<TdsDeduction, Long> {

	boolean existsByEmpCodeAndMonthAndYear(String empcode, Integer month, Integer year);

	@Query(value=" select new com.au.dto.TdsDeductionDTO( tds.tdsDeductionId as id, tds.empCode as empCode, tds.employeeName as employeeName,"
			+ " tds.amount as amount, tds.createdBy as createdBy, tds.month as month, tds.year as year,"
			+ " u.username as createdByName, tds.created_date as createdDate ) from TdsDeduction tds left join UserAuthentication u on u.id= tds.createdBy order by tds.created_date desc   ")
	Page<TdsDeduction> getAllTds(Pageable pageable);
	
	TdsDeduction findByEmpCodeAndMonthAndYear(String empCode,Integer month,Integer year);
}
