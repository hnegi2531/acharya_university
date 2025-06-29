package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.CustomTemplateListDTO;
import com.au.model.CustomTemplate;

@Repository
@Transactional
public interface CustomTemplateRepository  extends JpaRepository<CustomTemplate, Integer>{

	@Query(value="select new com.au.dto.CustomTemplateListDTO( ct.referenceNo as referenceNo, ct.created_date as createdDate,ct.usertype as usertype,ctd.category_detail as categoryDetail,"
			+ " ua.username as  createdBy,ct.withLetterHead as withLetterHead,ct.templateType as templateType, sc.school_id As school_id, sc.school_name As school_name,"
			+ " ct.content As content , ct.categoryShortName As categoryShortName ,sc.school_name_short As school_name_short ,"
			+ "og.org_id As org_id,og.org_name As org_name,og.org_type As org_type ) from CustomTemplate ct "
			+ " left join CategoryTypeDetails ctd on ctd.category_details_id=ct.categoryDetailId "
			+ " left join Schools sc on sc.school_id=ct.school_id "
			+ " left join Organization og on sc.org_id=og.org_id "
			+ " left join UserAuthentication ua on ua.id=ct.createdBy order by ct.created_date desc")
	Page<CustomTemplateListDTO> getCustomTemplateList(Pageable pageable);

	CustomTemplate findByReferenceNo(String referenceNo);

	CustomTemplate findByUserCodeAndCategoryTypeIdAndCategoryDetailId(String userCode,Integer categoryTypeId,Integer categoryDetailId);

}
