package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.Board_School_Assignment;

@Transactional
@Repository
public interface BoardSchoolAssignmentRepository extends JpaRepository<Board_School_Assignment, Integer>{

	
	@Query(value = "select board_unique_id  from board_school_assignment  where school_id=?1 and active=1",nativeQuery = true)
	public Integer getBoardBySchool(Integer school_id);
	
	
	@Query(value="select new map(bsa.board_school_assignment_id as id,"
			+ "bsa.board_unique_id as board_unique_id,bsa.school_id as school_id,"
			+ "bsa.created_date as created_date,bsa.modified_date as modified_date,bsa.created_by as created_by,"
			+ "bsa.modified_by as modified_by,bsa.active as active,bsa.created_username as created_username,"
			+ "bsa.modified_username as modified_username,boa.board_unique_short_name as board_unique_short_name,"
			+ "boa.board_unique_name as board_unique_name,sc.school_name_short as school_name_short)"
			+ " from Board_School_Assignment bsa left join Board boa on bsa.board_unique_id= boa.board_unique_id "
			+ "left join Schools sc on bsa.school_id=sc.school_id "
			+ "Where CONCAT(IfNull(bsa.created_date ,''),'',IfNull(bsa.created_username,''),'',IfNull(boa.board_unique_short_name,''),'',IfNull(sc.school_name_short,'')) LIKE %?1%")
	public Page<Object> fetchAllDetail1(Pageable pageable, Object keyword);
	
	
	@Query(value="select new map(bsa.board_school_assignment_id as id,"
			+ "bsa.board_unique_id as board_unique_id,bsa.school_id as school_id,"
			+ "bsa.created_date as created_date,bsa.modified_date as modified_date,bsa.created_by as created_by,"
			+ "bsa.modified_by as modified_by,bsa.active as active,bsa.created_username as created_username,"
			+ "bsa.modified_username as modified_username,boa.board_unique_short_name as board_unique_short_name,"
			+ "boa.board_unique_name as board_unique_name,sc.school_name_short as school_name_short)"
			+ " from Board_School_Assignment bsa left join Board boa on bsa.board_unique_id= boa.board_unique_id "
			+ "left join Schools sc on bsa.school_id=sc.school_id ")
	public Page<Object> fetchAllDetail2(Pageable pageable);
	
	@Query(value = "select new map (boa.board_unique_name as board_unique_name,boa.board_unique_short_name as board_unique_short_name,boa.board_unique_id as board_unique_id)"
			+ " from Board_School_Assignment as bs left join Board as boa on bs.board_unique_id= boa.board_unique_id where bs.school_id =?1 and bs.active=true")
			public List<HashMap<String, Object>> getBoardSchool(Integer school_id);
	
	@Query(value="select bsa from Board_School_Assignment bsa where bsa.active=true")
	public List<Board_School_Assignment> findAll1();

	@Modifying
	@Query(value = "update Board_School_Assignment bsa set bsa.active=false where bsa.board_school_assignment_id=?1")
	public void update(Integer id);
	

	@Modifying
	@Query(value = "update Board_School_Assignment bsa set bsa.active=true where bsa.board_school_assignment_id=?1")
	public void update1(Integer id);

}
