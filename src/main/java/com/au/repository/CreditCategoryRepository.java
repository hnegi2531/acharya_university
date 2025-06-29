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
import com.au.model.CreditCategory;

@Repository
@Transactional
public interface CreditCategoryRepository extends JpaRepository<CreditCategory, Integer> {

	@Query(value = "select cc from CreditCategory cc where cc.active=true")
	public List<CreditCategory> findAll1();

	@Modifying
	@Query(value = "update CreditCategory cc set cc.active=false where cc.credit_category_id=?1")
	public void updateCreditCategory(Integer designation_id);

	@Modifying
	@Query(value = "update CreditCategory cc set cc.active=true where cc.credit_category_id=?1")
	public void updateCreditCategory1(Integer designation_id);

//	@Query(value = "SELECT cc1.course_category_name,cc1.course_category_code,cc.credit_category_id,cc.course_category_id,"
//			+ "cc.created_date,cc.created_username,cc.created_by,cc.credit_percentage,cc.min_credits,cc.modified_date,"
//			+ "cc.modified_username,cc.active,cc.modified_by FROM credit_category cc "
//			+ "left join course_category cc1 on cc.course_category_id=cc1.course_category_id",nativeQuery = true)
//	public List<Map<String, Object>> fetchAllCreditCategoryDetail();
	
	@Query(value = "SELECT new map(cc.credit_category_id as id,cc1.course_category_name as course_category_name,"
			+ "cc.course_category_id as course_category_id,cc1.course_category_code as course_category_code,"
			+ "cc.created_date as created_date,cc.created_username as created_username,cc.created_by as created_by,"
			+ "cc.credit_percentage as credit_percentage,cc.min_credits as min_credits,cc.modified_date as modified_date,"
			+ "cc.modified_username as modified_username,cc.active as active,cc.modified_by as modified_by) FROM CreditCategory cc "
			+ "left join CourseCategory cc1 on cc.course_category_id=cc1.course_category_id "
			+ "Where CONCAT(IfNull(cc.credit_category_id,''),'',IfNull(cc1.course_category_name,''),'',IfNull(cc1.course_category_code,''),'',IfNull(cc.created_date,''),'',IfNull(cc.created_by,''),'',IfNull(cc.created_username,'')) LIKE %?1%")
	public Page<Object> fetchAllCreditCategoryDetail1(Pageable pageable, Object keyword);
	
	
	@Query(value = "SELECT new map(cc.credit_category_id as id,cc1.course_category_name as course_category_name,"
			+ "cc.course_category_id as course_category_id,cc1.course_category_code as course_category_code,"
			+ "cc.created_date as created_date,cc.created_username as created_username,cc.created_by as created_by,"
			+ "cc.credit_percentage as credit_percentage,cc.min_credits as min_credits,cc.modified_date as modified_date,"
			+ "cc.modified_username as modified_username,cc.active as active,cc.modified_by as modified_by) FROM CreditCategory cc "
			+ "left join CourseCategory cc1 on cc.course_category_id=cc1.course_category_id")
	public Page<Object> fetchAllCreditCategoryDetail2(Pageable pageable);
	
	
	

}
