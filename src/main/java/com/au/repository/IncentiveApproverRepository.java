package com.au.repository;

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

import com.au.model.IncentiveApprover;
import com.au.model.Publications;

@Transactional
@Repository
public interface IncentiveApproverRepository extends JpaRepository<IncentiveApprover, Integer>{

	@Query(value = "SELECT ia from IncentiveApprover ia where ia.active=true")
	public List<IncentiveApprover> getAllActiveIncentiveApprover();
	
	
	@Modifying
	@Query(value = "update IncentiveApprover ia set ia.active=false where ia.incentive_approver_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update IncentiveApprover ia set ia.active=true where ia.incentive_approver_id=?1")
	public void activate(Integer id);
	
	
	 @Query(value = "SELECT new map(" +
	            "ia.incentive_approver_id as id,ia.status As status,ia.ip_address As ip_address, " +
	            "ia.ipr_status As ipr_status,ia.ipr_date As ipr_date,ia.ipr_remark As ipr_remark,ia.ipr_id As ipr_id,ia.ipr_name As ipr_name, " +
	            "ia.hod_id as hod_id, ed1.email as hod_email, ed1.employee_name as hod_name, ed1.empcode as hod_empcode, " +
	            "ia.hoi_id as hoi_id, ed2.email as hoi_email, ed2.employee_name as hoi_name, ed2.empcode as hoi_empcode, " +
	            "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status," +
	             "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status," +
	            "ia.asst_dir_id as asst_dir_id, ed4.email as asst_dir_email, ed4.employee_name as asst_dir_name, ed4.empcode as asst_dir_empcode, " +
	            "ia.qa_id as qa_id, ed5.email as qa_email, ed5.employee_name as qa_name, ed5.empcode as qa_empcode, " +
	            "ia.hr_id as hr_id, ed6.email as hr_email, ed6.employee_name as hr_name, ed6.empcode as hr_empcode, " +
	            "ia.finance_id as finance_id, ed7.email as finance_email, ed7.employee_name as finance_name, ed7.empcode as finance_empcode, " +
	            "ia.publications_id as publications_id, pub.Type as Type, pub.issn as issn, pub.doi as doi, pub.attachment_path as attachment_path, " +
	            "pub.journal_name as journal_name, pub.date as date, pub.paper_title as paper_title, pub.issn_type as issn_type, pub.attachment_name as attachment_name, " +
	            "pub.volume as volume, pub.issue_number as issue_number, pub.page_number as page_number, " +
	            "ia.conferences_id as conferences_id, con.conference_type as conference_type, con.paper_type as paper_type, " +
	            "con.conference_name as conference_name, con.paper_title as paper_title, con.from_date as from_date, con.to_date as to_date, " +
	            "con.organiser as organiser, con.place as place, con.presentation_type as presentation_type, con.attachment_paper_path as attachment_paper_path, " +
	            "con.attachment_paper_name as attachment_paper_name, con.attachment_cert_path as attachment_cert_path, con.attachment_cert_name as attachment_cert_name, " +
	            "ia.book_chapter_id as book_chapter_id, bok.book_chapter as book_chapter, bok.book_title as book_title, bok.doi as doi, " +
	            "bok.attachment_path as attachment_path, bok.attachment_name as attachment_name, bok.unit as unit, " +
	            "bok.authore as authore, bok.publisher as publisher, bok.published_year as published_year, bok.isbn_number as isbn_number, " +
	            "ia.membership_id as membership_id, mem.membership_type as membership_type, mem.professional_body as professional_body, mem.year as year, " +
	            "mem.attachment_path as attachment_path, mem.attachment_name as attachment_name, mem.citation as citation, " +
	            "mem.nature_of_membership as nature_of_membership, mem.priority as priority, mem.member_id as member_id, " +
	            "ia.grant_id as grant_id, gt.title as title, gt.funding as funding, gt.funding_name as funding_name, " +
	            "gt.attachment_path as attachment_path, gt.attachment_name as attachment_name, gt.sanction_amount as sanction_amount, " +
	            "gt.tenure as tenure, gt.pi as pi, gt.co_pi as co_pi, " +
	            "ia.patent_id as patent_id, pt.patent_name as patent_name, pt.reference_number as reference_number, " +
	            "pt.attachment_path as attachment_path, pt.attachment_name as attachment_name, pt.publication_status as publication_status, pt.patent_title as patent_title, " +
	            "ia.remark as remark, ia.date as date, ia.amount as amount, ia.hod_remark as hod_remark, " +
	            "ia.hoi_remark as hoi_remark, ia.asst_dir_remark as asst_dir_remark, ia.qa_remark as qa_remark, ia.hr_remark as hr_remark, " +
	            "ia.finance_remark as finance_remark, ia.created_username as created_username, ia.modified_username as modified_username, ia.active as active, " +
	            "ia.emp_id as applicant_id, ed.email as applicant_email, ed.employee_name as applicant_name, ed.empcode as applicant_empcode, " +
	            "ia.created_date as created_date, ia.modified_date as modified_date, ia.created_by as created_by, ia.modified_by as modified_by, ia.type as type) " +
	            "FROM IncentiveApprover ia " +
	            "LEFT JOIN EmployeeDetails ed ON ed.emp_id = ia.emp_id " +
	            "LEFT JOIN Publications pub ON pub.publications_id = ia.publications_id " +
	            "LEFT JOIN Conferences con ON con.conferences_id = ia.conferences_id " +
	            "LEFT JOIN BookChapter bok ON bok.book_chapter_id = ia.book_chapter_id " +
	            "LEFT JOIN Membership mem ON mem.membership_id = ia.membership_id " +
	            "LEFT JOIN Grants gt ON gt.grant_id = ia.grant_id " +
	            "LEFT JOIN Patent pt ON pt.patent_id = ia.patent_id " +
	            "LEFT JOIN EmployeeDetails ed1 ON ed1.emp_id = ia.hod_id " +
	            "LEFT JOIN EmployeeDetails ed2 ON ed2.emp_id = ia.hoi_id " +
	            "LEFT JOIN EmployeeDetails ed4 ON ed4.emp_id = ia.asst_dir_id " +
	            "LEFT JOIN EmployeeDetails ed5 ON ed5.emp_id = ia.qa_id " +
	            "LEFT JOIN EmployeeDetails ed6 ON ed6.emp_id = ia.hr_id " +
	            "LEFT JOIN EmployeeDetails ed7 ON ed7.emp_id = ia.finance_id " +
	            "WHERE CONCAT(IfNull(ia.incentive_approver_id,''), '', IfNull(ia.emp_id,''), '', IfNull(ia.created_by,''), '', IfNull(ia.created_date,''), '', IfNull(ia.hr_remark,''), '') " +
	            "LIKE %:keyword% AND (ed1.emp_id = :empId OR ed2.emp_id = :empId OR ed4.emp_id = :empId OR ed5.emp_id = :empId OR ed6.emp_id = :empId OR ed7.emp_id = :empId)")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable,Integer empId, Object keyword); 
	
	
	 @Query(value = "SELECT new map(" +
	            "ia.incentive_approver_id as id, ia.status As status,ia.ip_address As ip_address," +
	            "ia.ipr_status As ipr_status,ia.ipr_date As ipr_date,ia.ipr_remark As ipr_remark,ia.ipr_id As ipr_id,ia.ipr_name As ipr_name, " +
	            "ia.hod_id as hod_id, ed1.email as hod_email, ed1.employee_name as hod_name, ed1.empcode as hod_empcode, " +
	            "ia.hoi_id as hoi_id, ed2.email as hoi_email, ed2.employee_name as hoi_name, ed2.empcode as hoi_empcode, " +
	            "ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status," +
	             "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status," +
	            "ia.asst_dir_id as asst_dir_id, ed4.email as asst_dir_email, ed4.employee_name as asst_dir_name, ed4.empcode as asst_dir_empcode, " +
	            "ia.qa_id as qa_id, ed5.email as qa_email, ed5.employee_name as qa_name, ed5.empcode as qa_empcode, " +
	            "ia.hr_id as hr_id, ed6.email as hr_email, ed6.employee_name as hr_name, ed6.empcode as hr_empcode, " +
	            "ia.finance_id as finance_id, ed7.email as finance_email, ed7.employee_name as finance_name, ed7.empcode as finance_empcode, " +
	            "ia.publications_id as publications_id, pub.Type as Type, pub.issn as issn, pub.doi as doi, pub.attachment_path as attachment_path, " +
	            "pub.journal_name as journal_name, pub.date as date, pub.paper_title as paper_title, pub.issn_type as issn_type, pub.attachment_name as attachment_name, " +
	            "pub.volume as volume, pub.issue_number as issue_number, pub.page_number as page_number, " +
	            "ia.conferences_id as conferences_id, con.conference_type as conference_type, con.paper_type as paper_type, " +
	            "con.conference_name as conference_name, con.paper_title as paper_title, con.from_date as from_date, con.to_date as to_date, " +
	            "con.organiser as organiser, con.place as place, con.presentation_type as presentation_type, con.attachment_paper_path as attachment_paper_path, " +
	            "con.attachment_paper_name as attachment_paper_name, con.attachment_cert_path as attachment_cert_path, con.attachment_cert_name as attachment_cert_name, " +
	            "ia.book_chapter_id as book_chapter_id, bok.book_chapter as book_chapter, bok.book_title as book_title, bok.doi as doi, " +
	            "bok.attachment_path as attachment_path, bok.attachment_name as attachment_name, bok.unit as unit, " +
	            "bok.authore as authore, bok.publisher as publisher, bok.published_year as published_year, bok.isbn_number as isbn_number, " +
	            "ia.membership_id as membership_id, mem.membership_type as membership_type, mem.professional_body as professional_body, mem.year as year, " +
	            "mem.attachment_path as attachment_path, mem.attachment_name as attachment_name, mem.citation as citation, " +
	            "mem.nature_of_membership as nature_of_membership, mem.priority as priority, mem.member_id as member_id, " +
	            "ia.grant_id as grant_id, gt.title as title, gt.funding as funding, gt.funding_name as funding_name, " +
	            "gt.attachment_path as attachment_path, gt.attachment_name as attachment_name, gt.sanction_amount as sanction_amount, " +
	            "gt.tenure as tenure, gt.pi as pi, gt.co_pi as co_pi, " +
	            "ia.patent_id as patent_id, pt.patent_name as patent_name, pt.reference_number as reference_number, " +
	            "pt.attachment_path as attachment_path, pt.attachment_name as attachment_name, pt.publication_status as publication_status, pt.patent_title as patent_title, " +
	            "ia.remark as remark, ia.date as date, ia.amount as amount, ia.hod_remark as hod_remark, " +
	            "ia.hoi_remark as hoi_remark, ia.asst_dir_remark as asst_dir_remark, ia.qa_remark as qa_remark, ia.hr_remark as hr_remark, " +
	            "ia.finance_remark as finance_remark, ia.created_username as created_username, ia.modified_username as modified_username, ia.active as active, " +
	            "ia.emp_id as applicant_id, ed.email as applicant_email, ed.employee_name as applicant_name, ed.empcode as applicant_empcode, " +
	            "ia.created_date as created_date, ia.modified_date as modified_date, ia.created_by as created_by, ia.modified_by as modified_by, ia.type as type) " +
	            "FROM IncentiveApprover ia " +
	            "LEFT JOIN EmployeeDetails ed ON ed.emp_id = ia.emp_id " +
	            "LEFT JOIN Publications pub ON pub.publications_id = ia.publications_id " +
	            "LEFT JOIN Conferences con ON con.conferences_id = ia.conferences_id " +
	            "LEFT JOIN BookChapter bok ON bok.book_chapter_id = ia.book_chapter_id " +
	            "LEFT JOIN Membership mem ON mem.membership_id = ia.membership_id " +
	            "LEFT JOIN Grants gt ON gt.grant_id = ia.grant_id " +
	            "LEFT JOIN Patent pt ON pt.patent_id = ia.patent_id " +
	            "LEFT JOIN EmployeeDetails ed1 ON ed1.emp_id = ia.hod_id " +
	            "LEFT JOIN EmployeeDetails ed2 ON ed2.emp_id = ia.hoi_id " +  
	            "LEFT JOIN EmployeeDetails ed4 ON ed4.emp_id = ia.asst_dir_id " +
	            "LEFT JOIN EmployeeDetails ed5 ON ed5.emp_id = ia.qa_id " +
	            "LEFT JOIN EmployeeDetails ed6 ON ed6.emp_id = ia.hr_id " +
	            "LEFT JOIN EmployeeDetails ed7 ON ed7.emp_id = ia.finance_id " + 
	            " WHERE (ed1.emp_id = :empId OR ed2.emp_id = :empId  OR ed4.emp_id = :empId OR ed5.emp_id = :empId OR ed6.emp_id = :empId OR ed7.emp_id = :empId) ")
	public Page<Object> getAllSortedData(Pageable pageable,Integer empId);

		@Query(value = "select new map(ia.incentive_approver_id as incentive_approver_id,ia.emp_id as emp_id, ia.hod_id as hod_id,ia.hoi_id as hoi_id,ia.status As status,"
				+ "ia.ipr_status As ipr_status,ia.ipr_date As ipr_date,ia.ipr_remark As ipr_remark,ia.ipr_id As ipr_id,ia.ipr_name As ipr_name_ia, " 
				+ "ia.hod_date As hod_date,ia.hoi_date As hoi_date,ia.asst_dir_date As asst_dir_date,ia.qa_date As qa_date,ia.hr_date As hr_date,ia.finance_date As finance_date,"
				+ " ia.asst_dir_id as asst_dir_id, ia.qa_id as qa_id,ia.hr_id as hr_id, ia.finance_id as finance_id, ia.publications_id as publications_id,"
				+ " ia.conferences_id as conferences_id, ia.book_chapter_id as book_chapter_id, ia.membership_id as membership_id,ia.grant_id as grant_id, ia.patent_id as patent_id,"
				+ " ia.remark as remark, ia.date as date, ia.amount as amount, ia.hod_remark as hod_remark, ia.hoi_remark as hoi_remark,"
				+ "ia.asst_dir_remark as asst_dir_remark, ia.qa_remark as qa_remark, ia.hr_remark as hr_remark, ia.finance_remark as finance_remark, ia.created_username as created_username,"
				+ "ia.modified_username as modified_username, ia.active as active, ia.created_date as created_date, ia.modified_date as modified_date,"
				+"ia.hod_status As hod_status,ia.hoi_status As hoi_status,ia.asst_dir_status As asst_dir_status,ia.qa_status As qa_status," 
	            + "ia.hr_status As hr_status,ia.finance_status As finance_status,ia.approver_status As approver_status,ia.ip_address As ip_address," 
				+ "ia.hod_id as hod_id, ed1.email as hod_email, ed1.employee_name as hod_name, ed1.empcode as hod_empcode, "
				+ "ed3.email as ipr_email, ed3.employee_name as ipr_name, ed3.empcode as ipr_empcode," 
	            + "ia.hoi_id as hoi_id, ed2.email as hoi_email, ed2.employee_name as hoi_name, ed2.empcode as hoi_empcode, "  
	            + "ia.asst_dir_id as asst_dir_id, ed4.email as asst_dir_email, ed4.employee_name as asst_dir_name, ed4.empcode as asst_dir_empcode, " 
	            + "ia.qa_id as qa_id, ed5.email as qa_email, ed5.employee_name as qa_name, ed5.empcode as qa_empcode, " 
	            + "ia.hr_id as hr_id, ed6.email as hr_email, ed6.employee_name as hr_name, ed6.empcode as hr_empcode, " 
	            + "ia.finance_id as finance_id, ed7.email as finance_email, ed7.employee_name as finance_name, ed7.empcode as finance_empcode, " 
				+ " ia.created_date as created_date, ia.modified_date as modified_date, ia.created_by as created_by, ia.modified_by as modified_by, ia.type as type ) "
				+ " FROM IncentiveApprover ia "
				+ "left join EmployeeDetails ed on ed.emp_id=ia.emp_id  "
				+ "LEFT JOIN EmployeeDetails ed1 ON ed1.emp_id = ia.hod_id " 
				+ "LEFT JOIN EmployeeDetails ed2 ON ed2.emp_id = ia.hoi_id " 
				+ "LEFT JOIN EmployeeDetails ed3 ON ed3.emp_id = ia.ipr_id " 
	            + "LEFT JOIN EmployeeDetails ed4 ON ed4.emp_id = ia.asst_dir_id " 
	            + "LEFT JOIN EmployeeDetails ed5 ON ed5.emp_id = ia.qa_id " 
	            + "LEFT JOIN EmployeeDetails ed6 ON ed6.emp_id = ia.hr_id " 
	            + "LEFT JOIN EmployeeDetails ed7 ON ed7.emp_id = ia.finance_id " 
				+ "where ia.emp_id=?1 And ia.incentive_approver_id=?2 And ia.active=true")
	public List<Map<String, Object>> incentiveApproverBasedOnEmpId(Integer emp_id, Integer incentive_approver_id);



		 @Query(value = "SELECT ia FROM IncentiveApprover ia WHERE ia.incentive_approver_id = ?1 AND ia.active = true")
		    public Optional<IncentiveApprover> findByIncentiveApproverId(Integer id);

		 @Query(value = "SELECT ia FROM IncentiveApprover ia WHERE ia.incentive_approver_id =?1 And ia.emp_id=?2 AND ia.active = true")
		public IncentiveApprover findByIncentiveApproverData(Integer id, Integer emp_id);

		 @Query(value = "SELECT ia FROM IncentiveApprover ia WHERE ia.incentive_approver_id = ?1 AND ia.active = true")
		public IncentiveApprover getAllActiveIncentiveApprover(Integer incentiveApproverId);
	
}
