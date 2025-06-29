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

import com.au.model.StudentBonafide;

@Transactional
@Repository
public interface StudentBonafideRepository extends JpaRepository<StudentBonafide, Integer> {

	
	@Query(value = "SELECT stdb from StudentBonafide stdb where stdb.active=true")
	public List<StudentBonafide> findAll11();
	
	
	
	@Query(value = "select new map(stdb.student_bonafide_id as id,stdb.bonafide_type as bonafide_type,"
			+ "stdb.auid as auid,stdb.hostel_fee_template_id as hostel_fee_template_id,stdb.bonafide_number as bonafide_number,"
			+ "stdb.current_sem as current_sem,stdb.current_year as current_year,std.student_name as student_name,std.acharya_email as acharya_email,"
			+ "stdb.created_username as created_username,stdb.modified_username as modified_username,stdb.created_Date as created_Date,"
			+ "stdb.modified_Date as modified_Date,stdb.created_by as created_by,stdb.modified_by as modified_by,"
			+ "stdb.active as active, std.ac_year_id as acYearId, std.school_id as schoolId) from StudentBonafide stdb "
			+ "left join Student_Details std on stdb.auid=std.auid "
			+ "where CONCAT(IfNull(stdb.created_username,''),'',IfNull(stdb.auid,''),'',IfNull(stdb.hostel_fee_template_id,''),'',"
			+ "IfNull(stdb.created_by,''),'',IfNull(stdb.created_Date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	

	@Query(value = "select new map(stdb.student_bonafide_id as id,stdb.bonafide_type as bonafide_type,"
			+ "stdb.auid as auid,stdb.hostel_fee_template_id as hostel_fee_template_id,stdb.bonafide_number as bonafide_number,"
			+ "stdb.current_sem as current_sem,stdb.current_year as current_year,std.student_name as student_name,std.acharya_email as acharya_email,"
			+ "stdb.created_username as created_username,stdb.modified_username as modified_username,stdb.created_Date as created_Date,"
			+ "stdb.modified_Date as modified_Date,stdb.created_by as created_by,stdb.modified_by as modified_by,"
			+ "stdb.active as active, std.ac_year_id as acYearId, std.school_id as schoolId) from StudentBonafide stdb "
			+ "left join Student_Details std on stdb.auid=std.auid ")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Modifying
	@Query(value = "update StudentBonafide stdb set stdb.active=false where stdb.student_bonafide_id=?1")
	public void delete1(Integer id);
	
	@Modifying
	@Query(value = "update StudentBonafide stdb set stdb.active=true where stdb.student_bonafide_id=?1")
	public void delete2(Integer id);
	
	
	@Query(value = "SELECT std.auid from Student_Details std where std.active=true")
	public List<String> list1();
	
	
	@Query(value = "SELECT stdb.bonafide_type as bonafide_type,stdb.auid as auid,stdb.created_username as created_username,"
			+ "stdb.created_by as created_by,stdb.modified_by as modified_by,vhn.voucher_head_new_id as voucher_head_new_id,"
			+ "stdb.hostel_fee_template_id as hostel_fee_template_id,std.student_name as student_name,std.father_name as father_name,"
			+ "std.program_id as program_id,std.program_specialization_id as program_specialization_id,std.school_id as school_id,"
			+ "std.fee_template_id as fee_template_id,vhn.voucher_head as voucher_head,ftsa.year1_amt as year1_amt,"
			+ "ftsa.year2_amt as year2_amt,ftsa.year3_amt as year3_amt,ftsa.year4_amt as year4_amt,ftsa.year5_amt as year5_amt,"
			+ "ftsa.year6_amt as year6_amt,ftsa.year7_amt as year7_amt,ftsa.year8_amt as year8_amt,ftsa.year9_amt as year9_amt,"
			+ "ftsa.year10_amt as year10_amt,ftsa.year11_amt as year11_amt,ftsa.year12_amt as year12_amt,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name,stdb.bonafide_number as bonafide_number,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "concat(p.program_short_name,'-',ps.program_specialization_short_name) as course,sc.ref_no as ref_no,"
			+ "gt.graduation_id as graduation_id,gt.graduation_name as graduation_name,gt.graduation_name_short as graduation_name_short,"
			+ "pa.program_assignment_id as program_assignment_id,pa.number_of_years as number_of_years,pa.number_of_semester as number_of_semester,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ct.currency_type_id as currency_type_id,"
			+ "ct.currency_type_name as currency_type_name,ct.currency_type_short_name as currency_type_short_name,ay.ac_year as ac_year,"
			+ "Concat(IfNull(ay.current_year,''),'-',IfNull((ay.current_year + pa.number_of_years),'')) as academic_batch,"
			+ "ud.email as principalEmail,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,stdb.created_Date as created_Date from student_bonafide stdb "
			+ "left join student_details std on stdb.auid=std.auid "
			+ "left join academic_year ay on std.ac_year_id=ay.ac_year_id "
			+ "left join program p on std.program_id=p.program_id "
			+ "left join program_specialization ps on std.program_specialization_id=ps.program_specialization_id "
			+ "left join program_assignment pa on std.program_assignment_id=pa.program_assignment_id "
			+ "left join graduation_type gt on pa.graduation_id=gt.graduation_id "
			+ "left join schools sc on std.school_id=sc.school_id "
			+ "left join user_details ud on ud.id=sc.user_id_for_email "
			+ "left join fee_template_sub_amount ftsa on std.fee_template_id=ftsa.fee_template_id "
			+ "Left join fee_template ft on std.fee_template_id=ft.fee_template_id "
			+ "Left join currency_type ct on ct.currency_type_id=ft.currency_type_id "
			+ "left join voucher_head_new vhn on ftsa.voucher_head_new_id=vhn.voucher_head_new_id "
			+ " where stdb.auid=?1 And stdb.bonafide_type=?2 and stdb.active=true",nativeQuery=true)
	public List<Map<String,Object>> getStudentBonafideDetails(String auid, String bonafide_type);


	@Query(value = "select * from student_bonafide ORDER BY student_bonafide_id Desc LIMIT 1",nativeQuery = true)
	public StudentBonafide getLatestBonafideNumber();

	@Query(value = "select ifNull(bonafide_number,0) from student_bonafide ORDER BY student_bonafide_id Desc LIMIT 1",nativeQuery = true)
	public String getMaxLatestBonafideNumberCount();


	@Query(value = "select count(*) from student_bonafide sb "
			+ "where sb.auid=?1 And sb.bonafide_type=?2 And sb.current_sem=?3 And sb.current_year=?4 And sb.active=true",nativeQuery = true)
	public int getCount(String auid, String bonafide_type, Integer currentReportingSem, Integer currentReportingYear);
	
	@Query(value = "SELECT oft.feetype as feeType,ofd.other_fee_details_id as other_fee_details_id,ofd.total as total,stdb.bonafide_number as bonafide_number,"
			+ "ofd.sem1 as sem1,ofd.sem3 as sem3,ofd.sem5 as sem5,ofd.sem7 as sem7,ofd.sem9 as sem9,ofd.sem11 as sem11,ofd.sem2 as sem2,"
			+ "ofd.sem4 as sem4,ofd.sem6 as sem6,ofd.sem8 as sem8,ofd.sem10 as sem10,ofd.sem12 as sem12,oft.other_fee_template_id as other_fee_template_id,"
			+ "ofd.voucher_head_id as voucher_head_id,stdb.bonafide_type as bonafide_type,stdb.auid as auid,oft.uniform_number as uniform_number,"
			+ "ct.currency_type_id as currency_type_id,ct.currency_type_name as currency_type_name,ct.currency_type_short_name as currency_type_short_name,"
			+ "pa.program_assignment_id as program_assignment_id,pa.number_of_years as number_of_years,pa.number_of_semester as number_of_semester,"
			+ "vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
//			+ "cfr.cma_fee_receipt_id As cma_fee_receipt_id,cfr.amount As amount,cfr.cma_receipt_id As cma_receipt_id,"
//			+ "cfr.receipt_type As receipt_type,cfr.paid_year As paid_year,"
			+ "sd.student_name as student_name FROM student_bonafide stdb "
			+ "left join student_details sd on stdb.auid=sd.auid "
//			+ "left join cma_fee_receipt cfr on cfr.student_id=sd.student_id "
			+ "left join other_fee_template oft on oft.ac_year_id=sd.ac_year_id And oft.program_specialization_id = sd.program_specialization_id "
			+ "left join other_fee_details ofd on oft.other_fee_template_id=ofd.template_id "
			+ "left join voucher_head_new vhn on vhn.voucher_head_new_id=ofd.voucher_head_id "
			+ "Left join fee_template ft on sd.fee_template_id=ft.fee_template_id "
			+ "Left join currency_type ct on ct.currency_type_id=ft.currency_type_id "
			+ "left join program_assignment pa on sd.program_assignment_id=pa.program_assignment_id "
			+ " where stdb.auid=?1 And stdb.bonafide_type=?2 and stdb.active=true",nativeQuery=true)
	public List<Map<String,Object>> studentBonafideUniformAndStationaryDetails(String auid, String bonafide_type);
	
	@Query(value = "SELECT oft.feetype as feeType,ofd.other_fee_details_id as other_fee_details_id,ofd.total as total,stdb.bonafide_number as bonafide_number,"
			+ "ofd.sem1 as sem1,ofd.sem3 as sem3,ofd.sem5 as sem5,ofd.sem7 as sem7,ofd.sem9 as sem9,ofd.sem11 as sem11,ofd.sem2 as sem2,"
			+ "ofd.sem4 as sem4,ofd.sem6 as sem6,ofd.sem8 as sem8,ofd.sem10 as sem10,ofd.sem12 as sem12,oft.other_fee_template_id as other_fee_template_id,"
			+ "ofd.voucher_head_id as voucher_head_id,stdb.bonafide_type as bonafide_type,stdb.auid as auid,oft.uniform_number as uniform_number,"
			+ "ct.currency_type_id as currency_type_id,ct.currency_type_name as currency_type_name,ct.currency_type_short_name as currency_type_short_name,"
			+ "pa.program_assignment_id as program_assignment_id,pa.number_of_years as number_of_years,pa.number_of_semester as number_of_semester,"
			+ "vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
//			+ "cfr.cma_fee_receipt_id As cma_fee_receipt_id,cfr.amount As amount,cfr.cma_receipt_id As cma_receipt_id,"
//			+ "cfr.receipt_type As receipt_type,cfr.paid_year As paid_year,"
			+ "sd.student_name as student_name FROM student_bonafide stdb "
			+ "left join student_details sd on stdb.auid=sd.auid "
//			+ "left join cma_fee_receipt cfr on cfr.student_id=sd.student_id "
			+ "left join other_fee_template oft on oft.fee_template_id=sd.fee_template_id "
			+ "left join other_fee_details ofd on oft.other_fee_template_id=ofd.template_id "
			+ "left join voucher_head_new vhn on vhn.voucher_head_new_id=ofd.voucher_head_id "
			+ "Left join fee_template ft on sd.fee_template_id=ft.fee_template_id "
			+ "Left join currency_type ct on ct.currency_type_id=ft.currency_type_id "
			+ "left join program_assignment pa on sd.program_assignment_id=pa.program_assignment_id "
			+ " where stdb.auid=?1 And stdb.bonafide_type=?2 and stdb.active=true",nativeQuery=true)
	public List<Map<String,Object>> studentBonafideAddOnDetails(String auid, String bonafide_type);


	
	@Query(value = "select sd.student_id as student_id,sd.auid as auid,"
			+ "pa.number_of_semester as number_of_semester,pa.number_of_years as number_of_years,pa.program_assignment_id as program_assignment_id,"
			+ "pa.program_type_id as program_type_id,pt.program_type_code as program_type_code,pt.program_type_name as program_type_name "
			+ "from student_details sd "
			+ "left join program_assignment pa on sd.program_assignment_id=pa.program_assignment_id "
			+ "left join program_type pt on pa.program_type_id=pt.program_type_id "
			+ "where sd.auid=?1 And sd.active=true",nativeQuery = true)
	public List<Map<String, Object>> studentBonafideDetailsDropDown(String auid);
	
}
