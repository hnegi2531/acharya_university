package com.au.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.model.UniformReceipt;

@Transactional
@Repository
public interface UniformReceiptRepository extends JpaRepository<UniformReceipt, Integer> {
	
	@Query(value = "select ur from UniformReceipt ur where ur.active=true")
	public List<UniformReceipt> findAll1();

    @Query("SELECT new map(ur.uniformReceiptId as id, ur.fcYearId as fcYearId, ur.studentId as studentId, "
            + "ur.amount as amount, ur.createdDate as createdDate, ur.createdBy as createdBy, "
            + "ur.modifiedDate as modifiedDate, ur.modifiedBy as modifiedBy,ur.createdUsername as createdUsername, ur.modifiedUsername as modifiedUsername, "
            + "ur.issuedStatus as issuedStatus, ur.cancelStatus as cancelStatus, ur.orderId as orderId, "
            + "ur.active as active, ur.type as type, ur.transactionType as transactionType, ur.otherFeeTemplateId as otherFeeTemplateId,"
            + "ur.uniformReceiptNo as uniformReceiptNo, ur.paydetails as paydetails, ur.transactionDate as transactionDate, "
            + "sd.student_name as studentName, sc.school_name as schoolName, sc.school_name_short as schoolShortName) "
            + "FROM UniformReceipt ur "
            + "LEFT JOIN Student_Details sd ON sd.student_id = ur.studentId "
            + "LEFT JOIN Schools sc ON sc.school_id = sd.school_id "
            + "Where CONCAT(IfNull(sd.student_name,''),'',IfNull(ur.amount,''),'',IfNull(ur.createdDate,''),'',IfNull(ur.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

    @Query("SELECT new map(ur.uniformReceiptId as id, ur.fcYearId as fcYearId, ur.studentId as studentId, "
            + "ur.amount as amount, ur.createdDate as createdDate, ur.createdBy as createdBy, "
            + "ur.modifiedDate as modifiedDate, ur.modifiedBy as modifiedBy,ur.createdUsername as createdUsername, ur.modifiedUsername as modifiedUsername, "
            + "ur.issuedStatus as issuedStatus, ur.cancelStatus as cancelStatus, ur.orderId as orderId, "
            + "ur.active as active, ur.type as type, ur.transactionType as transactionType, ur.otherFeeTemplateId as otherFeeTemplateId,"
            + "ur.uniformReceiptNo as uniformReceiptNo, ur.paydetails as paydetails, ur.transactionDate as transactionDate, "
            + "sd.student_name as studentName, sc.school_name as schoolName, sc.school_name_short as schoolShortName) "
            + "FROM UniformReceipt ur "
            + "LEFT JOIN Student_Details sd ON sd.student_id = ur.studentId "
            + "LEFT JOIN Schools sc ON sc.school_id = sd.school_id")
    Page<Object> getAllSortedData(Pageable pageable);


	@Modifying
	@Query(value = "update UniformReceipt ur set ur.active=false where ur.uniformReceiptId=?1")
	public void updateUniformReceipt(Integer uniformReceiptId);

	@Modifying
	@Query(value = "update UniformReceipt ur set ur.active=true where ur.uniformReceiptId=?1")
	public void updateUniformReceipt1(Integer uniformReceiptId);
	
	@Query(value="SELECT COALESCE(( "  +
			"SELECT ur.uniform_receipt_no " +
			" FROM uniform_receipt ur " +
			" JOIN financial_year f ON f.financial_year_id = ur.fc_year_id " +
			" WHERE CURDATE() >= f.from_date  " +
			"  AND CURDATE() <= f.to_date " +
			" ORDER BY uniform_receipt_no DESC " +
			" LIMIT 1 " +
			" ), 0)",nativeQuery=true)
	public Integer getLatestReceitpNumber();

	@Query(value="select u from UniformReceipt u  where u.bankImportTransactionId=:bankImportTransactionId ")
	public List<UniformReceipt> getUniformFeeReceiptsByBankImportId(Integer bankImportTransactionId);
    
	@Query(value="select sum(u.amount) from uniform_receipt u  where  u.paid_year=:sem and u.student_id=:studentId and u.type='package' ",nativeQuery=true)
	public Double getSumOfPaidUniformFee(Integer sem,Integer studentId);

//	@Query(value="select u from UniformReceipt u  where u.type='package' ")
	@Modifying  
	@Query(value =  "SELECT  "
			+ "    MAX(ur.paid_year) AS paid_year, "
			+ "    ur.order_id AS order_id, "
			+ "    MAX(sd.school_id) AS institute_id,"
			+ "    MAX(sd.candidate_sex) AS gender, "
			+ "    MAX(sd.student_id) AS student_id,"
			+ "    MAX(sd.auid) AS auid, "
			+ "    MAX(sd.student_name) AS student_name,"
			+ "    MAX(sd.usn) AS usn,"
			+ "    MAX(ur.uniform_receipt_no) AS uniform_receipt, "
			+ "    SUM(ur.amount) AS amount, "
			+ "    MAX(ur.type) AS type, "
			+ "    MAX(ur.other_fee_template_id) AS uniform_id,  "
			+ "    MAX(ur.transaction_date) AS transaction_date, "
			+ "    MAX(ur.created_date) AS created_date,  "
			+ "    MAX(ur.fc_year_id) AS fc_year_id,  "
			+ "    MAX(fy.financial_year) AS fc_year,  "
			+ "    MAX(sd.fee_admission_category_id) AS fee_admission_category_id,"
			+ "    MAX(fac.fee_admission_category_short_name) AS fee_admission_category_short_name,"
			+ "			    CASE "
			+ "			        WHEN ur.type = 'loose' THEN "
			+ "			            GROUP_CONCAT( "
			+ "			                CONCAT_WS(':', "
			+ "			                    COALESCE(ur.env_item_id, 'N/A'), "
			+"                              COALESCE(ur.quantity, 0),  "
			+ "			                    COALESCE(ur.amount, 0) / CASE WHEN ur.quantity = 0 THEN NULL ELSE ur.quantity END, "
			+ "			                    COALESCE(ur.cgst_output, 0), "
			+ "			                    COALESCE(ur.sgst_output, 0)  "
			+ "			                ) ORDER BY ur.env_item_id SEPARATOR ','  "
			+ "			            )  "
			+ "			        ELSE NULL  "
			+ "			    END AS fee_details "
			+ "			FROM uniform_receipt AS ur "
			+ "			JOIN student_details AS sd ON ur.student_id = sd.student_id  "
			+ "			JOIN schools AS s ON sd.school_id = s.school_id  "
			+ "			LEFT JOIN financial_year AS fy ON fy.financial_year_id = ur.fc_year_id "
			+ "         JOIN fee_admission_category AS fac ON sd.fee_admission_category_id = fac.fee_admission_category_id"
			+ "			WHERE (?1 IS NULL OR CAST(ur.created_date AS DATE) LIKE CONCAT(?1, '%')) "
			+ "			  AND ( ?5 IS NULL OR ur.type = ?5)"
			+ "			  AND ((?2 IS NOT NULL AND ?3 IS NOT NULL AND CAST(ur.created_date AS DATE) >= ?2 AND CAST(ur.created_date AS DATE) <= ?3)"
			+ "               OR (?2 IS NULL AND CAST(ur.created_date AS DATE) <= ?3) "
			+ "			   OR (?3 IS NULL AND CAST(ur.created_date AS DATE) >= ?2)"
			+ "               OR (?2 IS NULL AND ?3 IS NULL))"
			+ "			  AND (?6 IS NULL OR sd.auid = ?6)  "
			+ "			  AND (?4 IS NULL OR s.school_name_short = ?4)  "
			+ "			  AND ur.active = TRUE  "
			+ "			  AND sd.active = TRUE  "
			+ "			  AND s.active = TRUE  "
			+ "			GROUP BY "
			+ "			     ur.order_id, ur.sem, ur.type,ur.uniform_receipt_no"
			+"              ORDER BY MAX(ur.created_date) desc ", nativeQuery = true)
	public List<Map<String, Object>> getUniformFeeReceipts(  String datee, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, @Param("insname") String insname, @Param("type") String type, @Param("auid") String auid);


 
	@Query(value =  "SELECT  "
			+ "    MAX(ur.paid_year) AS paid_year, "
			+ "    ur.order_id AS order_id, "
			+ "    MAX(sd.school_id) AS institute_id,"
			+ "    MAX(sd.candidate_sex) AS gender, "
			+ "    MAX(sd.student_id) AS student_id,"
			+ "    MAX(sd.auid) AS auid, "
			+ "    MAX(sd.student_name) AS student_name,"
			+ "    MAX(sd.usn) AS usn,"
			+ "    MAX(ur.uniform_receipt_no) AS uniform_receipt, "
			+ "    SUM(ur.amount) AS amount, "
			+ "    MAX(ur.type) AS type, "
			+ "    MAX(ur.other_fee_template_id) AS uniform_id,  "
			+ "    MAX(ur.transaction_date) AS transaction_date, "
			+ "    MAX(ur.created_date) AS created_date,  "
			+ "    MAX(ur.fc_year_id) AS fc_year_id,  "
			+ "    MAX(fy.financial_year) AS fc_year,  "
			+ "    MAX(sd.fee_admission_category_id) AS fee_admission_category_id,"
			+ "    MAX(fac.fee_admission_category_short_name) AS fee_admission_category_short_name,"
			+ "			    CASE "
			+ "			        WHEN ur.type = 'loose' THEN "
			+ "			            GROUP_CONCAT( "
			+ "			                CONCAT_WS(':', "
			+ "			                    COALESCE(ur.env_item_id, 'N/A'), "
			+ "                             COALESCE(ur.quantity, 0),  "
			+ "			                    COALESCE(ur.amount, 0) / CASE WHEN ur.quantity = 0 THEN NULL ELSE ur.quantity END, "
			+ "			                    COALESCE(ur.cgst_output, 0), "
			+ "			                    COALESCE(ur.sgst_output, 0)  "
			+ "			                ) ORDER BY ur.env_item_id SEPARATOR ','  "
			+ "			            )  "
			+ "			        ELSE NULL  "
			+ "			    END AS fee_details "
			+ "			FROM uniform_receipt AS ur "
			+ "			JOIN student_details AS sd ON ur.student_id = sd.student_id  "
			+ "			JOIN schools AS s ON sd.school_id = s.school_id  "
			+ "			LEFT JOIN financial_year AS fy ON fy.financial_year_id = ur.fc_year_id "
			+ "         JOIN fee_admission_category AS fac ON sd.fee_admission_category_id = fac.fee_admission_category_id"
			+ "			WHERE (?1 IS NULL OR CAST(ur.created_date AS DATE) LIKE CONCAT(?1, '%')) "
			+ "			  AND ( ?5 IS NULL OR ur.type = ?5)"
			+ "			  AND ((?2 IS NOT NULL AND ?3 IS NOT NULL AND CAST(ur.created_date AS DATE) >= ?2 AND CAST(ur.created_date AS DATE) <= ?3)"
			+ "               OR (?2 IS NULL AND CAST(ur.created_date AS DATE) <= ?3) "
			+ "			   OR (?3 IS NULL AND CAST(ur.created_date AS DATE) >= ?2)"
			+ "               OR (?2 IS NULL AND ?3 IS NULL))"
			+ "			  AND (?6 IS NULL OR sd.auid = ?6)  "
			+ "			  AND (?4 IS NULL OR s.school_name_short = ?4)  "
			+ "			  AND ur.active = TRUE  "
			+ "			  AND sd.active = TRUE  "
			+ "			  AND s.active = TRUE  "
			+ "			GROUP BY "
			+ "			     ur.order_id, ur.sem, ur.type "
			+"               ORDER BY MAX(ur.created_date) desc"
			+ "       LIMIT ?7  OFFSET ?8",
				        nativeQuery = true)
	public List<Map<String, Object>> getUniformFeeReceipts(String datee, LocalDate fromDate, LocalDate toDate, String insname, String type, String auid,long limit, long offset);

	
	@Query(value = " 			 SELECT COUNT(DISTINCT ur.order_id) FROM uniform_receipt AS ur "
			+ "				       JOIN student_details AS sd ON ur.student_id = sd.student_id "
			+ "				       JOIN schools AS s ON sd.school_id = s.school_id "
			+ "				      LEFT JOIN financial_year AS fy ON fy.financial_year_id = ur.fc_year_id "
			+ "                   JOIN fee_admission_category AS fac ON sd.fee_admission_category_id = fac.fee_admission_category_id"
			+ "				        WHERE (?1 IS NULL OR CAST(ur.created_date AS DATE) LIKE CONCAT(?1, '%'))"
			+ "				     AND (?5 IS NULL OR ur.type = ?5) "
			+ "				       AND ((?2 IS NOT NULL AND ?3 IS NOT NULL AND CAST(ur.created_date AS DATE) >= ?2 AND CAST(ur.created_date AS DATE) <= ?3)"
			+ "				     OR ( ?2 IS NULL AND CAST(ur.created_date AS DATE) <= ?3) "
			+ "				       OR (?3 IS NULL AND CAST(ur.created_date AS DATE) >= ?2)"
			+ "				       OR (?2 IS NULL AND ?3 IS NULL))"
			+ "				        AND (?6 IS NULL OR sd.auid = ?6) "
			+ "				       AND (?4 IS NULL OR s.school_name_short = ?4)"
			+ "				      AND ur.active = TRUE "
			+ "				       AND sd.active = TRUE "
			+ "				      AND s.active = TRUE"
			+ "                   AND fac.active = TRUE",
			        nativeQuery = true)
	public long countTotalReceipts(String datee, LocalDate fromDate, LocalDate toDate, String insname, String type,
			String auid);

	
	@Query(value = "SELECT cfr.uniform_receipt_no From uniform_receipt cfr where cfr.school_id=?1 And cfr.fc_year_id=?2 And cfr.type=?3 "
			+ " ORDER BY cfr.uniform_receipt_id DESC LIMIT 1",nativeQuery=true)
	public Integer getLatestData(Integer schoolId, Integer financial_year_id, String type);

	
	@Query(value = "select * From uniform_receipt where school_id=?1 And fc_year_id=?2 And type=?3 "
			+ " ORDER BY uniform_receipt_id Desc LIMIT 1",nativeQuery = true)
	public UniformReceipt getLatestServiceTicketId(Integer schoolId, Integer financial_year_id, String type);


	@Query(value = "select * from uniform_receipt where order_id  = ?1 ",nativeQuery = true)
	List<UniformReceipt> getUniformFeeReceiptByOrderId(String orderId);

	@Query(value = "select ur.transaction_date as transactionDate, sum(ur.amount) as amount ,ifnull(mba.bank_amount, 0) as bankAmount, " +
			"ifnull(mba.adjustment_amount, 0) as adjustment from uniform_receipt ur " +
			"left join mba_brs_amount mba on mba.transaction_date = ur.transaction_date " +
			"where ur.fc_year_id = :fcYearId and month(ur.transaction_date)  = :month " +
			"group by ur.transaction_date ", nativeQuery = true)
    List<Map<String, Object>> fetchUniformReceipts(Integer fcYearId, Integer month);

	@Query(value = "select sd.auid as auid,sd.student_name as studentName,ur.uniform_receipt_no as RPTNo ,ur.transaction_date as receiptDate," +
			"ur.amount as amount, ur.order_id as orderId, ut.payment_id as razorPayId " +
			"from uniform_receipt ur " +
			"left join student_details sd on sd.student_id = ur.student_id " +
			"left join uniform_transaction ut on ut.order_id = ur.order_id  " +
			"where ur.transaction_date = :date", nativeQuery = true)
	List<Map<String, Object>> getDateWiseUniformTransactions(String date);

}
