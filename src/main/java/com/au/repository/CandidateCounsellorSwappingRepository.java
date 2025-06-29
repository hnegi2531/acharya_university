package com.au.repository;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.CandidateCounsellorSwapping;

@Repository
@Transactional
public interface CandidateCounsellorSwappingRepository extends JpaRepository<CandidateCounsellorSwapping, Integer> {
	
	@Query(value = "Select new map(ccs.cousellor_swap_id as id,ccs.candidateId as candidateId,"
			+ "ccs.oldCounsellorId as oldCounsellorId,ccs.newCounsellorId as newCounsellorId,ccs.created_date as created_date,"
			+ "ccs.modified_date as modified_date,ccs.created_by as created_by,ccs.modified_by as modified_by,"
			+ "ccs.created_username as created_username,ccs.modified_username as modified_username,ccs.active as active,"
			+ "ua.username as oldCounsellorName, ua1.username as newCounsellorName) "
			+ "From CandidateCounsellorSwapping ccs "
			+ "left join Candidate_Walkin cw on cw.candidate_id=ccs.candidateId "
			+ "left join UserAuthentication ua on ua.id=ccs.oldCounsellorId "
			+ "left join UserAuthentication ua1 on ua1.id=ccs.newCounsellorId "
			+ "Where CONCAT(IfNull(ccs.cousellor_swap_id,''),'',IfNull(ccs.candidateId,''),'',IfNull(ccs.oldCounsellorId,''),'',IfNull(ccs.created_date,''),'',IfNull(ccs.created_by,''),'',IfNull(ccs.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(ccs.cousellor_swap_id as id,ccs.candidateId as candidateId,"
			+ "ccs.oldCounsellorId as oldCounsellorId,ccs.newCounsellorId as newCounsellorId,ccs.created_date as created_date,"
			+ "ccs.modified_date as modified_date,ccs.created_by as created_by,ccs.modified_by as modified_by,"
			+ "ccs.created_username as created_username,ccs.modified_username as modified_username,ccs.active as active,"
			+ "ua.username as oldCounsellorName, ua1.username as newCounsellorName) "
			+ "From CandidateCounsellorSwapping ccs "
			+ "left join Candidate_Walkin cw on cw.candidate_id=ccs.candidateId "
			+ "left join UserAuthentication ua on ua.id=ccs.oldCounsellorId "
			+ "left join UserAuthentication ua1 on ua1.id=ccs.newCounsellorId ")
	public Page<Object> findAll3(Pageable pageable);

	@Query(value = "Select new map(ccs.cousellor_swap_id as id,ccs.candidateId as candidateId,"
			+ "ccs.oldCounsellorId as oldCounsellorId,ccs.newCounsellorId as newCounsellorId,ccs.created_date as created_date,"
			+ "ccs.modified_date as modified_date,ccs.created_by as created_by,ccs.modified_by as modified_by,"
			+ "ccs.created_username as created_username,ccs.modified_username as modified_username,ccs.active as active,"
			+ "ua.username as oldCounsellorName, ua1.username as newCounsellorName) "
			+ "From CandidateCounsellorSwapping ccs "
			+ "left join Candidate_Walkin cw on cw.candidate_id=ccs.candidateId "
			+ "left join UserAuthentication ua on ua.id=ccs.oldCounsellorId "
			+ "left join UserAuthentication ua1 on ua1.id=ccs.newCounsellorId "
			+ "where ccs.candidateId=?1 ")
	public List<HashMap<String, Object>> findAllByCandidateId(Integer candidateId);

}
