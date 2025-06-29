package com.au.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.LegalDepartmentUsers;

@Transactional
@Repository
public interface LegalDepartmentUsersRepository extends JpaRepository<LegalDepartmentUsers, Integer> {
	
	@Query(value = "SELECT count(*) FROM LegalDepartmentUsers ldu where ldu.legal_department_user_name=?1 and ldu.active=true")
	public Integer countOfLegalDepartmentUserName(String legal_department_user_name);
	
	@Query(value = "SELECT count(*) FROM LegalDepartmentUsers ldu where ldu.legal_department_user_password=?1 and ldu.active=true")
	public Integer countOfLegalDepartmentUserPassword(String legal_department_user_password);
	
	@Query(value = "SELECT ldu FROM LegalDepartmentUsers ldu where ldu.legal_validation_token=?1 and ldu.active=true")
	public LegalDepartmentUsers getLegalDepartmentUsersByToken(String legal_validation_token);
	
	@Query(value = "SELECT ldu FROM LegalDepartmentUsers ldu where ldu.legal_department_user_name=?1 and ldu.legal_department_user_password=?2 and ldu.active=true")
	public LegalDepartmentUsers getLegalDepartmentUsers(String legal_department_user_name,String legal_department_user_password);
	
	@Query(value ="Select ldu.legal_department_user_name From LegalDepartmentUsers ldu Where ldu.legal_department_user_name=?1 and ldu.active=true")
	public String legalDepartmentUserName(String legal_department_user_name);
	
	@Query(value = "SELECT ldu FROM LegalDepartmentUsers ldu where ldu.legal_department_user_name=?1 and ldu.active=true")
	public LegalDepartmentUsers getLegalDepartmentUsers(String legal_department_user_name);
	
	@Modifying
	@Query(value = "Update LegalDepartmentUsers ldu Set ldu.legal_department_user_password = ?2 where ldu.legal_department_user_name=?1 and ldu.active=true")
	public void updateLegalDepartmentUsers(String legal_department_user_name,String legal_department_user_password);

}
