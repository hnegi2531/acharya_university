package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.au.model.StorePurchase;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface StorePurchaseRepository extends JpaRepository<StorePurchase, Integer> {

	
	@Query(value = "select sp from StorePurchase sp where sp.active=true")
	public List<StorePurchase> findAll1();
	
	
	@Query(value = "select new map(sp.store_purchase_id as id,sp.emp_id as emp_id,"
			+ "sp.food_approver as food_approver,sp.travel_indent as travel_indent,sp.bill_approver2 as bill_approver2,"
			+ "sp.bill_approver1 as bill_approver1,sp.created_by as created_by,sp.modified_by as modified_by,ed.employee_name as employee_name,"
			+ "sp.created_date as created_date,sp.modified_date as modified_date,sp.active as active,"
			+ "sp.created_username as created_username,sp.modified_username as modified_username) "
			+ "from StorePurchase sp "
			+ "left join EmployeeDetails ed on ed.emp_id=sp.emp_id "
			+ "where CONCAT(IfNull(sp.emp_id,''),'',IfNull(sp.food_approver,''),'',IfNull(sp.travel_indent,''),"
			+ "'',IfNull(sp.created_username,''),'',IfNull(sp.created_by,''),'',IfNull(sp.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	@Query(value = "select new map(sp.store_purchase_id as id,sp.emp_id as emp_id,"
			+ "sp.food_approver as food_approver,sp.travel_indent as travel_indent,sp.bill_approver2 as bill_approver2,"
			+ "sp.bill_approver1 as bill_approver1,sp.created_by as created_by,sp.modified_by as modified_by,ed.employee_name as employee_name,"
			+ "sp.created_date as created_date,sp.modified_date as modified_date,sp.active as active,"
			+ "sp.created_username as created_username,sp.modified_username as modified_username) "
			+ "from StorePurchase sp "
			+ "left join EmployeeDetails ed on ed.emp_id=sp.emp_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = "update StorePurchase sp set sp.active=false where sp.store_purchase_id=?1")
	public void updateToDeactive(Integer id);

	@Modifying
	@Query(value = "update StorePurchase sp set sp.active=true where sp.store_purchase_id=?1")
	public void updateToActive(Integer id);
	
	@Query(value = "select ed.emp_id as emp_id,concat(ed.empcode,'-',ed.employee_name) as employee from EmployeeDetails ed ")
	public List<Map<String,Object>> getEmpNameConcatWithCode();
	
	@Query(value = "select count(*) from StorePurchase sp where sp.emp_id=?1")
	public Integer getEmpCount(Integer emp_id);
}
