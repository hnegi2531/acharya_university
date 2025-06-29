package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.ConsoliatedAmount;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsoliatedAmountRepository extends JpaRepository<ConsoliatedAmount, Integer> {

	@Query(value=" select sum(ca.amount) from ConsoliatedAmount ca where ca.empId=:empId and ca.month=:month and ca.year=:year",nativeQuery = false)
	Float getSumOfAmount(Integer empId,Integer month,Integer year);

	@Query(value = "select ca.consoliatedAmount from ConsoliatedAmount ca where ca.consoliatedAmountId = :consoliatedAmountId ")
	Optional<Float> getConsoliatedAmount(Integer consoliatedAmountId);

	List<ConsoliatedAmount> findByEmpId(Integer empId);
}
