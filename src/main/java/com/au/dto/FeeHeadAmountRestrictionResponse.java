package com.au.dto;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeeHeadAmountRestrictionResponse {
	
	
	private Integer id;
	private Integer schoolId;
	private Date fromDate;
	private Date toDate;
	private Float amount;
	private Integer voucherHeadId;
	private Boolean fixed;
	private String externalStatus;
	private String remarks;
	private String userId;
	private Integer createdBy;
	private Date createdDate;
	private Boolean active;
	private String createdUsername;
	private Integer modifiedBy;
	private String modifiedUsername;
	private String schoolShortName;
//	private String userName;
	private String voucherHead;
	private String voucherHeadShortName;
	private String attachmentPath;
	private String attachmentFileName;
	private String attachmentType;
	
//	public FeeHeadAmountRestrictionResponse(Integer id, Integer schoolId, Date fromDate, Date toDate, Float amount,
//			Integer voucherHeadId, Integer fixed, Integer externalStatus, String remarks, Integer userId,
//			Integer createdBy, Date createdDate, Boolean active, String createdUsername) {
//		super();
//		this.id = id;
//		this.schoolId = schoolId;
//		this.fromDate = fromDate;
//		this.toDate = toDate;
//		this.amount = amount;
//		this.voucherHeadId = voucherHeadId;
//		this.fixed = fixed;
//		this.externalStatus = externalStatus;
//		this.remarks = remarks;
//		this.userId = userId;
//		this.createdBy = createdBy;
//		this.createdDate = createdDate;
//		this.active = active;
//		this.createdUsername = createdUsername;
//	}

	public FeeHeadAmountRestrictionResponse(Integer id, Integer schoolId, Date fromDate, Date toDate, Float amount,
			Integer voucherHeadId, Boolean fixed, String externalStatus, String remarks, String userId,
			Integer createdBy, Date createdDate, Boolean active, String createdUsername, Integer modifiedBy,
			String modifiedUsername, String schoolShortName, String voucherHead,
			String voucherHeadShortName,String attachmentPath,String attachmentFileName,String attachmentType) {
		super();
		this.id = id;
		this.schoolId = schoolId;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.amount = amount;
		this.voucherHeadId = voucherHeadId;
		this.fixed = fixed;
		this.externalStatus = externalStatus;
		this.remarks = remarks;
		this.userId = userId;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.active = active;
		this.createdUsername = createdUsername;
		this.modifiedBy = modifiedBy;
		this.modifiedUsername = modifiedUsername;
		this.schoolShortName = schoolShortName;
		this.voucherHead = voucherHead;
		this.voucherHeadShortName = voucherHeadShortName;
		this.attachmentPath= attachmentPath;
		this.attachmentFileName= attachmentFileName;
		this.attachmentType= attachmentType;
	}
	
	
	
	

}
