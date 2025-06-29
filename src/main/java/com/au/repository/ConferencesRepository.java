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

import com.au.model.Conferences;

@Transactional
@Repository
public interface ConferencesRepository extends JpaRepository<Conferences, Integer>{

	@Query(value = "SELECT con from Conferences con where con.active=true")
	public List<Conferences> getAllActiveConferences();
	
	
	@Modifying
	@Query(value = "update Conferences con set con.active=false where con.conferences_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update Conferences con set con.active=true where con.conferences_id=?1")
	public void activate(Integer id);
	
	
	
//	@Query(value = "select con.conferences_id as id,con.emp_id as emp_id,con.conference_type as conference_type,con.paper_type as paper_type,"
//			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,ia.remark As remark,"
//			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status As approved_status,"
//			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
//	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
//			+ "con.conference_name as conference_name,con.paper_title as paper_title,con.from_date as from_date,con.to_date as to_date,ia.status As status,"
//			+ "con.organiser as organiser,con.place as place,con.presentation_type as presentation_type,con.attachment_paper_path as attachment_paper_path,"
//			+ "con.attachment_paper_name as attachment_paper_name,con.attachment_cert_path as attachment_cert_path,con.attachment_cert_name as attachment_cert_name,"
//			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
//			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
//		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
//		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
//		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
//		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
//		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
//			+ "con.created_username as created_username,con.modified_username as modified_username,con.active as active,"
//			+ "con.created_date as created_date,con.modified_date as modified_date,con.created_by as created_by, "
//			+ "con.amount as amount, con.credited_year as credited_year, con.credited_month as credited_month ,ia.type as type, "
//			+ "con.modified_by as modified_by  from conferences con "
//			+ "left join employee_details ed on ed.emp_id=con.emp_id "
//			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
//			+ "LEFT JOIN incentive_approver ia ON con.conferences_id = ia.conferences_id "
//			+ "where CONCAT(IfNull(con.conference_type,''),'',IfNull(con.emp_id,''),"
//			+ "'',IfNull(con.created_by,''),'',IfNull(con.created_date,'',IfNull(con.conference_name,''),'')) LIKE %?1% "
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
//			+ "     End)", nativeQuery = true)
//	public Page<Map<String, Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer percentageFilter);
	
	
	@Query(value = "select con.conferences_id as id,con.emp_id as emp_id,con.conference_type as conference_type,con.paper_type as paper_type,ia.status As status,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,ia.remark As remark,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status As approved_status,"
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
			+ "con.conference_name as conference_name,con.paper_title as paper_title,con.from_date as from_date,con.to_date as to_date,"
			+ "con.organiser as organiser,con.place as place,con.presentation_type as presentation_type,con.attachment_paper_path as attachment_paper_path,"
			+ "con.attachment_paper_name as attachment_paper_name,con.attachment_cert_path as attachment_cert_path,con.attachment_cert_name as attachment_cert_name,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "con.created_username as created_username,con.modified_username as modified_username,con.active as active,"
			+ "con.created_date as created_date,con.modified_date as modified_date,con.created_by as created_by, "
			+ "con.amount as amount, con.credited_year as credited_year, con.credited_month as credited_month ,ia.type as type, "
			+ "con.modified_by as modified_by from conferences con "
			+ "left join employee_details ed on ed.emp_id=con.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN incentive_approver ia ON con.conferences_id = ia.conferences_id "
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


	
	@Query(value = "select con.conferences_id as id,con.emp_id as emp_id,con.conference_type as conference_type,con.paper_type as paper_type,ia.status As status,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status AS approved_status,"
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
			+ "con.conference_name as conference_name,con.paper_title as paper_title,con.from_date as from_date,con.to_date as to_date,"
			+ "con.organiser as organiser,con.place as place,con.presentation_type as presentation_type,con.attachment_paper_path as attachment_paper_path,"
			+ "con.attachment_paper_name as attachment_paper_name,con.attachment_cert_path as attachment_cert_path,con.attachment_cert_name as attachment_cert_name,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "con.created_username as created_username,con.modified_username as modified_username,con.active as active,"
			+ "con.created_date as created_date,con.modified_date as modified_date,con.created_by as created_by, "
			+ "con.amount as amount, con.credited_year as credited_year, con.credited_month as credited_month ,ia.type as type, "
			+ "con.modified_by as modified_by from conferences con "
			+ "left join employee_details ed on ed.emp_id=con.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN incentive_approver ia ON con.conferences_id = ia.conferences_id "
			+ "where con.emp_id in (?1) and con.active=true And "
			  + "     ((?2 = 10 AND (ia.status = true AND ia.hod_status is null And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 20 AND (ia.status = true AND ia.hod_status = true And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 30 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 40 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 60 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 80 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true And ia.finance_status is null)) "
		        + "  OR (?2 = 100 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true AND ia.finance_status = true)) "
		        + ")",
	        nativeQuery = true)
	public List<Map<String, Object>> conferenceDetailsBasedOnEmpId(List<Integer> emp_id, Integer percentageFilter);

	@Query(value="SELECT COUNT(*) FROM conferences cae where cae.conference_name=?1 And cae.emp_id=?2 And cae.active=true",nativeQuery = true)
	public Integer getCountOfConferenceName(String conference_name, Integer emp_id);

	@Query(value="SELECT COUNT(*) FROM conferences cae where cae.paper_title=?1 And cae.emp_id=?2 And cae.active=true",nativeQuery = true)
	public Integer getCountOfPaperTitle(String paper_title, Integer emp_id);

	@Query(value = "select con.conferences_id as typeId,ed.emp_id as emp_id,ed.email as email,ed.employee_name as employee_name," +
			"ed.empcode as empcode,d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short,ia.amount As amount," +
			"ia.credited_month as credited_month,ia.credited_year as credited_year,dg.designation_name as designation_name," +
			"dg.designation_short_name as designation_short_name,sch.school_name_short as schoolShortName,ia.date as date,ia.incentive_approver_id As incentive_approver_id," +
			"( CASE WHEN con.conferences_id is not null THEN 'Conference' End) as researchType FROM conferences con "
			+ "Inner JOIN incentive_approver ia ON con.conferences_id = ia.conferences_id "
			+ "LEFT JOIN employee_details ed ON ed.emp_id = con.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN schools sch ON sch.school_id = ed.school_id "
			+ "LEFT JOIN designation dg ON ed.designation_id = dg.designation_id "
			+ "where ia.status=true And ia.hod_status=true And ia.hoi_status=true And ia.asst_dir_status=true " +
			"And ia.qa_status=true And ia.hr_status=true And ia.finance_status=true And ia.credited_month is not null " +
			"And ia.credited_year is not null And con.active=true " +
			"And (:creditedMonth is null Or ia.credited_month=:creditedMonth) " +
			"And (:creditedYear is null Or ia.credited_year=:creditedYear)  ",nativeQuery = true)
	List<Map<String, Object>> conferencesApprovedByAll(Integer creditedMonth, Integer creditedYear);

	@Query(value = "select con.conferences_id as id,con.emp_id as emp_id,con.conference_type as conference_type,con.paper_type as paper_type,ia.status As status,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status AS approved_status,"
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
			+ "con.conference_name as conference_name,con.paper_title as paper_title,con.from_date as from_date,con.to_date as to_date,"
			+ "con.organiser as organiser,con.place as place,con.presentation_type as presentation_type,con.attachment_paper_path as attachment_paper_path,"
			+ "con.attachment_paper_name as attachment_paper_name,con.attachment_cert_path as attachment_cert_path,con.attachment_cert_name as attachment_cert_name,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "con.created_username as created_username,con.modified_username as modified_username,con.active as active,"
			+ "con.created_date as created_date,con.modified_date as modified_date,con.created_by as created_by, "
			+ "con.amount as amount, con.credited_year as credited_year, con.credited_month as credited_month ,ia.type as type, "
			+ "con.modified_by as modified_by from conferences con "
			+ "left join employee_details ed on ed.emp_id=con.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN incentive_approver ia ON con.conferences_id = ia.conferences_id "
			+ "where (:emp_id IS NULL OR con.emp_id = :emp_id) and con.active=true ", nativeQuery = true)
	public List<Map<String, Object>> conferenceBasedOnEmpId(Integer emp_id);
}
