package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.AdministrationEmailIds;


@Transactional
@Repository
public interface AdministrationEmailIdsRepository extends JpaRepository<AdministrationEmailIds, Integer>{

	@Query(value = "SELECT ade from AdministrationEmailIds ade where ade.active=true")
	List<AdministrationEmailIds> findAll11();

	
	@Modifying
	@Query(value = "update AdministrationEmailIds ade set ade.active=false where ade.administration_email_id=?1")
	public void deactivateAdministrationEmailIds(Integer id);

	@Modifying
	@Query(value = "update AdministrationEmailIds ade set ade.active=true where ade.administration_email_id=?1")
	public void activateAdministrationEmailIds(Integer id);
	
	
	@Query(value = "select new map(ade.administration_email_id as id,ade.school_id as school_id,ade.email as email,"
			+ "ade.recruit_purpose as recruit_purpose,ade.releave_purpose as releave_purpose,ade.frro as frro,"
			+ "ade.tenure_expired as tenure_expired,ade.email_with_salary as email_with_salary,"
			+ "ade.created_username as created_username,ade.modified_username as modified_username,ade.active as active,"
			+ "ade.created_date as created_date,ade.modified_date as modified_date,ade.created_by as created_by,"
			+ "ua.id as user_id,ua.usertype as usertype,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "ade.modified_by as modified_by ) from AdministrationEmailIds ade "
			+ "left join UserAuthentication ua on ua.email=ade.email "
			+ "left join Schools sc on sc.school_id=ade.school_id "
			+ "where CONCAT(IfNull(ade.email,''),'',IfNull(ade.school_id,''),"
			+ "'',IfNull(ade.created_by,''),'',IfNull(ade.created_date,'',IfNull(ua.id,''),'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	@Query(value = "select new map(ade.administration_email_id as id,ade.school_id as school_id,ade.email as email,"
			+ "ade.recruit_purpose as recruit_purpose,ade.releave_purpose as releave_purpose,ade.frro as frro,"
			+ "ade.tenure_expired as tenure_expired,ade.email_with_salary as email_with_salary,"
			+ "ade.created_username as created_username,ade.modified_username as modified_username,ade.active as active,"
			+ "ade.created_date as created_date,ade.modified_date as modified_date,ade.created_by as created_by,"
			+ "ua.id as user_id,ua.usertype as usertype,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "ade.modified_by as modified_by ) from AdministrationEmailIds ade "
			+ "left join UserAuthentication ua on ua.email=ade.email "
			+ "left join Schools sc on sc.school_id=ade.school_id ")
	public Page<Object> getAllSortedData(Pageable pageable);	
}
