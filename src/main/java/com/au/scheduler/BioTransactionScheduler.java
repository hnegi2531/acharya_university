package com.au.scheduler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.au.event.BioTransactionEvent;

@Component
public class BioTransactionScheduler {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    @Scheduled(cron = "0 0 8,10,12,14,16,18,20,22 * * ?", zone = "Asia/Kolkata")
    public void bioTransactionScheduler() {
        try {
            System.out.println("Started bioTransactionScheduler");
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println(currentDate);

            BioTransactionEvent bioTransactionEvent = new BioTransactionEvent(currentDate, null);
            applicationEventPublisher.publishEvent(bioTransactionEvent);

        } catch (Exception e) {
            System.out.println("Exception occur in Bio transaction scheduler " + e.getMessage());
            if (e instanceof DataAccessException) {
                SQLException sqlException = (SQLException) ((DataAccessException) e).getMostSpecificCause();
                System.out.println("SQL Error Code: " + sqlException.getErrorCode());
                System.out.println("SQL State: " + sqlException.getSQLState());
            }
        }
    }

}
