package com.au.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Documents;



@Repository
@Transactional
public interface DocumentsRepository extends JpaRepository<Documents, Integer>{


	@Query(value = "select new map(doc.documents_id as id,doc.group_type as group_type,doc.staff_student_reference as staff_student_reference,"
			+ "doc.contract_number as contract_number,doc.category as category,doc.school_id As school_id,sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "doc.document_attachment_path as document_attachment_path,doc.created_username as created_username,doc.modified_username as modified_username,"
			+ "doc.created_date as created_date,doc.modified_date as modified_date,doc.created_by as created_by,"
			+ "doc.modified_by as modified_by,doc.active as active) "
			+ "from Documents doc "
			+ " left join Schools sc on sc.school_id=doc.school_id "
			+ "where CONCAT(IfNull(doc.category,''),'',IfNull(doc.group_type,''),'',IfNull(doc.documents_id,''),'',IfNull(doc.staff_student_reference,''),"
			+ "'',IfNull(doc.contract_number,''),'',IfNull(doc.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	@Query(value = "select new map(doc.documents_id as id,doc.group_type as group_type,doc.staff_student_reference as staff_student_reference,"
			+ "doc.contract_number as contract_number,doc.category as category,doc.school_id As school_id,"
			+ "sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "doc.document_attachment_path as document_attachment_path,doc.created_username as created_username,doc.modified_username as modified_username,"
			+ "doc.created_date as created_date,doc.modified_date as modified_date,doc.created_by as created_by,"
			+ "doc.modified_by as modified_by,doc.active as active) "
			+ "from Documents doc "
			+ " left join Schools sc on sc.school_id=doc.school_id ")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Query(value = "SELECT doc from Documents doc where doc.active=true")
	public List<Documents> findAll11();

	
	
	@Modifying
	@Query(value = "update Documents doc set doc.active=false where doc.documents_id=?1")
	public void updateDocuments(Integer id);
	
	@Modifying
	@Query(value = "update Documents doc set doc.active=true where doc.documents_id=?1")
	public void updateDocuments1(Integer id);

	@Modifying
	@Query(value = "update Documents doc set doc.document_attachment_path=?2 where doc.documents_id=?1")
	public void updatePath(Integer documents_id, String t2);
	
	@Query(value = "SELECT doc.document_attachment_path from Documents doc where doc.documents_id=?1 and doc.active=true")
	public String getExisting_file_path(Integer documents_id);
	
}
