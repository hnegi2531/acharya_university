package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.SectionAssignmentHistory;

@Repository
@Transactional
public interface SectionAssignmentHistoryRepository extends JpaRepository<SectionAssignmentHistory,Integer>{

	@Query(value ="Select sah From SectionAssignmentHistory sah Where sah.section_assignment_id=?1")
	public List<SectionAssignmentHistory> sectionAssignmentHistoryOnSectionAssignmentId(Integer section_assignment_id);
}
