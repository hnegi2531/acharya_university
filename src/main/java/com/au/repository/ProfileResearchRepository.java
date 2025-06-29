package com.au.repository;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.ProfileResearch;

@Repository
@Transactional
public interface ProfileResearchRepository extends JpaRepository<ProfileResearch, Integer> {

	@Query(value = "select h from ProfileResearch h where h.active=true")
	public List<ProfileResearch> findAll1();
	
	@Query(value ="Select new map(pr.profileResearchId as id,pr.tenureStatus as tenureStatus,pr.universityName as universityName,"
			+ "pr.titleOfThesis as titleOfThesis,pr.peerViewed as peerViewed,pr.noOfConferences as noOfConferences,"
			+ "pr.professionalOrganisation as professionalOrganisation,pr.partOfResearchProject as partOfResearchProject,pr.yesNumberOfProjects as yesNumberOfProjects,pr.keywordsResearch as keywordsResearch,"
			+ "pr.techniquesExpert as techniquesExpert,pr.currentProfessional as currentProfessional,pr.areasOfExpertise as areasOfExpertise,"
			+ "pr.researchForCollaboration as researchForCollaboration,pr.researchAttachment as researchAttachment,pr.phdRegisterDate as phdRegisterDate,"
			+ "pr.phdCompletedDate as phdCompletedDate,pr.googleScholar as googleScholar,pr.otherCitationDatabase as otherCitationDatabase,"
			+ "pr.phdHolderPursuing as phdHolderPursuing,pr.phdCount as phdCount,pr.linkedInLink as linkedInLink,"
			+ "pr.createdBy as createdBy,pr.modifiedBy as modifiedBy,pr.createdDate as createdDate,"
			+ "pr.modifiedDate as modifiedDate,pr.createdUsername as createdUsername, pr.modifiedUsername as modifiedUsername ,pr.active as active, "
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "dept.dept_name_short as dept_name_short,dept.dept_name as dept_name,sch.school_name as school_name, sch.school_name_short as school_name_short) From ProfileResearch pr "
			+ "left join EmployeeDetails ed on ed.emp_id = pr.empId "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join Department dept on dept.dept_id = ed.dept_id "
			+ "left join Schools sch on sch.school_id = ed.school_id "
			+ "Where CONCAT(IfNull(pr.profileResearchId,''),'',IfNull(pr.universityName,''),'',IfNull(pr.createdDate,''),'',IfNull(pr.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query(value ="Select new map(pr.profileResearchId as id,pr.tenureStatus as tenureStatus,pr.universityName as universityName,"
			+ "pr.titleOfThesis as titleOfThesis,pr.peerViewed as peerViewed,pr.noOfConferences as noOfConferences,"
			+ "pr.professionalOrganisation as professionalOrganisation,pr.partOfResearchProject as partOfResearchProject,pr.yesNumberOfProjects as yesNumberOfProjects,pr.keywordsResearch as keywordsResearch,"
			+ "pr.techniquesExpert as techniquesExpert,pr.currentProfessional as currentProfessional,pr.areasOfExpertise as areasOfExpertise,"
			+ "pr.researchForCollaboration as researchForCollaboration,pr.researchAttachment as researchAttachment,pr.phdRegisterDate as phdRegisterDate,"
			+ "pr.phdCompletedDate as phdCompletedDate,pr.googleScholar as googleScholar,pr.otherCitationDatabase as otherCitationDatabase,"
			+ "pr.phdHolderPursuing as phdHolderPursuing,pr.phdCount as phdCount,pr.linkedInLink as linkedInLink,"
			+ "pr.createdBy as createdBy,pr.modifiedBy as modifiedBy,pr.createdDate as createdDate,"
			+ "pr.modifiedDate as modifiedDate,pr.createdUsername as createdUsername, pr.modifiedUsername as modifiedUsername ,pr.active as active, "
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "dept.dept_name_short as dept_name_short,dept.dept_name as dept_name,sch.school_name as school_name, sch.school_name_short as school_name_short) From ProfileResearch pr "
			+ "left join EmployeeDetails ed on ed.emp_id = pr.empId "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join Department dept on dept.dept_id = ed.dept_id "
			+ "left join Schools sch on sch.school_id = ed.school_id ")
	Page<Object> getAllSortedData(Pageable pageable);

	@Query(value ="Select new map(pr.profileResearchId as id,pr.tenureStatus as tenureStatus,pr.universityName as universityName,"
			+ "pr.titleOfThesis as titleOfThesis,pr.peerViewed as peerViewed,pr.noOfConferences as noOfConferences,"
			+ "pr.professionalOrganisation as professionalOrganisation,pr.partOfResearchProject as partOfResearchProject,pr.yesNumberOfProjects as yesNumberOfProjects,pr.keywordsResearch as keywordsResearch,"
			+ "pr.techniquesExpert as techniquesExpert,pr.currentProfessional as currentProfessional,pr.areasOfExpertise as areasOfExpertise,"
			+ "pr.researchForCollaboration as researchForCollaboration,pr.researchAttachment as researchAttachment,pr.phdRegisterDate as phdRegisterDate,"
			+ "pr.phdCompletedDate as phdCompletedDate,pr.googleScholar as googleScholar,pr.otherCitationDatabase as otherCitationDatabase,"
			+ "pr.phdHolderPursuing as phdHolderPursuing,pr.phdCount as phdCount,pr.linkedInLink as linkedInLink,"
			+ "pr.createdBy as createdBy,pr.modifiedBy as modifiedBy,pr.createdDate as createdDate,"
			+ "pr.modifiedDate as modifiedDate,pr.createdUsername as createdUsername, pr.modifiedUsername as modifiedUsername ,pr.active as active, "
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "dept.dept_name_short as dept_name_short,dept.dept_name as dept_name,sch.school_name as school_name, sch.school_name_short as school_name_short) From ProfileResearch pr "
			+ "left join EmployeeDetails ed on ed.emp_id = pr.empId "
			+ "left join Designation des on des.designation_id = ed.designation_id "
			+ "left join Department dept on dept.dept_id = ed.dept_id "
			+ "left join Schools sch on sch.school_id = ed.school_id where pr.empId=?1 and pr.active=true")
	List<HashMap<String, Object>> fetchAllProfileResearchForEmployee(Integer empId);
	
	
	@Modifying
	@Query(value = "update ProfileResearch h set h.active=false where h.profileResearchId=?1")
	void updateProfileResearch(Integer profileResearchId);

	@Modifying
	@Query(value = "update ProfileResearch h set h.active=true where h.profileResearchId=?1")
	void updateProfileResearch1(Integer profileResearchId);

	
	@Modifying
	@Query(value = "update ProfileResearch pr set pr.researchAttachment=?2 where pr.profileResearchId IN ?1")
	public void updatePath(List<Integer> profileResearchId, String t2);

	@Query(value = "select count(*) from ProfileResearch pr where pr.titleOfThesis=?1 and pr.empId=?2 and pr.active=true")
	public Integer getCountOfTitleOfThesis(String titleOfThesis, Integer empId);
	
	

}
