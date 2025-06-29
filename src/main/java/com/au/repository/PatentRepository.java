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

import com.au.model.Patent;

@Transactional
@Repository
public interface PatentRepository extends JpaRepository<Patent, Integer>{

	@Query(value = "SELECT pt from Patent pt where pt.active=true")
	public List<Patent> getAllActivePatent();

	
	@Modifying
	@Query(value = "update Patent pt set pt.active=false where pt.patent_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update Patent pt set pt.active=true where pt.patent_id=?1")
	public void activate(Integer id);
	
//	@Query(value = "select pt.patent_id as id,pt.emp_id as emp_id,pt.patent_name as patent_name,pt.reference_number as reference_number,ia.status As status,"
//			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,ia.remark As remark,"
//			+ "ia.ipr_status As ipr_status,ia.ipr_date As ipr_date,ia.ipr_remark As ipr_remark,ia.ipr_name As ipr_name, " 
//			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
//	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
//			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status As approved_status,"
//			+ "pt.attachment_path as attachment_path,pt.attachment_name as attachment_name,pt.publication_status as publication_status,pt.patent_title as patent_title,"
//			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
//			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
//		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
//		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
//		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
//		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
//		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
//			+ "pt.created_username as created_username,pt.modified_username as modified_username,pt.active as active,"
//			+ "pt.created_date as created_date,pt.modified_date as modified_date,pt.created_by as created_by,"
//			+ "pt.amount as amount, pt.credited_year as credited_year, pt.credited_month as credited_month ,ia.type as type, "
//			+ "pt.modified_by as modified_by from patent pt "
//			+ "left join employee_details ed on ed.emp_id=pt.emp_id "
//			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
//			+ "LEFT JOIN incentive_approver ia ON pt.patent_id = ia.patent_id "
//			+ "where CONCAT(IfNull(pt.patent_name,''),'',IfNull(pt.emp_id,''),"
//			+ "'',IfNull(pt.created_by,''),'',IfNull(pt.created_date,'',IfNull(pt.reference_number,''),'')) LIKE %?1% "
//			+"And ( "
//					+ "		CASE "
//					+ "			WHEN ?2 = 10 THEN true Or ia.status=true "
//					+ "			WHEN ?2 = 20 THEN ia.status=true And ia.hod_status=true "
//					+ "			WHEN ?2 = 30 THEN ia.status=true And ia.hod_status=true And ia.hoi_status=true "
//					+ "			WHEN ?2 = 40 THEN ia.status=true And ia.hod_status=true And ia.hoi_status=true And ia.ipr_status=true "
//					+ "			WHEN ?2 = 60 THEN ia.status=true And ia.hod_status=true And ia.hoi_status=true And ia.ipr_status=true And ia.qa_status=true "
//					+ "			WHEN ?2 = 80 THEN ia.status=true And ia.hod_status=true And ia.hoi_status=true And ia.ipr_status=true And ia.qa_status=true And ia.hr_status=true "
//					+ " 		WHEN ?2 = 100 THEN ia.status=true And ia.hod_status=true And ia.hoi_status=true And ia.ipr_status=true And ia.qa_status=true And ia.hr_status=true "
//					+ "     ELSE true"
//					+ "     End) ", nativeQuery = true)
//	public Page<Map<String, Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer percentageFilter);
	
	
	@Query(value = "select pt.patent_id as id,pt.emp_id as emp_id,pt.patent_name as patent_name,pt.reference_number as reference_number,ia.status As status,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,ia.remark As remark,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status As approved_status,"
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
			+ "ia.ipr_status As ipr_status,ia.ipr_date As ipr_date,ia.ipr_remark As ipr_remark,ia.ipr_name As ipr_name, " 
			+ "pt.attachment_path as attachment_path,pt.attachment_name as attachment_name,pt.publication_status as publication_status,pt.patent_title as patent_title,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "pt.created_username as created_username,pt.modified_username as modified_username,pt.active as active,"
			+ "pt.created_date as created_date,pt.modified_date as modified_date,pt.created_by as created_by,"
			+ "pt.amount as amount, pt.credited_year as credited_year, pt.credited_month as credited_month ,ia.type as type, "
			+ "pt.modified_by as modified_by from patent pt "
			+ "left join employee_details ed on ed.emp_id=pt.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN incentive_approver ia ON pt.patent_id = ia.patent_id "
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

	@Query(value = "select pt.patent_id as id,pt.emp_id as emp_id,pt.patent_name as patent_name,pt.reference_number as reference_number,ia.status As status,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
			+ "ia.ipr_status As ipr_status,ia.ipr_date As ipr_date,ia.ipr_remark As ipr_remark,ia.ipr_name As ipr_name, " 
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status AS approved_status,"
			+ "pt.attachment_path as attachment_path,pt.attachment_name as attachment_name,pt.publication_status as publication_status,pt.patent_title as patent_title,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "pt.created_username as created_username,pt.modified_username as modified_username,pt.active as active,"
			+ "pt.created_date as created_date,pt.modified_date as modified_date,pt.created_by as created_by,"
			+ "pt.amount as amount, pt.credited_year as credited_year, pt.credited_month as credited_month ,ia.type as type, "
			+ "pt.modified_by as modified_by from patent pt "
			+ "left join employee_details ed on ed.emp_id=pt.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN incentive_approver ia ON pt.patent_id = ia.patent_id "
			+ "where pt.emp_id in (?1) and pt.active=true And "
			  + "     ((?2 = 10 AND (ia.status = true AND ia.hod_status is null And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 20 AND (ia.status = true AND ia.hod_status = true And ia.hoi_status is null And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 30 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true And ia.asst_dir_status is null And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 40 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true And ia.qa_status is null And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 60 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true And ia.hr_status is null And ia.finance_status is null)) "
		        + "  OR (?2 = 80 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true And ia.finance_status is null)) "
		        + "  OR (?2 = 100 AND (ia.status = true AND ia.hod_status = true AND ia.hoi_status = true AND ia.asst_dir_status = true AND ia.qa_status = true AND ia.hr_status = true AND ia.finance_status = true)) "
		        + ")",
	        nativeQuery = true)
	public List<Map<String, Object>> patentDetailsBasedOnEmpId(List<Integer> emp_id, Integer percentageFilter);

	@Query(value="SELECT COUNT(*) FROM patent cae where cae.patent_title=?1 And cae.emp_id=?2 And cae.active=true",nativeQuery = true)
	public Integer getCountOfPatentTitle(String patent_title, Integer emp_id);

	@Query(value = "select pat.patent_id as typeId,ed.emp_id as emp_id,ed.email as email,ed.employee_name as employee_name," +
			"ed.empcode as empcode,d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short,ia.amount As amount," +
			"ia.credited_month as credited_month,ia.credited_year as credited_year,dg.designation_name as designation_name," +
			"dg.designation_short_name as designation_short_name,sch.school_name_short as schoolShortName,ia.date as date,ia.incentive_approver_id As incentive_approver_id," +
			"( CASE WHEN pat.patent_id is not null THEN 'Patent' End) as researchType FROM patent pat "
			+ "LEFT JOIN employee_details ed ON ed.emp_id = pat.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN designation dg ON ed.designation_id = dg.designation_id "
			+ "LEFT JOIN schools sch ON sch.school_id = ed.school_id "
			+ "Inner JOIN incentive_approver ia ON pat.patent_id = ia.patent_id "
			+ "where ia.status=true And ia.hod_status=true And ia.hoi_status=true And ia.ipr_status=true " +
			"And ia.qa_status=true And ia.hr_status=true And ia.finance_status=true And ia.credited_month is not null " +
			"And ia.credited_year is not null And pat.active=true " +
			"And (:creditedMonth is null Or ia.credited_month=:creditedMonth) " +
			"And (:creditedYear is null Or ia.credited_year=:creditedYear)  ",nativeQuery = true)
	List<Map<String, Object>> patentsApprovedByAll(Integer creditedMonth, Integer creditedYear);

	@Query(value = "select pt.patent_id as id,pt.emp_id as emp_id,pt.patent_name as patent_name,pt.reference_number as reference_number,ia.status As status,"
			+ "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status,"
			+ "ia.ipr_status As ipr_status,ia.ipr_date As ipr_date,ia.ipr_remark As ipr_remark,ia.ipr_name As ipr_name, " 
			+ "ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.asst_dir_id as asst_dir_id,ia.qa_id as qa_id,"
	        + "ia.hr_id as hr_id,ia.finance_id as finance_id,ia.ipr_id as ipr_id,ia.date as date,"
			+ "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.approved_status AS approved_status,"
			+ "pt.attachment_path as attachment_path,pt.attachment_name as attachment_name,pt.publication_status as publication_status,pt.patent_title as patent_title,"
			+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode,ia.incentive_approver_id As incentive_approver_id,"
			 + "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
		        + "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
		        + "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
		        + "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
		        + "DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
		        + "d.dept_id AS dept_id, d.dept_name AS dept_name, d.dept_name_short AS dept_name_short, "
			+ "pt.created_username as created_username,pt.modified_username as modified_username,pt.active as active,"
			+ "pt.created_date as created_date,pt.modified_date as modified_date,pt.created_by as created_by,"
			+ "pt.amount as amount, pt.credited_year as credited_year, pt.credited_month as credited_month ,ia.type as type, "
			+ "pt.modified_by as modified_by from patent pt "
			+ "left join employee_details ed on ed.emp_id=pt.emp_id "
			+ "LEFT JOIN department d ON ed.dept_id = d.dept_id "
			+ "LEFT JOIN incentive_approver ia ON pt.patent_id = ia.patent_id "
			+ "where (:emp_id IS NULL OR pt.emp_id = :emp_id) and pt.active=true ", nativeQuery = true)
	public List<Map<String, Object>> patentBasedOnEmpId(Integer emp_id);
}
