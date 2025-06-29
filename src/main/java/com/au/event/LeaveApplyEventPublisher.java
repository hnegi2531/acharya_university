package com.au.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.au.dto.ApiResponseLeaveApply;
import com.au.dto.ApiResponseLeaveApplyDTO;
import com.au.dto.ApiResponsephp;
import com.au.model.EmployeeDetails;
import com.au.model.LeaveApply;
import com.au.repository.EmployeeDetailsReportRepository;
import com.au.repository.LeaveApplyRepository;
import com.au.repository.LeaveTypeRepository;

@Component
public class LeaveApplyEventPublisher {

	@Autowired
	private EmployeeDetailsReportRepository employeeDetailsReportRepository;

	@Autowired
	private LeaveTypeRepository leaveTypeRepository;

	@Autowired
	private LeaveApplyRepository leaveApplyRepository;

	private static final String API_URL = "https://acharyainstitutes.in/index.php?r=acerp-api/ach-leaves&datee=";

	@Async
	@EventListener
	public void handleLeaveApplyEvent(LeaveApplyEventDTO leaveApplyEventDTO) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String urlString = "acharyainstitutes.in/index.php?r=acerp-api/ach-leaves&datee="
					+ leaveApplyEventDTO.getDate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<ApiResponseLeaveApply> responseEntity = restTemplate.exchange(
					API_URL + leaveApplyEventDTO.getDate(), HttpMethod.GET, entity, ApiResponseLeaveApply.class);
			ApiResponseLeaveApply apiResponse = responseEntity.getBody();

			if (apiResponse != null && apiResponse.isSuccess()) {

				for (ApiResponseLeaveApplyDTO apiResponseLeaveApply : apiResponse.getData()) {
				try {
					System.out.println(apiResponseLeaveApply.getContract_empcode());
					EmployeeDetails employeeDetails = employeeDetailsReportRepository
							.findByContractCode(apiResponseLeaveApply.getContract_empcode());
					SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date fromDate = originalFormat.parse(apiResponseLeaveApply.getFrom_date());
					Date toDate = originalFormat.parse(apiResponseLeaveApply.getTo_date());
					SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
					String newFromDate = newFormat.format(fromDate);
					String newToDate = newFormat.format(toDate);
					if (ObjectUtils.isNotEmpty(employeeDetails)) {
						System.out.println(employeeDetails.getEmp_id());
						LeaveApply isExists = leaveApplyRepository.existsBetweenDate(
								newFromDate, newToDate,
								employeeDetails.getEmp_id());
						if (ObjectUtils.isEmpty(isExists)) {
							LeaveApply leaveApply = new LeaveApply();
							leaveApply.setActive(Boolean.TRUE);
							leaveApply.setEmp_id(employeeDetails.getEmp_id());
							leaveApply.setApproved_status(2);
							leaveApply.setFrom_date(newFromDate);
							leaveApply.setTo_date(newToDate);
							Calendar calendar=Calendar.getInstance();
							calendar.setTime(toDate);
							Integer year=calendar.get(Calendar.YEAR);
							leaveApply.setYear(String.valueOf(year));
							leaveApply.setNo_of_days_applied(
									Float.valueOf(apiResponseLeaveApply.getNo_of_days_applied()));
							Integer leaveId=getLeaveId(apiResponseLeaveApply.getLeave_short_name());
							leaveApply.setLeave_id(leaveId);
							leaveApplyRepository.save(leaveApply);
						}
					}
				}catch(Exception e) {
					System.out.println(e.getMessage());
					
				}
				}

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private Integer getLeaveId(String leave_short_name) {

		return leaveTypeRepository.getLeaveIdByLeaveTypeShort(leave_short_name);
	}
}
