package com.au.repository;

import java.util.HashMap;
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

import com.au.model.VoucherHead;

@Transactional
@Repository
public interface VoucherHeadRepository extends JpaRepository<VoucherHead, Integer> {

	@Query(value = "SELECT school_id FROM voucher_head where voucher_head_new_id=?1 and active=true", nativeQuery = true)
	public List<Integer> getSchoolByVHead(Integer voucher_head_new_id);

	@Query(value = "SELECT count(*) FROM voucher_head where voucher_head_new_id=?1 and school_id=?2 and active=true", nativeQuery = true)
	public Integer findByVouHeadSchoolId1(Integer voucher_head_new_id, Integer hashMap);

	@Query(value = "SELECT school_id FROM voucher_head where voucher_head_new_id=?1 and voucher_type=?2 and active=true ", nativeQuery = true)
	public List<Integer> findByVouHeadSchoolId(Integer voucher_head_new_id, String voucher_type);
	
	@Query(value = "SELECT school_id FROM voucher_head where voucher_head_new_id=?1 and active=true ", nativeQuery = true)
	public List<Integer> findByVouHeadSchoolId2(Integer voucher_head_new_id);

//	@Query(value = "select new map(v.voucher_head_id as voucher_head_id,v.voucher_head_new_id as voucher_head_new_id,"
//			+ "v.school_id as school_id,v.tally_id as tally_id,v.opening_balance as opening_balance, v.created_by as created_by,v.modified_by as modified_by,"
//			+ "v.created_date as created_date,v.modified_date as modified_date,v.active as active,v.ledger_id as ledger_id,"
//			+ "v.voucher_type as voucher_type,v.budget_head as budget_head,v.created_username as created_username,"
//			+ "v.modified_username as modified_username,v.voucher_priority as voucher_priority,sch.school_name as school_name,"
//			+ "sch.school_name_short as school_name_short,tally.tally_fee_head as tally_fee_head,l.ledger_name as ledger_name,"
//			+ "ssh.salary_structure_head as salary_structure_head,ssh.category_short_name as category_short_name,vhn.voucher_head as voucher_head)"
//			+ " from  VoucherHead v left join Schools sch on v.school_id  = sch.school_id "
//			+ "left join VoucherHeadNew vhn on v.voucher_head_new_id  = vhn.voucher_head_new_id "
//			+ "left join TallyHead tally on v.tally_id =  tally.tally_id left join Ledger l on v.ledger_id=l.ledger_id "
//			+ "left join SalaryStructureHead ssh on v.salary_structure_head_id=ssh.salary_structure_head_id")
//	public List<HashMap<String, Object>> findAll1();
	
	@Query(value = "select new map(v.voucher_head_id as id,v.voucher_head_new_id as voucher_head_new_id,"
			+ "v.school_id as school_id,v.tally_id as tally_id,v.opening_balance as opening_balance, v.created_by as created_by,v.modified_by as modified_by,"
			+ "v.created_date as created_date,v.modified_date as modified_date,v.active as active,v.ledger_id as ledger_id,"
			+ "v.voucher_type as voucher_type,v.budget_head as budget_head,v.created_username as created_username,v.cash_or_bank as cash_or_bank,"
			+ "v.modified_username as modified_username,v.voucher_priority as voucher_priority,sch.school_name as school_name,"
			+ "sch.school_name_short as school_name_short,tally.tally_fee_head as tally_fee_head,l.ledger_name as ledger_name,"
			+ "ssh.category_short_name as category_short_name,vhn.voucher_head as voucher_head,v.priority as priority)"
			+ " from  VoucherHead v left join Schools sch on v.school_id  = sch.school_id "
			+ "left join VoucherHeadNew vhn on v.voucher_head_new_id  = vhn.voucher_head_new_id "
			+ "left join TallyHead tally on v.tally_id =  tally.tally_id left join Ledger l on v.ledger_id=l.ledger_id "
			+ "left join SalaryStructureHead ssh on v.salary_structure_head_id=ssh.salary_structure_head_id "
			+ "Where CONCAT(IfNull(v.voucher_head_id,''),'',IfNull(v.opening_balance,''),'',IfNull(v.created_by,''),'',"
			+ "IfNull(v.created_date,''),'',IfNull(v.budget_head,''),'',IfNull(v.created_username,''),'',"
			+ "IfNull(sch.school_name,''),'',IfNull(sch.school_name_short,''),'',IfNull(tally.tally_fee_head,''),'',"
			+ "IfNull(l.ledger_name,''),'','',IfNull(ssh.category_short_name,''),'',"
			+ "IfNull(vhn.voucher_head,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "select new map(v.voucher_head_id as id,v.voucher_head_new_id as voucher_head_new_id,"
			+ "v.school_id as school_id,v.tally_id as tally_id,v.opening_balance as opening_balance, v.created_by as created_by,v.modified_by as modified_by,"
			+ "v.created_date as created_date,v.modified_date as modified_date,v.active as active,v.ledger_id as ledger_id,"
			+ "v.voucher_type as voucher_type,v.budget_head as budget_head,v.created_username as created_username,v.cash_or_bank as cash_or_bank,"
			+ "v.modified_username as modified_username,v.voucher_priority as voucher_priority,sch.school_name as school_name,"
			+ "sch.school_name_short as school_name_short,tally.tally_fee_head as tally_fee_head,l.ledger_name as ledger_name,"
			+ "ssh.category_short_name as category_short_name,vhn.voucher_head as voucher_head,v.priority as priority)"
			+ " from  VoucherHead v left join Schools sch on v.school_id  = sch.school_id "
			+ "left join VoucherHeadNew vhn on v.voucher_head_new_id  = vhn.voucher_head_new_id "
			+ "left join TallyHead tally on v.tally_id =  tally.tally_id left join Ledger l on v.ledger_id=l.ledger_id "
			+ "left join SalaryStructureHead ssh on v.salary_structure_head_id=ssh.salary_structure_head_id")
	public Page<Object>  findAll2(Pageable pageable);

	@Query(value = "select vh from VoucherHead vh where vh.active=true")
	public List<VoucherHead> findAll11();

	@Modifying
	@Query(value = "update VoucherHead vh set vh.active=false where vh.voucher_head_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update VoucherHead vh set vh.active=true where vh.voucher_head_id=?1")
	public void update1(Integer id);

	@Modifying
	@Query(value = "update VoucherHead vh set vh.voucher_type= :voucher_type where vh.voucher_head_id= :voucher_head_id")
	public void updatevoucher(@Param(value = "voucher_head_id") Integer id, @Param("voucher_type") String voucher_type);

	@Query(value = "select new map(at.alias_id as alias_id,at.alias_name as alias_name) from AliasName at where at.active=true")
	public List<HashMap<String, Object>> getAliasDetails();
	
	@Query(value = "select new map(v.voucher_head_id as voucher_head_id,vhn.voucher_head as voucher_head,v.voucher_head_new_id as voucher_head_new_id) "
			+ "from VoucherHead v left join VoucherHeadNew vhn on v.voucher_head_new_id  = vhn.voucher_head_new_id where v.school_id=?1 and v.active=true")
	public List<HashMap<String, Object>> getVoucherBySchoolId(Integer school_id);
	
	
	@Query(value = "select new map(vhn.voucher_head as voucher_head,vhn.voucher_head_new_id as voucher_head_new_id) "
			+ "from VoucherHeadNew vhn "
			+ "where vhn.active=true And vhn.voucher_type in ('all','inflow') ")
	public List<HashMap<String, Object>> getVoucherTypeBySchoolId();
	
	@Query(value = "SELECT * FROM voucher_head where school_id=?1",nativeQuery = true)
	public List<VoucherHead> fetchHostelDetailsBySchoolHostel(Integer school_id);

	@Query(value = "SELECT vh.voucher_head_id FROM voucher_head vh where vh.voucher_head_new_id = ?1 and vh.active=true", nativeQuery = true)
	public Integer getVoucherHeadId(Integer voucher_head_new_id);


	@Query(value="select vh.voucher_head_short_name, vh.voucher_head, vh.voucher_head_new_id from  voucher_head_new vh  where vh.voucher_type='inflow' ",nativeQuery = true)
	public List<Map<String, Object>> getVoucherHeads();

	@Query(value = "select new map(vhn.voucher_head_new_id as voucher_head_new_id,vhn.voucher_type as voucher_type,vhn.is_vendor as is_vendor,vhn.ledger_id As ledger_id,"
			+ "vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name ) from VoucherHeadNew vhn "
			+ " where vhn.voucher_type not in  ('inflow') And vhn.active=true")
	public List<HashMap<String, Object>> getAllJournalTypeExceptInflow();
	
	@Query(value = "select vh from VoucherHead vh where vh.active=true")
	public List<VoucherHead> getAllVoucherHeadDetail();
	
	@Query(value = "select new map (vh.voucher_head_id as voucherHeadId, vh.voucher_type as voucherType,"
			+ "vhn.voucher_head_new_id as vouccherHeadNewId,"
			+ "vhn.voucher_head as voucherHeadNewName, vh.ledger_id as ledgerId , l.ledger_name as ledgerName) "
			+ "from VoucherHead vh "
			+ "left join VoucherHeadNew vhn on vh.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "left join Ledger l on vh.ledger_id= l.ledger_id "
			+ "where vh.voucher_type= 'inflow' And vh.active=true And vhn.active=true")
	public List<Object> getInFlowVoucherHead();

}
