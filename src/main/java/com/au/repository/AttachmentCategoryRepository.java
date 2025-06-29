package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.AttachmentCategory;

@Repository
public interface AttachmentCategoryRepository  extends JpaRepository<AttachmentCategory, Integer>{
	
	
	@Query(value = "Select new map(attch.attachments_category_id as id,"
			+ "attch.attachments_category_name as attachments_category_name,"
			+ "attch.attachments_category_name_short as attachments_category_name_short,attch.created_username as created_username,"
			+ "attch.modified_username as modified_username,attch.created_date as created_date,"
			+ "attch.modified_date as modified_date,attch.created_by as created_by,attch.modified_by as modified_by,"
			+ "attch.active as active) From AttachmentCategory attch "
			+ "Where CONCAT(IfNull(attch.attachments_category_id,''),'',IfNull(attch.attachments_category_name,''),'',IfNull(attch.attachments_category_name_short,''),'',IfNull(attch.created_username,''),'',IfNull(attch.created_date,''),'',IfNull(attch.created_by,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(attch.attachments_category_id as id,"
			+ "attch.attachments_category_name as attachments_category_name,"
			+ "attch.attachments_category_name_short as attachments_category_name_short,attch.created_username as created_username,"
			+ "attch.modified_username as modified_username,attch.created_date as created_date,"
			+ "attch.modified_date as modified_date,attch.created_by as created_by,attch.modified_by as modified_by,"
			+ "attch.active as active) From AttachmentCategory attch ")
	public Page<Object> findAll2(Pageable pageable);

}
