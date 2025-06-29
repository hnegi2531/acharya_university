package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.FeeTemplateDTO;
import com.au.dto.OtherFeeDetailsSemWiseDTO;
import com.au.dto.OtherFeeTemplateForStudentDTO;
import com.au.dto.UniformDetailsDTO;
import com.au.model.OtherFeeDetails;


@Transactional
@Repository
public interface OtherFeeDetailsRepository extends JpaRepository<OtherFeeDetails, Integer> {

	@Query(value = " select ofd from OtherFeeDetails ofd  left join OtherFeeTemplate oft on oft.otherFeeTemplateId=ofd.templateId "
			+ "where ofd.templateId=:otherFeeTemplateId and oft.feetype=:feeType and ofd.active=true ")
	List<OtherFeeDetails> getOtherFeeDetails(Integer otherFeeTemplateId, String feeType);

	@Query(value = " select ofd from OtherFeeDetails ofd  left join OtherFeeTemplate oft on oft.otherFeeTemplateId=ofd.templateId where ofd.templateId=:otherFeeTemplateId and ofd.active=true  ")
	List<OtherFeeDetails> getOtherFeeDetailsById(Integer otherFeeTemplateId);

	@Query(value = " select ofd from OtherFeeDetails ofd  left join OtherFeeTemplate oft on oft.otherFeeTemplateId=ofd.templateId where ofd.templateId=:otherFeeTemplateId and ofd.active=false  ")
	List<OtherFeeDetails> getOtherFeeDetailsDeactiveRecordsById(Integer otherFeeTemplateId);

	@Query(value = "SELECT  " + "    oft.other_fee_template_id AS otherFeeTemplateId, " + "    oft.feetype AS feetype, "
			+ "    oft.uniform_number AS uniformNumber, " + "    ofd.template_id AS templateId, " + "    SUM( "
			+ "        CASE  " + "            WHEN :semester = 1 THEN ofd.sem1 "
			+ "            WHEN :semester = 2 THEN ofd.sem2 " + "            WHEN :semester = 3 THEN ofd.sem3 "
			+ "            WHEN :semester = 4 THEN ofd.sem4 " + "            WHEN :semester = 5 THEN ofd.sem5 "
			+ "            WHEN :semester = 6 THEN ofd.sem6 " + "            WHEN :semester = 7 THEN ofd.sem7 "
			+ "            WHEN :semester = 8 THEN ofd.sem8 " + "            WHEN :semester = 9 THEN ofd.sem9 "
			+ "            WHEN :semester = 10 THEN ofd.sem10 " + "            WHEN :semester = 11 THEN ofd.sem11 "
			+ "            WHEN :semester = 12 THEN ofd.sem12 " + "            ELSE 0 " + "        END "
			+ "    ) AS totalSemAmount " + "FROM  " + "    other_fee_template oft " + "LEFT JOIN  "
			+ "    other_fee_details ofd  " + "    ON ofd.template_id = oft.other_fee_template_id    " + "WHERE  "
			+ "    oft.school_id=:schoolId  " + "    AND oft.program_id=:programId  "
			+ "    AND oft.program_specialization_id=:programSpecilizationId " + "    AND oft.ac_year_id=:acYearId "
			+ "GROUP BY  " + "    oft.other_fee_template_id,  " + "    ofd.template_id ", nativeQuery = true)
	Map<String, Object> getTotalOfSemAmount(@Param("semester") Integer semester, @Param("schoolId") Integer schoolId,
			@Param("programId") Integer programId, @Param("programSpecilizationId") Integer programSpecilizationId,
			@Param("acYearId") Integer acYearId);

	@Query(value = " select new com.au.dto.OtherFeeTemplateForStudentDTO( SUM(ofd.sem1),"
			+ " SUM(ofd.sem2), SUM(ofd.sem3),SUM(ofd.sem4), SUM(ofd.sem5),SUM(ofd.sem6),"
			+ "SUM(ofd.sem7),SUM(ofd.sem8),SUM(ofd.sem9)," + "SUM(ofd.sem10),SUM(ofd.sem11),SUM(ofd.sem12),"
			+ " oft.feetype ) from  OtherFeeTemplate oft "
			+ "left join OtherFeeDetails ofd on ofd.templateId=oft.otherFeeTemplateId "
			+ "where oft.schoolId=:schoolId " + "and oft.acYearId=:acYearId " + "and oft.programId=:programId "
			+ "and oft.programSpecializationId=:programSpecilizationId and oft.active=true and ofd.active=true "
			+ "and oft.feetype='Add-on Programme Fee' group by ofd.templateId ", nativeQuery = false)
	OtherFeeTemplateForStudentDTO getAddOnProgramFeeDetails(Integer schoolId, Integer acYearId, Integer programId,
			Integer programSpecilizationId);

	@Query(value = " select new com.au.dto.OtherFeeTemplateForStudentDTO( SUM(ofd.sem1),"
			+ " SUM(ofd.sem2), SUM(ofd.sem3),SUM(ofd.sem4), SUM(ofd.sem5),SUM(ofd.sem6),"
			+ "SUM(ofd.sem7),SUM(ofd.sem8),SUM(ofd.sem9)," + "SUM(ofd.sem10),SUM(ofd.sem11),SUM(ofd.sem12),"
			+ " oft.feetype ) from  OtherFeeTemplate oft "
			+ "left join OtherFeeDetails ofd on ofd.templateId=oft.otherFeeTemplateId "
			+ "where oft.schoolId=:schoolId " + "and oft.acYearId=:acYearId " + "and oft.programId=:programId "
			+ "and oft.programSpecializationId=:programSpecilizationId  and oft.active=true and ofd.active=true "
			+ "and oft.feetype='Uniform And Stationery Fee' group by ofd.templateId ", nativeQuery = false)
	OtherFeeTemplateForStudentDTO getUniformFeeDetails(Integer schoolId, Integer acYearId, Integer programId,
			Integer programSpecilizationId);


	@Query(value = " select ofd.other_fee_details_id as id,ofd.sem1 as sem1,ofd.sem2 as sem2,ofd.sem3 as sem3,ofd.sem4 as sem4,ofd.sem5 as sem5,"
			+ "ofd.sem6 as sem6,ofd.sem7 as sem7,ofd.sem8 as sem8,ofd.sem9 As sem9,ofd.sem10 As sem10,ofd.sem11 As sem11,ofd.sem12 As sem12,"
			+ "ofd.year1 As year1,ofd.year2 As year2,ofd.year3 As year3,ofd.year4 As year4,"
			+ "ofd.template_id as template_id,ofd.total as total,ofd.voucher_head_id as voucher_head_id,"
			+ "oft.feetype as feetype,oft.other_fee_template_id as other_fee_template_id,oft.ac_year_id as ac_year_id,oft.program_id as program_id,"
			+ "oft.program_specialization_id as program_specialization_id,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "oft.school_id as school_id,oft.uniform_number as uniform_number,p.program_name as program_name,p.program_short_name as program_short_name,p.program_code as program_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "vhn.voucher_head As voucher_head,vhn.voucher_head_short_name As voucher_head_short_name,"
			+ "ay.ac_year as ac_year  From other_fee_details ofd  "
			+ "left join other_fee_template oft on oft.other_fee_template_id=ofd.template_id "
			+ "left join program p on oft.program_id=p.program_id "
			+ "left join program_specialization ps on oft.program_specialization_id=ps.program_specialization_id "
			+ "left join schools sc on oft.school_id=sc.school_id "
			+ "left join academic_year ay on oft.ac_year_id=ay.ac_year_id "
			+ "left join voucher_head_new vhn on ofd.voucher_head_id=vhn.voucher_head_new_id "
			+ "where oft.school_id=?1 And oft.ac_year_id=?2 And oft.program_id=?3 And oft.program_specialization_id=?4 And oft.active=true ", nativeQuery = true)
	List<Map<String, Object>> getOtherFeeDetailsData(Integer schoolId, Integer acYearId, Integer programId, Integer ps);
	
	@Query(value = " select ofd.sem1 as sem1,ofd.sem2 as sem2,ofd.sem3 as sem3,ofd.sem4 as sem4,ofd.sem5 as sem5,"
			+ "ofd.sem6 as sem6,ofd.sem7 as sem7,ofd.sem8 as sem8,ofd.sem9 As sem9,ofd.sem10 As sem10,ofd.sem11 As sem11,ofd.sem12 As sem12,"
			+ "ofd.total as total,ofd.voucher_head_id as voucher_head_id,"
			+ "oft.feetype as feetype,"
			+ "oft.uniform_number as uniform_number From other_fee_details ofd  "
			+ "left join other_fee_template oft on oft.other_fee_template_id=ofd.template_id "
			+ "left join voucher_head_new vhn on ofd.voucher_head_id=vhn.voucher_head_new_id "
			+ "where oft.school_id=?1 And oft.ac_year_id=?2 And oft.program_id=?3 And oft.program_specialization_id=?4 And oft.active=true ", nativeQuery = true)
	Map<String, Object> getOtherFeeDetailsForStudentLedger(Integer schoolId, Integer acYearId, Integer programId,
			Integer ps);

	@Query(value = " select ofd.other_fee_details_id as id,ofd.sem1 as sem1,ofd.sem2 as sem2,ofd.sem3 as sem3,ofd.sem4 as sem4,ofd.sem5 as sem5,"
			+ "ofd.sem6 as sem6,ofd.sem7 as sem7,ofd.sem8 as sem8,ofd.sem9 As sem9,ofd.sem10 As sem10,ofd.sem11 As sem11,ofd.sem12 As sem12,"
			+ "ofd.year1 As year1,ofd.year2 As year2,ofd.year3 As year3,ofd.year4 As year4,"
			+ "ofd.template_id as template_id,ofd.total as total,ofd.voucher_head_id as voucher_head_id,"
			+ "oft.feetype as feetype,oft.other_fee_template_id as other_fee_template_id,oft.ac_year_id as ac_year_id,oft.program_id as program_id,"
			+ "oft.program_specialization_id as program_specialization_id,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "oft.school_id as school_id,oft.uniform_number as uniform_number,p.program_name as program_name,p.program_short_name as program_short_name,p.program_code as program_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "vhn.voucher_head As voucher_head,vhn.voucher_head_short_name As voucher_head_short_name,"
			+ "ay.ac_year as ac_year  From other_fee_details ofd  "
			+ "left join other_fee_template oft on oft.other_fee_template_id=ofd.template_id "
			+ "left join program p on oft.program_id=p.program_id "
			+ "left join program_specialization ps on oft.program_specialization_id=ps.program_specialization_id "
			+ "left join schools sc on oft.school_id=sc.school_id "
			+ "left join academic_year ay on oft.ac_year_id=ay.ac_year_id "
			+ "left join voucher_head_new vhn on ofd.voucher_head_id=vhn.voucher_head_new_id "
			+ "where oft.fee_template_id=?1 And ofd.active=true ", nativeQuery = true)
	List<Map<String, Object>> getOtherFeeDetailsData1(Integer fee_template_id);

//	@Query(value = " select sum(ofd.total)  from  OtherFeeTemplate oft "
//			+ "	left join OtherFeeDetails ofd on ofd.templateId=oft.otherFeeTemplateId where oft.schoolId=:schoolId and oft.feetype='Add-on Programme Fee' group by oft.schoolId  ")

	@Query(value="select SUM(st.totalAddOn) from StudentDues st where st.schoolId=:schoolId group by st.schoolId")
	public Double getSumOfAddOn(Integer schoolId);

	@Query(value = " select sum(ofd.sem1) as sem1,sum(ofd.sem2) as sem2,sum(ofd.sem3) as sem3,sum(ofd.sem4) as sem4,sum(ofd.sem5) as sem5,sum(ofd.sem6) as sem6,sum(ofd.sem7) as sem7,sum(ofd.sem8) as sem8,sum(ofd.sem9) as sem9,sum(ofd.sem10) as sem10,sum(ofd.sem11) as sem11,sum(ofd.sem12) as sem12  from  OtherFeeTemplate oft "
			+ "		left join OtherFeeDetails ofd on ofd.templateId=oft.otherFeeTemplateId where oft.programSpecializationId=:programSpecializationId and oft.feetype in ('Add-on Programme Fee','Uniform And Stationery Fee') group by oft.programSpecializationId   ", nativeQuery = false)
	Map<String, Object> getAddOnProgramFeeDetailsSemesterWise(Integer programSpecializationId);
	
	@Query(value = " select new com.au.dto.OtherFeeTemplateForStudentDTO(" + " SUM(ofd.sem1),"
			+ " SUM(ofd.sem2), SUM(ofd.sem3),SUM(ofd.sem4), SUM(ofd.sem5),SUM(ofd.sem6),"
			+ "SUM(ofd.sem7),SUM(ofd.sem8),SUM(ofd.sem9)," + "SUM(ofd.sem10),SUM(ofd.sem11),SUM(ofd.sem12),"
			+ " oft.feetype ) from  OtherFeeTemplate oft "
			+ "left join OtherFeeDetails ofd on ofd.templateId=oft.otherFeeTemplateId "
			+ "where oft.schoolId=:schoolId " + "and oft.acYearId=:acYearId " + "and oft.programId=:programId "
			+ "and oft.programSpecializationId=:programSpecilizationId "
			+ " group by ofd.templateId ", nativeQuery = false)
	OtherFeeTemplateForStudentDTO getOtherFeeDetailsSemAmountSum(Integer schoolId, Integer acYearId, Integer programId,
			Integer programSpecilizationId);
	
	@Query(value = "SELECT  " + "    oft.other_fee_template_id AS otherFeeTemplateId, " + "    oft.feetype AS feetype, "
			+ "    oft.uniform_number AS uniformNumber, " + "    ofd.template_id AS templateId, " + "    SUM( "
			+ "        CASE  " + "            WHEN :semester = 1 THEN ofd.sem1 "
			+ "            WHEN :semester = 2 THEN ofd.sem2 " + "            WHEN :semester = 3 THEN ofd.sem3 "
			+ "            WHEN :semester = 4 THEN ofd.sem4 " + "            WHEN :semester = 5 THEN ofd.sem5 "
			+ "            WHEN :semester = 6 THEN ofd.sem6 " + "            WHEN :semester = 7 THEN ofd.sem7 "
			+ "            WHEN :semester = 8 THEN ofd.sem8 " + "            WHEN :semester = 9 THEN ofd.sem9 "
			+ "            WHEN :semester = 10 THEN ofd.sem10 " + "            WHEN :semester = 11 THEN ofd.sem11 "
			+ "            WHEN :semester = 12 THEN ofd.sem12 " + "            ELSE 0 " + "        END "
			+ "    ) AS totalSemAmount " + "FROM  " + "    other_fee_template oft " + "LEFT JOIN  "
			+ "    other_fee_details ofd  " + "    ON ofd.template_id = oft.other_fee_template_id    " + "WHERE  "
			+ "    oft.school_id=:schoolId  " + "    AND oft.program_id=:programId  "
			+ "    AND oft.program_specialization_id=:programSpecilizationId " + "    AND oft.ac_year_id=:acYearId AND   oft.feetype='Add-on Programme Fee' "
			+ "GROUP BY  " + "    oft.other_fee_template_id,  " + "    ofd.template_id ", nativeQuery = true)
	Map<String, Object> getTotalOfSemAmountProgrammeFee(@Param("semester") Integer semester, @Param("schoolId") Integer schoolId,
			@Param("programId") Integer programId, @Param("programSpecilizationId") Integer programSpecilizationId,
			@Param("acYearId") Integer acYearId);

	@Query(value = "SELECT  " + "    oft.other_fee_template_id AS otherFeeTemplateId, " + "    oft.feetype AS feetype, "
			+ "    oft.uniform_number AS uniformNumber, " + "    ofd.template_id AS templateId, " + "    SUM( "
			+ "        CASE  " + "            WHEN :semester = 1 THEN ofd.sem1 "
			+ "            WHEN :semester = 2 THEN ofd.sem2 " + "            WHEN :semester = 3 THEN ofd.sem3 "
			+ "            WHEN :semester = 4 THEN ofd.sem4 " + "            WHEN :semester = 5 THEN ofd.sem5 "
			+ "            WHEN :semester = 6 THEN ofd.sem6 " + "            WHEN :semester = 7 THEN ofd.sem7 "
			+ "            WHEN :semester = 8 THEN ofd.sem8 " + "            WHEN :semester = 9 THEN ofd.sem9 "
			+ "            WHEN :semester = 10 THEN ofd.sem10 " + "            WHEN :semester = 11 THEN ofd.sem11 "
			+ "            WHEN :semester = 12 THEN ofd.sem12 " + "            ELSE 0 " + "        END "
			+ "    ) AS totalSemAmount " + "FROM  " + "    other_fee_template oft " + "LEFT JOIN  "
			+ "    other_fee_details ofd  " + "    ON ofd.template_id = oft.other_fee_template_id    " + "WHERE  "
			+ "    oft.school_id=:schoolId  " + "    AND oft.program_id=:programId  "
			+ "    AND oft.program_specialization_id=:programSpecilizationId " + "    AND oft.ac_year_id=:acYearId  AND   oft.feetype='Uniform And Stationery Fee' "
			+ "GROUP BY  " + "    oft.other_fee_template_id,  " + "    ofd.template_id ", nativeQuery = true)
	Map<String, Object> getTotalOfSemAmountUniformStationary(@Param("semester") Integer semester, @Param("schoolId") Integer schoolId,
			@Param("programId") Integer programId, @Param("programSpecilizationId") Integer programSpecilizationId,
			@Param("acYearId") Integer acYearId);

	@Query(value = " select new com.au.dto.OtherFeeTemplateForStudentDTO( SUM(ofd.sem1),"
			+ " SUM(ofd.sem2), SUM(ofd.sem3),SUM(ofd.sem4), SUM(ofd.sem5),SUM(ofd.sem6),"
			+ "SUM(ofd.sem7),SUM(ofd.sem8),SUM(ofd.sem9)," + "SUM(ofd.sem10),SUM(ofd.sem11),SUM(ofd.sem12),"
			+ " oft.feetype ) from  OtherFeeTemplate oft "
			+ "left join OtherFeeDetails ofd on ofd.templateId=oft.otherFeeTemplateId "
			+ "where oft.schoolId=:schoolId " + "and oft.acYearId=:acYearId " + "and oft.programId=:programId "
			+ "and oft.fee_template_id=:feeTemplateId "
			+ "and oft.feetype='Add-on Programme Fee' group by ofd.templateId ", nativeQuery = false)
	OtherFeeTemplateForStudentDTO getAddOnProgramFeeDetailsByFeeTemplateId(Integer schoolId, Integer acYearId, Integer programId,
			Integer feeTemplateId);

	@Modifying
	@Query(value = "update OtherFeeDetails ofd set ofd.active=false where ofd.otherFeeDetailsId=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update OtherFeeDetails ofd set ofd.active=true where ofd.otherFeeDetailsId=?1")
	public void activate(Integer id);

	
	@Query(value = " select ofd.other_fee_details_id as id,ofd.sem1 as sem1,ofd.sem2 as sem2,ofd.sem3 as sem3,ofd.sem4 as sem4,ofd.sem5 as sem5,"
			+ "ofd.sem6 as sem6,ofd.sem7 as sem7,ofd.sem8 as sem8,ofd.template_id as template_id,ofd.total as total,ofd.voucher_head_id as voucher_head_id,"
			+ "ofd.sem9 as sem9,ofd.sem10 as sem10,ofd.sem11 as sem11,ofd.sem12 as sem12,"
			+ "oft.feetype as feetype,oft.other_fee_template_id as other_fee_template_id,oft.ac_year_id as ac_year_id,oft.program_id as program_id,"
			+ "oft.program_specialization_id as program_specialization_id,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "oft.school_id as school_id,oft.uniform_number as uniform_number,p.program_name as program_name,p.program_short_name as program_short_name,p.program_code as program_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "vhn.voucher_head As voucher_head,vhn.voucher_head_short_name As voucher_head_short_name,"
			+ "ay.ac_year as ac_year  From other_fee_details ofd  "
			+ "left join other_fee_template oft on oft.other_fee_template_id=ofd.template_id "
			+ "left join program p on oft.program_id=p.program_id "
			+ "left join program_specialization ps on oft.program_specialization_id=ps.program_specialization_id "
			+ "left join schools sc on oft.school_id=sc.school_id "
			+ "left join academic_year ay on oft.ac_year_id=ay.ac_year_id "
			+ "left join voucher_head_new vhn on ofd.voucher_head_id=vhn.voucher_head_new_id "
			+ "where oft.fee_template_id=?1 And ofd.active=true ", nativeQuery = true)
	public List<Map<String, Object>> fetchAddOnDetails(Integer fee_template_id);

	
	@Query(value = " select ofd.other_fee_details_id as id,ofd.sem1 as sem1,ofd.sem2 as sem2,ofd.sem3 as sem3,ofd.sem4 as sem4,ofd.sem5 as sem5,"
			+ "ofd.sem6 as sem6,ofd.sem7 as sem7,ofd.sem8 as sem8,ofd.template_id as template_id,ofd.total as total,ofd.voucher_head_id as voucher_head_id,"
			+ "oft.feetype as feetype,oft.other_fee_template_id as other_fee_template_id,oft.ac_year_id as ac_year_id,oft.program_id as program_id,"
			+ "oft.program_specialization_id as program_specialization_id,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "oft.school_id as school_id,oft.uniform_number as uniform_number,p.program_name as program_name,p.program_short_name as program_short_name,p.program_code as program_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "vhn.voucher_head As voucher_head,vhn.voucher_head_short_name As voucher_head_short_name,"
			+ "ay.ac_year as ac_year  From other_fee_details ofd  "
			+ "left join other_fee_template oft on oft.other_fee_template_id=ofd.template_id "
			+ "left join program p on oft.program_id=p.program_id "
			+ "left join program_specialization ps on oft.program_specialization_id=ps.program_specialization_id "
			+ "left join schools sc on oft.school_id=sc.school_id "
			+ "left join academic_year ay on oft.ac_year_id=ay.ac_year_id "
			+ "left join voucher_head_new vhn on ofd.voucher_head_id=vhn.voucher_head_new_id "
			+ "where oft.program_specialization_id=?1 And oft.ac_year_id=?2 And oft.school_id=?3 And ofd.active=true ", nativeQuery = true)
	List<Map<String, Object>> fetchUniformDetails(Integer psi, Integer academicYear, Integer schoolId);

	
	@Query(value = "select sum(ufd.amount) from uniform_fee_details ufd where ufd.paid_year=?1 and ufd.student_id=?2 And ufd.payment_type='Uniform Fee Package' group by ufd.student_id" , nativeQuery = true)
	public Float getUniformAndStationaryPaid(Integer paid_year, Integer student_id);

	@Query(value = " select new com.au.dto.UniformDetailsDTO( SUM(ofd.sem1),"
			+ " SUM(ofd.sem2), SUM(ofd.sem3),SUM(ofd.sem4), SUM(ofd.sem5),SUM(ofd.sem6),"
			+ "SUM(ofd.sem7),SUM(ofd.sem8),SUM(ofd.sem9)," + "SUM(ofd.sem10),SUM(ofd.sem11),SUM(ofd.sem12),"
			+ " oft.uniformNumber, ofd.templateId ) from  OtherFeeTemplate oft "
			+ "left join OtherFeeDetails ofd on ofd.templateId=oft.otherFeeTemplateId "
			+ "where oft.active=true and ofd.active=true "
			+ "and oft.feetype='Uniform And Stationery Fee' group by ofd.templateId ", nativeQuery = false)
	List<UniformDetailsDTO> getUniformFeeDetails();

	@Query(value=" select oft.otherFeeTemplateId from  OtherFeeTemplate oft where oft.acYearId=:ac_year_id and oft.programId=:program_id "
			+ "and oft.programSpecializationId=:program_specialization_id  and oft.schoolId=:school_id  and oft.active=true  "
			+ " and oft.feetype='Uniform And Stationery Fee' " )
	Integer getUniformId(Integer ac_year_id, Integer program_id, Integer program_specialization_id, Integer school_id);

	
	@Query(value="select o.uniform_number as unifrom_name, count(s.student_id) as no_admissions from other_fee_template o left join student_details s on s.ac_year_id=o.ac_year_id "
			+ "	and s.program_id=o.program_id and s.program_specialization_id=o.program_specialization_id and  "
			+ "s.school_id=o.school_id where o.feetype='Uniform And Stationery Fee' group by  o.uniform_number ",nativeQuery = true)
	List<Map<String, Object>> getUniformAdmissionCount();

}
