package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.VendorOpeningBalance;

@Transactional
@Repository
public interface VendorOpeningBalanceRepository extends JpaRepository<VendorOpeningBalance, Integer>{
	
	@Query(value = "select vb from VendorOpeningBalance vb where vb.active=true")
	public List<VendorOpeningBalance> findAll11();
	
	@Modifying
	@Query(value = "update VendorOpeningBalance vb set vb.active=false where vb.ob_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update VendorOpeningBalance vb set vb.active=true where vb.ob_id=?1")
	public void update1(Integer id);
	
	@Query(value = "select new map(vb.ob_id as id,vb.school_id as school_id,vb.opening_balance as opening_balance,"
			+ "vb.voucher_head_new_id as voucher_head_new_id,vb.created_by as created_by,vb.modified_by as modified_by,"
			+ "vb.created_date as created_date,vb.modified_date as modified_date,vb.active as active,"
			+ "vb.created_username as created_username,vb.modified_username as modified_username,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,sch.school_name as school_name,"
			+ "sch.school_name_short as school_name_short,vhn.voucher_head as voucher_head) from VendorOpeningBalance vb "
			+ "Left join Schools sch on sch.school_id=vb.school_id "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id=vb.voucher_head_new_id "
			+ "where CONCAT(IfNull(vb.ob_id,''),'',IfNull(vb.school_id,''),'',IfNull(vb.opening_balance,''),"
			+ "'',IfNull(vhn.voucher_head_new_id,''),'',IfNull(vb.created_date,''),'',IfNull(vb.created_by,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(vb.ob_id as id,vb.school_id as school_id,vb.opening_balance as opening_balance,"
			+ "vb.voucher_head_new_id as voucher_head_new_id,vb.created_by as created_by,vb.modified_by as modified_by,"
			+ "vb.created_date as created_date,vb.modified_date as modified_date,vb.active as active,"
			+ "vb.created_username as created_username,vb.modified_username as modified_username,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,sch.school_name as school_name,"
			+ "sch.school_name_short as school_name_short,vhn.voucher_head as voucher_head) from VendorOpeningBalance vb "
			+ "Left join Schools sch on sch.school_id=vb.school_id "
			+ "Left join VoucherHeadNew vhn on vhn.voucher_head_new_id=vb.voucher_head_new_id ")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Query(value = "select count(*) from VendorOpeningBalance vb where vb.school_id=?1 and vb.voucher_head_new_id=?2 and vb.active=true")
	public Integer countSchoolIdAndVendorId(Integer a,Integer voucher_head_new_id);

	@Query(value = "select vb from VendorOpeningBalance vb where vb.ob_id=?1 and active=true")
	public VendorOpeningBalance getVendorOpeningBalanceByOb_id(Integer ob_id);

	@Query(value = "Select vb from VendorOpeningBalance vb where vb.voucher_head_new_id=?1")
	public List<VendorOpeningBalance> getVendorOpeningBalanceByVoucherHeadId(Integer voucher_head_new_id);

}
