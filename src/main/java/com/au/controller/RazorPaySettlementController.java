package com.au.controller;

import com.au.dto.SettlementReportProjection;
import com.au.response.ResponseHandler;
import com.au.service.RazorPaySettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RazorPaySettlementController {

    @Autowired
    private RazorPaySettlementService razorPaySettlementService;
    @PostMapping("/razorPaySettlements")
    public ResponseEntity<Object> razorPaySettlements(@RequestParam(value="year", required = false) Integer year, @RequestParam(value="month", required = false) Integer month, @RequestParam(value="day", required = false) Integer day, @RequestParam(value="schoolId", required = false) Integer schoolId) {

        razorPaySettlementService.razorPaySettlements(year,month,day,schoolId);
        return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Settlement Processing started");

    }

    @GetMapping("/settelmentSummary")
    public ResponseEntity<List<SettlementReportProjection>> getSettlementReport(
            @RequestParam("settledDate") String settledDate) {
        return ResponseEntity.ok(razorPaySettlementService.getSettelmentSummaryReport(settledDate));
    }

    @GetMapping("/settelmentBySettlementIdAndDate")
    public ResponseEntity<Object> getSettlementReport(
            @RequestParam("settlementId") String settlementId, @RequestParam("date") String date) {
        return razorPaySettlementService.settelmentBySettlementIdAndDate(settlementId,date);
    }

    @GetMapping("/transferSettelmentBySettlementIdAndDate")
    public ResponseEntity<Object> getTransferSettlementsBySettlementIdAndDate(
            @RequestParam("settlementId") String settlementId, @RequestParam("date") String date) {
        return razorPaySettlementService.getTransferSettlementsBySettlementIdAndDate(settlementId,date);
    }

    @GetMapping("/getPendingBankImportSettlements")
    public ResponseEntity<Object> getPendingSettlements(@RequestParam("settlementId") String settlementId) {
        return razorPaySettlementService.getPendingSettlements(settlementId);
    }

    @GetMapping("/getReceiptGeneratedSettlements")
    public ResponseEntity<Object> getReceiptGeneratedSettlements(@RequestParam("settlementId") String settlementId) {
        return razorPaySettlementService.getReceiptGeneratedBankImportTransactionBySettlementId(settlementId);
    }
    @GetMapping("/allPendingSettlements")
    public ResponseEntity<Object> allPendingSettlements(@RequestParam("date") String date) {
        return razorPaySettlementService.allPendingSettlements(date);
    }

}
