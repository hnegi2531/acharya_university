package com.au.service;

import com.au.dto.ConsoliatedAmountDTO;
import com.au.model.ConsoliatedAmount;
import com.au.model.EmployeeDetails;
import com.au.repository.ConsoliatedAmountRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.response.ResponseHandler;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Author: Anjan
 * Date: 24-03-2025
 * Description: ConsoliatedAmountService class
 */
@Service
public class ConsoliatedAmountService {

    @Autowired
    private ConsoliatedAmountRepository consoliatedAmountRepository;

    @Autowired ConsoliatedPayHistoryService consoliatedPayHistoryService;

    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    public ResponseEntity<Object> addSubject(Integer consoliatedAmountId, String subject) {
        Optional<ConsoliatedAmount> optionalConsoliatedAmount = consoliatedAmountRepository.findById(consoliatedAmountId);
        if(!optionalConsoliatedAmount.isPresent())
            return ResponseHandler.generateResponse(false, HttpStatus.NOT_FOUND, "FAILURE",
                    "No Data available for the consoliatedAmountId: " + consoliatedAmountId);
        optionalConsoliatedAmount.get().setSubject(subject);
        consoliatedAmountRepository.save(optionalConsoliatedAmount.get());
        return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", "Subject added to the employee");
    }

    public ResponseEntity<Object> updateConsoliated(Integer consoliatedAmountId, String toDate) {
        Optional<ConsoliatedAmount> optionalConsoliatedAmount = consoliatedAmountRepository.findById(consoliatedAmountId);
        if(!optionalConsoliatedAmount.isPresent())
            return ResponseHandler.generateResponse(false, HttpStatus.NOT_FOUND, "FAILURE",
                    "No Data available for the consoliatedAmountId: " + consoliatedAmountId);
        optionalConsoliatedAmount.get().setToDate(toDate);
        consoliatedAmountRepository.save(optionalConsoliatedAmount.get());
        return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", "Updated successfully!!");
    }

    public ResponseEntity<Object> createExistingConsoliated(Integer empId) {
        List<EmployeeDetails> employeeDetailsList;
        if (empId != null)
            employeeDetailsList = Collections.singletonList(employeeDetailsRepository.findById(empId).get());
        else
            employeeDetailsList = employeeDetailsRepository.getConsultantEmployees();

        employeeDetailsList.forEach(employee ->{
            if(employee.getEmp_id() == 1) return;
            ConsoliatedAmountDTO consoliatedAmountDTO = getConsoliatedAmountDTO(employee);
            consoliatedPayHistoryService.saveAdditionAmount(consoliatedAmountDTO);
        });
        return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", "Consoliated Amount entry created successfully!!");
    }

    private static ConsoliatedAmountDTO getConsoliatedAmountDTO(EmployeeDetails savedEmployee) {
        LocalDate currentDate = LocalDate.now();
        String fromDate = savedEmployee.getDate_of_joining();
        String toDate = savedEmployee.getTo_date();
        fromDate = convertFormat(fromDate);
        toDate = convertFormat(toDate);
        ConsoliatedAmountDTO consoliatedAmountDTO = new ConsoliatedAmountDTO();
        consoliatedAmountDTO.setAmount(savedEmployee.getConsolidated_amount());
        consoliatedAmountDTO.setEmpId(savedEmployee.getEmp_id());
        consoliatedAmountDTO.setRemarks(savedEmployee.getRemarks());
        consoliatedAmountDTO.setMonth(currentDate.getMonthOfYear());
        consoliatedAmountDTO.setYear(currentDate.getYear());
        consoliatedAmountDTO.setSubject(null);
        consoliatedAmountDTO.setFromDate(fromDate);
        consoliatedAmountDTO.setToDate(toDate);
        return consoliatedAmountDTO;
    }

    private static String convertFormat(String givenFormat) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        String requiredFormat = "";
        try {
            // Parse the input date strings
            Date from = inputFormat.parse(givenFormat);

            // Format the dates into the required format
            requiredFormat = outputFormat.format(from);

            // Print the results
            System.out.println("Formatted fromDate: " + requiredFormat);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return requiredFormat;
    }
}
