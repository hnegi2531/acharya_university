package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.FeeTemplate;
import com.au.model.ProgramType;



@Transactional
@Repository
public interface FeeTemplateRepository extends JpaRepository<FeeTemplate, Integer> {

	
	@Query(value = "select count(*) from fee_template where ac_year_id=?1 and fee_admission_category_id=?2 and program_id=?3", nativeQuery = true)
	public Integer findById(Integer id1, Integer id2, Integer id3);
/*
	@Query(value = "select new map (ft.fee_template_id as fee_template_id,ft.fee_template_name as fee_template_name,"
			+ "sc.school_name_short as school_name_short,ac.ac_year as ac_year,ft.nationality as nationality,"
			+ "pr.program_short_name as program_short_name,ct.currency_type_name as currency_type_name,ft.active as active,"
			+ "fau.fee_admission_category_type as fee_admission_category_type,"
			+ "fasc.fee_admission_sub_category_name as fee_admission_sub_category_name,pt.program_type_name as program_type_name,"
			+ "pss.program_specialization_short_name as program_specialization_short_name)"
			+ "from FeeTemplate ft left join Schools sc on ft.school_id=sc.school_id "
			+ "left join Academic_year ac on ft.ac_year_id=ac.ac_year_id "
			+ "left join Program pr on ft.program_id=pr.program_id "
			+ "left join Currency_Type ct on ft.currency_type_id= ct.currency_type_id "
			+ "left join FeeAdmissionCategory fau on ft.fee_admission_ca" + "tegory_id=fau.fee_admission_category_id "
			+ "left join FeeAdmissionSubCategory fasc on ft.fee_admission_sub_category_id= fasc.fee_admission_sub_category_id "
			+ "left join ProgramType pt on ft.program_type_id=pt.program_type_id "
			+ "left join ProgramSpecialization pss on ft.program_specialization_id=pss.program_specialization_id "
			+ "where ft.fee_template_id IN (:fee_template)")
	public List<HashMap<String, Object>> fetchFeeTemplateDetails(@Param("fee_template") List<Integer> fee_template_id);
	
*/
/*	
	@Query(value = "select new map (ft.fee_template_id as fee_template_id,"
			+ "sc.school_name_short as school_name_short,"
			+ "ac.ac_year as ac_year ,"
			+ "ft.nationality as Nationality,"
			+ "ft.is_nri as is_nri,"
			+ "ft.is_paid_at_board as is_paid_at_board,"
			+ "cu.currency_type_name as currency_type_name ,"
			+ "fac.fee_admission_category_type as fee_admission_category_type,"
			+ " fasc.fee_admission_sub_category_name as fee_admission_sub_category_name ,"
			+ "pt.program_type_name as program_type_name,"
			+ "pss.program_specialization_short_name as program_specialization_short_name)"
			+ "from FeeTemplate ft"
			+ " left join Schools sc on ft.school_id=sc.school_id"
			+ " left join Academic_year ac on ft.ac_year_id = ac.ac_year_id "
			+ "left join Currency_Type cu on ft.currency_id = cu.currency_id "
			+ "left join FeeAdmissionCategory fac on ft.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join FeeAdmissionSubCategory fasc on ft.fee_admission_sub_category_id=fasc.fee_admission_sub_category_id "
			+ " left join ProgramType pt on ft.program_type_id=pt.program_type_id "
			+ "left join ProgramSpecilization pss on ft.program_specialization_id=pss.program_specialization_id "
			+ "where ft.fee_template_id IN (:fee_template)")
			public List<HashMap<String, Object>> fetchFeeTemplateDetails(@Param("fee_template") List<Integer> fee_template_id);
	*/

	@Query(value = "select new map(fasc.fee_admission_sub_category_id as fee_admission_sub_category_id,"
			+ "fasc.fee_admission_sub_category_name as fee_admission_sub_category_name) "
			+ "from FeeTemplate ft left join FeeAdmissionSubCategory fasc "
			+ "on ft.fee_admission_sub_category_id=fasc.fee_admission_sub_category_id "
			+ "where ft.fee_admission_sub_category_id=?1")
	public List<HashMap<String, Object>> fetchFeeTemplateDetail(Integer fee_admission_sub_category_id);

	
	@Query(value = "select f from FeeTemplate f where f.fee_template_id=?1")
	public FeeTemplate findByfee_template_id(Integer fee_template_id);

	@Query(value = "select new map(ft.fee_template_id as fee_template_id,ft.fee_template_name as fee_template_name,"
			+ "sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,ac.ac_year as ac_year) "
			+ "from FeeTemplate ft left join Schools sc on ft.school_id=sc.school_id "
			+ "left join Program pr on ft.program_id=pr.program_id "
			+ "left join Academic_year ac on ft.ac_year_id=ac.ac_year_id where ft.active=true")
	public List<HashMap<String, Object>> edittemplate();

	@Modifying
	@Query(value = "update FeeTemplate ft set ft.active=false where ft.fee_template_id=?1")
	public void updateFeetemplate(Integer fee_template_id);
	

	@Modifying
	@Query(value = "update FeeTemplate ft set ft.active=true where ft.fee_template_id=?1")
	public void update(Integer id);
	
	@Modifying
	@Query(value = "update FeeTemplate ft set ft.approved_status=false where ft.fee_template_id=?1")
	public void update1(Integer id);

	
	@Query(value = "select new map(ft.ac_year_id as ac_year_id,ft.program_id as program_id,"
			+ "ft.fee_admission_sub_category_id as fee_admission_sub_category_id,ft.fee_admission_category_id as fee_admission_category_id,"
			+ "ft.program_type_id as program_type_id,ft.program_specialization as program_specialization,"
			+ "ft.uniform_status as uniform_status,ft.laptop_status as laptop_status,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,"
			+ "ps.program_specialization_name as program_specialization_name,ft.lat_year_sem as lat_year_sem,"
			+ "ft.program_specialization_id as program_specialization_id,ft.currency_type_id as currency_type_id,"
			+ "ft.fee_template_id as fee_template_id,ft.Is_paid_at_board as Is_paid_at_board,"
			+ "ft.fee_template_name as fee_template_name,sc.school_name_short as school_name_short,"
			+ "sc.school_id as school_id,pr.program_short_name as program_short_name,ac.ac_year as ac_year,"
			+ "org.org_id As org_id,org.org_name As org_name,org.org_type As org_type,"
			+ "ct.currency_type_name as currency_type_name,ft.created_by as created_by,ft.created_username as created_username,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,"
			+ "ft.nationality as nationality,ptn.program_type_name as program_type_name,"
			+ "fasc.fee_admission_sub_category_name as fee_admission_sub_category_name,ft.fee_year1_amt as fee_year1_amt,"
			+ "ft.fee_year2_amt as fee_year2_amt,ft.fee_year3_amt as fee_year3_amt,ft.fee_year4_amt as fee_year4_amt,"
			+ "ft.fee_year5_amt as fee_year5_amt,ft.fee_year6_amt as fee_year6_amt,ft.fee_year7_amt as fee_year7_amt,"
			+ "ft.fee_year8_amt as fee_year8_amt,ft.fee_year9_amt as fee_year9_amt,ft.fee_year10_amt as fee_year10_amt,"
			+ "ft.fee_year11_amt as fee_year11_amt,ft.fee_year12_amt as fee_year12_amt,ft.remarks as remarks,"
			+ "ft.fee_year_total_amount as fee_year_total_amount,ft.approved_status as approved_status,ft.Is_nri as Is_nri)"
			+ "from FeeTemplate ft "
			+ "left join Schools sc on ft.school_id=sc.school_id "
			+ "left join Organization org on org.org_id=sc.org_id "
			+ "left join Program pr on ft.program_id=pr.program_id "
			+ "left join Academic_year ac on ft.ac_year_id=ac.ac_year_id "
			+ "left join Currency_Type ct on ft.currency_type_id=ct.currency_type_id "
			+ "left join FeeAdmissionCategory fac on ft.fee_admission_category_id=fac.fee_admission_category_id"
			+ " left join ProgramType ptn on ft.program_type_id=ptn.program_type_id "
			+ "left join FeeAdmissionSubCategory fasc on ft.fee_admission_sub_category_id=fasc.fee_admission_sub_category_id "
			+ "left join ProgramSpecilization ps on ps.program_specialization_id=ft.program_specialization_id "
			+ "where ft.fee_template_id=?1 and ft.active=true")
	public List<HashMap<String, Object>> editfeetemplate(Integer fee_template_id);

	@Query(value = "select ft.fee_template_id as fee_template_id,ft.fee_template_name as fee_template_name,"
			+ "ft.program_specialization_id as program_specialization_id "
			+ " from fee_template ft  where ft.ac_year_id=?1 and ft.school_id=?2 and ft.program_id=?3 "
			+ "and ft.program_specialization_id like ?4 "
			+ "and ft.fee_admission_category_id=?5 "
			+ "and ft.fee_admission_sub_category_id =?6 and ft.Is_nri=?7 and ft.active=true and "
			+ "ft.approved_status=true", nativeQuery=true)
	public List<Map<String, Object>> getfeeTemplateName(Integer ac_year_id, Integer school_id, Integer program_id,
			String program_specialization_id, Integer fee_admission_category_id,
			Integer fee_admission_sub_category_id, Boolean is_nri);

	@Query(value = "select f from FeeTemplate f where f.active=true" )
	public List<FeeTemplate> findAll1();

	@Query(value = "select new map(ft.fee_template_id as id,ft.Is_paid_at_board as Is_paid_at_board,ft.lat_year_sem as lat_year_sem,"
			+ "ft.fee_template_name as fee_template_name,count(sd.fee_template_id) as countOfStudent,"
			+ "ft.uniform_status as uniform_status,ft.laptop_status as laptop_status,"
			+ "ft.ac_year_id as ac_year_id,ft.school_id as school_id,ft.program_id as program_id,"
			+ "ft.program_specialization_id as program_specialization_id,ft.currency_type_id as currency_type_id,"
			+ "ft.fee_admission_category_id as fee_admission_category_id,ft.fee_template_path as fee_template_path,"
			+ "ft.fee_admission_sub_category_id as fee_admission_sub_category_id,ft.nationality as nationality,"
			+ "ft.Is_nri as Is_nri,ft.program_type_id as program_type_id,ft.approved_by as approved_by,"
			+ "ft.approved_status as approved_status,ft.approved_date as approved_date,ft.created_by as created_by,"
			+ "ft.modified_by as modified_by,ft.created_date as created_date,ft.modified_date as modified_date,"
			+ "ft.active as active,ft.remarks as remarks,ft.created_username as created_username,"
			+ "ft.modified_username as modified_username,ft.program_specialization as program_specialization,"
			+ "ft.fee_year_total_amount as fee_year_total_amount,sc.school_name_short as school_name_short,"
			+ "pr.program_short_name as program_short_name,ac.ac_year as ac_year,ct.currency_type_name as currency_type_name,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,"
			+ "ptn.program_type_name as program_type_name,"
			+ "fasc.fee_admission_sub_category_name as fee_admission_sub_category_name) from FeeTemplate ft "
			+ "left join Student_Details sd on sd.fee_template_id=ft.fee_template_id "
			+ "left join Schools sc on ft.school_id=sc.school_id "
			+ "left join Program pr on ft.program_id=pr.program_id "
			+ "left join Academic_year ac on ft.ac_year_id=ac.ac_year_id "
			+ "left join Currency_Type ct on ft.currency_type_id=ct.currency_type_id "
			+ "left join FeeAdmissionCategory fac on ft.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join ProgramType ptn on ft.program_type_id=ptn.program_type_id "
			+ "left join FeeAdmissionSubCategory fasc on ft.fee_admission_sub_category_id=fasc.fee_admission_sub_category_id "
			+ "Where CONCAT(IfNull(ft.fee_template_id,''),'',IfNull(ft.fee_template_name,''),'',"
			+ "IfNull(ft.fee_template_path,''),'',IfNull(ft.nationality,''),'',IfNull(ft.created_date,''),'',"
			+ "IfNull(ft.created_username,''),'',IfNull(ft.program_specialization,''),'',IfNull(ft.fee_year_total_amount,''),'',"
			+ "IfNull(sc.school_name_short,''),'',IfNull(pr.program_short_name,''),'',IfNull(ac.ac_year,''),'',"
			+ "IfNull(fac.fee_admission_category_short_name,''),'',IfNull(ptn.program_type_name,''),'',IfNull(fasc.fee_admission_sub_category_name,'')) LIKE %?1% group by ft.fee_template_id")
	public Page<Object> fetchFeeTemplateDetail1(Pageable pageable, Object keyword);

	
	@Query(value = "select new map(ft.fee_template_id as id,ft.Is_paid_at_board as Is_paid_at_board,ft.lat_year_sem as lat_year_sem,"
			+ "ft.fee_template_name as fee_template_name,count(sd.fee_template_id) as countOfStudent,"
			+ "ft.ac_year_id as ac_year_id,ft.school_id as school_id,ft.program_id as program_id,"
			+ "ft.uniform_status as uniform_status,ft.laptop_status as laptop_status,"
			+ "ft.program_specialization_id as program_specialization_id,ft.currency_type_id as currency_type_id,"
			+ "ft.fee_admission_category_id as fee_admission_category_id,ft.fee_template_path as fee_template_path,"
			+ "ft.fee_admission_sub_category_id as fee_admission_sub_category_id,ft.nationality as nationality,"
			+ "ft.Is_nri as Is_nri,ft.program_type_id as program_type_id,ft.approved_by as approved_by,"
			+ "ft.approved_status as approved_status,ft.approved_date as approved_date,ft.created_by as created_by,"
			+ "ft.modified_by as modified_by,ft.created_date as created_date,ft.modified_date as modified_date,"
			+ "ft.active as active,ft.remarks as remarks,ft.created_username as created_username,"
			+ "ft.modified_username as modified_username,ft.program_specialization as program_specialization,"
			+ "ft.fee_year_total_amount as fee_year_total_amount,sc.school_name_short as school_name_short,"
			+ "pr.program_short_name as program_short_name,ac.ac_year as ac_year,ct.currency_type_name as currency_type_name,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,"
			+ "ptn.program_type_name as program_type_name,"
			+ "fasc.fee_admission_sub_category_name as fee_admission_sub_category_name) from FeeTemplate ft "
			+ "left join Student_Details sd on sd.fee_template_id=ft.fee_template_id "
			+ "left join Schools sc on ft.school_id=sc.school_id "
			+ "left join Program pr on ft.program_id=pr.program_id "
			+ "left join Academic_year ac on ft.ac_year_id=ac.ac_year_id "
			+ "left join Currency_Type ct on ft.currency_type_id=ct.currency_type_id "
			+ "left join FeeAdmissionCategory fac on ft.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join ProgramType ptn on ft.program_type_id=ptn.program_type_id "
			+ "left join FeeAdmissionSubCategory fasc on ft.fee_admission_sub_category_id=fasc.fee_admission_sub_category_id group by ft.fee_template_id")
	public Page<Object> fetchFeeTemplateDetail2(Pageable pageable);

	@Query(value = "select new map(ft.program_specialization_id as program_specialization_id) from FeeTemplate ft "
			+ "where ft.ac_year_id=?1 and ft.fee_admission_category_id=?2 "
			+ "and ft.fee_admission_sub_category_id=?3 and ft.nationality=?4 and ft.program_type_id=?5 and ft.currency_type_id =?6 "
			+ "and ft.school_id=?7 and ft.program_id=?8 and ft.active=true")
	public List<HashMap<String, Object>> getFeeTemplateId(Integer ac_year_id, Integer fee_admission_category_id,
			Integer fee_admission_sub_category_id,String nationality, Integer program_type_id, Integer currency_type_id, Integer school_id,
			Integer program_id);

	@Modifying
	@Query(value = "update FeeTemplate ft set ft.fee_template_path=?2 where ft.fee_template_id=?1")
	public void updatePath(Integer fee_template_id, String t1);
	
	
	@Query(value = "select new map(ft.fee_year1_amt as fee_year1_amt, ft.fee_year2_amt as fee_year2_amt,"
			+ "ft.fee_year3_amt as fee_year3_amt,ft.fee_year4_amt as fee_year4_amt,"
			+ "ft.fee_year5_amt as fee_year5_amt,ft.fee_year6_amt as fee_year6_amt,ft.fee_year7_amt as fee_year7_amt,"
			+ "ft.fee_year8_amt as fee_year8_amt,ft.fee_year9_amt as fee_year9_amt,ft.fee_year10_amt as fee_year10_amt,"
			+ "ft.fee_year11_amt as fee_year11_amt,ft.fee_year12_amt as fee_year12_amt,ft.remarks as remarks,"
			+ "ft.fee_year_total_amount as fee_year_total_amount,ft.approved_status as approved_status,ft.Is_nri as Is_nri)"
			+ "from FeeTemplate ft where ft.fee_template_id=?1 and ft.active=true")
	public List<HashMap<String, Object>> feeTemplateDetailsForFeeReceiptDueAmount(Integer fee_template_id);
	
	@Query(value = "Select ft from FeeTemplate ft where ft.fee_template_id=?1 And ft.active=true")
	public List<FeeTemplate> fetchFeeTemplateDetails(Integer fee_template_id);
	
	@Query(value = "Select ft.fee_template_id from FeeTemplate ft where ft.ac_year_id=?1 And ft.active=true")
	public List<Integer> fetchFeeTemplateIdsonAcademicYear(Integer ac_year_id);

	@Query(value = "Select Count(*) From fee_template ft Where ft.ac_year_id=?1 "
	        + "And ft.fee_admission_category_id=?2 "
	        + "And ft.fee_admission_sub_category_id=?3 "
	        + "And ft.program_id=?4 "
	        + "And (ft.program_specialization_id LIKE CONCAT('%,', ?5, ',%') "
	        + "   OR ft.program_specialization_id LIKE CONCAT(?5, ',%') "
	        + "   OR ft.program_specialization_id LIKE CONCAT('%,', ?5) "
	        + "   OR ft.program_specialization_id = ?5) "
	        + "And ft.active = true", nativeQuery = true)
	public int countFeeTemplate(Integer ac_year_id, Integer fee_admission_category_id,
	        Integer fee_admission_sub_category_id, Integer program_id, String program_specialization_id);

	@Query(value = "Select ft.fee_template_name from FeeTemplate ft where ft.ac_year_id=?1 and ft.active=true")
	public List<String> getFeeTemplateName(Integer old_ac_year_id);

	@Query(value = "select count(*) from FeeTemplate where ac_year_id=?1 and fee_template_name in (?2) And active=true")
	public Integer countFeeTemplate(Integer getOld_ac_year_id, List<String> feeTemplateName);

	@Query(value="select ft from FeeTemplate ft left join Student_Details  sd on sd.fee_template_id=ft.fee_template_id where sd.student_id=:studentId  ")
	public FeeTemplate getFeeTemplateByStudentId(Integer studentId);
	
	@Query(value="select pt from FeeTemplate ft "
			+ "left join ProgramType pt on ft.program_type_id=pt.program_type_id Where ft.fee_template_id=?1 And ft.active=1 ")
	public ProgramType feeTemplateType(Integer fee_template_id);

	
	@Query(value = "select new map(ft.fee_template_id as fee_template_id,ft.Is_paid_at_board as Is_paid_at_board,ft.lat_year_sem as lat_year_sem,"
			+ "ft.fee_template_name as fee_template_name,count(sd.fee_template_id) as countOfStudent,"
			+ "ft.ac_year_id as ac_year_id,ft.school_id as school_id,ft.program_id as program_id,"
			+ "ft.uniform_status as uniform_status,ft.laptop_status as laptop_status,"
			+ "ft.fee_year1_amt as fee_year1_amt, ft.fee_year2_amt as fee_year2_amt,"
			+ "ft.fee_year3_amt as fee_year3_amt,ft.fee_year4_amt as fee_year4_amt,"
			+ "ft.fee_year5_amt as fee_year5_amt,ft.fee_year6_amt as fee_year6_amt,ft.fee_year7_amt as fee_year7_amt,"
			+ "ft.fee_year8_amt as fee_year8_amt,ft.fee_year9_amt as fee_year9_amt,ft.fee_year10_amt as fee_year10_amt,"
			+ "ft.fee_year11_amt as fee_year11_amt,ft.fee_year12_amt as fee_year12_amt,"
			+ "ft.program_specialization_id as program_specialization_id,ft.currency_type_id as currency_type_id,"
			+ "ft.fee_admission_category_id as fee_admission_category_id,ft.fee_template_path as fee_template_path,"
			+ "ft.fee_admission_sub_category_id as fee_admission_sub_category_id,ft.nationality as nationality,"
			+ "ft.Is_nri as Is_nri,ft.program_type_id as program_type_id,ft.approved_by as approved_by,"
			+ "ft.approved_status as approved_status,ft.approved_date as approved_date,ft.created_by as created_by,"
			+ "ft.modified_by as modified_by,ft.created_date as created_date,ft.modified_date as modified_date,"
			+ "ft.active as active,ft.remarks as remarks,ft.created_username as created_username,"
			+ "ft.modified_username as modified_username,ft.program_specialization as program_specialization,"
			+ "ft.fee_year_total_amount as fee_year_total_amount,sc.school_name_short as school_name_short,"
			+ "pr.program_short_name as program_short_name,ac.ac_year as ac_year,ct.currency_type_name as currency_type_name,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,"
			+ "ptn.program_type_name as program_type_name,"
			+ "fasc.fee_admission_sub_category_name as fee_admission_sub_category_name) from FeeTemplate ft "
			+ "left join Student_Details sd on sd.fee_template_id=ft.fee_template_id "
			+ "left join Schools sc on ft.school_id=sc.school_id "
			+ "left join Program pr on ft.program_id=pr.program_id "
			+ "left join Academic_year ac on ft.ac_year_id=ac.ac_year_id "
			+ "left join Currency_Type ct on ft.currency_type_id=ct.currency_type_id "
			+ "left join FeeAdmissionCategory fac on ft.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join ProgramType ptn on ft.program_type_id=ptn.program_type_id "
			+ "left join FeeAdmissionSubCategory fasc on ft.fee_admission_sub_category_id=fasc.fee_admission_sub_category_id "
			+ "where ft.fee_template_id=?1 And ft.active=true")
	public Map<String, Object> getFeeTemplateDetailsData(Integer fee_template_id);

	@Query(value = "select CASE WHEN :semOrYear=1 THEN ft.fee_year1_amt "
			+ " WHEN :semOrYear=2 THEN ft.fee_year2_amt "
			+ " WHEN :semOrYear=3 THEN ft.fee_year3_amt "
			+ " WHEN :semOrYear=4 THEN ft.fee_year4_amt "
			+ " WHEN :semOrYear=5 THEN ft.fee_year5_amt "
			+ " WHEN :semOrYear=6 THEN ft.fee_year6_amt "
			+ " WHEN :semOrYear=7 THEN ft.fee_year7_amt "
			+ " WHEN :semOrYear=8 THEN ft.fee_year8_amt "
			+ " WHEN :semOrYear=9 THEN ft.fee_year9_amt "
			+ " WHEN :semOrYear=10 THEN ft.fee_year10_amt "
			+ " WHEN :semOrYear=11 THEN ft.fee_year11_amt "
			+ " WHEN :semOrYear=12 THEN ft.fee_year12_amt "
			+ " ELSE 0 END as dueAmount "
			+ " from FeeTemplate ft where ft.fee_template_id=:fee_template_id")
	public Integer getsemOrYearFeeForReadmission(Integer semOrYear, Integer fee_template_id);

	@Query(value = "Select ft.is_regular from FeeAdmissionCategory ft where ft.fee_admission_category_id=?1 And ft.active=true")
	public Boolean checkIsRegularOrNot(Integer fee_admission_category_id);

	@Query(value = "select new map(ft.fee_template_id as id,ft.Is_paid_at_board as Is_paid_at_board,ft.lat_year_sem as lat_year_sem,"
			+ "ft.fee_template_name as fee_template_name,count(sd.fee_template_id) as countOfStudent,"
			+ "ft.uniform_status as uniform_status,ft.laptop_status as laptop_status,"
			+ "ft.ac_year_id as ac_year_id,ft.school_id as school_id,ft.program_id as program_id,"
			+ "ft.program_specialization_id as program_specialization_id,ft.currency_type_id as currency_type_id,"
			+ "ft.fee_admission_category_id as fee_admission_category_id,ft.fee_template_path as fee_template_path,"
			+ "ft.fee_admission_sub_category_id as fee_admission_sub_category_id,ft.nationality as nationality,"
			+ "ft.Is_nri as Is_nri,ft.program_type_id as program_type_id,ft.approved_by as approved_by,"
			+ "ft.approved_status as approved_status,ft.approved_date as approved_date,ft.created_by as created_by,"
			+ "ft.modified_by as modified_by,ft.created_date as created_date,ft.modified_date as modified_date,"
			+ "ft.active as active,ft.remarks as remarks,ft.created_username as created_username,"
			+ "ft.modified_username as modified_username,ft.program_specialization as program_specialization,"
			+ "ft.fee_year_total_amount as fee_year_total_amount,sc.school_name_short as school_name_short,"
			+ "pr.program_short_name as program_short_name,ac.ac_year as ac_year,ct.currency_type_name as currency_type_name,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,"
			+ "ptn.program_type_name as program_type_name,"
			+ "fasc.fee_admission_sub_category_name as fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name) from FeeTemplate ft "
			+ "left join Student_Details sd on sd.fee_template_id=ft.fee_template_id "
			+ "left join Schools sc on ft.school_id=sc.school_id "
			+ "left join Program pr on ft.program_id=pr.program_id "
			+ "left join Academic_year ac on ft.ac_year_id=ac.ac_year_id "
			+ "left join Currency_Type ct on ft.currency_type_id=ct.currency_type_id "
			+ "left join FeeAdmissionCategory fac on ft.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join ProgramType ptn on ft.program_type_id=ptn.program_type_id "
			+ "left join FeeAdmissionSubCategory fasc on ft.fee_admission_sub_category_id=fasc.fee_admission_sub_category_id "
			+ "where (:ac_year_id is null or ft.ac_year_id = :ac_year_id) "
		    + "and (:school_id is null or ft.school_id = :school_id) "
		    + "and (:fee_admission_category_id is null or ft.fee_admission_category_id = :fee_admission_category_id) and "
			+ "CONCAT(IfNull(ft.fee_template_id,''),'',IfNull(ft.fee_template_name,''),'',"
			+ "IfNull(ft.fee_template_path,''),'',IfNull(ft.nationality,''),'',IfNull(ft.created_date,''),'',"
			+ "IfNull(ft.created_username,''),'',IfNull(ft.program_specialization,''),'',IfNull(ft.fee_year_total_amount,''),'',"
			+ "IfNull(sc.school_name_short,''),'',IfNull(pr.program_short_name,''),'',IfNull(ac.ac_year,''),'',"
			+ "IfNull(fac.fee_admission_category_short_name,''),'',IfNull(ptn.program_type_name,''),'',IfNull(fasc.fee_admission_sub_category_name,'')) "
			+ "LIKE %:keyword% group by ft.fee_template_id")
	public Page<Object> listAllAcYearId1(Pageable pageable, Object keyword, Integer ac_year_id, Integer school_id, Integer fee_admission_category_id);

	@Query(value = "select new map(ft.fee_template_id as id,ft.Is_paid_at_board as Is_paid_at_board,ft.lat_year_sem as lat_year_sem,"
			+ "ft.fee_template_name as fee_template_name,count(sd.fee_template_id) as countOfStudent,"
			+ "ft.ac_year_id as ac_year_id,ft.school_id as school_id,ft.program_id as program_id,"
			+ "ft.uniform_status as uniform_status,ft.laptop_status as laptop_status,"
			+ "ft.program_specialization_id as program_specialization_id,ft.currency_type_id as currency_type_id,"
			+ "ft.fee_admission_category_id as fee_admission_category_id,ft.fee_template_path as fee_template_path,"
			+ "ft.fee_admission_sub_category_id as fee_admission_sub_category_id,ft.nationality as nationality,"
			+ "ft.Is_nri as Is_nri,ft.program_type_id as program_type_id,ft.approved_by as approved_by,"
			+ "ft.approved_status as approved_status,ft.approved_date as approved_date,ft.created_by as created_by,"
			+ "ft.modified_by as modified_by,ft.created_date as created_date,ft.modified_date as modified_date,"
			+ "ft.active as active,ft.remarks as remarks,ft.created_username as created_username,"
			+ "ft.modified_username as modified_username,ft.program_specialization as program_specialization,"
			+ "ft.fee_year_total_amount as fee_year_total_amount,sc.school_name_short as school_name_short,"
			+ "pr.program_short_name as program_short_name,ac.ac_year as ac_year,ct.currency_type_name as currency_type_name,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,"
			+ "ptn.program_type_name as program_type_name,"
			+ "fasc.fee_admission_sub_category_name as fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name) from FeeTemplate ft "
			+ "left join Student_Details sd on sd.fee_template_id=ft.fee_template_id "
			+ "left join Schools sc on ft.school_id=sc.school_id "
			+ "left join Program pr on ft.program_id=pr.program_id "
			+ "left join Academic_year ac on ft.ac_year_id=ac.ac_year_id "
			+ "left join Currency_Type ct on ft.currency_type_id=ct.currency_type_id "
			+ "left join FeeAdmissionCategory fac on ft.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join ProgramType ptn on ft.program_type_id=ptn.program_type_id "
			+ "left join FeeAdmissionSubCategory fasc on ft.fee_admission_sub_category_id=fasc.fee_admission_sub_category_id "
			+ "where (:ac_year_id is null or ft.ac_year_id = :ac_year_id) "
		    + "and (:school_id is null or ft.school_id = :school_id) "
		    + "and (:fee_admission_category_id is null or ft.fee_admission_category_id = :fee_admission_category_id) "
			+ "group by ft.fee_template_id")
	public Page<Object> listAllAcYearId2(Pageable pageable, Integer ac_year_id, Integer school_id, Integer fee_admission_category_id);

	@Query(value = "Select ft.fee_template_id as fee_template_id,ft.fee_template_name as fee_template_name,"
			+ "br.board_unique_name as board_unique_name,br.board_unique_short_name as board_unique_short_name,ft.ac_year_id as ac_year_id,"
			+ "fsa.board_unique_id as board_unique_id,"
			+ "CASE WHEN :yearOrSem=1 THEN SUM(fsa.year1_amt) "
			+ " WHEN :yearOrSem=2 THEN SUM(fsa.year2_amt) "
			+ " WHEN :yearOrSem=3 THEN SUM(fsa.year3_amt) "
			+ " WHEN :yearOrSem=4 THEN SUM(fsa.year4_amt) "
			+ " WHEN :yearOrSem=5 THEN SUM(fsa.year5_amt) "
			+ " WHEN :yearOrSem=6 THEN SUM(fsa.year6_amt) "
			+ " WHEN :yearOrSem=7 THEN SUM(fsa.year7_amt) "
			+ " WHEN :yearOrSem=8 THEN SUM(fsa.year8_amt) "
			+ " WHEN :yearOrSem=9 THEN SUM(fsa.year9_amt) "
			+ " WHEN :yearOrSem=10 THEN SUM(fsa.year10_amt) "
			+ " WHEN :yearOrSem=11 THEN SUM(fsa.year11_amt) "
			+ " WHEN :yearOrSem=12 THEN SUM(fsa.year12_amt) "
			+ " ELSE 0 END as templateAmount "
			+ "from fee_template ft "
			+ "Inner Join fee_template_sub_amount fsa On ft.fee_template_id=fsa.fee_template_id "
			+ "Left Join board br On br.board_unique_id=fsa.board_unique_id "
			+ "where ft.ac_year_id=:acYearId And fsa.board_unique_id Is Not null and fsa.board_unique_id != 0 And fsa.active=true And ft.active=true "
			+ "GROUP BY ft.fee_template_name,fsa.board_unique_id,ft.ac_year_id,ft.fee_template_id ", nativeQuery=true)
	public List<Map<String, Object>> feeTemplateDetailsByAcademicYearAndYearSem(Integer acYearId, Integer yearOrSem);

	@Query(value = "select new map(ft.fee_template_id as id,ft.Is_paid_at_board as Is_paid_at_board,ft.lat_year_sem as lat_year_sem,"
			+ "ft.fee_template_name as fee_template_name,count(sd.fee_template_id) as countOfStudent,"
			+ "ft.ac_year_id as ac_year_id,ft.school_id as school_id,ft.program_id as program_id,"
			+ "ft.uniform_status as uniform_status,ft.laptop_status as laptop_status,"
			+ "ft.program_specialization_id as program_specialization_id,ft.currency_type_id as currency_type_id,"
			+ "ft.fee_admission_category_id as fee_admission_category_id,ft.fee_template_path as fee_template_path,"
			+ "ft.fee_admission_sub_category_id as fee_admission_sub_category_id,ft.nationality as nationality,"
			+ "ft.Is_nri as Is_nri,ft.program_type_id as program_type_id,ft.approved_by as approved_by,"
			+ "ft.approved_status as approved_status,ft.approved_date as approved_date,ft.created_by as created_by,"
			+ "ft.modified_by as modified_by,ft.created_date as created_date,ft.modified_date as modified_date,"
			+ "ft.active as active,ft.remarks as remarks,ft.created_username as created_username,"
			+ "ft.modified_username as modified_username,ft.program_specialization as program_specialization,"
			+ "ft.fee_year_total_amount as fee_year_total_amount,sc.school_name_short as school_name_short,"
			+ "pr.program_short_name as program_short_name,ac.ac_year as ac_year,ct.currency_type_name as currency_type_name,"
			+ "ft.fee_year1_amt as fee_year1_amt,ft.fee_year2_amt as fee_year2_amt,ft.fee_year3_amt as fee_year3_amt,ft.fee_year4_amt as fee_year4_amt,"
			+ "ft.fee_year5_amt as fee_year5_amt,ft.fee_year6_amt as fee_year6_amt,ft.fee_year7_amt as fee_year7_amt,"
			+ "ft.fee_year8_amt as fee_year8_amt,ft.fee_year9_amt as fee_year9_amt,ft.fee_year10_amt as fee_year10_amt,"
			+ "ft.fee_year11_amt as fee_year11_amt,ft.fee_year12_amt as fee_year12_amt,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,"
			+ "ptn.program_type_name as program_type_name,org.org_id As org_id,org.org_name As org_name,org.org_type As org_type,"
			+ "fasc.fee_admission_sub_category_name as fee_admission_sub_category_name) from FeeTemplate ft "
			+ "left join Student_Details sd on sd.fee_template_id=ft.fee_template_id "
			+ "left join Schools sc on ft.school_id=sc.school_id "
			+ "left join Organization org on org.org_id=sc.org_id "
			+ "left join Program pr on ft.program_id=pr.program_id "
			+ "left join Academic_year ac on ft.ac_year_id=ac.ac_year_id "
			+ "left join Currency_Type ct on ft.currency_type_id=ct.currency_type_id "
			+ "left join FeeAdmissionCategory fac on ft.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join ProgramType ptn on ft.program_type_id=ptn.program_type_id "
			+ "left join FeeAdmissionSubCategory fasc on ft.fee_admission_sub_category_id=fasc.fee_admission_sub_category_id "
			+ "WHERE ft.fee_template_id=?1 And ft.active = TRUE")
	public Map<String, Object> FetchFeeTemplateDetailsByFeeTemplateId(Integer fee_template_id);
	


//select ifNULL( sum(ft.year1_amt),0 ) from fee_template f left join fee_template_sub_amount ft on f.fee_template_id=ft.fee_template_id where f.fee_template_id=240 and f.is_paid_at_board=1;


//
	@Query(value="select IFNULL( sum(ft.year1_amt),0 ) from fee_template f left join fee_template_sub_amount ft on f.fee_template_id=ft.fee_template_id where f.fee_template_id=:feetemplateId and f.is_paid_at_board=1 and ft.board_unique_id is null  ",nativeQuery = true)
	Double getSwoSem1(Integer feetemplateId);
	
	@Query(value="select IFNULL( SUM( CASE WHEN :sem=2 THEN COALESCE(ft.year2_amt,0) "
			+ "WHEN :sem=3 THEN COALESCE(ft.year3_amt,0)"
			+ "WHEN :sem=4 THEN COALESCE(ft.year4_amt,0)"
			+ "WHEN :sem=5 THEN COALESCE(ft.year5_amt,0)"
			+ "WHEN :sem=6 THEN COALESCE(ft.year6_amt,0)"
			+ "WHEN :sem=7 THEN COALESCE(ft.year7_amt,0)"
			+ "WHEN :sem=8 THEN COALESCE(ft.year8_amt,0)"
			+ "WHEN :sem=9 THEN COALESCE(ft.year9_amt,0)"
			+ "WHEN :sem=10 THEN COALESCE(ft.year10_amt,0)"
			+ "WHEN :sem=11 THEN COALESCE(ft.year11_amt,0) "
			+ "WHEN :sem=12 THEN COALESCE(ft.year12_amt,0) END ),0) as totalAmount from fee_template f left join fee_template_sub_amount ft on f.fee_template_id=ft.fee_template_id where f.fee_template_id=:feetemplateId and f.is_paid_at_board=1 and ft.receive_for_all_year=0 ",nativeQuery = true)
	Double getSwoSemWise(Integer feetemplateId, Integer sem);


	@Query(value = "SELECT CONCAT(COALESCE(fee_template_name, ''), '-', COALESCE(fee_template_id, ''))" +
			"FROM fee_template ft " +
			"WHERE ft.active = TRUE", nativeQuery = true)
	List<String> getAllActiveFeeTemplate();

	@Query(value="select ft.board_unique_id as board_unique_id,br.board_unique_name as board_unique_name,IFNULL( SUM( CASE WHEN :yearOrSem=2 THEN COALESCE(ft.year2_amt,0) "
			+ "WHEN :yearOrSem=3 THEN COALESCE(ft.year3_amt,0)"
			+ "WHEN :yearOrSem=4 THEN COALESCE(ft.year4_amt,0)"
			+ "WHEN :yearOrSem=5 THEN COALESCE(ft.year5_amt,0)"
			+ "WHEN :yearOrSem=6 THEN COALESCE(ft.year6_amt,0)"
			+ "WHEN :yearOrSem=7 THEN COALESCE(ft.year7_amt,0)"
			+ "WHEN :yearOrSem=8 THEN COALESCE(ft.year8_amt,0)"
			+ "WHEN :yearOrSem=9 THEN COALESCE(ft.year9_amt,0)"
			+ "WHEN :yearOrSem=10 THEN COALESCE(ft.year10_amt,0)"
			+ "WHEN :yearOrSem=11 THEN COALESCE(ft.year11_amt,0) "
			+ "WHEN :yearOrSem=12 THEN COALESCE(ft.year12_amt,0) END ),0) as totalAmount from fee_template f " +
			"Inner join fee_template_sub_amount ft on f.fee_template_id=ft.fee_template_id " +
			"Left join board br on br.board_unique_id=ft.board_unique_id where f.fee_template_id=:feetemplateId and f.is_paid_at_board=true and ft.board_unique_id is not null ",nativeQuery = true)
	Map<String, Object> boardPaidAmount(Integer feetemplateId, Integer yearOrSem);

	@Query(value="select IFNULL( sum(ft.year1_amt),0 ) from fee_template f left join fee_template_sub_amount ft on f.fee_template_id=ft.fee_template_id where f.fee_template_id=:feetemplateId and f.is_paid_at_board=0  ",nativeQuery = true)
	Double getSwoSem1WithOutBoard(Integer feetemplateId);

	@Query(value="select IFNULL( SUM( CASE WHEN :sem=2 THEN COALESCE(ft.year2_amt,0) "
			+ "WHEN :sem=3 THEN COALESCE(ft.year3_amt,0)"
			+ "WHEN :sem=4 THEN COALESCE(ft.year4_amt,0)"
			+ "WHEN :sem=5 THEN COALESCE(ft.year5_amt,0)"
			+ "WHEN :sem=6 THEN COALESCE(ft.year6_amt,0)"
			+ "WHEN :sem=7 THEN COALESCE(ft.year7_amt,0)"
			+ "WHEN :sem=8 THEN COALESCE(ft.year8_amt,0)"
			+ "WHEN :sem=9 THEN COALESCE(ft.year9_amt,0)"
			+ "WHEN :sem=10 THEN COALESCE(ft.year10_amt,0)"
			+ "WHEN :sem=11 THEN COALESCE(ft.year11_amt,0) "
			+ "WHEN :sem=12 THEN COALESCE(ft.year12_amt,0) END ),0) as totalAmount from fee_template f left join fee_template_sub_amount ft on f.fee_template_id=ft.fee_template_id where f.fee_template_id=:feetemplateId and f.is_paid_at_board=0  ",nativeQuery = true)
	Double getSwoSemWiseWithOutBoard(Integer feetemplateId, Integer sem);

	@Query(value = "select IFNULL( SUM( CASE WHEN :sem=2 THEN COALESCE(ft.year2_amt,0) "
		+	" WHEN :sem=3 THEN COALESCE(ft.year3_amt,0) "
		+	" WHEN :sem=4 THEN COALESCE(ft.year4_amt,0) "
		+	" WHEN :sem=5 THEN COALESCE(ft.year5_amt,0) "
		+	" WHEN :sem=6 THEN COALESCE(ft.year6_amt,0) "
		+	" WHEN :sem=7 THEN COALESCE(ft.year7_amt,0) "
		+	" WHEN :sem=8 THEN COALESCE(ft.year8_amt,0) "
		+	" WHEN :sem=9 THEN COALESCE(ft.year9_amt,0) "
		+	" WHEN :sem=10 THEN COALESCE(ft.year10_amt,0) "
		+	" WHEN :sem=11 THEN COALESCE(ft.year11_amt,0) "
		+	" WHEN :sem=12 THEN COALESCE(ft.year12_amt,0) END ),0) as totalAmount from fee_template f left join fee_template_sub_amount ft on f.fee_template_id=ft.fee_template_id where f.fee_template_id=:feetemplateId and f.is_paid_at_board=1 and ft.board_unique_id is null ", nativeQuery = true)
	Double getSwoSemWiseForLateralYear(Integer feetemplateId, Integer sem);

}