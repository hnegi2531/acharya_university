package com.au.repository;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Map;
import com.au.model.Readmission;
import com.au.model.Readmission;

@Transactional
@Repository
public interface ReadmissionRepository extends JpaRepository<Readmission, Integer> {
	
	
	@Query(value = "select re from Readmission re where re.active=true")
	public List<Readmission> findAll1();
	
    @Query("SELECT new map(re.readmissionId as id, re.oldAuid as auid, re.voucherHeadNewId as voucherHeadNewId,"
    		+ "re.feeTemplateId as feeTemplateId, re.acYearId as acYearId, re.semOrYear as semOrYear, "
    		+ "re.totalAmount as totalAmount, re.type as type, re.balance as balance, re.oldStudentId as studentId, "
    		+ "re.remarks as remarks, re.active as active, "
            + "re.createdBy as createdBy,re.modifiedBy as modifiedBy, re.createdDate as createdDate, "
            + "re.modifiedDate as modifiedDate, re.createdUsername as createdUsername, re.modifiedUsername as modifiedUsername,"
            + "sc.school_name as school_name, sc.school_name_short as school_name_short,"
            + "ac.ac_year as ac_year, ft.fee_template_name as fee_template_name,"
            + "vhn.voucher_head as voucher_head, vhn.voucher_head_short_name as voucher_head_short_name) "
            + "FROM Readmission re "
            + "left join Student_Details sd on sd.student_id=re.oldStudentId "
            + "left join Schools sc on sc.school_id=sd.school_id "
            + "left join Academic_year ac on ac.ac_year_id=sd.ac_year_id "
            + "left join FeeTemplate ft on ft.fee_template_id=re.feeTemplateId "
            + "left join VoucherHeadNew vhn on vhn.voucher_head_new_id=re.voucherHeadNewId "
		+ "Where CONCAT(IfNull(re.oldAuid,''),'',IfNull(re.voucherHeadNewId,''),'',IfNull(re.createdDate,''),'',IfNull(re.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

    @Query("SELECT new map(re.readmissionId as id, re.oldAuid as auid, re.voucherHeadNewId as voucherHeadNewId,"
    		+ "re.feeTemplateId as feeTemplateId, re.acYearId as acYearId, re.semOrYear as semOrYear, "
    		+ "re.totalAmount as totalAmount, re.type as type, re.balance as balance,  re.oldStudentId as studentId,"
    		+ "re.remarks as remarks, re.active as active, "
            + "re.createdBy as createdBy,re.modifiedBy as modifiedBy, re.createdDate as createdDate, "
            + "re.modifiedDate as modifiedDate, re.createdUsername as createdUsername, re.modifiedUsername as modifiedUsername,"
            + "sc.school_name as school_name, sc.school_name_short as school_name_short,"
            + "ac.ac_year as ac_year, ft.fee_template_name as fee_template_name,"
            + "vhn.voucher_head as voucher_head, vhn.voucher_head_short_name as voucher_head_short_name) "
            + "FROM Readmission re "
            + "left join Student_Details sd on sd.student_id=re.oldStudentId "
            + "left join Schools sc on sc.school_id=sd.school_id "
            + "left join Academic_year ac on ac.ac_year_id=sd.ac_year_id "
            + "left join FeeTemplate ft on ft.fee_template_id=re.feeTemplateId "
            + "left join VoucherHeadNew vhn on vhn.voucher_head_new_id=re.voucherHeadNewId ")
	Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update Readmission re set re.active=false where re.readmissionId=?1")
	public void updateReadmission(Integer readmissionId);

	@Modifying
	@Query(value = "update Readmission re set re.active=true where re.readmissionId=?1")
	public void updateReadmission1(Integer readmissionId);

	@Query(value = "select CASE WHEN :semOrYear=1 THEN sd.s1due "
			+ " WHEN :semOrYear=2 THEN sd.s2due "
			+ " WHEN :semOrYear=3 THEN sd.s3due "
			+ " WHEN :semOrYear=4 THEN sd.s4due "
			+ " WHEN :semOrYear=5 THEN sd.s5due "
			+ " WHEN :semOrYear=6 THEN sd.s6due "
			+ " WHEN :semOrYear=7 THEN sd.s7due "
			+ " WHEN :semOrYear=8 THEN sd.s8due "
			+ " WHEN :semOrYear=9 THEN sd.s9due "
			+ " WHEN :semOrYear=10 THEN sd.s10due "
			+ " WHEN :semOrYear=11 THEN sd.s11due "
			+ " WHEN :semOrYear=12 THEN sd.s12due "
			+ " ELSE 0 END as dueAmount "
			+ " from StudentDues sd where sd.studentId=:studentId")
	public Float checkDuesClearOrNot(Integer semOrYear , Integer studentId);

	@Query(value = "select re from Readmission re where re.newStudentId=?1 and re.active=true")
	public Readmission getSemOrYearReadmitted(Integer student_id);

	@Query(value = "select new map (re.readmissionId as readmissionId," +
			"    re.oldAuid as oldAuid," +
			"    re.voucherHeadNewId as voucherHeadNewId," +
			"    re.feeTemplateId as feeTemplateId," +
			"    re.acYearId as acYearId," +
			"    re.semOrYear as semOrYear," +
			"    re.totalAmount as totalAmount," +
			"    re.type as type," +
			"    re.balance as balance," +
			"    re.oldStudentId as oldStudentId," +
			"    re.remarks as remarks," +
			"    re.active as active," +
			"    re.newStudentId as newStudentId," +
			"    re.newAuid as newAuid," +
			"    re.createdBy as createdBy," +
			"    re.modifiedBy as modifiedBy," +
			"    re.createdDate as createdDate," +
			"    re.modifiedDate as modifiedDate," +
			"    re.createdUsername as createdUsername," +
			"    re.modifiedUsername as modifiedUsername," +
			"    COALESCE(sum(sph.paid_amount), 0) as paidAmount," +
			"    (re.totalAmount - COALESCE(sum(sph.paid_amount), 0)) as dueAmount," +
			"    vhn.voucher_head as voucherHead) " +
			"from Readmission re " +
			"left join VoucherHeadNew vhn on vhn.voucher_head_new_id = re.voucherHeadNewId " +
			"left join StudentPaymentHistory sph on sph.student_id = re.newStudentId " +
			"and sph.voucher_head_new_id = re.voucherHeadNewId " +
			"where re.newStudentId = ?1 and re.active = true " +
			"GROUP BY re.readmissionId, re.oldAuid, re.voucherHeadNewId, re.feeTemplateId, " +
			"re.acYearId, re.semOrYear, re.totalAmount, re.type, re.balance, " +
			"re.oldStudentId, re.remarks, re.active, re.newStudentId, re.newAuid, " +
			"re.createdBy, re.modifiedBy, re.createdDate, re.modifiedDate, " +
			"re.createdUsername, re.modifiedUsername, vhn.voucher_head")
	public HashMap<String, Object> getReadmissionDataByStudentId(Integer student_id);




	@Query(value = "select re from Readmission re where re.newStudentId=:studentId and re.active=true")
	public Readmission getReadmittedStudent(Integer studentId);

}
