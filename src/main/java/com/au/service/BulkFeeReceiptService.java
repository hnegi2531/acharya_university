package com.au.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.au.model.*;
import com.au.repository.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.au.dto.BulkFeeReceiptDto;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@Service
public class BulkFeeReceiptService {
	
	@Autowired
	private BulkFeeReceiptRepository bulkFeeReceiptRepository;
	
	@Autowired
	private TallyReceiptRepository tallyReceiptRepository;
	
	@Autowired
	private FeeReceiptRepository feeReceiptRepository;
	
	@Autowired
	private FinancialYearRepository financialYearRepository;
	
	@Autowired
	private BankImportTransactionRepository bankImportTransactionRepository;
	
	@Autowired
	private RTGSFeeHistoryRepository rTGSFeeHistoryRepository;
	
	@Autowired
	private CancelledFeeReceiptsRepository cancelledFeeReceiptsRepository;

	@Autowired
	private VoucherHeadNewRepository voucherHeadNewRepository;

	@Autowired
	StudentDetailsRepository studentDetailsRepository;

	@Autowired
	private FeeReceiptService feeReceiptService;
	
	
	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private DollarToInrConversionRepository dollarToInrConversionRepository;
	
	public List<BulkFeeReceipt> saveBulkFeeReceipt(BulkFeeReceiptDto frc,JwtDetails jwtDetails) throws Exception {


		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.now(); // Get current date without time
		int month = date.getMonthValue();
		int year = date.getYear();

		Date date1=null;
		date1 = java.sql.Date.valueOf(date);
// usd conversion
//		Double amountInInr;
//		if(frc.getReceived_in().equalsIgnoreCase("USD"))
//		{
//			DollarToInrConversion dollarToInrConversion = dollarToInrConversionRepository.findByDateAndActive(month,
//					year);
//			amountInInr = getConvertValue(frc.getAmount(), dollarToInrConversion);
//		} else {
//            amountInInr = frc.getAmount();
//        }

        FinancialYear f_year = financialYearRepository.getFinancialYearData(date1);

		
		List<BulkFeeReceipt> bulk_list = new ArrayList<BulkFeeReceipt>();
		Integer bulk_fee_receipt=bulkFeeReceiptRepository.getMaxId();
		 
		frc.getFr().getReceipt_type().substring(0,1);
		
		String number1 ;
		FeeReceipt latest_fee_recpt= feeReceiptRepository.getLastFeeReceiptByFinancialIdAndSchoolId(f_year.getFinancial_year_id(),frc.getSchool_id());
		String number = latest_fee_recpt != null ?latest_fee_recpt.getFee_receipt(): null;

			if(number== null) {

				number1 = String.format("%01d", 1);

			}
			
			else if(ObjectUtils.isNotEmpty(latest_fee_recpt)  && f_year.getFinancial_year_id() == latest_fee_recpt.getFinancial_year_id() && latest_fee_recpt.getSchool_id() == frc.getSchool_id()) {	
				Integer count = Integer.valueOf(number)+1;
				number1 = String.format("%01d", count );
		
			}else {

				number1 = String.format("%01d", 1);

			}
	   	String fee_receipt = number1;
		frc.getFr().setFee_receipt(fee_receipt);
		frc.getFr().setFinancial_year_id(f_year.getFinancial_year_id());
		frc.getFr().setCreated_by(jwtDetails.getUserId());
		frc.getFr().setCreated_username(jwtDetails.getUserName());
		FeeReceipt fee_rec = feeReceiptRepository.save(frc.getFr());
		 
		
		frc.getVoucher_head_new_id().entrySet().stream().forEach(bulk -> {
			BulkFeeReceipt bfr = new BulkFeeReceipt();
			
			bfr.setActive(Boolean.TRUE);
			bfr.setAmount(bulk.getValue());
			//bfr.setAmount(amountInInr);
			bfr.setFinancial_year_id(f_year.getFinancial_year_id());
			bfr.setFrom_name(frc.getFrom_name());
			bfr.setBulk_fee_receipt(bulk_fee_receipt+1);
			bfr.setReceived_in(frc.getReceived_in());
			bfr.setRemarks(frc.getRemarks());
			bfr.setSchool_id(frc.getSchool_id());
			bfr.setStudent_id(frc.getStudent_id());
			bfr.setTransaction_type(frc.getTransaction_type());
			bfr.setVendor_id(frc.getVendor_id());
			bfr.setCreated_by(frc.getCreated_by());
			bfr.setCreated_username(frc.getCreated_username());
			bfr.setVoucher_head_new_id(bulk.getKey());
			bfr.setAmount_in_som(bulk.getValue());
			bfr.setFee_receipt_id(Integer.valueOf(fee_rec.getFee_receipt()));
			bfr.setBank_id(frc.getBank_id());
			if(ObjectUtils.isNotEmpty(frc.getStudent_id())) {
				bfr.setStudent_id(frc.getStudent_id());
				Student_Details studentDetails =  studentDetailsRepository.getStudentByStudentId(frc.getStudent_id());
				bfr.setFrom_name(studentDetails.getStudent_name());
			}
			else {
				bfr.setFrom_name(frc.getFrom_name());
			}
			
			bulk_list.add(bfr);
//		});
			String voucherHeadName = voucherHeadNewRepository.getVoucherName(bulk.getKey());
		
		TallyReceipt tl = new TallyReceipt();
		tl.setFee_receipt_id(fee_rec.getFee_receipt_id());
		tl.setAuid(frc.getTr().getAuid());
		tl.setBank_institute(frc.getTr().getBank_institute());
		tl.setCreated_by(fee_rec.getCreated_by());
		tl.setCreated_username(fee_rec.getCreated_username());
		tl.setCreated_date(frc.getTr().getCreated_date());
		tl.setDd_bank_name(frc.getTr().getDd_bank_name());
		tl.setDd_no(frc.getTr().getDd_no());
		tl.setDeposited_bank(frc.getTr().getDeposited_bank());
		tl.setFee_receipt(fee_receipt);
		tl.setFinancial_year(f_year.getFinancial_year());
		tl.setParticulars(voucherHeadName);
		tl.setReceived_from(frc.getTr().getReceived_from());
		tl.setReceived_in(frc.getTr().getReceived_in());
		tl.setReceived_type(frc.getTr().getReceived_type());
		tl.setRemarks(frc.getTr().getRemarks());
		tl.setSchool_name(frc.getTr().getSchool_name());
	//	tl.setTally_receipt_id(frc.getTr().getTally_receipt_id());
			if(ObjectUtils.isNotEmpty(frc.getStudent_id())) {
				tl.setStudent_id(frc.getStudent_id());
				Student_Details studentDetails =  studentDetailsRepository.getStudentByStudentId(frc.getStudent_id());
				tl.setStudent_name(studentDetails.getStudent_name());
			}
			else {
				tl.setStudent_name(frc.getFrom_name());
			}
		tl.setTotal(bulk.getValue());
		tl.setTotal_amount(frc.getFr().getInr_value());
//		tl.setTotal_amount_som(frc.getTr().getTotal_amount_som());
//		tl.setTotal_som(frc.getTr().getTotal_som());
		tl.setTransaction_date(frc.getTr().getTransaction_date());
		tl.setTransaction_no(frc.getTr().getTransaction_no());
		tl.setTransaction_type(frc.getTr().getTransaction_type());
		tl.setUsn(frc.getTr().getUsn());
		tl.setVendor_id(frc.getTr().getVendor_id());
		tl.setActive(Boolean.TRUE);
		
		tallyReceiptRepository.save(tl);

		});
		
		 if(frc.getFr().getTransaction_type().equalsIgnoreCase("RTGS")) {
			 frc.getBit().setModified_by(jwtDetails.getUserId());
			 frc.getBit().setModified_username(jwtDetails.getUserName());
			 frc.getBit().setReceipt_no(fee_receipt);
			 frc.getBit().setFc_year_id(f_year.getFinancial_year_id());
			 
			 bankImportTransactionRepository.save(frc.getBit()); 
			 
			 RTGSFeeHistory rfh=new RTGSFeeHistory();
			 
			 rfh.setBank_transaction_history_id( frc.getBit().getBank_import_transaction_id());
			 rfh.setStudent_id(frc.getBit().getStudent_id());
			 rfh.setSchool_id(frc.getBit().getSchool_id());
			 rfh.setReceipt_no(fee_receipt);
			 rfh.setFc_year_id(f_year.getFinancial_year_id());
			 rfh.setRtgs_net_amount(frc.getBit().getAmount());
			 rfh.setPaid(frc.getBit().getPaid());
			 rfh.setRtgs_balance_amount(frc.getBit().getBalance());
			 rfh.setReceipt_type(frc.getFr().getReceipt_type());
			 rfh.setRemarks(frc.getBit().getTransaction_remarks());
			 rfh.setCreated_by(jwtDetails.getUserId());
			 rfh.setCreated_username(jwtDetails.getUserName());
			 rfh.setActive(true);
			 rTGSFeeHistoryRepository.save(rfh);
		 }
		 
		 bulkFeeReceiptRepository.saveAll(bulk_list);
		fee_rec.setBulk_id(String.valueOf(bulk_fee_receipt+1));
		feeReceiptRepository.save(fee_rec);
		return bulk_list;
		
	}
	
	public List<BulkFeeReceipt> listAll1() {
		return bulkFeeReceiptRepository.findAll11();
	}
	
//	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
//		Page<Object> vob_filtered_response = bulkFeeReceiptRepository.getAllDataFilteredByKeyword(pageable, keyword );
//		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, vob_filtered_response);
//		}
//
//	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
//		Page<Object> vob_sorted_response = bulkFeeReceiptRepository.getAllSortedData(pageable);
//		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, vob_sorted_response);
//	}
	
	public BulkFeeReceipt getFeeReceiptById(Integer id) {
		return bulkFeeReceiptRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("BulkFeeReceipt Not Found:" + id));
	}
	
	public BulkFeeReceipt updateFeeReceipt(BulkFeeReceipt frc) {
		return bulkFeeReceiptRepository.save(frc);
	}
	
	public void deActivateFeeReceipt(Integer id) {
		bulkFeeReceiptRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("BulkFeeReceipt Not Found:" + id));
		bulkFeeReceiptRepository.update(id);
	}

	public void activateFeeReceipt(Integer id) {
		bulkFeeReceiptRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("BulkFeeReceipt Not Found:" + id));
		bulkFeeReceiptRepository.update1(id);
	}

	
	public HashMap<String,Object> getDataForDisplayingBulkFeeReceipt(Integer fee_receipt_id, String transaction_type,Integer financial_year_id) {
		HashMap<String,Object> hs= new HashMap<String,Object>();
		
	
//		Double som_value = dollar_to_uzb_currency_conv_repo.getDollarToUzbekistaniCurrencyConversion().getUz_som();
//		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +som_value);
		
		if(transaction_type.equalsIgnoreCase("CASH")) {
			
			 List<Map<String, Object>> bulk_fee_data = bulkFeeReceiptRepository.getBulkFeeReceiptCashData(fee_receipt_id,transaction_type);
		  List<Map<String, Object>> student_data=	bulkFeeReceiptRepository.getDataForDisplayingStudentDetailsBulkFeeRcpt(fee_receipt_id);
		  hs.put("Voucher_Head_Wise_Amount", bulk_fee_data);
			hs.put("student_details", student_data);
			return hs;
			
		} else if(transaction_type.equalsIgnoreCase("RTGS") || transaction_type.equalsIgnoreCase("p_gateway")) {
			
			 List<Map<String, Object>> bulk_fee_data = bulkFeeReceiptRepository.getBulkFeeReceiptRtgsData(fee_receipt_id,transaction_type,financial_year_id);
			  List<Map<String, Object>> student_data=	bulkFeeReceiptRepository.getDataForDisplayingStudentDetailsBulkFeeRcpt(fee_receipt_id);
			  hs.put("Voucher_Head_Wise_Amount", bulk_fee_data);
				hs.put("student_details", student_data);
				return hs;
		
		} else if(transaction_type.equalsIgnoreCase("DD")) {
			
			 List<Map<String, Object>> bulk_fee_data = bulkFeeReceiptRepository.getBulkFeeReceiptRtgsData(fee_receipt_id,transaction_type,financial_year_id);
			  List<Map<String, Object>> student_data=	bulkFeeReceiptRepository.getDataForDisplayingStudentDetailsBulkFeeRcpt(fee_receipt_id);
			  hs.put("Voucher_Head_Wise_Amount", bulk_fee_data);
				hs.put("student_details", student_data);
				return hs;
		
		}else {
			throw new RuntimeException("transaction_type is not in CASH or RTGS");
		}
	}
	
	public HashMap<String,Object> getDataForDisplayingBulkFeeReceiptByStudentId(Integer student_id, Integer fee_receipt_id,String transaction_type,Integer financial_year_id) {
		HashMap<String,Object> hs= new HashMap<String,Object>();
		
		
//		Double som_value = dollar_to_uzb_currency_conv_repo.getDollarToUzbekistaniCurrencyConversion().getUz_som();
//		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +som_value);
		
		if(transaction_type.equalsIgnoreCase("CASH")) {
			
			 List<Map<String, Object>> bulk_fee_data = bulkFeeReceiptRepository.getBulkFeeReceiptCashDataByStudentId(student_id,fee_receipt_id,transaction_type,financial_year_id);
		  List<Map<String, Object>> student_data=	bulkFeeReceiptRepository.getDataForDisplayingStudentDetailsBulkFeeRcptByStudentId(student_id,fee_receipt_id,financial_year_id);
		  System.out.println("54534545454545454545454545454 "+bulk_fee_data);
		  hs.put("Voucher_Head_Wise_Amount", bulk_fee_data);
			hs.put("student_details", student_data);
			return hs;
			
		} else if(transaction_type.equalsIgnoreCase("RTGS") || transaction_type.equalsIgnoreCase("p_gateway")) {
			
			 List<Map<String, Object>> bulk_fee_data = bulkFeeReceiptRepository.getBulkFeeReceiptRtgsDataByStudentId(student_id,fee_receipt_id,transaction_type,financial_year_id);
			  List<Map<String, Object>> student_data=	bulkFeeReceiptRepository.getDataForDisplayingStudentDetailsBulkFeeRcptByStudentId(student_id,fee_receipt_id,financial_year_id);
			  hs.put("Voucher_Head_Wise_Amount", bulk_fee_data);
				hs.put("student_details", student_data);
				return hs;
		
		} else if(transaction_type.equalsIgnoreCase("DD")) {
			
			 List<Map<String, Object>> bulk_fee_data = bulkFeeReceiptRepository.getBulkFeeReceiptRtgsData(fee_receipt_id,transaction_type,financial_year_id);
			  List<Map<String, Object>> student_data=	bulkFeeReceiptRepository.getDataForDisplayingStudentDetailsBulkFeeRcpt(fee_receipt_id);
			  hs.put("Voucher_Head_Wise_Amount", bulk_fee_data);
				hs.put("student_details", student_data);
				return hs;
		
		}else {
			throw new RuntimeException("transaction_type is not in CASH or RTGS");
		}
	}

	public HashMap<String, Object> getDataForDisplayingBulkFeeReceiptAndCancel(Integer financial_year_id, Integer school_id, String fee_receipt) {
		HashMap<String,Object> hs= new HashMap<String,Object>();
		List<Map<String, Object>> bulk_fee_data = bulkFeeReceiptRepository.getDataForDisplayingBulkFeeReceiptAndCancel(financial_year_id,school_id,fee_receipt);
		  List<Map<String, Object>> student_data=	bulkFeeReceiptRepository.getDataForDisplayingStudentDetailsBulkFeeRcptAndCancel(fee_receipt);
		  System.out.println("54534545454545454545454545454 "+bulk_fee_data);
		  hs.put("Voucher_Head_Wise_Amount", bulk_fee_data);
			hs.put("student_details", student_data);
			return hs;
	}
	
	public String cancelBulkFeeReceipt(@RequestBody @Valid CancelledFeeReceipts cfr, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {

		feeReceiptService.cancelFeeReceipt1(cfr, jwtToken);

		 return "Receipt cancelled successfully";
	}

	private Double getConvertValue(Double amount, DollarToInrConversion dollarToInrConversion) {
		if (ObjectUtils.isNotEmpty(dollarToInrConversion)) {
			double conversionRate = dollarToInrConversion.getInr();
			double amountInINR = amount * conversionRate;
			return amountInINR;
		}
		return amount;
	}
}
