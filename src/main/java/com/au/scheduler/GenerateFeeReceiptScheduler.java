package com.au.scheduler;


import com.au.event.BioTransactionEvent;
import com.au.event.GenerateFeeReceiptEvent;
import com.au.model.BankImportTransaction;
import com.au.model.SchedulerLock;
import com.au.repository.BankImportTransactionRepository;
import com.au.repository.SchedulerLockRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GenerateFeeReceiptScheduler {

    @Autowired
    BankImportTransactionRepository bankImportTransactionRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private SchedulerLockRepository schedulerLockRepository;

    @Scheduled(cron = "0 10 * * * ?", zone = "Asia/Kolkata")
    public void generateFeeReceiptScheduler() {
//        try {
//            System.out.println("Started Generate Fee Receipt Scheduler");
//            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            System.out.println(currentDate);
//
//            List<BankImportTransaction> bankImportTransactionsOfCollegeFee =
//                    bankImportTransactionRepository.getAllPendingBankImportTransactionByReceiptType("College Fees");
//            applicationEventPublisher.publishEvent(new GenerateFeeReceiptEvent(bankImportTransactionsOfCollegeFee));
//
//            List<BankImportTransaction> bankImportTransactionsOfExamFee =
//                    bankImportTransactionRepository.getAllPendingBankImportTransactionByReceiptType("Exam Fee");
//            applicationEventPublisher.publishEvent(new GenerateFeeReceiptEvent(bankImportTransactionsOfExamFee));
//
//            List<BankImportTransaction> bankImportTransactionsOfBulk =
//                    bankImportTransactionRepository.getAllPendingBankImportTransactionByReceiptType("Bulk");
//            applicationEventPublisher.publishEvent(new GenerateFeeReceiptEvent(bankImportTransactionsOfBulk));
//
//            List<BankImportTransaction> bankImportTransactionsOfRegistrationFee =
//                    bankImportTransactionRepository.getAllPendingBankImportTransactionByReceiptType("Registration Fee");
//            applicationEventPublisher.publishEvent(new GenerateFeeReceiptEvent(bankImportTransactionsOfRegistrationFee));
//
//        } catch (Exception e) {
//            System.out.println("Exception occur in Generate Fee Receipt Scheduler: " + e.getMessage());
//        }
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
        String formattedDateTime = now.format(formatter);
        String uniqueJobName = "settlementJob_" + formattedDateTime;

        SchedulerLock newLock = new SchedulerLock();
        newLock.setJobName(uniqueJobName); // Ensures a unique name
        newLock.setLockedBy("Instance-" + UUID.randomUUID());
        newLock.setLockedAt(LocalDateTime.now());
        newLock.setStatus("LOCKED");



        try {

            schedulerLockRepository.save(newLock);
            System.out.println("Processing settlements for " + uniqueJobName);

            System.out.println("Started Generate Fee Receipt Scheduler");

            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println(currentDate);

            // Fetch all pending transactions in ONE query instead of four
            List<BankImportTransaction> pendingTransactions =
                    bankImportTransactionRepository.findAllPendingBankImportTransactions();

            System.out.println("All pending bank import - "+pendingTransactions);

            if (pendingTransactions.isEmpty()) {
                System.out.println("No pending transactions found.");
                return;
            }


            // Publish event for processing
            applicationEventPublisher.publishEvent(new GenerateFeeReceiptEvent(pendingTransactions));

            Thread.sleep(3000);

            newLock.setStatus("UNLOCKED");
            schedulerLockRepository.save(newLock);

        }

        catch (Exception e) {
            if (e instanceof DataIntegrityViolationException || e instanceof ConstraintViolationException) {
                System.out.println("Scheduler is already locked by another instance!"+e.getMessage());
            } else {
                System.out.println("Unexpected error in scheduler: " + e.getMessage());
                newLock.setStatus("UNLOCKED");
                schedulerLockRepository.save(newLock);
            }
        }
    }
}




