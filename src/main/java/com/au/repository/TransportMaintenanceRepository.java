package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.TransportMaintenance;

@Transactional
@Repository
public interface TransportMaintenanceRepository extends JpaRepository<TransportMaintenance, Integer>{

	
	@Query(value = "SELECT tm from TransportMaintenance tm where tm.active=true")
	public List<TransportMaintenance> getAllActiveTransportMaintenance();
	
	
	@Modifying
	@Query(value = "update TransportMaintenance tm set tm.active=false where tm.transport_maintenance_id=?1")
	public void deactivate(Integer id);
	
	
	@Modifying
	@Query(value = "update TransportMaintenance tm set tm.active=true where tm.transport_maintenance_id=?1")
	public void activate(Integer id);
	
	
	@Query(value = "select new map(tm.transport_maintenance_id as id,tm.created_date as created_date,tm.modified_date as modified_date,tm.created_by as created_by,"
			+ "tm.created_username as created_username,tm.modified_username as modified_username,tm.modified_by as modified_by,tm.active as active,tm.complaintStatus As complaintStatus,"
			+ "tm.service_type_id as service_type_id,tm.maintenance_id as maintenance_id,tm.type_of_vehicle as type_of_vehicle,tm.reporting_date_time as reporting_date_time,"
			+ "tm.duration as duration,tm.reporting_place as reporting_place,tm.report_to_person as report_to_person,tm.report_to_person_number as report_to_person_number,"
			+ "tm.requesting_from_datetime as requesting_from_datetime,tm.requesting_to_datetime as requesting_to_datetime,tm.place_of_visit as place_of_visit,tm.purpose as purpose,"
			+ "tm.request_stage as request_stage,tm.request_status as request_status,tm.alloted_person_name as alloted_person_name,tm.alloted_person_number as alloted_person_number,"
			+ "tm.attending_remarks as attending_remarks,tm.attend_status as attend_status,tm.dateOfAttended as dateOfAttended,tm.dateOfClosed as dateOfClosed,tm.username as username,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "tm.cancelled_status as cancelled_status,tm.dept_id as dept_id,tm.school_id as school_id ) from TransportMaintenance tm "
			+ "left join UserAuthentication ua on ua.id = tm.user_id "
			+ "left join ServiceTicketMaintenance stm on stm.id=tm.maintenance_id "
			+ "left join ServiceType st on st.id=tm.service_type_id "
			+ "left join Department d on tm.dept_id = d.dept_id "
			+ "left join Schools sc on tm.school_id = sc.school_id "
			+ "where (:dept_id IS NULL OR d.dept_id = :dept_id) And (:user_id IS NULL OR tm.user_id = :user_id) "
			+ "And CONCAT(IfNull(tm.service_type_id,''),'',IfNull(tm.maintenance_id,''),"
			+ "'',IfNull(tm.created_by,''),'',IfNull(tm.created_date,'',IfNull(tm.transport_maintenance_id,''),'')) LIKE %:keyword")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer dept_id, Integer user_id); 
	
	
	@Query(value = "select new map(tm.transport_maintenance_id as id,tm.created_date as created_date,tm.modified_date as modified_date,tm.created_by as created_by,"
			+ "tm.created_username as created_username,tm.modified_username as modified_username,tm.modified_by as modified_by,tm.active as active,tm.complaintStatus AS complaintStatus,"
			+ "tm.service_type_id as service_type_id,tm.maintenance_id as maintenance_id,tm.type_of_vehicle as type_of_vehicle,tm.reporting_date_time as reporting_date_time,"
			+ "tm.duration as duration,tm.reporting_place as reporting_place,tm.report_to_person as report_to_person,tm.report_to_person_number as report_to_person_number,"
			+ "tm.requesting_from_datetime as requesting_from_datetime,tm.requesting_to_datetime as requesting_to_datetime,tm.place_of_visit as place_of_visit,tm.purpose as purpose,"
			+ "tm.request_stage as request_stage,tm.request_status as request_status,tm.alloted_person_name as alloted_person_name,tm.alloted_person_number as alloted_person_number,"
			+ "tm.attending_remarks as attending_remarks,tm.attend_status as attend_status,tm.dateOfAttended as dateOfAttended,tm.dateOfClosed as dateOfClosed,tm.username as username,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "tm.cancelled_status as cancelled_status,tm.dept_id as dept_id,tm.school_id as school_id ) from TransportMaintenance tm "
			+ "left join ServiceTicketMaintenance stm on stm.id=tm.maintenance_id "
			+ "left join ServiceType st on st.id=tm.service_type_id "
			+ "left join Department d on tm.dept_id = d.dept_id "
			+ "left join Schools sc on tm.school_id = sc.school_id "
			+ "where (:dept_id IS NULL OR d.dept_id = :dept_id) And (:user_id IS NULL OR tm.user_id = :user_id)")
	public Page<Object> getAllSortedData(Pageable pageable, Integer dept_id, Integer user_id);

	
	
	@Query(value = "select tm.transport_maintenance_id as id,tm.created_date as created_date,tm.modified_date as modified_date,tm.created_by as created_by,"
			+ "tm.created_username as created_username,tm.modified_username as modified_username,tm.modified_by as modified_by,tm.active as active,tm.complaint_status As complaintStatus,"
			+ "tm.service_type_id as service_type_id,tm.maintenance_id as maintenance_id,tm.type_of_vehicle as type_of_vehicle,tm.reporting_date_time as reporting_date_time,"
			+ "tm.duration as duration,tm.reporting_place as reporting_place,tm.report_to_person as report_to_person,tm.report_to_person_number as report_to_person_number,"
			+ "tm.requesting_from_datetime as requesting_from_datetime,tm.requesting_to_datetime as requesting_to_datetime,tm.place_of_visit as place_of_visit,tm.purpose as purpose,"
			+ "tm.request_stage as request_stage,tm.request_status as request_status,tm.alloted_person_name as alloted_person_name,tm.alloted_person_number as alloted_person_number,"
			+ "tm.attending_remarks as attending_remarks,tm.attend_status as attend_status,tm.date_of_attended as date_of_attended,tm.date_of_closed as date_of_closed,tm.username as username,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "tm.cancelled_status as cancelled_status,tm.dept_id as dept_id,tm.school_id as school_id from transport_maintenance tm "
			+ "left join service_ticket_maintenance stm on stm.id=tm.maintenance_id "
			+ "left join service_type st on st.id=tm.service_type_id "
			+ "left join department d on tm.dept_id = d.dept_id "
			+ "left join schools sc on tm.school_id = sc.school_id where tm.transport_maintenance_id=?1 And tm.active=true", nativeQuery = true)
	public Map<String, Object> transportMaintenanceById(Integer id);

	@Query(value = "select new map(tm.transport_maintenance_id as id,tm.created_date as created_date,tm.modified_date as modified_date,tm.created_by as created_by,"
			+ "tm.created_username as created_username,tm.modified_username as modified_username,tm.modified_by as modified_by,tm.active as active,tm.complaintStatus As complaintStatus,"
			+ "tm.service_type_id as service_type_id,tm.maintenance_id as maintenance_id,tm.type_of_vehicle as type_of_vehicle,tm.reporting_date_time as reporting_date_time,"
			+ "tm.duration as duration,tm.reporting_place as reporting_place,tm.report_to_person as report_to_person,tm.report_to_person_number as report_to_person_number,"
			+ "tm.requesting_from_datetime as requesting_from_datetime,tm.requesting_to_datetime as requesting_to_datetime,tm.place_of_visit as place_of_visit,tm.purpose as purpose,"
			+ "tm.request_stage as request_stage,tm.request_status as request_status,tm.alloted_person_name as alloted_person_name,tm.alloted_person_number as alloted_person_number,"
			+ "tm.attending_remarks as attending_remarks,tm.attend_status as attend_status,tm.dateOfAttended as dateOfAttended,tm.dateOfClosed as dateOfClosed,tm.username as username,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "d1.dept_name as empDeptName,d1.dept_name_short as empDeptNameShort,d1.dept_id as empDeptId,"
			+ "tm.cancelled_status as cancelled_status,tm.dept_id as dept_id,tm.school_id as school_id ) from TransportMaintenance tm "
			+ "left join ServiceTicketMaintenance stm on stm.id=tm.maintenance_id "
			+ "left join ServiceType st on st.id=tm.service_type_id "
			+ "left join Department d on tm.dept_id = d.dept_id "
			+ "left join Schools sc on tm.school_id = sc.school_id "
			+ "left join UserAuthentication uad on uad.id = tm.created_by "
			+ "left join EmployeeDetails edd on uad.email = edd.email "
			+ "left join Department d1 on edd.dept_id = d1.dept_id "
			+ "where tm.active=true And (:dept_id IS NULL OR d1.dept_id = :dept_id) "
			+ "And CONCAT(IfNull(tm.service_type_id,''),'',IfNull(tm.maintenance_id,''),"
			+ "'',IfNull(tm.created_by,''),'',IfNull(tm.created_date,'',IfNull(tm.transport_maintenance_id,''),'')) LIKE %:keyword")
	public Page<Object> getAllDataFilteredByKeywordDeptId(Pageable pageable, Object keyword, Integer dept_id);

	@Query(value = "select new map(tm.transport_maintenance_id as id,tm.created_date as created_date,tm.modified_date as modified_date,tm.created_by as created_by,"
			+ "tm.created_username as created_username,tm.modified_username as modified_username,tm.modified_by as modified_by,tm.active as active,tm.complaintStatus As complaintStatus,"
			+ "tm.service_type_id as service_type_id,tm.maintenance_id as maintenance_id,tm.type_of_vehicle as type_of_vehicle,tm.reporting_date_time as reporting_date_time,"
			+ "tm.duration as duration,tm.reporting_place as reporting_place,tm.report_to_person as report_to_person,tm.report_to_person_number as report_to_person_number,"
			+ "tm.requesting_from_datetime as requesting_from_datetime,tm.requesting_to_datetime as requesting_to_datetime,tm.place_of_visit as place_of_visit,tm.purpose as purpose,"
			+ "tm.request_stage as request_stage,tm.request_status as request_status,tm.alloted_person_name as alloted_person_name,tm.alloted_person_number as alloted_person_number,"
			+ "tm.attending_remarks as attending_remarks,tm.attend_status as attend_status,tm.dateOfAttended as dateOfAttended,tm.dateOfClosed as dateOfClosed,tm.username as username,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "d1.dept_name as empDeptName,d1.dept_name_short as empDeptNameShort,d1.dept_id as empDeptId,"
			+ "tm.cancelled_status as cancelled_status,tm.dept_id as dept_id,tm.school_id as school_id ) from TransportMaintenance tm "
			+ "left join ServiceTicketMaintenance stm on stm.id=tm.maintenance_id "
			+ "left join ServiceType st on st.id=tm.service_type_id "
			+ "left join Department d on tm.dept_id = d.dept_id "
			+ "left join Schools sc on tm.school_id = sc.school_id "
			+ "left join UserAuthentication uad on uad.id = tm.created_by "
			+ "left join EmployeeDetails edd on uad.email = edd.email "
			+ "left join Department d1 on edd.dept_id = d1.dept_id "
			+ "where tm.active=true And (:dept_id IS NULL OR d1.dept_id = :dept_id)")
	public Page<Object> getAllSortedDataDeptId(Pageable pageable1, Integer dept_id);
	
	
	@Query(value = "select new map(tm.transport_maintenance_id as id,tm.created_date as created_date,tm.modified_date as modified_date,tm.created_by as created_by,"
			+ "tm.created_username as created_username,tm.modified_username as modified_username,tm.modified_by as modified_by,tm.active as active,tm.complaintStatus As complaintStatus,"
			+ "tm.service_type_id as service_type_id,tm.maintenance_id as maintenance_id,tm.type_of_vehicle as type_of_vehicle,tm.reporting_date_time as reporting_date_time,"
			+ "tm.duration as duration,tm.reporting_place as reporting_place,tm.report_to_person as report_to_person,tm.report_to_person_number as report_to_person_number,"
			+ "tm.requesting_from_datetime as requesting_from_datetime,tm.requesting_to_datetime as requesting_to_datetime,tm.place_of_visit as place_of_visit,tm.purpose as purpose,"
			+ "tm.request_stage as request_stage,tm.request_status as request_status,tm.alloted_person_name as alloted_person_name,tm.alloted_person_number as alloted_person_number,"
			+ "tm.attending_remarks as attending_remarks,tm.attend_status as attend_status,tm.dateOfAttended as dateOfAttended,tm.dateOfClosed as dateOfClosed,tm.username as username,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "d1.dept_name as empDeptName,d1.dept_name_short as empDeptNameShort,d1.dept_id as empDeptId,"
			+ "tm.cancelled_status as cancelled_status,tm.dept_id as dept_id,tm.school_id as school_id ) from TransportMaintenance tm "
			+ "left join ServiceTicketMaintenance stm on stm.id=tm.maintenance_id "
			+ "left join ServiceType st on st.id=tm.service_type_id "
			+ "left join Department d on tm.dept_id = d.dept_id "
			+ "left join Schools sc on tm.school_id = sc.school_id "
			+ "left join UserAuthentication uad on uad.id = tm.created_by "
			+ "left join EmployeeDetails edd on uad.email = edd.email "
			+ "left join Department d1 on edd.dept_id = d1.dept_id "
			+ "where (:dept_id IS NULL OR d1.dept_id = :dept_id) And tm.active=false "
			+ "And CONCAT(IfNull(tm.service_type_id,''),'',IfNull(tm.maintenance_id,''),"
			+ "'',IfNull(tm.created_by,''),'',IfNull(tm.created_date,'',IfNull(tm.transport_maintenance_id,''),'')) LIKE %:keyword")
	public Page<Object> getAllDataFilteredByKeywordHistory(Pageable pageable, Object keyword, Integer dept_id);

	@Query(value = "select new map(tm.transport_maintenance_id as id,tm.created_date as created_date,tm.modified_date as modified_date,tm.created_by as created_by,tm.complaintStatus As complaintStatus,"
			+ "tm.created_username as created_username,tm.modified_username as modified_username,tm.modified_by as modified_by,tm.active as active,"
			+ "tm.service_type_id as service_type_id,tm.maintenance_id as maintenance_id,tm.type_of_vehicle as type_of_vehicle,tm.reporting_date_time as reporting_date_time,"
			+ "tm.duration as duration,tm.reporting_place as reporting_place,tm.report_to_person as report_to_person,tm.report_to_person_number as report_to_person_number,"
			+ "tm.requesting_from_datetime as requesting_from_datetime,tm.requesting_to_datetime as requesting_to_datetime,tm.place_of_visit as place_of_visit,tm.purpose as purpose,"
			+ "tm.request_stage as request_stage,tm.request_status as request_status,tm.alloted_person_name as alloted_person_name,tm.alloted_person_number as alloted_person_number,"
			+ "tm.attending_remarks as attending_remarks,tm.attend_status as attend_status,tm.dateOfAttended as dateOfAttended,tm.dateOfClosed as dateOfClosed,tm.username as username,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "d1.dept_name as empDeptName,d1.dept_name_short as empDeptNameShort,d1.dept_id as empDeptId,"
			+ "tm.cancelled_status as cancelled_status,tm.dept_id as dept_id,tm.school_id as school_id ) from TransportMaintenance tm "
			+ "left join ServiceTicketMaintenance stm on stm.id=tm.maintenance_id "
			+ "left join ServiceType st on st.id=tm.service_type_id "
			+ "left join Department d on tm.dept_id = d.dept_id "
			+ "left join Schools sc on tm.school_id = sc.school_id "
			+ "left join UserAuthentication uad on uad.id = tm.created_by "
			+ "left join EmployeeDetails edd on uad.email = edd.email "
			+ "left join Department d1 on edd.dept_id = d1.dept_id "
			+ "where (:dept_id IS NULL OR d1.dept_id = :dept_id) And tm.active=false")
	public Page<Object> getAllSortedDataHistory(Pageable pageable1, Integer dept_id);

}
