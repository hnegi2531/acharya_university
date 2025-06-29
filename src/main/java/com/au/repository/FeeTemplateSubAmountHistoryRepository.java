package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.FeeTemplateSubAmountHistory;

@Transactional
@Repository
public interface FeeTemplateSubAmountHistoryRepository extends JpaRepository<FeeTemplateSubAmountHistory, Integer> {

//	@Query(value = "select ftsah from FeeTemplateSubAmountHistory ftsah where ftsah.fee_template_id=?1")
	@Query(value = "Select new map(ftsah.fee_sub_amt_his_id as id,ftsah.fee_sub_amt_id as fee_sub_amt_id,"
			+ "ftsah.fee_template_id as fee_template_id,ftsah.voucher_head as voucher_head,"
			+ "ftsah.board_unique_name as board_unique_name,ftsah.alias_name as alias_name,ftsah.year1_amt as year1_amt,"
			+ "ftsah.year2_amt as year2_amt,ftsah.year3_amt as year3_amt,ftsah.year4_amt as year4_amt,"
			+ "ftsah.year5_amt as year5_amt,ftsah.year6_amt as year6_amt,ftsah.year7_amt as year7_amt,ftsah.year8_amt as year8_amt,"
			+ "ftsah.year9_amt as year9_amt,ftsah.year10_amt as year10_amt,ftsah.year11_amt as year11_amt,ftsah.year12_amt as year12_amt,"
			+ "ftsah.total_amt as total_amt,ftsah.created_by as created_by,ftsah.modified_date as modified_date,ftsah.active as active,"
			+ "ft.approved_date as approved_date,ft.approved_status as approved_status,ft.approved_by as approved_by,ua.username as userName,"
			+ "ftsah.remarks as remarks,ftsah.created_username as created_username) From FeeTemplateSubAmountHistory ftsah "
			+ "left join FeeTemplate ft on ft.fee_template_id=ftsah.fee_template_id "
			+ "left join UserAuthentication ua on ft.approved_by=ua.id "
			+ "where ftsah.fee_template_id=?1")
	public List<Map<String, Object>> findAll1(Integer fee_template_id);
	
	@Query(value = "Select new map(ftsah.fee_sub_amt_his_id as id,ftsah.fee_sub_amt_id as fee_sub_amt_id,"
			+ "ftsah.fee_template_id as fee_template_id,ftsah.voucher_head as voucher_head,"
			+ "ftsah.board_unique_name as board_unique_name,ftsah.alias_name as alias_name,ftsah.year1_amt as year1_amt,"
			+ "ftsah.year2_amt as year2_amt,ftsah.year3_amt as year3_amt,ftsah.year4_amt as year4_amt,"
			+ "ftsah.year5_amt as year5_amt,ftsah.year6_amt as year6_amt,ftsah.year7_amt as year7_amt,ftsah.year8_amt as year8_amt,"
			+ "ftsah.year9_amt as year9_amt,ftsah.year10_amt as year10_amt,ftsah.year11_amt as year11_amt,ftsah.year12_amt as year12_amt,"
			+ "ftsah.total_amt as total_amt,ftsah.created_by as created_by,ftsah.modified_date as modified_date,ftsah.active as active,ftsah.remarks as remarks,ftsah.created_username as created_username) From FeeTemplateSubAmountHistory ftsah "
			+ "Where CONCAT(IfNull(ftsah.fee_sub_amt_his_id,''),'',IfNull(ftsah.voucher_head,''),'',IfNull(ftsah.board_unique_name ,''),'',"
			+ "IfNull(ftsah.alias_name,''),'',IfNull(ftsah.remarks,''),'',IfNull(ftsah.created_username,''),'',IfNull(ftsah.modified_date,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(ftsah.fee_sub_amt_his_id as id,ftsah.fee_sub_amt_id as fee_sub_amt_id,"
			+ "ftsah.fee_template_id as fee_template_id,ftsah.voucher_head as voucher_head,"
			+ "ftsah.board_unique_name as board_unique_name,ftsah.alias_name as alias_name,ftsah.year1_amt as year1_amt,"
			+ "ftsah.year2_amt as year2_amt,ftsah.year3_amt as year3_amt,ftsah.year4_amt as year4_amt,"
			+ "ftsah.year5_amt as year5_amt,ftsah.year6_amt as year6_amt,ftsah.year7_amt as year7_amt,ftsah.year8_amt as year8_amt,"
			+ "ftsah.year9_amt as year9_amt,ftsah.year10_amt as year10_amt,ftsah.year11_amt as year11_amt,ftsah.year12_amt as year12_amt,"
			+ "ftsah.total_amt as total_amt,ftsah.created_by as created_by,ftsah.modified_date as modified_date,ftsah.active as active,ftsah.remarks as remarks,ftsah.created_username as created_username) From FeeTemplateSubAmountHistory ftsah ")
	public Page<Object> findAll2(Pageable pageable);

}
