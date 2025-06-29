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

import com.au.model.TutionFeeWaiver;

@Repository
@Transactional
public interface TutionFeeWaiverRepository extends JpaRepository<TutionFeeWaiver , Integer> {
	
	@Query(value = "Select tut_fee From TutionFeeWaiver tut_fee where tut_fee.active=true")
	public List<TutionFeeWaiver> getActiveDetails();
	
	@Modifying
	@Query(value = "Update TutionFeeWaiver tut_fee Set tut_fee.active=true Where tut_fee.tution_fee_waiver_id=?1")
	public void delete1(Integer tution_fee_waiver_id);
	
	@Modifying
	@Query(value = "Update TutionFeeWaiver tut_fee Set tut_fee.active=false Where tut_fee.tution_fee_waiver_id=?1")
	public void delete2(Integer tution_fee_waiver_id);
	
	@Query(value = "Select tut_fee From TutionFeeWaiver tut_fee where tut_fee.student_id=?1 And tut_fee.active=true")
	public TutionFeeWaiver getTutionFeeDetailsByStudentId(Integer student_id);
	
	@Query(value = "Select COALESCE(count(tut_fee.tution_fee_waiver_id),0)  From tution_fee_waiver tut_fee where student_id=?1 and active=true",nativeQuery=true)
	public Integer countOfTutionFeeDetail(Integer student_id);
	
	@Query(value = "Select new map(tut_fee.tution_fee_waiver_id as tution_fee_waiver_id, tut_fee.student_id as student_id, "
			+ "tut_fee_report.year_sem as year_sem, "
			+ "tut_fee_report.yearly_waiver_amount as yearly_waiver_amount, tut_fee_report.tution_fee_waiver_report_id as tution_fee_waiver_report_id)"
			+ "From TutionFeeWaiver tut_fee Inner Join TutionFeeWaiverReport tut_fee_report On tut_fee_report.tution_fee_waiver_id=tut_fee.tution_fee_waiver_id "
			+ "Where tut_fee.student_id=?1 and tut_fee.active=true")
	public List<HashMap<String, Object>> tutionFeeWaiverForStudentLedger(Integer student_id);
	
	@Query(value = "Select new map(tut_fee.tution_fee_waiver_id as id, tut_fee.active as active, tut_fee.approved_status as approved_status, "
			+ "tut_fee.created_date as created_date, tut_fee.reference_name as reference_name, tut_fee.modified_date as modified_date, "
			+ "tut_fee.remarks as remarks, tut_fee.student_id as student_id, tut_fee.total_amount as total_amount, tut_fee.created_username as created_username,"
			+ "sd.student_name as student_name,sd.auid as auid,ta.tution_fee_waiver_attachment_path as tution_fee_waiver_attachment_path,"
			+ "ay.ac_year as ac_year,s.school_name as school_name,s.school_name_short as school_name_short) From TutionFeeWaiver tut_fee "
			+ "left join TutionFeeWaiverAttachment ta on tut_fee.tution_fee_waiver_id=ta.tution_fee_waiver_id "
			+ "Left join Student_Details sd On tut_fee.student_id=sd.student_id "
			+ "Left join Academic_year ay On sd.ac_year_id=ay.ac_year_id "
			+ "Left join Schools s On sd.school_id=s.school_id "
			+ "Where CONCAT(IfNull(tut_fee.tution_fee_waiver_id,''),'',IfNull(tut_fee.created_date,''),'',IfNull(tut_fee.reference_name,''),'',IfNull(tut_fee.created_username,''),'',IfNull(sd.student_name,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(tut_fee.tution_fee_waiver_id as id, tut_fee.active as active, tut_fee.approved_status as approved_status, "
			+ "tut_fee.created_date as created_date, tut_fee.reference_name as reference_name, tut_fee.modified_date as modified_date, "
			+ "tut_fee.remarks as remarks, tut_fee.student_id as student_id, tut_fee.total_amount as total_amount, tut_fee.created_username as created_username,"
			+ "sd.student_name as student_name,sd.auid as auid,ta.tution_fee_waiver_attachment_path as tution_fee_waiver_attachment_path,"
			+ "ay.ac_year as ac_year,s.school_name as school_name,s.school_name_short as school_name_short) From TutionFeeWaiver tut_fee "
			+ "left join TutionFeeWaiverAttachment ta on tut_fee.tution_fee_waiver_id=ta.tution_fee_waiver_id "
			+ "Left join Student_Details sd On tut_fee.student_id=sd.student_id "
			+ "Left join Academic_year ay On sd.ac_year_id=ay.ac_year_id "
			+ "Left join Schools s On sd.school_id=s.school_id ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "select tfw.approved_status,tfw.active,tfw.tution_fee_waiver_id,tfw.reference_name,tfw.remarks,tfw.student_id,"
			+ "tfw.total_amount,tfwa.tution_fee_waiver_attachment_path from tution_fee_waiver tfw left join tution_fee_waiver_attachment "
			+ "tfwa on tfw.tution_fee_waiver_id=tfwa.tution_fee_waiver_id where tfw.tution_fee_waiver_id=?1 and tfw.active=true",nativeQuery=true)
	public Map<String ,Object> findById1(Integer tution_fee_waiver_id);
	
	@Query(value = "select ft.tution_fee_waiver_sub_amt_id as tution_fee_waiver_sub_amt_id,ft.year1_amt as year1_amt,"
			+ " ft.voucher_head_new_id as voucher_head_new_id,vhn.voucher_head as voucher_head,ft.tution_fee_waiver_id as tution_fee_waiver_id,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,ft.offer_waiver_amount as offer_waiver_amount,"
			+ "ft.year2_amt as year2_amt,ft.year3_amt as year3_amt,ft.year4_amt as year4_amt,ft.year5_amt as year5_amt,"
			+ "ft.year6_amt as year6_amt,ft.year7_amt as year7_amt,ft.year8_amt as year8_amt,ft.year9_amt as year9_amt,"
			+ "ft.year10_amt as year10_amt,ft.year11_amt as year11_amt,ft.year12_amt as year12_amt,tut.total_amount as total_amount,"
			+ "tut.remarks as remarks,ft.active as active from tution_fee_waiver_sub_amount ft "
			+ "left join tution_fee_waiver tut on tut.tution_fee_waiver_id=ft.tution_fee_waiver_id "
			+ "left join voucher_head_new vhn on vhn.voucher_head_new_id=ft.voucher_head_new_id "
			+ "where ft.tution_fee_waiver_id=?1 and ft.active=true",nativeQuery=true)
	public List<Map<String ,Object>> fetchTutionFeeWaiverForEdit(Integer tution_fee_waiver_id);
	
	@Query(value = "Select tut_fee From TutionFeeWaiver tut_fee where tut_fee.tution_fee_waiver_id=?1 And tut_fee.active=true")
	public TutionFeeWaiver getTutionFeeWaiver(Integer tution_fee_waiver_id);
	
	
	@Modifying
	@Query(value = "update tution_fee_waiver tf set tf.approved_status=true where tf.tution_fee_waiver_id=?1 and tf.active=true",nativeQuery=true)
	public void updateApproveStatus(Integer tution_fee_waiver_id);

}
