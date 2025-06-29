package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.Board;

@Transactional
@Repository
public interface BoardRepository extends JpaRepository<Board, Integer>{

	@Query(value="select b from Board b where b.active=true")
	public List<Board> findAll1();

	@Modifying
	@Query(value = "update Board b set b.active=false where b.board_unique_id=?1")
	public void update(Integer fee_template_id);
	

	@Modifying
	@Query(value = "update Board b set b.active=true where b.board_unique_id=?1")
	public void update1(Integer id);

	@Query(value="select * from board where board_unique_name=?1 and board_unique_short_name=?2 and active=true", nativeQuery = true)
	public List<Board> checkExist(String board_unique_name,String board_unique_short_name );
	
	@Query(value = "Select new map(b.board_unique_id as id,b.board_unique_name as board_unique_name,"
			+ "b.board_unique_short_name as board_unique_short_name,b.created_date as created_date,"
			+ "b.modified_date as modified_date,b.created_by as created_by,b.modified_by as modified_by,"
			+ "b.school_id as school_id,b.created_username as created_username,b.modified_username as modified_username,b.active as active) From Board b "
			+ "Where CONCAT(IfNull(b.board_unique_id,''),'',IfNull(b.board_unique_name,''),'',IfNull(b.board_unique_short_name,''),'',IfNull(b.created_date,''),'',IfNull(b.created_by,''),'',IfNull(b.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(b.board_unique_id as id,b.board_unique_name as board_unique_name,"
			+ "b.board_unique_short_name as board_unique_short_name,b.created_date as created_date,"
			+ "b.modified_date as modified_date,b.created_by as created_by,b.modified_by as modified_by,"
			+ "b.school_id as school_id,b.created_username as created_username,b.modified_username as modified_username,b.active as active) From Board b")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM Board b where b.board_unique_name=?1 and b.active=true")
	public Integer countOfBoardUniqueName(String board_unique_name);
	
	@Query(value = "SELECT count(*) FROM Board b where b.board_unique_short_name=?1 and b.active=true")
	public Integer countOfBoardUniqueShortName(String board_unique_short_name);
}
