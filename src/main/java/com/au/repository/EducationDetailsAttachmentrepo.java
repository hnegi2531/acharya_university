package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.EducationDetailsAttachment;

@Transactional
@Repository
public interface EducationDetailsAttachmentrepo extends  JpaRepository<EducationDetailsAttachment, Integer> {

	EducationDetailsAttachment findByEmpIdAndGraduationId(Integer eduId, Integer gradId);



	@Query(value = "SELECT eia FROM EducationDetailsAttachment eia where eia.active=true And eia.empId=?1")
	public List<EducationDetailsAttachment> getEducationDocsByEmpId(Integer empId);



	@Modifying
	@Query(value = "update EducationDetailsAttachment eia set eia.active=false where eia.education_details_attachment_id=?1")
	public void updateEducationDetailsAttachment(Integer education_details_attachment_id);
	
}
