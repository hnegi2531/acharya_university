package com.au.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.au.model.ServiceTypeTicket;

public interface ServiceTicketRepo extends JpaRepository<ServiceTypeTicket, Integer> {
	
	@Query(value ="SELECT NEW map(s.id AS id,s.complaintDetails AS complaintDetails,s.floorAndExtension AS floorAndExtension,\r\n"
			+ "    s.blockId AS blockId,s.userId AS userId,\r\n"
			+ "    s.tagId AS tagId,s.serviceTypeId AS serviceTypeId,s.ticketStatus AS ticketStatus,s.created_date AS created_date,\r\n"
			+ "    s.modified_date AS modified_date,s.created_by AS created_by,s.modified_by AS modified_by,\r\n"
			+ "    s.active AS active,s.created_username AS created_username,s.modified_username AS modified_username)\r\n"
			+ "    FROM ServiceTypeTicket s \r\n"
			+ "    where s.created_date BETWEEN ?2 AND ?3 "
			+ "    and CONCAT(IfNull(s.complaintDetails,''),'',IfNull(s.active,''),'',IfNull(s.floorAndExtension,''),''\r\n"
			+ "    ) LIKE %?1%")
	Page<Object> findAll1(Pageable pageable, Object keyword,Date fromDate,Date toDate);

	@Query(value ="SELECT NEW map(s.id AS id,s.complaintDetails AS complaintDetails,s.floorAndExtension AS floorAndExtension,\r\n"
			+ "    s.blockId AS blockId,s.userId AS userId,\r\n"
			+ "    s.tagId AS tagId,s.serviceTypeId AS serviceTypeId,s.ticketStatus AS ticketStatus,s.created_date AS created_date,\r\n"
			+ "    s.modified_date AS modified_date,s.created_by AS created_by,s.modified_by AS modified_by,\r\n"
			+ "    s.active AS active,s.created_username AS created_username,s.modified_username AS modified_username)\r\n"
			+ "    FROM ServiceTypeTicket s where s.created_date BETWEEN ?1 AND ?2")
	Page<Object> findAll2(Pageable pageable,Date fromDate,Date toDate);

}
