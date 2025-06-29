package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.StudentDocumentAudit;

@Transactional
@Repository
public interface StudentDocumentAuditRepository extends JpaRepository<StudentDocumentAudit, Integer> {

	@Query(value = "SELECT sda from StudentDocumentAudit sda where sda.active=true")
	public List<StudentDocumentAudit> getAllActiveStudentDocumentAudit();

	@Modifying
	@Query(value = "update StudentDocumentAudit sda set sda.active=false where sda.student_document_audit_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update StudentDocumentAudit sda set sda.active=true where sda.student_document_audit_id=?1")
	public void activate(Integer id);

	@Query(value = "select new map(sda.student_document_audit_id as id,sda.created_date as created_date,sda.modified_date as modified_date,sda.created_by as created_by,"
			+ "sda.created_username as created_username,sda.modified_username as modified_username,sda.modified_by as modified_by,sda.active as active,"
			+ "sda.student_id As student_id,sda.audit_status As audit_status,sda.remarks As remarks,sd.student_name As student_name,sd.auid As auid,"
			+ "sd.acharya_email as acharya_email ) from StudentDocumentAudit sda "
			+ "left join Student_Details sd on sda.student_id = sd.student_id "
			+ "where CONCAT(IfNull(sd.student_name,''),'',IfNull(sd.auid,''),'',IfNull(sda.created_username,''),"
			+ "'',IfNull(sda.created_by,''),'',IfNull(sda.created_date,'',IfNull(sda.student_document_audit_id,''),'')) LIKE %:keyword%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query(value = "select new map(sda.student_document_audit_id as id,sda.created_date as created_date,sda.modified_date as modified_date,sda.created_by as created_by,"
			+ "sda.created_username as created_username,sda.modified_username as modified_username,sda.modified_by as modified_by,sda.active as active,"
			+ "sda.student_id As student_id,sda.audit_status As audit_status,sda.remarks As remarks,sd.student_name As student_name,sd.auid As auid,"
			+ "sd.acharya_email as acharya_email ) from StudentDocumentAudit sda "
			+ "left join Student_Details sd on sda.student_id = sd.student_id ")
	public Page<Object> getAllSortedData(Pageable pageable);
}
