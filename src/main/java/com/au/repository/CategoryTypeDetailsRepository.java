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

import com.au.model.CategoryTypeDetails;

@Repository
@Transactional
public interface CategoryTypeDetailsRepository extends JpaRepository<CategoryTypeDetails, Integer> {
	
	@Query(value = "Select ctd from CategoryTypeDetails ctd where ctd.active=true")
	public List<CategoryTypeDetails> findAll1();
	
	@Query(value = "Select ctd.category_details_id As category_details_id,ctd.category_type_id As category_type_id,ctd.category_detail As category_detail,ctd.active As active,"
			+ "ctc.category_name As category_name,ctc.category_name_sort As category_name_sort,ctc.remarks As remarks,ctc.attendance_status As attendance_status,"
			+ "ctc.outside_campus As outside_campus "
			+ "from CategoryTypeDetails ctd "
			+ "left join CategoryTypeCreation ctc on ctd.category_type_id=ctc.category_type_id "
			+ "where(ctc.category_name_sort like '%BONAFIDE%' or ctc.category_name_sort like '%MIS%') And ctd.active=true")
	public List<Map<String,Object>> categoryTypeDetailsOnBonafide();
	
	@Query(value = "Select new map(ctd.category_details_id as id,ctd.category_type_id as category_type_id,ctd.category_detail as category_detail,ctc.category_name as category_name,"
			+ "ctd.created_date as created_date,ctd.modified_date as modified_date,ctd.created_by as created_by,ctd.modified_by as modified_by,"
			+ "ctd.created_username as created_username,ctd.modified_username as modified_username,ctd.active as active) "
			+ "From CategoryTypeDetails ctd left join CategoryTypeCreation ctc on ctd.category_type_id=ctc.category_type_id "
			+ "Where CONCAT(IfNull(ctd.category_details_id,''),'',IfNull(ctd.category_type_id,''),'',IfNull(ctd.category_detail,''),'',IfNull(ctd.created_date,''),'',IfNull(ctd.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(ctd.category_details_id as id,ctd.category_type_id as category_type_id,ctd.category_detail as category_detail,ctc.category_name as category_name,"
			+ "ctd.created_date as created_date,ctd.modified_date as modified_date,ctd.created_by as created_by,ctd.modified_by as modified_by,"
			+ "ctd.created_username as created_username,ctd.modified_username as modified_username,ctd.active as active) "
			+ "From CategoryTypeDetails ctd left join CategoryTypeCreation ctc on ctd.category_type_id=ctc.category_type_id")
	public Page<Object> findAll3(Pageable pageable);
	
	@Modifying
	@Query(value = "update CategoryTypeDetails ctd set ctd.active=false where ctd.category_details_id=?1")
	public void deactivateCategoryTypeDetails(Integer category_details_id);
	

	@Modifying
	@Query(value = "update CategoryTypeDetails ctd set ctd.active=true where ctd.category_details_id=?1")
	public void activateCategoryTypeDetails(Integer category_details_id);
	
	@Query(value = "SELECT count(*) FROM CategoryTypeDetails ctd where ctd.category_type_id =?1 and ctd.category_detail = ?2 and ctd.active=true")
	public Integer countOfCategoryTypeCreation(Integer category_type_id, String  category_detail);
	
//	@Query(value = "SELECT count(*) FROM CategoryTypeCreation ctd where ctd.category_name =?1 and ctd.category_type=?2 and ctd.active=true")
//	public Integer countOfCategoryTypeCreation(String  category_name,String category_type);
//	
//	@Query(value = "SELECT count(*) FROM CategoryTypeCreation ctd where ctd.category_type_id != ?1 and ctd.category_name=?2 and ctd.category_type=?3 and ctd.active=true")
//	public Integer updateCountOfCategoryTypeCreation(Integer category_type_id,String  category_name,String category_type);
	
	@Query(value = "Select new map(ctd.category_details_id as category_details_id,ctd.category_detail as category_detail) From CategoryTypeDetails ctd "
			+ "Where ctd.category_type_id in (Select ctc.category_type_id From CategoryTypeCreation ctc Where ctc.category_name Like '%Scheme%')")
	public List<HashMap<String,Object>> categoryTypeDetailsOnCatgoryTypeCreation();
	
	
	@Query(value = "Select ctd From CategoryTypeDetails ctd "
			+ "Where ctd.category_type_id = (Select ctc.category_type_id From CategoryTypeCreation ctc Where ctc.category_name Like '%Scholarship Type%')")
	public List<CategoryTypeDetails> categoryTypeDetailsForReasonFeeExcemption();

	@Query(value = "select category_detail from CategoryTypeDetails ctd where category_type_id = "
			+ "(select category_type_id from CategoryTypeCreation ctc where ctc.category_name_sort=?1 "
			+ "and ctc.active=true) and ctd.active=true")
	public List<String> getCategoryData(String category_name_sort);

}
