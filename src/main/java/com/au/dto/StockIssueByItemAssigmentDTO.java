package com.au.dto;


import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockIssueByItemAssigmentDTO {

    private String stockNumber;
    private String requestedDate;
    private Integer requestedQuantity;
    private Integer issuedQuantity;
    private String uom;
    private String itemDescription;
    private String itemName;
    private String itemAssignmentName;
    private String make;
    private Date issueDate;
    private String issueTo;

    public StockIssueByItemAssigmentDTO(String stockNumber, String requestedDate, Integer requestedQuantity,
                                        Integer issuedQuantity, String uom, String itemDescription, String itemName, String itemAssignmentName, String make,
                                        Date issueDate, String issueTo) {

        this.stockNumber = stockNumber;
        this.requestedDate = requestedDate;
        this.requestedQuantity = requestedQuantity;
        this.issuedQuantity = issuedQuantity;
        this.uom = uom;
        this.itemDescription = itemDescription;
        this.itemName = itemName;
        this.itemAssignmentName = itemAssignmentName;
        this.make = make;
        this.issueDate = issueDate;
        this.issueTo = issueTo;
    }


}
