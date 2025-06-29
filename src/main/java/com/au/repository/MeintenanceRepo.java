package com.au.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ServiceTicketMaintenance;
import org.springframework.web.bind.annotation.PathVariable;

@Transactional
@Repository
public interface MeintenanceRepo extends JpaRepository<ServiceTicketMaintenance, Long> {
	
	Optional<ServiceTicketMaintenance> findById(Integer id);



	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,stm.attachment_path as attachment_path,"
			+ "stm.serviceTicketId as serviceTicketId,ua1.username as complaintAttendedByName,stm.complaintAttendedBy as complaintAttendedBy,stm.program_specialization_id As program_specialization_id,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,stm.created_username as created_username,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,"
			+ "stm.blockId As blockId,ib.block_name As block_name,ib.block_short_name As block_short_name,stm.event_status As event_status,"
			+ "ua.username as username,ua.usertype as usertype,stm.attendedBy as attendedBy,ua.id as userId,st.id as serviceTypeId,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended, stm.date as date, stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.userId "
			+ "left join UserAuthentication ua1 on ua1.id = stm.complaintAttendedBy "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "left join InfrastructureBlocks ib on stm.blockId = ib.block_id "
			+ "where ua.id=?1 AND d.dept_id = ?2")
	List<Map<String, Object>> getAllServiceByUserIdAndDeptId(Integer user_id, Integer dept_id);
	

	@Query(value = "SELECT stm.service_ticket_id from service_ticket_maintenance stm ORDER BY stm.id DESC LIMIT 1",nativeQuery=true)
	public Integer getLatestData();

	@Query(value = "select * from service_ticket_maintenance ORDER BY id Desc LIMIT 1",nativeQuery = true)
	public ServiceTicketMaintenance getLatestServiceTicketId();



	
	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,stm.attachment_path as attachment_path,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,stm.program_specialization_id As program_specialization_id,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,edd.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "des1.designation_name as empDesignationName,des1.designation_short_name as empDesignationShortName,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,stm.event_status As event_status,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "edd.emp_type_id as emp_type_id,edd.job_type_id as job_type_id,"
			+ "ec.event_id As event_id,ec.event_name As event_name,ec.event_sub_name As event_sub_name,"
			+ "Concat(IfNull(ec.event_name,''),'-',IfNull(st.serviceTypeName,'')) as concateName,"
			+ "stm.blockId As blockId,ib.block_name As block_name,ib.block_short_name As block_short_name,"
			+ "et.empType as empType,et.empTypeShortName as empTypeShortName,jt.job_type as job_type,jt.job_short_name as job_short_name,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join EventCreation ec on ec.created_by = stm.created_by "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join EmployeeDetails ed on ua.email = ed.email "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "left join UserAuthentication uad on uad.id = stm.created_by "
			+ "left join EmployeeDetails edd on uad.email = edd.email "
			+ "left join Designation des1 on des1.designation_id = edd.designation_id "
			+ "left join EmployeeType et on et.empTypeId = edd.emp_type_id "
			+ "left join JobType jt on jt.job_type_id = edd.job_type_id "
			+ "left join InfrastructureBlocks ib on stm.blockId = ib.block_id "
			+ "where stm.created_date BETWEEN ?2 AND ?3 And stm.active=true And d.dept_id=?4 "
			+ "and CONCAT(IfNull(stm.complaintDetails,''),'',IfNull(stm.active,''),'',IfNull(stm.floorAndExtension,''),'') LIKE %?1% Group By stm.id")
	public Page<Object> findAll1(Pageable pageable, Object keyword,Date fromDate,Date toDate, Integer dept_id);

	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,stm.attachment_path as attachment_path,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,stm.program_specialization_id As program_specialization_id,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,stm.event_status As event_status,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,edd.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "des1.designation_name as empDesignationName,des1.designation_short_name as empDesignationShortName,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "edd.emp_type_id as emp_type_id,edd.job_type_id as job_type_id,"
			+ "ec.event_id As event_id,ec.event_name As event_name,ec.event_sub_name As event_sub_name,"
			+ "Concat(IfNull(ec.event_name,''),'-',IfNull(st.serviceTypeName,'')) as concateName,"
			+ "stm.blockId As blockId,ib.block_name As block_name,ib.block_short_name As block_short_name,"
			+ "et.empType as empType,et.empTypeShortName as empTypeShortName,jt.job_type as job_type,jt.job_short_name as job_short_name,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join EventCreation ec on ec.created_by = stm.created_by "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join EmployeeDetails ed on ua.email = ed.email "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "left join UserAuthentication uad on uad.id = stm.created_by "
			+ "left join EmployeeDetails edd on uad.email = edd.email "
			+ "left join Designation des1 on des1.designation_id = edd.designation_id "
			+ "left join EmployeeType et on et.empTypeId = edd.emp_type_id "
			+ "left join JobType jt on jt.job_type_id = edd.job_type_id "
			+ "left join InfrastructureBlocks ib on stm.blockId = ib.block_id "
			+ "where stm.active=true and stm.created_date BETWEEN ?1 AND ?2 And d.dept_id=?3 And stm.active=true Group By stm.id")
	public Page<Object> findAll2(Pageable pageable,Date fromDate,Date toDate, Integer dept_id);
	
	
	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,stm.attachment_path as attachment_path,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,stm.event_status As event_status,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,stm.program_specialization_id As program_specialization_id,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,ed.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "et.empType as empType,et.empTypeShortName as empTypeShortName,jt.job_type as job_type,jt.job_short_name as job_short_name,"
			+ "edd.emp_type_id as emp_type_id,edd.job_type_id as job_type_id,"
			+ "ec.event_id As event_id,ec.event_name As event_name,ec.event_sub_name As event_sub_name,"
			+ "Concat(IfNull(ec.event_name,''),'-',IfNull(st.serviceTypeName,'')) as concateName,"
			+ "stm.blockId As blockId,ib.block_name As block_name,ib.block_short_name As block_short_name,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join EventCreation ec on ec.created_by = stm.created_by "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join EmployeeDetails ed on ua.email = ed.email "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "left join UserAuthentication uad on uad.id = stm.created_by "
			+ "left join EmployeeDetails edd on uad.email = edd.email "  
			+ "left join EmployeeType et on et.empTypeId = edd.emp_type_id "
			+ "left join JobType jt on jt.job_type_id = edd.job_type_id "
			+ "left join InfrastructureBlocks ib on stm.blockId = ib.block_id "
			+ "where stm.active=true and d.dept_id=?1 and "
			+ "CONCAT(IfNull(stm.complaintDetails,''),'',IfNull(stm.complaintStage,''),'',IfNull(stm.floorAndExtension,''),'') LIKE %?1%  Group By stm.id")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer dept_id);

	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.date as date, stm.active AS active,st.showInEvent AS showInEvent,stm.attachment_path as attachment_path,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,stm.program_specialization_id As program_specialization_id,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,stm.event_status As event_status,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,edd.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "des1.designation_name as empDesignationName,des1.designation_short_name as empDesignationShortName,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "edd.emp_type_id as emp_type_id,edd.job_type_id as job_type_id," 
			+ "ec.event_id As event_id,ec.event_name As event_name,ec.event_sub_name As event_sub_name,"
			+ "Concat(IfNull(ec.event_name,''),'-',IfNull(st.serviceTypeName,'')) as concateName,"
			+ "stm.blockId As blockId,ib.block_name As block_name,ib.block_short_name As block_short_name,"
			+ "d1.dept_name as empDeptName,d1.dept_name_short as empDeptNameShort,d1.dept_id as empDeptId,"
			+ "et.empType as empType,et.empTypeShortName as empTypeShortName,jt.job_type as job_type,jt.job_short_name as job_short_name,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join EventCreation ec on ec.created_by = stm.created_by "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join EmployeeDetails ed on ua.email = ed.email "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "left join UserAuthentication uad on uad.id = stm.created_by "
			+ "left join EmployeeDetails edd on uad.email = edd.email "
			+ "left join Designation des1 on des1.designation_id = edd.designation_id "
			+ "left join Department d1 on edd.dept_id = d1.dept_id "
			+ "left join EmployeeType et on et.empTypeId = edd.emp_type_id "
			+ "left join JobType jt on jt.job_type_id = edd.job_type_id "
			+ "left join InfrastructureBlocks ib on stm.blockId = ib.block_id "
			+ "where stm.active=true and d.dept_id=?1  Group By stm.id")
	public Page<Object> getAllSortedData(Pageable pageable, Integer dept_id);
	

	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,stm.attachment_path as attachment_path,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,stm.event_status As event_status,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "stm.blockId As blockId,ib.block_name As block_name,ib.block_short_name As block_short_name,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,stm.program_specialization_id As program_specialization_id,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join InfrastructureBlocks ib on stm.blockId = ib.block_id "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where stm.active=false and "
			+ "CONCAT(IfNull(stm.complaintDetails,''),'',IfNull(stm.complaintStage,''),'',IfNull(stm.floorAndExtension,''),'') LIKE %?1%")
	public Page<Object> fetchAllServiceTypeHistory(Pageable pageable, Object keyword);

	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,stm.attachment_path as attachment_path,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,stm.program_specialization_id As program_specialization_id,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,stm.event_status As event_status,"
			+ "stm.blockId As blockId,ib.block_name As block_name,ib.block_short_name As block_short_name,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join InfrastructureBlocks ib on stm.blockId = ib.block_id "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id  where stm.active=false")
	public Page<Object> fetchAllServiceTypeHistory12(Pageable pageable);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,stm.attachment_path as attachment_path,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.event_status As event_status,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,stm.program_specialization_id As program_specialization_id,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where stm.created_date BETWEEN ?2 AND ?3 And d.dept_id=?4 And stm.complaintStatus=?5 "
			+ "and CONCAT(IfNull(stm.complaintDetails,''),'',IfNull(stm.active,''),'',IfNull(stm.floorAndExtension,''),'') LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeywordWithStatus(Pageable pageable, Object keyword, Date dateTimeFrom,
			Date dateTimeTo, Integer dept_id, String complaintStatus);

	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,stm.attachment_path as attachment_path,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.event_status As event_status,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,stm.program_specialization_id As program_specialization_id,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where stm.created_date BETWEEN ?1 AND ?2 And d.dept_id=?3 And stm.complaintStatus=?4")
	public Page<Object> getAllDataFilteredWithstatus111(Pageable pageable, String fromDate, String toDate, Integer dept_id,
			String complaintStatus);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,stm.attachment_path as attachment_path,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.event_status As event_status,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,stm.program_specialization_id As program_specialization_id,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where stm.complaintStatus=?1 And stm.created_date BETWEEN ?2 AND ?3 "
			+ "and CONCAT(IfNull(stm.complaintDetails,''),'',IfNull(stm.active,''),'',IfNull(stm.floorAndExtension,''),'') LIKE %?1%")
	Page<Object> getAllDataFilteredByKeywordWithStatus(Pageable pageable, Object keyword, String complaintStatus, String fromDate, String toDate);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,stm.attachment_path as attachment_path,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.event_status As event_status,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,stm.program_specialization_id As program_specialization_id,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where stm.created_date BETWEEN ?1 AND ?2 And d.dept_id=?3 And stm.complaintStatus=?4")
	Page<Object> getAllDataFilteredWithstatus1(Pageable pageable, Date dateTimeFrom, Date dateTimeTo, Integer dept_id,
			String complaintStatus);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,stm.attachment_path as attachment_path,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,stm.event_status As event_status,"
			+ "stm.blockId As blockId,ib.block_name As block_name,ib.block_short_name As block_short_name,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,stm.program_specialization_id As program_specialization_id,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join InfrastructureBlocks ib on stm.blockId = ib.block_id "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where stm.active=false And d.dept_id=?1 And "
			+ "CONCAT(IfNull(stm.complaintDetails,''),'',IfNull(stm.complaintStage,''),'',IfNull(stm.floorAndExtension,''),'') LIKE %?1%")
	Page<Object> fetchAllServiceTypeHistory(Pageable pageable, Object keyword2,Integer dept_id);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,stm.attachment_path as attachment_path,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.event_status As event_status,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,stm.program_specialization_id As program_specialization_id,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "stm.blockId As blockId,ib.block_name As block_name,ib.block_short_name As block_short_name,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join InfrastructureBlocks ib on stm.blockId = ib.block_id "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id  where stm.active=false And d.dept_id=?1")
	Page<Object> fetchAllServiceTypeHistory1(Pageable pageable1, Integer dept_id);


	@Query(value = "select count(*) from ServiceTicketMaintenance stm "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where d.dept_id=?1")
	public Integer getTotalCountOfServiceType(Integer dept_id);


	@Query(value = "select count(*) from ServiceTicketMaintenance stm "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where d.dept_id=?1 And stm.complaintStatus Like '%PENDING%'")
	public Integer getPendingStatusCount(Integer dept_id);


	@Query(value = "select count(*) from ServiceTicketMaintenance stm "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where d.dept_id=?1 And stm.complaintStatus Like '%COMPLETED%'")
	public Integer getCompletedStatusCount(Integer dept_id);
	
	@Query(value = "select count(*) from ServiceTicketMaintenance stm "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where d.dept_id=?1 And stm.complaintStatus Like '%UNDERPROCESS%'")
	public Integer getUnderProcessStatusCount(Integer dept_id);


	@Query(value = "select count(*) from ServiceTicketMaintenance stm "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where d.dept_id=?1 And stm.complaintStatus=?2")
	public Integer getCountOfServiceTypeWithStatus(Integer dept_id, String complaintStatus);

	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,stm.event_status As event_status,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,stm.program_specialization_id As program_specialization_id,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where stm.created_date BETWEEN ?1 AND ?2 And d.dept_id=?3 And stm.complaintStatus=?4")
	public Page<Object> getAllDataFilteredWithstatus(Pageable pageable, String fromDate, String toDate,
			String complaintStatus);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.program_specialization_id As program_specialization_id,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,stm.event_status As event_status,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,ed.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join EmployeeDetails ed on ua.email = ed.email "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where stm.created_date BETWEEN ?2 AND ?3 And stm.active=true And d.dept_id=?4 And stm.complaintStatus=?5 "
			+ "and CONCAT(IfNull(stm.complaintDetails,''),'',IfNull(stm.active,''),'',IfNull(stm.floorAndExtension,''),'') LIKE %?1% Group By stm.id")
	public Page<Object> getAllDataFiltered(Pageable pageable, Object keyword, Date dateTimeFrom, Date dateTimeTo,
			Integer dept_id, String complaintStatus);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.program_specialization_id As program_specialization_id,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,stm.event_status As event_status,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,ed.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join EmployeeDetails ed on ua.email = ed.email "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where stm.active=true and d.dept_id=?1 and stm.complaintStatus=?2 And "
			+ "CONCAT(IfNull(stm.complaintDetails,''),'',IfNull(stm.complaintStage,''),'',IfNull(stm.floorAndExtension,''),'') LIKE %?1%  Group By stm.id")
	public Page<Object> getAllData(Pageable pageable, Object keyword, Integer dept_id, String complaintStatus);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.program_specialization_id As program_specialization_id,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,stm.event_status As event_status,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,ed.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join EmployeeDetails ed on ua.email = ed.email "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where stm.active=true and stm.created_date BETWEEN ?1 AND ?2 And d.dept_id=?3 And stm.complaintStatus=?4 Group By stm.id")
	public Page<Object> getAllDataFiltered12(Pageable pageable, Date dateTimeFrom, Date dateTimeTo, Integer dept_id,
			String complaintStatus);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.program_specialization_id As program_specialization_id,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,stm.event_status As event_status,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,ed.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join EmployeeDetails ed on ua.email = ed.email "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id  "
			+ "where stm.active=true and d.dept_id=?1 And stm.complaintStatus=?2 Group By stm.id")
	public Page<Object> getAllData12(Pageable pageable, Integer dept_id, String complaintStatus);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.program_specialization_id As program_specialization_id,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,stm.event_status As event_status,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,ed.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.userId "
			+ "left join EmployeeDetails ed on ua.email = ed.email "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id  "
			+ "where stm.active=true and stm.id=?1 group by stm.id")
	public HashMap<String, Object> getDataForMailing(Long id);


	@Query(value = "SELECT ed.email From employee_details ed where ed.emp_id in (select ed1.emp_id from employee_details ed1 where ed1.dept_id=?1 and ed1.active=true) And ed.active=true ",nativeQuery=true)
	public List<String> getEmployeeData(Integer dept_id);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.program_specialization_id As program_specialization_id,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,stm.event_status As event_status,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,ed.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join EmployeeDetails ed on ua.email = ed.email "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where stm.created_date BETWEEN ?2 AND ?3 And stm.active=true And stm.complaintStatus=?4 "
			+ "and CONCAT(IfNull(stm.complaintDetails,''),'',IfNull(stm.active,''),'',IfNull(stm.floorAndExtension,''),'') LIKE %?1% Group By stm.id")
	public Page<Object> getAllDataFilteredWODept(Pageable pageable, Object keyword, Date dateTimeFrom, Date dateTimeTo,
			String complaintStatus);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.program_specialization_id As program_specialization_id,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,stm.event_status As event_status,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,ed.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join EmployeeDetails ed on ua.email = ed.email "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where stm.active=true and stm.complaintStatus=?1 And "
			+ "CONCAT(IfNull(stm.complaintDetails,''),'',IfNull(stm.complaintStage,''),'',IfNull(stm.floorAndExtension,''),'') LIKE %?1%  Group By stm.id")
	public Page<Object> getAllDataWODept(Pageable pageable, Object keyword, String complaintStatus);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.program_specialization_id As program_specialization_id,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,stm.event_status As event_status,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,ed.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join EmployeeDetails ed on ua.email = ed.email "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where stm.active=true and stm.created_date BETWEEN ?1 AND ?2 And stm.complaintStatus=?3 Group By stm.id")
	public Page<Object> getAllDataFiltered12WODept(Pageable pageable, Date dateTimeFrom, Date dateTimeTo,
			String complaintStatus);


	@Query(value = "SELECT NEW map(stm.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,st.is_attachment as is_attachment,"
			+ "stm.created_date AS createdDate,stm.created_by AS createdBy,stm.active AS active,st.showInEvent AS showInEvent,"
			+ "stm.serviceTicketId as serviceTicketId,stm.complaintAttendedBy as complaintAttendedBy,stm.program_specialization_id As program_specialization_id,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stm.complaintDetails as complaintDetails,stm.floorAndExtension as floorAndExtension,stm.event_status As event_status,"
			+ "stm.created_username as created_username,ua.id as userId,st.id as serviceTypeId,"
			+ "stm.from_date As from_date,stm.to_date As to_date,stm.program_id As program_id,stm.year_sem As year_sem,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,ed.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attendedBy as attendedBy,"
			+ "stm.complaintStage as complaintStage,stm.complaintStatus as complaintStatus,stm.remarks as remarks,"
			+ "stm.dateOfAttended as dateOfAttended,stm.dateOfClosed as dateOfClosed) "
			+ "from ServiceTicketMaintenance stm "
			+ "left join UserAuthentication ua on ua.id = stm.complaintAttendedBy "
			+ "left join EmployeeDetails ed on ua.email = ed.email "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join ServiceType st on stm.serviceTypeId = st.id "
			+ "left join Department d on st.dept_id = d.dept_id  "
			+ "where stm.active=true And stm.complaintStatus='PENDING' Group By stm.id")
	public Page<Object> getAllData12WODept(Pageable pageable, String complaintStatus);

	@Modifying
	@Query(value = "update ServiceTicketMaintenance stm set stm.attachment_path=?2 where stm.id=?1")
	public void updatePath(Long id, String t2);

	
	@Query(value = "SELECT count(*) FROM service_ticket_maintenance stm  "
			+ "left join service_type st on stm.serviceTypeId = st.id "
			+ "where stm.userId=?1 and st.dept_id=?2 and stm.active=true ",nativeQuery = true)
	public Integer getCountOfCombination(Integer userId, Integer dept_id);

	@Query(value = "SELECT stm.complaint_status FROM service_ticket_maintenance stm  "
			+ "left join service_type st on stm.service_type_id = st.id "
			+ "where stm.user_id=?1 and st.dept_id=?2 and stm.active=true order by stm.id desc limit 1 ",nativeQuery = true)
	public	String getcomplainStatus(Integer userId, Integer dept_id);


	long countByUserIdAndDeptIdAndComplaintStatus(Integer userId, Integer deptId, String status);
	long countByUserIdAndServiceTypeIdAndComplaintStatus(Integer userId, Long serviceTypeId, String string);


	@Query(value = "SELECT stm.id As id,stm.attended_by_user_name As attended_by_user_name,stm.complaint_stage As complaint_stage,"
			+ "stm.complaint_status As complaint_status,stm.created_by As created_by,stm.created_username As created_username,"
			+ "stm.date_of_closed As date_of_closed,stm.service_ticket_id As service_ticket_id,stm.service_type_id As service_type_id,"
			+ "stm.event_status As event_status,stm.remarks As remarks,stm.complaint_details AS complaint_details,stm.created_date As created_date,"
			+ "ec.event_id As event_id,ec.event_description As event_description,ec.event_end_time As event_end_time,"
			+ "ec.event_start_time As event_start_time,ec.event_name As event_name,ec.event_sub_name As event_sub_name,ec.guest_name As guest_name,"
			+ "ec.school_id As school_id,ec.approved_status As approved_status,ec.approved_by As approved_by,"
			+ "st.hostel_status as hostel_status,st.is_attachment As is_attachment,st.id as serviceTypeId,stm.floor_and_extension As floor_and_extension,"
			+ "st.service_type_name As service_type_name,st.service_type_short_name As service_type_short_name,st.show_in_event As show_in_event,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,ua.id as userId,stm.attachment_path As attachment_path,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,edd.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "des1.designation_name as empDesignationName,des1.designation_short_name as empDesignationShortName,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,"
			+ "edd.emp_type_id as emp_type_id,edd.job_type_id as job_type_id," 
			+ "ib.block_id As block_id,ib.block_name As block_name,ib.block_short_name As block_short_name,"
			+ "d1.dept_name as empDeptName,d1.dept_name_short as empDeptNameShort,d1.dept_id as empDeptId,"
			+ "jt.job_type as job_type,jt.job_short_name as job_short_name,et.emp_type As emp_type,et.emp_type_id As empTypeId,et.emp_type_short_name As emp_type_short_name,"
			+ "(Select GROUP_CONCAT(schools.school_name_short) From schools Where FIND_IN_SET(schools.school_id,ec.school_id)) as school_name_short,"
			+ "Concat(IfNull(ec.event_name,''),'-',IfNull(st.service_type_name,'')) as concateName "
			+ "from service_ticket_maintenance stm "
			+ "left join event_creation ec on ec.event_id = stm.event_id "
			+ "left join user_details ua on ua.id = stm.complaint_attended_by "
			+ "left join employee_details ed on ua.email = ed.email "
			+ "left join designation des on des.designation_id = ed.designation_id "
			+ "left join service_type st on stm.service_type_id = st.id "
			+ "left join department d on st.dept_id = d.dept_id "
			+ "left join user_details uad on uad.id = stm.created_by "
			+ "left join employee_details edd on uad.email = edd.email "
			+ "left join designation des1 on des1.designation_id = edd.designation_id "
			+ "left join department d1 on edd.dept_id = d1.dept_id "
			+ "left join employee_type et on et.emp_type_id = edd.emp_type_id "
			+ "left join job_type jt on jt.job_type_id = edd.job_type_id "
			+ "left join infrastructure_blocks ib on stm.block_id = ib.block_id "
			+ "where ec.event_status=true And stm.event_status=true And ec.created_by=?2 And  stm.event_id =?3 "
			+ "And CONCAT(IfNull(ec.event_id,''),'',IfNull(ec.event_name,''),'',IfNull(ec.event_sub_name,''),'',"
			+ "IfNull(ec.event_start_time,''),'',IfNull(ec.event_end_time,''),'',IfNull(sc.school_name_short,''),'',"
			+ "IfNull(stm.complaintDetails,''),'',IfNull(stm.complaintStage,''),'',IfNull(stm.floorAndExtension,'')) LIKE %?1% ",nativeQuery=true)
	public  Page<Map<String, Object>> fetchServiceThroughEvent(Pageable pageable, Integer userId,Integer event_id,Object keyword);
	
	@Query(value = "SELECT stm.id As id,stm.attended_by_user_name As attended_by_user_name,stm.complaint_stage As complaint_stage,"
			+ "stm.complaint_status As complaint_status,stm.created_by As created_by,stm.created_username As created_username,"
			+ "stm.date_of_closed As date_of_closed,stm.service_ticket_id As service_ticket_id,stm.service_type_id As service_type_id,"
			+ "stm.event_status As event_status,stm.remarks As remarks,stm.complaint_details AS complaint_details,"
			+ "ec.event_id As event_id,ec.event_description As event_description,ec.event_end_time As event_end_time,stm.created_date As created_date,"
			+ "ec.event_start_time As event_start_time,ec.event_name As event_name,ec.event_sub_name As event_sub_name,ec.guest_name As guest_name,"
			+ "ec.school_id As school_id,ec.approved_status As approved_status,ec.approved_by As approved_by,"
			+ "st.hostel_status as hostel_status,st.is_attachment As is_attachment,st.id as serviceTypeId,"
			+ "st.service_type_name As service_type_name,st.service_type_short_name As service_type_short_name,st.show_in_event As show_in_event,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,ua.id as userId,stm.floor_and_extension As floor_and_extension,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,edd.designation_id as designation_id,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "des1.designation_name as empDesignationName,des1.designation_short_name as empDesignationShortName,"
			+ "ua.username as complaintAttendedByName,ua.usertype as usertype,stm.attachment_path As attachment_path,"
			+ "edd.emp_type_id as emp_type_id,edd.job_type_id as job_type_id," 
			+ "ib.block_id As block_id,ib.block_name As block_name,ib.block_short_name As block_short_name,"
			+ "d1.dept_name as empDeptName,d1.dept_name_short as empDeptNameShort,d1.dept_id as empDeptId,"
			+ "jt.job_type as job_type,jt.job_short_name as job_short_name,et.emp_type As emp_type,et.emp_type_id As empTypeId,et.emp_type_short_name As emp_type_short_name,"
			+ "(Select GROUP_CONCAT(schools.school_name_short) From schools Where FIND_IN_SET(schools.school_id,ec.school_id)) as school_name_short,"
			+ "Concat(IfNull(ec.event_name,''),'-',IfNull(st.service_type_name,'')) as concateName "
			+ "from service_ticket_maintenance stm "
			+ "left join event_creation ec on ec.event_id = stm.event_id "
			+ "left join user_details ua on ua.id = stm.complaint_attended_by "
			+ "left join employee_details ed on ua.email = ed.email "
			+ "left join designation des on des.designation_id = ed.designation_id "
			+ "left join service_type st on stm.service_type_id = st.id "
			+ "left join department d on st.dept_id = d.dept_id "
			+ "left join user_details uad on uad.id = stm.created_by "
			+ "left join employee_details edd on uad.email = edd.email "
			+ "left join designation des1 on des1.designation_id = edd.designation_id "
			+ "left join department d1 on edd.dept_id = d1.dept_id "
			+ "left join employee_type et on et.emp_type_id = edd.emp_type_id "
			+ "left join job_type jt on jt.job_type_id = edd.job_type_id "
			+ "left join infrastructure_blocks ib on stm.block_id = ib.block_id "
			+ "where ec.event_status=true And stm.event_status=true "
			+ "And  ec.created_by=?1 And  stm.event_id =?2",nativeQuery=true)
	public  Page<Map<String, Object>> fetchServiceThroughEventWOKeyword(Pageable pageable1, Integer userId, Integer event_id);





}
