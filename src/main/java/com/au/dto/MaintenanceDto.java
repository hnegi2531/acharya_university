package com.au.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;


public class MaintenanceDto {
	
	private Long id;
	private Integer serviceTicketId;
	private Integer complaintAttendedBy;
	private String complaintStage;
	private String complaintStatus;
	private String remarks;
	private String purchaseNeed;
	private Integer instituteId;
	private Integer branchId;
	private String username;
	private boolean active;
	private String dateOfAttended;
	private String dateOfClosed;

	private String attendedByUserName;
	private String complaintDetails;
	private String floorAndExtension;
	private Integer userId;
	private Long serviceTypeId;
	private Integer attendedBy;
	
	private Integer financial_year_id;
	private Integer blockId;
	private String attachment_path;
	private MultipartFile file;

	@Setter @Getter
	private Integer deptId;
	@Setter @Getter
	private String date;
	
	private String from_date;
	private String to_date;
	private String program_id;
	private String year_sem;
	private String program_specialization_id;
	private Boolean event_status;
	private Integer event_id;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getServiceTicketId() {
		return serviceTicketId;
	}
	public void setServiceTicketId(Integer serviceTicketId) {
		this.serviceTicketId = serviceTicketId;
	}
	public Integer getComplaintAttendedBy() {
		return complaintAttendedBy;
	}
	public void setComplaintAttendedBy(Integer complaintAttendedBy) {
		this.complaintAttendedBy = complaintAttendedBy;
	}
	

	public String getComplaintStage() {
		return complaintStage;
	}
	public void setComplaintStage(String complaintStage) {
		this.complaintStage = complaintStage;
	}
	public String getComplaintStatus() {
		return complaintStatus;
	}
	public void setComplaintStatus(String complaintStatus) {
		this.complaintStatus = complaintStatus;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getPurchaseNeed() {
		return purchaseNeed;
	}
	public void setPurchaseNeed(String purchaseNeed) {
		this.purchaseNeed = purchaseNeed;
	}
	public Integer getInstituteId() {
		return instituteId;
	}
	public void setInstituteId(Integer instituteId) {
		this.instituteId = instituteId;
	}
	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDateOfAttended() {
		return dateOfAttended;
	}
	public void setDateOfAttended(String dateOfAttended) {
		this.dateOfAttended = dateOfAttended;
	}
	public String getDateOfClosed() {
		return dateOfClosed;
	}
	public void setDateOfClosed(String dateOfClosed) {
		this.dateOfClosed = dateOfClosed;
	}
	public boolean getActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getAttendedByUserName() {
		return attendedByUserName;
	}
	public void setAttendedByUserName(String attendedByUserName) {
		this.attendedByUserName = attendedByUserName;
	}
	public String getComplaintDetails() {
		return complaintDetails;
	}
	public void setComplaintDetails(String complaintDetails) {
		this.complaintDetails = complaintDetails;
	}
	public String getFloorAndExtension() {
		return floorAndExtension;
	}
	public void setFloorAndExtension(String floorAndExtension) {
		this.floorAndExtension = floorAndExtension;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Long getServiceTypeId() {
		return serviceTypeId;
	}
	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
	public Integer getAttendedBy() {
		return attendedBy;
	}
	public void setAttendedBy(Integer attendedBy) {
		this.attendedBy = attendedBy;
	}
	public Integer getFinancial_year_id() {
		return financial_year_id;
	}
	public void setFinancial_year_id(Integer financial_year_id) {
		this.financial_year_id = financial_year_id;
	}
	public Integer getBlockId() {
		return blockId;
	}
	public void setBlockId(Integer blockId) {
		this.blockId = blockId;
	}
	public String getAttachment_path() {
		return attachment_path;
	}
	public void setAttachment_path(String attachment_path) {
		this.attachment_path = attachment_path;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getFrom_date() {
		return from_date;
	}
	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}
	public String getTo_date() {
		return to_date;
	}
	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}
	public String getProgram_id() {
		return program_id;
	}
	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}
	public String getYear_sem() {
		return year_sem;
	}
	public void setYear_sem(String year_sem) {
		this.year_sem = year_sem;
	}
	public String getProgram_specialization_id() {
		return program_specialization_id;
	}
	public void setProgram_specialization_id(String program_specialization_id) {
		this.program_specialization_id = program_specialization_id;
	}
	public Boolean getEvent_status() {
		return event_status;
	}
	public void setEvent_status(Boolean event_status) {
		this.event_status = event_status;
	}
	public Integer getEvent_id() {
		return event_id;
	}
	public void setEvent_id(Integer event_id) {
		this.event_id = event_id;
	}



}
