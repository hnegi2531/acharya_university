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
import com.au.model.VoucherHeadNew;

@Transactional
@Repository
public interface VoucherHeadNewRepository extends JpaRepository<VoucherHeadNew, Integer> {

	@Query(value = "select h from VoucherHeadNew h where h.active=true")
	public List<VoucherHeadNew> findAll1();

	@Modifying
	@Query(value = "update VoucherHeadNew h set h.active=false where h.voucher_head_new_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update VoucherHeadNew h set h.active=true where h.voucher_head_new_id=?1")
	public void update1(Integer id);
	
	@Query(value = "Select new map(vhn.voucher_head_new_id as id,vhn.voucher_head as voucher_head,vhn.is_common as is_common,vhn.is_exam as is_exam,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,vhn.created_by as created_by,vhn.is_salaries as is_salaries,vhn.is_vendor as is_vendor,"
			+ "vhn.modified_by as modified_by,vhn.created_date as created_date,vhn.modified_date as modified_date,vhn.hostel_status as hostel_status,"
			+ "vhn.school_id as school_id,vhn.tally_id as tally_id,vhn.ledger_id as ledger_id,vhn.voucher_type as voucher_type,vhn.budget_head as budget_head,"
			+ "vhn.voucher_priority as voucher_priority,vhn.salary_structure_head_id as salary_structure_head_id,vhn.cash_or_bank as cash_or_bank,"
			+ "vhn.opening_balance as opening_balance,vhn.priority as priority,le.ledger_name as ledger_name,le.ledger_short_name as ledger_short_name,"
			+ "vhn.active as active,vhn.created_username as created_username,vhn.modified_username as modified_username) "
			+ "From VoucherHeadNew vhn "
			+ "left join Ledger le on le.ledger_id=vhn.ledger_id "
			+ "Where CONCAT(IfNull(vhn.voucher_head_new_id,''),'',IfNull(vhn.voucher_head,''),'',IfNull(vhn.voucher_head_short_name,''),"
			+ "'',IfNull(vhn.created_by,''),'',IfNull(vhn.created_date,''),'',IfNull(vhn.is_common,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(vhn.voucher_head_new_id as id,vhn.voucher_head as voucher_head,vhn.is_common as is_common,vhn.is_exam as is_exam,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,vhn.created_by as created_by,vhn.is_salaries as is_salaries,vhn.is_vendor as is_vendor,"
			+ "vhn.modified_by as modified_by,vhn.created_date as created_date,vhn.modified_date as modified_date,vhn.hostel_status as hostel_status,"
			+ "vhn.school_id as school_id,vhn.tally_id as tally_id,vhn.ledger_id as ledger_id,vhn.voucher_type as voucher_type,vhn.budget_head as budget_head,"
			+ "vhn.voucher_priority as voucher_priority,vhn.salary_structure_head_id as salary_structure_head_id,vhn.cash_or_bank as cash_or_bank,"
			+ "vhn.opening_balance as opening_balance,vhn.priority as priority,le.ledger_name as ledger_name,le.ledger_short_name as ledger_short_name,"
			+ "vhn.active as active,vhn.created_username as created_username,vhn.modified_username as modified_username) "
			+ "From VoucherHeadNew vhn "
			+ "left join Ledger le on le.ledger_id=vhn.ledger_id")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) From VoucherHeadNew vhn where vhn.voucher_head=?1 and vhn.active=true")
	public Integer countOfVoucherHead(String voucher_head);
	
	@Query(value = "SELECT count(*) From VoucherHeadNew vhn where vhn.voucher_head_short_name=?1 and vhn.active=true")
	public Integer countOfVoucherHeadShortName(String voucher_head_short_name);
	
	@Query(value = "Select s.school_id as school_id,s.school_name as school_name,s.school_name_short as school_name_short from schools s "
			+ "where s.school_id NOT IN (Select v.school_id from voucher_head v where v.voucher_head_new_id=?1) and s.active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchUnassignedSchoolDetails(Integer voucher_head_new_id);
	
	@Query(value = "select vhn from VoucherHeadNew vhn where vhn.is_salaries = true and vhn.active=true")
	public List<VoucherHeadNew> VoucherHeadNewDetailsOnIsSalaries();
	
	@Query(value = "select vhn from VoucherHeadNew vhn where vhn.hostel_status = true and vhn.active=true")
	public List<VoucherHeadNew> voucherHeadNewOnHostelStatus();
	
	@Query(value = "select vhn.voucher_head from VoucherHeadNew vhn where vhn.voucher_head_new_id = ?1 and vhn.active=true")
	public String getVoucherName(Integer voucher_head_new_id);
	
	@Query(value = "Select new map(vhn.voucher_head_new_id as voucherHeadNewId,vhn.voucher_head as voucherHead) from VoucherHeadNew vhn Where vhn.is_vendor=true And vhn.active=true")
	public List<HashMap<String, Object>> getVoucherHeadNewData();
	
	@Query(value = "Select new map(vhn.voucher_head_new_id as voucherHeadNewId,vhn.voucher_head as voucherHead) "
			+ "from VoucherHeadNew vhn Where vhn.voucher_type='outflow' And vhn.active=true")
	public List<HashMap<String, Object>> getVoucherHeadNewDataOutflow();
	
	@Query(value = "select h from VoucherHeadNew h where h.active=true And h.cash_or_bank=true")
	public List<VoucherHeadNew> fetchVoucherHeadNewDetailsBasedOnCashOrBank();

	@Query(value="select v.voucher_head_new_id from VoucherHeadNew v where v.ledger_id=:ledgerId ")
	public List<Integer> getAllVoucherIdByLedgerId(Integer ledgerId);
	
	@Query(value = "select vhn from VoucherHeadNew vhn where vhn.hostel_status = true And vhn.active=true")
	public List<VoucherHeadNew> voucherHeadDetailsOnHostelStatus();

	@Query(value = "select new map(v.voucher_head_id as voucher_head_id,vhn.voucher_head as voucher_head,vhn.voucher_head_new_id as voucher_head_new_id) "
			+" from VoucherHead v "
			+ "left join VoucherHeadNew vhn on v.voucher_head_new_id  = vhn.voucher_head_new_id "
			+ "where v.school_id=?1 and v.voucher_type='inflow' and vhn.active=true")
	public List<HashMap<String, Object>> fetchVoucherHeadIds(Integer school_id);

	
	
	@Query(value = "Select new map(vhn.voucher_head_new_id as voucherHeadNewId,vhn.voucher_head as voucherHead,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,vhn.voucher_type as voucher_type,vhn.ledger_id as ledger_id,"
			+ "le.ledger_name as ledger_name,le.ledger_short_name as ledger_short_name) from VoucherHeadNew vhn "
			+ "left join Ledger le on le.ledger_id  = vhn.ledger_id "
			+ "Where vhn.budget_head=true And vhn.active=true")
	public List<HashMap<String, Object>> getVoucherHeadDataBasedOnBudget();

	@Query(value = "select h from VoucherHeadNew h where h.voucher_head_new_id not in ?1 And h.active=true")
	public List<VoucherHeadNew> voucherHeadDetails(List<Integer> numberIntegers);
	
	
	@Query(value = "select vhn from VoucherHeadNew vhn where vhn.voucher_head_new_id in (?1) ")
	public List<VoucherHeadNew> voucherHeadDetailsByVoucherHeadNewIds(List<Integer> voucherHeadNewIds);

	@Query(value = "Select GROUP_CONCAT(vhn.voucher_head ORDER BY voucher_head_new_id ASC) From voucher_head_new vhn where vhn.voucher_head_new_id in (:voucher_head_new_id)", nativeQuery = true)
	public String getCommaSeperartedVoucher_head(List<Integer> voucher_head_new_id);

	@Query(value = "Select sc.school_id from schools sc where sc.school_id not in (?1) and sc.active=true",nativeQuery = true)
	List<Integer> schoolIdsForJournalVoucherCreation(List<Integer> createdSchoolIds);

	@Query(value = "Select * from voucher_head_new vhn Where vhn.voucher_head_new_id in (7,8,9,10,11,330,331,332,439,440,441,485,486) and vhn.active=true Order by vhn.voucher_head_new_id Desc",nativeQuery=true)
	List<VoucherHeadNew> getVoucherHeadNew();

	@Query(value = "select * from voucher_head_new where voucher_head_new_id = ?1 and active = true ",nativeQuery = true)
	VoucherHeadNew getVoucherHeadBYVoucherID(Integer VoucherHeadNewId);

	@Query(value = "select vhn from VoucherHeadNew vhn where vhn.voucher_type in ('all','journal') And vhn.active=true")
	public List<VoucherHeadNew> VoucherHeadNewDetailsOnJournal();

	@Query(value = "select vhn from VoucherHeadNew vhn where vhn.voucher_type not in ('journal') And vhn.active=true")
	public List<VoucherHeadNew> VoucherHeadNewDetailsWoJournal();

}
