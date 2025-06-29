package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.AttachmentSubCategory;

@Repository
public interface AttachmentSubCategoryRepository extends JpaRepository<AttachmentSubCategory, Integer>{

	@Query(value = "SELECT * FROM attachment_sub_category where attachments_subcategory_name=?1",nativeQuery = true)
	public AttachmentSubCategory getAttachCategory(String as_name);
	
	@Query(value ="Select new map(atsc.attachments_subcategory_id as id,atsc.attachments_category_id as attachments_category_id,"
			+ "atsc.attachments_subcategory_name as attachments_subcategory_name,atsc.attachments_subcategory_name_short as attachments_subcategory_name_short,"
			+ "atsc.created_username as created_username,atsc.modified_username as modified_username,atsc.created_date as created_date,"
			+ "atsc.modified_date as modified_date,atsc.created_by as created_by,atsc.modified_by as modified_by,"
			+ "atsc.active as active,atc.attachments_category_name_short as attachments_category_name_short) From AttachmentSubCategory atsc "
			+ "Left Join AttachmentCategory atc On atc.attachments_category_id = atsc.attachments_category_id "
			+ "Where CONCAT(IfNull(atsc.attachments_subcategory_name,''),'',IfNull(atsc.attachments_subcategory_name_short,''),'',IfNull(atsc.created_username,''),'',IfNull(atsc.created_date,''),'',IfNull(atc.attachments_category_name_short,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(atsc.attachments_subcategory_id as id,atsc.attachments_category_id as attachments_category_id,"
			+ "atsc.attachments_subcategory_name as attachments_subcategory_name,atsc.attachments_subcategory_name_short as attachments_subcategory_name_short,"
			+ "atsc.created_username as created_username,atsc.modified_username as modified_username,atsc.created_date as created_date,"
			+ "atsc.modified_date as modified_date,atsc.created_by as created_by,atsc.modified_by as modified_by,"
			+ "atsc.active as active) From AttachmentSubCategory atsc")
	public Page<Object> findAll2(Pageable pageable);
	
	
	
}
