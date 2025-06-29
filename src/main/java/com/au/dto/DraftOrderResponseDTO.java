package com.au.dto;

import java.util.Date;

import javax.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DraftOrderResponseDTO {

    private String vendor;
    private String institute;
    private Date createdDate;
    private String purchaseApprover;
    private Integer temporaryPurchaseOrderId;
    private String remarks;
    private Integer billApprovedId;
    private String userName;
    private String createdUsername;
    private String tpoAttachmentFilePath;
    private Integer billApprovedStatus;
    private Double totalAmount;
    @Lob
    private String annexureText;
    private Integer empId;
}
