package com.au.repository;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.MealVendorAssignment;
@Transactional
@Repository
public interface MealVendorAssignmentRepository extends JpaRepository<MealVendorAssignment, Integer> {

	@Query(value = "select ven from MealVendorAssignment ven where ven.active=true")
	public List<MealVendorAssignment> findAll1();
	
	@Modifying
	@Query(value = "update MealVendorAssignment mva set mva.active=false where mva.meal_vendor_assignment_id=?1")
	public void update(Integer vendor_ass_id);
	
	@Query(value = "select new map(mva.meal_vendor_assignment_id as id,mva.meal_id as meal_id,ml.menu_contents as menu_contents,"
	   		+ "mva.voucher_head_new_id as voucher_head_new_id,mva.created_by as created_by,mva.modified_by as modified_by,"
	   		+ "mva.created_username as created_username,mva.modified_username as modified_username,mva.refreshment_id as refreshment_id,"
	   		+ "mva.created_date as created_date,mva.modified_date as modified_date,mva.rate_per_count as rate_per_count,"
	   		+ "mva.remarks as remarks,mva.active as active, ml.meal_type as meal_type,ve.vendor_name as vendor_name) "
	   		+ "from MealVendorAssignment mva "
	   		+ "left join MealType ml on mva.meal_id=ml.meal_id "
	   		+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
	   		+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id")
		public List<HashMap<String, Object>> findAll2();
	
	@Modifying
	@Query(value = "update MealVendorAssignment mva set mva.active=true where mva.meal_vendor_assignment_id=?1")
	public void update1(Integer vendor_ass_id);
	
	@Query(value ="select new map(mva.meal_vendor_assignment_id as id,mva.meal_id as meal_id,ml.menu_contents as menu_contents,"
	   		+ "mva.voucher_head_new_id as voucher_head_new_id,mva.created_by as created_by,mva.modified_by as modified_by,"
	   		+ "mva.created_username as created_username,mva.modified_username as modified_username,mva.refreshment_id as refreshment_id,"
	   		+ "mva.created_date as created_date,mva.modified_date as modified_date,mva.rate_per_count as rate_per_count,"
	   		+ "mva.remarks as remarks,mva.active as active, ml.meal_type as meal_type,ve.vendor_name as vendor_name) "
	   		+ "from MealVendorAssignment mva "
	   		+ "left join MealType ml on mva.meal_id=ml.meal_id "
	   		+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
	   		+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id "
			+ "Where CONCAT(IfNull(mva.meal_vendor_assignment_id,''),'',IfNull(ml.meal_type,''),'',IfNull(mva.created_date,''),'',IfNull(mva.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="select new map(mva.meal_vendor_assignment_id as id,mva.meal_id as meal_id,ml.menu_contents as menu_contents,"
	   		+ "mva.voucher_head_new_id as voucher_head_new_id,mva.created_by as created_by,mva.modified_by as modified_by,"
	   		+ "mva.created_username as created_username,mva.modified_username as modified_username,mva.refreshment_id as refreshment_id,"
	   		+ "mva.created_date as created_date,mva.modified_date as modified_date,mva.rate_per_count as rate_per_count,"
	   		+ "mva.remarks as remarks,mva.active as active, ml.meal_type as meal_type,ve.vendor_name as vendor_name) "
	   		+ "from MealVendorAssignment mva "
	   		+ "left join MealType ml on mva.meal_id=ml.meal_id "
	   		+ "left join VoucherHeadNew vhn on mva.voucher_head_new_id=vhn.voucher_head_new_id "
	   		+ "left join Vendor ve on vhn.voucher_head_new_id=ve.voucher_head_new_id ")
	public Page<Object> findAll3(Pageable pageable);

	@Query(value = "select count(*) from MealVendorAssignment mva where mva.meal_id=?1 and mva.voucher_head_new_id=?2 and active=true")
	public Integer checkValidation(Integer meal_id, Integer voucher_head_new_id);

	@Modifying
	@Query(value = "update MealVendorAssignment mva set mva.rate_per_count=?3 where mva.meal_id=?1 And mva.voucher_head_new_id=?2 And mva.active=true")
	public void updateRatePerCount(Integer meal_id, Integer voucher_head_new_id, Double rate_per_count);



}
