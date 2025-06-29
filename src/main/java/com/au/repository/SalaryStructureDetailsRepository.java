package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.SalaryStructureDetails;

@Transactional
@Repository
public interface SalaryStructureDetailsRepository extends JpaRepository<SalaryStructureDetails, Integer> {

	@Query(value = "select ssd from SalaryStructureDetails ssd where ssd.active=true")
	public List<SalaryStructureDetails> findAll1();

	@Query(value = "select new map(ssd.salary_structure_details_id as id,ssd.salary_structure_id as salary_structure_id,ssd.created_by as created_by,ssd.modified_by as modified_by,"
			+ "ssd.created_date as created_date,ssd.modified_date as modified_date,ssd.created_username as created_username,ssd.percentage as percentage,ssd.remarks as remarks,"
			+ "ssd.modified_username as modified_username,ssd.active as active,ssd.from_date as from_date,ssd.value as value,ssd.to_date as to_date,ssd.gross_limit as gross_limit,"
			+ "ssd.salary_structure_head_id as salary_structure_head_id,ssd.salary_category as salary_category,ssd.formula_name as formula_name,ssd.gross_percentage as gross_percentage,"
			+ "ssd.testing_expression as testing_expression,ss.salary_structure as salary_structure,ssd.gross_expression as gross_expression,ssd.pension_fund as pension_fund,"
			+ "ssd.slab_details_id as slab_details_id,vhn.voucher_head_short_name as voucher_head_short_name,ssh.priority as priority,ssd.isPayDay as isPayDay,"
			+ "ssh.category_name_type as category_name_type,ssd.voucher_head_new_ids as voucher_head_new_ids) from SalaryStructureDetails ssd "
			+ "join SalaryStructure ss on ssd.salary_structure_id=ss.salary_structure_id "
			+ "left join SalaryStructureHead ssh on ssd.salary_structure_head_id=ssh.salary_structure_head_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id=ssh.voucher_head_new_id "
			+ "Where CONCAT(IfNull(ssd.salary_structure_id,''),'',IfNull(ssd.salary_category,''),'',IfNull(ssd.print_names,''),'',"
			+ "IfNull(ss.salary_structure,''),'',IfNull(vhn.voucher_head_short_name,''),'',IfNull(ssd.created_date,''),'',IfNull(ssd.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "select new map(ssd.salary_structure_details_id as id,ssd.salary_structure_id as salary_structure_id,ssd.created_by as created_by,ssd.modified_by as modified_by,"
			+ "ssd.created_date as created_date,ssd.modified_date as modified_date,ssd.created_username as created_username,ssd.percentage as percentage,ssd.remarks as remarks,"
			+ "ssd.modified_username as modified_username,ssd.active as active,ssd.from_date as from_date,ssd.value as value,ssd.to_date as to_date,ssd.gross_limit as gross_limit,"
			+ "ssd.salary_structure_head_id as salary_structure_head_id,ssd.salary_category as salary_category,ssd.formula_name as formula_name,ssd.gross_percentage as gross_percentage,"
			+ "ssd.testing_expression as testing_expression,ss.salary_structure as salary_structure,ssd.gross_expression as gross_expression,ssd.pension_fund as pension_fund,"
			+ "ssd.slab_details_id as slab_details_id,vhn.voucher_head_short_name as voucher_head_short_name,ssh.priority as priority,ssd.isPayDay as isPayDay,"
			+ "ssh.category_name_type as category_name_type,ssd.voucher_head_new_ids as voucher_head_new_ids) from SalaryStructureDetails ssd "
			+ "join SalaryStructure ss on ssd.salary_structure_id=ss.salary_structure_id "
			+ "left join SalaryStructureHead ssh on ssd.salary_structure_head_id=ssh.salary_structure_head_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id=ssh.voucher_head_new_id ")
	public Page<Object> findAll3(Pageable pageable);

	@Modifying
	@Query(value = "update SalaryStructureDetails ssd set ssd.active=false where ssd.salary_structure_details_id=?1")
	public void update(Integer salary_structure_id);

	@Modifying
	@Query(value = "update SalaryStructureDetails ssd set ssd.active=true where ssd.salary_structure_details_id=?1")
	public void update1(Integer salary_structure_id);

	@Query(value = "select new map(ssd.salary_structure_details_id as salary_structure_details_id,"
			+ "ssd.salary_structure_id as salary_structure_id,ssd.salary_structure_head_id as salary_structure_head_id,"
			+ "ssd.salary_category as salary_category,ssd.print_names as print_names,ssd.gross_limit as gross_limit,"
			+ "ssd.slab_details_id as slab_details_id,ssd.from_date as from_date,ssd.to_date as to_date,ssd.remarks as remarks,"
			+ "ssd.formula as formula,ssd.percentage as percentage,ssd.created_by as created_by,ssd.modified_by as modified_by,"
			+ "ssd.created_date as created_date,ssd.modified_date as modified_date,ssd.active as active,ssh.priority as priority,"
			+ "ssd.created_username as created_username,ssd.modified_username as modified_username,ssd.formula_name as formula_name,"
			+ "ssh.print_name as salaryStructureHeadPrintName,ssd.testing_expression as testing_expression,ssd.value as value,ssd.isPayDay as isPayDay,"
			+ "ssd.gross_percentage as gross_percentage,ssd.gross_expression as gross_expression,ssh.category_name_type as category_name_type,"
			+ "ss.salary_structure as salary_structure,vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,"
			+ "ssh.category_name_type as category_name_type,ssd.voucher_head_new_ids as voucher_head_new_ids) from SalaryStructureDetails ssd "
			+ "Inner join SalaryStructureHead ssh on ssd.salary_structure_head_id=ssh.salary_structure_head_id "
			+ "left join SalaryStructure ss on ssd.salary_structure_id=ss.salary_structure_id "
			+ "left join VoucherHeadNew vhn on vhn.voucher_head_new_id=ssh.voucher_head_new_id "
			+ "where ssd.salary_structure_id=?1 and ssd.active=true")
	public List<HashMap<String, Object>> getFormulaDetails(Integer salary_structure_id);

	@Query(value= "select * from salary_structure_details where salary_structure_id=?1 and salary_structure_head_id=?2 "
			+ "order by created_date desc limit 1",nativeQuery = true)
	public SalaryStructureDetails getAllDeatils(Integer salary_structure_id, Integer salary_structure_head_id);

	@Modifying
	@Query(value = "update SalaryStructureDetails ssd set ssd.active=false "
			+ "where ssd.salary_structure_id=?1 and salary_structure_head_id=?2 ")
	public void updateActive(Integer salary_structure_id,Integer salary_structure_head_id);
	
	@Query(value = "select count(*) from salary_structure_details where salary_structure_id=?1 "
			+ "and salary_structure_head_id=?2",nativeQuery = true)
	public Integer getStructureAndHeadDetails(Integer salary_structure_id, Integer salary_structure_head_id);

	@Query(value = "update salary_structure_details set active=false where salary_structure_id=?1 "
			+ "and salary_structure_head_id=?2 order by created_date desc limit 1",nativeQuery = true)
	public SalaryStructureDetails updateActive1(Integer salary_structure_id, Integer salary_structure_head_id);

	@Modifying
	@Query(value = "update salary_structure_details ssd set ssd.active=false where salary_structure_id=?1 "
			+ "and salary_structure_head_id=?2 order by created_date desc limit 1",nativeQuery = true)
	public Object savesssss(Integer salary_structure_id, Integer salary_structure_head_id); 
	
	@Query(value = "select * from salary_structure_details where salary_structure_id=?1 and active=true",nativeQuery=true)
	public List<SalaryStructureDetails> getSalaryStructureDetails(Integer salary_structure_id);
	
	@Query(value = "select ssd.percentage from SalaryStructureDetails ssd where ssd.salary_structure_head_id=?1 and ssd.active=true")
	public Float getPercentage(Integer salary_structure_head_id);

}