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

import com.au.model.Publications;


@Transactional
@Repository
public interface PublicationsRepository  extends JpaRepository<Publications, Integer>{

	@Query(value = "SELECT pub from Publications pub where pub.active=true")
	public List<Publications> getAllActivePublication();

	@Modifying
	@Query(value = "update Publications pub set pub.active=false where pub.publications_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update Publications pub set pub.active=true where pub.publications_id=?1")
	public void activate(Integer id);

//	@Query(value = "SELECT pub.publications_id AS id, pub.emp_id AS emp_id, pub.Type AS Type, pub.issn AS issn, pub.doi AS doi, "
//	        + "pub.attachment_path AS attachment_path, pub.journal_name AS journal_name, pub.date AS date,ia.status As status, "
//	        + "pub.paper_title AS paper_title, pub.issn_type AS issn_type, pub.attachment_name AS attachment_name, "
//	        + "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
//	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as iaDate,"
//	        + "pub.volume AS volume, pub.issue_number AS issue_number, pub.page_number AS page_number,ia.remark As remark, "
//	        + "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
//			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status As approved_status,"
//	        + "ed.email AS email, ed.employee_name AS employee_name, ed.empcode AS empcode, ia.incentive_approver_id AS incentive_approver_id, "
//	        + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
//	        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
//	        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
//	        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
//	        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
//	        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
//	        + "pub.created_username AS created_username, pub.modified_username AS modified_username, pub.active AS active, "
//	        + "pub.created_date AS created_date, pub.modified_date AS modified_date, pub.created_by AS created_by, "
//			+ "pub.amount as amount, pub.credited_year as credited_year, pub.credited_month as credited_month ,ia.type as type, "
//	        + "pub.modified_by AS modified_by FROM publications pub "
//	        + "LEFT JOIN employee_details ed ON ed.emp_id = pub.emp_id "
//	        + "LEFT JOIN department d ON ed.dept_id = d.dept_id "
//	        + "LEFT JOIN incentive_approver ia ON pub.publications_id = ia.publications_id "
//			+ "where CONCAT(IfNull(pub.Type,''),'',IfNull(pub.emp_id,''),"
//			+ "'',IfNull(pub.created_by,''),'',IfNull(pub.created_date,'',IfNull(pub.journal_name,''),'')) LIKE %?1% "
//			+ "AND ( "
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


	@Query(value = "SELECT pub.publications_id AS id, pub.emp_id AS emp_id, pub.Type AS Type, pub.issn AS issn, pub.doi AS doi, "
	        + "pub.attachment_path AS attachment_path, pub.journal_name AS journal_name, pub.date AS date,ia.status As status, "
	        + "pub.paper_title AS paper_title, pub.issn_type AS issn_type, pub.attachment_name AS attachment_name, "
	        + "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as iaDate,"
	        + "pub.volume AS volume, pub.issue_number AS issue_number, pub.page_number AS page_number,ia.remark As remark, "
	        + "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status As approved_status,"
	        + "ed.email AS email, ed.employee_name AS employee_name, ed.empcode AS empcode, ia.incentive_approver_id AS incentive_approver_id, "
	        + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
	        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
	        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
	        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
	        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
	        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
	        + "pub.created_username AS created_username, pub.modified_username AS modified_username, pub.active AS active, "
	        + "pub.created_date AS created_date, pub.modified_date AS modified_date, pub.created_by AS created_by, "
			+ "pub.amount as amount, pub.credited_year as credited_year, pub.credited_month as credited_month ,ia.type as type, "
	        + "pub.modified_by AS modified_by FROM publications pub "
	        + "LEFT JOIN employee_details ed ON ed.emp_id = pub.emp_id "
	        + "LEFT JOIN department d ON ed.dept_id = d.dept_id "
	        + "LEFT JOIN incentive_approver ia ON pub.publications_id = ia.publications_id "
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


	@Query(value = "select pub.publications_id as id,pub.emp_id as emp_id,pub.Type as Type,pub.issn as issn,pub.doi as doi,pub.attachment_path as attachment_path,"
			+ "pub.journal_name as journal_name,pub.date as date,pub.paper_title as paper_title,pub.issn_type as issn_type,pub.attachment_name as attachment_name,"
			+ "pub.volume as volume,pub.issue_number as issue_number,pub.page_number as page_number,ia.status As status,"
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as iaDate,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status AS approved_status,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			+ "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
	        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
	        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
	        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
	        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
	        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "pub.created_username as created_username,pub.modified_username as modified_username,pub.active as active,"
			+ "pub.created_date as created_date,pub.modified_date as modified_date,pub.created_by as created_by,"
			+ "pub.amount as amount, pub.credited_year as credited_year, pub.credited_month as credited_month ,ia.type as type, "
			+ "pub.modified_by as modified_by "
			   + "FROM publications pub "
		        + "LEFT JOIN employee_details ed ON ed.emp_id = pub.emp_id "
		        + "LEFT JOIN department d ON ed.dept_id = d.dept_id "
		        + "LEFT JOIN incentive_approver ia ON pub.publications_id = ia.publications_id "
			+ "where pub.emp_id in (?1) and pub.active=true And "
			  + "     ((?2 = 10 AND (ia.status = true AND ia.hod_status is null And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 20 AND (ia.status = true AND ia.hod_status = true And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 30 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 40 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 60 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 80 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true And ia.finance_status is null)) "
		        + "  OR (?2 = 100 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true AND ia.finance_status = true)) "
		        + ")",
	        nativeQuery = true)
	public List<Map<String, Object>> publicationDetailsBasedOnEmpId(List<Integer> emp_id, Integer percentageFilter);


	@Query(value="SELECT COUNT(*) FROM publications cae where cae.issn=?1 And cae.emp_id=?2 And cae.active=true",nativeQuery = true)
	public Integer getCountOfIssn(String issn, Integer emp_id);


	@Query(value="SELECT COUNT(*) FROM publications cae where cae.issue_number=?1 And cae.emp_id=?2 And cae.active=true",nativeQuery = true)
	public Integer getCountOfIssue_number(String issue_number, Integer emp_id);

	@Query(value = "select pub.publications_id as typeId,pub.emp_id as emp_id,ed.email as email,ed.employee_name as employee_name," +
			"ed.empcode as empcode,d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short,ia.amount As amount," +
			"ia.credited_month as credited_month,ia.credited_year as credited_year,dg.designation_name as designation_name," +
			"dg.designation_short_name as designation_short_name,sch.school_name_short as schoolShortName,ia.date as date,ia.incentive_approver_id As incentive_approver_id," +
			"( CASE WHEN pub.publications_id is not null THEN 'Publication' ELSE 'Publication' End) as researchType FROM publications pub "
			+ "LEFT JOIN employee_details ed ON ed.emp_id = pub.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN schools sch ON sch.school_id = ed.school_id "
			+ "LEFT JOIN designation dg ON ed.designation_id = dg.designation_id "
			+ "Inner JOIN incentive_approver ia ON pub.publications_id = ia.publications_id "
			+ "where ia.status=true And ia.hod_status=true And ia.hoi_status=true And ia.asst_dir_status=true " +
			"And ia.qa_status=true And ia.hr_status=true And ia.finance_status=true And ia.credited_month is not null " +
			"And ia.credited_year is not null And pub.active=true " +
			"And (:creditedMonth is null Or ia.credited_month=:creditedMonth) " +
			"And (:creditedYear is null Or ia.credited_year=:creditedYear)  ",nativeQuery = true)
	List<Map<String, Object>> publicationsApprovedByAll(Integer creditedMonth, Integer creditedYear);

	
	@Query(value = "select pub.publications_id as id,pub.emp_id as emp_id,pub.Type as Type,pub.issn as issn,pub.doi as doi,pub.attachment_path as attachment_path,"
			+ "pub.journal_name as journal_name,pub.date as date,pub.paper_title as paper_title,pub.issn_type as issn_type,pub.attachment_name as attachment_name,"
			+ "pub.volume as volume,pub.issue_number as issue_number,pub.page_number as page_number,ia.status As status,"
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as iaDate,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status AS approved_status,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			+ "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
	        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
	        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
	        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
	        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
	        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "pub.created_username as created_username,pub.modified_username as modified_username,pub.active as active,"
			+ "pub.created_date as created_date,pub.modified_date as modified_date,pub.created_by as created_by,"
			+ "pub.amount as amount, pub.credited_year as credited_year, pub.credited_month as credited_month ,ia.type as type, "
			+ "pub.modified_by as modified_by "
			+ "FROM publications pub "
		    + "LEFT JOIN employee_details ed ON ed.emp_id = pub.emp_id "
		    + "LEFT JOIN department d ON ed.dept_id = d.dept_id "
		    + "LEFT JOIN incentive_approver ia ON pub.publications_id = ia.publications_id "
			+ "where (:emp_id IS NULL OR pub.emp_id = :emp_id) and pub.active=true ", nativeQuery = true)
	public List<Map<String, Object>> publicationBasedOnEmpId(Integer emp_id);
}

