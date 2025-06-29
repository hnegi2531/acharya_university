package com.au.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MasterSalaryHistoryDTO {
	// java.lang.String, java.lang.String, java.lang.String, java.lang.String, 
	//java.lang.String, double, java.lang.String, java.lang.String, java.lang.String, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, 
	//java.lang.String, double, float, java.lang.String, int, int, float, java.lang.String, java.lang.String 
	private String empCode;
	private String employeeName;
	private String dateOfJoining;
	private String departmentName;
	private String designationName;
	private Double payDays;
	private String bankName;
	private String accountNo;
	private String mfo; // String type for mfo based on the query
	private Float basic;
	private Float hra;
	private Float da;
	private Float cca;
	private Float ta;
	private Float mr;
	private Float fr;
	private Float otherAllow; // Renamed to match query alias
	private Float spl1; // Renamed to match query alias
	private Float grossPay; // Renamed to match query alias
	private Float pf;
	private Float pt; // Renamed to match query alias
	private Float esi;
	private Float tds;
	private Float advance1;
	private Float advance2;
	private Float totalEarning; 
	private Float totalDeduction; 
	private Float netPay; // Renamed to match query alias
	private Float pfAccountNo; // Changed to String as IDs might be alphanumeric
	private Float pfEarnings; // Renamed to match query alias
	private Float contributionEpf; // Renamed to match query alias
	private Float epfDifference; // Renamed to match query alias
	private Float pensionFund; // Renamed to match query alias
	private Float esiEarnings; // Renamed to match query alias
	private Float esiContributionEmployee; // Renamed to match query alias
	private String passportNo;
	private Double er;
	private Float tax;
	private String pinfl;
	private Integer month;
	private Integer year;
	private Float advance;
	private String remarks;
	private String schoolName;

	private List<InvPayPaySlipDTO> invPayPaySlipDTOs;

	public MasterSalaryHistoryDTO(String empCode, String employeeName, String dateOfJoining, String departmentName,
			String designationName, Double payDays, String bankName, String accountNo, String mfo, Float basic,
			Float hra, Float da, Float cca, Float ta, Float mr, Float fr, Float otherAllow, Float spl1, Float grossPay,
			Float pf, Float pt, Float esi, Float tds, Float advance1, Float advance2, Float totalEarning,
			Float totalDeduction, Float netPay, Float pfAccountNo, Float pfEarnings, Float contributionEpf,
			Float epfDifference, Float pensionFund, Float esiEarnings, Float esiContributionEmployee, String passportNo,
			Double er, Float tax, String pinfl, Integer month, Integer year, Float advance, String remarks,
			String schoolName) {
		this.empCode = empCode;
		this.employeeName = employeeName;
		this.dateOfJoining = dateOfJoining;
		this.departmentName = departmentName;
		this.designationName = designationName;
		this.payDays = payDays;
		this.bankName = bankName;
		this.accountNo = accountNo;
		this.mfo = mfo;
		this.basic = basic;
		this.hra = hra;
		this.da = da;
		this.cca = cca;
		this.ta = ta;
		this.mr = mr;
		this.fr = fr;
		this.otherAllow = otherAllow;
		this.spl1 = spl1;
		this.grossPay = grossPay;
		this.pf = pf;
		this.pt = pt;
		this.esi = esi;
		this.tds = tds;
		this.advance1 = advance1;
		this.advance2 = advance2;
		this.totalEarning = totalEarning;
		this.totalDeduction = totalDeduction;
		this.netPay = netPay;
		this.pfAccountNo = pfAccountNo;
		this.pfEarnings = pfEarnings;
		this.contributionEpf = contributionEpf;
		this.epfDifference = epfDifference;
		this.pensionFund = pensionFund;
		this.esiEarnings = esiEarnings;
		this.esiContributionEmployee = esiContributionEmployee;
		this.passportNo = passportNo;
		this.er = er;
		this.tax = tax;
		this.pinfl = pinfl;
		this.month = month;
		this.year = year;
		this.advance = advance;
		this.remarks = remarks;
		this.schoolName = schoolName;
	}

	public MasterSalaryHistoryDTO(String empCode, String employeeName, String dateOfJoining, String departmentName,
			String designationName, Double payDays, String bankName, String accountNo, String mfo, Float basic,
			Float hra, Float da, Float cca, Float ta, Float mr, Float fr, Float otherAllow, Float spl1, Float grossPay,
			Float pf, Float pt, Float esi, Float tds, Float advance1, Float advance2, Float totalEarning,
			Float totalDeduction, Float netPay, Float pfAccountNo, Float pfEarnings, Float contributionEpf,
			Float epfDifference, Float pensionFund, Float esiEarnings, Float esiContributionEmployee, String passportNo,
			Double er, Float tax, String pinfl, Integer month, Integer year, Float advance, String remarks,
			String schoolName, List<InvPayPaySlipDTO> invPayPaySlipDTOs) {

		this.empCode = empCode;
		this.employeeName = employeeName;
		this.dateOfJoining = dateOfJoining;
		this.departmentName = departmentName;
		this.designationName = designationName;
		this.payDays = payDays;
		this.bankName = bankName;
		this.accountNo = accountNo;
		this.mfo = mfo;
		this.basic = basic;
		this.hra = hra;
		this.da = da;
		this.cca = cca;
		this.ta = ta;
		this.mr = mr;
		this.fr = fr;
		this.otherAllow = otherAllow;
		this.spl1 = spl1;
		this.grossPay = grossPay;
		this.pf = pf;
		this.pt = pt;
		this.esi = esi;
		this.tds = tds;
		this.advance1 = advance1;
		this.advance2 = advance2;
		this.totalEarning = totalEarning;
		this.totalDeduction = totalDeduction;
		this.netPay = netPay;
		this.pfAccountNo = pfAccountNo;
		this.pfEarnings = pfEarnings;
		this.contributionEpf = contributionEpf;
		this.epfDifference = epfDifference;
		this.pensionFund = pensionFund;
		this.esiEarnings = esiEarnings;
		this.esiContributionEmployee = esiContributionEmployee;
		this.passportNo = passportNo;
		this.er = er;
		this.tax = tax;
		this.pinfl = pinfl;
		this.month = month;
		this.year = year;
		this.advance = advance;
		this.remarks = remarks;
		this.schoolName = schoolName;
		this.invPayPaySlipDTOs = invPayPaySlipDTOs;
	}

}
