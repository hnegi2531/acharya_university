package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.HostelWaiver;

@Transactional
@Repository
public interface HostelWaiverRepository extends JpaRepository<HostelWaiver,Integer> {
	
	@Query(value = "select hw from HostelWaiver hw where hw.active=true")
	public List<HostelWaiver> findAll1();
	
	@Modifying
	@Query(value = "update HostelWaiver hw set hw.active=false where hw.hostel_waiver_id=?1")
	public void update(Integer hostel_waiver_id);
	
	@Modifying
	@Query(value = "update HostelWaiver hw set hw.active=true where hw.hostel_waiver_id=?1")
	public void update1(Integer hostel_waiver_id);

	@Query(value = "select hw from HostelWaiver hw where hw.hostel_waiver_id=?1")
	public HostelWaiver findById1(Integer hostel_waiver_id);

	
	@Query(value = "select new map(hw.hostel_waiver_id as id,hw.student_id as student_id,hw.ac_year_id as ac_year_id,hw.total_amount as total_amount,hw.remarks as remarks,"
			+ "hw.approve_status as approve_status,hw.hw_attachment_id as hw_attachment_id,hw.hw_attachment_path as hw_attachment_path,hw.hw_attachment_file_name as hw_attachment_file_name,"
			+ "hw.hw_attachement_type as hw_attachement_type,hw.created_by as created_by,hw.modified_by as modified_by,hw.hostel_bed_assignment_id as hostel_bed_assignment_id,"
			+ "hw.created_date as created_date,hw.modified_date as modified_date,hw.active as active,hb.bedName as bedName,"
			+ "sd.auid as auid,sd.usn as usn,sd.student_name as student_name,ay.ac_year as ac_year,ay.current_year as current_year,"
			+ "hw.created_username as created_username,hw.modified_username as modified_username,hw.type as type) "
			+ "from HostelWaiver hw "
			+ " left join Student_Details sd on hw.student_id=sd.student_id  "
			+ " left join Academic_year ay on hw.ac_year_id=ay.ac_year_id  "
			+ " left join HostelBeds hb on hw.hostel_bed_id=hb.hostelBedId  "
			+ "where CONCAT(IfNull(hw.hostel_waiver_id,''),'',IfNull(hw.student_id,''),'',IfNull(hw.ac_year_id,''),'',IfNull(hw.total_amount,''),"
			+ "'',IfNull(hw.created_by,''),'',IfNull(hw.created_date,'')) LIKE %?1% ")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query(value = "select new map(hw.hostel_waiver_id as id,hw.student_id as student_id,hw.ac_year_id as ac_year_id,hw.total_amount as total_amount,hw.remarks as remarks,"
			+ "hw.approve_status as approve_status,hw.hw_attachment_id as hw_attachment_id,hw.hw_attachment_path as hw_attachment_path,hw.hw_attachment_file_name as hw_attachment_file_name,"
			+ "hw.hw_attachement_type as hw_attachement_type,hw.created_by as created_by,hw.modified_by as modified_by,hw.hostel_bed_assignment_id as hostel_bed_assignment_id,"
			+ "hw.created_date as created_date,hw.modified_date as modified_date,hw.active as active,hb.bedName as bedName,"
			+ "sd.auid as auid,sd.usn as usn,sd.student_name as student_name,ay.ac_year as ac_year,ay.current_year as current_year,"
			+ "hw.created_username as created_username,hw.modified_username as modified_username,hw.type as type) "
			+ "from HostelWaiver hw "
			+ " left join Student_Details sd on hw.student_id=sd.student_id  "
			+ " left join Academic_year ay on hw.ac_year_id=ay.ac_year_id  "
			+ " left join HostelBeds hb on hw.hostel_bed_id=hb.hostelBedId  ")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "select hw from HostelWaiver hw Where hw.ac_year_id=?1 And hw.student_id=?2 And hw.type=?3 And hw.active=true")
	public HostelWaiver hostelWaiverByAcademicYearAndStudentId(Integer ac_year_id, Integer student_id, String string);

	
	@Query(value = "select Count(*) from hostel_waiver where student_id=?1 And ac_year_id=?2 And type=?3 and active=true",nativeQuery = true)
	public int checkAuidWithTypeIsAlreadyPresentOrNot(Integer student_id, Integer academic_year_id, String type);

	@Query(value = "select COALESCE(sum(hw.total_amount),0) from HostelWaiver hw Where hw.ac_year_id=?1 And hw.student_id=?2 And hw.active=true")
	public Integer getDataForLedger(Integer acYearId, Integer studentId);

	@Query(value = "select COALESCE(sum(hw.total_amount),0) from HostelWaiver hw Where hw.ac_year_id=?1 And hw.student_id=?2 And hw.type='Fee Paid' And hw.active=true")
	public Integer getFeePaidDataForLedger(Integer acYearId, Integer studentId);
	
}
