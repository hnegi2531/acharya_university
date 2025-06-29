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

import com.au.dto.FeeTemplateDTO;
import com.au.model.FeeTemplateSubAmount;

@Transactional
@Repository
public interface FeeTemplateSubAmountRepository extends JpaRepository<FeeTemplateSubAmount, Integer> {

	@Query(value = "select ft.fee_template_id as fee_template_id,ft.fee_sub_amt_id as fee_sub_amt_id,"
			+ "vnh.voucher_head as voucher_head,ft.voucher_head_new_id as voucher_head_new_id,"
			+ "ft.created_by as created_by,ft.created_username as created_username,"
			+ "bn.board_unique_name as board_unique_name,bn.board_unique_id as board_unique_id,an.alias_name as alias_name,"
			+ "an.alias_id as alias_id,ft.receive_for_all_year as receive_for_all_year,ft.year1_amt as year1_amt,"
			+ "ft.year2_amt as year2_amt,ft.year3_amt as year3_amt,ft.year4_amt as year4_amt,ft.year5_amt as year5_amt,"
			+ "ft.year6_amt as year6_amt,ft.year7_amt as year7_amt,ft.year8_amt as year8_amt,ft.year9_amt as year9_amt,"
			+ "ft.year10_amt as year10_amt,ft.year11_amt as year11_amt,ft.year12_amt as year12_amt,ft.total_amt as total_amt,"
			+ "ft.remarks as remarks,ft.active as active,fts.fee_year1_amt as fee_year1_amt,bn.board_unique_short_name as board_unique_short_name,"
			+ "fts.fee_year2_amt as fee_year2_amt,fts.fee_year3_amt as fee_year3_amt,fts.fee_year4_amt as fee_year4_amt,"
			+ "fts.fee_year5_amt as fee_year5_amt,fts.fee_year6_amt as fee_year6_amt,fts.fee_year7_amt as fee_year7_amt,"
			+ "fts.fee_year8_amt as fee_year8_amt,fts.fee_year9_amt as fee_year9_amt,fts.fee_year10_amt as fee_year10_amt,"
			+ "fts.fee_year11_amt as fee_year11_amt,fts.fee_year12_amt as fee_year12_amt,"
			+ "fts.fee_year_total_amount as fee_year_total_amount from fee_template_sub_amount ft "
			+ "left join voucher_head_new vnh on ft.voucher_head_new_id=vnh.voucher_head_new_id "
			+ "left join board bn on ft.board_unique_id=bn.board_unique_id "
			+ "left join alias_name an on ft.alias_id=an.alias_id "
			+ "left join fee_template fts on ft.fee_template_id=fts.fee_template_id "
			+ "where ft.fee_template_id=?1 and ft.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchByFeeTemplateId(Integer fee_template_id);

	@Query(value = "SELECT * FROM fee_template_sub_amount where fee_template_id=?1", nativeQuery = true)
	public List<FeeTemplateSubAmount> fetchByFeeTemplateId1(Integer fee_template_id);

	// @Query(value = "select f from FeeTemplate f where fee_template_id=?1")
	// @Query(value = "select new map(ftsa.fee_sub_amt_id as
	// fee_sub_amt_id,ftsa.fee_template_id as fee_template_id,ftsa.voucher_head_id
	// as voucher_head_id,ftsa.board_unique_id as board_unique_id,ftsa.alias_id as
	// alias_id,ftsa.year1_amt as year1_amt,ftsa.year2_amt as
	// year2_amt,ftsa.year3_amt as year3_amt,ftsa.year4_amt as
	// year4_amt,ftsa.year5_amt as year5_amt,ftsa.year6_amt as
	// year6_amt,ftsa.year7_amt as year7_amt,ftsa.year8_amt as
	// year8_amt,ftsa.year9_amt as year9_amt,ftsa.year10_amt as
	// year10_amt,ftsa.year11_amt as year2_amt,ftsa.year12_amt as
	// year12_amt,ftsa.total_amt as total_amt,ftsa.created_by as
	// created_by,ftsa.modified_by as modified_by,ftsa.receive_for_all_year as
	// receive_for_all_year,ftsa.active as active,ftsa.remarks as
	// remarks,ft.fee_year1_amt as fee_year1_amt,ft.fee_year2_amt as
	// fee_year2_amt,ft.fee_year3_amt as fee_year3_amt,ft.fee_year4_amt as
	// fee_year4_amt,ft.fee_year5_amt as fee_year5_amt,ft.fee_year6_amt as
	// fee_year6_amt,ft.fee_year7_amt as fee_year7_amt,ft.fee_year8_amt as
	// fee_year8_amt,ft.fee_year9_amt as fee_year9_amt,ft.fee_year10_amt as
	// fee_year10_amt,ft.fee_year11_amt as fee_year11_amt,ft.fee_year12_amt as
	// fee_year12_amt) from FeeTemplateSubAmount ftsa left join FeeTemplate ft on
	// ftsa.fee_template_id=ft.fee_template_id")
	// public FeeTemplateSubAmount save1(FeeTemplateSubAmount feetemplatesubamount);

	@Query(value = "select new map(ft.fee_sub_amt_id as fee_sub_amt_id,"
			+ "vnh.voucher_head as voucher_head,ft.voucher_head_new_id as voucher_head_new_id,"
			+ "bn.board_unique_name as board_unique_name,bn.board_unique_id as board_unique_id,an.alias_name as alias_name,"
			+ "an.alias_id as alias_id, f.Is_paid_at_board as Is_paid_at_board, ft.receive_for_all_year as receive_for_all_year, ft.year1_amt as year1_amt,"
			+ "ft.year2_amt as year2_amt,ft.year3_amt as year3_amt,ft.year4_amt as year4_amt,ft.year5_amt as year5_amt,"
			+ "ft.year6_amt as year6_amt,ft.year7_amt as year7_amt,ft.year8_amt as year8_amt,ft.year9_amt as year9_amt,"
			+ "ft.year10_amt as year10_amt,ft.year11_amt as year11_amt,ft.year12_amt as year12_amt,ft.total_amt as total_amt,"
			+ "ft.remarks as remarks,f.lat_year_sem as lat_year_sem) from FeeTemplateSubAmount ft "
			+ "left join VoucherHeadNew vnh on ft.voucher_head_new_id=vnh.voucher_head_new_id "
			+ "left join FeeTemplate f on ft.fee_template_id=f.fee_template_id "
			+ "left join Board bn on ft.board_unique_id=bn.board_unique_id "
			+ "left join AliasName an on ft.alias_id=an.alias_id "
			+ "where ft.fee_template_id=?1 and ft.active=true")
	public List<HashMap<String, Object>> feeTemplateSubAmountForFeeReceipt(Integer fee_template_id);

	@Query(value = "select new map(ft.fee_sub_amt_id as fee_sub_amt_id,"
			+ "vnh.voucher_head as voucher_head,ft.voucher_head_new_id as voucher_head_new_id,"
			+ "bn.board_unique_name as board_unique_name,bn.board_unique_id as board_unique_id,an.alias_name as alias_name,"
			+ "an.alias_id as alias_id, f.Is_paid_at_board as is_paid_at_board, ft.receive_for_all_year as receive_for_all_year, Sum(ft.year1_amt) as year1_amt,"
			+ "Sum(ft.year2_amt) as year2_amt, Sum(ft.year3_amt) as year3_amt, Sum(ft.year4_amt) as year4_amt, Sum(ft.year5_amt) as year5_amt,"
			+ "Sum(ft.year6_amt) as year6_amt, Sum(ft.year7_amt) as year7_amt, Sum(ft.year8_amt) as year8_amt, Sum(ft.year9_amt) as year9_amt,"
			+ "Sum(ft.year10_amt) as year10_amt, Sum(ft.year11_amt) as year11_amt, Sum(ft.year12_amt) as year12_amt, Sum(ft.total_amt) as total_amt,"
			+ "ft.remarks as remarks) from FeeTemplateSubAmount ft "
			+ "left join VoucherHeadNew vnh on ft.voucher_head_new_id=vnh.voucher_head_new_id "
			+ "left join FeeTemplate f on ft.fee_template_id=f.fee_template_id "
			+ "left join Board bn on ft.board_unique_id=bn.board_unique_id "
			+ "left join AliasName an on ft.alias_id=an.alias_id where ft.fee_template_id=:fee_template_id and ft.active=true group by ft.fee_sub_amt_id")
	public List<HashMap<String, Object>> feeTemplateSubAmountForFeeReceipt1(Integer fee_template_id);

	@Query(value = "select (ft.fee_year1_amt-sum(fts.year1_amt)) as year1_amt, (ft.fee_year2_amt-sum(fts.year2_amt)) as year2_amt,"
			+ " (ft.fee_year3_amt-sum(fts.year3_amt)) as year3_amt,(ft.fee_year6_amt-sum(fts.year6_amt)) as year6_amt,"
			+ "(ft.fee_year4_amt-sum(fts.year4_amt)) as year4_amt,(ft.fee_year5_amt-sum(fts.year5_amt)) as year5_amt,"
			+ "(ft.fee_year7_amt-sum(fts.year7_amt)) as year7_amt,(ft.fee_year8_amt-sum(fts.year8_amt)) as year8_amt,"
			+ "(ft.fee_year9_amt-sum(fts.year9_amt)) as year9_amt,(ft.fee_year11_amt-sum(fts.year11_amt)) as year11_amt,"
			+ "(ft.fee_year10_amt-sum(fts.year10_amt)) as year10_amt,(ft.fee_year12_amt-sum(fts.year12_amt)) as year12_amt "
			+ " from fee_template_sub_amount fts inner join fee_template ft on fts.fee_template_id= ft.fee_template_id "
			+ " where fts.fee_template_id=?1 and fts.board_unique_id != coalesce(NULL,0) and fts.board_unique_id != 0", nativeQuery = true)
	public List<Map<String, Object>> feeTemplateSubAmountForFeeReceiptOnPaidAtBoard(Integer fee_template_id);
	
	@Query(value = "select ft.fee_sub_amt_id as fee_sub_amt_id,"
			+ "vnh.voucher_head as voucher_head,ft.voucher_head_new_id as voucher_head_new_id,"
			+ "bn.board_unique_name as board_unique_name,bn.board_unique_id as board_unique_id,an.alias_name as alias_name,"
			+ "an.alias_id as alias_id, f.Is_paid_at_board as Is_paid_at_board, ft.receive_for_all_year as receive_for_all_year, ft.year1_amt as year1_amt,"
			+ "ft.year2_amt as year2_amt,ft.year3_amt as year3_amt,ft.year4_amt as year4_amt,ft.year5_amt as year5_amt,"
			+ "ft.year6_amt as year6_amt,ft.year7_amt as year7_amt,ft.year8_amt as year8_amt,ft.year9_amt as year9_amt,"
			+ "ft.year10_amt as year10_amt,ft.year11_amt as year11_amt,ft.year12_amt as year12_amt,ft.total_amt as total_amt,"
			+ "ft.remarks as remarks from fee_template_sub_amount ft "
			+ "left join voucher_head_new vnh on ft.voucher_head_new_id=vnh.voucher_head_new_id "
			+ "left join fee_template f on ft.fee_template_id=f.fee_template_id "
			+ "left join board bn on ft.board_unique_id=bn.board_unique_id "
			+ "left join alias_name an on ft.alias_id=an.alias_id where ft.fee_template_id=?1 "
			+ "and ft.board_unique_id != coalesce(NULL,0) and ft.board_unique_id != 0", nativeQuery = true)
	public List<Map<String, Object>> feeTemplateSubAmountForFeeReceiptPaidAtBoardForLedger(Integer fee_template_id);

	@Query(value = "Select new map(ftsa.fee_sub_amt_id as id,ftsa.fee_template_id as fee_template_id,ftsa.voucher_head_new_id as voucher_head_new_id,"
			+ "ftsa.board_unique_id as board_unique_id,ftsa.alias_id as alias_id,ftsa.year1_amt as year1_amt,"
			+ "ftsa.year2_amt as year2_amt,ftsa.year3_amt as year3_amt,ftsa.year4_amt as year4_amt,ftsa.year5_amt as year5_amt,"
			+ "ftsa.year6_amt as year6_amt,ftsa.year7_amt as year7_amt,ftsa.year8_amt as year8_amt,"
			+ "ftsa.year9_amt as year9_amt,ftsa.year10_amt as year10_amt,ftsa.year11_amt as year11_amt,ftsa.year12_amt as year12_amt,"
			+ "ftsa.total_amt as total_amt,ftsa.created_by as created_by,ftsa.modified_by as modified_by,"
			+ "ftsa.created_date as created_date,ftsa.modified_date as modified_date,ftsa.receive_for_all_year as receive_for_all_year,"
			+ "ftsa.active as active,ftsa.remarks as remarks,ftsa.created_username as created_username,ftsa.modified_username as modified_username) From FeeTemplateSubAmount ftsa "
			+ "Where CONCAT(IfNull(ftsa.voucher_head_new_id,''),'',IfNull(ftsa.fee_template_id,''),'',IfNull(ftsa.voucher_head_new_id,''),'',"
			+ "IfNull(ftsa.total_amt,''),'',IfNull(ftsa.created_by,''),'',IfNull(ftsa.created_date,''),'',IfNull(ftsa.created_username,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);

	@Query(value = "Select new map(ftsa.fee_sub_amt_id as id,ftsa.fee_template_id as fee_template_id,ftsa.voucher_head_new_id as voucher_head_new_id,"
			+ "ftsa.board_unique_id as board_unique_id,ftsa.alias_id as alias_id,ftsa.year1_amt as year1_amt,"
			+ "ftsa.year2_amt as year2_amt,ftsa.year3_amt as year3_amt,ftsa.year4_amt as year4_amt,ftsa.year5_amt as year5_amt,"
			+ "ftsa.year6_amt as year6_amt,ftsa.year7_amt as year7_amt,ftsa.year8_amt as year8_amt,"
			+ "ftsa.year9_amt as year9_amt,ftsa.year10_amt as year10_amt,ftsa.year11_amt as year11_amt,ftsa.year12_amt as year12_amt,"
			+ "ftsa.total_amt as total_amt,ftsa.created_by as created_by,ftsa.modified_by as modified_by,"
			+ "ftsa.created_date as created_date,ftsa.modified_date as modified_date,ftsa.receive_for_all_year as receive_for_all_year,"
			+ "ftsa.active as active,ftsa.remarks as remarks,ftsa.created_username as created_username,ftsa.modified_username as modified_username) From FeeTemplateSubAmount ftsa")
	public Page<Object> findAll2(Pageable pageable);

	@Modifying
	@Query(value = "delete from fee_template_sub_amount ftsa where ftsa.fee_template_id=?1", nativeQuery = true)
	public void deleteDataByFeeTemplateId(Integer fee_template_id);

//	@Modifying
//	@Query(value = "delete from fee_template_sub_amount ftsa where ftsa.fee_sub_amt_id NOT IN :fee_sub_amt_id and ftsa.fee_template_id =:fee_template_id",nativeQuery=true)
//	public void deleteDataByFeeTemplateId(List<Integer> fee_sub_amt_id, Integer fee_template_id);

	@Query(value = "select ftsa from FeeTemplateSubAmount ftsa where ftsa.fee_template_id in (?1) and ftsa.active=true")
	public List<FeeTemplateSubAmount> fetchFeeTemplateSubAmountDetails(Integer fee_template_id);

	@Query(value = "select ftsa.voucher_head_new_id from FeeTemplateSubAmount ftsa where ftsa.fee_template_id=?1 and ftsa.active=true")
	public List<Integer> getAllVocherHeadNewIdAssignedToFeeTemplate(Integer fee_template_id);

	@Query(value = "select ftsa from FeeTemplateSubAmount ftsa "
			+ "left join FeeTemplate ft on ftsa.fee_template_id=ft.fee_template_id "
			+ "where ft.fee_template_id=?1 and ftsa.active=true")
	public List<FeeTemplateSubAmount> getFeeTemplateIds(Integer fee_template_id);

	@Query(value = " select new  com.au.dto.FeeTemplateDTO( fsa.year1_amt, fsa.year2_amt, fsa.year3_amt, fsa.year4_amt,"
			+ " fsa.year5_amt, fsa.year6_amt, fsa.year7_amt, fsa.year8_amt, fsa.year9_amt,"
			+ " fsa.year10_amt, fsa.year11_amt, fsa.year12_amt, vhn.voucher_head  ) from FeeTemplateSubAmount fsa left join VoucherHeadNew vhn on vhn.voucher_head_new_id=fsa.voucher_head_new_id "
			+ " where fsa.fee_template_id=:templateId  ", nativeQuery = false)
	public List<FeeTemplateDTO> getFeeTemplateDetails(Integer templateId);

	@Query(value=" select f from FeeTemplateSubAmount f left join VoucherHeadNew vh on vh.voucher_head_new_id=f.voucher_head_new_id where f.fee_template_id=:fee_template_id and vh.voucher_head='Registration Fee' ")
	public FeeTemplateSubAmount findByFeeTemplateId(Integer fee_template_id);
	

		@Query(value = "select CASE WHEN :yearOrSem=1 THEN sd.year1_amt "
				+ " WHEN :yearOrSem=2 THEN sd.year2_amt "
				+ " WHEN :yearOrSem=3 THEN sd.year3_amt "
				+ " WHEN :yearOrSem=4 THEN sd.year4_amt "
				+ " WHEN :yearOrSem=5 THEN sd.year5_amt "
				+ " WHEN :yearOrSem=6 THEN sd.year6_amt "
				+ " WHEN :yearOrSem=7 THEN sd.year7_amt "
				+ " WHEN :yearOrSem=8 THEN sd.year8_amt "
				+ " WHEN :yearOrSem=9 THEN sd.year9_amt "
				+ " WHEN :yearOrSem=10 THEN sd.year10_amt "
				+ " WHEN :yearOrSem=11 THEN sd.year11_amt "
				+ " WHEN :yearOrSem=12 THEN sd.year12_amt "
				+ " ELSE 0 END as paidAtBoardValue "
				+ " from fee_template_sub_amount sd where sd.voucher_head_new_id=:voucher_head_new_id and sd.fee_template_id=:fee_template_id and "
				+ "sd.board_unique_id != coalesce(NULL,0) and sd.board_unique_id != 0", nativeQuery=true)
		public Double getPaidAtBoardValue(Integer voucher_head_new_id, Integer yearOrSem, Integer fee_template_id);
		
		
		@Query(value = "SELECT new map(fsa.fee_sub_amt_id AS fee_sub_amt_id,fsa.fee_template_id AS fee_template_id,fsa.voucher_head_new_id AS voucher_head_new_id,"
				+ "fsa.board_unique_id AS board_unique_id,fsa.alias_id AS alias_id,fsa.year1_amt AS year1_amt,fsa.year2_amt AS year2_amt,"
				+ "vnh.voucher_head as voucher_head,vnh.voucher_head_short_name As voucher_head_short_name,"
				+ "bn.board_unique_name as board_unique_name,bn.board_unique_id as board_unique_id,an.alias_name as alias_name,bn.board_unique_short_name As board_unique_short_name,"
				+ "fsa.year3_amt AS year3_amt,fsa.year4_amt AS year4_amt,fsa.year5_amt AS year5_amt,fsa.year6_amt AS year6_amt,"
				+ "fsa.year7_amt AS year7_amt,fsa.year8_amt AS year8_amt,fsa.year9_amt AS year9_amt,fsa.year10_amt AS year10_amt,"
				+ "	fsa.year11_amt AS year11_amt,fsa.year12_amt AS year12_amt,fsa.total_amt AS total_amt,"
				+ "fsa.created_by AS created_by,fsa.modified_by AS modified_by,fsa.created_date AS created_date,"
				+ "fsa.modified_date AS modified_date,fsa.receive_for_all_year AS receive_for_all_year,fsa.active AS active,"
				+ "fsa.remarks AS remarks,fsa.created_username AS created_username,fsa.modified_username AS modified_username) "
				+ "FROM FeeTemplateSubAmount fsa "
				+ "left join VoucherHeadNew vnh on fsa.voucher_head_new_id=vnh.voucher_head_new_id "
				+ "left join FeeTemplate f on fsa.fee_template_id=f.fee_template_id "
				+ "left join Board bn on fsa.board_unique_id=bn.board_unique_id "
				+ "left join AliasName an on fsa.alias_id=an.alias_id "
				+ "WHERE fsa.active = TRUE And fsa.fee_template_id=?1" )
		public  List<Map<String, Object>> fetchFeeTempalteSubAmount(Integer fee_template_id);


		@Query(value=" select f from FeeTemplateSubAmount f left join FeeTemplate ft on ft.fee_template_id=f.fee_template_id"
				+ " left join Student_Details s on s.fee_template_id=ft.fee_template_id where s.student_id=:studentId and s.active = true and f.active = true and ft.active = true ")
		public List<FeeTemplateSubAmount> getFeeTemplateSubAmounts(Integer studentId);

}
