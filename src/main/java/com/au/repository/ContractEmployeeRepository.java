package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.ContractEmployee;

@Transactional
@Repository
public interface ContractEmployeeRepository extends JpaRepository<ContractEmployee, Integer> {

	@Query(value = "select max(master_code) from ContractEmployee ce")
	public String fetchgetMaxMasterCode();

}
