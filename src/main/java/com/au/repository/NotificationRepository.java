package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.Notifications;

@Transactional
@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Integer> {
	
	@Query(value = "select n from Notifications n where n.active=true")
	public List<Notifications> findAll1();
	
	@Modifying
	@Query(value = "update Notifications n set n.active=false where n.notification_id=?1")
	public void delete(Integer notification_id);
	
	@Modifying
	@Query(value = "update Notifications n set n.active=true where n.notification_id=?1")
	public void delete1(Integer notification_id);
	
	@Modifying
	@Query(value = "update Notifications n set n.notification_attach_path=?2 where n.notification_id=?1")
	public void updatePath(Integer notification_id, String t2);
	
	@Query(value = "select * from notification ORDER BY created_date DESC LIMIT 5",nativeQuery=true)
	public List<Notifications> getLatestFiveNotifications();
	
	@Query(value = "SELECT "
			+ "noty.notification_id AS id,"
			+ "noty.school_ids AS school_ids,"
			+ "noty.dept_ids AS dept_ids,"
			+ "noty.notify AS notify,"
			+ "noty.title AS title,"
			+ "noty.description AS description,"
			+ "noty.notification_by AS notification_by,"
			+ "noty.notification_date As notification_date,"
			+ "noty.lock_flag AS lock_flag,"
			+ "noty.sent_flag AS sent_flag,"
			+ "noty.active AS active,"
			+ "noty.created_username AS created_username,"
			+ "noty.modified_username AS modified_username,"
			+ "noty.created_date AS created_date,"
			+ "noty.modified_date AS modified_date,"
			+ "noty.created_by AS created_by,"
			+ "noty.modified_by AS modified_by,"
			+ "noty.notification_attach_path AS notification_attach_path,"
			+ "noty.notify_to AS notify_to,"
			+ "noty.notification_type AS notification_type,"
			+ "(Select GROUP_CONCAT(schools.school_name) From schools Where FIND_IN_SET(schools.school_id,noty.school_ids)) as schools,"
			+ "(Select GROUP_CONCAT(schools.school_name_short) From schools Where FIND_IN_SET(schools.school_id,noty.school_ids)) as schools_short_names,"
			+ "(Select GROUP_CONCAT(department.dept_name) From department Where FIND_IN_SET(department.dept_id,noty.dept_ids)) as departments,"
			+ "(Select GROUP_CONCAT(department.dept_name_short) From department Where FIND_IN_SET(department.dept_id,noty.dept_ids)) as departments_short_name "
			+ "FROM notification AS noty "
			+ "where CONCAT(IfNull(noty.created_username,''),'',IfNull(noty.description,''),'',IfNull(noty.title,''),"
			+ "'',IfNull(ir.created_by,''),'',IfNull(ir.created_date,'')) LIKE %?1%", nativeQuery = true)
	Page<Map<String, Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query(value = "SELECT "
			+ "noty.notification_id AS id,"
			+ "noty.school_ids AS school_ids,"
			+ "noty.dept_ids AS dept_ids,"
			+ "noty.notify AS notify,"
			+ "noty.title AS title,"
			+ "noty.description AS description,"
			+ "noty.notification_by AS notification_by,"
			+ "noty.notification_date As notification_date,"
			+ "noty.lock_flag AS lock_flag,"
			+ "noty.sent_flag AS sent_flag,"
			+ "noty.active AS active,"
			+ "noty.created_username AS created_username,"
			+ "noty.modified_username AS modified_username,"
			+ "noty.created_date AS created_date,"
			+ "noty.modified_date AS modified_date,"
			+ "noty.created_by AS created_by,"
			+ "noty.modified_by AS modified_by,"
			+ "noty.notification_attach_path AS notification_attach_path,"
			+ "noty.notify_to AS notify_to,"
			+ "noty.notification_type AS notification_type,"
			+ "(Select GROUP_CONCAT(schools.school_name) From schools Where FIND_IN_SET(schools.school_id,noty.school_ids)) as schools,"
			+ "(Select GROUP_CONCAT(schools.school_name_short) From schools Where FIND_IN_SET(schools.school_id,noty.school_ids)) as schools_short_names,"
			+ "(Select GROUP_CONCAT(department.dept_name) From department Where FIND_IN_SET(department.dept_id,noty.dept_ids)) as departments,"
			+ "(Select GROUP_CONCAT(department.dept_name_short) From department Where FIND_IN_SET(department.dept_id,noty.dept_ids)) as departments_short_name "
			+ "FROM notification AS noty", nativeQuery = true)
	Page<Map<String, Object>> getAllSortedData(Pageable pageable);

	
	@Query(value = "SELECT DISTINCT "
			+ "        CONCAT(dept.dept_name_short, ' - ', sch.school_name_short) AS dept_school_combined, dept.dept_id as dept_id "
			+ "    FROM employee_details ed "
			+ "    JOIN department dept ON ed.dept_id = dept.dept_id "
			+ "    JOIN schools sch ON ed.school_id = sch.school_id "
			+ "    WHERE ed.school_id IN (:school_ids) and ed.active=true", nativeQuery = true)
	public List<Map<String, Object>> getDepartmentWithSchools(List<Integer> school_ids);

	
	@Query(value = "SELECT COUNT(n.notification_id) AS countNotification FROM notification n WHERE "
            + "n.dept_ids LIKE CONCAT('%,', :dept_ids, ',%') "
            + "OR n.dept_ids LIKE CONCAT(:dept_ids, ',%') "
            + "OR n.dept_ids LIKE CONCAT('%,', :dept_ids) "
            + "OR n.dept_ids = :dept_ids", nativeQuery = true)
	public Map<String, Object> getCountOfNotification(Integer dept_ids);

	@Query(value = "SELECT "
			+ "noty.notification_id AS id,"
			+ "noty.school_ids AS school_ids,"
			+ "noty.dept_ids AS dept_ids,"
			+ "noty.notify AS notify,"
			+ "noty.title AS title,"
			+ "noty.description AS description,"
			+ "noty.notification_by AS notification_by,"
			+ "noty.notification_date As notification_date,"
			+ "noty.lock_flag AS lock_flag,"
			+ "noty.sent_flag AS sent_flag,"
			+ "noty.active AS active,"
			+ "noty.created_username AS created_username,"
			+ "noty.modified_username AS modified_username,"
			+ "noty.created_date AS created_date,"
			+ "noty.modified_date AS modified_date,"
			+ "noty.created_by AS created_by,"
			+ "noty.modified_by AS modified_by,"
			+ "noty.notification_attach_path AS notification_attach_path,"
			+ "noty.notify_to AS notify_to,"
			+ "ed.emp_id As emp_id,ed.emp_image_attachment_path As photo,"
			+ "sc.school_id As schoolId,sc.school_name As schoolName,sc.school_name_short As schoolNameShort,"
			+ "dep.dept_id As DeptId,dep.dept_name_short As DeptNameShort,dep.dept_name As deptName,"
			+ "des.designation_id As designationId,des.designation_name As designationName,des.designation_short_name As designationShortName,"
			+ "noty.notification_type AS notification_type,"
			+ "(Select GROUP_CONCAT(schools.school_name) From schools Where FIND_IN_SET(schools.school_id,noty.school_ids)) as schools,"
			+ "(Select GROUP_CONCAT(schools.school_name_short) From schools Where FIND_IN_SET(schools.school_id,noty.school_ids)) as schools_short_names,"
			+ "(Select GROUP_CONCAT(department.dept_name) From department Where FIND_IN_SET(department.dept_id,noty.dept_ids)) as departments,"
			+ "(Select GROUP_CONCAT(department.dept_name_short) From department Where FIND_IN_SET(department.dept_id,noty.dept_ids)) as departments_short_name "
			+ "FROM notification AS noty  "
			+ "left join user_details ud on ud.id=noty.created_by "
			+ "left join employee_details ed on ed.email=ud.email "
			+ "left join department dep on ed.dept_id=dep.dept_id "
			+ "left join designation des on ed.designation_id=des.designation_id "
			+ "left join schools sc on ed.school_id=sc.school_id "
		    + "Where noty.dept_ids is null OR (noty.dept_ids LIKE CONCAT('%,', :dept_ids, ',%') "
            + "OR noty.dept_ids LIKE CONCAT(:dept_ids, ',%') "
            + "OR noty.dept_ids LIKE CONCAT('%,', :dept_ids) "
			+ "OR noty.dept_ids = :dept_ids) "
			 + "AND noty.created_date >= CURDATE() - INTERVAL 14 DAY "
			+ "ORDER BY noty.created_date DESC" , nativeQuery = true)
	public List<Map<String, Object>> getNotificationDataBasedOnDept(Integer dept_ids);

	@Query(value = "SELECT "
			+ "noty.notification_id AS id,"
			+ "noty.school_ids AS school_ids,"
			+ "noty.dept_ids AS dept_ids,"
			+ "noty.notify AS notify,"
			+ "noty.title AS title,"
			+ "noty.description AS description,"
			+ "noty.notification_by AS notification_by,"
			+ "noty.notification_date As notification_date,"
			+ "noty.lock_flag AS lock_flag,"
			+ "noty.sent_flag AS sent_flag,"
			+ "noty.active AS active,"
			+ "noty.created_username AS created_username,"
			+ "noty.modified_username AS modified_username,"
			+ "noty.created_date AS created_date,"
			+ "noty.modified_date AS modified_date,"
			+ "noty.created_by AS created_by,"
			+ "noty.modified_by AS modified_by,"
			+ "noty.notification_attach_path AS notification_attach_path,"
			+ "noty.notify_to AS notify_to,"
			+ "ed.emp_id As emp_id,ed.emp_image_attachment_path As photo,"
			+ "sc.school_id As schoolId,sc.school_name As schoolName,sc.school_name_short As schoolNameShort,"
			+ "dep.dept_id As DeptId,dep.dept_name_short As DeptNameShort,dep.dept_name As deptName,"
			+ "des.designation_id As designationId,des.designation_name As designationName,des.designation_short_name As designationShortName,"
			+ "noty.notification_type AS notification_type,"
			+ "(Select GROUP_CONCAT(schools.school_name) From schools Where FIND_IN_SET(schools.school_id,noty.school_ids)) as schools,"
			+ "(Select GROUP_CONCAT(schools.school_name_short) From schools Where FIND_IN_SET(schools.school_id,noty.school_ids)) as schools_short_names,"
			+ "(Select GROUP_CONCAT(department.dept_name) From department Where FIND_IN_SET(department.dept_id,noty.dept_ids)) as departments,"
			+ "(Select GROUP_CONCAT(department.dept_name_short) From department Where FIND_IN_SET(department.dept_id,noty.dept_ids)) as departments_short_name "
			+ "FROM notification AS noty "
			+ "left join user_details ud on ud.id=noty.created_by "
			+ "left join employee_details ed on ed.email=ud.email "
			+ "left join department dep on ed.dept_id=dep.dept_id "
			+ "left join designation des on ed.designation_id=des.designation_id "
			+ "left join schools sc on ed.school_id=sc.school_id "
			+ "Where DATE(noty.created_date) = CURRENT_DATE And (noty.dept_ids is null OR  "
		    + "(noty.dept_ids LIKE CONCAT('%,', :dept_ids, ',%') "
            + "OR noty.dept_ids LIKE CONCAT(:dept_ids, ',%') "
            + "OR noty.dept_ids LIKE CONCAT('%,', :dept_ids)) "
			+ "OR noty.dept_ids = :dept_ids) ORDER BY noty.created_date DESC ", nativeQuery = true)
	public List<Map<String, Object>> getNotificationDataOfToday(Integer dept_ids);

	
	@Query(value = "SELECT "
			+ "noty.notification_id AS id,"
			+ "noty.school_ids AS school_ids,"
			+ "noty.dept_ids AS dept_ids,"
			+ "noty.notify AS notify,"
			+ "noty.title AS title,"
			+ "noty.description AS description,"
			+ "noty.notification_by AS notification_by,"
			+ "noty.notification_date As notification_date,"
			+ "noty.lock_flag AS lock_flag,"
			+ "noty.sent_flag AS sent_flag,"
			+ "noty.active AS active,"
			+ "noty.created_username AS created_username,"
			+ "noty.modified_username AS modified_username,"
			+ "noty.created_date AS created_date,"
			+ "noty.modified_date AS modified_date,"
			+ "noty.created_by AS created_by,"
			+ "noty.modified_by AS modified_by,"
			+ "noty.notification_attach_path AS notification_attach_path,"
			+ "noty.notify_to AS notify_to,"
			+ "noty.notification_type AS notification_type,"
			+ "(Select GROUP_CONCAT(schools.school_name) From schools Where FIND_IN_SET(schools.school_id,noty.school_ids)) as schools,"
			+ "(Select GROUP_CONCAT(schools.school_name_short) From schools Where FIND_IN_SET(schools.school_id,noty.school_ids)) as schools_short_names,"
			+ "(Select GROUP_CONCAT(department.dept_name) From department Where FIND_IN_SET(department.dept_id,noty.dept_ids)) as departments,"
			+ "(Select GROUP_CONCAT(department.dept_name_short) From department Where FIND_IN_SET(department.dept_id,noty.dept_ids)) as departments_short_name "
			+ "FROM notification AS noty "
			+ "where (:userId is null or noty.created_by = :userId) And CONCAT(IfNull(noty.created_username,''),'',IfNull(noty.description,''),'',IfNull(noty.title,''),"
			+ "'',IfNull(ir.created_by,''),'',IfNull(ir.created_date,'')) LIKE %:keyword%", nativeQuery = true)
	public Page<Map<String, Object>> fetchAllNotificationsForIndexBasedOnUserByKeyword(Pageable pageable,
			Object keyword, Integer userId);

	@Query(value = "SELECT "
			+ "noty.notification_id AS id,"
			+ "noty.school_ids AS school_ids,"
			+ "noty.dept_ids AS dept_ids,"
			+ "noty.notify AS notify,"
			+ "noty.title AS title,"
			+ "noty.description AS description,"
			+ "noty.notification_by AS notification_by,"
			+ "noty.notification_date As notification_date,"
			+ "noty.lock_flag AS lock_flag,"
			+ "noty.sent_flag AS sent_flag,"
			+ "noty.active AS active,"
			+ "noty.created_username AS created_username,"
			+ "noty.modified_username AS modified_username,"
			+ "noty.created_date AS created_date,"
			+ "noty.modified_date AS modified_date,"
			+ "noty.created_by AS created_by,"
			+ "noty.modified_by AS modified_by,"
			+ "noty.notification_attach_path AS notification_attach_path,"
			+ "noty.notify_to AS notify_to,"
			+ "noty.notification_type AS notification_type,"
			+ "(Select GROUP_CONCAT(schools.school_name) From schools Where FIND_IN_SET(schools.school_id,noty.school_ids)) as schools,"
			+ "(Select GROUP_CONCAT(schools.school_name_short) From schools Where FIND_IN_SET(schools.school_id,noty.school_ids)) as schools_short_names,"
			+ "(Select GROUP_CONCAT(department.dept_name) From department Where FIND_IN_SET(department.dept_id,noty.dept_ids)) as departments,"
			+ "(Select GROUP_CONCAT(department.dept_name_short) From department Where FIND_IN_SET(department.dept_id,noty.dept_ids)) as departments_short_name "
			+ "FROM notification AS noty where (:userId is null or noty.created_by = :userId)", nativeQuery = true)
	public Page<Map<String, Object>> fetchAllNotificationsForIndexBasedOnUserData(Pageable pageable, Integer userId);

	
}
