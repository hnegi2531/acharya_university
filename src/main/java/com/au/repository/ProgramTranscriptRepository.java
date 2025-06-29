package com.au.repository;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.model.ProgramTranscript;

@Transactional
@Repository
public interface ProgramTranscriptRepository extends JpaRepository<ProgramTranscript,Integer> {
	
	@Query(value = "select pt from ProgramTranscript pt where pt.active=true")
	public List<ProgramTranscript> findAll1();
	
	@Modifying
	@Query(value = "update ProgramTranscript pt set pt.active=false where pt.trans_id=?1")
	public void update(Integer trans_id);
	
	
	@Modifying
	@Query(value = "update ProgramTranscript pt set pt.active=true where pt.trans_id=?1")
	public void update1(Integer trans_id);
	
	@Query(value = "select new map(pt.trans_id as id,pt.transcript as transcript,pt.priority as priority,pt.show_status As show_status,"
			+ "pt.transcript_short_name as transcript_short_name,pt.created_username as created_username,"
			+ "pt.modified_username as modified_username,pt.created_date as created_date,pt.modified_date as modified_date,"
			+ "pt.created_by as created_by,pt.modified_by as modified_by,pt.active as active) from ProgramTranscript pt "
			+ "where CONCAT(IfNull(pt.trans_id,''),'',IfNull(pt.transcript,''),'',IfNull(pt.priority,''),"
			+ "'',IfNull(pt.transcript_short_name,''),'',IfNull(pt.created_by,''),'',IfNull(pt.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(pt.trans_id as id,pt.transcript as transcript,pt.priority as priority,pt.show_status As show_status,"
			+ "pt.transcript_short_name as transcript_short_name,pt.created_username as created_username,"
			+ "pt.modified_username as modified_username,pt.created_date as created_date,pt.modified_date as modified_date,"
			+ "pt.created_by as created_by,pt.modified_by as modified_by,pt.active as active) from ProgramTranscript pt")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "select count(*) from ProgramTranscript pt where pt.transcript=?1 and pt.active=true")
	public Integer getCountTranscript(String transcript);
	
	@Query(value = "select count(*) from ProgramTranscript pt where pt.transcript_short_name=?1 and pt.active=true")
	public Integer getCountTranscriptShortName(String transcript_short_name);
	
	@Query(value = "select count(*) from ProgramTranscript pt where pt.priority=?1 and pt.active=true")
	public Integer getCountPriority(Integer priority);
	
	@Query(value = "Select new map (sm.submenu_id as submenu_id,sm.submenu_name as submenu_name)"
			+ "from SubMenu sm where sm.submenu_id NOT IN :submenu_id and sm.active=true")
	public List<HashMap<String, Object>> fetchUnassignedProgramDetail(@Param("submenu_id") List<Integer> program_id);

}
