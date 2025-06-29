package com.au.dto;


import java.math.BigDecimal;
import java.time.LocalDate;


public interface SettlementReportProjection {

    String getInstName();
    String getDate();
    String getSettlementId();
    BigDecimal getTotalCredit();
    BigDecimal getTotalDebit();
    BigDecimal getReceiptAmount();
    BigDecimal getPendingAmount();
}