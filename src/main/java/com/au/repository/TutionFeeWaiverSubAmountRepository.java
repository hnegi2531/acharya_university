package com.au.repository;


import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.TutionFeeWaiverSubAmount;


@Repository
@Transactional
public interface TutionFeeWaiverSubAmountRepository extends JpaRepository<TutionFeeWaiverSubAmount, Integer>{
	
	
	@Query(value ="Select tfwsa.tution_fee_waiver_sub_amt_id as tution_fee_waiver_sub_amt_id, tfwsa.tution_fee_waiver_id as tution_fee_waiver_id, "
			+ "tfwsa.voucher_head_new_id as voucher_head_new_id,tfwsa.offer_waiver_amount as offer_waiver_amount,ifNull(tfwsa.year1_amt,0) as year1_amt,"
			+ "ifNull(tfwsa.year2_amt,0) as year2_amt,ifNull(tfwsa.year3_amt,0) as year3_amt,ifNull(tfwsa.year4_amt,0) as year4_amt,"
			+ "ifNull(tfwsa.year5_amt,0) as year5_amt,ifNull(tfwsa.year6_amt,0) as year6_amt,ifNull(tfwsa.year7_amt,0) as year7_amt,"
			+ "ifNull(tfwsa.year8_amt,0) as year8_amt,ifNull(tfwsa.year9_amt,0) as year9_amt,ifNull(tfwsa.year10_amt,0) as year10_amt,"
			+ "ifNull(tfwsa.year11_amt,0) as year11_amt,ifNull(tfwsa.year12_amt,0) as year12_amt,tfwsa.total_amt as total_amt,"
			+ "tfwsa.active as active,tfw.student_id as student_id From tution_fee_waiver_sub_amount tfwsa "
			+ "Inner Join tution_fee_waiver tfw On tfw.tution_fee_waiver_id=tfwsa.tution_fee_waiver_id "
			+ "Where tfw.student_id=?1 and tfwsa.active=true",nativeQuery=true)
	public List<Map<String, Object>> getTutionFeeWaiverSubAmountOnStudentId(Integer student_id);
	
	

}
