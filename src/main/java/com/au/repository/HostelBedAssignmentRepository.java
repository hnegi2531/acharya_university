package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.dto.HostelBedAssignmentRequest;
import com.au.model.Academic_year;
import com.au.model.HostelBedAssignment;
import com.au.model.HostelBeds;
import com.au.model.HostelBlocks;
import com.au.model.HostelRooms;
import com.au.model.Student_Details;

@Repository
@Transactional
public interface HostelBedAssignmentRepository extends JpaRepository<HostelBedAssignment, Integer> {
	
	
	String CANCELLED_STATUS="NOT CANCELLED";

//	@Query(value="SELECT NEW MAP(hbahostelBedAssignmentId as id, hba.commentsVacate as commentsVacate, hba.remarks as remarks,hba.fromDate as fromDate,hba.toDate as toDate,"
//			+ "hba.confirmJoin as confirmJoin,hba.bookingType as bookingType,hba.cancelHostel as cancelHostel, hba.cancelledRemarks as cancelledRemarks,"
//			+ "hba.studentCancelledRemarks as studentCancelledRemarks,hba.hostelCancelledAttachmentPath as hostelCancelledAttachmentPath,hba.hostelCancelledAttachmentFileName as hostelCancelledAttachmentFileName,"
//			+ "hba.photoUploadStatus as photoUploadStatus,hba.foodStatus as foodStatus,hba.idCardAcStatus as idCardAcStatus) "
//			+ "FROM HostelBedAssignment hba "
//			+  "LEFT JOIN HostelBlocks hb ON hba.hostelBlockId = hb.hostelBlockId "
//			+  "LEFT JOIN HostelFloor hf ON hba.hostelFloorId = hf.hostelFloorId  "
//			+  "LEFT JOIN HostelRooms hr ON hba.hostelRoomId = hr.hostelRoomId "
//			+  "LEFT JOIN Academic_year ac ON hba.acYearId = ac.ac_year_id "
//		    +  "LEFT JOIN HostelBeds hbed ON hba.hostelBedId = hbed.hostelBedId "
//		    +  "LEFT JOIN Student_Details st ON hba.studentId = st.student_id "
//		    +  "LEFT JOIN HostelFeeTemplate hft ON hba.hostelFeeTemplateId = hft.hostel_fee_template_id "
//		    +  "LEFT JOIN UserAuthentication ua ON hba.vacateBy = ua.id")
	@Query("SELECT NEW MAP(hba.hostelBedAssignmentId AS id, " 
			   +"hba.commentsVacate AS commentsVacate, " 
			   +"hba.remarks AS remarks, " 
	           +"hba.fromDate AS fromDate, "
	           +"hba.toDate AS toDate,"
	           +"hba.assigned_year As assigned_year, "
	           +"hba.createdDate AS createdDate, " 
	           +"hba.confirmJoin AS confirmJoin, "
	           +"hba.createdUsername as createdUsername, " 
	           +"hba.bookingType AS bookingType, "
	           +"hba.cancelHostel AS cancelHostel, " 
	           +"hba.cancelledRemarks AS cancelledRemarks, "
	           +"hba.studentCancelledRemarks AS studentCancelledRemarks, "
	           +"hba.hostelCancelledAttachmentPath AS hostelCancelledAttachmentPath, "
	           +"hba.hostelCancelledAttachmentFileName AS hostelCancelledAttachmentFileName, "
	           +"hba.photoUploadStatus AS photoUploadStatus, "
	           +"hba.foodStatus AS foodStatus, "
	           +"hba.idCardAcStatus AS idCardAcStatus,hb.blockName as blockName,hb.blockShortName AS blockShortName, "
	           +"hb.hostelBlockId AS hostelBlockId, hf.hostelFloorId AS hostelFloorId,hf.floorName AS floorName, "
	           +"hr.hostelRoomId AS hostelRoomId,hr.roomName AS roomName,ac.ac_year_id AS acYearId,ac.ac_year AS acYear, "
	           +"hbed.hostelBedId AS hostelBedId,hbed.bedName as bedName,st.student_id AS studentId,st.auid as auid,st.student_name AS studentName, "
	           +"hft.hostel_fee_template_id AS hostelFeeTemplateId,hft.template_name AS templateName,ua.id AS vacateBy, ua.username AS vacatByUsername, "
	           +"rs.current_sem as currentSem,rs.current_year as currentYear,hft.total_amount as totalAmount,hd.paid as paid,hd.due as due,"
			   +"hw.total_amount as waiverAmount,hft.total_amount as hostelFeeTemplateAmount,hw.type as type,sch.school_name as schoolName,sch.school_name_short as schoolNameShort) "
	           +"FROM HostelBedAssignment hba "
	           +"LEFT JOIN hba.hostelBlock hb "
	           +"LEFT JOIN hba.hostelFloor hf "
	           +"LEFT JOIN hba.hostelRoom hr "
	           +"LEFT JOIN hba.acYear ac "
	           +"LEFT JOIN hba.hostelBed hbed "
	           +"LEFT JOIN hba.student st "
	           +"LEFT JOIN hba.hostelFeeTemplate hft "
	           +"LEFT JOIN hba.vacateBy ua "
	           +"LEFT JOIN ReportingStudents rs On rs.student_id=hba.student And rs.active=true "
	           +"LEFT JOIN HostelDue hd On hd.student_id=hba.student And hd.ac_year_id=hba.acYear "
			   +"LEFT JOIN Schools sch On sch.school_id=hba.student.school_id "
			   +"LEFT JOIN HostelWaiver hw On hw.student_id=hba.student And hw.ac_year_id=hba.acYear And hw.active=true "
	           +"Where (:active IS Null OR hba.active=:active) "
	           + "AND (:cancelledStatus IS Null OR (:cancelledStatus = 'NOT CANCELLED' AND IFNULL(hba.cancelledRemarks, 'NOT CANCELLED') = :cancelledStatus "
	           + "OR :cancelledStatus <> 'NOT CANCELLED' AND hba.cancelledRemarks IS NOT NULL)) "
			   + "And (:schoolId Is Null Or hba.student.school_id=:schoolId) And (:acYearId is Null Or hba.acYear.id=:acYearId) And (:blockId is Null Or hb.hostelBlockId=:blockId) "
	           +"And CONCAT(IfNull(hba.foodStatus,''),'',IfNull(hba.createdDate,''),'',IfNull(hba.createdUsername,''),'',"
	           +"IfNull(hb.blockName,''),'',IfNull(hf.floorName,''),'',IfNull(ac.ac_year,''),'',IfNull(st.student_name,''),'',"
			   +"IfNull(hb.blockName,''),'',IfNull(hb.blockShortName,''),'',IfNull(sch.school_name,''),'',IfNull(sch.school_name_short,'')) LIKE %:keyword%")
	Page<Object> filteredAndSortedResponses(Pageable pageable, Object keyword,Boolean active, String cancelledStatus, Integer schoolId, Integer acYearId, Integer blockId);
	
//	@Query(value="SELECT NEW MAP(hbahostelBedAssignmentId as id, hba.commentsVacate as commentsVacate, hba.remarks as remarks,hba.fromDate as fromDate,hba.toDate as toDate,"
//			+ "hba.confirmJoin as confirmJoin,hba.bookingType as bookingType,hba.cancelHostel as cancelHostel, hba.cancelledRemarks as cancelledRemarks,"
//			+ "hba.studentCancelledRemarks as studentCancelledRemarks,hba.hostelCancelledAttachmentPath as hostelCancelledAttachmentPath,hba.hostelCancelledAttachmentFileName as hostelCancelledAttachmentFileName,"
//			+ "hba.photoUploadStatus as photoUploadStatus,hba.foodStatus as foodStatus,hba.idCardAcStatus as idCardAcStatus) "
//			+ "FROM HostelBedAssignment hba "
//			+ "LEFT JOIN HostelBlocks hb ON hba.hostelBlockId = hb.hostelBlockId "
//			+ "LEFT JOIN HostelFloor hf ON hba.hostelFloorId = hf.hostelFloorId  "
//			+ "LEFT JOIN HostelRooms hr ON hba.hostelRoomId = hr.hostelRoomId "
//			+ "LEFT JOIN Academic_year ac ON hba.acYearId = ac.ac_year_id "
//		    + "LEFT JOIN HostelBeds hbed ON hba.hostelBedId = hbed.hostelBedId "
//		    + "LEFT JOIN Student_Details st ON hba.studentId = st.student_id "
//		    + "LEFT JOIN HostelFeeTemplate hft ON hba.hostelFeeTemplateId = hft.hostel_fee_template_id "
//		    + "LEFT JOIN UserAuthentication ua ON hba.vacateBy = ua.id")
	@Query("SELECT NEW MAP(hba.hostelBedAssignmentId AS id, " 
			   +"hba.commentsVacate AS commentsVacate, " 
			   +"hba.remarks AS remarks, " 
	           +"hba.fromDate AS fromDate, "
	           +"hba.toDate AS toDate, "
	           +"hba.createdDate AS createdDate, " 
	           +"hba.confirmJoin AS confirmJoin, "
	           +"hba.assigned_year As assigned_year, "
	           +"hba.createdUsername as createdUsername, " 
	           +"hba.bookingType AS bookingType, "
	           +"hba.cancelHostel AS cancelHostel, " 
	           +"hba.cancelledRemarks AS cancelledRemarks, "
	           +"hba.studentCancelledRemarks AS studentCancelledRemarks, "
	           +"hba.hostelCancelledAttachmentPath AS hostelCancelledAttachmentPath, "
	           +"hba.hostelCancelledAttachmentFileName AS hostelCancelledAttachmentFileName, "
	           +"hba.photoUploadStatus AS photoUploadStatus, "
	           +"hba.foodStatus AS foodStatus, "
	           +"hba.idCardAcStatus AS idCardAcStatus,hb.blockName as blockName,hb.blockShortName AS blockShortName, "
	           +"hb.hostelBlockId AS hostelBlockId, hf.hostelFloorId AS hostelFloorId,hf.floorName AS floorName, "
	           +"hr.hostelRoomId AS hostelRoomId,hr.roomName AS roomName,ac.ac_year_id AS acYearId,ac.ac_year AS acYear, "
	           +"hbed.hostelBedId AS hostelBedId,hbed.bedName as bedName,st.student_id AS studentId,st.auid as auid,st.student_name AS studentName, "
	           +"hft.hostel_fee_template_id AS hostelFeeTemplateId,hft.template_name AS templateName,ua.id AS vacateBy, ua.username AS vacatByUsername,"
	           +"rs.current_sem as currentSem,rs.current_year as currentYear,hft.total_amount as totalAmount,hba.active as active,hd.paid as paid,hd.due as due,"
			   +"hw.total_amount as waiverAmount,hft.total_amount as hostelFeeTemplateAmount,hw.type as type,sch.school_name as schoolName,sch.school_name_short as schoolNameShort) "
	           +"FROM HostelBedAssignment hba "
	           +"LEFT JOIN hba.hostelBlock hb "
	           +"LEFT JOIN hba.hostelFloor hf "
	           +"LEFT JOIN hba.hostelRoom hr "
	           +"LEFT JOIN hba.acYear ac "
	           +"LEFT JOIN hba.hostelBed hbed "
	           +"LEFT JOIN hba.student st "
	           +"LEFT JOIN hba.hostelFeeTemplate hft "
	           +"LEFT JOIN hba.vacateBy ua "
	           +"LEFT JOIN ReportingStudents rs On rs.student_id=hba.student And rs.active=true "
			   +"LEFT JOIN Schools sch On sch.school_id=hba.student.school_id "
			   +"LEFT JOIN HostelWaiver hw On hw.student_id=hba.student And hw.ac_year_id=hba.acYear And hw.active=true "
	           +"LEFT JOIN HostelDue hd On hd.student_id=hba.student And hd.ac_year_id=hba.acYear Where (:active IS Null OR hba.active=:active) "
	           +"AND (:cancelledStatus IS Null OR ((:cancelledStatus = 'NOT CANCELLED' AND IFNULL(hba.cancelledRemarks, 'NOT CANCELLED') = :cancelledStatus) "
	           +"OR (:cancelledStatus = 'CANCELLED' AND hba.cancelledRemarks IS NOT NULL))) "
	           +"And (:schoolId Is Null Or hba.student.school_id=:schoolId) And (:acYearId is Null Or hba.acYear.id=:acYearId) And (:blockId is Null Or hb.hostelBlockId=:blockId) ")
	Page<Object> sortedResponses(Pageable pageable1, Boolean active, String cancelledStatus, Integer schoolId, Integer acYearId, Integer blockId);
	
	@Modifying
	@Query(value="Update HostelBedAssignment Set active=false Where hostelBedAssignmentId=?1")
	void deactiveHostelBedAssignment(Integer hostelBedAssignmentId);
	
	@Modifying
	@Query(value="Update HostelBedAssignment Set active=true Where hostelBedAssignmentId=?1")
	void activateHostelBedAssignment(Integer hostelBedAssignmentId);

	HostelBedAssignment findByHostelRoomAndHostelBedAndActiveTrue(HostelRooms hostelRooms, HostelBeds hostelBeds);

	@Query(value="SELECT hostel_bed_assignment_id From hostel_bed_assignment where ac_year_id=?1 And student_id=?2 And cancelled_remarks IS NULL And active=true",nativeQuery = true)
	Integer getHostelBedAssignmentDetails(Integer ac_year_id, Integer student_id);

	@Query(value="SELECT hostel_bed_id From hostel_bed_assignment where ac_year_id=?1 And student_id=?2 And cancelled_remarks IS NULL And active=true",nativeQuery = true)
	Integer getHostelBedAssignmentData(Integer ac_year_id, Integer student_id);



	List<HostelBedAssignment> findByAcYearAndStudentAndActiveFalse(Academic_year academicYear,
			Student_Details studentDetails);

	@Query(value="SELECT * From hostel_bed_assignment where student_id=?1 And active=true",nativeQuery = true)	
	HostelBedAssignment checkForHostelCancelledOrNot(Integer student_id);

	
	@Query("SELECT NEW MAP(hba.hostelBedAssignmentId AS id, " 
			   +"hba.commentsVacate AS commentsVacate, " 
			   +"hba.remarks AS remarks, " 
	           +"hba.fromDate AS fromDate, "
	           +"hba.toDate AS toDate, "
	           +"hba.assigned_year As assigned_year, "
	           +"hba.createdDate AS createdDate, " 
	           +"hba.confirmJoin AS confirmJoin, "
	           +"hba.createdUsername as createdUsername, " 
	           +"hba.bookingType AS bookingType, "
	           +"hba.cancelHostel AS cancelHostel, " 
	           +"hba.cancelledRemarks AS cancelledRemarks, "
	           +"hba.studentCancelledRemarks AS studentCancelledRemarks, "
	           +"hba.hostelCancelledAttachmentPath AS hostelCancelledAttachmentPath, "
	           +"hba.hostelCancelledAttachmentFileName AS hostelCancelledAttachmentFileName, "
	           +"hba.photoUploadStatus AS photoUploadStatus, "
	           +"hba.foodStatus AS foodStatus, "
	           +"hba.idCardAcStatus AS idCardAcStatus,hb.blockName as blockName,hb.blockShortName AS blockShortName, "
	           +"hb.hostelBlockId AS hostelBlockId, hf.hostelFloorId AS hostelFloorId,hf.floorName AS floorName, "
	           +"hr.hostelRoomId AS hostelRoomId,hr.roomName AS roomName,ac.ac_year_id AS acYearId,ac.ac_year AS acYear, "
	           +"hbed.hostelBedId AS hostelBedId,hbed.bedName as bedName,st.student_id AS studentId,st.auid as auid,st.student_name AS studentName, "
	           +"hft.hostel_fee_template_id AS hostelFeeTemplateId,hft.template_name AS templateName,ua.id AS vacateBy, ua.username AS vacatByUsername,"
	           +"rs.current_sem as currentSem,rs.current_year as currentYear,hft.total_amount as totalAmount,hba.active as active) "
	           +"FROM HostelBedAssignment hba "
	           +"LEFT JOIN hba.hostelBlock hb "
	           +"LEFT JOIN hba.hostelFloor hf "
	           +"LEFT JOIN hba.hostelRoom hr "
	           +"LEFT JOIN hba.acYear ac "
	           +"LEFT JOIN hba.hostelBed hbed "
	           +"LEFT JOIN hba.student st "
	           +"LEFT JOIN hba.hostelFeeTemplate hft "
	           +"LEFT JOIN hba.vacateBy ua "
	           +"LEFT JOIN ReportingStudents rs On rs.student_id=hba.student Where hba.active=true And hba.cancelledRemarks IS NOT NULL ")
	List<HashMap<String,Object>> cancelhostelBedAssignmentDetails();

	List<HostelBedAssignment> findByHostelRoomAndCancelledRemarksIsNullAndActiveTrue(HostelRooms hostelRooms);
	
	@Query(value="SELECT hostel_bed_assignment_id From hostel_bed_assignment where  student_id=?1 And ac_year_id=?2 And cancelled_remarks IS NULL And active=true",nativeQuery = true)
	Integer getAssignedHostelBedId(Integer student_id, Integer academic_year_id);
	
	@Query("SELECT NEW MAP(hba.hostelBedAssignmentId AS hostelBedAssignmentId, " 
			   +"hba.commentsVacate AS commentsVacate, " 
			   +"hba.remarks AS remarks, " 
	           +"hba.fromDate AS fromDate, "
	           +"hba.toDate AS toDate, "
	           +"hba.createdDate AS createdDate, " 
	           +"hba.confirmJoin AS confirmJoin, "
	           +"hba.assigned_year As assigned_year, "
	           +"hba.createdUsername as createdUsername, " 
	           +"hba.bookingType AS bookingType, "
	           +"hba.cancelHostel AS cancelHostel, " 
	           +"hba.cancelledRemarks AS cancelledRemarks, "
	           +"hba.studentCancelledRemarks AS studentCancelledRemarks, "
	           +"hba.hostelCancelledAttachmentPath AS hostelCancelledAttachmentPath, "
	           +"hba.hostelCancelledAttachmentFileName AS hostelCancelledAttachmentFileName, "
	           +"hba.photoUploadStatus AS photoUploadStatus, "
	           +"hba.foodStatus AS foodStatus, "
	           +"hba.idCardAcStatus AS idCardAcStatus,hb.blockName as blockName,hb.blockShortName AS blockShortName, "
	           +"hb.hostelBlockId AS hostelBlockId, hf.hostelFloorId AS hostelFloorId,hf.floorName AS floorName, "
	           +"hr.hostelRoomId AS hostelRoomId,hr.roomName AS roomName,ac.ac_year_id AS acYearId,ac.ac_year AS acYear, "
	           +"hbed.hostelBedId AS hostelBedId,hbed.bedName as bedName,st.student_id AS studentId,st.auid as auid,st.usn as usn,st.student_name AS studentName, "
	           +"hft.hostel_fee_template_id AS hostelFeeTemplateId,hft.template_name AS templateName,ua.id AS vacateBy, ua.username AS vacatByUsername,"
	           +"rs.current_sem as currentSem,rs.current_year as currentYear,hft.total_amount as totalAmount,hba.active as active,"
			   +"COALESCE(hd.paid, 0.0) as paid,COALESCE(hd.due, 0.0) as due,COALESCE(hw.total_amount, 0) as waiverAmount) "
	           +"FROM HostelBedAssignment hba "
	           +"LEFT JOIN hba.hostelBlock hb "
	           +"LEFT JOIN hba.hostelFloor hf "
	           +"LEFT JOIN hba.hostelRoom hr "
	           +"LEFT JOIN hba.acYear ac "
	           +"LEFT JOIN hba.hostelBed hbed "
	           +"LEFT JOIN hba.student st "
	           +"LEFT JOIN hba.hostelFeeTemplate hft "
	           +"LEFT JOIN hba.vacateBy ua "
	           +"LEFT JOIN ReportingStudents rs On rs.student_id=hba.student "
	           +"LEFT JOIN HostelDue hd On hd.student_id=hba.student And hd.ac_year_id=hba.acYear "
			   +"LEFT JOIN HostelWaiver hw On hw.student_id=hba.student And hw.ac_year_id=hba.acYear And hw.active=true "
	           +"Where (:academicYear IS Null OR hba.acYear=:academicYear) AND hba.active=true ")
	List<HashMap<String, Object>> getHostelDueReportByAcademicYearGroupedByBlock(Academic_year academicYear);

	HostelBedAssignment findByAcYearAndStudentAndCancelledRemarksIsNullAndActiveTrue(Academic_year academicYear,
			Student_Details studentDetails);

	List<HostelBedAssignment> findByHostelBlockAndCancelledRemarksIsNullAndActiveTrue(HostelBlocks hostelBlocks);
	
	Boolean existsByAcYearAndStudentAndCancelledRemarksIsNullAndActiveTrue(Academic_year academicYear,
			Student_Details studentDetails);

	HostelBedAssignment findByAcYearAndHostelBedAndCancelledRemarksIsNullAndActiveTrue(Academic_year academicYear,
			HostelBeds hostelBed);

	
	@Query("SELECT NEW MAP(hba.hostelBedAssignmentId AS id, " 
			   +"hba.commentsVacate AS commentsVacate, " 
			   +"hba.remarks AS remarks, " 
	           +"hba.fromDate AS fromDate, "
	           +"hba.toDate AS toDate, "
	           +"hba.assigned_year As assigned_year, "
	           +"hba.createdDate AS createdDate, " 
	           +"hba.confirmJoin AS confirmJoin, "
	           +"hba.createdUsername as createdUsername, " 
	           +"hba.bookingType AS bookingType, "
	           +"hba.cancelHostel AS cancelHostel, " 
	           +"hba.cancelledRemarks AS cancelledRemarks, "
	           +"hba.studentCancelledRemarks AS studentCancelledRemarks, "
	           +"hba.hostelCancelledAttachmentPath AS hostelCancelledAttachmentPath, "
	           +"hba.hostelCancelledAttachmentFileName AS hostelCancelledAttachmentFileName, "
	           +"hba.photoUploadStatus AS photoUploadStatus, "
	           +"hba.foodStatus AS foodStatus, "
	           +"hba.idCardAcStatus AS idCardAcStatus,hb.blockName as blockName,hb.blockShortName AS blockShortName, "
	           +"hb.hostelBlockId AS hostelBlockId, hf.hostelFloorId AS hostelFloorId,hf.floorName AS floorName, "
	           +"hr.hostelRoomId AS hostelRoomId,hr.roomName AS roomName,ac.ac_year_id AS acYearId,ac.ac_year AS acYear, "
	           +"hbed.hostelBedId AS hostelBedId,hbed.bedName as bedName,st.student_id AS studentId,st.auid as auid,st.student_name AS studentName,st.student_image_path As student_image_path, "
	           +"hft.hostel_fee_template_id AS hostelFeeTemplateId,hft.template_name AS templateName,ua.id AS vacateBy, ua.username AS vacatByUsername,"
	           +"sc.school_id As schoolId,sc.school_name As schoolName,sc.school_name_short As schoolNameShort,sc.display_name As displayName,"
	           +"rs.current_sem as currentSem,rs.current_year as currentYear,hft.total_amount as totalAmount,hba.active as active,hd.paid as paid,hd.due as due) "
	           +"FROM HostelBedAssignment hba "
	           +"LEFT JOIN hba.hostelBlock hb "
	           +"LEFT JOIN hba.hostelFloor hf "
	           +"LEFT JOIN hba.hostelRoom hr "
	           +"LEFT JOIN hba.acYear ac "
	           +"LEFT JOIN hba.hostelBed hbed "
	           +"LEFT JOIN hba.student st "
	           +"LEFT JOIN hba.hostelFeeTemplate hft "
	           +"LEFT JOIN hba.vacateBy ua "
	           +"LEFT JOIN ReportingStudents rs On rs.student_id=hba.student And rs.active=true "
	           +"LEFT JOIN Schools sc ON sc.school_id=st.school_id "
	           +"LEFT JOIN HostelDue hd On hd.student_id=hba.student And hd.ac_year_id=hba.acYear "
	           + "WHERE hba.acYear.id = :acYearId AND hba.hostelBlock.id = :hostelBlockId AND hba.active = true")
	List<Map<String, Object>> getStudentDetailDataBasedOnBlock(Integer acYearId, Integer hostelBlockId);

	
	List<HostelBedAssignment> findByStudentAndActiveTrueAndConfirmJoinTrue(Student_Details studentDetails);
	
	@Query("Select new com.au.dto.HostelBedAssignmentRequest(hba as hostelBedAssignment,hbed as hostelBeds,hft as hosteFeeTemplate,st as student) "
	           +"FROM HostelBedAssignment hba "
	           +"LEFT JOIN hba.hostelBed hbed "
	           +"LEFT JOIN hba.student st "
	           +"LEFT JOIN hba.hostelFeeTemplate hft "
	           +"WHERE hba.hostelBedAssignmentId = ?1 ")
	HostelBedAssignmentRequest hostelBedAssignmentById(Integer hostelBedAssignmentId);

	@Query(value="SELECT * From hostel_bed_assignment where student_id=?1 And active=true",nativeQuery = true)	
	List<HostelBedAssignment> getStudentAndActiveTrue(Integer studentId);

	//@Query(value="SELECT * From hostel_bed_assignment where ac_year_id=?1 And student_id=?2 And cancelled_remarks IS NULL And active=true",nativeQuery = true)
	@Query("Select new com.au.dto.HostelBedAssignmentRequest(hba as hostelBedAssignment,hbed as hostelBeds,hft as hosteFeeTemplate,st as student) "
			+"FROM HostelBedAssignment hba "
			+"LEFT JOIN hba.hostelBed hbed "
			+"LEFT JOIN hba.student st "
			+"LEFT JOIN hba.hostelFeeTemplate hft "
			+"WHERE hba.acYear.ac_year_id = ?1 "
			+"AND st.student_id = ?2 "
			+"AND hba.studentCancelledRemarks IS NULL AND hba.active=true ")
	HostelBedAssignmentRequest getHostelBedAssignmentDataByStudentIdAndAcYearId(Integer acYearId, Integer studentId);
}
