package com.au.repository;

import java.util.HashMap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Attachments;

@Transactional
@Repository
public interface AttachmentsRepository extends JpaRepository<Attachments, Integer> {

	@Query(value = "select email from JobProfile jp where jp.job_id=?1")
	public String getEmail(Integer job_id);

	@Query(value = "select firstname from JobProfile jp where jp.job_id=?1")
	public String getName(Integer job_id);

	@Query(value = "select a.attachment_path from Attachments a where a.job_id=?1")
	public String getAllRecipients(Integer job_id);

	@Query(value = "select a from Attachments a where a.job_id=?1")
	public Attachments getAttachmentPath(Integer job_id);
	
	@Query(value = "select a from Attachments a where a.job_id=?1")
	public Attachments getFileName(Integer job_id);
	
	@Query(value = "select count(*) from Attachments a where a.job_id=?1")
	public Integer getcountjobid(Integer job_id);


}
