package com.au.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SettlementList {

    @JsonProperty("entity_id")
    private String entityId;

    private String type;
    private Double debit;
    private Double credit;
    private Double amount;
    private String currency;
    private int fee;
    private int tax;

    @JsonProperty("on_hold")
    private boolean onHold;

    private boolean settled;

    @JsonProperty("created_at")
    private long createdAt;

    @JsonProperty("settled_at")
    private long settledAt;

    @JsonProperty("settlement_id")
    private String settlementId;

    @JsonProperty("posted_at")
    private String postedAt;

    @JsonProperty("credit_type")
    private String creditType;

    private String description;

    private String notes;

    @JsonProperty("payment_id")
    private String paymentId;

    @JsonProperty("settlement_utr")
    private String settlementUtr;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("order_receipt")
    private String orderReceipt;

    private String method;

    @JsonProperty("card_network")
    private String cardNetwork;

    @JsonProperty("card_issuer")
    private String cardIssuer;

    @JsonProperty("card_type")
    private String cardType;

    @JsonProperty("dispute_id")
    private String disputeId;
}
