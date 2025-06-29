package com.au.controller;

import com.au.dto.ConsoliatedAmountDTO;
import com.au.model.ConsoliatedAmount;
import com.au.service.ConsoliatedAmountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Anjan
 * Date: 24-03-2025
 * Description: ConsoliatedAmountController class
 */
@RestController
@RequestMapping("/api/consoliatedAmount")
@RequiredArgsConstructor
@CrossOrigin
public class ConsoliatedAmountController {

    private final ConsoliatedAmountService consoliatedAmountService;

    @PatchMapping("/addSubject/{consoliatedAmountId}")
    public ResponseEntity<Object> addSubject(@PathVariable Integer consoliatedAmountId, @RequestParam String subject){
        return consoliatedAmountService.addSubject(consoliatedAmountId, subject);
    }

    @PatchMapping("/updateConsoliatedAmount/{consoliatedAmountId}")
    public ResponseEntity<Object> updateConsoliated(@PathVariable Integer consoliatedAmountId,@RequestParam String toDate){
        return consoliatedAmountService.updateConsoliated(consoliatedAmountId, toDate);
    }

    @PostMapping("/existingEmpConsoliated")
    public ResponseEntity<Object> existingEmployeeConsoliatedAmount(@RequestParam(required = false) Integer empId){
        return consoliatedAmountService.createExistingConsoliated(empId);
    }
}
