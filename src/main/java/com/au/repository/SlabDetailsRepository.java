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
import com.au.model.SlabDetails;

@Transactional
@Repository
public interface SlabDetailsRepository extends JpaRepository<SlabDetails, Integer> {
	
	@Query(value = "select sd from SlabDetails sd where sd.active=true")
	public List<SlabDetails> findAll1();
	
	@Modifying
	@Query(value = "update SlabDetails sd set sd.active=false where sd.slab_details_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update SlabDetails sd set sd.active=true where sd.slab_details_id=?1")
	public void update1(Integer id);
	
	@Query(value = "Select sd.slab_details_id as id,"
			+ "sd.slab_details_name as slab_details_name,sd.slab_details_short_name as slab_details_short_name,sd.salary_structure_head_ids as salary_structure_head_ids,"
			+ "sd.description as description,sd.created_by as created_by,sd.modified_by as modified_by,sd.created_date as created_date,"
			+ "sd.modified_date as modified_date,sd.active as active,sd.created_username as created_username,sd.modified_username as modified_username,"
			+ "(Select GROUP_CONCAT(voucher_head_new.voucher_head_short_name) From voucher_head_new where FIND_IN_SET(voucher_head_new.voucher_head_new_id,(select GROUP_CONCAT(salary_structure_head.voucher_head_new_id) From salary_structure_head where FIND_IN_SET(salary_structure_head.salary_structure_head_id,sd.salary_structure_head_ids)))) as voucher_head_short_names,"
			+ "(Select GROUP_CONCAT(voucher_head_new.voucher_head)  From voucher_head_new where FIND_IN_SET(voucher_head_new.voucher_head_new_id,(select GROUP_CONCAT(salary_structure_head.voucher_head_new_id) From salary_structure_head where FIND_IN_SET(salary_structure_head.salary_structure_head_id,sd.salary_structure_head_ids)))) as voucher_heads From slab_details sd "
			+ "Where CONCAT(IfNull(sd.slab_details_id,''),'',IfNull(sd.slab_details_name,''),'',IfNull(sd.slab_details_short_name,''),'',IfNull(sd.description,''),'',"
			+ "IfNull(sd.created_by,''),'',IfNull(sd.created_date,''),'',IfNull(sd.created_username,'')) LIKE %?1%",nativeQuery =true)
	public List<Map<String,Object>> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select sd.slab_details_id as id,"
			+ "sd.slab_details_name as slab_details_name,sd.slab_details_short_name as slab_details_short_name,sd.salary_structure_head_ids as salary_structure_head_ids,"
			+ "sd.description as description,sd.created_by as created_by,sd.modified_by as modified_by,sd.created_date as created_date,"
			+ "sd.modified_date as modified_date,sd.active as active,sd.created_username as created_username,sd.modified_username as modified_username,"
			+ "(Select GROUP_CONCAT(voucher_head_new.voucher_head_short_name) From voucher_head_new where FIND_IN_SET(voucher_head_new.voucher_head_new_id,(select GROUP_CONCAT(salary_structure_head.voucher_head_new_id) From salary_structure_head where FIND_IN_SET(salary_structure_head.salary_structure_head_id,sd.salary_structure_head_ids)))) as voucher_head_short_names,"
			+ "(Select GROUP_CONCAT(voucher_head_new.voucher_head)  From voucher_head_new where FIND_IN_SET(voucher_head_new.voucher_head_new_id,(select GROUP_CONCAT(salary_structure_head.voucher_head_new_id) From salary_structure_head where FIND_IN_SET(salary_structure_head.salary_structure_head_id,sd.salary_structure_head_ids)))) as voucher_heads From slab_details sd",nativeQuery =true)
	public List<Map<String,Object>> findAll3(Pageable pageable);
	
	@Query(value = "select count(*) from SlabDetails sd where sd.slab_details_name=?1")
	public Integer fetchCountSlabName(String slab_details_name);
	
	@Query(value = "select count(*) from SlabDetails sd where sd.slab_details_short_name=?1")
	public Integer fetchCountSlabShortName(String slab_details_short_name);

}
