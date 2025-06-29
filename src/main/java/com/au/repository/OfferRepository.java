package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.Offer;

@Transactional
@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {

	@Query(value = "select email from Offer o where o.offer_id=?1")
	public String getEmail(Integer offer_id);

	@Query(value = "select designation from Offer o where o.offer_id=?1")
	public String getDesignation(Integer offer_id);

	@Query(value = "select new map(o.offer_id as offer_id,o.salary_structure as salary_structure,o.dept_id as dept_id,o.salary_structure_id as salary_structure_id,"
			+ "o.job_type_id as job_type_id,d.dept_name as dept_name,jt.job_type as job_type,o.job_id as job_id,o.ctc as ctc,o.annual_salary as annual_salary,o.description as description,"
			+ "o.net_pay as net_pay,o.school_id as school_id,s.school_name as school_name,o.designation as designation,o.active as active,o.ma as ma,o.date_of_joining as date_of_joining,"
			+ "o.basic as basic,o.hra as hra,o.cca as cca,o.spl_1 as spl_1,o.pf as pf,o.esi as esi,o.da as da,o.offercode as offercode,o.end_date as end_date,o.remarks as remarks,"
			+ "o.gross as gross,o.esic as esic,o.management_pf as management_pf,o.employee_type as employee_type,o.email as email,o.offerstatus as offerstatus,o.isPf as isPf,o.isPt as isPt,"
			+ "o.consolidated_amount as consolidated_amount,o.from_date as from_date,o.to_date as to_date,o.interview_date as interview_date,o.ctc_status as ctc_status,"
			+ "jp.firstname as firstname,o.pt as pt,o.report_id as report_id,o.created_by as created_by,o.created_date as created_date,o.program_specialization_id as program_specialization_id,"
			+ "o.consultant_emp_type as consultant_emp_type,ed.exp_in_years as exp_in_years,ed.exp_in_months as exp_in_months,o.ta as ta,o.mr as mr,o.fr as fr,o.me as me,"
			+ "s.school_name_short as school_name_short,o.designation_id as designation_id,o.offer_name as offer_name,o.comments as comments,o.other_allow as other_allow,"
			+ "o.emp_type_id as emp_type_id,jp.current_location as current_location,jp.mobile as mobile,o.pfc as pfc,o.ref_no as ref_no,o.ip_address as ip_address) "
			+ "from Offer o left join Department d on o.dept_id=d.dept_id left join JobType jt on o.job_type_id=jt.job_type_id "
			+ "left join Schools s on o.school_id=s.school_id left join JobProfile jp on o.job_id=jp.job_id "
			+ "left join ExperienceDetails ed on o.job_id=ed.job_id where o.offer_id=?1" )
	public List<HashMap<String, Object>> fetchAllDetails(Integer offer_id);
	

	@Query(value = "select date_of_joining from Offer o where o.offer_id=?1")
	public String getDateTime(Integer offer_id);

	@Query(value = "select offer_name from Offer o where o.offer_id=?1")
	public String getofferName(Integer offer_id);
	
	@Query(value = "select new map(o.offer_id as id,o.salary_structure as salary_structure,o.dept_id as dept_id,"
			+ "o.job_type_id as job_type_id,d.dept_name as dept_name,jt.job_type as job_type,o.job_id as job_id,o.ctc as ctc,"
			+ "o.net_pay as net_pay,o.school_id as school_id,s.school_name as school_name,o.designation as designation,o.designation_id as designation_id,"
			+ "o.basic as basic,o.hra as hra,o.cca as cca,o.spl_1 as specialAllowance,o.pf as pf,o.esi as esi,o.da as da,"
			+ "o.gross as gross,o.esic as esic,o.management_pf as management_pf,o.employee_type as employee_type,o.email as email,"
			+ "o.consolidated_amount as consolidated_amount,o.from_date as from_date,o.to_date as to_date,"
			+ "jp.firstname as firstname,o.pt as pt,o.report_id as report_id,o.created_date as created_date,o.created_by as created_by,"
			+ "o.consultant_emp_type as consultant_emp_type,ed.exp_in_years as exp_in_years,ed.exp_in_months as exp_in_months,"
			+ "s.school_name_short as school_name_short) "
			+ "from Offer o left join Department d on o.dept_id=d.dept_id left join JobType jt on o.job_type_id=jt.job_type_id "
			+ "left join Schools s on o.school_id=s.school_id left join JobProfile jp on o.job_id=jp.job_id "
			+ "left join ExperienceDetails ed on o.job_id=ed.job_id "
			+ "where CONCAT(IfNull(d.dept_name,''),'',IfNull(jt.job_type,''),'',IfNull(o.ctc,''),'',IfNull(o.net_pay,''),"
			+ "'',IfNull(s.school_name,''),'',IfNull(o.designation,''),'',IfNull(o.created_by,''),'',IfNull(o.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(o.offer_id as offer_id,o.salary_structure as salary_structure,o.dept_id as dept_id,"
			+ "o.job_type_id as job_type_id,d.dept_name as dept_name,jt.job_type as job_type,o.job_id as job_id,o.ctc as ctc,"
			+ "o.net_pay as net_pay,o.school_id as school_id,s.school_name as school_name,o.designation as designation,o.designation_id as designation_id,"
			+ "o.basic as basic,o.hra as hra,o.cca as cca,o.spl_1 as specialAllowance,o.pf as pf,o.esi as esi,o.da as da,"
			+ "o.gross as gross,o.esic as esic,o.management_pf as management_pf,o.employee_type as employee_type,o.email as email,"
			+ "o.consolidated_amount as consolidated_amount,o.from_date as from_date,o.to_date as to_date,"
			+ "jp.firstname as firstname,o.pt as pt,o.report_id as report_id,o.created_date as created_date,o.created_by as created_by,"
			+ "o.consultant_emp_type as consultant_emp_type,ed.exp_in_years as exp_in_years,ed.exp_in_months as exp_in_months,"
			+ "s.school_name_short as school_name_short) "
			+ "from Offer o left join Department d on o.dept_id=d.dept_id left join JobType jt on o.job_type_id=jt.job_type_id "
			+ "left join Schools s on o.school_id=s.school_id left join JobProfile jp on o.job_id=jp.job_id "
			+ "left join ExperienceDetails ed on o.job_id=ed.job_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "select new map(o.offer_id as offer_id,o.salary_structure as salary_structure,o.dept_id as dept_id,o.salary_structure_id as salary_structure_id,"
			+ "o.job_type_id as job_type_id,d.dept_name as dept_name,jt.job_type as job_type,o.job_id as job_id,o.ctc as ctc,o.ta as ta,o.other_allow as other_allow,o.epf as epf,"
			+ "o.net_pay as net_pay,o.school_id as school_id,s.school_name as school_name,o.designation as designation,o.designation_id as designation_id,"
			+ "o.basic as basic,o.hra as hra,o.cca as cca,o.spl_1 as spl_1,o.pf as pf,o.esi as esi,o.da as da,o.pfc as pfc,o.esi as esi,"
			+ "o.gross as gross,o.esic as esic,o.management_pf as management_pf,o.employee_type as employee_type,o.email as email,"
			+ "o.consolidated_amount as consolidated_amount,o.from_date as from_date,o.to_date as to_date,ct.name as cityName,"
			+ "jp.street as street,jp.locality as locality,jp.pincode as pincode,"
			+ "jp.firstname as firstname,o.pt as pt,o.report_id as report_id,o.created_date as created_date,o.created_by as created_by,"
			+ "o.consultant_emp_type as consultant_emp_type,ed.exp_in_years as exp_in_years,ed.exp_in_months as exp_in_months,"
			+ "s.school_name_short as school_name_short) "
			+ "from Offer o left join Department d on o.dept_id=d.dept_id left join JobType jt on o.job_type_id=jt.job_type_id "
			+ "left join Schools s on o.school_id=s.school_id left join JobProfile jp on o.job_id=jp.job_id "
			+ "left join ExperienceDetails ed on o.job_id=ed.job_id "
			+ "left join City ct on jp.city_id=ct.id where o.offer_id=?1")
	public List<HashMap<String, Object>> getOffer(Integer offer_id);
	
	@Query(value = "select :print_name From offer  where offer_id=:offer_id and active=true",nativeQuery=true)
	public String getValue(String print_name, Integer offer_id);

	@Query(value = "select MAX(o.offercode) from Offer o")
	public String getofferCode();
	
	@Query(value = "select o.offercode from Offer o where o.offer_id=?1")
	public String getofferCode1(Integer offer_id);
	
	@Query(value = "select * from offer o where o.offer_id=?1",nativeQuery=true)
	public Offer getOffer1(Integer offer_id);
	
	@Modifying
	@Query(value = "update Offer o set o.offerstatus=false where o.offer_id=?1")
	public void updateOfferStatusToPending(Integer offer_id);
	
	@Query(value = "Select Count(*) from Offer o where o.salary_structure_id=?1 and o.active=true")
	public Integer getCountOfSalaryStructureId(Integer salary_structure_id);
	
	@Modifying
	@Query(value = "update Offer o set o.offerstatus=?2, o.ip_address=?3 where o.offer_id=?1")
	public Object updateOfferStatus(Integer offer_id, Boolean offerstatus, String ip_address);

	@Query(value = "select o.remarks from Offer o where o.job_id=?1 and o.active=true")
	public String getRemarks(Integer job_id);
	
	@Query(value = "select o from Offer o where o.job_id=?1 and o.active=true order by o.offer_id desc")
	public  List<Offer>  offerDetailsByJobId(Integer job_id);
}
