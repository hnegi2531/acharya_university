package com.au.repository;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.SlabStructure;

@Transactional
@Repository
public interface SlabStructureRepository extends JpaRepository<SlabStructure, Integer> {

	@Query(value = "select ss from SlabStructure ss where ss.active=true")
	public List<SlabStructure> findAll1();
	
	@Modifying
	@Query(value = "update SlabStructure ss set ss.active=false where ss.slab_structure_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update SlabStructure ss set ss.active=true where ss.slab_structure_id=?1")
	public void update1(Integer id);
	
	@Query(value = "select ss.slab_details_id as slab_details_id,ss.min_value as min_value,ss.max_value as max_value,"
			+ "ss.head_value as head_value,ss.slab_structure_id as slab_structure_id,ss.active as active,"
			+ "ss.created_by as created_by,ss.created_username as created_username,sd.slab_details_name as slab_details_name,sd.slab_details_short_name as slab_details_short_name,"
			+ "(Select GROUP_CONCAT(salary_structure_head.print_name) From salary_structure_head where FIND_IN_SET(salary_structure_head.salary_structure_head_id,(select slab_details.salary_structure_head_ids From slab_details where slab_details.slab_details_id=ss.slab_details_id))) as print_name,"
			+ "(Select GROUP_CONCAT(voucher_head_new.voucher_head_short_name) From voucher_head_new where FIND_IN_SET(voucher_head_new.voucher_head_new_id,(select GROUP_CONCAT(salary_structure_head.voucher_head_new_id) From salary_structure_head where FIND_IN_SET(salary_structure_head.salary_structure_head_id,sd.salary_structure_head_ids)))) as voucher_head_short_names,"
			+ "(Select GROUP_CONCAT(voucher_head_new.voucher_head)  From voucher_head_new where FIND_IN_SET(voucher_head_new.voucher_head_new_id,(select GROUP_CONCAT(salary_structure_head.voucher_head_new_id) From salary_structure_head where FIND_IN_SET(salary_structure_head.salary_structure_head_id,sd.salary_structure_head_ids)))) as voucher_heads from slab_structure ss "
			+ "left join slab_details sd on sd.slab_details_id = ss. slab_details_id where ss.active=true",nativeQuery=true)
	public List<Map<String, Object>> getAllValue();
	
	@Query(value = "Select new map(ss.slab_structure_id as id,ss.slab_details_id as slab_details_id,ss.min_value as min_value,"
			+ "ss.max_value as max_value,ss.head_value as head_value,ss.created_by as created_by,ss.modified_by as modified_by,"
			+ "ss.created_date as created_date,ss.modified_date as modified_date,ss.active as active,ss.created_username as created_username,"
			+ "ss.modified_username as modified_username,sd.slab_details_short_name as slab_details_short_name) From SlabStructure ss "
			+ "Left join SlabDetails sd on sd.slab_details_id = ss.slab_details_id "
			+ "Where CONCAT(IfNull(ss.slab_structure_id,''),'',IfNull(ss.min_value,''),'',IfNull(ss.max_value,''),'',IfNull(ss.head_value,''),'',"
			+ "IfNull(ss.created_date,''),'',IfNull(ss.created_username,''),'',IfNull(sd.slab_details_short_name,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(ss.slab_structure_id as id,ss.slab_details_id as slab_details_id,ss.min_value as min_value,"
			+ "ss.max_value as max_value,ss.head_value as head_value,ss.created_by as created_by,ss.modified_by as modified_by,"
			+ "ss.created_date as created_date,ss.modified_date as modified_date,ss.active as active,ss.created_username as created_username,"
			+ "ss.modified_username as modified_username,sd.slab_details_short_name as slab_details_short_name) From SlabStructure ss "
			+ "Left join SlabDetails sd on sd.slab_details_id = ss.slab_details_id")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "Select ifnull(max(ss.max_value),0) From slab_structure ss Where ss.slab_details_id =?1 and ss.active=true",nativeQuery=true)
	public Integer getLastDataMaxValue(Integer slab_details_id);
	
	@Query(value = "Select count(*) From slab_structure ss Where ss.slab_details_id =?1 and ss.active=true",nativeQuery=true)
	public Integer countSlabDetailsId(Integer slab_details_id);
	
	@Query(value = "Select ifnull(ss.head_value,0) From slab_structure ss Where ss.slab_details_id =?1 and ss.active=true Order by slab_structure_id DESC Limit 1",nativeQuery=true)
	public Integer getLastDataOfHeadValue(Integer slab_details_id);
	
	@Query(value = "Select ifnull(ss.head_value,0) From slab_structure ss Where ss.slab_structure_id != ?1 and ss.slab_details_id =?2 and ss.active=true Order by slab_structure_id DESC Limit 1",nativeQuery=true)
	public Integer getLastDataOfHeadValueForUpdate(Integer slab_structure_id,Integer slab_details_id);

	@Query(value="select sb from SlabStructure sb where sb.slab_details_id=:slabDetailId and :annualSalary between sb.min_value and sb.max_value ")
	public SlabStructure getSlabStructureDetailsBySlabDetailsId(@Param("slabDetailId") Integer slabDetailId, Integer annualSalary);
}