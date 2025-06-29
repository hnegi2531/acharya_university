package com.au.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "razor_pay_settlement")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RazorPaySettelments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long razorPaySettlementId;

    private String entityId;

    private String type;

    private Double credit;
    private Double amount;
    private Double debit;
    private String currency;

    private boolean settled;

    private String createdAt;

    private String settledAt;

    private String settlementId;

    private String paymentId;

    private String settlementUtr;

    private String orderId;

    private String description;

    private Integer schoolId;

    private String instName;



}
