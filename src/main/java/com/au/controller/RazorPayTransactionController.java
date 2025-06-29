package com.au.controller;

import com.au.response.ResponseHandler;
import com.au.service.RazorPayTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RazorPayTransactionController {

    @Autowired
    RazorPayTransactionService razorPayTransactionService;

    @GetMapping("/transaction")
    public ResponseEntity<Object> getTransactionByType(@RequestParam("type") String type, @RequestParam(value = "date",required = false) String date)
    {
        if (date == null || date.isEmpty()) {
            date = LocalDate.now().format(DateTimeFormatter.ISO_DATE); // Default to current date
        }
       
        return razorPayTransactionService.getTransactionDetailsByType(type,date);

    }

    @PostMapping("/transferToRazorPay")
    public ResponseEntity<Object> transferToRazorPay(@RequestParam(value="year", required = false) Integer year,@RequestParam(value="month", required = false) Integer month,@RequestParam(value="day", required = false) Integer day,@RequestParam(value="schoolId", required = false) Integer schoolId) {

        razorPayTransactionService.transferToRazorPay(year,month,day,schoolId);
        return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Settlement Processing started");

    }
}
