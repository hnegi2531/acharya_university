package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.FarmProfessionalActivity;

@Repository
@Transactional
public interface FarmProfessionalActivityRepository  extends JpaRepository<FarmProfessionalActivity, Integer> {
	
	@Query(value = "select fpa from FarmProfessionalActivity fpa where fpa.active=true")
	public List<FarmProfessionalActivity> findAll1();
	
	@Query("SELECT new map("
	        + "fpa.professionalActivityId AS id, "
	        + "fpa.empId AS empId, "
	        + "fpa.month AS month, "
	        + "fpa.year AS year, "
	        + "fpa.professionalAchieved AS professionalAchieved, "
	        + "fpa.professionalProposed AS professionalProposed, "
	        + "fpa.professionalMaxMarks AS professionalMaxMarks, "
	        + "fpa.professionalSelfMarks AS professionalSelfMarks, "
	        + "fpa.publicationsAchieved AS publicationsAchieved, "
	        + "fpa.publicationsProposed AS publicationsProposed, "
	        + "fpa.publicationsMaxMarks AS publicationsMaxMarks, "
	        + "fpa.publicationsSelfMarks AS publicationsSelfMarks, "
	        + "fpa.conferencesAchieved AS conferencesAchieved, "
	        + "fpa.conferencesProposed AS conferencesProposed, "
	        + "fpa.conferencesMaxMarks AS conferencesMaxMarks, "
	        + "fpa.conferencesSelfMarks AS conferencesSelfMarks, "
	        + "fpa.workProgressAchieved AS workProgressAchieved, "
	        + "fpa.workProgressProposed AS workProgressProposed, "
	        + "fpa.workProgressMaxMarks AS workProgressMaxMarks, "
	        + "fpa.workProgressSelfMarks AS workProgressSelfMarks, "
	        + "fpa.grantApplicationsAchieved AS grantApplicationsAchieved, "
	        + "fpa.grantApplicationsProposed AS grantApplicationsProposed, "
	        + "fpa.grantApplicationsMaxMarks AS grantApplicationsMaxMarks, "
	        + "fpa.grantApplicationsSelfMarks AS grantApplicationsSelfMarks, "
	        + "fpa.professionalHodMarks AS professionalHodMarks, "
	        + "fpa.publicationsHodMarks AS publicationsHodMarks, "
	        + "fpa.conferencesHodMarks AS conferencesHodMarks, "
	        + "fpa.workProgressHodMarks AS workProgressHodMarks, "
	        + "fpa.grantApplicationsHodMarks AS grantApplicationsHodMarks, "
	        + "fpa.attachmentPath AS attachmentPath, "
	        + "fpa.active AS active, "
	        + "fpa.createdBy AS createdBy, "
	        + "fpa.modifiedBy AS modifiedBy, "
	        + "fpa.createdDate AS createdDate, "
	        + "fpa.modifiedDate AS modifiedDate, "
	        + "fpa.createdUsername AS createdUsername, "
	        + "fpa.modifiedUsername AS modifiedUsername) "
	        + "FROM FarmProfessionalActivity fpa "
		+ "Where CONCAT(IfNull(fpa.month,''),'',IfNull(fpa.year,''),'',IfNull(fpa.createdDate,''),'',IfNull(fpa.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query("SELECT new map("
	        + "fpa.professionalActivityId AS id, "
	        + "fpa.empId AS empId, "
	        + "fpa.month AS month, "
	        + "fpa.year AS year, "
	        + "fpa.professionalAchieved AS professionalAchieved, "
	        + "fpa.professionalProposed AS professionalProposed, "
	        + "fpa.professionalMaxMarks AS professionalMaxMarks, "
	        + "fpa.professionalSelfMarks AS professionalSelfMarks, "
	        + "fpa.publicationsAchieved AS publicationsAchieved, "
	        + "fpa.publicationsProposed AS publicationsProposed, "
	        + "fpa.publicationsMaxMarks AS publicationsMaxMarks, "
	        + "fpa.publicationsSelfMarks AS publicationsSelfMarks, "
	        + "fpa.conferencesAchieved AS conferencesAchieved, "
	        + "fpa.conferencesProposed AS conferencesProposed, "
	        + "fpa.conferencesMaxMarks AS conferencesMaxMarks, "
	        + "fpa.conferencesSelfMarks AS conferencesSelfMarks, "
	        + "fpa.workProgressAchieved AS workProgressAchieved, "
	        + "fpa.workProgressProposed AS workProgressProposed, "
	        + "fpa.workProgressMaxMarks AS workProgressMaxMarks, "
	        + "fpa.workProgressSelfMarks AS workProgressSelfMarks, "
	        + "fpa.grantApplicationsAchieved AS grantApplicationsAchieved, "
	        + "fpa.grantApplicationsProposed AS grantApplicationsProposed, "
	        + "fpa.grantApplicationsMaxMarks AS grantApplicationsMaxMarks, "
	        + "fpa.grantApplicationsSelfMarks AS grantApplicationsSelfMarks, "
	        + "fpa.professionalHodMarks AS professionalHodMarks, "
	        + "fpa.publicationsHodMarks AS publicationsHodMarks, "
	        + "fpa.conferencesHodMarks AS conferencesHodMarks, "
	        + "fpa.workProgressHodMarks AS workProgressHodMarks, "
	        + "fpa.grantApplicationsHodMarks AS grantApplicationsHodMarks, "
	        + "fpa.attachmentPath AS attachmentPath, "
	        + "fpa.active AS active, "
	        + "fpa.createdBy AS createdBy, "
	        + "fpa.modifiedBy AS modifiedBy, "
	        + "fpa.createdDate AS createdDate, "
	        + "fpa.modifiedDate AS modifiedDate, "
	        + "fpa.createdUsername AS createdUsername, "
	        + "fpa.modifiedUsername AS modifiedUsername) "
	        + "FROM FarmProfessionalActivity fpa "
	)
	Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update FarmProfessionalActivity fpa set fpa.active=false where fpa.professionalActivityId=?1")
	public void updateFarmProfessionalActivity(Integer professionalActivityId);

	@Modifying
	@Query(value = "update FarmProfessionalActivity fpa set fpa.active=true where fpa.professionalActivityId=?1")
	public void updateFarmProfessionalActivity1(Integer professionalActivityId);

	@Modifying
	@Query(value = "update FarmProfessionalActivity fpa set fpa.attachmentPath=?2 where fpa.professionalActivityId=?1")
	public void updatePath(Integer professionalActivityId, String t2);
}
