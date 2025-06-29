package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.CategoriesDTO;
import com.au.model.CategoryTypeCreation;



@Repository
@Transactional
public interface CategoryTypeCreationRepository extends JpaRepository<CategoryTypeCreation, Integer>{
	
	@Query(value = "Select ctc from CategoryTypeCreation ctc where ctc.active=true")
	public List<CategoryTypeCreation> findAll1();
	
	@Query(value = "Select new map(ctc.category_type_id as id,ctc.category_name as category_name,ctc.category_name_sort as category_name_sort,ctc.remarks as remarks,"
			+ "ctc.created_date as created_date,ctc.modified_date as modified_date,ctc.created_by as created_by,ctc.modified_by as modified_by,ctc.attendance_status as attendance_status,"
			+ "ctc.created_username as created_username,ctc.modified_username as modified_username,ctc.active as active,ctc.outside_campus as outside_campus) From CategoryTypeCreation ctc "
			+ "Where CONCAT(IfNull(ctc.category_type_id,''),'',IfNull(ctc.category_name,''),'',IfNull(ctc.category_name_sort,''),'',IfNull(ctc.remarks,''),'',IfNull(ctc.created_date,''),'',IfNull(ctc.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(ctc.category_type_id as id,ctc.category_name as category_name,ctc.category_name_sort as category_name_sort,ctc.remarks as remarks,"
			+ "ctc.created_date as created_date,ctc.modified_date as modified_date,ctc.created_by as created_by,ctc.modified_by as modified_by,"
			+ "ctc.created_username as created_username,ctc.modified_username as modified_username,ctc.active as active) From CategoryTypeCreation ctc ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Modifying
	@Query(value = "update CategoryTypeCreation ctc set ctc.active=false where ctc.category_type_id=?1")
	public void deactivateCategoryTypeCreation(Integer category_type_id);
	

	@Modifying
	@Query(value = "update CategoryTypeCreation ctc set ctc.active=true where ctc.category_type_id=?1")
	public void activateCategoryTypeCreation(Integer category_type_id);
	
	@Query(value = "SELECT count(*) FROM CategoryTypeCreation ctc where ctc.category_name =?1 and ctc.active=true")
	public Integer countOfCategoryTypeCreation(String  category_name);
	
	@Query(value = "SELECT count(*) FROM CategoryTypeCreation ctc where ctc.category_name_sort=?1 and ctc.active=true")
	public Integer countOfCategoryTypeShortName(String category_name_sort);
	
	@Query(value = "SELECT count(*) FROM CategoryTypeCreation ctc where ctc.category_type_id != ?1 and ctc.category_name=?2 and ctc.category_name_sort=?3 and ctc.active=true")
	public Integer updateCountOfCategoryTypeCreation(Integer category_type_id,String  category_name,String category_name_sort);

	@Query(value=" select new com.au.dto.CategoriesDTO( ctc.category_type_id as categoryTypeId, ctd.category_details_id as categoryDetailsId, CONCAT(ctc.category_name_sort,'-',ctd.category_detail) as categoryName ,ctc.category_name_sort as categoryShortName ) from CategoryTypeCreation ctc left join CategoryTypeDetails ctd on ctd.category_type_id=ctc.category_type_id "
			+ " where ctc.active=1 and ctc.category_name_sort='FRRO'  ")
	public List<CategoriesDTO> getCategoriesForFrro();

	@Query(value = "Select ctc.category_type_id As category_type_id,ctc.category_name As category_name,ctc.category_name_sort As category_name_sort,"
			+ "ctd.category_detail As category_detail,ctd.category_details_id As category_details_id FROM category_type_creation ctc "
			+ "left join category_type_details ctd on ctc.category_type_id = ctd.category_type_id "
			+ "where ctc.category_name_sort like '%PAY TYPE%' And ctd.active=true" ,nativeQuery = true)
	public List<Map<String, Object>> getCategoriesForPaymentType();

}
