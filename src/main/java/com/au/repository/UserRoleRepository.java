package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.UserRole;

@Transactional
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

	@Query(value = "select ur from UserRole ur where ur.active=true")
	public List<UserRole> findAllUserRole();

	@Modifying
	@Query(value = "update UserRole ur set ur.active=false where ur.user_role_id=?1")
	public void updateToDeactivate(Integer id);

	@Modifying
	@Query(value = "update UserRole ur set ur.active=true where ur.user_role_id=?1")
	public void updateToActivate(Integer id);

	@Query(value = "select new map (ur.user_role_id as id,ur.id as user_id,ur.role_id as role_id,"
			+ "ur.created_by as created_by,ur.modified_by as modified_by,ur.created_date as created_date,"
			+ "ur.modified_date as modified_date,ua.active as active,ur.created_username as created_username,"
			+ "ur.modified_username as modified_username,r.role_name as role_name,r.role_short_name as role_short_name,"
			+ "ua.username as username,ua.book_chapter_approver_designation as book_chapter_approver_designation,"
			+ "ua.email as email,ua.usertype as usertype,ua.usercode as usercode) "
			+ "from UserRole ur left join Roles r on ur.role_id=r.role_id "
			+ "left join UserAuthentication ua on ur.id=ua.id "
			+ "where CONCAT(IfNull(r.role_name,''),'',IfNull(r.role_short_name,''),'',IfNull(ua.username,''),'',IfNull(ur.user_role_id,''),"
			+ "'',IfNull(ur.id,''),'',IfNull(ua.email,''),'',IfNull(ua.usertype,''),'',IfNull(ua.usercode,''),'',IfNull(ur.created_date,''),'',IfNull(ur.created_by,''),"
			+ "'',IfNull(ur.modified_by,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value="select new map (ur.user_role_id as id,ur.id as user_id,ur.role_id as role_id,"
			+ "ur.created_by as created_by,ur.modified_by as modified_by,ur.created_date as created_date,"
			+ "ur.modified_date as modified_date,ua.active as active,ur.created_username as created_username,"
			+ "ur.modified_username as modified_username,r.role_name as role_name,r.role_short_name as role_short_name,ua.username as username,"
			+ "ua.email as email,ua.usertype as usertype,ua.usercode as usercode,ua.book_chapter_approver_designation as book_chapter_approver_designation) "
			+ "from UserRole ur left join Roles r on ur.role_id=r.role_id "
			+ "left join UserAuthentication ua on ur.id=ua.id")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Query(value = "select new map(ur.role_id as role_id,r.role_name as role_name,r.role_short_name as role_short_name,ur.user_role_id as user_role_id, ua.usertype as usertype) "
			+ "from UserRole ur "
			+ "left join UserAuthentication ua on ur.id=ua.id "
			+ "inner join Roles r on ur.role_id=r.role_id where ur.id=?1 and ur.active=true")
	public List<HashMap<String, Object>> fetchRoleDetails(Integer id);

	@Query(value = "select new map(ur.active as active,ur.user_role_id as user_role_id) "
			+ "from UserRole ur where ur.id=?1 and ur.role_id=?2")
	public List<HashMap<String, Object>> fetchRoleDetails(Integer id, Integer role_id);
	
	@Query(value = "select ur from UserRole ur where ur.user_role_id=?1")
	public UserRole findByUserRoleId(Integer user_role_id);
	
	@Query(value = "select new map(ur.role_id as role_id,r.role_name as role_name,"
			+ "r.role_short_name as role_short_name,ur.id as user_id,ua.username as username,ua.usertype as usertype) from UserRole ur "
			+ "Left join Roles r on ur.role_id=r.role_id "
			+ "left join UserAuthentication ua on ur.id=ua.id  where ur.role_id=?1 And ua.usertype='staff' And ur.active=true")
	public List<HashMap<String, Object>> userDetailsByRoleId(Integer role_id);

	@Query(value="select new map (ur.id as user_id,ua.username as username,ua.usertype as usertype,ua.email as email) "
			+ "From UserRole ur left join UserAuthentication ua on ur.id=ua.id where ur.role_id=?1 And ur.active=true")
	public List<HashMap<String, Object>> getUserDetailsBasedOnRole(Integer role_id);
	
	@Modifying
	@Query(value = "update UserRole ur set ur.active=false where ur.id=?1")
	public void deactivateRoleOfUser(Integer id);
	
	@Modifying
	@Query(value = "update UserRole ur set ur.active=true where ur.id=?1")
	public void activateRoleOfUser(Integer id);

	@Query(value = "select ur.role_id from UserRole ur Where ur.id=?1 And ur.active=true")
	public Integer getRoleIdByUserId(Integer userId);
	
	@Query(value = "select case when (count(*) > 0)  then true else false end from UserRole ur "
			+ "Left join Roles r on ur.role_id=r.role_id where ur.id=?1 And (r.role_name='Admin' OR r.role_name='Super Admin')")
	public Boolean checkUserRoleAdminORSuperAdmin(Integer id);
	
}
