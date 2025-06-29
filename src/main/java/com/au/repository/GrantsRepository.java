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

import com.au.model.Grants;


@Transactional
@Repository
public interface GrantsRepository extends JpaRepository<Grants, Integer>{

	@Query(value = "SELECT gt from Grants gt where gt.active=true")
	public List<Grants> getAllActiveGrant();
	
	@Modifying
	@Query(value = "update Grants gt set gt.active=false where gt.grant_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update Grants gt set gt.active=true where gt.grant_id=?1")
	public void activate(Integer id);
	
	
//	@Query(value = "select gt.grant_id as id,gt.emp_id as emp_id,gt.title as title,gt.funding as funding,gt.funding_name as funding_name,"
//			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
//			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.remark As remark,"
//			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
//	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
//			+ "gt.attachment_path as attachment_path,gt.attachment_name as attachment_name,gt.sanction_amount as sanction_amount,"
//			+ "gt.tenure as tenure,gt.pi as pi,gt.co_pi as co_pi,ia.status As status,ia.approved_status As approved_status,"
//			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
//			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
//		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
//		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
//		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
//		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
//		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
//			+ "gt.created_username as created_username,gt.modified_username as modified_username,gt.active as active,"
//			+ "gt.created_date as created_date,gt.modified_date as modified_date,gt.created_by as created_by,"
//			+ "gt.amount as amount, gt.credited_year as credited_year, gt.credited_month as credited_month ,ia.type as type, "
//			+ "gt.modified_by as modified_by from grants gt "
//			+ "left join employee_details ed on ed.emp_id=gt.emp_id "
//			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
//			+ "LEFT JOIN incentive_approver ia ON gt.grant_id = ia.grant_id "
//			+ "where CONCAT(IfNull(gt.title,''),'',IfNull(gt.emp_id,''),"
//			+ "'',IfNull(gt.created_by,''),'',IfNull(gt.created_date,'',IfNull(gt.funding,''),'')) LIKE %?1% "
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
//			+ "     End) Group By  gt.grant_id", nativeQuery = true)
//	public Page<Map<String, Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer percentageFilter);
	
	
	@Query(value = "select bt.grant_id as id,bt.emp_id as emp_id,bt.title as title,bt.funding as funding,bt.funding_name as funding_name,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.remark As remark,"
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
			+ "bt.attachment_path as attachment_path,bt.attachment_name as attachment_name,bt.sanction_amount as sanction_amount,"
			+ "bt.tenure as tenure,bt.pi as pi,bt.co_pi as co_pi,ia.status As status,ia.approved_status As approved_status,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "bt.created_username as created_username,bt.modified_username as modified_username,bt.active as active,"
			+ "bt.created_date as created_date,bt.modified_date as modified_date,bt.created_by as created_by,"
			+ "bt.amount as amount, bt.credited_year as credited_year, bt.credited_month as credited_month ,ia.type as type, "
			+ "bt.modified_by as modified_by from grants bt "
			+ "left join employee_details ed on ed.emp_id=bt.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN incentive_approver ia ON bt.grant_id = ia.grant_id "
			 + "WHERE ( "
		        + "     (?1 = 10 AND (ia.status = true AND ia.hod_status is null And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?1 = 20 AND (ia.status = true AND ia.hod_status = true And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?1 = 30 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?1 = 40 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?1 = 60 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?1 = 80 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true And ia.finance_status is null)) "
		        + "  OR (?1 = 100 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true AND ia.finance_status = true)) "
		        + ") Group By  bt.grant_id ",
		        nativeQuery = true)
	public List<Map<String, Object>> getAllSortedData(Integer percentageFilter);

	
	@Query(value = "select gt.grant_id as id,gt.emp_id as emp_id,gt.title as title,gt.funding as funding,gt.funding_name as funding_name,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status AS approved_status,"
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
			+ "gt.attachment_path as attachment_path,gt.attachment_name as attachment_name,gt.sanction_amount as sanction_amount,"
			+ "gt.tenure as tenure,gt.pi as pi,gt.co_pi as co_pi,ia.status As status,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "gt.created_username as created_username,gt.modified_username as modified_username,gt.active as active,"
			+ "gt.created_date as created_date,gt.modified_date as modified_date,gt.created_by as created_by,"
			+ "gt.amount as amount, gt.credited_year as credited_year, gt.credited_month as credited_month ,ia.type as type, "
			+ "gt.modified_by as modified_by from grants gt "
			+ "left join employee_details ed on ed.emp_id=gt.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN incentive_approver ia ON gt.grant_id = ia.grant_id "
			+ "where gt.emp_id in (?1) and gt.active=true And "
			  + "     ((?2 = 10 AND (ia.status = true AND ia.hod_status is null And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 20 AND (ia.status = true AND ia.hod_status = true And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 30 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 40 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 60 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 80 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true And ia.finance_status is null)) "
		        + "  OR (?2 = 100 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true AND ia.finance_status = true)) "
		        + ")",
	        nativeQuery = true)
	public List<Map<String, Object>> grantsDetailsBasedOnEmpId(List<Integer> emp_id, Integer percentageFilter);

	@Query(value="SELECT COUNT(*) FROM grants cae where cae.title=?1 And cae.emp_id=?2 And cae.active=true",nativeQuery = true)
	public Integer getCountOfTitle(String title, Integer emp_id);

	@Query(value = "select gra.grant_id as typeId,ed.emp_id as emp_id,ed.email as email,ed.employee_name as employee_name," +
			"ed.empcode as empcode,d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short,ia.amount As amount," +
			"ia.credited_month as credited_month,ia.credited_year as credited_year,dg.designation_name as designation_name," +
			"dg.designation_short_name as designation_short_name,sch.school_name_short as schoolShortName,ia.date as date,ia.incentive_approver_id As incentive_approver_id," +
			"( CASE WHEN gra.grant_id is not null THEN 'Grant' End) as researchType FROM grants gra "
			+ "Inner JOIN incentive_approver ia ON gra.grant_id = ia.grant_id "
			+ "LEFT JOIN employee_details ed ON ed.emp_id = gra.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN schools sch ON sch.school_id = ed.school_id "
			+ "LEFT JOIN designation dg ON ed.designation_id = dg.designation_id "
			+ "where ia.status=true And ia.hod_status=true And ia.hoi_status=true And ia.asst_dir_status=true " +
			"And ia.qa_status=true And ia.hr_status=true And ia.finance_status=true And ia.credited_month is not null " +
			"And ia.credited_year is not null And gra.active=true " +
			"And (:creditedMonth is null Or ia.credited_month=:creditedMonth) " +
			"And (:creditedYear is null Or ia.credited_year=:creditedYear)  ",nativeQuery = true)
    List<Map<String, Object>> grantsApprovedByAll(Integer creditedMonth, Integer creditedYear);

	@Query(value = "select gt.grant_id as id,gt.emp_id as emp_id,gt.title as title,gt.funding as funding,gt.funding_name as funding_name,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status AS approved_status,"
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
			+ "gt.attachment_path as attachment_path,gt.attachment_name as attachment_name,gt.sanction_amount as sanction_amount,"
			+ "gt.tenure as tenure,gt.pi as pi,gt.co_pi as co_pi,ia.status As status,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "gt.created_username as created_username,gt.modified_username as modified_username,gt.active as active,"
			+ "gt.created_date as created_date,gt.modified_date as modified_date,gt.created_by as created_by,"
			+ "gt.amount as amount, gt.credited_year as credited_year, gt.credited_month as credited_month ,ia.type as type, "
			+ "gt.modified_by as modified_by from grants gt "
			+ "left join employee_details ed on ed.emp_id=gt.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN incentive_approver ia ON gt.grant_id = ia.grant_id "
			+ "where (:emp_id IS NULL OR gt.emp_id = :emp_id) and gt.active=true ", nativeQuery = true)
	public List<Map<String, Object>> grantsBasedOnEmpId(Integer emp_id);
}
