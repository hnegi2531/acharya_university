package com.au.repository;


import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.TutionFeeWaiverReport;

@Repository
@Transactional
public interface TutionFeeWaiverReportRepository extends JpaRepository<TutionFeeWaiverReport , Integer> {
	
	@Query(value = "Select tut_fee_report From TutionFeeWaiverReport tut_fee_report Where tut_fee_report.active=true")
	public List<TutionFeeWaiverReport> activeTutionFeeWaiverReportDetails();
	
	@Modifying
	@Query(value = "Update TutionFeeWaiverReport tut_fee_report Set tut_fee_report.active=true Where tut_fee_report.tution_fee_waiver_report_id=?1")
	public void delete1(Integer tution_fee_waiver_report_id);
	
	@Modifying
	@Query(value = "Update TutionFeeWaiverReport tut_fee_report Set tut_fee_report.active=false Where tut_fee_report.tution_fee_waiver_report_id=?1")
	public void delete2(Integer tution_fee_waiver_report_id);
	
	@Query(value = "Select tut_fee_report From TutionFeeWaiverReport tut_fee_report Where tut_fee_report.tution_fee_waiver_id=?1 And tut_fee_report.active=true")
	public List<TutionFeeWaiverReport> getTutionFeeWaiverReportDetailByTutionFeeWaiverId(Integer tution_fee_waiver_id);
	
	@Query(value = "Select COALESCE(count(tut_fee_report.tution_fee_waiver_report_id),0)  From tution_fee_waiver_report tut_fee_report where tut_fee_report.tution_fee_waiver_id=?1 and tut_fee_report.active=true",nativeQuery=true)
	public Integer CountOfTutionFeeWaiverReportByTution_fee_waiver_id(Integer tution_fee_waiver_id);
	
	@Query(value ="Select tut_fee_report.yearly_waiver_amount From TutionFeeWaiverReport tut_fee_report where tution_fee_waiver_id=?1 And year_sem=?2 And active=true")
	public Double waiverAmountForCalculationOfDueAmount(Integer tution_fee_waiver_id , Integer year_sem ); 
	
	@Query(value ="Select new map(tut_fee_report.tution_fee_waiver_report_id as id,tut_fee_report.tution_fee_waiver_id as tution_fee_waiver_id,"
			+ "tut_fee_report.yearly_waiver_amount as yearly_waiver_amount,tut_fee_report.created_username as created_username,"
			+ "tut_fee_report.modified_username as modified_username,tut_fee_report.created_date as created_date,tut_fee_report.modified_date as modified_date,"
			+ "tut_fee_report.created_by as created_by,tut_fee_report.modified_by as modified_by,tut_fee_report.active as active) From TutionFeeWaiverReport tut_fee_report "
			+ "Where CONCAT(IfNull(tut_fee_report.tution_fee_waiver_report_id,''),'',IfNull(tut_fee_report.yearly_waiver_amount,''),'',IfNull(tut_fee_report.created_username,''),'',IfNull(tut_fee_report.created_date,''),'',IfNull(tut_fee_report.created_by,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(tut_fee_report.tution_fee_waiver_report_id as id,tut_fee_report.tution_fee_waiver_id as tution_fee_waiver_id,"
			+ "tut_fee_report.yearly_waiver_amount as yearly_waiver_amount,tut_fee_report.created_username as created_username,"
			+ "tut_fee_report.modified_username as modified_username,tut_fee_report.created_date as created_date,tut_fee_report.modified_date as modified_date,"
			+ "tut_fee_report.created_by as created_by,tut_fee_report.modified_by as modified_by,tut_fee_report.active as active) From TutionFeeWaiverReport tut_fee_report")
	public Page<Object> findAll3(Pageable pageable);
	



	@Query(value ="Select ifNull(Sum(yearly_waiver_amount),0) From tution_fee_waiver_report tfwr "
			+ "Inner Join tution_fee_waiver tfw on tfw.tution_fee_waiver_id=tfwr.tution_fee_waiver_id "
            + "Where tfw.student_id=?1 And tfwr.year_sem=?2 And tfwr.active=true",nativeQuery=true)
	public Double getWavierAmount(Integer student_id,Integer year_sem);
	
	@Modifying
	@Query(value = "update tution_fee_waiver_report tfwr set tfwr.yearly_waiver_amount=?3 where tfwr.year_sem=?2 and tfwr.tution_fee_waiver_id=?1 and tfwr.active=true",nativeQuery=true)
	public void updateYearlyWaiverAmount(Integer tution_fee_waiver_id, Integer year_sem, Double yearly_waiver_amount);

	@Query(value="SELECT tr.yearly_waiver_amount FROM tution_fee_waiver_report tr left JOIN tution_fee_waiver t "
			+ "ON tr.tution_fee_waiver_id = t.tution_fee_waiver_id WHERE tr.year_sem=:year AND t.student_id=:studentId", nativeQuery = true)
	public Float getYearAmount(@Param("year") Integer year,@Param("studentId") Integer studentId);
	

}
