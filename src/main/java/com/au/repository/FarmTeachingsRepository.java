package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.FarmTeachings;

@Repository
@Transactional
public interface FarmTeachingsRepository extends JpaRepository<FarmTeachings, Integer> {
	
	@Query(value = "select ft from FarmTeachings ft where ft.active=true")
	public List<FarmTeachings> findAll1();
	
	@Query(value = "SELECT new map("
	        + "ft.teachingsId AS id, "
	        + "ft.empId AS empId, "
	        + "ft.month AS month, "
	        + "ft.year AS year, "
	        + "ft.teachingAchieved AS teachingAchieved, "
	        + "ft.teachingProposed AS teachingProposed, "
	        + "ft.teachingMaxMarks AS teachingMaxMarks, "
	        + "ft.teachingSelfMarks AS teachingSelfMarks, "
	        + "ft.innovationsAchieved AS innovationsAchieved, "
	        + "ft.innovationsProposed AS innovationsProposed, "
	        + "ft.innovationsMaxMarks AS innovationsMaxMarks, "
	        + "ft.active AS active, "
	        + "ft.innovationsSelfMarks AS innovationsSelfMarks, "
	        + "ft.courseReductionAchieved AS courseReductionAchieved, "
	        + "ft.courseReductionProposed AS courseReductionProposed, "
	        + "ft.courseReductionMaxMarks AS courseReductionMaxMarks, "
	        + "ft.courseReductionSelfMarks AS courseReductionSelfMarks, "
	        + "ft.attachmentPath AS attachmentPath, "
	        + "ft.proctoringStdAchieved AS proctoringStdAchieved, "
	        + "ft.proctoringStdProposed AS proctoringStdProposed, "
	        + "ft.proctoringStdMaxMarks AS proctoringStdMaxMarks, "
	        + "ft.proctoringStdSelfMarks AS proctoringStdSelfMarks, "
	        + "ft.teachingHodMarks AS teachingHodMarks, "
	        + "ft.innovationsHodMarks AS innovationsHodMarks, "
	        + "ft.courseReductionHodMarks AS courseReductionHodMarks, "
	        + "ft.proctoringStdHodMarks AS proctoringStdHodMarks, "
	        + "ft.createdBy AS createdBy, "
	        + "ft.modifiedBy AS modifiedBy, "
	        + "ft.createdDate AS createdDate, "
	        + "ft.modifiedDate AS modifiedDate, "
	        + "ft.createdUsername AS createdUsername, "
	        + "ft.modifiedUsername AS modifiedUsername) "
	    + "FROM FarmTeachings ft "
		+ "Where CONCAT(IfNull(ft.month,''),'',IfNull(ft.year,''),'',IfNull(ft.createdDate,''),'',IfNull(ft.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query(value = "SELECT new map("
	        + "ft.teachingsId AS id, "
	        + "ft.empId AS empId, "
	        + "ft.month AS month, "
	        + "ft.year AS year, "
	        + "ft.teachingAchieved AS teachingAchieved, "
	        + "ft.teachingProposed AS teachingProposed, "
	        + "ft.teachingMaxMarks AS teachingMaxMarks, "
	        + "ft.teachingSelfMarks AS teachingSelfMarks, "
	        + "ft.innovationsAchieved AS innovationsAchieved, "
	        + "ft.innovationsProposed AS innovationsProposed, "
	        + "ft.innovationsMaxMarks AS innovationsMaxMarks, "
	        + "ft.active AS active, "
	        + "ft.innovationsSelfMarks AS innovationsSelfMarks, "
	        + "ft.courseReductionAchieved AS courseReductionAchieved, "
	        + "ft.courseReductionProposed AS courseReductionProposed, "
	        + "ft.courseReductionMaxMarks AS courseReductionMaxMarks, "
	        + "ft.courseReductionSelfMarks AS courseReductionSelfMarks, "
	        + "ft.attachmentPath AS attachmentPath, "
	        + "ft.proctoringStdAchieved AS proctoringStdAchieved, "
	        + "ft.proctoringStdProposed AS proctoringStdProposed, "
	        + "ft.proctoringStdMaxMarks AS proctoringStdMaxMarks, "
	        + "ft.proctoringStdSelfMarks AS proctoringStdSelfMarks, "
	        + "ft.teachingHodMarks AS teachingHodMarks, "
	        + "ft.innovationsHodMarks AS innovationsHodMarks, "
	        + "ft.courseReductionHodMarks AS courseReductionHodMarks, "
	        + "ft.proctoringStdHodMarks AS proctoringStdHodMarks, "
	        + "ft.createdBy AS createdBy, "
	        + "ft.modifiedBy AS modifiedBy, "
	        + "ft.createdDate AS createdDate, "
	        + "ft.modifiedDate AS modifiedDate, "
	        + "ft.createdUsername AS createdUsername, "
	        + "ft.modifiedUsername AS modifiedUsername) "
	    + "FROM FarmTeachings ft")
	Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update FarmTeachings ft set ft.active=false where ft.teachingsId=?1")
	public void updateFarmTeachings(Integer teachingSubjectId);

	@Modifying
	@Query(value = "update FarmTeachings ft set ft.active=true where ft.teachingsId=?1")
	public void updateFarmTeachings1(Integer teachingSubjectId);

	@Modifying
	@Query(value = "update FarmTeachings ft set ft.attachmentPath=?2 where ft.teachingsId=?1")
	public void updatePath(Integer teachingsId, String t2);

}
