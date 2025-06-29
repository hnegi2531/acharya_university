package com.au.repository;

import java.util.HashMap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.HigherEducationAttachments;

@Transactional
@Repository
public interface HigherEducationAttachmentsRepository extends JpaRepository<HigherEducationAttachments,Integer> {
	
	/*
	 * @Modifying
	 * 
	 * @Query(value =
	 * "update HigherEducationAttachments hea set hea.active=false where hea.he_attachment_id=?1"
	 * ) public void updateDeactivateHigherEducationAttachment(Integer
	 * he_attachment_id);
	 * 
	 * @Modifying
	 * 
	 * @Query(value =
	 * "update HigherEducationAttachments hea set hea.active=true where hea.he_attachment_id=?1"
	 * ) public void updateActivateHigherEducationAttachment(Integer
	 * he_attachment_id);
	 */
	@Query(value = "select hea from HigherEducationAttachments hea where hea.job_id=?1")
	public HigherEducationAttachments getAttachmentPath(Integer job_id);
	
	@Query(value = "select hea from HigherEducationAttachments hea where hea.job_id=?1")
	public HigherEducationAttachments getHigherEducationAttachment(Integer job_id);

}
