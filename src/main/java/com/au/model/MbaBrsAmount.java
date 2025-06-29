package com.au.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Author: Anjan
 * Date: 12-04-2025
 * Description: MbaBrsAmount class
 */

@Setter
@Getter
@Entity
public class MbaBrsAmount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mbaBrsAmountId;
    private String transactionDate;
    private Date createdDate;
    private Integer createdBy;
    private Date modifiedDate;
    private Integer modifiedBy;
    private Boolean active;
    private Float bankAmount;
    private Integer fcYearId;
    private Float adjustmentAmount;
}
