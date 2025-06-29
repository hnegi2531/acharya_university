package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.MealType;

@Transactional
@Repository
public interface MealTypeRepository extends JpaRepository<MealType, Integer>  {
	
	@Query(value = "select c from MealType c where c.active=true")
	public List<MealType> findAll1();
	
//	@Query(value = "SELECT mt.meal_id as meal_id, mt.date as date,"
//			+ "ml.remarks as remarks,ml.mess_meal_type as mess_meal_type "
//			+ "FROM menu_table mt left join meal_type ml "
//			+ "on mt.meal_id=ml.meal_id where month(date) >= month(getdate()) and ml.mess_meal_type=?1",nativeQuery = true)
//	public List<Map<String, Object>> findAll3(Integer mess_meal_type);
	
	@Query(value = "select ml from MealType ml where ml.mess_meal_type=?1 and active=true")
	public List<MealType> findAll4(String mess_meal_type);
	
	@Modifying
	@Query(value = "update MealType ml set ml.active=false where ml.meal_id=?1")
	public void update(Integer meal_id);
	
	@Modifying
	@Query(value = "update MealType ml set ml.active=true where ml.meal_id=?1")
	public void update1(Integer meal_id);

	@Query(value ="Select new map(mt.meal_id as id,mt.meal_type as meal_type,mt.created_by as created_by,mt.for_end_user as for_end_user,"
			+ "mt.modified_by as modified_by,mt.created_date as created_date,mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,"
			+ "mt.modified_date as modified_date,mt.active as active,mt.menu_contents as menu_contents,mt.remarks as remarks,"
			+ "mt.created_username as created_username,mt.modified_username as modified_username) From MealType mt "
			+ "Where CONCAT(IfNull(mt.meal_type,''),'',IfNull(mt.created_date,''),'',IfNull(mt.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(mt.meal_id as id,mt.meal_type as meal_type,mt.created_by as created_by,mt.for_end_user as for_end_user,"
			+ "mt.modified_by as modified_by,mt.created_date as created_date,mt.for_mess as for_mess,mt.mess_meal_type as mess_meal_type,"
			+ "mt.modified_date as modified_date,mt.active as active,mt.menu_contents as menu_contents,mt.remarks as remarks,"
			+ "mt.created_username as created_username,mt.modified_username as modified_username) From MealType mt ")
	public Page<Object> findAll3(Pageable pageable);

	@Query(value = "select count(*) from MealType ml where ml.meal_type=?1 and active=true")
	public Integer checkValidationForMealType(String meal_type);

	@Query(value = "select ml from MealType ml where ml.for_end_user=true and active=true")
	public List<MealType> getOnlyEndUserMealType();


}
