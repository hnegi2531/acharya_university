package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.EventImageAttachments;

@Repository
@Transactional
public interface EventImageAttachmentsRepository extends JpaRepository<EventImageAttachments , Integer>{
	
	@Query(value ="Select eia From EventImageAttachments eia Where eia.event_id =?1 And eia.active=true")
	public List<EventImageAttachments> eventImageAttachmentsDetails(Integer event_id);

}
