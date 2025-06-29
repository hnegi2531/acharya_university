package com.au.dto;


import com.au.model.RazorPayPaymentDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RazorPayTransactionDetails {

    private Long razorPayTransactionId;

    private String receiptId;

    private String orderId;

    private Float amount;

    private String status;

    private Integer studentId;

    private Integer currentSem;

    private Integer currentYear;

    private Integer acYearId;

    private String paidYear;

    private String transactionType;

    private String paymentId;
    private String transactionId;
    private String signature;
    private String code;
    private String description;
    private String source;
    private String step;
    private String reason;

    private String auid;

    private Date created_date;


    private Date modified_date;

    private Date transactionDate;

    private String remarks;
    private List<RazorPayPaymentDetails> collegeFee;
    private List<RazorPayPaymentDetails> addOnFee;
    private List<RazorPayPaymentDetails> uniformFee;
    private List<RazorPayPaymentDetails> bulkFee;
    private List<RazorPayPaymentDetails> examFee;

}
