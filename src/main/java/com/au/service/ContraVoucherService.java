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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.dto.JwtDetails;
import com.au.model.ContraVoucher;
import com.au.model.FinancialYear;
import com.au.model.PaymentVoucher;
import com.au.repository.ContraVoucherRepository;
import com.au.repository.FeeReceiptRepository;
import com.au.repository.FinancialYearRepository;
import com.au.repository.PaymentVoucherRepository;
import com.au.repository.PettyCashRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class ContraVoucherService {
	
	@Autowired
	private ContraVoucherRepository contraVoucherRepository;

	@Autowired
	private PaymentVoucherRepository paymentVoucherRepository;
	
	@Autowired
	private FinancialYearRepository financialYearRepository;
	
	@Autowired
	private FeeReceiptRepository feeReceiptRepository;
	
	@Autowired
    private PettyCashRepository pettyCashRepository;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	Integer count = 0;
	public List<ContraVoucher> savePaymentVoucher(@Valid List<ContraVoucher> contraVoucherList, String jwtToken) 
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDateTime now = LocalDateTime.now();
	    String date = dtf.format(now);
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    Date currentDate;

	    try {
	        currentDate = df.parse(date);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error parsing current date");
	    }

	    for (ContraVoucher cv : contraVoucherList) {
	        FinancialYear f_year = financialYearRepository.getFinancialYearData(currentDate);
	        if (f_year == null) {
	            throw new NullPointerException("Financial Year Is Not Created Yet !!!");
	        }
	        
	        Integer pVoucher_no = paymentVoucherRepository.getLatestData(f_year.getFinancial_year_id(), cv.getSchool_id());
	        Integer cVoucher_no = contraVoucherRepository.getLatestData(f_year.getFinancial_year_id(), cv.getSchool_id());

	        int latestVoucherNo = Math.max(
	            (pVoucher_no != null ? pVoucher_no : 0),
	            (cVoucher_no != null ? cVoucher_no : 0)
	        ) + 1;

	        cv.setVoucher_no(latestVoucherNo);
	        cv.setFinancial_year_id(f_year.getFinancial_year_id());

	        if (!cv.getInter_school_id().equals(cv.getSchool_id())) {
	            PaymentVoucher pv = new PaymentVoucher();
	            pv.setSchool_id(cv.getInter_school_id());
	            pv.setBank_id(cv.getBank_id());
	            pv.setVoucher_no(latestVoucherNo);
	            pv.setFinancial_year_id(f_year.getFinancial_year_id());
	            pv.setInter_school_id(cv.getSchool_id());
	            pv.setActive(cv.getActive());
	            pv.setDebit(cv.getDeposited_amount().toString());
	            pv.setCredit(cv.getDeposited_amount());
	            pv.setDebit_total(cv.getDeposited_amount());
	            pv.setCredit_total(cv.getDeposited_amount());
	            pv.setCreated_by(jwtDetails.getUserId());
	            pv.setCreated_username(jwtDetails.getUserName());
	            pv.setType("INTER-COLLEGE");
	            pv.setDate(date);

	            paymentVoucherRepository.save(pv); // Save to PaymentVoucher table
	        }
	    }

	    return contraVoucherRepository.saveAll(contraVoucherList);
	}


	public List<ContraVoucher> allActiveContraVoucher() {
		return contraVoucherRepository.allActiveContraVoucher();
	}

	public ContraVoucher get(Integer id) {
		return contraVoucherRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(" contraVoucher:" + id));
	}
	
	public List<ContraVoucher> updateContraVoucher(List<ContraVoucher> cos) {
		return contraVoucherRepository.saveAll(cos);
	}
	
	public void deactivate(Integer id) {
		ContraVoucher ed = contraVoucherRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("contraVoucher Not Found:" + id));
		contraVoucherRepository.deactivate(id);
	}

	public void activate(Integer id) {
		ContraVoucher ed = contraVoucherRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("contraVoucher Not Found:" + id));
		contraVoucherRepository.activate(id);
	}

	public ResponseEntity<Object> fetchAllContraVoucher(Pageable pageable, Object keyword) {
		Page<Object> response1 = contraVoucherRepository.fetchAllContraVoucher(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllContraVoucherWoKeyword(Pageable pageable) {	
		Page<Object> response = contraVoucherRepository.fetchAllContraVoucherWoKeyword(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public Map<String, Object> getBalanceAmountAndClosingCase(String selected_date) {

	    Float paid_amount = feeReceiptRepository.getPaidAmount(selected_date);   // a 11000
	    Double amount = pettyCashRepository.getTotalAmount(selected_date);       // b 0
	    Double balance = contraVoucherRepository.getBalance(selected_date);      // c 11000
	    
	    double closing_cash = paid_amount - amount;    //11000
	    double balanceAmount = closing_cash - balance;  // 
	    double cashSummary = paid_amount + amount;
	    
	    

	    Map<String, Object> result = new HashMap<>();
	    result.put("closing_cash", closing_cash);
	    result.put("balance", balanceAmount);  
	    result.put("net_amount", closing_cash);
	    result.put("cash_summary", cashSummary);
	    result.put("cash_received", paid_amount);
	    return result;
	}	

	public Map<String, Object> getInsData(String selected_date) {
	  
		Map<String, Object> paid_amountHos = feeReceiptRepository.getPaidAmountHos(selected_date);
	    List<Map<String, Object>> paid_amountGroupBy = feeReceiptRepository.getPaidAmountWoHos(selected_date);


	    if (paid_amountHos != null && paid_amountHos.containsKey("Hos")) {
	        Map<String, Object> hostelEntry = new HashMap<>();
	        hostelEntry.put("paidAmount", paid_amountHos.get("Hos"));
	        hostelEntry.put("school_id", 13);
	        hostelEntry.put("school_name_short", "Hostel");
	        hostelEntry.put("hostel_status", 1);
	        paid_amountGroupBy.add(hostelEntry);
	    }

	    List<Map<String, Object>> contraAmountList = contraVoucherRepository.getContraAmountGroupBy(selected_date);
	    List<Map<String, Object>> pettyCashList = pettyCashRepository.getTotalAmountGroupBy(selected_date);

	    Map<Integer, Double> contraMap = new HashMap<>();
	    for (Map<String, Object> row : contraAmountList) {
	        Integer schoolId = (Integer) row.get("school_id");
	        Double amount = row.get("deposited_amount") != null
	                ? ((Number) row.get("deposited_amount")).doubleValue()
	                : 0.0;
	        contraMap.put(schoolId, amount);
	    }

	    Map<Integer, Double> pettyMap = new HashMap<>();
	    for (Map<String, Object> row : pettyCashList) {
	        Integer schoolId = (Integer) row.get("school_id");
	        Double amount = row.get("totalAmount") != null
	                ? ((Number) row.get("totalAmount")).doubleValue()
	                : 0.0;
	        pettyMap.put(schoolId, amount);
	    }

	    List<Map<String, Object>> finalResult = new ArrayList<>();
	    List<Map<String, Object>> hostelResult = new ArrayList<>();

	    for (Map<String, Object> row : paid_amountGroupBy) {
	        Integer schoolId = (Integer) row.get("school_id");
	        String schoolName = (String) row.get("school_name_short");
	        Double paidAmount = row.get("paidAmount") != null
	                ? ((Number) row.get("paidAmount")).doubleValue()
	                : 0.0;
	        Integer hostelStatus = (Integer) row.get("hostel_status"); 

	        Double petty = pettyMap.getOrDefault(schoolId, 0.0);
	        Double contra = contraMap.getOrDefault(schoolId, 0.0);

	        Double balance = paidAmount - petty - contra;

	        Map<String, Object> resultRow = new HashMap<>();
	        resultRow.put("school_id", schoolId);
	        resultRow.put("school_name_short", schoolName);
	        resultRow.put("paidAmount", paidAmount);
	        resultRow.put("pettyCash", petty);
	        resultRow.put("contraAmount", contra);
	        resultRow.put("balance", balance);

	        if (hostelStatus != null && hostelStatus == 1) {
	            hostelResult.add(resultRow); // Add to hostelResult for status 1
	        } else {
	            finalResult.add(resultRow); // Add to finalResult for non-hostel schools
	        }
	    }

	    finalResult.addAll(hostelResult);

	    Map<String, Object> response = new HashMap<>();
	    response.put("data", finalResult);
	    return response;
	}


	public List<Map<String, Object>> getContraVoucherData(Integer voucher_no,Integer school_id,Integer financial_year_id){
		return contraVoucherRepository.getContraVoucherData(voucher_no,school_id,financial_year_id);
	}

}
