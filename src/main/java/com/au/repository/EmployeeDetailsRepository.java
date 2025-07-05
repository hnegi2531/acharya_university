package com.au.repository;

import com.au.dto.EmployeeDetailsByEmail;
import com.au.dto.EmployeeDetailsForLeavePattern;
import com.au.model.EmployeeDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.EmployeeDetailsByEmail;
import com.au.dto.EmployeeDetailsForLeavePattern;
import com.au.dto.EmployeeFeedbackReport;
import com.au.model.EmployeeDetails;
import com.au.model.LeaveApply;

import javax.persistence.Tuple;
import java.util.*;

@Transactional
@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Integer> {

	@Query(value = "select ed.email from EmployeeDetails ed where ed.school_id=?1 and ed.dept_id=?2")
	public String[] getEmailList(Integer school_id, Integer dept_id);

	@Query(value = "select new map(ed.email as email,ua.id as user_id,ua.username as username) from EmployeeDetails ed "
			+ "join UserAuthentication ua on ed.email=ua.email where ed.school_id=?1 and ed.dept_id=?2")
	public List<HashMap<String, Object>> getEmailLists(Integer school_id, Integer dept_id);

	@Query(value = "select ed from EmployeeDetails ed where ed.active=true")
	public List<EmployeeDetails> findAll1();

	@Query(value = "select emp_id from EmployeeDetails ed where ed.email=?1 and ed.active=true")
	public Integer getEmpId(String email);

	@Query(value = "select employee_name from EmployeeDetails ed where ed.email=?1 and ed.active=true")
	public String getEmpName(String email);

	@Query(value = "select max(empcode) from employee_details ", nativeQuery = true)
	public String fetchEmployeeCodes();

	@Query(value = "select max(master_code) from EmployeeDetails ed")
	public String fetchgetMaxMasterCode();

	@Query(value = "SELECT contract_empcode FROM employee_details ORDER BY right(contract_empcode,3) "
			+ "DESC LIMIT 1", nativeQuery = true)
	public String fetchContractEmployeeCodes();

//	@Query(value = "select new map(ed.job_id as job_id,ed.empcode as empcode,ed.employee_name as employee_name,"
//			+ "ed.emp_type as emp_type,ed.designation_id as designation_id,ed.date_of_joining as date_of_joining,ed.email as email,"
//			+ "ed.grosspay_ctc as grosspay_ctc,ed.ctc as ctc,ed.gender as gender,ed.employee_status as employee_status,"
//			+ "sc.school_name_short as school_name_short,d.dept_name_short as dept_name_short,ed.net_pay as net_pay) "
//			+ "from EmployeeDetails ed left join Schools sc on ed.school_id=sc.school_id "
//			+ "left join Department d on ed.dept_id=d.dept_id")
//	public List<HashMap<String, Object>> findAlll();

	@Query(value = "select new map(edh.emp_id as id,edh.job_id as job_id,edh.empcode as empcode,edh.employee_name as employee_name,edh.phd_status as phd_status,"
			+ "edh.designation_id as designation_id,edh.date_of_joining as date_of_joining,edh.email as email,edh.employee_name as employee_name,"
			+ "edh.grosspay_ctc as grosspay_ctc,edh.ctc as ctc,edh.gender as gender,edh.employee_status as employee_status,edh.created_date as created_date,"
			+ "sc.school_name_short as school_name_short,d.dept_name_short as dept_name_short,edh.net_pay as net_pay,edh.bank_id as bank_name,"
			+ "edh.created_by as created_by,edh.created_username as created_username,edh.active as active,edh.master_code as master_code,"
			+ "edh.empcode as empcode,edh.firstname as firstname,edh.lastname as lastname,edh.report_id as report_id,"
			+ "sc.school_id as school_id,sc.school_name as school_name,d.dept_id as dept_id,d.dept_name as dept_name,"
			+ "edh.mobile as mobile,edh.alt_mobile_no as alt_mobile_no,edh.dateofbirth as dateofbirth,edh.martial_status as martial_status,edh.blood_group as blood_group,"
			+ "edh.father_name as father_name,edh.current_location as current_location,edh.salary_structure_id as salary_structure_id,"
			+ "edh.ctc as ctc,edh.pf_no as pf_no,edh.pan_no as pan_no,edh.emp_type_id as emp_type_id,edh.job_type_id as job_type_id,edh.exp_in_years as exp_in_years,"
			+ "edh.exp_in_months as exp_in_months,edh.hometown as hometown,edh.pincode as pincode,edh.annual_salary as annual_salary,edh.spl_pay as spl_pay,"
			+ "edh.key_skills as key_skills,edh.bank_account_no as bank_account_no,edh.bank_branch as bank_branch,edh.bank_ifsccode as bank_ifsccode,"
			+ "edh.leave_approver1_emp_id as leave_approver1_emp_id,edh.leave_approver2_emp_id as leave_approver2_emp_id,edh.attach as attach,edh.dlno as dlno,edh.dlexpno as dlexpno,"
			+ "edh.passportno as passportno,edh.passportexpno as passportexpnow,edh.bankacc2 as bankacc2,edh.to_date as to_date,edh.prob as prob,"
			+ "edh.nda as nda,edh.nca as nca,edh.uan_no as uan_no,edh.punched_card_status as punched_card_status,edh.photo as photo,edh.maternity_status as maternity_status,"
			+ "edh.marriage_status as marriage_status,edh.paternity_status as paternity_status,edh.transport_status as transport_status,edh.salary_approve_status as salary_approve_status,"
			+ "edh.vehicle_route_id as vehicle_route_id,"
			+ "edh.new_join_status as new_join_status,edh.pf_status as pf_status,"
			+ "edh.pt_status as pt_status,edh.permanent_file as permanent_file,edh.permanent_done_by as permanent_done_by,edh.permanent_status as permanent_status,edh.date_of_permanent as date_of_permanent,"
			+ "edh.aadhar as aadhar,edh.photo_upload_status as photo_upload_status,edh.proctor_assign_status as proctor_assign_status,"
			+ "edh.bank_account_holder_name as bank_account_holder_name,edh.grosspay_ctc as grosspay_ctc,edh.nda_nca_attach as nda_nca_attach,edh.store_indent_approver1 as store_indent_approver1,"
			+ "edh.esi_no as esi_no,edh.hra as hra,edh.da as da,edh.cca as cca,edh.ta as ta,edh.net_pay as net_pay,"
			+ "edh.other_allow as other_allow,edh.spl_1 as spl_1,edh.fte_status as fte_status,edh.title as title,edh.caste_category as caste_category,edh.contract_emp_type as contract_emp_type,"
			+ "edh.school as school,edh.contract_empcode as contract_empcode,edh.consolidated_amount as consolidated_amount,edh.from_date as from_date,"
			+ "edh.date_of_joining as date_of_joining,edh.remarks as remarks,ua.id As userId,"
			+ "edh.subject_skills as subject_skills,edh.employee_status as employee_status,edh.proctor_type as proctor_type,ss.salary_structure as salary_structure,"
			+ "dg.designation_short_name as designation_short_name,dg.designation_name As designation_name,"
			+ "emt.empTypeShortName as empTypeShortName,ua.username as username,jt.job_type as job_type,"
			+ "lan.employee_name as leaveApproverName1,lan2.employee_name as leaveApproverName2,sia.employee_name as storeIndentApproverName,"
			+ "edh.job_short_name as jobShortName,edh.dept_name_short as deptNameShort,edh.school_name_short as schoolNameShort,edh.shift_name as UpdateShiftName,"
			+ "edh.chief_proctor_id as chief_proctor_id,o.offer_id as offer_id,edh.religion as religion,edh.preferred_name_for_email as preferred_name_for_email,"
			+ "sh.shiftName as shiftName,jt.job_short_name as job_short_name, ca.consoliatedAmountId as consoliatedAmountId,"
			+ "ca.subject as subject) from EmployeeDetails edh "
			+ "LEFT JOIN ConsoliatedAmount ca ON ca.empId = edh.emp_id AND ca.created_Date = (SELECT MIN(ca2.created_Date) FROM ConsoliatedAmount ca2 WHERE ca2.empId = edh.emp_id) "
			+ "left join Schools sc on edh.school_id=sc.school_id "
			+ "left join Offer o on edh.job_id=o.job_id and o.offer_id = "
			+ "(SELECT MAX(offer.offer_id) FROM Offer offer WHERE offer.job_id = edh.job_id ) "
			+ "left join Department d on edh.dept_id=d.dept_id "
			+ "left join Shift sh on edh.shift_category_id=sh.shiftCategoryId "
			+ "left join JobType jt on edh.job_type_id=jt.job_type_id "
			+ "left join EmployeeType emt on edh.emp_type_id=emt.empTypeId "
			+ "left join SalaryStructure ss on edh.salary_structure_id=ss.salary_structure_id "
			+ "left join UserAuthentication ua on ua.email=edh.email "
			+ "left join Designation dg on edh.designation_id=dg.designation_id "
			+ "left join EmployeeDetails lan on lan.emp_id=edh.leave_approver1_emp_id "
			+ "left join EmployeeDetails lan2 on lan2.emp_id=edh.leave_approver2_emp_id "
			+ "left join EmployeeDetails sia on sia.emp_id=edh.store_indent_approver1 "
			+ "where (:school_id is null or edh.school_id = :school_id) "
		    + "And (:dept_id is null or edh.dept_id = :dept_id) "
		    + "And (:designation_id is null or edh.designation_id = :designation_id) "
		    + "And (:job_type_id is null or edh.job_type_id = :job_type_id) "
			+ "And CONCAT(IfNull(edh.empcode,''),'',IfNull(edh.employee_name,''),'',IfNull(edh.preferred_name_for_email,''),' ',IfNull(edh.email,''),'',IfNull(sc.school_name_short,''),'',"
			+ "IfNull(d.dept_name_short,'')) LIKE %:keyword% And edh.active=true group by edh.emp_id")
	public Page<Object> getAllDataFilteredByKeywordBySchool(Pageable pageable, Object keyword, Integer school_id, Integer dept_id, Integer designation_id, Integer job_type_id);

	@Query(value = "select new map(edh.emp_id as id,edh.job_id as job_id,edh.empcode as empcode,edh.employee_name as employee_name,edh.phd_status as phd_status,"
			+ "edh.designation_id as designation_id,edh.date_of_joining as date_of_joining,edh.email as email,edh.employee_name as employee_name,"
			+ "edh.grosspay_ctc as grosspay_ctc,edh.ctc as ctc,edh.gender as gender,edh.employee_status as employee_status,edh.created_date as created_date,"
			+ "sc.school_name_short as school_name_short,d.dept_name_short as dept_name_short,edh.net_pay as net_pay,edh.bank_id as bank_name,"
			+ "edh.created_by as created_by,edh.created_username as created_username,edh.active as active,edh.master_code as master_code,"
			+ "edh.empcode as empcode,edh.firstname as firstname,edh.lastname as lastname,edh.report_id as report_id,"
			+ "sc.school_id as school_id,sc.school_name as school_name,d.dept_id as dept_id,d.dept_name as dept_name,"
			+ "edh.mobile as mobile,edh.alt_mobile_no as alt_mobile_no,edh.dateofbirth as dateofbirth,edh.martial_status as martial_status,edh.blood_group as blood_group,"
			+ "edh.father_name as father_name,edh.current_location as current_location,edh.salary_structure_id as salary_structure_id,"
			+ "edh.ctc as ctc,edh.pf_no as pf_no,edh.pan_no as pan_no,edh.emp_type_id as emp_type_id,edh.job_type_id as job_type_id,edh.exp_in_years as exp_in_years,"
			+ "edh.exp_in_months as exp_in_months,edh.hometown as hometown,edh.pincode as pincode,edh.annual_salary as annual_salary,edh.spl_pay as spl_pay,"
			+ "edh.key_skills as key_skills,edh.bank_account_no as bank_account_no,edh.bank_branch as bank_branch,edh.bank_ifsccode as bank_ifsccode,"
			+ "edh.leave_approver1_emp_id as leave_approver1_emp_id,edh.leave_approver2_emp_id as leave_approver2_emp_id,edh.attach as attach,edh.dlno as dlno,edh.dlexpno as dlexpno,"
			+ "edh.passportno as passportno,edh.passportexpno as passportexpnow,edh.bankacc2 as bankacc2,edh.to_date as to_date,edh.prob as prob,"
			+ "edh.nda as nda,edh.nca as nca,edh.uan_no as uan_no,edh.punched_card_status as punched_card_status,edh.photo as photo,edh.maternity_status as maternity_status,"
			+ "edh.marriage_status as marriage_status,edh.paternity_status as paternity_status,edh.transport_status as transport_status,edh.salary_approve_status as salary_approve_status,"
			+ "edh.vehicle_route_id as vehicle_route_id,dg.designation_name As designation_name,"
			+ "edh.new_join_status as new_join_status,edh.pf_status as pf_status,ua.id As userId,"
			+ "edh.pt_status as pt_status,edh.permanent_file as permanent_file,edh.permanent_done_by as permanent_done_by,edh.permanent_status as permanent_status,edh.date_of_permanent as date_of_permanent,"
			+ "edh.aadhar as aadhar,edh.photo_upload_status as photo_upload_status,edh.proctor_assign_status as proctor_assign_status,"
			+ "edh.bank_account_holder_name as bank_account_holder_name,edh.grosspay_ctc as grosspay_ctc,edh.nda_nca_attach as nda_nca_attach,edh.store_indent_approver1 as store_indent_approver1,"
			+ "edh.esi_no as esi_no,edh.hra as hra,edh.da as da,edh.cca as cca,edh.ta as ta,edh.net_pay as net_pay,"
			+ "edh.other_allow as other_allow,edh.spl_1 as spl_1,edh.fte_status as fte_status,edh.title as title,edh.caste_category as caste_category,edh.contract_emp_type as contract_emp_type,"
			+ "edh.school as school,edh.contract_empcode as contract_empcode,edh.consolidated_amount as consolidated_amount,edh.from_date as from_date,edh.date_of_joining as date_of_joining,edh.remarks as remarks,"
			+ "edh.subject_skills as subject_skills,edh.employee_status as employee_status,edh.proctor_type as proctor_type,ss.salary_structure as salary_structure,dg.designation_short_name as designation_short_name,"
			+ "emt.empTypeShortName as empTypeShortName,ua.username as username,jt.job_type as job_type,"
			+ "lan.employee_name as leaveApproverName1,lan2.employee_name as leaveApproverName2,sia.employee_name as storeIndentApproverName,"
			+ "edh.job_short_name as jobShortName,edh.dept_name_short as deptNameShort,edh.school_name_short as schoolNameShort,edh.shift_name as UpdateShiftName,"
			+ "edh.chief_proctor_id as chief_proctor_id,o.offer_id as offer_id,edh.religion as religion,edh.preferred_name_for_email as preferred_name_for_email,sh.shiftName as shiftName,"
			+ "jt.job_short_name as job_short_name,ca.consoliatedAmountId as consoliatedAmountId,"
			+ "ca.subject as subject) from EmployeeDetails edh "
			+ "LEFT JOIN ConsoliatedAmount ca ON ca.empId = edh.emp_id AND ca.created_Date = (SELECT MIN(ca2.created_Date) FROM ConsoliatedAmount ca2 WHERE ca2.empId = edh.emp_id) "
			+ "left join Schools sc on edh.school_id=sc.school_id " 
			+ "left join Offer o on edh.job_id=o.job_id and o.offer_id = "
			+ "(SELECT MAX(offer.offer_id) FROM Offer offer WHERE offer.job_id = edh.job_id ) "
			+ "left join Department d on edh.dept_id=d.dept_id "
			+ "left join Shift sh on edh.shift_category_id=sh.shiftCategoryId "
			+ "left join JobType jt on edh.job_type_id=jt.job_type_id "
			+ "left join EmployeeType emt on edh.emp_type_id=emt.empTypeId "
			+ "left join SalaryStructure ss on edh.salary_structure_id=ss.salary_structure_id "
			+ "left join UserAuthentication ua on ua.email=edh.email "
			+ "left join Designation dg on edh.designation_id=dg.designation_id "
			+ "left join EmployeeDetails lan on lan.emp_id=edh.leave_approver1_emp_id "
			+ "left join EmployeeDetails lan2 on lan2.emp_id=edh.leave_approver2_emp_id "
			+ "left join EmployeeDetails sia on sia.emp_id=edh.store_indent_approver1 "
			+ "where (:school_id is null or edh.school_id = :school_id) "
		    + "And (:dept_id is null or edh.dept_id = :dept_id) "
		    + "And (:designation_id is null or edh.designation_id = :designation_id) "
		    + "And (:job_type_id is null or edh.job_type_id = :job_type_id) "
			+ "And CONCAT(IfNull(edh.empcode,''),'',IfNull(edh.employee_name,''),'',IfNull(edh.preferred_name_for_email,''),' ',IfNull(edh.email,''),'',"
			+ "IfNull(sc.school_name_short,''),'',IfNull(d.dept_name_short,'')) LIKE %:keyword%  And edh.active=true group by edh.emp_id")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer dept_id, Integer school_id, Integer designation_id, Integer job_type_id);

	@Query(value = "select new map(edh.emp_id as id,edh.job_id as job_id,edh.empcode as empcode,edh.employee_name as employee_name,edh.phd_status as phd_status,"
			+ "edh.designation_id as designation_id,edh.date_of_joining as date_of_joining,edh.email as email,edh.employee_name as employee_name,"
			+ "edh.grosspay_ctc as grosspay_ctc,edh.ctc as ctc,edh.gender as gender,edh.employee_status as employee_status,edh.created_date as created_date,"
			+ "sc.school_name_short as school_name_short,d.dept_name_short as dept_name_short,edh.net_pay as net_pay,edh.bank_id as bank_name,"
			+ "edh.created_by as created_by,edh.created_username as created_username,edh.active as active,edh.master_code as master_code,"
			+ "edh.empcode as empcode,edh.firstname as firstname,edh.lastname as lastname,edh.report_id as report_id,"
			+ "sc.school_id as school_id,sc.school_name as school_name,d.dept_id as dept_id,d.dept_name as dept_name,"
			+ "edh.mobile as mobile,edh.alt_mobile_no as alt_mobile_no,edh.dateofbirth as dateofbirth,edh.martial_status as martial_status,edh.blood_group as blood_group,"
			+ "edh.father_name as father_name,edh.current_location as current_location,edh.salary_structure_id as salary_structure_id,"
			+ "edh.ctc as ctc,edh.pf_no as pf_no,edh.pan_no as pan_no,edh.emp_type_id as emp_type_id,edh.job_type_id as job_type_id,edh.exp_in_years as exp_in_years,"
			+ "edh.exp_in_months as exp_in_months,edh.hometown as hometown,edh.pincode as pincode,edh.annual_salary as annual_salary,edh.spl_pay as spl_pay,"
			+ "edh.key_skills as key_skills,edh.bank_account_no as bank_account_no,edh.bank_branch as bank_branch,edh.bank_ifsccode as bank_ifsccode,"
			+ "edh.leave_approver1_emp_id as leave_approver1_emp_id,edh.leave_approver2_emp_id as leave_approver2_emp_id,edh.attach as attach,edh.dlno as dlno,edh.dlexpno as dlexpno,"
			+ "edh.passportno as passportno,edh.passportexpno as passportexpnow,edh.bankacc2 as bankacc2,edh.to_date as to_date,edh.prob as prob,"
			+ "edh.nda as nda,edh.nca as nca,edh.uan_no as uan_no,edh.punched_card_status as punched_card_status,edh.photo as photo,edh.maternity_status as maternity_status,"
			+ "edh.marriage_status as marriage_status,edh.paternity_status as paternity_status,edh.transport_status as transport_status,edh.salary_approve_status as salary_approve_status,"
			+ "edh.vehicle_route_id as vehicle_route_id,ua.id As userId,"
			+ "edh.new_join_status as new_join_status,edh.pf_status as pf_status,"
			+ "edh.pt_status as pt_status,edh.permanent_file as permanent_file,edh.permanent_done_by as permanent_done_by,edh.permanent_status as permanent_status,edh.date_of_permanent as date_of_permanent,"
			+ "edh.aadhar as aadhar,edh.photo_upload_status as photo_upload_status,edh.proctor_assign_status as proctor_assign_status,"
			+ "edh.bank_account_holder_name as bank_account_holder_name,edh.grosspay_ctc as grosspay_ctc,edh.nda_nca_attach as nda_nca_attach,edh.store_indent_approver1 as store_indent_approver1,"
			+ "edh.esi_no as esi_no,edh.hra as hra,edh.da as da,edh.cca as cca,edh.ta as ta,edh.net_pay as net_pay,"
			+ "edh.other_allow as other_allow,edh.spl_1 as spl_1,edh.fte_status as fte_status,edh.title as title,edh.caste_category as caste_category,edh.contract_emp_type as contract_emp_type,"
			+ "edh.school as school,edh.contract_empcode as contract_empcode,edh.consolidated_amount as consolidated_amount,edh.from_date as from_date,"
			+ "edh.date_of_joining as date_of_joining,edh.remarks as remarks,"
			+ "edh.subject_skills as subject_skills,edh.employee_status as employee_status,edh.proctor_type as proctor_type,ss.salary_structure as salary_structure,"
			+ "dg.designation_short_name as designation_short_name,dg.designation_name As designation_name,"
			+ "emt.empTypeShortName as empTypeShortName,ua.username as username,jt.job_type as job_type,"
			+ "lan.employee_name as leaveApproverName1,lan2.employee_name as leaveApproverName2,sia.employee_name as storeIndentApproverName,"
			+ "edh.job_short_name as jobShortName,edh.dept_name_short as deptNameShort,edh.school_name_short as schoolNameShort,edh.shift_name as UpdateShiftName,"
			+ "edh.chief_proctor_id as chief_proctor_id,o.offer_id as offer_id,edh.religion as religion,edh.preferred_name_for_email as preferred_name_for_email,"
			+ "sh.shiftName as shiftName,jt.job_short_name as job_short_name, ca.consoliatedAmountId as consoliatedAmountId,"
			+ "ca.subject as subject) from EmployeeDetails edh "
			+"LEFT JOIN ConsoliatedAmount ca ON ca.empId = edh.emp_id AND ca.created_Date = (SELECT MIN(ca2.created_Date) FROM ConsoliatedAmount ca2 WHERE ca2.empId = edh.emp_id) "
			+ "left join Schools sc on edh.school_id=sc.school_id " 
			+ "left join Offer o on edh.job_id=o.job_id and o.offer_id = "
			+ "(SELECT MAX(offer.offer_id) FROM Offer offer WHERE offer.job_id = edh.job_id ) "
			+ "left join Department d on edh.dept_id=d.dept_id "
			+ "left join Shift sh on edh.shift_category_id=sh.shiftCategoryId "
			+ "left join JobType jt on edh.job_type_id=jt.job_type_id "
			+ "left join EmployeeType emt on edh.emp_type_id=emt.empTypeId "
			+ "left join SalaryStructure ss on edh.salary_structure_id=ss.salary_structure_id "
			+ "left join UserAuthentication ua on ua.email=edh.email "
			+ "left join EmployeeDetails lan on lan.emp_id=edh.leave_approver1_emp_id "
			+ "left join EmployeeDetails lan2 on lan2.emp_id=edh.leave_approver2_emp_id "
			+ "left join EmployeeDetails sia on sia.emp_id=edh.store_indent_approver1 "
			+ "left join Designation dg on edh.designation_id=dg.designation_id "
			+ "where (:school_id is null or edh.school_id = :school_id) "
		    + "And (:dept_id is null or edh.dept_id = :dept_id) "
		    + "And (:designation_id is null or edh.designation_id = :designation_id) "
		    + "And (:job_type_id is null or edh.job_type_id = :job_type_id) And edh.active=true group by edh.emp_id")
	public Page<Object> getAllSortedDataBySchool(Pageable pageable, Integer school_id, Integer dept_id, Integer designation_id, Integer job_type_id);

	@Query(value = "select new map(edh.emp_id as id,edh.job_id as job_id,edh.empcode as empcode,edh.employee_name as employee_name,edh.phd_status as phd_status,"
			+ "edh.designation_id as designation_id,edh.date_of_joining as date_of_joining,edh.email as email,edh.employee_name as employee_name,"
			+ "edh.grosspay_ctc as grosspay_ctc,edh.ctc as ctc,edh.gender as gender,edh.employee_status as employee_status,edh.created_date as created_date,"
			+ "sc.school_name_short as school_name_short,d.dept_name_short as dept_name_short,edh.net_pay as net_pay,edh.bank_id as bank_name,"
			+ "edh.created_by as created_by,edh.created_username as created_username,edh.active as active,edh.master_code as master_code,"
			+ "edh.empcode as empcode,edh.firstname as firstname,edh.lastname as lastname,edh.report_id as report_id,"
			+ "sc.school_id as school_id,sc.school_name as school_name,d.dept_id as dept_id,d.dept_name as dept_name,"
			+ "edh.mobile as mobile,edh.alt_mobile_no as alt_mobile_no,edh.dateofbirth as dateofbirth,edh.martial_status as martial_status,edh.blood_group as blood_group,"
			+ "edh.father_name as father_name,edh.current_location as current_location,edh.salary_structure_id as salary_structure_id,"
			+ "edh.ctc as ctc,edh.pf_no as pf_no,edh.pan_no as pan_no,edh.emp_type_id as emp_type_id,edh.job_type_id as job_type_id,edh.exp_in_years as exp_in_years,"
			+ "edh.exp_in_months as exp_in_months,edh.hometown as hometown,edh.pincode as pincode,edh.annual_salary as annual_salary,edh.spl_pay as spl_pay,"
			+ "edh.key_skills as key_skills,edh.bank_account_no as bank_account_no,edh.bank_branch as bank_branch,edh.bank_ifsccode as bank_ifsccode,"
			+ "edh.leave_approver1_emp_id as leave_approver1_emp_id,edh.leave_approver2_emp_id as leave_approver2_emp_id,edh.attach as attach,edh.dlno as dlno,edh.dlexpno as dlexpno,"
			+ "edh.passportno as passportno,edh.passportexpno as passportexpnow,edh.bankacc2 as bankacc2,edh.to_date as to_date,edh.prob as prob,"
			+ "edh.nda as nda,edh.nca as nca,edh.uan_no as uan_no,edh.punched_card_status as punched_card_status,edh.photo as photo,edh.maternity_status as maternity_status,"
			+ "edh.marriage_status as marriage_status,edh.paternity_status as paternity_status,edh.transport_status as transport_status,edh.salary_approve_status as salary_approve_status,"
			+ "edh.vehicle_route_id as vehicle_route_id,ua.id As userId,"
			+ "edh.new_join_status as new_join_status,edh.pf_status as pf_status,"
			+ "edh.pt_status as pt_status,edh.permanent_file as permanent_file,edh.permanent_done_by as permanent_done_by,edh.permanent_status as permanent_status,edh.date_of_permanent as date_of_permanent,"
			+ "edh.aadhar as aadhar,edh.photo_upload_status as photo_upload_status,edh.proctor_assign_status as proctor_assign_status,"
			+ "edh.bank_account_holder_name as bank_account_holder_name,edh.grosspay_ctc as grosspay_ctc,edh.nda_nca_attach as nda_nca_attach,edh.store_indent_approver1 as store_indent_approver1,"
			+ "edh.esi_no as esi_no,edh.hra as hra,edh.da as da,edh.cca as cca,edh.ta as ta,edh.net_pay as net_pay,"
			+ "edh.other_allow as other_allow,edh.spl_1 as spl_1,edh.fte_status as fte_status,edh.title as title,edh.caste_category as caste_category,edh.contract_emp_type as contract_emp_type,"
			+ "edh.school as school,edh.contract_empcode as contract_empcode,edh.consolidated_amount as consolidated_amount,edh.from_date as from_date,"
			+ "edh.date_of_joining as date_of_joining,edh.remarks as remarks,"
			+ "edh.subject_skills as subject_skills,edh.employee_status as employee_status,edh.proctor_type as proctor_type,ss.salary_structure as salary_structure,"
			+ "dg.designation_short_name as designation_short_name,dg.designation_name As designation_name,"
			+ "emt.empTypeShortName as empTypeShortName,ua.username as username,jt.job_type as job_type,"
			+ "lan.employee_name as leaveApproverName1,lan2.employee_name as leaveApproverName2,sia.employee_name as storeIndentApproverName,"
			+ "edh.job_short_name as jobShortName,edh.dept_name_short as deptNameShort,edh.school_name_short as schoolNameShort,edh.shift_name as UpdateShiftName,"
			+ "edh.chief_proctor_id as chief_proctor_id,o.offer_id as offer_id,edh.religion as religion,edh.preferred_name_for_email as preferred_name_for_email,"
			+ "sh.shiftName as shiftName,jt.job_short_name as job_short_name, ca.consoliatedAmountId as consoliatedAmountId,ca.subject as subject) "
			+ "from EmployeeDetails edh "
			+ "LEFT JOIN ConsoliatedAmount ca ON ca.empId = edh.emp_id AND ca.created_Date = (SELECT MIN(ca2.created_Date) FROM ConsoliatedAmount ca2 WHERE ca2.empId = edh.emp_id) "
			+ "left join Schools sc on edh.school_id=sc.school_id " 
			+ "left join Offer o on edh.job_id=o.job_id and o.offer_id = "
			+ "(SELECT MAX(offer.offer_id) FROM Offer offer WHERE offer.job_id = edh.job_id ) "
			+ "left join Department d on edh.dept_id=d.dept_id "
			+ "left join Shift sh on edh.shift_category_id=sh.shiftCategoryId "
			+ "left join JobType jt on edh.job_type_id=jt.job_type_id "
			+ "left join EmployeeType emt on edh.emp_type_id=emt.empTypeId "
			+ "left join SalaryStructure ss on edh.salary_structure_id=ss.salary_structure_id "
			+ "left join UserAuthentication ua on ua.email=edh.email "
			+ "left join EmployeeDetails lan on lan.emp_id=edh.leave_approver1_emp_id "
			+ "left join EmployeeDetails lan2 on lan2.emp_id=edh.leave_approver2_emp_id "
			+ "left join EmployeeDetails sia on sia.emp_id=edh.store_indent_approver1 "
			+ "left join Designation dg on edh.designation_id=dg.designation_id "
			+ "where (:school_id is null or edh.school_id = :school_id) "
		    + "And (:dept_id is null or edh.dept_id = :dept_id) "
		    + "And (:designation_id is null or edh.designation_id = :designation_id) "
		    + "And (:job_type_id is null or edh.job_type_id = :job_type_id) And edh.active=true group by edh.emp_id")
	public Page<Object> getAllSortedData(Pageable pageable, Integer dept_id, Integer school_id, Integer designation_id, Integer job_type_id);

//	+ "Inner join UserAuthentication ua on ua.email=edh.email "
//	+ "left join UserRole ur on ur.id=ua.id "
//	+ "left join Role r on r.role_id=ur.role_id ")

//	@Query(value = "select new map(ed.email as email,ed.emp_id as emp_id) from EmployeeDetails ed "
//			+ "left join JobProfile jp on ed.job_id=jp.job_id where jp.resume_headline='Teaching' and ")
//	public List<HashMap<String, Object>> getEmailForCourseAssign();

	@Query(value = "select new map(ed.email as email,ed.emp_id as emp_id) from EmployeeDetails ed "
			+ "left join JobType jt on ed.job_type_id=jt.job_type_id where jt.job_type='Teaching' OR jt.job_type='Teaching Admin'")
	public List<HashMap<String, Object>> getEmailForCourseAssign();

	@Query(value = "select new map(ed.employee_name as employee_name,ed.emp_id as emp_id) from EmployeeDetails ed "
			+ "where ed.proctor_type IN (1,2,3)")
	public List<HashMap<String, Object>> getProctorDeatil();

	@Query(value = "select new map(ed.emp_id as emp_id,ed.employee_name as employee_name,ed.empcode as empcode,"
			+ "ed.dept_id as dept_id,d.dept_name_short as dept_name_short) from EmployeeDetails ed "
			+ "left join Department d on ed.dept_id=d.dept_id where ed.school_id=?1 and ed.active=true")
	public List<HashMap<String, Object>> getEmployeeName(Integer school_id);

	@Query(value = "SELECT ed.emp_id as emp_id,ed.employee_name as employee_name,ed.empcode as empcode,ed.dept_id as dept_id,"
			+ "d.dept_name_short as dept_name_short,sc.school_id as school_id,sc.school_name_short as school_name_short FROM employee_details ed "
			+ "left join department d on ed.dept_id=d.dept_id "
			+ "left join schools sc on ed.school_id=sc.school_id "
			+ "WHERE emp_id NOT IN (SELECT ph.emp_id FROM proctor_head ph where ph.emp_id Is Not null) and ed.active=true", nativeQuery = true)
	public List<Map<String, Object>> getEmployeeNames();

	@Modifying
	@Query(value = "update EmployeeDetails ed set ed.chief_proctor_id=:chief_proctor_id where ed.emp_id IN :emp_id")
	public void updateProctorHead(Integer chief_proctor_id, List<Integer> emp_id);

	@Query(value = "Select new map(ed.emp_id as emp_id, ed.employee_name as employee_name, ed.email as email, "
			+ "ed.dept_id as dept_id, ed.school_id as school_id, sc.school_name_short as school_name_short, "
			+ "d.dept_name_short as dept_name_short, be.financial_year_id as financial_year_id, "
			+ "be.created_date as created_date, fy.financial_year as financial_year) "
			+ "From EmployeeDetails ed Inner join BudgetExpense be On be.school_id=ed.school_id "
			+ "left join Department d On ed.dept_id=d.dept_id " + "left join Schools sc On ed.school_id=sc.school_id "
			+ "Left join FinancialYear fy On fy.financial_year_id=be.financial_year_id Where ed.email=?1")
	public List<HashMap<String, Object>> EmployeeDetailsForBudgetExpense(String email);

	@Query(value = "Select new map(ed.employee_name as employee_name, ed.email as email, ed.current_location as current_location, "
			+ "ed.designation_id as designation_id, ed.email as email, ed.empcode as empcode, ed.father_name as father_name, "
			+ "ed.gender as gender, ed.master_code as master_code, ed.mobile as mobile, sc.school_name_short as school_name_short, "
			+ "d.dept_name_short as dept_name_short, d.dept_name as dept_name, "
			+ "st.name as state_name, con.name as country_name,dg.designation_short_name as designation_short_name,"
			+ "concat(sh.shiftName,' ',sh.shiftStartTime,'-',sh.shiftEndTime) as shift_name,jt.job_short_name as job_short_name,"
			+ "ed.dateofbirth as date_of_birth,ed.blood_group as blood_group,ed.alt_mobile_no as alternate_phone_no,"
			+ "ed.personal_email as personal_email,ed.father_name as father_name,lap1.employee_name as leave_approver1_name,lap2.employee_name as leave_approver2_name,"
			+ "ed.emp_image_attachment_path as emp_image_attachment_path,ed.emp_id as emp_id,ed.date_of_joining as date_of_joining,dg.designation_name as designation_name) From EmployeeDetails ed "
			+ "Left join JobProfile jp On jp.job_id=ed.job_id "
			+ "left join Department d On ed.dept_id=d.dept_id " 
			+ "left join State st on jp.state_id = st.id "
			+ "left join Country con on jp.country_id = con.id "
			+ "left join Designation dg on ed.designation_id=dg.designation_id "
			+ "left join Shift sh on ed.shift_category_id=sh.shiftCategoryId "
			+ "left join JobType jt on ed.job_type_id=jt.job_type_id "
			+ "left join EmployeeDetails lap1 on lap1.emp_id=ed.leave_approver1_emp_id "
			+ "left join EmployeeDetails lap2 on lap2.emp_id=ed.leave_approver2_emp_id "
			+ "left join Schools sc On ed.school_id=sc.school_id Where ed.email=?1")
	public HashMap<String, Object> EmployeeDetailsForMobileApp(String email);

	@Query(value = "select ed from EmployeeDetails ed where ed.emp_id in :emp_id and ed.active=true")
	public List<EmployeeDetails> activeEmployeeDetailsForProctor(List<Integer> emp_id);

	@Modifying
	@Query(value = "update EmployeeDetails ed set ed.email=?1 where ed.emp_id=?2")
	public void updateEmail(String email, Integer emp_id);

	@Query(value = "Select ed from EmployeeDetails ed where ed.emp_id=?1 and ed.active=true")
	public EmployeeDetails getEmployeeDetailsByEmployeeId(Integer emp_id);

	@Query(value = "select r.role_id,r.role_name,r.role_short_name from roles r left join user_role ur on "
			+ "r.role_id=ur.role_id left join user_details u on u.id=ur.id where u.email=?1", nativeQuery = true)
	public List<Map<String, Object>> getRolesByEmployeeEmail(String email);

	@Query(value = "select edh.emp_id as emp_id,edh.job_id as job_id,edh.empcode as empcode,edh.employee_name as employee_name,edh.phd_status as phd_status,edh.personal_email as personal_email,"
			+ "(select distinct bt.cardid from  biometric_transaction bt WHERE bt.empcode = edh.empcode AND bt.cardid IS NOT NULL LIMIT 1) as biometricCardId,"
			+ "edh.designation_id as designation_id,edh.date_of_joining as date_of_joining,edh.email as email,edh.school_id as school_id,edh.dept_id as dept_id,"
			+ "edh.grosspay_ctc as grosspay_ctc,edh.ctc as ctc,edh.gender as gender,edh.employee_status as employee_status,edh.created_date as created_date,"
			+ " CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ',TIMESTAMPDIFF(MONTH, STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ',"
			+ " DATEDIFF(CURDATE(), DATE_ADD(STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y'),INTERVAL TIMESTAMPDIFF(MONTH, STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) % 30, 'D') AS experience,"
			+ "sc.school_name_short as school_name_short,sc.school_name As school_name,d.dept_name_short as dept_name_short,edh.net_pay as net_pay,edh.bank_id as bank_name,d.dept_name as dept_name,"
			+ "edh.created_by as created_by,edh.created_username as created_username,edh.active as active,edh.master_code as master_code,edh.spouse_name as spouse_name,"
			+ "edh.firstname as firstname,edh.lastname as lastname,edh.report_id as report_id,edh.bank_id as bank_id,edh.shift_category_id as shift_category_id,"
			+ "edh.mobile as mobile,edh.alt_mobile_no as alt_mobile_no,edh.dateofbirth as dateofbirth,edh.martial_status as martial_status,edh.blood_group as blood_group,"
			+ "edh.father_name as father_name,edh.current_location as current_location,edh.salary_structure_id as salary_structure_id,edh.permanent_status As permanent_status,"
			+ "edh.pf_no as pf_no,edh.pan_no as pan_no,edh.emp_type_id as emp_type_id,edh.job_type_id as job_type_id,edh.exp_in_years as exp_in_years,"
			+ "edh.exp_in_months as exp_in_months,edh.hometown as hometown,edh.pincode as pincode,edh.annual_salary as annual_salary,edh.spl_pay as spl_pay,"
			+ "edh.key_skills as key_skills,edh.bank_account_no as bank_account_no,edh.bank_branch as bank_branch,edh.bank_ifsccode as bank_ifsccode,"
			+ "edh.leave_approver1_emp_id as leave_approver1_emp_id,edh.leave_approver2_emp_id as leave_approver2_emp_id,edh.attach as attach,edh.dlno as dlno,edh.dlexpno as dlexpno,"
			+ "edh.passportno as passportno,edh.passportexpno as passportexpno,edh.bankacc2 as bankacc2,edh.to_date as to_date,edh.prob as prob,"
			+ "edh.nda as nda,edh.nca as nca,edh.uan_no as uan_no,edh.punched_card_status as punched_card_status,edh.photo as photo,edh.maternity_status as maternity_status,"
			+ "edh.marriage_status as marriage_status,edh.paternity_status as paternity_status,edh.transport_status as transport_status,edh.salary_approve_status as salary_approve_status,"
			+ "edh.vehicle_route_id as vehicle_route_id,edh.transport_assign_date as transport_assign_date,"
			+ "edh.emp_image_attachment_path as emp_image_attachment_path,edh.emp_attachment_file_name2 as emp_attachment_file_name2,"
			+ "edh.transport_deassign_date as transport_deassign_date,edh.new_join_status as new_join_status,edh.pf_status as pf_status,"
			+ "edh.pt_status as pt_status,edh.permanent_file as permanent_file,edh.aadhar as aadhar,edh.photo_upload_status as photo_upload_status,edh.proctor_assign_status as proctor_assign_status,"
			+ "edh.bank_account_holder_name as bank_account_holder_name,edh.nda_nca_attach as nda_nca_attach,edh.store_indent_approver1 as store_indent_approver1,"
			+ "edh.store_indent_approver2 as store_indent_approver2,edh.esi_no as esi_no,edh.hra as hra,edh.da as da,edh.cca as cca,edh.ta as ta,"
			+ "edh.mfo As mfo,edh.pinfl As pinfl,edh.plastic_card As plastic_card,edh.transport_assign_month As transport_assign_month,edh.transport_deassign_month as transport_deassign_month,"
			+ "edh.other_allow as other_allow,edh.spl_1 as spl_1,edh.fte_status as fte_status,edh.title as title,edh.caste_category as caste_category,edh.contract_emp_type as contract_emp_type,"
			+ "edh.school as school,edh.contract_empcode as contract_empcode,edh.consolidated_amount as consolidated_amount,edh.from_date as from_date,edh.remarks as remarks,"
			+ "edh.subject_skills as subject_skills,edh.proctor_type as proctor_type,ss.salary_structure as salary_structure,dg.designation_short_name as designation_short_name,"
			+ "emt.emp_type_short_name as emp_type_short_name,emt.emp_type as emp_type,edh1.email as reporting_email,dg.designation_name as designation_name,"
			+ "edh.personal_medical_history as personal_medical_history,edh.family_medical_history as family_medical_history,edh1.employee_name as reporting_employeeName,"
			+ "edh.chief_proctor_id as chief_proctor_id,edh.religion as religion,edh.preferred_name_for_email as preferred_name_for_email,sh.shift_name as shift_name,"
			+ "sh.shift_start_time as shift_start_time,sh.shift_end_time as shift_end_time,jt.job_short_name as job_short_name,edh.height as height,"
			+ "jt.job_type as job_type,sia.employee_name as storeIndentApproverName1,sia2.employee_name as storeIndentApproverName2,ed.employee_name as proctorName,"
			+ "lap1.employee_name as leave_approver1_name,lap2.employee_name as leave_approver2_name,o.offer_id as offer_id from employee_details edh "
			+ "left join schools sc on edh.school_id=sc.school_id "
			+ "left join department d on edh.dept_id=d.dept_id " 
			+ "left join bank b on edh.bank_id=b.bank_id "
			+ "left join proctor_head ph on edh.chief_proctor_id=ph.chief_proctor_id "
			+ "left join employee_details ed on ed.emp_id=ph.emp_id "
			+ "left join offer o on edh.job_id=o.job_id " 
			+ "left join job_profile jp on edh.job_id=jp.job_id "
			+ "left join shift sh on edh.shift_category_id=sh.shift_category_id "
			+ "left join job_type jt on edh.job_type_id=jt.job_type_id "
			+ "left join employee_type emt on edh.emp_type_id=emt.emp_type_id "
			+ "left join family_structure fs on edh.emp_id=fs.emp_id "
			+ "left join salary_structure ss on edh.salary_structure_id=ss.salary_structure_id "
			+ "left join user_details ud on ud.email=edh.email "
			+ "left join employee_details edh1 on edh1.emp_id=edh.report_id "
			+ "left join designation dg on edh.designation_id=dg.designation_id "
			+ "left join employee_details sia on sia.emp_id=edh.store_indent_approver1 "
			+ "left join employee_details sia2 on sia2.emp_id=edh.store_indent_approver2 "
			+ "left join employee_details lap1 on lap1.emp_id=edh.leave_approver1_emp_id "
			+ "left join employee_details lap2 on lap2.emp_id=edh.leave_approver2_emp_id where edh.emp_id=?1 ", nativeQuery = true)
	public List<Map<String, Object>> getEmployeeDetailById(Integer id);

	@Query(value = "select ed.email from EmployeeDetails ed where ed.dept_id=?1 and ed.active=true")
	public String[] getAllEmailsOfTeam(Integer dept_id);

	@Query(value = "select CONCAT(IFNULL(edh.employee_name,''),'-',IFNULL(edh.empcode,''),'-',(select d.dept_name_short from department d where "
			+ "edh.dept_id=d.dept_id and d.active=true)) as employeeName,edh.emp_id as emp_id,ud.id as id from user_details ud left join employee_details edh on ud.email=edh.email where "
			+ "ud.id NOT IN(select DISTINCT tte.emp_id from time_table_employee tte left join time_table tt on "
			+ "tte.time_table_id=tt.time_table_id where tte.selected_date between ?1 and ?2 "
			+ "and tte.time_slots_id=?3 and tt.week_day=?4 and tte.active=true) and edh.active=true", nativeQuery = true)
	public List<Map<String, Object>> getAllEmployeesForTimeTable(Date date1, Date date2, Integer time_slots_id,
			String day);

	@Query(value = "select emp from EmployeeDetails emp where emp.dept_id=(select edh.dept_id FROM EmployeeDetails edh where edh.emp_id=?1 and edh.active=true) and emp.active=true ")
	public List<EmployeeDetails> listEmailByDept11(Integer emp_id);

	@Query(value = "select new map(ed.emp_id as emp_id,ed.employee_name as employee_name) from EmployeeDetails ed where ed.emp_id=?1")
	public HashMap<String, Object> getLeaveApprover(Integer emp_id);

	@Query(value = "select ed from EmployeeDetails ed where ed.active=true And ed.email=(select ua.email from UserAuthentication ua where ua.id=?1 And ua.active=true)")
	public EmployeeDetails getEmployeeDataByUserID(Integer user_id);

//	@Query(value = "select CONCAT(emp.employee_name,'-',emp.empcode,'-',(select d.dept_name_short from department d where "
//			+ "emp.dept_id=d.dept_id and d.active=true)) as employeeName,emp.emp_id as emp_id from employee_details emp where "
//			+ "emp.dept_id=(select edh.dept_id FROM employee_details edh where edh.emp_id=?1 and edh.active=true) and emp.active=true",nativeQuery=true)
//	public List<Map<String, Object>> getEmployeesUnderDepartment(Integer emp_id);

	@Query(value = "select emp.emp_id as emp_id from employee_details emp where "
			+ "emp.dept_id=(select edh.dept_id FROM employee_details edh where edh.emp_id=?1 and edh.active=true) and emp.active=true", nativeQuery = true)
	public List<Integer> getEmployeesUnderDepartment(Integer emp_id);

	@Query(value = "select CONCAT(emp.employee_name,'-',emp.empcode) as employeeName,emp.emp_id as emp_id from employee_details emp where "
			+ "emp.emp_id IN ?1 and emp.active=true", nativeQuery = true)
	public List<Map<String, Object>> getStudentData1(List<Integer> emp_ids);

	@Query(value = "select emp.emp_id from employee_details emp where emp.emp_type_id=1", nativeQuery = true)
	public List<Integer> getAllEmployees();

	@Query(value = "select emp_id from employee_details where dept_id IN (?1) and active=true", nativeQuery = true)
	public List<Integer> getAllEmployeesByDeptId(List<Integer> dept_id);

	@Query(value = "select emp.emp_id from employee_details emp where emp.school_id=?1", nativeQuery = true)
	public List<Integer> getAllEmployeesBySchoolId(Integer school_id);

	@Query(value = "select emp.emp_id from employee_details emp where emp.dept_id=?1", nativeQuery = true)
	public List<Integer> getAllEmployeesById(Integer dept_id);

	@Modifying
	@Query(value = "update EmployeeDetails ed set ed.date_of_permanent=?2, ed.permanent_done_by=?3, ed.permanent_remarks=?4, ed.permanent_status=2 where ed.emp_id=?1 and ed.active=true")
	public void makeEmployeePermanent(Integer emp_id, String date, String permanent_done_by,String permanent_remarks);

	@Query(value = "select emp.emp_id from employee_details emp where emp.email=?1", nativeQuery = true)
	public Integer getEmployeeIdOnUserMail(String email);

	@Query(value = "select et.emp_type from employee_type et where et.emp_type_id=(select ed.emp_type_id from employee_details ed "
			+ "where ed.emp_id=?1 and ed.active=true)", nativeQuery = true)
	public String getEmpType(Integer emp_id);

	@Query(value = "select ed.emp_id as emp_id,ed.employee_name as employee_name,ed.empcode as empcode from employee_details ed "
			+ "where ed.emp_id=(select tte.emp_id from time_table_employee tte Where tte.time_table_id=?1 and tte.selected_date=?2 and tte.active=true)", nativeQuery = true)
	public List<Map<String, Object>> employeeDetailsForTimeTableView(Integer time_table_id, Date selected_date);

	public EmployeeDetails findByEmailAndActiveTrue(String email);

	@Query(value = "select new map(edh.emp_id as id,edh.job_id as job_id,edh.empcode as empcode,edh.employee_name as employee_name,edh.phd_status as phd_status,"
			+ "edh.designation_id as designation_id,edh.date_of_joining as date_of_joining,edh.email as email,edh.employee_name as employee_name,"
			+ "edh.grosspay_ctc as grosspay_ctc,edh.ctc as ctc,edh.gender as gender,edh.employee_status as employee_status,edh.created_date as created_date,"
			+ "sc.school_name_short as school_name_short,d.dept_name_short as dept_name_short,edh.net_pay as net_pay,edh.bank_id as bank_name,"
			+ "edh.created_by as created_by,edh.created_username as created_username,edh.active as active,edh.master_code as master_code,"
			+ "edh.empcode as empcode,edh.firstname as firstname,edh.lastname as lastname,edh.report_id as report_id,"
			+ "edh.mobile as mobile,edh.alt_mobile_no as alt_mobile_no,edh.dateofbirth as dateofbirth,edh.martial_status as martial_status,edh.blood_group as blood_group,"
			+ "edh.designation_id as designation_id,edh.father_name as father_name,edh.current_location as current_location,edh.salary_structure_id as salary_structure_id,"
			+ "edh.ctc as ctc,edh.pf_no as pf_no,edh.pan_no as pan_no,edh.emp_type_id as emp_type_id,edh.job_type_id as job_type_id,edh.exp_in_years as exp_in_years,"
			+ "edh.exp_in_months as exp_in_months,edh.hometown as hometown,edh.pincode as pincode,edh.annual_salary as annual_salary,edh.spl_pay as spl_pay,"
			+ "edh.key_skills as key_skills,edh.bank_account_no as bank_account_no,edh.bank_branch as bank_branch,edh.bank_ifsccode as bank_ifsccode,"
			+ "edh.leave_approver1_emp_id as leave_approver1_emp_id,edh.leave_approver2_emp_id as leave_approver2_emp_id,edh.attach as attach,edh.dlno as dlno,edh.dlexpno as dlexpno,"
			+ "edh.passportno as passportno,edh.passportexpno as passportexpnow,edh.bankacc2 as bankacc2,edh.to_date as to_date,edh.prob as prob,"
			+ "edh.nda as nda,edh.nca as nca,edh.uan_no as uan_no,edh.punched_card_status as punched_card_status,edh.photo as photo,edh.maternity_status as maternity_status,"
			+ "edh.marriage_status as marriage_status,edh.paternity_status as paternity_status,edh.transport_status as transport_status,edh.salary_approve_status as salary_approve_status,"
			+ "edh.vehicle_route_id as vehicle_route_id,edh.transport_assign_month as transport_assign_month,edh.transport_assign_date as transport_assign_date,"
			+ "edh.transport_deassign_date as transport_deassign_date,edh.transport_deassign_month as transport_deassign_month,edh.new_join_status as new_join_status,edh.pf_status as pf_status,"
			+ "edh.pt_status as pt_status,edh.permanent_file as permanent_file,edh.aadhar as aadhar,edh.photo_upload_status as photo_upload_status,edh.proctor_assign_status as proctor_assign_status,"
			+ "edh.bank_account_holder_name as bank_account_holder_name,edh.grosspay_ctc as grosspay_ctc,edh.nda_nca_attach as nda_nca_attach,edh.store_indent_approver1 as store_indent_approver1,"
			+ "edh.store_indent_approver2 as store_indent_approver2,edh.esi_no as esi_no,edh.hra as hra,edh.da as da,edh.cca as cca,edh.ta as ta,edh.net_pay as net_pay,"
			+ "edh.other_allow as other_allow,edh.spl_1 as spl_1,edh.fte_status as fte_status,edh.title as title,edh.caste_category as caste_category,edh.contract_emp_type as contract_emp_type,"
			+ "edh.school as school,edh.contract_empcode as contract_empcode,edh.consolidated_amount as consolidated_amount,edh.from_date as from_date,edh.date_of_joining as date_of_joining,edh.remarks as remarks,"
			+ "edh.subject_skills as subject_skills,edh.employee_status as employee_status,edh.proctor_type as proctor_type,ss.salary_structure as salary_structure,dg.designation_short_name as designation_short_name,"
			+ "emt.empTypeShortName as empTypeShortName,res.relieving_date as relieving_date,res.resignation_id as resignation_id,"
			+ "edh.chief_proctor_id as chief_proctor_id,o.offer_id as offer_id,edh.religion as religion,edh.preferred_name_for_email as preferred_name_for_email,sh.shiftName as shiftName,jt.job_short_name as job_short_name) from EmployeeDetails edh "
			+ "left join Schools sc on edh.school_id=sc.school_id " 
			+ "left join Offer o on edh.job_id=o.job_id "
			+ "left join Department d on edh.dept_id=d.dept_id "
			+ "left join Shift sh on edh.shift_category_id=sh.shiftCategoryId "
			+ "left join JobType jt on edh.job_type_id=jt.job_type_id "
			+ "left join EmployeeType emt on edh.emp_type_id=emt.empTypeId "
			+ "left join SalaryStructure ss on edh.salary_structure_id=ss.salary_structure_id "
			+ "left join Designation dg on edh.designation_id=dg.designation_id "
			+ "left join Resignation res on edh.emp_id=res.emp_id "
			+ "where edh.active=false And CONCAT(IfNull(edh.empcode,''),'',IfNull(edh.employee_name,''),'',IfNull(edh.preferred_name_for_email,''),' ',IfNull(edh.email,''),'',IfNull(sc.school_name_short,''),'',IfNull(d.dept_name_short,'')) LIKE %?1% ")
	public Page<Object> getAllInActiveDataFilteredByKeyword(Pageable pageable, Object keyword); 
	

	@Query(value = "select new map(edh.emp_id as id,edh.job_id as job_id,edh.empcode as empcode,edh.employee_name as employee_name,edh.phd_status as phd_status,"
			+ "edh.designation_id as designation_id,edh.date_of_joining as date_of_joining,edh.email as email,edh.employee_name as employee_name,"
			+ "edh.grosspay_ctc as grosspay_ctc,edh.ctc as ctc,edh.gender as gender,edh.employee_status as employee_status,edh.created_date as created_date,"
			+ "sc.school_name_short as school_name_short,d.dept_name_short as dept_name_short,edh.net_pay as net_pay,edh.bank_id as bank_name,"
			+ "edh.created_by as created_by,edh.created_username as created_username,edh.active as active,edh.master_code as master_code,"
			+ "edh.empcode as empcode,edh.firstname as firstname,edh.lastname as lastname,edh.report_id as report_id,"
			+ "edh.mobile as mobile,edh.alt_mobile_no as alt_mobile_no,edh.dateofbirth as dateofbirth,edh.martial_status as martial_status,edh.blood_group as blood_group,"
			+ "edh.father_name as father_name,edh.current_location as current_location,edh.salary_structure_id as salary_structure_id,"
			+ "edh.ctc as ctc,edh.pf_no as pf_no,edh.pan_no as pan_no,edh.emp_type_id as emp_type_id,edh.job_type_id as job_type_id,edh.exp_in_years as exp_in_years,"
			+ "edh.exp_in_months as exp_in_months,edh.hometown as hometown,edh.pincode as pincode,edh.annual_salary as annual_salary,edh.spl_pay as spl_pay,"
			+ "edh.key_skills as key_skills,edh.bank_account_no as bank_account_no,edh.bank_branch as bank_branch,edh.bank_ifsccode as bank_ifsccode,"
			+ "edh.leave_approver1_emp_id as leave_approver1_emp_id,edh.leave_approver2_emp_id as leave_approver2_emp_id,edh.attach as attach,edh.dlno as dlno,edh.dlexpno as dlexpno,"
			+ "edh.passportno as passportno,edh.passportexpno as passportexpnow,edh.bankacc2 as bankacc2,edh.to_date as to_date,edh.prob as prob,"
			+ "edh.nda as nda,edh.nca as nca,edh.uan_no as uan_no,edh.punched_card_status as punched_card_status,edh.photo as photo,edh.maternity_status as maternity_status,"
			+ "edh.marriage_status as marriage_status,edh.paternity_status as paternity_status,edh.transport_status as transport_status,edh.salary_approve_status as salary_approve_status,"
			+ "edh.vehicle_route_id as vehicle_route_id,edh.transport_assign_month as transport_assign_month,edh.transport_assign_date as transport_assign_date,"
			+ "edh.transport_deassign_date as transport_deassign_date,edh.transport_deassign_month as transport_deassign_month,edh.new_join_status as new_join_status,edh.pf_status as pf_status,"
			+ "edh.pt_status as pt_status,edh.permanent_file as permanent_file,edh.aadhar as aadhar,edh.photo_upload_status as photo_upload_status,edh.proctor_assign_status as proctor_assign_status,"
			+ "edh.bank_account_holder_name as bank_account_holder_name,edh.grosspay_ctc as grosspay_ctc,edh.nda_nca_attach as nda_nca_attach,edh.store_indent_approver1 as store_indent_approver1,"
			+ "edh.store_indent_approver2 as store_indent_approver2,edh.esi_no as esi_no,edh.hra as hra,edh.da as da,edh.cca as cca,edh.ta as ta,edh.net_pay as net_pay,"
			+ "edh.other_allow as other_allow,edh.spl_1 as spl_1,edh.fte_status as fte_status,edh.title as title,edh.caste_category as caste_category,edh.contract_emp_type as contract_emp_type,"
			+ "edh.school as school,edh.contract_empcode as contract_empcode,edh.consolidated_amount as consolidated_amount,edh.from_date as from_date,edh.date_of_joining as date_of_joining,edh.remarks as remarks,"
			+ "edh.subject_skills as subject_skills,edh.employee_status as employee_status,edh.proctor_type as proctor_type,ss.salary_structure as salary_structure,dg.designation_short_name as designation_short_name,"
			+ "emt.empTypeShortName as empTypeShortName,res.relieving_date as relieving_date,res.resignation_id as resignation_id,"
			+ "edh.chief_proctor_id as chief_proctor_id,o.offer_id as offer_id,edh.religion as religion,edh.preferred_name_for_email as preferred_name_for_email,sh.shiftName as shiftName,jt.job_short_name as job_short_name) from EmployeeDetails edh "
			+ "left join Schools sc on edh.school_id=sc.school_id " + "left join Offer o on edh.job_id=o.job_id "
			+ "left join Department d on edh.dept_id=d.dept_id "
			+ "left join Shift sh on edh.shift_category_id=sh.shiftCategoryId "
			+ "left join JobType jt on edh.job_type_id=jt.job_type_id "
			+ "left join EmployeeType emt on edh.emp_type_id=emt.empTypeId "
			+ "left join SalaryStructure ss on edh.salary_structure_id=ss.salary_structure_id "
			+ "left join Designation dg on edh.designation_id=dg.designation_id "
			+ "left join Resignation res on edh.emp_id=res.emp_id "
			+ "Where edh.active=false")
	public Page<Object> getAllInActiveSortedData(Pageable pageable);	
	


//	@Query(value = "select edh.emp_id as emp_id,edh.job_id as job_id,edh.employee_name as employee_name,edh.phd_status as phd_status,"
//			+ "edh.designation_id as designation_id,edh.date_of_joining as date_of_joining,edh.email as email,"
//			+ "edh.grosspay_ctc as grosspay_ctc,edh.ctc as ctc,edh.gender as gender,edh.employee_status as employee_status,edh.created_date as created_date,"
//			+ "sc.school_name_short as school_name_short,edh.net_pay as net_pay,b.bank_name as bank_name,"
//			+ "edh.created_by as created_by,edh.created_username as created_username,edh.active as active,edh.master_code as master_code,"
//			+ "edh.empcode as empcode,edh.firstname as firstname,edh.lastname as lastname,edh.report_id as report_id,"
//			+ "edh.mobile as mobile,edh.alt_mobile_no as alt_mobile_no,edh.dateofbirth as dateofbirth,edh.martial_status as martial_status,edh.blood_group as blood_group,"
//			+ "edh.father_name as father_name,edh.current_location as current_location,edh.salary_structure_id as salary_structure_id,"
//			+ "edh.pf_no as pf_no,edh.emp_type_id as emp_type_id,edh.job_type_id as job_type_id,edh.exp_in_years as exp_in_years,"
//			+ "edh.exp_in_months as exp_in_months,edh.hometown as hometown,edh.pincode as pincode,edh.annual_salary as annual_salary,edh.spl_pay as spl_pay,"
//			+ "edh.key_skills as key_skills,edh.bank_account_no as bank_account_no,edh.bank_branch as bank_branch,"
//			+ "d.dept_name as dept_name,dg.designation_name as designation_name,edh.language_id as language_id,"
//			+ "edh.nationality as nationality,edh.height as height,edh.pinfl as pinfl,edh.id_barcode_generated as id_barcode_generated,"
//			+ "d.dept_name_short as dept_name_short,dg.designation_short_name as designation_short_name,edh.cancel_remark as cancel_remark,"
//			+ "edh.leave_approver1_emp_id as leave_approver1_emp_id,edh.leave_approver2_emp_id as leave_approver2_emp_id,edh.attach as attach,edh.dlno as dlno,edh.dlexpno as dlexpno,"
//			+ "edh.passportno as passportno,edh.passportexpno as passportexpnow,edh.bankacc2 as bankacc2,edh.to_date as to_date,edh.prob as prob,"
//			+ "edh.nda as nda,edh.nca as nca,edh.uan_no as uan_no,edh.punched_card_status as punched_card_status,edh.photo as photo,edh.maternity_status as maternity_status,"
//			+ "edh.marriage_status as marriage_status,edh.paternity_status as paternity_status,edh.transport_status as transport_status,edh.salary_approve_status as salary_approve_status,"
//			+ "edh.vehicle_route_id as vehicle_route_id,edh.salary_block_date as salary_block_date,edh.transport_assign_month as transport_assign_month,edh.transport_assign_date as transport_assign_date,"
//			+ "edh.transport_deassign_date as transport_deassign_date,edh.transport_deassign_month as transport_deassign_month,edh.new_join_status as new_join_status,edh.pf_status as pf_status,"
//			+ "edh.pt_status as pt_status,edh.permanant_file as permanant_file,edh.aadhar as aadhar,edh.photo_upload_status as photo_upload_status,edh.proctor_assign_status as proctor_assign_status,"
//			+ "edh.pick_up_point as pick_up_point,edh.bank_account_holder_name as bank_account_holder_name,edh.nda_nca_attach as nda_nca_attach,edh.store_indent_approver1 as store_indent_approver1,"
//			+ "edh.store_indent_approver2 as store_indent_approver2,edh.esi_no as esi_no,edh.hra as hra,edh.da as da,edh.cca as cca,edh.ta as ta,edh.mr as mr,edh.me as me,"
//			+ "edh.other_allow as other_allow,edh.spl_1 as spl_1,edh.fte_status as fte_status,edh.pick_id as pick_id,edh.title as title,edh.caste_category as caste_category,edh.contract_emp_type as contract_emp_type,"
//			+ "edh.school as school,edh.contract_empcode as contract_empcode,edh.consolidated_amount as consolidated_amount,edh.from_date as from_date,edh.remarks as remarks,"
//			+ "edh.subject_skills as subject_skills,edh.proctor_type as proctor_type,ss.salary_structure as salary_structure,"
//			+ "edh.oked as oked,edh.mfo as mfo,edh.pin_number as pin_number,edh.plastic_card as plastic_card,"
//			+ "edh.personal_medical_history as personal_medical_history,edh.family_medical_history as family_medical_history,"
//			+ "edh.visa_expiry_date as visa_expiry_date,edh.visa_document_number as visa_document_number,"
//			+ "edh.chief_proctor_id as chief_proctor_id,o.offer_id as offer_id,edh.religion as religion,"
//			+ "edh.preferred_name_for_email as preferred_name_for_email,jt.job_short_name as job_short_name,ua.id as user_id "
//			+ "from employee_details edh " + "left join schools sc on edh.school_id=sc.school_id "
//			+ "left join offer o on edh.job_id=o.job_id " + "left join department d on edh.dept_id=d.dept_id "
//			+ "left join bank b on edh.bank_id=b.bank_id "
//			+ "left join job_type jt on edh.job_type_id=jt.job_type_id "
//			+ "left join salary_structure ss on edh.salary_structure_id=ss.salary_structure_id "
//			+ "left join designation dg on edh.designation_id=dg.designation_id "
//			+ "left join user_details ua on ua.email=edh.email and ua.active=true and edh.active=true "
//			+ "where edh.active=true", nativeQuery = true)
	
	@Query(value = "select edh.emp_id as emp_id,edh.job_id as job_id,edh.empcode as empcode,edh.employee_name as employee_name,edh.phd_status as phd_status,"
			+ "edh.designation_id as designation_id,edh.date_of_joining as date_of_joining,edh.email as email,edh.school_id as school_id,edh.dept_id as dept_id,"
			+ "edh.grosspay_ctc as grosspay_ctc,edh.ctc as ctc,edh.gender as gender,edh.employee_status as employee_status,edh.created_date as created_date,"
			+ "sc.school_name_short as school_name_short,d.dept_name_short as dept_name_short,edh.net_pay as net_pay,edh.bank_id as bank_name,"
			+ "edh.created_by as created_by,edh.created_username as created_username,edh.active as active,edh.master_code as master_code,edh.spouse_name as spouse_name,"
			+ "edh.firstname as firstname,edh.lastname as lastname,edh.report_id as report_id,edh.bank_id as bank_id,edh.shift_category_id as shift_category_id,"
			+ "edh.mobile as mobile,edh.alt_mobile_no as alt_mobile_no,edh.dateofbirth as dateofbirth,edh.martial_status as martial_status,edh.blood_group as blood_group,"
			+ "edh.father_name as father_name,edh.current_location as current_location,edh.salary_structure_id as salary_structure_id,"
			+ "edh.pf_no as pf_no,edh.pan_no as pan_no,edh.emp_type_id as emp_type_id,edh.job_type_id as job_type_id,edh.exp_in_years as exp_in_years,"
			+ "edh.exp_in_months as exp_in_months,edh.hometown as hometown,edh.pincode as pincode,edh.annual_salary as annual_salary,edh.spl_pay as spl_pay,"
			+ "edh.key_skills as key_skills,edh.bank_account_no as bank_account_no,edh.bank_branch as bank_branch,edh.bank_ifsccode as bank_ifsccode,"
			+ "edh.leave_approver1_emp_id as leave_approver1_emp_id,edh.leave_approver2_emp_id as leave_approver2_emp_id,edh.attach as attach,edh.dlno as dlno,edh.dlexpno as dlexpno,"
			+ "edh.passportno as passportno,edh.passportexpno as passportexpno,edh.bankacc2 as bankacc2,edh.to_date as to_date,edh.prob as prob,"
			+ "edh.nda as nda,edh.nca as nca,edh.uan_no as uan_no,edh.punched_card_status as punched_card_status,edh.photo as photo,edh.maternity_status as maternity_status,"
			+ "edh.marriage_status as marriage_status,edh.paternity_status as paternity_status,edh.transport_status as transport_status,edh.salary_approve_status as salary_approve_status,"
			+ "edh.vehicle_route_id as vehicle_route_id,edh.transport_assign_month as transport_assign_month,edh.transport_assign_date as transport_assign_date,"
			+ "edh.transport_deassign_date as transport_deassign_date,edh.transport_deassign_month as transport_deassign_month,edh.new_join_status as new_join_status,edh.pf_status as pf_status,"
			+ "edh.pt_status as pt_status,edh.permanent_file as permanent_file,edh.aadhar as aadhar,edh.photo_upload_status as photo_upload_status,edh.proctor_assign_status as proctor_assign_status,"
			+ "edh.bank_account_holder_name as bank_account_holder_name,edh.nda_nca_attach as nda_nca_attach,edh.store_indent_approver1 as store_indent_approver1,"
			+ "edh.store_indent_approver2 as store_indent_approver2,edh.esi_no as esi_no,edh.hra as hra,edh.da as da,edh.cca as cca,edh.ta as ta,"
			+ "edh.other_allow as other_allow,edh.spl_1 as spl_1,edh.fte_status as fte_status,edh.title as title,edh.caste_category as caste_category,edh.contract_emp_type as contract_emp_type,"
			+ "edh.school as school,edh.contract_empcode as contract_empcode,edh.consolidated_amount as consolidated_amount,edh.from_date as from_date,edh.remarks as remarks,"
			+ "edh.subject_skills as subject_skills,edh.proctor_type as proctor_type,ss.salary_structure as salary_structure,dg.designation_short_name as designation_short_name,"
			+ "emt.emp_type_short_name as emp_type_short_name,emt.emp_type as emp_type,edh1.email as reporting_email,dg.designation_name as designation_name,"
			+ "edh.personal_medical_history as personal_medical_history,edh.family_medical_history as family_medical_history,edh1.employee_name as reporting_employeeName,"
			+ "edh.chief_proctor_id as chief_proctor_id,edh.religion as religion,edh.preferred_name_for_email as preferred_name_for_email,sh.shift_name as shift_name,"
			+ "sh.shift_start_time as shift_start_time,sh.shift_end_time as shift_end_time,jt.job_short_name as job_short_name,edh.height as height,"
			+ "jt.job_type as job_type,sia.employee_name as storeIndentApproverName1,sia2.employee_name as storeIndentApproverName2,"
			+ "lap1.employee_name as leave_approver1_name,lap2.employee_name as leave_approver2_name,o.offer_id as offer_id from employee_details edh "
			+ "left join schools sc on edh.school_id=sc.school_id "
			+ "left join department d on edh.dept_id=d.dept_id " 
			+ "left join bank b on edh.bank_id=b.bank_id "
			+ "left join offer o on edh.job_id=o.job_id " 
			+ "left join job_profile jp on edh.job_id=jp.job_id "
			+ "left join shift sh on edh.shift_category_id=sh.shift_category_id "
			+ "left join job_type jt on edh.job_type_id=jt.job_type_id "
			+ "left join employee_type emt on edh.emp_type_id=emt.emp_type_id "
			+ "left join family_structure fs on edh.emp_id=fs.emp_id "
			+ "left join salary_structure ss on edh.salary_structure_id=ss.salary_structure_id "
			+ "left join user_details ud on ud.email=edh.email  and ud.active=true and edh.active=true "
			+ "left join employee_details edh1 on edh1.emp_id=edh.report_id "
			+ "left join designation dg on edh.designation_id=dg.designation_id "
			+ "left join employee_details sia on sia.emp_id=edh.store_indent_approver1 "
			+ "left join employee_details sia2 on sia2.emp_id=edh.store_indent_approver2 "
			+ "left join employee_details lap1 on lap1.emp_id=edh.leave_approver1_emp_id "
			+ "left join employee_details lap2 on lap2.emp_id=edh.leave_approver2_emp_id where edh.active=true", nativeQuery = true)
	public List<Map<String, Object>> getEmployeeDetailsData();

	@Query(value = "select ed.employee_name as employee_name,ed.email as email,ed.mobile as mobile,ed.alt_mobile_no as alt_mobile_no,ua.id as id,"
			+ "ed.designation_id as designation_id,dg.designation_name as designation_name,dg.designation_short_name as designation_short_name "
			+ " from employee_details ed " + "left join designation dg on ed.designation_id=dg.designation_id "
			+ "inner join user_details ua on ed.email=ua.email " + "  where ed.active=true", nativeQuery = true)
	public List<Map<String, Object>> getStaffDetailsData();

	@Query(value = "Select edh.emp_id as emp_id,edh.dept_id as dept_id,edh.designation_id as designation_id,"
			+ "edh.employee_name as employee_name,edh.empcode as empcode,edh.date_of_joining as date_of_joining,"
			+ "edh.emp_image_attachment_path as emp_image_attachment_path,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,edh.phd_status as phd_status From employee_details edh "
			+ "left join department d on edh.dept_id=d.dept_id "
			+ "left join designation des on edh.designation_id=des.designation_id "
			+ "where edh.school_id=:schoolId And (:#{#departmentId} is null OR edh.dept_id=:#{#departmentId}) And edh.active=true And edh.emp_id not in (Select distinct eich.emp_id From employee_id_card_history eich Where eich.active=true) ", nativeQuery = true)
	public List<Map<String, Object>> getEmployeeDataForIdCard( @Param("schoolId") Integer schoolId, @Param("departmentId") Integer departmentId);

	@Query(value = "Select edh.emp_id as emp_id,edh.dept_id as dept_id,edh.designation_id as designation_id,"
			+ "edh.employee_name as employee_name,edh.empcode as empcode,edh.date_of_joining as date_of_joining,"
			+ "edh.emp_image_attachment_path as emp_image_attachment_path,edh.gender As gender,edh.active As active,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,edh.phd_status as phd_status,"
			+ "edh.mobile as mobile,edh.email as email,edh.created_date as created_date,edh.created_username as created_username,"
			+ "et.emp_type_short_name as emp_type_short_name,sch.school_name as school_name,"
			+ "sch.school_name_short as school_name_short,sch.display_name as display_name From employee_details edh "
			+ "left join department d on edh.dept_id=d.dept_id "
			+ "left join designation des on edh.designation_id=des.designation_id "
			+ "left join schools sch on sch.school_id=edh.school_id "
			+ "left join employee_type et on et.emp_type_id=edh.emp_type_id "
			+ "where edh.active=true "
			 + "And (:deptId is null or edh.dept_id = :deptId) "
			 + "And (:schoolId is null or edh.school_id = :schoolId) "
			+ "And edh.emp_type_id in (1,2) "
			+ "And edh.emp_id not in (Select distinct eich.emp_id From employee_id_card_history eich Where eich.active=true) ", nativeQuery = true)
	public List<Map<String, Object>> getEmployeeDataForIdCardNew(Integer deptId, Integer schoolId);
	
	
	@Query(value = "Select edh.emp_id as emp_id,edh.dept_id as dept_id,edh.designation_id as designation_id,"
			+ "edh.employee_name as employee_name,edh.empcode as empcode,edh.date_of_joining as date_of_joining,"
			+ "edh.emp_image_attachment_path as emp_image_attachment_path,edh.gender As gender,edh.active As active,"
			+ "des.designation_name as designation_name,des.designation_short_name as designation_short_name,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,edh.phd_status as phd_status,"
			+ "edh.mobile as mobile,edh.email as email,edh.created_date as created_date,edh.created_username as created_username,"
			+ "et.emp_type_short_name as emp_type_short_name,sch.school_name as school_name,"
			+ "sch.school_name_short as school_name_short,sch.display_name as display_name From employee_details edh "
			+ "left join department d on edh.dept_id=d.dept_id "
			+ "left join designation des on edh.designation_id=des.designation_id "
			+ "left join schools sch on sch.school_id=edh.school_id "
			+ "left join employee_type et on et.emp_type_id=edh.emp_type_id "
			+ "where edh.active=true "
			 + "And (:deptId is null or edh.dept_id = :deptId) "
			 + "And (:schoolId is null or edh.school_id = :schoolId) "
			+ "And edh.emp_type_id in (1,2) ", nativeQuery = true)
	public List<Map<String, Object>> getEmployeeDetailsForIdCardWoHistory(Integer deptId, Integer schoolId);

	@Modifying
	@Query(value = "update EmployeeDetails ed set ed.active=false where ed.emp_id=?1")
	public void deactivateEmployeeDetails(Integer emp_id);

	@Query(value = "select new map(ed.emp_id as emp_id,ed.employee_name as employee_name,ed.empcode as empcode,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name,ed.date_of_joining as date_of_joining) from EmployeeDetails ed "
			+ "left join Designation d on d.designation_id=ed.designation_id "
			+ "where ed.email=(select ua.email from UserAuthentication ua where ua.id=?1) And ed.active=true")
	public HashMap<String, Object> getEmployeeDetailsByUserID(Integer user_id);

	@Query(value = "select * from employee_details ed where ed.job_id=?1 And ed.active=true", nativeQuery = true)
	public EmployeeDetails getEmployeeDetailsByJobId(Integer job_id);

	@Query(value = "select ed.emp_id as emp_id,ed.dept_id as dept_id,"
			+ "Concat(IfNull(ed.employee_name,''),'-',IfNull(ed.empcode,''),'-',IfNull(dept.dept_name_short,'')) as mentor "
			+ "from employee_details ed " + "left join department dept on ed.dept_id=dept.dept_id "
			+ "where ed.chief_proctor_id IS NOT NULL and ed.active=true", nativeQuery = true)
	public List<Map<String, Object>> getEmployeeNameConcateWithEmployeeCodeAndDept();

	@Query(value = "select new map(edh.emp_id as id,edh.job_id as job_id,edh.empcode as empcode,edh.email as email,"
			+ "edh.employee_name as employee_name,edh.phd_status as phd_status,b.bank_name as bank_name,edh.cancel_remark as cancel_remark,"
			+ "edh.date_of_joining as date_of_joining,edh.employee_name as employee_name,edh.active as active,"
			+ "edh.grosspay_ctc as grosspay_ctc,edh.ctc as ctc,edh.created_date as created_date,jt.job_short_name as job_short_name,"
			+ "sc.school_name_short as school_name_short,edh.school_id as school_id,sc.school_name as school_name,"
			+ "edh.emp_type_id as emp_type_id,emt.empType as empType,emt.empTypeShortName as empTypeShortName,"
			+ "edh.designation_id as designation_id,dg.designation_name as designation_name,edh.new_join_status as new_join_status,"
			+ "dg.designation_short_name as designation_short_name,d.dept_name_short as dept_name_short,o.offer_id as offer_id,"
			+ "o.ctc as offer_ctc,o.gross as gross,edh.salary_approve_status as salary_approve_status,jt.job_type as job_type,"
			+ "edh.created_by as created_by,edh.created_username as created_username,edh.master_code as master_code) "
			+ "from EmployeeDetails edh " + "left join Schools sc on edh.school_id=sc.school_id "
			+ "left join Offer o on edh.job_id=o.job_id " + "left join Department d on edh.dept_id=d.dept_id "
			+ "left join Bank b on edh.bank_id=b.bank_id " + "left join JobType jt on edh.job_type_id=jt.job_type_id "
			+ "left join EmployeeType emt on edh.emp_type_id=emt.empTypeId "
			+ "left join Designation dg on edh.designation_id=dg.designation_id "
			+ "where edh.active=true And edh.new_join_status=0 And CONCAT(IfNull(edh.empcode,''),'',"
			+ "IfNull(edh.employee_name,''),'',IfNull(edh.preferred_name_for_email,''),' ',"
			+ "IfNull(edh.email,''),'',IfNull(sc.school_name_short,''),'',IfNull(d.dept_name_short,'')) LIKE %?1% ")
	public Page<Object> getAllNewJoineeDetailsDataKeyword(Pageable pageable, Object keyword);


		@Query(value = "select CONCAT(emp.employee_name,'-',emp.empcode) as employeeName,emp.emp_id as emp_id,ua.id as userDetail_id "
				+ "from employee_details emp Left join user_details ua on emp.email=ua.email "
				+ "where emp.emp_id IN ?1 and emp.active=true",nativeQuery=true)
		public  List<Map<String,Object>> getEmployeeData1(List<Integer> emp_ids);
		
		@Query(value = "select new map(ed.emp_id as emp_id, CONCAT(IFNULL(ed.employee_name,''),'-',IFNULL(ed.empcode,'')) ) from EmployeeDetails ed where ed.leave_approver1_emp_id=?1 OR ed.leave_approver2_emp_id=?1 and ed.active=true")
		public List<HashMap<String, Object>> fetchAllLeaveApplyDetailByApproverId(Integer emp_id); 

		@Modifying
		@Query(value = "update EmployeeDetails ed set ed.permanent_file=?2 where ed.emp_id=?1")
		public void updatePath(Integer emp_id, String t2); 
		
	@Query(value = "select new map(edh.emp_id as id,edh.job_id as job_id,edh.empcode as empcode,edh.email as email,"
			+ "edh.employee_name as employee_name,edh.phd_status as phd_status,b.bank_name as bank_name,edh.cancel_remark as cancel_remark,"
			+ "edh.date_of_joining as date_of_joining,edh.employee_name as employee_name,edh.active as active,"
			+ "edh.grosspay_ctc as grosspay_ctc,edh.ctc as ctc,edh.created_date as created_date,jt.job_short_name as job_short_name,"
			+ "sc.school_name_short as school_name_short,edh.school_id as school_id,sc.school_name as school_name,"
			+ "edh.emp_type_id as emp_type_id,emt.empType as empType,emt.empTypeShortName as empTypeShortName,"
			+ "edh.designation_id as designation_id,dg.designation_name as designation_name,edh.new_join_status as new_join_status,"
			+ "dg.designation_short_name as designation_short_name,d.dept_name_short as dept_name_short,o.offer_id as offer_id,"
			+ "o.ctc as offer_ctc,o.gross as gross,edh.salary_approve_status as salary_approve_status,jt.job_type as job_type,"
			+ "edh.created_by as created_by,edh.created_username as created_username,edh.master_code as master_code) "
			+ "from EmployeeDetails edh " + "left join Schools sc on edh.school_id=sc.school_id "
			+ "left join Offer o on edh.job_id=o.job_id " + "left join Department d on edh.dept_id=d.dept_id "
			+ "left join Bank b on edh.bank_id=b.bank_id " + "left join JobType jt on edh.job_type_id=jt.job_type_id "
			+ "left join EmployeeType emt on edh.emp_type_id=emt.empTypeId "
			+ "left join Designation dg on edh.designation_id=dg.designation_id "
			+ "where edh.active=true And edh.new_join_status=0 ")
	public Page<Object> getAllNewJoineeDetailsDataSortedData(Pageable pageable);

	@Query(value = "select new map(edh.emp_id as emp_id,edh.employee_name as employee_name,edh.empcode as empcode,"
			+ "edh.date_of_joining as date_of_joining,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "edh.dept_id as dept_id,edh.salary_structure_id as salary_structure_id,ss.salary_structure as salary_structure,"
			+ "edh.job_id as job_id,o.offer_id as offer_id,o.basic as basic,o.hra as hra,o.cca as cca,"
			+ "o.ta as ta,o.mr as mr,o.fr as fr,o.me as me,o.other_allow as other_allow,o.spl_1 as spl_1,o.pf as pf,o.pt as pt,"
			+ "o.esi as esi,o.pfc as pfc,o.da as da,o.job_type as job_type,o.ma as ma,o.gross as gross,o.esic as esic,"
			+ "o.management_pf as management_pf,o.epf as epf,o.ctc as ctc,dg.designation_id as designation_id,edh.cancel_remark as cancel_remark,"
			+ "dg.designation_name as designation_name,dg.designation_short_name as designation_short_name) "
			+ "from EmployeeDetails edh " + "left join Designation dg on dg.designation_id=edh.designation_id "
			+ "left join Department d on edh.dept_id=d.dept_id "
			+ "left join SalaryStructure ss on edh.salary_structure_id=ss.salary_structure_id "
			+ "left join Offer o on edh.job_id=o.job_id " + "where edh.emp_id=?1 And edh.active=true")
	public HashMap<String, Object> getNewJoineeDetailsByEmpID(Integer emp_id);

	@Query(value = "select e.shift_category_id from employee_details e where e.contract_empcode=:empCode and e.active=true ", nativeQuery = true)
	Integer getShiftId(@Param("empCode") String empCode);

	@Query(value = "select e.shift_category_id from employee_details e where e.empcode=:empCode and e.active=true ", nativeQuery = true)
	Integer getShiftIdByJavaEmpcode(@Param("empCode") String empCode);

	@Query(value = "select e.emp_id from employee_details e where e.contract_empcode=:empCode and e.active=true ", nativeQuery = true)
	public Integer getEmpIdByEmpCode(@Param("empCode") String empCode);

	@Query(value = "select e.dept_id from employee_details e where e.contract_empcode=:empCode and e.active=true ", nativeQuery = true)
	 Integer getDeptIdByEmpCode(@Param("empCode") String empCode);

	@Query(value = "select e.dept_id from employee_details e where e.empcode=:empCode and e.active=true ", nativeQuery = true)
	 Integer getDeptIdByJavaEmpCode(@Param("empCode") String empCode);

	@Query(value = "select e.employee_name from employee_details e where e.contract_empcode=:empCode and e.active=true ", nativeQuery = true)
	String getEmployeeNameByEmpCode(@Param("empCode") String empCode);

	@Query(value = "select e.employee_name from employee_details e where e.empcode=:empCode and e.active=true ", nativeQuery = true)
	String getEmployeeNameByJavaEmpCode(@Param("empCode") String empCode);

	@Query(value = "SELECT * FROM employee_details ud WHERE ud.date_of_joining = DATE_FORMAT(CURDATE(), '%d-%m-%Y') AND ud.active = TRUE And ud.emp_type_id !=3", nativeQuery = true)
	public List<EmployeeDetails> findAllEmails();
	// @Query(value = "select * from employee_details ud where
	// str_to_date(ud.date_of_joining,'%Y-%m-%d')=date(now()-INTERVAL 1 DAY) and
	// ud.active= true",nativeQuery=true)

	@Query(value = "SELECT ud.email FROM employee_details ud WHERE ud.dept_id=:deptId and ud.school_id=:school_id and ud.active=true", nativeQuery = true)
	List<String> findByDeptId(@Param("deptId") Integer dept_id, Integer school_id);

	@Query(value = " select e.emp_id from employee_details e where e.active=true  and (e.punched_card_status='Optional' or e.punched_card_status='Mandatory' or e.punched_card_status is null or e.punched_card_status='flexible' )  ", nativeQuery = true)
	List<Integer> getAllEmployeeId();

	@Query(value = "select ed.date_of_joining as date_of_joining from employee_details ed where ed.emp_id=:emp_id ", nativeQuery = true)
	public String getDateofJoining(@Param("emp_id") Integer empId);

	@Query(value = "select es.employee_sheet_id as employee_sheet_id,es.emp_id as emp_id,es.emp_code as empCode,"
			+ "ds.designation_name as designation,s.school_name as school_name,es.month as month,es.year as year,"
			+ "d.dept_name as dept_name,es.day1 as day1,es.day2 as day2,es.day3 as day3,es.day4 as day4,es.day5 as day5,"
			+ "es.day6 as day6,es.day7 as day7,es.day8 as day8,es.day9 as day9,es.day10 as day10,es.day11 as day11,"
			+ "es.day12 as day12,es.day13 as day13,es.day14 as day14,es.day15 as day15,es.day16 as day16,es.day17 as day17,"
			+ "es.day18 as day18,es.day19 as day19,es.day20 as day20,es.day21 as day21,es.day22 as day22,es.day23 as day23,"
			+ "es.day24 as day24,es.day25 as day25,es.day26 as day26,es.day27 as day27,es.day28 as day28,es.day29 as day29,"
			+ "es.day30 as day30, es.day31 as day31,es.paydays as paydays,es.presentdays as presentday,es.leavetaken as leaveTaken,"
			+ "es.absentdays as absentday,es.generalWO as generalWo,ed.employee_name as employee_name,ed.empcode as EmployeeCode,"
			+ "ed.date_of_joining as date_of_joining, es.working_days as working_days from employee_sheets es "
			+ "left join department d on d.dept_id=es.dept_id " + "left join schools s on s.school_id=es.school_id "
			+ "left join designation ds on ds.designation_id=es.designation_id "
			+ "left join employee_details ed on ed.emp_id=es.emp_id "
			+ "where es.emp_id IN (:emp_id) And es.month between month(:from_date) and month(:to_date) And "
			+ "es.year between year(:from_date) and year(:to_date)  ", nativeQuery = true)
	public List<Map<String, Object>> getAttendanceOfEmployeeByEmployeeId(Integer emp_id, Date from_date, Date to_date);

	@Query(value = "Select ed.leave_approver2_emp_id from EmployeeDetails ed where ed.emp_id=?1 and ed.active=true")
	public Integer getaApprover2_id(Integer emp_id);

	@Query(value = "select new com.au.dto.EmployeeDetailsForLeavePattern( e.emp_id as empId, e.emp_type_id as empTypeId, e.job_type_id as jobTypeId, e.school_id as schoolId, e.date_of_joining, e.permanent_status ,e.marriage_status,e.martial_status,e.gender,e.maternity_status,e.paternity_status) from EmployeeDetails e where e.emp_id=:empId and e.active=true ", nativeQuery = false)
	public EmployeeDetailsForLeavePattern getEmployeeDetailsForLeavePatternWithEmployeeId(Integer empId);

	@Query(value = "Select ed.leave_approver1_emp_id from EmployeeDetails ed where ed.emp_id=?1 and ed.active=true")
	public Integer getaApprover1_id(Integer emp_id);

	@Query(value = "select emp.emp_id from employee_details emp where emp.active=true", nativeQuery = true)
	public List<Integer> getAllEmployees1();

	@Query(value = "select new com.au.dto.EmployeeDetailsForLeavePattern( e.emp_id as empId, e.emp_type_id as empTypeId, e.job_type_id as jobTypeId, e.school_id as schoolId, e.date_of_joining, e.permanent_status,e.marriage_status, e.martial_status, e.gender,e.maternity_status,e.paternity_status) from EmployeeDetails e where e.active=true ", nativeQuery = false)
	public List<EmployeeDetailsForLeavePattern> getEmployeeDetailsForLeavePattern();

	
	@Query(value = "select ed.emp_id as emp_id,ed.empcode as empcode,ed.employee_name as employee_name,ed.email as email,"
			+ "d.dept_name as dept_name,d.dept_name_short as dept_name_short,ed.dept_id as dept_id,"
			+ "ed.emp_type_id As emp_type_id,et.emp_type_short_name As emp_type_short_name,et.emp_type As emp_type,"
			+ "dg.designation_id As designation_id,dg.designation_name As designation_name,dg.designation_short_name As designation_short_name "
			+ "from employee_details ed "
			+ "Left join department d on ed.dept_id=d.dept_id "
			+ "Left join designation dg on ed.designation_id=dg.designation_id "
			+ "Left join employee_type et on ed.emp_type_id=et.emp_type_id "
			+ "where ed.active=true ",nativeQuery=true)
	public List<Map<String, Object>> getAllActiveEmployeeDetails();
	
	
	
	@Query(value = "select emp.emp_id as emp_id,emp.empcode as empcode,emp.employee_name as employee_name,emp.email as email,ud.id as id,"
			+ "d.dept_id as dept_id,d.dept_name as dept_name,d.dept_name_short as dept_name_short,emp.leave_approver1_emp_id as leave_approver1_emp_id,"
			+ "udd.id as approvers_user_id,"
			+ "emp1.employee_name as leaveApproverName,dd.dept_id as LeaveApproverdept_id,dd.dept_name as LeaveApproverdept_name,dd.dept_name_short as LeaveApproverdept_name_short "
			+ "from employee_details emp "
			+ "Left join user_details ud on emp.email=ud.email "
			+ "Left join employee_details emp1 on emp1.emp_id=emp.leave_approver1_emp_id "
			+ "left join department d on emp.dept_id=d.dept_id "
			+ "left join department dd on emp1.dept_id=dd.dept_id "
			+ "Left join user_details udd on emp1.email=udd.email "
			+ "where emp.active=true ",nativeQuery=true)
	public List<Map<String, Object>> getAllActiveEmployeeDetailsWithUserId();

	@Query(value = "select emp.dept_id from employee_details emp where emp.email=?1 And emp.active=true", nativeQuery = true)
	public Integer getEmployeeDeptId(String email);
	
	@Query(value = "select emp.school_id from employee_details emp where emp.email=?1 And emp.active=true", nativeQuery = true)
	public Integer getEmployeeSchoolId(String email);

	@Query(value = "select edh.emp_id as emp_id,edh.job_id as job_id,edh.empcode as empcode,edh.employee_name as employee_name,edh.designation_id as designation_id,edh.dept_id as dept_id,edh.gender as gender,"
			+ "edh1.email as reporting_email,dg.designation_name as designation_name,d.dept_name as dept_name,d.dept_name_short as dept_name_short from employee_details edh "
			+ "left join designation dg on edh.designation_id=dg.designation_id "
			+ "left join department d on edh.dept_id=d.dept_id "
			+ "left join employee_details edh1 on edh1.emp_id=edh.report_id "
			+ "where edh.emp_id=?1", nativeQuery = true)
	public List<Map<String, Object>> getEmployeeDetailByEmpId(Integer emp_id);

	@Query(value = "select ed from EmployeeDetails ed where ed.emp_id=?1 And ed.active=true")
	public EmployeeDetails employeDetails(Integer emp_id);

	@Query(value = "Select ed.store_indent_approver1 as store_indent_approver1, ed.store_indent_approver2 as store_indent_approver2,"
			+ "ed.emp_id as emp_id,sir.cancel_status As cancel_status,"
			+ "concat(ed.employee_name,'-',DATE_FORMAT(sir.created_date, '%d/%m/20%y')) as requested_by_With_date,"
			+ "ed1.employee_name as StoreIndent_approver1_name,ed2.employee_name as StoreIndent_approver2_name,"
			+ "sir.store_indent_request_id as id,sir.indent_ticket as indent_ticket,sir.quantity as quantity,"
			+ "sir.stock_description as stock_description,sir.purpose as purpose,sir.requested_by as requested_by,sir.remarks as remarks,"
			+ "sir.requested_date as requested_date,sir.purchase_status as purchase_status,sir.approver1_id as approver1_id,"
			+ "sir.approver1_remarks as approver1_remarks,sir.approver1_date as approver1_date,sir.approver2_id as approver2_id,"
			+ "sir.approver2_remarks as approver2_remarks,sir.approver2_date as approver2_date,sir.approver1_status as approver1_status,"
			+ "sir.approver2_status as approver2_status,sir.approver1_active as approver1_active,sir.approver2_active as approver2_active,"
			+ "sir.item_id as item_id,sir.description as description,sir.others as others,sir.school_id as school_id,"
			+ "sir.dept_id as dept_id,sir.tag_id as tag_id,sir.draft_po_status as draft_po_status,sir.grn_date as grn_date,"
			+ "sir.expense_head_id as expense_head_id,sir.financial_year_id as financial_year_id,sir.purchase_request as purchase_request,"
			+ "sir.created_username as created_username,sir.modified_username as modified_username,sir.issued_status as issued_status,"
			+ "sir.created_date as created_date,sir.modified_date as modified_date,sir.created_by as created_by,sir.measure_id as measure_id,"
			+ "sir.env_item_id as env_item_id,sir.modified_by as modified_by,sir.active as active,"
			+ "sir.item_assignment_id as item_assignment_id,sir.ac_year_id as ac_year_id,sir.ledger_id as ledger_id "
			+ " from employee_details ed " + "inner join store_indent_request sir on ed.emp_id=sir.emp_id "
			+ " left join employee_details ed1 on ed.emp_id=ed1.store_indent_approver1 "
			+ " left join employee_details ed2 on ed.emp_id=ed2.store_indent_approver2 "
			+ "where (ed.store_indent_approver1=?1 or ed.store_indent_approver2=?1) and ed.active=true and sir.active=true "
			+ "And sir.issued_status='Pending' And sir.cancel_status = false GROUP BY sir.indent_ticket", nativeQuery = true)
	public List<Map<String, Object>> getItemApporoverData(Integer emp_id);

	@Query(value=" select e.emp_id as empId, e.empcode as empCode, e.employee_name as employeeName,"
			+ " s.school_name as schoolName, d.dept_name as department, dg.designation_name as designationName,e.date_of_joining as dateOfJoining,jt.job_type as jobType, e.email as email,s.school_name_short as schoolShortName  from employee_details e left join designation dg on e.designation_id=dg.designation_id "
			+ " left join department d on e.dept_id=d.dept_id"
			+ " left join schools s  on  s.school_id=e.school_id"
			+ " left join job_type jt on jt.job_type_id=e.job_type_id  where e.active=1 ",nativeQuery = true)
	public List<Map<String, Object>> getEmployeeDetailsForReporting();

	@Query(value=" select e.emp_id as empId, e.empcode as empCode, e.employee_name as employeeName,"
			+ " s.school_name as schoolName, d.dept_name as department, dg.designation_name as designationName,e.date_of_joining as dateOfJoining,jt.job_type as jobType, "
			+ " e.email as email,e.to_date as tillDate, e.current_location as address, s.school_name_short as schoolShortName, e.gender as gender, e.ctc as ctc, "
			+ "r.employee_name as reportingStaffName, CONCAT(s.school_name_short,'/',RIGHT(YEAR(CURDATE()),2),'/',et.emp_type_short_name,'/',e.empcode) as hrReferenceNo, "
			+ "et.emp_type_short_name as employeeType, e.emp_image_attachment_path as image, o.org_type,"
			+ "jp.street as street,jp.locality as locality,jp.pincode as pincode,"
			+ "jp.job_id as job_id,itver.interviewer_id as interviewer_id,itver.hr_id as hr_id, edh.emp_id as HRemp_id,edh.employee_name as HRemployeeName,"
			+ "dg1.designation_id as HRdesignation_id,d1.dept_id as HRdept_id,dg1.designation_name as HRdesignationName,d1.dept_name as HRdepartment " 
			+ " from employee_details e "
			+ " left join designation dg on e.designation_id=dg.designation_id "
			+ " left join department d on e.dept_id=d.dept_id"	
			+ " left join schools s  on  s.school_id=e.school_id"
			+ " left join job_type jt on jt.job_type_id=e.job_type_id "
			+ " left join job_profile jp on jp.job_id=e.job_id "
			+ " left join interviewer itver on itver.job_id=jp.job_id "
			+ " left join employee_details edh on itver.hr_id=edh.emp_id "
			+ " left join designation dg1 on edh.designation_id=dg1.designation_id "
			+ " left join department d1 on edh.dept_id=d1.dept_id	"
			+ " left join employee_type et on et.emp_type_id=e.emp_type_id "
			+ " left join employee_details r on r.emp_id=e.report_id"
			+ " left join organization o on o.org_id=s.org_id "
			+ "where e.emp_id=:empId and e.active=1 group by e.emp_id ",nativeQuery = true)
	public Map<String, Object> getEmployeeDetailsForReportingById(Integer empId);

	@Query(value=" select e.emp_id as empId, e.empcode as empCode, e.employee_name as employeeName,"
			+ " s.school_name as schoolName, d.dept_name as department, dg.designation_name as designationName,"
			+ " e.date_of_joining as dateOfJoining,jt.job_type as jobType, e.email as email, c.user_id as empUserId "
			+ " from class_feedback_answers c "
			+ "	left join employee_details e on e.emp_id=c.user_id "
			+ " left join designation dg on e.designation_id=dg.designation_id "
			+ " left join department d on e.dept_id=d.dept_id"
			+ " left join schools s  on  s.school_id=e.school_id"
			+ " left join job_type jt on jt.job_type_id=e.job_type_id "
			+ "where e.active=1 group by c.user_id ",nativeQuery = true)
	public List<Map<String, Object>> getEmployeeDetailsForFeedbackReporting();

	
	@Query(value = "select ed.emp_id as emp_id,ed.empcode as empcode,"
			+ "Concat(IfNull(ed.employee_name,''),'-',IfNull(ed.empcode,'')) as EmpolyeeName "
			+ "from employee_details ed " + "where ed.active=true", nativeQuery = true)
	public List<Map<String, Object>> getEmployeeNameConcateWithEmployeeCode();

	
	@Query(value = "select count(*) from EmployeeDetails where email=?1")
	public Integer checkEmailPresentOrNot(String email);
	
	@Query(value = "select employee_name from EmployeeDetails ed where ed.email=?1 And ed.active=true")
	public String getEmpNameByEmail(String email);


	

	@Query(value = "select e.punched_card_status from employee_details e where e.contract_empcode=:empCode and e.active=true ", nativeQuery = true)
	public String getPunchCardStatusByEmpCode(@Param("empCode") String empCode);

	@Query(value = "select e.punched_card_status from employee_details e where e.empcode=:empCode and e.active=true ", nativeQuery = true)
	public String getPunchCardStatusByJavaEmpCode(@Param("empCode") String empCode);
	
	@Query(value = "select e.empcode from employee_details e where e.contract_empcode=:empCode and e.active=true ", nativeQuery = true)
	public String getEmpCodeByContractEmpCode(@Param("empCode") String empCode);
	
	@Query(value = " select e.emp_id from employee_details e where e.active=true and e.punched_card_status='No Swipe' ", nativeQuery = true)
	List<Integer> getAllNoSwipeEmployeeId();

	@Query(value = "select e.dept_id from employee_details e where e.emp_id=:empId ", nativeQuery = true)
	public Integer getDeptIdByEmpId(@Param("empId") Integer empId);

	@Query(value = "select e.job_type_id from employee_details e where e.emp_id=:empId ", nativeQuery = true)
	public Integer getJobTypeIdByEmpId(Integer empId);

	@Query(value = "select ed.emp_id from employee_details ed "
			+ "left join user_details ua on ua.email=ed.email "
			+ "where ua.id=?1 And ua.active=true And ed.active=true", nativeQuery = true)
	public Integer getEmployeeId(Integer userId);
	
	@Query(value = "select ed.emp_id As emp_id,ed.dept_id As dept_id,ed.school_id As school_id,ua1.id as proctorHeadId,ed.report_id As report_id,s.school_name_short As school_name_short "
			+ "from employee_details ed "
			+ "left join user_details ua on ua.email=ed.email "
			+ "left join schools s on s.school_id=ed.school_id "
			+ "left join proctor_head ph on ph.chief_proctor_id=ed.chief_proctor_id "
			+ "left join employee_details ed1 on ed1.emp_id=ph.emp_id "
			+ "left join user_details ua1 on ua1.email=ed1.email "
			+ "where ua.id=?1 And ua.active=true And ed.active=true", nativeQuery = true)
	public Map<String, Object> getDeptIdAndSchoolIdBasedOnUser(Integer user_id);
	
	
	@Query(value = "select new map(edh.emp_id as id,edh.empcode as empcode,edh.employee_name as employee_name,edh.gender as gender,"
			+ "edh.designation_id as designation_id,edh.emp_type_id as emp_type_id,edh.job_type_id  as job_type_id,edh.dept_id as dept_id,edh.school_id as school_id,"
			+ "sc.school_name_short as school_name_short,d.dept_name_short as dept_name_short,edh.email as email,edh.date_of_joining as date_of_joining,"
			+ "emt.empTypeShortName as empTypeShortName,jt.job_type as job_type,dg.designation_short_name as designation_short_name,"
			+ "lan.employee_name as leaveApproverName1,lan2.employee_name as leaveApproverName2,sia.employee_name as storeIndentApproverName, o.offer_id as offer_id ) "
			+ "from EmployeeDetails edh "
			+ "left join Schools sc on edh.school_id=sc.school_id "
			+ "left join Department d on edh.dept_id=d.dept_id "
			+ "left join JobType jt on edh.job_type_id=jt.job_type_id "
			+ "left join EmployeeType emt on edh.emp_type_id=emt.empTypeId "
			+ "left join UserAuthentication ua on ua.email=edh.email "
			+ "left join Designation dg on edh.designation_id=dg.designation_id "
			+ "left join EmployeeDetails lan on lan.emp_id=edh.leave_approver1_emp_id "
			+ "left join EmployeeDetails lan2 on lan2.emp_id=edh.leave_approver2_emp_id "
			+ "left join EmployeeDetails sia on sia.emp_id=edh.store_indent_approver1 "
			+ "left join Offer o on o.job_id=edh.job_id "
			+ "where CONCAT(IfNull(edh.empcode,''),'',IfNull(edh.employee_name,''),'',IfNull(edh.preferred_name_for_email,''),' ',IfNull(edh.email,''),'',"
			+ "IfNull(sc.school_name_short,''),'',IfNull(d.dept_name_short,'')) LIKE %?1% and (edh.leave_approver1_emp_id =?1 or edh.leave_approver2_emp_id =?1) And edh.active=true group by edh.emp_id")
	public Page<Object> getAllDataFilteredByKeywordUserId(Pageable pageable, Object keyword, Integer empId);

	
	@Query(value = "select new map(edh.emp_id as id,edh.empcode as empcode,edh.employee_name as employee_name,edh.gender as gender,"
			+ "edh.designation_id as designation_id,edh.emp_type_id as emp_type_id,edh.job_type_id  as job_type_id,edh.dept_id as dept_id,edh.school_id as school_id,"
			+ "sc.school_name_short as school_name_short,d.dept_name_short as dept_name_short,edh.email as email,edh.date_of_joining as date_of_joining,"
			+ "emt.empTypeShortName as empTypeShortName,jt.job_type as job_type,dg.designation_short_name as designation_short_name,"
			+ "lan.employee_name as leaveApproverName1,lan2.employee_name as leaveApproverName2,sia.employee_name as storeIndentApproverName, o.offer_id as offer_id ) " +
			"from EmployeeDetails edh "
			+ "left join Schools sc on edh.school_id=sc.school_id "
			+ "left join Department d on edh.dept_id=d.dept_id "
			+ "left join JobType jt on edh.job_type_id=jt.job_type_id "
			+ "left join EmployeeType emt on edh.emp_type_id=emt.empTypeId "
			+ "left join UserAuthentication ua on ua.email=edh.email "
			+ "left join Designation dg on edh.designation_id=dg.designation_id "
			+ "left join EmployeeDetails lan on lan.emp_id=edh.leave_approver1_emp_id "
			+ "left join EmployeeDetails lan2 on lan2.emp_id=edh.leave_approver2_emp_id "
			+ "left join EmployeeDetails sia on sia.emp_id=edh.store_indent_approver1 "
			+ "left join Offer o on o.job_id=edh.job_id "
			+ "where(edh.leave_approver1_emp_id =?1 or edh.leave_approver2_emp_id =?1) And edh.active=true group by edh.emp_id")
	public Page<Object> getAllSortedDataUserId(Pageable pageable, Integer empId);


	@Query(value = "select lan.email from employee_details ed "
			+ "left join employee_details lan on lan.emp_id=ed.leave_approver1_emp_id "
			+ "where ed.emp_id=?1 ", nativeQuery = true)
	public String getHodEmailData(Integer emp_id);

	@Query(value = " select e.emp_id from employee_details e where e.active=true  and (e.punched_card_status='Optional' or e.punched_card_status='Mandatory' or e.punched_card_status is null or e.punched_card_status='flexible' )  and e.emp_id=:empId ", nativeQuery = true)
	List<Integer> getAllEmployeeIdByEmpId(Integer empId);

	@Query(value = " select e.emp_id from employee_details e where e.active=true  and (e.punched_card_status='Optional' or e.punched_card_status='Mandatory' or e.punched_card_status is null or e.punched_card_status='flexible' )  and e.school_id=:schoolId ", nativeQuery = true)
	List<Integer> getAllEmployeeIdBySchoolId(Integer schoolId);

	@Query(value = " select e.emp_id from employee_details e where e.active=true and e.punched_card_status='No Swipe'  and e.emp_id=:empId", nativeQuery = true)
	List<Integer> getAllNoSwipeEmployeeIdByEmpId(Integer empId);
	
	@Query(value = " select e.emp_id from employee_details e where e.active=true and e.punched_card_status='No Swipe' and e.school_id=:schoolId ", nativeQuery = true)
	List<Integer> getAllNoSwipeEmployeeIdBySchoolId(Integer schoolId);
	
	@Query(value = " select e.emp_id from employee_details e where e.active=true ", nativeQuery = true)
	List<Integer> getAllActiveEmployeeId();
	
	@Query(value = "select new map(ss.salary_structure as salary_structure,edh.dept_id as dept_id,edh.salary_structure_id as salary_structure_id,"
			+ "edh.job_type_id as job_type_id,d.dept_name as dept_name,jt.job_type as job_type,edh.job_id as job_id,edh.ctc as ctc,edh.annual_salary as annual_salary,"
			+ "edh.net_pay as net_pay,edh.school_id as school_id,s.school_name as school_name,dg.designation_name as designation,edh.active as active,edh.date_of_joining as date_of_joining,"
			+ "edh.annual_salary as basic,edh.hra as hra,edh.cca as cca,edh.spl_1 as spl_1,edh.mfo as pf,edh.transport_assign_month as esi,edh.da as da,edh.remarks as remarks,"
			+ "edh.grosspay_ctc as gross,edh.transport_deassign_month as esic,edh.pinfl as management_pf,et.empType as employee_type,edh.email as email,edh.pf_status as isPf,edh.pt_status as isPt,"
			+ "edh.consolidated_amount as consolidated_amount,edh.from_date as from_date,edh.to_date as to_date,"
			+ "edh.employee_name as firstname,edh.plastic_card as pt,edh.report_id as report_id,edh.created_by as created_by,edh.created_date as created_date,"
			+ "ed.exp_in_years as exp_in_years,ed.exp_in_months as exp_in_months,edh.ta as ta,edh.mr as mr,edh.fr as fr,edh.me as me,"
			+ "s.school_name_short as school_name_short,edh.designation_id as designation_id,edh.other_allow as other_allow,"
			+ "edh.emp_type_id as emp_type_id,jp.current_location as current_location,jp.mobile as mobile) "
			+ "from EmployeeDetails edh "
			+ "left join Department d on edh.dept_id=d.dept_id "
			+ "left join SalaryStructure ss on edh.salary_structure_id=ss.salary_structure_id "
			+ "left join JobType jt on edh.job_type_id=jt.job_type_id "
			+ "left join Designation dg on edh.designation_id=dg.designation_id "
			+ "left join Schools s on edh.school_id=s.school_id "
			+ "left join EmployeeType et on edh.emp_type_id=et.empTypeId "
			+ "left join JobProfile jp on edh.job_id=jp.job_id "
			+ "left join ExperienceDetails ed on edh.job_id=ed.job_id where edh.emp_id=?1" )
	public List<HashMap<String, Object>> fetchAllOfferDetailsByEmployeeId(Integer employeeId);

	@Query(value = "select CONCAT(IFNULL(edh.employee_name,''),'-',IFNULL(edh.empcode,''),'-',(select d.dept_name_short from department d where "
			+ "edh.dept_id=d.dept_id and d.active=true)) as employeeName,edh.emp_id as emp_id from employee_details edh where "
			+ "edh.emp_id NOT IN(select DISTINCT tte.emp_id from time_table_employee tte where tte.selected_date between ?1 and ?2 "
			+ "and tte.time_slots_id=?3) and edh.active=true", nativeQuery = true)
	public List<Map<String, Object>> getAllEmployeesForTimeTable(Date date1, Date date2, Integer time_slots_id);

@Query(value = "select CONCAT(IFNULL(edh.employee_name,''),'-',IFNULL(edh.empcode,''),'-',(select d.dept_name_short from department d where edh.dept_id=d.dept_id and d.active=true)) as employeeName, "
			+ "edh.emp_id as emp_id, ud.id as id "
			+ "from user_details ud "
			+ "inner join employee_details edh on ud.email=edh.email and edh.active=true "
			+ "inner join subject_assignment sa on sa.user_id = ud.id and sa.active =true "
			+ "inner join course_assignment ca on ca.course_assignment_id = sa.course_assignment_id and ca.active =true and ca.year_sem =?2 "
			+ "inner join program_specialization ps on ps.program_specialization_id=ca.program_specialization_id and ps.program_specialization_id =?1 "
			+ "group by edh.emp_id", nativeQuery = true)
	public List<Map<String, Object>> getEmployeesForSectionTimeTable(Integer program_specialization_id,Integer year_sem);

	@Query(value = "SELECT " +
    "    ed.emp_id AS emp_id, " +
    "    CONCAT( " +
    "        IFNULL(ed.employee_name, ''), '-', " +
    "        IFNULL(ed.empcode, ''), '-', " +
    "        (SELECT d.dept_name_short " +
    "         FROM department d " +
    "         WHERE ed.dept_id = d.dept_id AND d.active = TRUE) " +
    "    ) AS employeeName " +
    "FROM batch_program_assignment bpa " +
    "INNER JOIN course_assignment ca ON bpa.program_specialization_id = ca.program_specialization_id " +
    "INNER JOIN subject_assignment sa ON sa.course_assignment_id = ca.course_assignment_id " +
    "INNER JOIN user_details ud ON ud.id = sa.user_id " +
    "INNER JOIN employee_details ed ON ed.email = ud.email " +
    "WHERE bpa.batch_assignment_id = ?1 " +
    "GROUP BY ed.emp_id", nativeQuery = true)
	public List<Map<String, Object>> getEmployeesForBatchTimeTable(Integer batch_assignment_id);

	@Query(value = "select emp_id from EmployeeDetails ed where ed.email=?1 and ed.active=true")
	public Integer getEmpId1(String email);

	@Query(value = "select gender from employee_details ed where ed.emp_id=?1", nativeQuery = true)
	public Character getGender(Integer emp_id);
	
	@Query(value = "select ed.emp_id, ed.employee_name, ed.empcode, ed.school_id, dept.dept_name_short, dept.dept_name, ed.job_type_id, ed.martial_status, ed.gender FROM employee_details ed "
			+ "left join department dept on dept.dept_id=ed.dept_id where ed.active=true ", nativeQuery = true)
	public List<Map<String, Object>> getAllEmployeeData();

	@Query(value = "SELECT ed.emp_id, ed.employee_name, ed.empcode, ed.school_id, dept.dept_name_short, dept.dept_name, ed.job_type_id, ed.martial_status, ed.gender FROM employee_details ed "
			+ "left join department dept on dept.dept_id=ed.dept_id "
			+ "where ed.active=true and (ed.leave_approver1_emp_id=?1 or ed.leave_approver2_emp_id=?1)", nativeQuery = true)
	public List<Map<String, Object>> getDataByLeaveApproversId(Integer empId);

	
	@Query(value = " SELECT NEW map(edh.leave_approver1_emp_id As leave_approver1_emp_id ) FROM EmployeeDetails edh "
			+ "left join UserAuthentication ua on edh.email=ua.email "
			+ " where edh.leave_approver1_emp_id=?1 ")
	public List<Map<String, Object>> getEmpLeaveApproverBasedOnUserId(Integer leave_approver1_emp_id);

	@Query(value = "select ed.permanent_status from EmployeeDetails ed where ed.emp_id=?1 and ed.active=true")
	public Integer checkForPermanentStatus(Integer emp_id);

		@Query(value = " select new com.au.dto.EmployeeDetailsByEmail( e.emp_id as empId, CONCAT(e.empcode, ' - ', e.employee_name) as employeeName, e.empcode as empCode) from EmployeeDetails e where e.email=:email and e.active=true ", nativeQuery = false)
	public EmployeeDetailsByEmail getEmployeeByEmail(String email);

		@Query(value = " SELECT edh.emp_id As applicant_id,edh.report_id As emp_id,"
				+ "ed1.employee_name As hoiName FROM employee_details edh "
				+ "left join employee_details ed1 on edh.report_id=ed1.emp_id "
				+ " where edh.emp_id=?1 And edh.active=true" , nativeQuery = true)
		public Map<String, Object> getEmployeeData2(Integer emp_id);
		
	
	@Query(value = " SELECT edh.leave_approver1_emp_id As emp_id,edh.emp_id As applicant_id,"
			+ "ed2.employee_name As hodName FROM employee_details edh "
			+ "left join employee_details ed2 on edh.leave_approver1_emp_id=ed2.emp_id "
			+ " where edh.emp_id=?1 And edh.active=true" , nativeQuery = true)
	public Map<String, Object> getEmployeeData3(Integer emp_id);

	@Query(value = " SELECT edh.emp_id As emp_id,edh.employee_name As employee_name FROM employee_details edh "
			+ " where (edh.leave_approver1_emp_id=?1 or edh.report_id=?1) And edh.active=true" , nativeQuery = true)
	public List<Map<String, Object>> getEmpDetailsBasedOnApprover(Integer emp_id);

	@Modifying
	@Query(value = "update EmployeeDetails ed set ed.ta=:ta, ed.hra=:hra, ed.da=:da where ed.emp_id=:empId ")
	public void updateDetailsForMasterSalary(float ta, float hra, float da, Integer empId);
	
	@Query(value = " SELECT edh.emp_id As emp_id,edh.employee_name As employee_name,edh.school_id As school_id,sc.school_name As school_name,sc.ref_no As ref_no,"
			+ "sc.school_name_short As school_name_short,sc.display_name As display_name FROM employee_details edh "
			+ "left join schools sc on edh.school_id=sc.school_id "
			+ " where edh.emp_id=?1  And edh.active=true" , nativeQuery = true)
	public Map<String, Object> getSchoolDetailsBasedOnEmpId(Integer emp_id);
	
	
	@Query(value = " SELECT edh.emp_id As emp_id,edh.employee_name As employee_name,d.dept_id As dept_id,d.dept_name As dept_name,"
			+ "d.dept_name_short As dept_name_short,de.designation_id As designation_id,de.designation_name As designation_name," 
			+ "sc.school_id As school_id,sc.school_name_short As school_name_short,sc.school_name As school_name,"
			+ "de.designation_short_name As designation_short_name,de.priority As priority,edh.current_location As current_location,"
			+ "edh.hometown As hometown FROM employee_details edh "
			+ "left join department d on edh.dept_id=d.dept_id "
			+ "left join schools sc on edh.school_id=sc.school_id "
			+ "left join designation de on de.designation_id = edh.designation_id "
			+ " WHERE edh.emp_id IN ?1 AND edh.active = true ORDER BY FIELD(edh.emp_id, ?1)" , nativeQuery = true)
	public List<Map<String, Object>> getDeptAndDesignationBasedOnEmpId(List<Integer> empIds);

	
	@Query(value = "select new map(ed.emp_id as emp_id,ed.employee_name as employee_name,ed.empcode as empcode,"
			+ "ed.school_id As school_id,sc.school_name As school_name,sc.school_name_short As school_name_short,sc.display_name As display_name,"
			+ "d.dept_id As dept_id,d.dept_name As dept_name,d.dept_name_short As dept_name_short,ed.date_of_joining as date_of_joining) from EmployeeDetails ed "
			+ "left join Schools sc on ed.school_id=sc.school_id "
			+ "left join Department d on ed.dept_id=d.dept_id "
			+ "where ed.email=(select ua.email from UserAuthentication ua where ua.id=?1) And ed.active=true")
	public Map<String, Object> getEmployeeDetailsBasedOnUserID(Integer user_id);

	@Query(value = " select count(ed.designation_id) As counts,d.designation_id As designation_id,d.designation_name As designation_name,d.priority As priority,"
			  + "COUNT(CASE WHEN ed.gender = 'M' THEN 1 END) AS male_count, "
	            + "COUNT(CASE WHEN ed.gender = 'F' THEN 1 END) AS female_count, "
			+ "ed.leave_approver1_emp_id As leave_approver1_emp_id,ed.emp_id As emp_id FROM employee_details ed "
			+ "left join designation d on d.designation_id = ed.designation_id "
			+ " where ed.leave_approver1_emp_id =?1 And ed.active=true group by ed.designation_id ", nativeQuery = true)
	public List<Map<String, Object>> getcountOfDesignationBasedOnHod(Integer leave_approver1_emp_id);

	@Query(value = "select ed.emp_id As emp_id, ed.employee_name As employee_name, ed.empcode As empcode,ed.designation_id As designation_id,ed.gender As gender,"
			+ "d.designation_name As designation_name,d.designation_short_name As designation_short_name "
			+ " FROM employee_details ed "
			+ "left join designation d on d.designation_id = ed.designation_id "
			+ "where ed.leave_approver1_emp_id=?1 And ed.designation_id=?2 And ed.active=true ", nativeQuery = true)	
	public List<Map<String, Object>> getEmployeeDetailsDataBasedOnEmpId(Integer leave_approver1_emp_id, Integer designation_id);

	
	
	@Query(value = "Select  rs.current_sem AS semester,rs.current_year AS years,COUNT(DISTINCT rs.student_id) AS reportingStudent,"
			+ "COUNT(DISTINCT CASE WHEN std.candidate_sex = 'Male' THEN std.student_id END) AS maleStudentCount, "
			+ "COUNT(DISTINCT CASE WHEN std.candidate_sex = 'Female' THEN std.student_id END) AS femaleStudentCount "
			+ "From employee_details ed "
			+ "left JOIN program_specialization ps ON ed.dept_id = ps.dept_id "
			+ "left JOIN student_details std ON std.program_specialization_id = ps.program_specialization_id "
			+ "left JOIN reporting_students rs ON std.student_id = rs.student_id "
			+ "WHERE ed.emp_id =?1 And ed.active=true And std.active=true "
			+ "GROUP BY CASE  WHEN rs.current_sem = 0 THEN rs.current_year  ELSE rs.current_sem  END " , nativeQuery = true)
	public List<Map<String, Object>> getHodStudentCount(Integer leave_approver1_emp_id);
	

	@Query(value = "Select COUNT(distinct(std.student_id)) as studentCount,"
			  + " COUNT(DISTINCT CASE WHEN std.candidate_sex = 'Male' THEN std.student_id END) AS maleStudentCount, "
		       + "COUNT(DISTINCT CASE WHEN std.candidate_sex = 'Female' THEN std.student_id END) AS femaleStudentCount "
			+ "From employee_details ed "
			+ "left JOIN program_specialization ps ON ed.dept_id = ps.dept_id "
			+ "left JOIN student_details std ON std.program_specialization_id = ps.program_specialization_id "
			+ "Where ed.emp_id=?1 And ed.active=true And std.active=true" , nativeQuery = true)
	public Map<String, Object> getHodStudentCount1(Integer leave_approver1_emp_id);

	@Query(value = "Select COUNT(distinct(ed.emp_id)) as employeeCount, "
			 + "COUNT(CASE WHEN ed.gender = 'M' THEN 1 END) AS maleEmployeeCount, "
	            + "COUNT(CASE WHEN ed.gender = 'F' THEN 1 END) AS femaleEmployeeCount "
			+ "From employee_details ed Where ed.leave_approver1_emp_id=?1 And ed.active=true " , nativeQuery = true)
	public Map<String, Object> getHodEmployeeCount1(Integer leave_approver1_emp_id);
	
	
	@Query(value = "select ed.emp_id As emp_id, ed.employee_name As employee_name, ed.empcode As empcode,ed.designation_id As designation_id,ed.gender As gender,"
			+ "d.designation_name As designation_name,d.designation_short_name As designation_short_name "
			+ " FROM employee_details ed "
			+ "left join designation d on d.designation_id = ed.designation_id "
			+ "where ed.school_id=?1 And ed.designation_id=?2 And ed.active=true ", nativeQuery = true)	
	public List<Map<String, Object>> getEmployeeDetailsDataBasedOnReportId(Integer schoolId, Integer designation_id);
	
	
	@Query(value = "Select COUNT(distinct(std.student_id)) as studentCount,"
			  + " COUNT(DISTINCT CASE WHEN std.candidate_sex = 'Male' THEN std.student_id END) AS maleStudentCount, "
		       + "COUNT(DISTINCT CASE WHEN std.candidate_sex = 'Female' THEN std.student_id END) AS femaleStudentCount "
			+ "From employee_details ed "
			+ "left JOIN student_details std ON std.school_id = ed.school_id "
			+ "Where ed.school_id=?1 And ed.active=true And std.active=true" , nativeQuery = true)
	public Map<String, Object> getHodStudentCount1Hoi(Integer schoolId);
	
	@Query(value = "Select COUNT(distinct(ed.emp_id)) as employeeCount, "
			 + "COUNT(CASE WHEN ed.gender = 'M' THEN 1 END) AS maleEmployeeCount, "
	            + "COUNT(CASE WHEN ed.gender = 'F' THEN 1 END) AS femaleEmployeeCount "
			+ "From employee_details ed Where ed.school_id=?1 And ed.active=true " , nativeQuery = true)
	public Map<String, Object> getHodEmployeeCount1Hoi(Integer schoolId);
	
	
	@Query(value = "select rs.current_year,rs.current_sem,count(*) as totalStudentCount,sum(case when candidate_sex='Male' then 1 else 0 end ) as maleStudentCount, "
			+ "sum(case when candidate_sex='Female' then 1 else 0 end ) as femaleStudentCount from student_details s  "
			+ "inner join reporting_students rs on s.student_id=rs.student_id "
			+ "where s.active=true and s.auid is not null and rs.active=true  "
			+ "and s.school_id=?1 "
			+ "GROUP BY CASE  WHEN rs.current_sem = 0 THEN rs.current_year  ELSE rs.current_sem  END " , nativeQuery = true)
	public List<Map<String, Object>> getHoiStudentCount(Integer schoolId);

	@Query(value = "select et.emp_type as emp_type_short_name,ed.emp_type_id As emp_type_id,count(et.emp_type) as employee_type_count, "
			 + "COUNT(CASE WHEN ed.gender = 'M' THEN 1 END) AS maleEmployeeCount, "
	            + "COUNT(CASE WHEN ed.gender = 'F' THEN 1 END) AS femaleEmployeeCount "
			+ "From employee_details ed "
			+ "inner join employee_type et on ed.emp_type_id=et.emp_type_id "
			+ "Where ed.schoolId =?1 And ed.active=true group by et.emp_type_short_name " , nativeQuery = true)
	public List<Map<String, Object>> getCountOfJobTypeBasedOnHoi(Integer schoolId);

	
	@Query(value = "select count(ed.gender) as gender_count,"
			 + "COUNT(CASE WHEN ed.gender = 'M' THEN 1 END) AS maleEmployeeCount, "
	            + "COUNT(CASE WHEN ed.gender = 'F' THEN 1 END) AS femaleEmployeeCount "
			+ "From employee_details ed "
			+ "Where ed.school_id =?1 And ed.active=true " , nativeQuery = true)
	public List<Map<String, Object>> getCountOfGenderBasedOnHoi(Integer schoolId);

	@Query(value = "select dg.designation_name as designation_name,dg.designation_id As designation_id,dg.priority As priority,ed.school_id As school,"
			+ "count(dg.designation_name) as designation_count,"
			+ "COUNT(CASE WHEN ed.gender = 'M' THEN 1 END) AS male_count, "
			+ "COUNT(CASE WHEN ed.gender = 'F' THEN 1 END) AS female_count "
			+ "from employee_details ed "
			+ "inner join designation dg on ed.designation_id=dg.designation_id "
			+ "Where ed.school_id =?1 and ed.active=true group by dg.designation_short_name " , nativeQuery = true)
	public List<Map<String, Object>> getCountOfDesignationBasedOnHoi(Integer schoolId);

	@Query(value = "select de.dept_name_short as dept_name_short,"
			+ "count(de.dept_name_short) as department_count,"
			+ "COUNT(CASE WHEN edh.gender = 'M' THEN 1 END) AS male_count, "
			+ "COUNT(CASE WHEN edh.gender = 'F' THEN 1 END) AS female_count "
			+ "from employee_details edh "
			+ "inner join department de on edh.dept_id=de.dept_id "
			+ "Where edh.school_id =?1 and edh.active=true group by de.dept_name_short " , nativeQuery = true)
	public List<Map<String, Object>> getCountOfDeptBasedOnHoi(Integer schoolId);


	@Query(value = "SELECT edh.report_id AS report_id,"
			+ "       CASE "
			+ "           WHEN (YEAR(CURDATE()) - YEAR(edh.dateofbirth)) BETWEEN 20 AND 29 THEN '20-29'"
			+ "           WHEN (YEAR(CURDATE()) - YEAR(edh.dateofbirth)) BETWEEN 30 AND 39 THEN '30-39'"
			+ "           WHEN (YEAR(CURDATE()) - YEAR(edh.dateofbirth)) BETWEEN 40 AND 49 THEN '40-49'"
			+ "           WHEN (YEAR(CURDATE()) - YEAR(edh.dateofbirth)) BETWEEN 50 AND 59 THEN '50-59'"
			+ "           WHEN (YEAR(CURDATE()) - YEAR(edh.dateofbirth)) BETWEEN 60 AND 69 THEN '60-69'"
			+ "           WHEN (YEAR(CURDATE()) - YEAR(edh.dateofbirth)) >= 70 THEN '70+'"
			+ "       END AS employee_age_gap,"
			+ "       COUNT(*) AS age_count,"
			+ "       COUNT(CASE WHEN edh.gender = 'M' THEN 1 END) AS male_count,"
			+ "       COUNT(CASE WHEN edh.gender = 'F' THEN 1 END) AS female_count "
			+ "FROM  employee_details edh "
			+ "WHERE edh.school_id = ?1 AND edh.active = true GROUP BY employee_age_gap", nativeQuery = true)
	public List<Map<String, Object>> getCountOfAgeGroupBasedOnHoi(Integer schoolId);

	
	@Query(value = "select edh.report_id As report_id,month(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y')) As monthNo, "
			+ "DATE_FORMAT(STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y'), '%M') AS monthName, "
			+ "count(month(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y'))) as date_of_joining_count , "
			+ " COUNT(CASE WHEN edh.gender = 'M' THEN 1 END) AS male_count, "
			+ "COUNT(CASE WHEN edh.gender = 'F' THEN 1 END) AS female_count "
			+ "from  employee_details edh  "
			+ "Where edh.school_id =?1 and edh.active=true group by month(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y'))", nativeQuery = true)
	public List<Map<String, Object>> getCountOfDateOfJoiningMonthWiseBasedOnHoi(Integer schoolId);

	@Query(value = "select ed.email from EmployeeDetails ed where ed.emp_id=?1 and ed.active=true")
	public String getApproveEmail(Integer emp_id);


	@Query("SELECT ed.employee_name, ed.mobile from EmployeeDetails ed WHERE ed.active = true AND ed.email = :email")
	Optional<Tuple> findMobileByActiveTrueAndEmail(@Param("email") String email);

	@Query(value = "SELECT ed.school_id from employee_details ed WHERE ed.active = true AND ed.report_id=?1 group by ed.school_id", nativeQuery = true)
	public Integer getSchoolIdBasedOnHoi(Integer report_id);

	
	@Query(value = "select edh.emp_id as emp_id,edh.job_id as job_id,edh.empcode as proctorCode,edh.email as proctorEmail, "
			+ "edh.employee_name as proctorName,edh.active as active,edh.mobile As mobile,d.dept_id As dept_id, "
			+ "ph.emp_id As proctorHeadId,ed.employee_name As proctorHeadName,ed.empcode as proctorHeadCode, "
			+ "sc.school_name_short as school_name_short,edh.school_id as school_id,sc.school_name as school_name,ud.id As userId, "
			+ "edh.designation_id as designation_id,dg.designation_name as designation_name,edh.chief_proctor_id As chief_proctor_id, "
			+ "dg.designation_short_name as designation_short_name,d.dept_name_short as dept_name_short,edh.master_code as master_code, "
			+ "(select count(*) from  proctor_student_assignment where emp_id = edh.emp_id) As studentCount "
			+ "from  employee_details edh   "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join schools sc on edh.school_id=sc.school_id  "
			+ "left join department d on edh.dept_id=d.dept_id  "
			+ "left join proctor_head ph on edh.chief_proctor_id=ph.chief_proctor_id "
			+ "left join employee_details ed on ed.emp_id=ph.emp_id "
			+ "left join designation dg on edh.designation_id=dg.designation_id  "
			+ "where edh.active=true And edh.chief_proctor_id is not null group by edh.chief_proctor_id , edh.emp_id ", nativeQuery = true)
	public List<Map<String, Object>> getEmployeeDetailsBasedOnProctor();
	
	
	@Query(value = "select ed from EmployeeDetails ed where ed.emp_id in (?1) And ed.active=true")
	public List<EmployeeDetails> findAll1(List<Integer> empId);

	
	@Query(value = "select edh.emp_id as emp_id,edh.job_id as job_id,edh.empcode as proctorCode,edh.email as proctorEmail, "
			+ "edh.employee_name as proctorName,edh.active as active,edh.mobile As mobile,d.dept_id As dept_id, "
			+ "ph.emp_id As proctorHeadId,ed.employee_name As proctorHeadName,ed.empcode as proctorHeadCode, "
			+ "sc.school_name_short as school_name_short,edh.school_id as school_id,sc.school_name as school_name,ud.id As userId, "
			+ "edh.designation_id as designation_id,dg.designation_name as designation_name,edh.chief_proctor_id As chief_proctor_id, "
			+ "dg.designation_short_name as designation_short_name,d.dept_name_short as dept_name_short,edh.master_code as master_code, "
			+ "(select count(*) from  proctor_student_assignment where emp_id = edh.emp_id) As studentCount "
			+ "from  employee_details edh   "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join schools sc on edh.school_id=sc.school_id  "
			+ "left join department d on edh.dept_id=d.dept_id  "
			+ "left join proctor_head ph on edh.chief_proctor_id=ph.chief_proctor_id "
			+ "left join employee_details ed on ed.emp_id=ph.emp_id "
			+ "left join designation dg on edh.designation_id=dg.designation_id  "
			+ "where (:school_id IS NULL OR edh.school_id = :school_id) "
			+ "And (:dept_id IS NULL OR edh.dept_id = :dept_id) "
			+ "And (:emp_id IS NULL OR ed.emp_id = :emp_id) "
			+ "And edh.active=true And edh.chief_proctor_id is not null "
			+ "And CONCAT(IfNull(edh.emp_id,''),'',IfNull(edh.employee_name,''),'',IfNull(edh.empcode,''),"
			+ "'',IfNull(sc.school_name,''),'',IfNull(sc.school_name_short,''),'',IfNull(dg.designation_short_name,''),"
			+ "'',IfNull(d.dept_name_short,''),"
			+ "'',IfNull(ed.employee_name,''),'',IfNull(ed.empcode,'')) LIKE %:keyword% Group by edh.chief_proctor_id , edh.emp_id", nativeQuery = true)
	public Page<Map<String, Object>> fetchAllEmployeeDetailsBasedOnProctor(Pageable pageable, Object keyword, Integer school_id,
			Integer dept_id, Integer emp_id);

	@Query(value = "select edh.emp_id as emp_id,edh.job_id as job_id,edh.empcode as proctorCode,edh.email as proctorEmail,r.role_name As role_name,"
			+ "edh.employee_name as proctorName,edh.active as active,edh.mobile As mobile,d.dept_id As dept_id, "
			+ "ph.emp_id As proctorHeadId,ed.employee_name As proctorHeadName,ed.empcode as proctorHeadCode, "
			+ "sc.school_name_short as school_name_short,edh.school_id as school_id,sc.school_name as school_name,ud.id As userId, "
			+ "edh.designation_id as designation_id,dg.designation_name as designation_name,edh.chief_proctor_id As chief_proctor_id, "
			+ "dg.designation_short_name as designation_short_name,d.dept_name_short as dept_name_short,edh.master_code as master_code, "
			+ "(select count(*) from  proctor_student_assignment where emp_id = edh.emp_id And active=true) As studentCount "
			+ "from  employee_details edh   "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "left join schools sc on edh.school_id=sc.school_id  "
			+ "left join department d on edh.dept_id=d.dept_id  "
			+ "left join proctor_head ph on edh.chief_proctor_id=ph.chief_proctor_id "
			+ "left join employee_details ed on ed.emp_id=ph.emp_id "
			+ "left join designation dg on edh.designation_id=dg.designation_id  "
			+ "where (:school_id IS NULL OR edh.school_id = :school_id) "
			+ "And (:dept_id IS NULL OR edh.dept_id = :dept_id) "
			+ "And (:emp_id IS NULL OR ed.emp_id = :emp_id) "
			+ "And edh.active=true And edh.chief_proctor_id is not null And r.role_name not in ('Guest User')"
			+ " Group by edh.chief_proctor_id , edh.emp_id", nativeQuery = true)
	public Page<Map<String, Object>> fetchAllEmployeeDetailsBasedOnProctor(Pageable pageable1, Integer school_id, Integer dept_id,
			Integer emp_id);
	
	@Query(value = "select ed.mobile FROM employee_details ed where ed.emp_id=?1", nativeQuery = true)
	public String getProctorNumber(Integer proctorId);
	
	@Query(value = "SELECT ed.mobile FROM user_details ua "
			+ "left join employee_details ed on ed.email = ua.email where ua.id=?1", nativeQuery = true)
	public String getExecutiveNumber(Integer userId);

	@Query("SELECT e FROM EmployeeDetails e WHERE e.emp_id IN :employeeIds and active = 1")
	List<EmployeeDetails> getEmployeeDetailsByEmployeeIds(@Param("employeeIds") List<Integer> employeeIds);

	@Query(value = "select edh.emp_id as emp_id,edh.empcode as empcode,edh.email as email,edh.emp_image_attachment_path As emp_image_attachment_path, "
			+ "edh.employee_name as employee_name,edh.active as active,edh.mobile As mobile,d.dept_id As dept_id, "
			+ "sc.school_name_short as school_name_short,edh.school_id as school_id,sc.school_name as school_name,ud.id As userId, "
			+ "edh.designation_id as designation_id,dg.designation_name as designation_name,edh.chief_proctor_id As chief_proctor_id, "
			+ "dg.designation_short_name as designation_short_name,d.dept_name_short as dept_name_short,edh.master_code as master_code, "
			+ "edh.date_of_joining As date_of_joining,edh.dateofbirth As dateofbirth,"
			+ "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ', "
			+ "TIMESTAMPDIFF(MONTH, STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ', "
			+ "DAY(LAST_DAY(STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH, "
			+ "STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - "
			+ "DAY(STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience, "
			+ "TIMESTAMPDIFF(YEAR, edh.dateofbirth, CURDATE()) AS age ,"
			+ "pr.phd_holder_pursuing As phd_holder_pursuing,pr.university_name As university_name,"
			+ "pr.phd_completed_date As phd_completed_date,pr.phd_register_date As phd_register_date,"
			+ "(select count(*) from  proctor_student_assignment where emp_id = edh.emp_id) As studentCount,"
			+ "(select count(*) from  ivr_creation where proctor_id = edh.emp_id) As studentCallCount "
			+ "from  employee_details edh   "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join schools sc on edh.school_id=sc.school_id  "
			+ "left join department d on edh.dept_id=d.dept_id  "
			+ "left join designation dg on edh.designation_id=dg.designation_id  "
			+ "left join profile_research pr on edh.emp_id = pr.emp_id "
			+ "where edh.active=true And edh.emp_id =?1 ", nativeQuery = true)
	public List<Map<String, Object>> getEmployeeDetailsDetailsDataBasedOnEmpId(Integer empId);

	@Query("SELECT new com.au.dto.EmployeeFeedbackReport(e.emp_id, e.employee_name, e.empcode, d.designation_name, de.dept_name, e.date_of_joining, e.emp_image_attachment_path) " +
		       "FROM EmployeeDetails e " +
		       "LEFT JOIN Designation d ON d.designation_id=e.designation_id " +
		       "LEFT JOIN Department de ON de.dept_id=e.dept_id " +
		       "WHERE e.emp_id=:empId")
		public EmployeeFeedbackReport getEmployeeDetailsForFeedbackReport(Integer empId);

	
	@Query(value = "select ed.emp_id As emp_id,ed.empcode As empcode,ed.email As email,ed.employee_name As employee_name "
			+ "from  employee_details ed ", nativeQuery = true)
	public List<Map<String, Object>> getEmployeeDetailsDataBasedOnEmpId();
	
	@Query(value = "select ed.employee_name As employee_name from  employee_details ed Where ed.emp_id=?1 And ed.active=true ", nativeQuery = true)
	public String getEmployeeNameById(Integer empId);

	
	@Query(value = "select ed.emp_id As emp_id,ed.empcode As empcode,ed.email As email,ed.employee_name As employee_name "
			+ "from  employee_details ed Where ed.emp_id=?1 And ed.active=true", nativeQuery = true)
	public Map<String, Object> getEmployeeDetailsDataBasedOnEmpId11111(Integer empId);

	
	
	@Query(value = "select edh.emp_id as emp_id,edh.job_id as job_id,edh.empcode as empcode,edh.employee_name as employee_name,edh.phd_status as phd_status,edh.personal_email as personal_email,"
			+ "(select distinct bt.cardid from  biometric_transaction bt WHERE bt.empcode = edh.empcode AND bt.cardid IS NOT NULL LIMIT 1) as biometricCardId,"
			+ "edh.designation_id as designation_id,edh.date_of_joining as date_of_joining,edh.email as email,edh.school_id as school_id,edh.dept_id as dept_id,"
			+ "edh.grosspay_ctc as grosspay_ctc,edh.ctc as ctc,edh.gender as gender,edh.employee_status as employee_status,edh.created_date as created_date,"
			+ " CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ',TIMESTAMPDIFF(MONTH, STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ',"
			+ " DATEDIFF(CURDATE(), DATE_ADD(STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y'),INTERVAL TIMESTAMPDIFF(MONTH, STR_TO_DATE(edh.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) % 30, 'D') AS experience,"
			+ "sc.school_name_short as school_name_short,sc.school_name As school_name,d.dept_name_short as dept_name_short,edh.net_pay as net_pay,edh.bank_id as bank_name,d.dept_name as dept_name,"
			+ "edh.created_by as created_by,edh.created_username as created_username,edh.active as active,edh.master_code as master_code,edh.spouse_name as spouse_name,"
			+ "edh.firstname as firstname,edh.lastname as lastname,edh.report_id as report_id,edh.bank_id as bank_id,edh.shift_category_id as shift_category_id,"
			+ "edh.mobile as mobile,edh.alt_mobile_no as alt_mobile_no,edh.dateofbirth as dateofbirth,edh.martial_status as martial_status,edh.blood_group as blood_group,"
			+ "edh.father_name as father_name,edh.current_location as current_location,edh.salary_structure_id as salary_structure_id,edh.permanent_status As permanent_status,"
			+ "edh.pf_no as pf_no,edh.pan_no as pan_no,edh.emp_type_id as emp_type_id,edh.job_type_id as job_type_id,edh.exp_in_years as exp_in_years,"
			+ "edh.exp_in_months as exp_in_months,edh.hometown as hometown,edh.pincode as pincode,edh.annual_salary as annual_salary,edh.spl_pay as spl_pay,"
			+ "edh.key_skills as key_skills,edh.bank_account_no as bank_account_no,edh.bank_branch as bank_branch,edh.bank_ifsccode as bank_ifsccode,"
			+ "edh.leave_approver1_emp_id as leave_approver1_emp_id,edh.leave_approver2_emp_id as leave_approver2_emp_id,edh.attach as attach,edh.dlno as dlno,edh.dlexpno as dlexpno,"
			+ "edh.passportno as passportno,edh.passportexpno as passportexpno,edh.bankacc2 as bankacc2,edh.to_date as to_date,edh.prob as prob,"
			+ "edh.nda as nda,edh.nca as nca,edh.uan_no as uan_no,edh.punched_card_status as punched_card_status,edh.photo as photo,edh.maternity_status as maternity_status,"
			+ "edh.marriage_status as marriage_status,edh.paternity_status as paternity_status,edh.transport_status as transport_status,edh.salary_approve_status as salary_approve_status,"
			+ "edh.vehicle_route_id as vehicle_route_id,edh.transport_assign_date as transport_assign_date,"
			+ "edh.emp_image_attachment_path as emp_image_attachment_path,edh.emp_attachment_file_name2 as emp_attachment_file_name2,"
			+ "edh.transport_deassign_date as transport_deassign_date,edh.new_join_status as new_join_status,edh.pf_status as pf_status,"
			+ "edh.pt_status as pt_status,edh.permanent_file as permanent_file,edh.aadhar as aadhar,edh.photo_upload_status as photo_upload_status,edh.proctor_assign_status as proctor_assign_status,"
			+ "edh.bank_account_holder_name as bank_account_holder_name,edh.nda_nca_attach as nda_nca_attach,edh.store_indent_approver1 as store_indent_approver1,"
			+ "edh.store_indent_approver2 as store_indent_approver2,edh.esi_no as esi_no,edh.hra as hra,edh.da as da,edh.cca as cca,edh.ta as ta,"
			+ "edh.mfo As mfo,edh.pinfl As pinfl,edh.plastic_card As plastic_card,edh.transport_assign_month As transport_assign_month,edh.transport_deassign_month as transport_deassign_month,"
			+ "edh.other_allow as other_allow,edh.spl_1 as spl_1,edh.fte_status as fte_status,edh.title as title,edh.caste_category as caste_category,edh.contract_emp_type as contract_emp_type,"
			+ "edh.school as school,edh.contract_empcode as contract_empcode,edh.consolidated_amount as consolidated_amount,edh.from_date as from_date,edh.remarks as remarks,"
			+ "edh.subject_skills as subject_skills,edh.proctor_type as proctor_type,ss.salary_structure as salary_structure,dg.designation_short_name as designation_short_name,"
			+ "emt.emp_type_short_name as emp_type_short_name,emt.emp_type as emp_type,edh1.email as reporting_email,dg.designation_name as designation_name,"
			+ "edh.personal_medical_history as personal_medical_history,edh.family_medical_history as family_medical_history,edh1.employee_name as reporting_employeeName,"
			+ "edh.chief_proctor_id as chief_proctor_id,edh.religion as religion,edh.preferred_name_for_email as preferred_name_for_email,sh.shift_name as shift_name,"
			+ "sh.shift_start_time as shift_start_time,sh.shift_end_time as shift_end_time,jt.job_short_name as job_short_name,edh.height as height,"
			+ "jt.job_type as job_type,sia.employee_name as storeIndentApproverName1,sia2.employee_name as storeIndentApproverName2,ed.employee_name as proctorName,"
			+ "lap1.employee_name as leave_approver1_name,lap2.employee_name as leave_approver2_name,o.offer_id as offer_id from employee_details edh "
			+ "left join schools sc on edh.school_id=sc.school_id "
			+ "left join department d on edh.dept_id=d.dept_id " 
			+ "left join bank b on edh.bank_id=b.bank_id "
			+ "left join proctor_head ph on edh.chief_proctor_id=ph.chief_proctor_id "
			+ "left join employee_details ed on ed.emp_id=ph.emp_id "
			+ "left join offer o on edh.job_id=o.job_id " 
			+ "left join job_profile jp on edh.job_id=jp.job_id "
			+ "left join shift sh on edh.shift_category_id=sh.shift_category_id "
			+ "left join job_type jt on edh.job_type_id=jt.job_type_id "
			+ "left join employee_type emt on edh.emp_type_id=emt.emp_type_id "
			+ "left join salary_structure ss on edh.salary_structure_id=ss.salary_structure_id "
			+ "left join user_details ud on ud.email=edh.email "
			+ "left join employee_details edh1 on edh1.emp_id=edh.report_id "
			+ "left join designation dg on edh.designation_id=dg.designation_id "
			+ "left join employee_details sia on sia.emp_id=edh.store_indent_approver1 "
			+ "left join employee_details sia2 on sia2.emp_id=edh.store_indent_approver2 "
			+ "left join employee_details lap1 on lap1.emp_id=edh.leave_approver1_emp_id "
			+ "left join employee_details lap2 on lap2.emp_id=edh.leave_approver2_emp_id where edh.emp_id=?1", nativeQuery = true)
	public Map<String, Object> encryptedEmployeeDetails(Integer id);

	@Query("select contract_empcode from EmployeeDetails where emp_id = :empId")
	String findContractEmpcodeByEmpId(Integer empId);

	
	@Query(value = "select ed.emp_id As emp_id,ed.empcode As empcode,ed.employee_name As employee_name,ed.emp_image_attachment_path As emp_image_attachment_path,"
			+ "dg.designation_name As designation_name,dg.designation_short_name As designation_short_name,"
			+ "sc.school_name As school_name,sc.school_name_short As school_name_short,og.org_name As org_name,og.org_type As org_type "
			+ "from  employee_details ed "
			+ "left join designation dg on ed.designation_id=dg.designation_id "
			+ "left join schools sc on ed.school_id=sc.school_id "
			+ "left join organization og on sc.org_id=og.org_id "
			+ "Where ed.emp_id=?1 And ed.active=true", nativeQuery = true)
	public Map<String, Object> getDetails(Integer empId);
	
	
	@Query(value = "select new map(ss.salary_structure as salary_structure,edh.dept_id as dept_id,edh.salary_structure_id as salary_structure_id,"
			+ "edh.job_type_id as job_type_id,d.dept_name as dept_name,jt.job_type as job_type,edh.job_id as job_id,edh.ctc as ctc,edh.annual_salary as annual_salary,"
			+ "edh.net_pay as net_pay,edh.school_id as school_id,s.school_name as school_name,dg.designation_name as designation,edh.active as active,edh.date_of_joining as date_of_joining,"
			+ "edh.annual_salary as basic,edh.hra as hra,edh.cca as cca,edh.spl_1 as spl_1,edh.mfo as pf,edh.transport_assign_month as esi,edh.da as da,edh.remarks as remarks,"
			+ "edh.grosspay_ctc as gross,edh.transport_deassign_month as esic,edh.pinfl as management_pf,et.empType as employee_type,edh.email as email,edh.pf_status as isPf,edh.pt_status as isPt,"
			+ "edh.consolidated_amount as consolidated_amount,edh.from_date as from_date,edh.to_date as to_date,"
			+ "edh.employee_name as firstname,edh.plastic_card as pt,edh.report_id as report_id,edh.created_by as created_by,edh.created_date as created_date,"
			+ "ed.exp_in_years as exp_in_years,ed.exp_in_months as exp_in_months,edh.ta as ta,edh.mr as mr,edh.fr as fr,edh.me as me,"
			+ "s.school_name_short as school_name_short,edh.designation_id as designation_id,edh.other_allow as other_allow,"
			+ "edh.emp_type_id as emp_type_id,jp.current_location as current_location,jp.mobile as mobile) "
			+ "from EmployeeDetails edh "
			+ "left join Department d on edh.dept_id=d.dept_id "
			+ "left join SalaryStructure ss on edh.salary_structure_id=ss.salary_structure_id "
			+ "left join JobType jt on edh.job_type_id=jt.job_type_id "
			+ "left join Designation dg on edh.designation_id=dg.designation_id "
			+ "left join Schools s on edh.school_id=s.school_id "
			+ "left join EmployeeType et on edh.emp_type_id=et.empTypeId "
			+ "left join JobProfile jp on edh.job_id=jp.job_id "
			+ "left join ExperienceDetails ed on edh.job_id=ed.job_id where edh.emp_id=?1" )
	public Map<String, Object> encryptedFetchAllOfferDetailsByEmployeeId(Integer employeeId);

	
	@Query(value = "SELECT c.class_feedback_answers_id As class_feedback_answers_id,c.window_count As window_count,c.ratings As ratings, "
			+ "c.user_id As emp_id,c.course_id As course_id,ed.employee_name As employee_name,ed.empcode As empcode,ed.date_of_joining As date_of_joining, "
			+ "rs.current_sem As current_sem,rs.current_year As current_year,co.course_code As course_code,co.course_name As course_name, "
			+ "co.course_short_name As course_short_name,ac.ac_year As ac_year,de.designation_short_name As designation_short_name, "
			+ "d.dept_name_short As dept_name_short,d.dept_name As dept_name,de.designation_name As designation_name,s.school_name As school_name,s.school_name_short As school_name_short, "
			+ "o.org_name As org_name,o.org_type As org_type,ed.emp_image_attachment_path As emp_image_attachment_path,"
			+ "CONCAT(TIMESTAMPDIFF(YEAR, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()), 'Y ',  "
			+ "TIMESTAMPDIFF(MONTH, STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) % 12, 'M ',  "
			+ "DAY(LAST_DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y') + INTERVAL TIMESTAMPDIFF(MONTH,  "
			+ "STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y'), CURDATE()) MONTH)) - DAY(STR_TO_DATE(ed.date_of_joining, '%d-%m-%Y')) + DAY(CURDATE()), 'D ') AS experience "
			+ "FROM  class_feedback_answers c "
			+ "left join  academic_year ac on ac.ac_year_id=c.ac_year_id  "
			+ "left join  course co on co.course_id=c.course_id  "
			+ "left join  student_details sd on sd.student_id=c.student_id  "
			+ "left join  reporting_students rs on sd.student_id=rs.student_id  "
			+ "left join  employee_details ed on ed.emp_id=c.user_id  "
			+ "left join  department d on ed.dept_id=d.dept_id  "
			+ "left join  designation de on ed.designation_id=de.designation_id  "
			+ "left join  schools s on ed.school_id=s.school_id  "
			+ "left join  organization o on o.org_id=s.org_id "
			+ "where c.user_id =?1  group by c.user_id ", nativeQuery = true)
	public Map<String, Object> getemployeeDetailsBasedOnEmployeeId(Integer employeeId);

	@Query(value = "select * from employee_details where emp_type_id = 3 and active = 1 and employee_name not like 'GUEST%' ", nativeQuery = true)
	List<EmployeeDetails> getConsultantEmployees();


	@Query(value = "select ed.emp_id from employee_details ed " +
			"left join user_details ud on ud.email = ed.email " +
			"where ud.id = :userId", nativeQuery = true)
	Integer findByUserId(Integer userId);

	@Query("select email from EmployeeDetails where emp_id = :empId And active=true")
	public String getEmployeeEmailId(Integer empId);

	@Query(value = "select count(*) from employee_details ed where ed.email = :email And ed.active=true",nativeQuery = true)
	int emailCount(String email);

	@Modifying
	@Query(value = "update employee_details set paternity_status = 1 where emp_id = :empId", nativeQuery = true)
	void updatePaternityStatus(Integer empId);

	@Modifying
	@Query(value = "update employee_details set maternity_status = 1 where emp_id = :empId", nativeQuery = true)
	void updateMaternityStatus(Integer empId);

	@Query("select empcode from EmployeeDetails where emp_id = :empId")
    String findEmpcodeByEmpId(Integer empId);

	@Query(value = "select e.emp_id from employee_details e where e.empcode=:empCode and e.active=true ", nativeQuery = true)
	Integer findByJavaEmpcode(String empCode);
}