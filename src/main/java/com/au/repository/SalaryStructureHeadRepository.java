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
import com.au.model.SalaryStructureHead;

@Transactional
@Repository
public interface SalaryStructureHeadRepository extends JpaRepository<SalaryStructureHead, Integer> {
	
	@Query(value="Select new map(ssh.salary_structure_head_id as salary_structure_head_id,ssh.print_name as print_name,"
			+ "ssh.priority as priority,ssh.category_name_type as category_name_type,vhn.voucher_head as voucher_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,vhn.voucher_head_new_id as voucher_head_new_id) from SalaryStructureHead ssh "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id = ssh.voucher_head_new_id where ssh.category_name_type='Earning' and ssh.active=true")
	public List<HashMap<String, Object>> findAll1();
	
	@Modifying
	@Query(value = "update SalaryStructureHead ssh set ssh.active=false where ssh.salary_structure_head_id=?1")
	public void update(Integer salary_structure_id);
	
	@Modifying
	@Query(value = "update SalaryStructureHead ssh set ssh.active=true where ssh.salary_structure_head_id=?1")
	public void update1(Integer salary_structure_id);

	@Query(value = "select new map(ssh.print_name as print_name) from SalaryStructureHead ssh "
			+ "where ssh.salary_structure_head_id=?1")
	public List<HashMap<String, Object>> getPrintNames(Integer salary_structure_head_id);
	
	@Query(value = "Select new map(ssh.salary_structure_head_id as id,ssh.voucher_head_new_id as voucher_head_new_id,"
			+ "ssh.print_name as print_name,ssh.priority as priority,ssh.category_short_name as category_short_name,"
			+ "ssh.category_name_type as category_name_type,ssh.created_by as created_by,ssh.modified_by as modified_by,"
			+ "ssh.created_date as created_date,ssh.modified_date as modified_date,ssh.active as active,ssh.created_username as created_username,"
			+ "ssh.modified_username as modified_username,vhn.voucher_head_short_name as voucher_head_short_name) From SalaryStructureHead ssh "
			+ "Left Join VoucherHeadNew vhn on vhn.voucher_head_new_id =ssh.voucher_head_new_id "
			+ "Where CONCAT(IfNull(ssh.salary_structure_head_id,''),'',IfNull(vhn.voucher_head_short_name,''),'',"
			+ "IfNull(ssh.print_name,''),'',IfNull(ssh.category_short_name,''),'',IfNull(ssh.category_name_type,''),'',"
			+ "IfNull(ssh.created_by,''),'',IfNull(ssh.created_date,''),'',IfNull(ssh.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(ssh.salary_structure_head_id as id,ssh.voucher_head_new_id as voucher_head_new_id,"
			+ "ssh.print_name as print_name,ssh.priority as priority,ssh.category_short_name as category_short_name,"
			+ "ssh.category_name_type as category_name_type,ssh.created_by as created_by,ssh.modified_by as modified_by,"
			+ "ssh.created_date as created_date,ssh.modified_date as modified_date,ssh.active as active,ssh.created_username as created_username,"
			+ "ssh.modified_username as modified_username,vhn.voucher_head_short_name as voucher_head_short_name) From SalaryStructureHead ssh "
			+ "Left Join VoucherHeadNew vhn on vhn.voucher_head_new_id =ssh.voucher_head_new_id ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "Select count(*) from SalaryStructureHead ssh where ssh.voucher_head_new_id=?1 and active=true")
	public Integer getCountSalaryStructureHead(Integer voucher_head_new_id);
	
	@Query(value = "select count(*) from SalaryStructureHead ssh where ssh.priority=?1 and active=true")
	public Integer getCountPriority(Integer priority);
	
	@Query(value = "select new map(ssh.salary_structure_head_id as salary_structure_head_id,ssh.category_name_type as category_name_type,"
			+ "ssh.voucher_head_new_id as voucher_head_new_id,ssh.print_name as print_name) from SalaryStructureHead ssh "
			+ "where ssh.voucher_head_new_id=?1 and ssh.active=true")
	public List<HashMap<String, Object>> categoryNameTypes(Integer voucher_head_new_id);
	
	@Query(value = "select * from salary_structure_head where salary_structure_head_id=?1",nativeQuery=true)
	public SalaryStructureHead getSalaryStructureHead(Integer salary_structure_head_id);
	
	@Query(value="Select new map(ssh.salary_structure_head_id as salary_structure_head_id,ssh.print_name as print_name,"
			+ "ssh.priority as priority,ssh.category_name_type as category_name_type,vhn.voucher_head as voucher_head,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,vhn.voucher_head_new_id as voucher_head_new_id) from SalaryStructureHead ssh "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id = ssh.voucher_head_new_id where ssh.active=true")
	public List<HashMap<String, Object>> allSalaryStructureHeadAndVocherHeadDetails();
	
	@Query(value="select new map(vhn.voucher_head_new_id as voucher_head_new_id,vhn.voucher_head as voucher_head_names) from VoucherHeadNew vhn where "
			+ "voucher_head_new_id IN (select ssh.voucher_head_new_id from SalaryStructureHead ssh "
			+ "where ssh.salary_structure_head_id IN (select ssd.salary_structure_head_id from SalaryStructureDetails ssd "
			+ "where ssd.salary_structure_id=?1 and ssd.active=true) "
			+ "and ssh.active=true and ssh.category_name_type='Earning') and vhn.active=true")
	public List<HashMap<String, Object>> findAll4(Integer salary_structure_id);

}