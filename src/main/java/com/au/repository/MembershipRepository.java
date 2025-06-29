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

import com.au.model.Membership;


@Transactional
@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer>{

	
	@Query(value = "SELECT mem from Membership mem where mem.active=true")
	public List<Membership> getAllActiveMembership();
	
	
	@Modifying
	@Query(value = "update Membership mem set mem.active=false where mem.membership_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update Membership mem set mem.active=true where mem.membership_id=?1")
	public void activate(Integer id);
	
	
//	@Query(value = "select mem.membership_id as id,mem.emp_id as emp_id,mem.membership_type as membership_type,mem.professional_body as professional_body,mem.year as year,"
//			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,ia.remark As remark,"
//			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status As approved_status,"
//			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
//	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
//			+ "mem.attachment_path as attachment_path,mem.attachment_name as attachment_name,mem.citation as citation,ia.status As status,"
//			+ "mem.nature_of_membership as nature_of_membership,mem.priority as priority,mem.member_id as member_id,"
//			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
//			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
//		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
//		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
//		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
//		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
//		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
//			+ "mem.created_username as created_username,mem.modified_username as modified_username,mem.active as active,"
//			+ "mem.created_date as created_date,mem.modified_date as modified_date,mem.created_by as created_by,"
//			+ "mem.amount as amount, mem.credited_year as credited_year, mem.credited_month as credited_month ,ia.type as type, "
//			+ "mem.modified_by as modified_by from membership mem "
//			+ "left join employee_details ed on ed.emp_id=mem.emp_id "
//			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
//			+ "LEFT JOIN incentive_approver ia ON mem.membership_id = ia.membership_id "
//			+ "where CONCAT(IfNull(mem.membership_type,''),'',IfNull(mem.emp_id,''),"
//			+ "'',IfNull(mem.created_by,''),'',IfNull(mem.created_date,'',IfNull(mem.professional_body,''),'')) LIKE %?1% "
//			+ "And ( "
//			+ "		CASE "
//			+ "			WHEN ?2 = 10 THEN true Or ia.status=true "
//			+ "			WHEN ?2 = 20 THEN ia.status=true And ia.hod_status=true "
//			+ "			WHEN ?2 = 30 THEN ia.status=true And ia.hod_status=true And ia.hoi_status=true "
//			+ "			WHEN ?2 = 40 THEN ia.status=true And ia.hod_status=true And ia.hoi_status=true And ia.asst_dir_status=true "
//			+ "			WHEN ?2 = 60 THEN ia.status=true And ia.hod_status=true And ia.hoi_status=true And ia.asst_dir_status=true And ia.qa_status=true "
//			+ "			WHEN ?2 = 80 THEN ia.status=true And ia.hod_status=true And ia.hoi_status=true And ia.asst_dir_status=true And ia.qa_status=true And ia.hr_status=true "
//			+ " 		WHEN ?2 = 100 THEN ia.status=true And ia.hod_status=true And ia.hoi_status=true And ia.asst_dir_status=true And ia.qa_status=true And ia.hr_status=true "
//			+ "     ELSE true"
//			+ "     End) ", nativeQuery = true)
//	public Page<Map<String, Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer percentageFilter);
	
	
	@Query(value = "select mem.membership_id as id,mem.emp_id as emp_id,mem.membership_type as membership_type,mem.professional_body as professional_body,mem.year as year,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,ia.remark As remark,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status As approved_status,"
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
			+ "mem.attachment_path as attachment_path,mem.attachment_name as attachment_name,mem.citation as citation,ia.status As status,"
			+ "mem.nature_of_membership as nature_of_membership,mem.priority as priority,mem.member_id as member_id,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "mem.created_username as created_username,mem.modified_username as modified_username,mem.active as active,"
			+ "mem.created_date as created_date,mem.modified_date as modified_date,mem.created_by as created_by,"
			+ "mem.amount as amount, mem.credited_year as credited_year, mem.credited_month as credited_month ,ia.type as type, "
			+ "mem.modified_by as modified_by from membership mem "
			+ "left join employee_details ed on ed.emp_id=mem.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN incentive_approver ia ON mem.membership_id = ia.membership_id "
			  + "WHERE ( "
		        + "     (?1 = 10 AND (ia.status = true AND ia.hod_status is null And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?1 = 20 AND (ia.status = true AND ia.hod_status = true And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?1 = 30 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?1 = 40 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?1 = 60 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?1 = 80 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true And ia.finance_status is null)) "
		        + "  OR (?1 = 100 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true AND ia.finance_status = true)) "
		        + ")",
		        nativeQuery = true)
	public List<Map<String, Object>> getAllSortedData(Integer percentageFilter);

	@Query(value = "select mem.membership_id as id,mem.emp_id as emp_id,mem.membership_type as membership_type,mem.professional_body as professional_body,mem.year as year,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status AS approved_status,"
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
			+ "mem.attachment_path as attachment_path,mem.attachment_name as attachment_name,mem.citation as citation,ia.status As status,"
			+ "mem.nature_of_membership as nature_of_membership,mem.priority as priority,mem.member_id as member_id,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "mem.created_username as created_username,mem.modified_username as modified_username,mem.active as active,"
			+ "mem.created_date as created_date,mem.modified_date as modified_date,mem.created_by as created_by,"
			+ "mem.amount as amount, mem.credited_year as credited_year, mem.credited_month as credited_month ,ia.type as type, "
			+ "mem.modified_by as modified_by from membership mem "
			+ "left join employee_details ed on ed.emp_id=mem.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN incentive_approver ia ON mem.membership_id = ia.membership_id "
			+ "where mem.emp_id in (?1) and mem.active=true And "
			  + "     ((?2 = 10 AND (ia.status = true AND ia.hod_status is null And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 20 AND (ia.status = true AND ia.hod_status = true And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 30 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 40 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 60 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 80 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true And ia.finance_status is null)) "
		        + "  OR (?2 = 100 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true AND ia.finance_status = true)) "
		        + ")",
	        nativeQuery = true)
	public List<Map<String, Object>> membershipDetailsBasedOnEmpId(List<Integer> emp_id, Integer percentageFilter);

	@Query(value="SELECT COUNT(*) FROM membership cae where cae.member_id=?1 And cae.emp_id=?2 And cae.active=true",nativeQuery = true)
	public Integer getCountOfMemberId(String member_id, Integer emp_id);

	@Query(value = "select mem.membership_id as typeId,ed.emp_id as emp_id,ed.email as email,ed.employee_name as employee_name," +
			"ed.empcode as empcode,d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short,ia.amount As amount," +
			"ia.credited_month as credited_month,ia.credited_year as credited_year,dg.designation_name as designation_name," +
			"dg.designation_short_name as designation_short_name,sch.school_name_short as schoolShortName,ia.date as date,ia.incentive_approver_id As incentive_approver_id," +
			"( CASE WHEN mem.membership_id is not null THEN 'Membership' End) as researchType FROM membership mem "
			+ "Inner JOIN incentive_approver ia ON mem.membership_id = ia.membership_id "
			+ "LEFT JOIN employee_details ed ON ed.emp_id = mem.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN designation dg ON ed.designation_id = dg.designation_id "
			+ "LEFT JOIN schools sch ON sch.school_id = ed.school_id "
			+ "where ia.status=true And ia.hod_status=true And ia.hoi_status=true And ia.asst_dir_status=true " +
			"And ia.qa_status=true And ia.hr_status=true And ia.finance_status=true And ia.credited_month is not null " +
			"And ia.credited_year is not null And mem.active=true " +
			"And (:creditedMonth is null Or ia.credited_month=:creditedMonth) " +
			"And (:creditedYear is null Or ia.credited_year=:creditedYear)  ",nativeQuery = true)
    List<Map<String, Object>> membershipsApprovedByAll(Integer creditedMonth, Integer creditedYear);

	@Query(value = "select mem.membership_id as id,mem.emp_id as emp_id,mem.membership_type as membership_type,mem.professional_body as professional_body,mem.year as year,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status AS approved_status,"
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
			+ "mem.attachment_path as attachment_path,mem.attachment_name as attachment_name,mem.citation as citation,ia.status As status,"
			+ "mem.nature_of_membership as nature_of_membership,mem.priority as priority,mem.member_id as member_id,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "mem.created_username as created_username,mem.modified_username as modified_username,mem.active as active,"
			+ "mem.created_date as created_date,mem.modified_date as modified_date,mem.created_by as created_by,"
			+ "mem.amount as amount, mem.credited_year as credited_year, mem.credited_month as credited_month ,ia.type as type, "
			+ "mem.modified_by as modified_by from membership mem "
			+ "left join employee_details ed on ed.emp_id=mem.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN incentive_approver ia ON mem.membership_id = ia.membership_id "
			+ "where (:emp_id IS NULL OR mem.emp_id = :emp_id) and mem.active=true ", nativeQuery = true)
	public List<Map<String, Object>> membershipBasedOnEmpId(Integer emp_id);
}
