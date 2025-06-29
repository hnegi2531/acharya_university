package com.au.repository;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.BoardUniversity;



@Transactional
@Repository
public interface BoardUniversityRepository extends JpaRepository<BoardUniversity, Integer> {
	
	@Query(value = "SELECT count(*) FROM BoardUniversity bu where bu.board_university_name=?1 and bu.board_university_type=?2 and bu.active=true")
	public Integer countOfboardName(String board_university_name,String board_university_type);
	
	//@Query(value = "SELECT count(*) FROM BoardUniversity bu where bu.board_university_type=?1 and bu.active=true")
	//public Integer countOfboardtype(String board_university_type);
	
	@Query(value = "SELECT * FROM board_university where active=true", nativeQuery = true)
	public List<BoardUniversity> findAll1();
	
	@Query(value ="Select new map(bu.board_university_id as id,bu.board_university_name as board_university_name,bu.board_university_type as board_university_type,"
			+ "bu.created_date as created_date,bu.modified_date as modified_date,"
			+ "bu.created_by as created_by,bu.modified_by as modified_by,bu.active as active,"
			+ "bu.created_username as created_username,bu.modified_username as modified_username) From BoardUniversity bu "
			+ "Where CONCAT(IfNull(bu.board_university_type,''),'',IfNull(bu.board_university_name,''),'',IfNull(bu.created_date,''),'',IfNull(bu.created_by,''),'',IfNull(bu.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(bu.board_university_id as id,bu.board_university_name as board_university_name,bu.board_university_type as board_university_type,"
			+ "bu.created_date as created_date,bu.modified_date as modified_date,"
			+ "bu.created_by as created_by,bu.modified_by as modified_by,bu.active as active,"
			+ "bu.created_username as created_username,bu.modified_username as modified_username) From BoardUniversity bu ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Modifying
	@Query(value = "update BoardUniversity bu set bu.active=false where bu.board_university_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update BoardUniversity bu set bu.active=true where bu.board_university_id=?1")
	public void update11(Integer id);
	
	@Query(value = "SELECT bu FROM BoardUniversity bu where bu.board_university_type=?1 and bu.active=true")
	public List<BoardUniversity> findAll12(String board_university_type);
	
}
