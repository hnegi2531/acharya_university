package com.au.repository;

import com.au.dto.SettlementReportProjection;
import com.au.model.BankImportTransaction;
import com.au.model.RazorPaySettelments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface RazorPaySettlementRepository extends JpaRepository<RazorPaySettelments, Long> {

    @Query(value = "WITH razor_summary AS ( " +
            "            SELECT " +
            "                settlement_id, " +
            "                inst_name, " +
            "                settled_at, " +
            "                ROUND(SUM(COALESCE(credit, 0)), 2) AS total_credit, " +
            "                ROUND(SUM(COALESCE(debit, 0)), 2) AS total_debit " +
            "            FROM  " +
            "                razor_pay_settlement " +
            "            WHERE  " +
            "                settled_at = :settledDate " +
            "            GROUP BY  " +
            "                settlement_id, inst_name, settled_at " +
            "        ) " +
            "        SELECT  " +
            "            rs.inst_name AS instName, " +
            "            DATE_FORMAT(rs.settled_at, '%Y-%m-%d') AS date, " +
            "            rs.settlement_id AS settlementId, " +
            "            rs.total_credit AS totalCredit, " +
            "            rs.total_debit AS totalDebit, " +
            "            SUM(CASE  " +
            "                    WHEN b.balance <= 0 AND b.receipt_status = 'S' " +
            "                    THEN COALESCE(b.paid, 0) " +
            "                    ELSE 0 " +
            "                END) AS receiptAmount, " +
            "            SUM(CASE  " +
            "                    WHEN b.balance > 0 AND b.receipt_status = 'P' " +
            "                    THEN COALESCE(b.paid, 0)  " +
            "                    ELSE 0  " +
            "                END) AS pendingAmount " +
            "        FROM  " +
            "            razor_summary rs " +
            "        LEFT JOIN  " +
            "            bank_import_transaction b  " +
            "            ON b.settlement_id = rs.settlement_id AND b.active = true " +
            "        LEFT JOIN  " +
            "            schools s  " +
            "            ON s.school_id = b.school_id " +
            "        WHERE  " +
            "            ( " +
            "                (b.balance <= 0 AND b.receipt_status = 'S') OR " +
            "                (b.balance > 0 AND b.receipt_status = 'P') OR " +
            "                b.settlement_id IS NULL " +
            "            ) " +
            "        GROUP BY " +
            "            rs.settlement_id, rs.inst_name, rs.settled_at, s.school_name_short",nativeQuery = true)
    List<SettlementReportProjection> getSettlementReport(@Param("settledDate") String settledDate);

    @Query(value = "SELECT DISTINCT r.orderId FROM RazorPaySettelments r WHERE r.orderId IS NOT NULL")
    public List<String> findAllDistictOrderId();

    @Query(value = " select * from razor_pay_settlement where settlement_id = ?1 and type = 'payment' ",nativeQuery = true)
    public List<RazorPaySettelments> getSettlementsBySettlementIdAndDate(String settlementId, String date);

    @Query(value = " select * from razor_pay_settlement where settlement_id = ?1 and type = 'transfer' ",nativeQuery = true)
    public List<RazorPaySettelments> getTransferSettlementsBySettlementIdAndDate(String settlementId, String date);

    @Query(value = " SELECT rps.* " +
            " FROM razor_pay_settlement rps " +
            " LEFT JOIN bank_import_transaction b " +
            "    ON rps.order_id = b.order_id " +
            " WHERE b.order_id IS NULL " +
            " and  rps.type = 'payment' " +
            " and (:date is null or :date = '' OR rps.settled_at LIKE CONCAT(:date, '%')) ", nativeQuery = true)
    public List<RazorPaySettelments> allPendingSettlements(@Param("date") String date);
}
