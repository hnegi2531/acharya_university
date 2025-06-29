package com.au.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.YearSem;

@Repository
@Transactional
public interface YearSemRepository extends JpaRepository<YearSem,Integer>{

	@Query(value = "select h from YearSem h where h.active=true")
	public List<YearSem> findAll1();

	@Modifying
	@Query(value = "update YearSem h set h.active=false where h.sem_id=?1")
	public void updateYearSem(Integer id);

	@Modifying
	@Query(value = "update YearSem h set h.active=true where h.sem_id=?1")
	public void updateYearSem1(Integer id);
	
}
