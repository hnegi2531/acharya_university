package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.StageOfTheCase;

@Transactional
@Repository
public interface StageOfTheCaseRepository extends JpaRepository<StageOfTheCase, Integer>{
	
	
	@Query(value = "SELECT sotc FROM StageOfTheCase sotc where sotc.active=true")
	public List<StageOfTheCase> findAll1();

	@Modifying
	@Query(value = "update StageOfTheCase sotc set sotc.active=false where sotc.stage_of_the_case_id=?1")
	public void update(Integer stage_of_the_case_id);

	@Modifying
	@Query(value = "update StageOfTheCase sotc set sotc.active=true where sotc.stage_of_the_case_id=?1")
	public void update1(Integer stage_of_the_case_id);
	
	@Query(value ="Select new map(sotc.stage_of_the_case_id as id,sotc.stage_of_the_case_name as stage_of_the_case_name,"
			+ "sotc.stage_of_the_case_short_name as stage_of_the_case_short_name,sotc.created_date as created_date,sotc.modified_date as modified_date,"
			+ "sotc.active as active) From StageOfTheCase sotc "
			+ "Where CONCAT(IfNull(sotc.stage_of_the_case_id,''),'',IfNull(sotc.stage_of_the_case_name,''),'',IfNull(sotc.stage_of_the_case_short_name,''),'',IfNull(sotc.created_date,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(sotc.stage_of_the_case_id as id,sotc.stage_of_the_case_name as stage_of_the_case_name,"
			+ "sotc.stage_of_the_case_short_name as stage_of_the_case_short_name,sotc.created_date as created_date,sotc.modified_date as modified_date,"
			+ "sotc.active as active) From StageOfTheCase sotc")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM StageOfTheCase sotc where sotc.stage_of_the_case_name=?1 and sotc.active=true")
	public Integer countOfStageOfThecaseName(String stage_of_the_case_name);
	
	@Query(value = "SELECT count(*) FROM StageOfTheCase sotc where sotc.stage_of_the_case_short_name=?1 and sotc.active=true")
	public Integer countOfStageOfTheCaseShortName(String stage_of_the_case_short_name);

}
