package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.TallyHead;

@Transactional
@Repository
public interface TallyHeadRepository extends JpaRepository<TallyHead,Integer>{

	@Query(value = "select th from TallyHead th where th.active=true")
	public List<TallyHead> findAll1();
	
	@Modifying
	@Query(value = "update TallyHead th set th.active=false where th.tally_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update TallyHead th set th.active=true where th.tally_id=?1")
	public void update1(Integer id);
	
	@Query(value = "Select new map(th.tally_id as id,th.tally_fee_head as tally_fee_head,th.remarks as remarks,"
			+ "th.created_date as created_date,th.modified_date as modified_date,th.created_by as created_by,"
			+ "th.modified_by as modified_by,th.active as active,th.created_username as created_username,"
			+ "th.modified_username as modified_username) From TallyHead th "
			+ "Where CONCAT(IfNull(th.tally_id,''),'',IfNull(th.tally_fee_head,''),'',IfNull(th.remarks,''),'',IfNull(th.created_date,''),'',IfNull(th.created_by,''),'',IfNull(th.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(th.tally_id as id,th.tally_fee_head as tally_fee_head,th.remarks as remarks,"
			+ "th.created_date as created_date,th.modified_date as modified_date,th.created_by as created_by,"
			+ "th.modified_by as modified_by,th.active as active,th.created_username as created_username,"
			+ "th.modified_username as modified_username) From TallyHead th ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "select count(*) from TallyHead th where th.tally_fee_head=?1 and th.active=true")
	public Integer getTallyFeehead(String tally_fee_head);

}
