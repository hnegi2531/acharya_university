package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.dto.StudentWiseDueReport;
import com.au.model.AcerpAmount;
import com.au.model.AcerpAmount;

@Transactional
@Repository
public interface AcerpAmountRepository extends JpaRepository<AcerpAmount, Integer> {
	
	
	@Query(value = "select twp from AcerpAmount twp where twp.active=true")
	public List<AcerpAmount> findAll1();

    @Query("SELECT new map(twp.acerpAmountId as id, twp.auid as auid, twp.studentId as studentId, "
            + "twp.active as active, twp.remarks as remarks,twp.acerpAmountAttachPath as acerpAmountAttachPath,twp.type as type,"
            + "twp.paidYear1 as paidYear1,twp.paidYear2 as paidYear2, twp.paidYear3 as paidYear3, "
            + "twp.paidYear4 as paidYear4,twp.paidYear5 as paidYear5, twp.paidYear6 as paidYear6,"
            + "twp.paidYear7 as paidYear7,twp.paidYear8 as paidYear8, twp.paidYear9 as paidYear9,"
            + "twp.paidYear10 as paidYear10,twp.paidYear11 as paidYear11, twp.paidYear12 as paidYear12,"
            + "twp.createdBy as createdBy,twp.modifiedBy as modifiedBy, twp.createdDate as createdDate, "
            + "twp.modifiedDate as modifiedDate, twp.createdUsername as createdUsername, twp.modifiedUsername as modifiedUsername,"
            + "sc.school_name as school_name, sc.school_name_short as school_name_short,"
            + "pt.program_type_id as program_type_id,pt.program_type_name as program_type_name,pt.program_type_code as program_type_code,"
            + "pra.number_of_semester as number_of_semester, pra.number_of_years as number_of_years,"
            + "ac.ac_year as ac_year, sd.student_name as studentName) "
            + "FROM AcerpAmount twp "
            + "left join Student_Details sd on sd.student_id=twp.studentId "
            + "Inner join ProgramAssigment pra on sd.program_assignment_id=pra.program_assignment_id "
            + "left join ProgramType pt on pra.program_type_id=pt.program_type_id "
            + "left join Schools sc on sc.school_id=sd.school_id "
            + "left join Academic_year ac on ac.ac_year_id=sd.ac_year_id "
		+ "Where CONCAT(IfNull(twp.auid,''),'',IfNull(twp.studentId,''),'',IfNull(twp.createdDate,''),'',IfNull(twp.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

    @Query("SELECT new map(twp.acerpAmountId as id, twp.auid as auid, twp.studentId as studentId, "
            + "twp.active as active, twp.remarks as remarks,twp.acerpAmountAttachPath as acerpAmountAttachPath,twp.type as type,"
            + "twp.paidYear1 as paidYear1,twp.paidYear2 as paidYear2, twp.paidYear3 as paidYear3, "
            + "twp.paidYear4 as paidYear4,twp.paidYear5 as paidYear5, twp.paidYear6 as paidYear6,"
            + "twp.paidYear7 as paidYear7,twp.paidYear8 as paidYear8, twp.paidYear9 as paidYear9,"
            + "twp.paidYear10 as paidYear10,twp.paidYear11 as paidYear11, twp.paidYear12 as paidYear12,"
            + "twp.createdBy as createdBy,twp.modifiedBy as modifiedBy, twp.createdDate as createdDate, "
            + "twp.modifiedDate as modifiedDate, twp.createdUsername as createdUsername, twp.modifiedUsername as modifiedUsername,"
            + "sc.school_name as school_name, sc.school_name_short as school_name_short,"
            + "pt.program_type_id as program_type_id,pt.program_type_name as program_type_name,pt.program_type_code as program_type_code,"
            + "pra.number_of_semester as number_of_semester, pra.number_of_years as number_of_years,"
            + "ac.ac_year as ac_year, sd.student_name as studentName) "
            + "FROM AcerpAmount twp "
            + "left join Student_Details sd on sd.student_id=twp.studentId "
            + "Inner join ProgramAssigment pra on sd.program_assignment_id=pra.program_assignment_id "
            + "left join ProgramType pt on pra.program_type_id=pt.program_type_id "
            + "left join Schools sc on sc.school_id=sd.school_id "
            + "left join Academic_year ac on ac.ac_year_id=sd.ac_year_id ")
	Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update AcerpAmount twp set twp.active=false where twp.acerpAmountId=?1")
	public void updateAcerpAmount(Integer acerpAmountId);

	@Modifying
	@Query(value = "update AcerpAmount twp set twp.active=true where twp.acerpAmountId=?1")
	public void updateAcerpAmount1(Integer acerpAmountId);

	@Query(value = "select am from AcerpAmount am where am.auid=?1 And am.type=?2 and am.active=true")
	public AcerpAmount getAcerpAmountByAuid(String auid, String type);

//	@Modifying
//	@Query(value = "update AcerpAmount am set am.amount=?2,am.remarks=?3, am.modifiedBy=?4, "
//			+ "am.modifiedUsername=?5 where am.acerpAmountId=?1 and am.active=true")
//	public Object updateAcerpAmountByacerpAmountId(Integer acerpAmountId, Double amount, String remarks,
//			Integer modifiedBy, String modifiedUsername);

	@Query(value = "select twp from AcerpAmount twp where twp.studentId=?1 And type=?2 And twp.active=true")
	public AcerpAmount getDataByStudentId(Integer studentId, String type);

	@Modifying
	@Query(value = "update AcerpAmount am set am.acerpAmountAttachPath=?2 where am.acerpAmountId=?1")
	public void updatePath(Integer acerpAmountId, String t2);
	
	@Query(value = "select twp from AcerpAmount twp where twp.studentId=?1 and twp.active=true ")
	public List<AcerpAmount> getDataByStudentIdForWaiver(Integer studentId);

	@Query(value = "select twp from AcerpAmount twp where twp.studentId=?1 and twp.active=true and twp.type='Fee Paid'  ")
	public AcerpAmount getDataByStudentIdForFeePaid(Integer studentId);

	

	@Query(value = "select Count(*) from acerp_amount where auid=?1 And type=?2 and active=true",nativeQuery = true)
	public int checkAuidWithFeeTypeIsAlreadyPresentOrNot(String auid, String type);

	@Query(value = "select sum(acerp.paid_year1) as paidYear1, sum(acerp.paid_year2) as paidYear2, sum(acerp.paid_year3) as paidYear3, "
			+ "sum(acerp.paid_year4) as paidYear4, sum(acerp.paid_year5) as paidYear5, sum(acerp.paid_year6) as paidYear6,"
			+ "sum(acerp.paid_year7) as paidYear7,sum(acerp.paid_year8) as paidYear8, sum(acerp.paid_year9) as paidYear9, "
			+ "sum(acerp.paid_year10) as paidYear10, sum(acerp.paid_year11) as paidYear11, sum(acerp.paid_year12) as paidYear12 "
			+ "from acerp_amount acerp where acerp.student_id=?1 and acerp.active=true",nativeQuery = true)
	public Map<String, Object> getAcerpAmountData(Integer student_id);
}
