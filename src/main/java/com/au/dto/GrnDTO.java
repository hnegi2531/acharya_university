package com.au.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrnDTO {

	
	private String invoiceNo;
	private Date invoiceDate;
	private String aquistion;
	private String remarks;
	private Integer empId;
	private String poNo;
	private Integer vendorId;
	private String vendorName;
	private Integer instituteId;
	private Integer storeId;
	private String storeName;
	private Integer userId;
	private String userName;
	private String requestType;

	private List<DirectGrnDTO> directGrnDTOs;
	
	private List<ListOfGrnProducts> grnProducts;
	
}
