package com.au.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ScholarshipAttachment;

@Repository
@Transactional
public interface ScholarshipAttachmentRepository extends JpaRepository<ScholarshipAttachment,Integer>{

	@Query(value = "SELECT * FROM scholarship_attachment where candidate_id=?1 and active=true",nativeQuery = true)
	ScholarshipAttachment getScholarshipAttachByCid(Integer candidate_id);
	
	@Query(value = "Select new map(sch_attch.scholarship_attachment_id as id,sch_attch.student_id as student_id,"
			+ "sch_attch.scholarship_attachment_path as scholarship_attachment_path,"
			+ "sch_attch.scholarship_attachment_file_name as scholarship_attachment_file_name,sch_attch.candidate_id as candidate_id,"
			+ "sch_attch.created_date as created_date,sch_attch.modified_date as modified_date,sch_attch.created_by as created_by,"
			+ "sch_attch.modified_by as modified_by,sch_attch.active as active,sch_attch.created_username as created_username,"
			+ "sch_attch.modified_username as modified_username) From ScholarshipAttachment sch_attch "
			+ "Where CONCAT(IfNull(sch_attch.scholarship_attachment_id,''),'',IfNull(sch_attch.scholarship_attachment_path,''),'',"
			+ "IfNull(sch_attch.scholarship_attachment_file_name,''),'',IfNull(sch_attch.created_date,''),'',"
			+ "IfNull(sch_attch.created_by,''),'',IfNull(sch_attch.created_username,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(sch_attch.scholarship_attachment_id as id,sch_attch.student_id as student_id,"
			+ "sch_attch.scholarship_attachment_path as scholarship_attachment_path,"
			+ "sch_attch.scholarship_attachment_file_name as scholarship_attachment_file_name,sch_attch.candidate_id as candidate_id,"
			+ "sch_attch.created_date as created_date,sch_attch.modified_date as modified_date,sch_attch.created_by as created_by,"
			+ "sch_attch.modified_by as modified_by,sch_attch.active as active,sch_attch.created_username as created_username,"
			+ "sch_attch.modified_username as modified_username) From ScholarshipAttachment sch_attch")
	public Page<Object> findAll2(Pageable pageable);
	
	@Query(value = "SELECT * FROM scholarship_attachment where candidate_id=?1 and active=true",nativeQuery = true)
	public ScholarshipAttachment getDetailByCandidateId(Integer candidate_id);

	@Modifying
	@Query(value = "update ScholarshipAttachment d set d.active=false where d.candidate_id=?1")
	public void update(Integer id);
	
	@Modifying
	@Query(value = "update ScholarshipAttachment d set d.active=true where d.scholarship_attachment_id=?1")
	public void update1(Integer id);


	@Query(value = "Select * from scholarship_attachment d where d.active=true and d.candidate_id=?1",nativeQuery = true)
	public ScholarshipAttachment checkAttachmentAlreadyPresentOrNot(Integer candidate_id);
}
