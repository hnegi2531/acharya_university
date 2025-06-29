package com.au.service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.ConsoliatedAmountDTO;
import com.au.dto.ConsoliatedPayHistoryDTO;
import com.au.model.ConsoliatedAmount;
import com.au.model.ConsoliatedPayHistory;
import com.au.repository.ConsoliatedAmountRepository;
import com.au.repository.ConsoliatedPayHistoryRepository;
import com.au.response.ResponseHandler;

@Service
public class ConsoliatedPayHistoryService {

    @Autowired
    private ConsoliatedPayHistoryRepository consoliatedPayHistoryRepository;

    @Autowired
    private ConsoliatedAmountRepository consoliatedAmountRepository;

    public ResponseEntity<Object> getEmployeeDetailsForConsoliation(Integer empId, Integer month, Integer year) {
        try {

            ConsoliatedPayHistoryDTO consoliatedPayHistoryDTO = consoliatedPayHistoryRepository
                    .getEmployeeDetailsForConsoliation(empId, month, year);
            return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", consoliatedPayHistoryDTO);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
        }
    }

    public ResponseEntity<Object> saveConsoliation(ConsoliatedPayHistoryDTO consoliatedPayHistoryDTO) {
        try {
			/*Boolean isExistInMonthAndYear = consoliatedPayHistoryRepository
					.existsByMonthAndYearAndEmpId(consoliatedPayHistoryDTO.getMonth(), consoliatedPayHistoryDTO.getYear(),consoliatedPayHistoryDTO.getEmpId());
			if (isExistInMonthAndYear) {
				return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,
						"Already Consoliated for this month " + consoliatedPayHistoryDTO.getMonth(), null);
			}*/
            ConsoliatedPayHistory consoliatedPayHistory = new ConsoliatedPayHistory();
            consoliatedPayHistory.setEmpId(consoliatedPayHistoryDTO.getEmpId());
            consoliatedPayHistory.setMonth(consoliatedPayHistoryDTO.getMonth());
            consoliatedPayHistory.setYear(consoliatedPayHistoryDTO.getYear());
            consoliatedPayHistory.setPayingAmount(consoliatedPayHistoryDTO.getPayingAmount());
            consoliatedPayHistory.setConsoliatedAmountId(consoliatedPayHistoryDTO.getConsoliatedAmountId());
            Optional<ConsoliatedAmount> optionalConsoliatedAmount = consoliatedAmountRepository.findById(consoliatedPayHistoryDTO.getConsoliatedAmountId());
            if(!optionalConsoliatedAmount.isPresent()) {
                throw  new RuntimeException("Consolidated Amount is not present with id: " + consoliatedPayHistoryDTO.getConsoliatedAmountId());
            }
            /*Optional<Float> optionalFloat = consoliatedAmountRepository.getConsoliatedAmount(consoliatedPayHistoryDTO.getConsoliatedAmountId());
            Float consoliatedAmount = 0.0f;
            if (optionalFloat.isPresent()) consoliatedAmount = optionalFloat.get();
//			Float additionAmount = consoliatedAmountRepository.getSumOfAmount(consoliatedPayHistoryDTO.getEmpId(),consoliatedPayHistoryDTO.getMonth(),
//					consoliatedPayHistoryDTO.getYear());
//			Float aAmount = ObjectUtils.isNotEmpty(additionAmount) ? additionAmount : 0.0f;
//			Float remainingAmount = consoliatedPayHistoryRepository.findTopByEmpIdOrderByCreatedDateDesc(consoliatedPayHistoryDTO.getEmpId());
            Integer month;
            Integer year;
            if(consoliatedPayHistoryDTO.getMonth() == 1){
                month = 12;
                year = consoliatedPayHistoryDTO.getYear() - 1;
            }else{
                month = consoliatedPayHistoryDTO.getMonth() - 1;
                year = consoliatedPayHistoryDTO.getYear();
            }
            Optional<Float> optionalRA = consoliatedPayHistoryRepository.getRemainingAmount(consoliatedPayHistoryDTO.getConsoliatedAmountId(), month, year);
            Float remainingAmount = 0.0f;
            if (optionalRA.isPresent()) remainingAmount = optionalRA.get();
            Float rAmount = ObjectUtils.isNotEmpty(remainingAmount) ? remainingAmount : 0.0f;
            Float deductedAmount = 0.0f;
//			Float amount = ObjectUtils.isNotEmpty(additionAmount) ? consoliatedAmount + aAmount : consoliatedAmount;
//			consoliatedPayHistory.setTotalAmount(amount);

            System.out.println("rAmount------------------------------------ " + rAmount);
//			System.out.println("amount ---------------------------------------"+ amount);

            if (rAmount == 0.0f) {
                deductedAmount = consoliatedAmount - consoliatedPayHistoryDTO.getPayingAmount();
            } else {
                deductedAmount = rAmount - consoliatedPayHistoryDTO.getPayingAmount();
            }*/
            Float deductedAmount = optionalConsoliatedAmount.get().getRemainingAmount() - consoliatedPayHistoryDTO.getPayingAmount();
            if (deductedAmount < 0) {
                return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,
                        "Balance is low to consoliated", null);
            }
            Float tds = consoliatedPayHistoryDTO.getPayingAmount() * 0.10f;
            consoliatedPayHistory.setRemainingAmount(deductedAmount);
            optionalConsoliatedAmount.get().setRemainingAmount(deductedAmount);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + deductedAmount);
            consoliatedPayHistory.setTds(tds);
            Float netpay = consoliatedPayHistoryDTO.getPayingAmount() - tds;
            consoliatedPayHistory.setNetPay(netpay);
            consoliatedPayHistory.setActive(Boolean.TRUE);

            consoliatedPayHistoryRepository.save(consoliatedPayHistory);
            consoliatedAmountRepository.save(optionalConsoliatedAmount.get());
            return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
        }
    }

    public ResponseEntity<Object> getConsoliationList(Integer month, Integer year, Integer schoolId) {
        try {
            List<Map<String, Object>> consoliatedPayHistoryDTO = null;
            if (schoolId == null) {
                consoliatedPayHistoryDTO = consoliatedPayHistoryRepository.getConsoliationList(month, year);
            } else {
                consoliatedPayHistoryDTO = consoliatedPayHistoryRepository.getConsoliationListBySchoolId(month, year, schoolId);
            }
            return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", consoliatedPayHistoryDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
        }
    }

    public ResponseEntity<Object> updateConsoliation(ConsoliatedPayHistoryDTO consoliatedPayHistoryDTO) {
        try {
            ConsoliatedPayHistory consoliatedPayHistory = consoliatedPayHistoryRepository.findById(consoliatedPayHistoryDTO.getConsoliatedPayHistoryId()).get();
            consoliatedPayHistory.setPayingAmount(consoliatedPayHistoryDTO.getPayingAmount());
            Float remainingAmount = consoliatedPayHistory.getRemainingAmount();
            Float deductedAmount = 0.0f;
            deductedAmount = remainingAmount - consoliatedPayHistoryDTO.getPayingAmount();
            if (deductedAmount < 0) {
                return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,
                        "Balance is low to consoliated", null);
            }
            Float tds = consoliatedPayHistoryDTO.getPayingAmount() * 0.10f;
            consoliatedPayHistory.setRemainingAmount(deductedAmount);
            consoliatedPayHistory.setTds(tds);
            Float netpay = consoliatedPayHistoryDTO.getPayingAmount() - tds;
            consoliatedPayHistory.setNetPay(netpay);
            consoliatedPayHistory.setActive(Boolean.TRUE);

            consoliatedPayHistoryRepository.save(consoliatedPayHistory);

            return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
        }
    }

    public ResponseEntity<Object> getConsultants(Integer month, Integer year) {
        try {

            List<Map<String, Object>> consultants = consoliatedPayHistoryRepository.getConsultants(month, year);
            consultants.stream().forEach(c -> {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA " + (c.get("consoliatedAmount") == null ? null : c.get("consoliatedAmount").toString()));
            });
            return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", consultants);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
        }
    }

    public ResponseEntity<Object> saveAdditionAmount(ConsoliatedAmountDTO consoliatedAmountDTO) {
        try {
            ConsoliatedAmount consoliatedAmount = new ConsoliatedAmount();
            consoliatedAmount.setEmpId(consoliatedAmountDTO.getEmpId());
            consoliatedAmount.setFromDate(consoliatedAmountDTO.getFromDate());
            consoliatedAmount.setToDate(consoliatedAmountDTO.getToDate());
            consoliatedAmount.setMonth(consoliatedAmountDTO.getMonth());
            consoliatedAmount.setYear(consoliatedAmountDTO.getYear());
            consoliatedAmount.setSubject(consoliatedAmountDTO.getSubject());
            consoliatedAmount.setConsoliatedAmount(consoliatedAmountDTO.getAmount());
            consoliatedAmount.setRemarks(consoliatedAmountDTO.getRemarks());
            consoliatedAmount.setRemainingAmount(consoliatedAmountDTO.getAmount());
            consoliatedAmountRepository.save(consoliatedAmount);
            return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
        }
    }

    public ResponseEntity<Object> getConsoliationListByEmpId(Integer empId) {
        try {
            List<Map<String, Object>> consoliatedPayHistoryDTO = consoliatedPayHistoryRepository.getConsoliationListByEmpId(empId);
            if (consoliatedPayHistoryDTO.isEmpty()){
                List<ConsoliatedAmount> consoliatedAmountList = consoliatedAmountRepository.findByEmpId(empId);
                return  ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", consoliatedAmountList);
            }
            return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", consoliatedPayHistoryDTO);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
        }
    }


    public ResponseEntity<Object> getMonthWisePaymentHistory(Integer consoliatedAmountId) {

        List<ConsoliatedPayHistory> consoliatedPayHistories = consoliatedPayHistoryRepository.findByconsoliatedAmountId(consoliatedAmountId);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", consoliatedPayHistories);

    }
}
