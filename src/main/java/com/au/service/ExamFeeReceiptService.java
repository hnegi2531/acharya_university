package com.au.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.ExamFeeReceiptDto;
import com.au.dto.JwtDetails;
import com.au.model.BankImportTransaction;
import com.au.model.ExamFeeReceipt;
import com.au.model.FeeReceipt;
import com.au.model.FinancialYear;
import com.au.model.RTGSFeeHistory;
import com.au.model.TallyReceipt;
import com.au.repository.Academic_year_repository;
import com.au.repository.BankImportTransactionRepository;
import com.au.repository.ExamFeeReceiptRepository;
import com.au.repository.FeeReceiptRepository;
import com.au.repository.FinancialYearRepository;
import com.au.repository.RTGSFeeHistoryRepository;
import com.au.repository.School_Repository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.TallyReceiptRepository;
import com.au.repository.VoucherHeadNewRepository;
import com.au.response.ResponseHandler;

@Service
public class ExamFeeReceiptService {
	
	Logger log=LoggerFactory.getLogger(ExamFeeReceiptService.class);
	
	@Autowired
	private ExamFeeReceiptRepository examFeeReceiptRepository;
	
	@Autowired
	private JwtTokenService jwtService;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private FinancialYearRepository financialYearRepository;
	
	@Autowired
	private Academic_year_repository academicYearRepository ;
	
	@Autowired
	private VoucherHeadNewRepository voucherHeadNewRepository;
	
	@Autowired
	private School_Repository schoolRepository;
	
	@Autowired
	private FeeReceiptRepository feeReceiptRepository;
	
	@Autowired
	private TallyReceiptRepository tallyReceiptRepository;
	
	@Autowired
	private BankImportTransactionRepository bankImportTransactionRepository;
	
	@Autowired
	private RTGSFeeHistoryRepository rTGSFeeHistoryRepository;
	
	private final ModelMapper modelMapper = new ModelMapper();
	
	private static final Integer DEFAULT_FEE_RECEIPT_NUMBER=1;

	public ResponseEntity<Object> createExamFeeReceipt(@Valid ExamFeeReceiptDto examFeeReceiptDto, String jwtToken) {
		try {
			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			Date currentDate=df.parse(df.format(new Date()).toString());
			FinancialYear financialYear=financialYearRepository.getFinancialYearData(currentDate);
			if(ObjectUtils.isEmpty(financialYear)) {
				throw new RuntimeException("Financial Year is not present for current year !! "); 
			}
			FeeReceipt lastFeeReceiptByFinancialAndSchool= feeReceiptRepository.getLastFeeReceiptByFinancialIdAndSchoolId(financialYear.getFinancial_year_id(),examFeeReceiptDto.getSchoolId());
			if(ObjectUtils.isNotEmpty(lastFeeReceiptByFinancialAndSchool) && ObjectUtils.isNotEmpty(lastFeeReceiptByFinancialAndSchool.getFee_receipt())) {
				String feeReceiptNumber = lastFeeReceiptByFinancialAndSchool.getFee_receipt();
				Integer incrementCount = Integer.valueOf(feeReceiptNumber)+1;
				examFeeReceiptDto.getFeeReceipt().setFee_receipt(String.format("%01d", incrementCount ));
			}else if(ObjectUtils.isNotEmpty(lastFeeReceiptByFinancialAndSchool) && ObjectUtils.isEmpty(lastFeeReceiptByFinancialAndSchool.getFee_receipt())) {
				throw new RuntimeException("Fee Receipt Number of last receipt data is null or empty !! ");
			} else {
				examFeeReceiptDto.getFeeReceipt().setFee_receipt(String.format("%01d", DEFAULT_FEE_RECEIPT_NUMBER ));
			}
			examFeeReceiptDto.getFeeReceipt().setFinancial_year_id(financialYear.getFinancial_year_id());
			examFeeReceiptDto.getFeeReceipt().setCreated_by(jwtDetails.getUserId());
			examFeeReceiptDto.getFeeReceipt().setCreated_username(jwtDetails.getUserName());
			FeeReceipt savedFeeRceipt=feeReceiptRepository.save(examFeeReceiptDto.getFeeReceipt());
            List<ExamFeeReceipt> examFeeReceipts=new ArrayList<>();
			examFeeReceiptDto.getVoucherHeadAndYearSemDtos().stream().forEach(vhYearOrSem -> {
				vhYearOrSem.getVoucherHeadWithAmountDtos().stream().forEach(vhn -> {
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				ExamFeeReceipt examFeeReceipt=modelMapper.map(vhYearOrSem, ExamFeeReceipt.class);
				examFeeReceipt.setCreatedBy(jwtDetails.getUserId());
				examFeeReceipt.setCreatedUsername(jwtDetails.getUserName());
				examFeeReceipt.setStudent(studentDetailsRepository.findById(examFeeReceiptDto.getStudentId()).get());
				examFeeReceipt.setFinancialYear(financialYear);
				examFeeReceipt.setAcYear(academicYearRepository.findById(examFeeReceiptDto.getAcYearId()).get());
				examFeeReceipt.setVoucherHeadNew(voucherHeadNewRepository.findById(vhn.getVoucherHeadNewId()).get());
				examFeeReceipt.setAmount(vhn.getAmount());
				examFeeReceipt.setSchool(schoolRepository.findById(examFeeReceiptDto.getSchoolId()).get());
				examFeeReceipt.setFeeReceipt(savedFeeRceipt);
				examFeeReceipt.setPaidYear(vhYearOrSem.getYearOrSem());
				examFeeReceiptRepository.save(examFeeReceipt);
				examFeeReceipts.add(examFeeReceipt);
				});
				
			});
			String examFeeReceiptIds=examFeeReceipts.stream().map(e -> e.getExamFeeReceiptId().toString()).collect(Collectors.joining(","));
			feeReceiptRepository.updateExamIdInFeeReceipt(examFeeReceiptIds,savedFeeRceipt.getFee_receipt_id());
			saveTallyReceipt(examFeeReceiptDto.getTallyReceipt(),savedFeeRceipt,financialYear);
			if(examFeeReceiptDto.getFeeReceipt().getTransaction_type().equalsIgnoreCase("RTGS")) {
				updateBankImportTransactionAndSaveRTGSFeeHistory(examFeeReceiptDto.getBankImportTransaction(),savedFeeRceipt,financialYear);
			}
			return	ResponseHandler.generateResponse(true, HttpStatus.CREATED,savedFeeRceipt);
		}catch(Exception e) {
			return	ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
		}
	}

	private void saveTallyReceipt(List<TallyReceipt> tallyReceipt, FeeReceipt savedFeeRceipt, FinancialYear financialYear) {
		tallyReceipt.stream().forEach( tr ->{
			tr.setFee_receipt_id(savedFeeRceipt.getFee_receipt_id());
			tr.setCreated_by(savedFeeRceipt.getCreated_by());
			tr.setCreated_username(savedFeeRceipt.getCreated_username());
			tr.setFee_receipt(savedFeeRceipt.getFee_receipt());
			tr.setFinancial_year(financialYear.getFinancial_year());
		});
		tallyReceiptRepository.saveAll(tallyReceipt);
	}
	
	private void updateBankImportTransactionAndSaveRTGSFeeHistory(BankImportTransaction bankImportTransaction,
			FeeReceipt savedFeeRceipt, FinancialYear financialYear) {
		bankImportTransaction.setModified_by(savedFeeRceipt.getCreated_by());
		bankImportTransaction.setModified_username(savedFeeRceipt.getCreated_username());
		bankImportTransaction.setReceipt_no(savedFeeRceipt.getFee_receipt());
		bankImportTransaction.setFc_year_id(financialYear.getFinancial_year_id());
		 
		 bankImportTransactionRepository.save(bankImportTransaction); 
		 
		 RTGSFeeHistory rfh=new RTGSFeeHistory();
		 
		 rfh.setBank_transaction_history_id( bankImportTransaction.getBank_import_transaction_id());
		 rfh.setStudent_id(bankImportTransaction.getStudent_id());
		 rfh.setSchool_id(bankImportTransaction.getSchool_id());
		 rfh.setReceipt_no(savedFeeRceipt.getFee_receipt());
		 rfh.setFc_year_id(financialYear.getFinancial_year_id());
		 rfh.setRtgs_net_amount(bankImportTransaction.getAmount());
		 rfh.setPaid(bankImportTransaction.getPaid());
		 rfh.setRtgs_balance_amount(bankImportTransaction.getBalance());
		 rfh.setReceipt_type(savedFeeRceipt.getReceipt_type());
		 rfh.setRemarks(bankImportTransaction.getTransaction_remarks());
		 rfh.setCreated_by(savedFeeRceipt.getCreated_by());
		 rfh.setCreated_username(savedFeeRceipt.getCreated_username());
		 rfh.setActive(true);
		 rTGSFeeHistoryRepository.save(rfh);
		
	}

	public ResponseEntity<Object> getExamFeeReceipt() {
		List<HashMap<String,Object>> examFeeReceiptDetails=examFeeReceiptRepository.getExamFeeReceipt();
		return	ResponseHandler.generateResponse(true, HttpStatus.CREATED,examFeeReceiptDetails);
	}

//	double  allTotal;
//	public ResponseEntity<Object> getExamFeeReceiptForRceiptByFeeRceiptId(Integer feeReceiptId) {
//		Map<String,Object> response=new HashMap<>();
//		List<HashMap<String,Object>>  feeRceiptWithStudentDetails=feeReceiptRepository.getFeeReceiptAndStudentDetailsByFeeReceiptId(feeReceiptId);
//		List<HashMap<String,Object>> examFeeRceipt=examFeeReceiptRepository.examFeeReceiptByFeeReceiptId(feeReceiptId);
//		Map<String, List<HashMap<String,Object>>>  formatedExamFeeReceipt=examFeeRceipt.stream().collect(Collectors.groupingBy(e -> e.get("voucherHead").toString()));
//		int maxSize = formatedExamFeeReceipt.values().stream()
//	            .mapToInt(List::size)
//	            .max()
//	            .orElse(0);
//	    Map<Object, List<HashMap<String, Object>>> resultMap = new HashMap<>();
//	    formatedExamFeeReceipt.forEach((voucherHead, dataList) -> {
//	    		double total=dataList.stream().mapToDouble(e -> (Double )e.get("amount")).sum();
//	    		dataList.forEach(e  -> e.put("totalAmount",total));
//	            List<HashMap<String, Object>> permitData = new ArrayList<>();
//	            
//	            while (dataList.size() < maxSize) {
//	            	permitData.add(new HashMap<>());
//	            }
//	            permitData.addAll(new ArrayList<>(dataList));
//	            allTotal += total;
//	            resultMap.put(voucherHead, permitData);
//	            
//	        });
//	
//	    response.put("totalPaidAmount", allTotal);
//		response.put("feeRceiptWithStudentDetails", feeRceiptWithStudentDetails);
//		response.put("examFeeRceipt", resultMap);
//		response.put("examFeeRceiptForSem", examFeeRceipt);
//		allTotal=0;
//		return	ResponseHandler.generateResponse(true, HttpStatus.CREATED,response);
//	}
	
	public ResponseEntity<Object> getExamFeeReceiptForRceiptByFeeRceiptId(Integer feeReceiptId) {
	    Map<String, Object> response = new HashMap<>();
	    
	    List<HashMap<String, Object>> feeReceiptWithStudentDetails = feeReceiptRepository.getFeeReceiptAndStudentDetailsByFeeReceiptId(feeReceiptId);
	    List<HashMap<String, Object>> examFeeReceipt = examFeeReceiptRepository.examFeeReceiptByFeeReceiptId(feeReceiptId);
	    
	    Map<String, List<HashMap<String, Object>>> formattedExamFeeReceipt = examFeeReceipt.stream()
	            .collect(Collectors.groupingBy(e -> e.get("voucherHead").toString()));
	    
	    double allTotal = 0;
	    
	    
	    Map<String, List<HashMap<String, Object>>> resultMap = new HashMap<>();
	    for (Map.Entry<String, List<HashMap<String, Object>>> entry : formattedExamFeeReceipt.entrySet()) {
	        List<HashMap<String, Object>> dataList = entry.getValue();
	        
	        double total = dataList.stream()
	                .mapToDouble(e -> (Double) e.get("amount"))
	                .sum();
	        
	        
	        for (HashMap<String, Object> record : dataList) {
	            record.put("totalAmount", total);
	        }
	        
	        
	        int maxSize = formattedExamFeeReceipt.values().stream()
	                .mapToInt(List::size)
	                .max()
	                .orElse(0);
	        
	        List<HashMap<String, Object>> permitData = new ArrayList<>(dataList);
	        
	        
	        while (permitData.size() < maxSize) {
	            permitData.add(new HashMap<>());
	        }

	        
	        allTotal += total;
	        
	        resultMap.put(entry.getKey(), permitData);
	    }

	    
	    response.put("totalPaidAmount", allTotal);
	    response.put("feeReceiptWithStudentDetails", feeReceiptWithStudentDetails);
	    response.put("examFeeReceipt", resultMap);
	    response.put("examFeeReceiptForSem", examFeeReceipt);

	    return ResponseHandler.generateResponse(true, HttpStatus.CREATED, response);
	}

	

}
