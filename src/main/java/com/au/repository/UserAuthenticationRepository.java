package com.au.repository;

import java.time.LocalDateTime;
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

import com.au.dto.UserListForLibrary;
import com.au.model.UserAuthentication;

@Transactional
@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Integer> {
	
	@Query(value = "select new map(ua.id as id,ua.username as username,ua.usertype as usertype,ua.guest_type as guest_type,"
			+ "ua.email as email,ua.created_by as created_by,ua.modified_by as modified_by,"
			+ "ua.created_date as created_date,ua.modified_date as modified_date,ua.active as active,"
			+ "ua.created_username as created_username,ua.modified_username as modified_username,ua.usercode as usercode,ua.password as password) "
			+ "from UserAuthentication ua "
			+ "where CONCAT(IfNull(ua.id,''),'',IfNull(ua.username,''),'',IfNull(ua.usertype,''),"
			+ "'',IfNull(ua.created_by,''),'',IfNull(ua.email,''),'',IfNull(ua.usercode,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(ua.id as id,ua.username as username,ua.usertype as usertype,ua.guest_type as guest_type,"
			+ "ua.email as email,ua.created_by as created_by,ua.modified_by as modified_by,"
			+ "ua.created_date as created_date,ua.modified_date as modified_date,ua.active as active,"
			+ "ua.created_username as created_username,ua.modified_username as modified_username,ua.usercode as usercode,ua.password as password) "
			+ "from UserAuthentication ua")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query("select u from UserAuthentication u where BINARY(u.username)=:username And u.active=true")
	UserAuthentication findByUsernameCaseSensitive(String username);

	@Query(value = "select ua from UserAuthentication ua where ua.active=true")
	public List<UserAuthentication> findAll1();
	
	@Query(value = "select ua from UserAuthentication ua where ua.active=true And ua.guest_type=true")
	public List<UserAuthentication> getGuestDetailsData();

	@Modifying
	@Query(value = "update UserAuthentication ua set ua.active=false where ua.id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update UserAuthentication ua set ua.active=true where ua.id=?1")
	public void update1(Integer id);

	@Query(value = "Select new map (sm.submenu_id as submenu_id) from SubMenu sm "
			+ "where user_ids like ?1 or user_ids like ?2 or user_ids like ?3 or sm.user_ids=?4 and sm.active=true")
	public List<HashMap<String, Object>> getSubMenuDetail(String submenu_ids1, String submenu_ids2, String submenu_ids3,
			String submenu_ids4);
	
	@Query("select u from UserAuthentication u where u.id=?1")
	public UserAuthentication findById1(Integer id);
	
	@Query("select u from UserAuthentication u where u.username=?1 and u.active=true")
	public UserAuthentication userDetailsByUsername(String username);
	
	@Query(value = "select ifNull(count(*),0) from user_details ua where ua.username=?1 and ua.active=true", nativeQuery=true)
	public Integer getUsernameCount(String username);
	
	@Query(value = "Select ua.email From UserAuthentication ua Where ua.id=?1")
	public String getEmail(Integer id);
	
	@Modifying
	@Query(value = "update UserAuthentication ua set ua.reset_password_time=?2,ua.reset_password_token=?3 where ua.id=?1")
	public void updatePasswordResetToken(Integer id,LocalDateTime reset_password_time,String reset_password_token);
	
	@Query("select u.reset_password_token from UserAuthentication u where u.reset_password_token=?1 and u.active=true")
	public String fetchResetTokenByToken(String reset_password_token);
	
	@Query("select u.reset_password_time from UserAuthentication u where u.reset_password_token=?1 and u.active=true")
	public LocalDateTime fetchOldResetTimeByToken(String reset_password_token);
	@Modifying
	@Query(value = "update UserAuthentication ua set ua.reset_password_token=null,ua.password=?2 where ua.reset_password_token=?1")
	public void resetPasswordAndToken(String reset_password_token,String passwordEncoder1);
	
	@Query(value = "SELECT count(*) FROM UserAuthentication u where u.username=?1 and u.active=true")
	public Integer countOfUsername(String username);
	
	@Query(value = "SELECT count(*) FROM UserAuthentication u where u.email=?1 and u.active=true")
	public Integer countOfUsername1(String email);
	
	@Query(value ="Select new map(u.id as id,sc.school_id As school_id,CONCAT(IfNull(u.username,''),'-',IfNull(d.dept_name_short,'')) as username_with_department) From UserAuthentication u "
			+ "inner join EmployeeDetails ed on ed.email=u.email "
			+ "left join Schools sc on ed.school_id = sc.school_id "
			+ "inner join Department d on d.dept_id=ed.dept_id where u.active=true")
	public List<HashMap<String, Object>> userDetailswithDepartment();
	
	@Query(value ="Select u.username as username,u.id as user_id,u.email as email From user_details u Where u.id in (?1) and u.usertype LIKE '%student%' and u.active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchAssignedGuestStudentDetails(List<Integer> guest_user_ids);
	
	@Query(value ="Select u.username as username,u.id as user_id,u.email as email From user_details u Where u.id not in (?1) and u.usertype LIKE '%student%' and u.active=true",nativeQuery=true )
	public List<Map<String, Object>> fetchUnAssignedGuestStudentDetails(List<Integer> guest_user_ids);
	
	@Query(value ="Select u.username as username,u.id as user_id,u.email as email From user_details u Where u.usertype LIKE '%student%' and u.active=true",nativeQuery=true )
	public List<Map<String, Object>> fetchGuestStudentDetails();

	@Query(value = "select u.username From UserAuthentication u where u.email=?1 and active=true")
	public String getUsername(String email);
	
	@Modifying
	@Query(value = "update UserAuthentication ua set ua.password=?2 where ua.username=?1 and ua.active=true")
	public void changePassword(String username,String new_password);

	@Query(value = "select new map(ua.id as id,ua.username as username,ua.usertype as usertype,ua.guest_type as guest_type,"
			+ "ua.email as email,ua.created_by as created_by,ua.modified_by as modified_by,"
			+ "ua.created_date as created_date,ua.modified_date as modified_date,ua.active as active,"
			+ "ua.created_username as created_username,ua.modified_username as modified_username,ua.usercode as usercode) "
			+ "from UserAuthentication ua Where ua.active=true And ua.usertype='Staff' ")
	public List<HashMap<String,Object>> staffUserDetails();


	@Query(value ="Select u.username as username,u.id as user_id,u.usercode as usercode,u.email as email From user_details u "
			+ "Where u.id in (?1) and u.active=true",nativeQuery=true )
	public List<Map<String, Object>> getAllSubmenuAssignedUser(List<Integer> id);
	
	Optional<UserAuthentication> findByEmailAndActiveTrue(String email);
	
	@Query(value = "select id from UserRole where role_id=2 and active=true")
	public List<Integer> getOnlyCounselorIds();

	@Query(value = "select r.role_name from Roles r where r.role_id=(select ur.role_id from UserRole ur "
			+ "where ur.id=?1 and ur.active=true) and r.active=true")
	public String checkIfUserIsPrincipal(Integer userId);
	
	@Query(value = "select ed.emp_id from employee_details ed where ed.email = "
			+ "(select ua.email from user_details ua where ua.id=?1 and ua.active=true) and ed.active=true", nativeQuery = true)
	public Integer getEmployee_id(Integer id);

	@Query(value = " select sd.student_id from student_details sd where sd.acharya_email=(select u.email from user_details u where u.id=?1)", nativeQuery = true)
	public Integer getStudentEmail(Integer id);

	@Query(value = " SELECT NEW map(ua.id AS id,st.dept_id as st_dept_id,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id) FROM UserAuthentication ua "
			+ "left join EmployeeDetails ed on ed.email=ua.email " + "left join Department d on ed.dept_id = d.dept_id "
			+ "left join ServiceType st on st.dept_id = d.dept_id " + "where ua.id=?1 ")
	public List<Map<String, Object>> getDeptIdBasedOnUserId(Integer user_id);
	
	
	@Query(value = " SELECT NEW map(ua.id AS id,ed.emp_id as emp_id,sc.school_id As school_id,"
			+ "sc.school_name As school_name,sc.school_name_short As school_name_short) FROM UserAuthentication ua "
			+ "left join EmployeeDetails ed on ed.email=ua.email " 
			+ "left join Schools sc on ed.school_id = sc.school_id "
			+ "where ua.id=?1 And ua.active=true")
	public Map<String, Object> getSchoolIdBasedOnUserId(Integer user_id);

	@Query(value = "select ua.email from UserAuthentication ua where ua.id=?1 And ua.active=true")
	public String getUserEmail(Integer user_id);
	
	@Query(value = "select u From UserAuthentication u where u.email=?1 ")
	public UserAuthentication getUserByEmail(String email);
	
	@Query(value = "select u From UserAuthentication u where u.reset_password_token=?1 and u.active=true")
	public UserAuthentication findByOtp(String otp);
	
	@Modifying
	@Query(value = "update UserAuthentication ua set ua.active=false where ua.email=?1")
	public void deavtivateByEmail(String acharya_email);

	@Query(value = "Select ua.email From UserAuthentication ua Where ua.id=?1 and ua.active=true")
	public String getEmail1(Integer id);

	
	@Query(value = "select new map(ua.id as id,ua.username as username,ua.usertype as usertype,ua.guest_type as guest_type,"
			+ "ua.email as email,ua.created_by as created_by,ua.modified_by as modified_by,"
			+ "ua.created_date as created_date,ua.modified_date as modified_date,ua.active as active,"
			+ "ua.created_username as created_username,ua.modified_username as modified_username,ua.usercode as usercode,ua.password as password) "
			+ "from UserAuthentication ua where ua.id=?1 And ua.active=true")
	public Map<String, Object> getUserData(Integer userId);

	@Query(value = "Select GROUP_CONCAT(ua.username ORDER BY id ASC) From user_details ua where ua.id in (:user_id)", nativeQuery = true)
	public String getCommaSeperartedUserName(List<Integer> user_id);

	@Query(value = "select ua1.id As UserId From user_details ua1 "
			+ "Left join employee_details edh1 on edh1.email=ua1.email "
			+ " where edh1.emp_id = (SELECT edh.leave_approver1_emp_id FROM user_details ua "
			+ "left join employee_details edh on edh.email=ua.email "
			+ " where ua.id=?1 )", nativeQuery = true)
	public List<Map<String, Object>> getleaveApprover(Integer user_id);
	
	

	
	@Modifying
	@Query(value = "update UserAuthentication ua set ua.active=false where ua.email=?1")
	public void deactivateUserByEmail(String email);

	@Query(value = " select new com.au.dto.UserListForLibrary( u.usertype as userType, u.email as email ) from UserAuthentication u", nativeQuery = false)
	public List<UserListForLibrary> getAllUserTypes();

	
	@Query( value = "select ua.id from user_details ua where ua.book_chapter_approver_designation is not null And "
			+ "ua.book_chapter_approver_designation not like '%Dean Research & Development%' "
			+ "And ua.book_chapter_approver_designation not like '%IPR Head%' And ua.active=true",nativeQuery=true)
	public List<Integer> getUserIds();
	
	@Query(value = "select ua.id from user_details ua where ua.book_chapter_approver_designation is not null And "
			+ "ua.book_chapter_approver_designation not like '%Dean Research & Development%' And ua.active=true",nativeQuery=true)
	public List<Integer> getUserIdsPatent();

	@Query(value ="Select ed.emp_id AS emp_id, ed.empcode AS empcode, ed.email AS email,ua.id As userId,ed.employee_name As employee_name,"
			+ "ua.book_chapter_approver_designation As book_chapter_approver_designation From user_details ua " 
			+ "LEFT JOIN employee_details ed ON ed.email = ua.email "
			+ "Where ua.id in (?1) and ed.active=true",nativeQuery=true )
	public List<Map<String, Object>> getEmployeeData1(List<Integer> userIds);

	@Query(value = "select ua from UserAuthentication ua where ua.email=?1 And ua.active=true")
	public UserAuthentication getUserDetailsData(String counsellorEmail);
	
	
	@Query(value = "select ua from user_details ua "
			+ "Inner JOIN employee_details ed ON ed.email = ua.email And ua.active=true Where ed.emp_id=?1 and ed.active.true ",nativeQuery=true)
	public UserAuthentication userDetailsByEmpId(Integer empId);
	
	@Query(value = "select ua from user_details ua "
			+ "Inner JOIN employee_details ed ON ed.email = ua.email And ua.active=true Where ed.empcod=?1 and ed.active.true ",nativeQuery=true)
	public UserAuthentication userDetailsByEmployeeCode(String empCode);

	
	@Query("select u from UserAuthentication u where u.email=?1 And u.active=true")
	public UserAuthentication getUserAuthenticationDetails(String acharya_email);

	@Query(value = "select count(*) from user_details ua where ua.book_chapter_approver_designation=?1 and ua.active=true", nativeQuery=true)
    int approverDesignationAssignmentcheck(String bookChapterApproverDesignation);
}
