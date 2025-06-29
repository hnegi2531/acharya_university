package com.au.scheduler;

import org.joda.time.LocalDate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.au.event.LmsAttendanceEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LmsAttendanceSchedular {


	private ApplicationEventPublisher applicationEventPublisher;

	@Scheduled(cron = "0 0 20 * * *")
	public void lmsAttendanceScheduler() {
		try {
			LmsAttendanceEvent lae=new LmsAttendanceEvent(LocalDate.now());
			applicationEventPublisher.publishEvent(lae);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
