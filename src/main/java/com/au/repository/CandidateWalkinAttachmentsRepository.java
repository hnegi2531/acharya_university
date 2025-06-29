package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.CandidateWalkinAttachments;

@Transactional
@Repository
public interface CandidateWalkinAttachmentsRepository extends JpaRepository<CandidateWalkinAttachments , Integer> {
	
	@Query(value =" Select Count(*) From CandidateWalkinAttachments cwa Where cwa.candidate_id =?1 and cwa.attachment_purpose=?2 and cwa.active=true")
	public Integer checkingOfCandidateAttachment(Integer candidate_id,String attachment_purpose);
	
	@Query(value =" Select cwa From CandidateWalkinAttachments cwa Where cwa.candidate_id =?1 and cwa.attachment_purpose=?2 and cwa.active=true")
	public CandidateWalkinAttachments getCandidateAttachment(Integer candidate_id,String attachment_purpose);
	
	@Query(value =" Select cwa From CandidateWalkinAttachments cwa Where cwa.candidate_id =?1 and cwa.active=true")
	public List<CandidateWalkinAttachments> get(Integer candidate_id);
	
	@Query(value =" Select cwa From CandidateWalkinAttachments cwa Where cwa.candidate_id =?1 and cwa.attachment_purpose = 'photo' and cwa.active=true")
	public List<CandidateWalkinAttachments> candidatePhotoAttachmentDetails(Integer candidate_id);

}
