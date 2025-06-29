package com.au.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.HostelBedChange;

@Repository
@Transactional
public interface HostelBedChangeRepository extends JpaRepository<HostelBedChange, Integer>{

	@Query(value="Select new map(hbc.hostelBedChangeId as id, hbc.approveStatus as approveStatus,hbc.createdDate as createdDate,hbc.modifiedDate as modifiedDate,"
			+ "hbc.createdUsername as createdUsername,hbc.modifiedUsername as modifiedUsername,hbc.active as active,"
			+ "st.student_id AS studentId,st.auid as auid,st.student_name AS studentName,hb.blockName as changeBlockName,hb.blockShortName AS changeBlockShortName,"
			+ "hb.hostelBlockId AS changeHostelBlockId,hr.hostelRoomId AS changeHostelRoomId,hr.roomName AS changeRoomName,"
			+ "hft.hostel_fee_template_id AS hostelFeeTemplateId,hft.template_name AS templateName,hft.total_amount as totalAmount,hbed.hostelBedId AS hostelBedId,"
			+ "hbed.bedName as changeBedName,ua.id as approverId,ua.username as approverUsername,ac.ac_year_id AS acYearId,ac.ac_year AS acYear,"
			+ "phft.hostel_fee_template_id AS previousHostelFeeTemplateId,phft.template_name AS previousTemplateName,phft.total_amount as previousTotalAmount,"
			+ "pbed.hostelBedId AS previousHostelBedId,pbed.bedName as previousBedName,phr.hostelRoomId AS previousHostelRoomId,phr.roomName AS previousRoomName) From HostelBedChange hbc "
			+ "Left Join hbc.student st "
			+ "Left Join hbc.hostelBlock hb "
			+ "Left Join hbc.hostelRoom hr "
			+ "Left Join hbc.hostelFeeTemplate hft "
			+ "Left Join hbc.hostelBed hbed "
			+ "Left Join hbc.approver ua "
			+ "Left Join hbc.acYear ac "
			+ "Left Join hbc.previousHostelFeeTemplate phft "
			+ "Left Join hbc.previousHostelBed pbed "
			+ "Left Join hbc.previousHostelRoom phr "
			+"Where CONCAT(IfNull(hbc.approveStatus,''),'',IfNull(hbc.createdDate,''),'',IfNull(st.auid,''),'',"
	        +"IfNull(st.student_name,''),'',IfNull(hb.blockName,''),'',IfNull(hr.roomName,''),'',IfNull(hft.template_name,'')) LIKE %?1%")
	Page<Object> filteredAndSortedResponses(Pageable pageable, Object keyword);
	
	@Query(value="Select new map(hbc.hostelBedChangeId as id, hbc.approveStatus as approveStatus,hbc.createdDate as createdDate,hbc.modifiedDate as modifiedDate,"
			+ "hbc.createdUsername as createdUsername,hbc.modifiedUsername as modifiedUsername,hbc.active as active,"
			+ "st.student_id AS studentId,st.auid as auid,st.student_name AS studentName,hb.blockName as changeBlockName,hb.blockShortName AS changeBlockShortName,"
			+ "hb.hostelBlockId AS changeHostelBlockId,hr.hostelRoomId AS changeHostelRoomId,hr.roomName AS changeRoomName,"
			+ "hft.hostel_fee_template_id AS hostelFeeTemplateId,hft.template_name AS templateName,hft.total_amount as totalAmount,hbed.hostelBedId AS hostelBedId,"
			+ "hbed.bedName as changeBedName,ua.id as approverId,ua.username as approverUsername,ac.ac_year_id AS acYearId,ac.ac_year AS acYear,"
			+ "phft.hostel_fee_template_id AS previousHostelFeeTemplateId,phft.template_name AS previousTemplateName,phft.total_amount as previousTotalAmount,"
			+ "pbed.hostelBedId AS previousHostelBedId,pbed.bedName as previousBedName,phr.hostelRoomId AS previousHostelRoomId,phr.roomName AS previousRoomName) From HostelBedChange hbc "
			+ "Left Join hbc.student st "
			+ "Left Join hbc.hostelBlock hb "
			+ "Left Join hbc.hostelRoom hr "
			+ "Left Join hbc.hostelFeeTemplate hft "
			+ "Left Join hbc.hostelBed hbed "
			+ "Left Join hbc.approver ua "
			+ "Left Join hbc.acYear ac "
			+ "Left Join hbc.previousHostelFeeTemplate phft "
			+ "Left Join hbc.previousHostelBed pbed "
			+ "Left Join hbc.previousHostelRoom phr ")
	Page<Object> sortedResponses(Pageable pageable1);

}
