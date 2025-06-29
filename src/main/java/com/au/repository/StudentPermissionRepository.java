package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.au.model.StudentPermission;

public interface StudentPermissionRepository extends JpaRepository<StudentPermission, Integer> {

	Boolean existsByAuidAndCurrentSem(String auid, Integer currentSem);

	@Query(value=" select s from StudentPermission s where s.auid=:auid and s.currentSem=:currentSem and s.permissionType=:permissionType and s.active=true ")
	StudentPermission getByAuidAndCurrentSemAndPermissionType(String auid, Integer currentSem,String permissionType);

	
	@Query(value="select s from StudentPermission s where s.active=true order by s.created_date desc ")
	List<StudentPermission> getStudentPermissionList();

	Boolean existsByAuidAndCurrentSemAndPermissionTypeAndActive(String auid, Integer currentSem, String permissionType,
			Boolean true1);

	StudentPermission findByAuidAndCurrentYearAndCurrentSemAndPermissionTypeAndActiveTrue(String auid,
			Integer currentYear, Integer currentSem, String permissionType);

	@Query(value="Select sp.till_date from student_permission sp where sp.auid=?1 And (sp.current_sem=?2 Or sp.current_year=?3) And STR_TO_DATE(sp.till_date, '%Y-%m-%d') >= CURDATE() And sp.active=true",nativeQuery=true)
	String getPermissionDate(String auid, Integer currentSem, Integer currentYear);
}
