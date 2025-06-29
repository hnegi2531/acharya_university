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
import com.au.model.ReferenceBooks;

@Repository
@Transactional
public interface ReferenceBooksRepository extends JpaRepository<ReferenceBooks, Integer>{
	
	@Modifying
	@Query(value = "update ReferenceBooks r set r.active=false where r.book_id=?1")
	public void updateReferenceBooks(Integer id);

	@Modifying
	@Query(value = "update ReferenceBooks r set r.active=true where r.book_id=?1")
	public void updateReferenceBooks1(Integer id);

//	@Query(value = "select case when title_of_book =:title_of_book then 'true' else 'false' end as title_of_book from "
//			+ "reference_books where title_of_book =:title_of_book",nativeQuery = true)
	@Query(value = "select count(*) from ReferenceBooks rb where rb.title_of_book=?1 and active=true")
	public Integer existsBytitleOfBook(String title_of_book);

	@Query(value = "select new map(rb.book_id as book_id,rb.title_of_book as title_of_book,rb.eresource as eresource) from ReferenceBooks rb "
			+ "where rb.school_id=?1 and rb.program_specialization_id=?2 and rb.active=true")
	public List<HashMap<String, Object>> getAllReferenceBooks(Integer school_id,Integer program_specialization_id);
	
	@Query(value = "select new map(rb.book_id as book_id,rb.title_of_book as title_of_book,rb.eresource as eresource) from ReferenceBooks rb "
			+ "where rb.program_specialization_id=?1 and rb.active=true")
	public List<HashMap<String, Object>> getAllReferenceBooks(Integer program_specialization_id);
	
	@Query(value = "select count(*) from ReferenceBooks rb where rb.title_of_book=?1 And rb.isbn_code=?2 and active=true")
	public Integer existsBytitleOfBook(String title_of_book,String isbn_code);
	
	@Query(value = "select rb.book_id as id,rb.title_of_book as title_of_book,rb.author as author,rb.edition as edition,"
			+ "(select CONCAT((select ps.program_specialization_short_name from "
			+ "program_specialization ps where rb.program_specialization_id=ps.program_specialization_id)"
			+ ",'-',(select p.program_short_name from program p where ps.program_id=p.program_id)) ) as concatenated_program_specialization,"
			+ "rb.yr_of_Publish as yr_of_Publish,rb.publisher_details as publisher_details,rb.created_by as created_by,rb.modified_by as modified_by,"
			+ "rb.created_date as created_date,rb.modified_date as modified_date,rb.active as active,rb.available_books as available_books,rb.course_assignment_id as course_assignment_id,"
			+ "rb.created_username as created_username,rb.modified_username as modified_username,ca.course_id as course_id,c.course_code as course_code,"
			+ "rb.school_id as school_id,rb.reference_code as reference_code,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "rb.program_specialization_id as program_specialization_id,rb.eresource as eresource from reference_books rb "
			+ "left join schools sc on rb.school_id=sc.school_id "
			+ "left join course_assignment ca on rb.course_assignment_id=ca.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join program_specialization ps on rb.program_specialization_id = ps.program_specialization_id "
			+ "where CONCAT(IfNull(rb.book_id,''),'',IfNull(rb.title_of_book,''),'',IfNull(rb.author,''),'',IfNull(rb.publisher_details,''),"
			+ "'',IfNull(rb.edition,''),'',IfNull(rb.yr_of_Publish,''),'',IfNull(rb.available_books,''),'',IfNull(rb.created_by,''),'',IfNull(rb.created_date,'')) LIKE %?1%",nativeQuery=true)
	public List<Map<String, Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select rb.book_id as id,rb.title_of_book as title_of_book,rb.author as author,rb.edition as edition,"
			+ "(select CONCAT((select ps.program_specialization_short_name from "
			+ "program_specialization ps where rb.program_specialization_id=ps.program_specialization_id)"
			+ ",'-',(select p.program_short_name from program p where ps.program_id=p.program_id)) ) as concatenated_program_specialization,"
			+ "rb.yr_of_Publish as yr_of_Publish,rb.publisher_details as publisher_details,rb.created_by as created_by,rb.modified_by as modified_by,"
			+ "rb.created_date as created_date,rb.modified_date as modified_date,rb.active as active,rb.available_books as available_books,rb.course_assignment_id as course_assignment_id,"
			+ "rb.created_username as created_username,rb.modified_username as modified_username,ca.course_id as course_id,c.course_code as course_code,"
			+ "rb.school_id as school_id,rb.reference_code as reference_code,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "rb.program_specialization_id as program_specialization_id,rb.eresource as eresource from reference_books rb "
			+ "left join schools sc on rb.school_id=sc.school_id "
			+ "left join course_assignment ca on rb.course_assignment_id=ca.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join program_specialization ps on rb.program_specialization_id = ps.program_specialization_id ",nativeQuery=true)
	public List<Map<String, Object>> getAllSortedData(Pageable pageable);
	
	@Query(value = "select rb.book_id as book_id,CONCAT(rb.title_of_book,'-',rb.author,'-',rb.yr_of_Publish ) as title_of_book "
			+ "from reference_books rb where rb.school_id=?1 and rb.program_specialization_id=?2 and rb.active=true",nativeQuery=true)
	public List<Map<String, Object>> getReferenceBooksForLessonPlan(Integer school_id,Integer program_specialization_id);

	
	@Query(value = "select rb.school_id,rb.program_specialization_id,rb.course_id,sc.school_name,sc.school_name_short,"
			+ "ps.program_specialization_name,ps.program_specialization_short_name,c.course_name,c.course_short_name,"
			+ "rb.author,rb.edition,rb.publisher_details,rb.title_of_book,rb.reference_code,rb.eresource as eresource "
			+ "from reference_books rb left join schools sc on rb.school_id=sc.school_id "
			+ "left join program_specialization ps on rb.program_specialization_id = ps.program_specialization_id "
			+ "left join course c on rb.course_id=c.course_id "
			+ "where rb.book_id=?1 and rb.active=true",nativeQuery=true)
	public List<Map<String, Object>> getDetails1(Integer id);
}
