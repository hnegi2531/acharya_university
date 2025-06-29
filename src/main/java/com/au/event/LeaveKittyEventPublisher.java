package com.au.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.au.constant.LeaveKittyEnum;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.au.dto.EmployeeDetailsForLeavePattern;
import com.au.model.LeaveKitty;
import com.au.model.LeavePattern;
import com.au.model.LeaveType;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.EmployeeTypeRepository;
import com.au.repository.JobTypeRepository;
import com.au.repository.LeaveApplyRepository;
import com.au.repository.LeaveKittyRepository;
import com.au.repository.LeavePatternRepository;
import com.au.repository.LeaveTypeRepository;

@Component
public class LeaveKittyEventPublisher {

	@Autowired
	private LeavePatternRepository leavePatternRepository;

	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;

	@Autowired
	private LeaveTypeRepository leaveTypeRepository;

	@Autowired
	private EmployeeTypeRepository employeeTypeRepository;


	@Autowired
	private LeaveApplyRepository leaveApplyRepository;
	
	@Autowired
	private JobTypeRepository jobTypeRepository;
	
	@Autowired
	private LeaveKittyRepository leaveKittyRepository;

	@EventListener
	@Async
	public void handleLeaveKittyEventPublisher(LeaveKittyEvent leaveKittyEvent) {
		try {
			int currentYear = Year.now().getValue();
			List<EmployeeDetailsForLeavePattern> employeeDetailsForLeavePattern;
			if(leaveKittyEvent.getEmpId() != null)
				employeeDetailsForLeavePattern = Collections.singletonList(employeeDetailsRepository
						.getEmployeeDetailsForLeavePatternWithEmployeeId(leaveKittyEvent.getEmpId()));

			else employeeDetailsForLeavePattern = employeeDetailsRepository.getEmployeeDetailsForLeavePattern();

			if (ObjectUtils.isNotEmpty(employeeDetailsForLeavePattern)) {
				employeeDetailsForLeavePattern.forEach(elp -> {

					List<LeavePattern> leavePattern = leavePatternRepository.getLeavePatternByEmpTypeIdAndJobTypeIdAndSchoolId(elp.getEmpTypeId(), elp.getJobTypeId(),
									elp.getSchoolId(),currentYear);
					String empType = employeeTypeRepository.getEmployeeTypeName(elp.getEmpTypeId());
                    String jobType=jobTypeRepository.getJobTypeName(elp.getJobTypeId());
					leavePattern.forEach(lp -> {
						try {
							System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id());
							LeaveKitty leaveKitty = leaveKittyRepository.getByEmpIdAndLeaveId(elp.getEmpId(), lp.getLeave_pattern_id());
							LeaveType leaveType = leaveTypeRepository.fetchLeaveType(lp.getLeave_id());
							System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short());
							
							if (ObjectUtils.isEmpty(leaveKitty)) {
								leaveKitty = new LeaveKitty();
								leaveKitty.setActive(Boolean.TRUE);
							}

							//Casual leave for probationary
							if(LeaveKittyEnum.CAUSAL_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())) {
								handleCLForProbationary(leaveKitty, leaveType, lp, elp, empType);
							}

							//Casual leave for permanent
							if(LeaveKittyEnum.CAUSAL_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())) {
								handleCLForPermanent(leaveKitty, leaveType, lp, elp, empType);
							}

							//Earned leave for permanent
							if(LeaveKittyEnum.EARNED_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())) {
								handleELForPermanent(leaveKitty, leaveType, lp, elp, empType, jobType);
							}

						} catch (Exception e) {
							// Log the error and continue with the next leave pattern
							System.out.println("Exception occurred for EmpId: " + elp.getEmpId() + ", LeavePatternId: "
									+ lp.getLeave_pattern_id() + ". Error: " + e.getMessage());
						}
					});
				});
			}

		} catch (Exception e) {
			System.out.println("Exception occur:- " + e.getMessage());
		}

	}

	public void handleELForPermanent(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp, String empType, String jobType) {

		if (StringUtils.equals(empType, "ORR")
				&& (StringUtils.equals(jobType, "Non Teaching") || StringUtils.equals(jobType, "Teaching Admin") || StringUtils.equals(jobType, "NT Lab"))
				&& ObjectUtils.isNotEmpty(elp.getPermanentStatus()) && elp.getPermanentStatus() == 2) {
			assignElToPermanent(leaveKitty, leaveType, lp, elp);
		}
	}

	public void handleCLForPermanent(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp, String empType) {

		if (StringUtils.equals(empType, "ORR") && ObjectUtils.isNotEmpty(elp.getPermanentStatus()) && elp.getPermanentStatus() == 2) {
			assignClToORRPermanentStaff(leaveKitty, leaveType, lp, elp);
		}
	}

	public void handleCLForProbationary(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp, String empType) {

		if ((StringUtils.equals(empType, "FTE") || StringUtils.equals(empType, "ORR") || StringUtils.equals(empType, "CON"))
				&& ObjectUtils.isNotEmpty(elp.getPermanentStatus()) && elp.getPermanentStatus() == 1) {
			assignClForProbationaryStaff(leaveKitty, leaveType, lp, elp);
		}
	}

	private void assignClForProbationaryStaff(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp) {

		try {
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int currentYear = Year.now().getValue();
			int month = calendar.get(Calendar.MONTH) + 1;
		    int day = calendar.get(Calendar.DAY_OF_MONTH);
			
			String year = String.valueOf(currentYear);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Double accumalatedCount=0.0;
			if(month==1) {
				accumalatedCount = 1.0;
			}

			Date dj = simpleDateFormat.parse(elp.getDateOfJoining());
			if (dj.before(date) && month > 1 && day >= 16 ) {
				/*Integer leaveApplyCount = leaveApplyRepository.getCountLeaveApply(elp.getEmpId(), lp.getLeave_id(), year);

				 accumalatedCount = calculateClAccumulatedCountOfProbationary(month, day);

				if (lp.getLeave_days_permit() >= accumalatedCount) {
					Double updateCount = accumalatedCount - leaveApplyCount;
					if (updateCount > 0.0) {
						leaveKitty.setAccumulated_count(updateCount);
					} else {
						leaveKitty.setAccumulated_count(0.0);
					}
				} */
				accumalatedCount = leaveKitty.getAccumulated_count() + 1.0;
			}
			System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short()+" "+accumalatedCount);

			if(accumalatedCount < 12)
				leaveKitty.setAccumulated_count(accumalatedCount);
			leaveKitty.setAccumulated_date(new Date());
			leaveKitty.setInitial_days_count(lp.getLeave_days_permit());
			leaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
			leaveKitty.setEmp_id(elp.getEmpId());

			leaveKittyRepository.save(leaveKitty);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		
	}

	private void assignMLToPermanent(LeaveKitty LeaveKitty, LeaveType leaveType, LeavePattern lp,
			EmployeeDetailsForLeavePattern elp) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 20);
			Date date = calendar.getTime();
			int currentYear = Year.now().getValue();

			String year = String.valueOf(currentYear);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date dj = simpleDateFormat.parse(elp.getDateOfJoining());
			if (dj.before(date)) {
				Integer leaveApplyCount = leaveApplyRepository.getCountLeaveApply(elp.getEmpId(), lp.getLeave_id(),
						year);

				if (leaveApplyCount <= lp.getLeave_days_permit()) {
					LeaveKitty.setAccumulated_count((double) leaveApplyCount);
				} else {
					LeaveKitty.setAccumulated_count(0.0);
				}

				LeaveKitty.setAccumulated_date(new Date());
				LeaveKitty.setInitial_days_count(lp.getLeave_days_permit());
				LeaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
				LeaveKitty.setEmp_id(elp.getEmpId());

				leaveKittyRepository.save(LeaveKitty);
				System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short()+" "+LeaveKitty.getAccumulated_count());
				
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private void assignMLToOnRole(LeaveKitty LeaveKitty, LeaveType leaveType, LeavePattern lp,
			EmployeeDetailsForLeavePattern elp) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 20);
			Date date = calendar.getTime();
			int currentYear = Year.now().getValue();

			String year = String.valueOf(currentYear);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date dj = simpleDateFormat.parse(elp.getDateOfJoining());
			if (dj.before(date)) {
				Integer leaveApplyCount = leaveApplyRepository.getCountLeaveApply(elp.getEmpId(), lp.getLeave_id(),
						year);

				if (leaveApplyCount <= lp.getLeave_days_permit()) {
					LeaveKitty.setAccumulated_count((double) leaveApplyCount);
				} else {
					LeaveKitty.setAccumulated_count(0.0);
				}

				LeaveKitty.setAccumulated_date(new Date());
				LeaveKitty.setInitial_days_count(lp.getLeave_days_permit());
				LeaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
				LeaveKitty.setEmp_id(elp.getEmpId());

				leaveKittyRepository.save(LeaveKitty);
				System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short()+" "+LeaveKitty.getAccumulated_count());
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private void assignRHToPermanent(LeaveKitty LeaveKitty, LeaveType leaveType, LeavePattern lp,
			EmployeeDetailsForLeavePattern elp) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 20);
			Date date = calendar.getTime();
			int currentYear = Year.now().getValue();

			String year = String.valueOf(currentYear);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date dj = simpleDateFormat.parse(elp.getDateOfJoining());
			if (dj.before(date)) {
				Integer leaveApplyCount = leaveApplyRepository.getCountLeaveApply(elp.getEmpId(), lp.getLeave_id(),
						year);

				if (leaveApplyCount <= lp.getLeave_days_permit()) {
					LeaveKitty.setAccumulated_count((double) leaveApplyCount);
				} else {
					LeaveKitty.setAccumulated_count(0.0);
				}

				LeaveKitty.setAccumulated_date(new Date());
				LeaveKitty.setInitial_days_count(lp.getLeave_days_permit());
				LeaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
				LeaveKitty.setEmp_id(elp.getEmpId());
				LeaveKitty.setActive(true);

				leaveKittyRepository.save(LeaveKitty);
				System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short()+" "+LeaveKitty.getAccumulated_count());
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private void assignRHToOnRole(LeaveKitty LeaveKitty, LeaveType leaveType, LeavePattern lp,
			EmployeeDetailsForLeavePattern elp) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 20);
			Date date = calendar.getTime();
			int currentYear = Year.now().getValue();

			String year = String.valueOf(currentYear);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date dj = simpleDateFormat.parse(elp.getDateOfJoining());
			if (dj.before(date)) {
				Integer leaveApplyCount = leaveApplyRepository.getCountLeaveApply(elp.getEmpId(), lp.getLeave_id(),
						year);

				if (leaveApplyCount <= lp.getLeave_days_permit()) {
					LeaveKitty.setAccumulated_count((double) leaveApplyCount);
				} else {
					LeaveKitty.setAccumulated_count(0.0);
				}

				LeaveKitty.setAccumulated_date(new Date());
				LeaveKitty.setInitial_days_count(lp.getLeave_days_permit());
				LeaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
				LeaveKitty.setEmp_id(elp.getEmpId());

				leaveKittyRepository.save(LeaveKitty);
				System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short()+" "+LeaveKitty.getAccumulated_count());
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}


	private void assignElToPermanent(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 20);
			Date currentDate = calendar.getTime();
			int currentYear = Year.now().getValue();
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String year = String.valueOf(currentYear);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date dateOfJoining = simpleDateFormat.parse(elp.getDateOfJoining());
			Double accumulatedCount = 0.0;
			if(month == 1)
				accumulatedCount = 1.5;

			if (dateOfJoining.before(currentDate) && day >= 16) {
				/*Integer leaveApplyCount = leaveApplyRepository.getCountLeaveApply(elp.getEmpId(), lp.getLeave_id(), year);

				accumulatedCount = calculateElAccumulatedCountOfProbationary(dateOfJoining,lp);

				double maxAllowedEl = Math.min(lp.getLeave_days_permit(), 20.0);
				accumulatedCount = Math.min(accumulatedCount, maxAllowedEl);

				Double updatedCount = accumulatedCount - leaveApplyCount;

				updatedCount = Math.max(updatedCount, 0.0);

				LeaveKitty.setAccumulated_count(updatedCount);*/
				accumulatedCount = leaveKitty.getAccumulated_count() + 1.5;
			}
			if (accumulatedCount <= 20)
				leaveKitty.setAccumulated_count(accumulatedCount);
			leaveKitty.setAccumulated_date(new Date());
			leaveKitty.setInitial_days_count(lp.getLeave_days_permit());
			leaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
			leaveKitty.setEmp_id(elp.getEmpId());

			leaveKittyRepository.save(leaveKitty);
			System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short()+" "+leaveKitty.getAccumulated_count());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	
	private Double calculateElAccumulatedCountOfProbationary(Date dateOfJoining, LeavePattern lp) {
		Calendar calendar = Calendar.getInstance();
		 int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
		 int currentYear = calendar.get(Calendar.YEAR);
			
		 int currentMonth = calendar.get(Calendar.MONTH) + 1;  
	    double accumulatedCount = 0;

	    for (int month = 1; month <= currentMonth; month++) {
	    	 if (month < currentMonth || (month == currentMonth && currentDay >= 20 && currentYear==lp.getYear())) {
	             accumulatedCount += 1.5; 
	         }
	    }

	    return accumulatedCount;
	}

	
	private void assignClToORRPermanentStaff(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp,
			EmployeeDetailsForLeavePattern elp) {
		Double accumalatedCount = 0.0;
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH) + 1;
	    int day = calendar.get(Calendar.DAY_OF_MONTH);
		int currentYear = Year.now().getValue();
		String year = String.valueOf(currentYear);

		if (month != 1 && month != 7) return;

		Integer leaveApplyCount = leaveApplyRepository.getCountLeaveApply(elp.getEmpId(), lp.getLeave_id(), year);
		if (month == 1) {
			accumalatedCount = ObjectUtils.isNotEmpty(leaveApplyCount) && leaveApplyCount != 0 ? 6.0 - leaveApplyCount : 6.0;
		} else {
			accumalatedCount = ObjectUtils.isNotEmpty(leaveApplyCount) && leaveApplyCount != 0 ? 12.0 - leaveApplyCount : 12.0;
		}
		if (leaveApplyCount <= lp.getLeave_days_permit()) {
			leaveKitty.setAccumulated_count(accumalatedCount);
		} else {
			leaveKitty.setAccumulated_count(0.0);
		}

		leaveKitty.setInitial_days_count(lp.getLeave_days_permit());
		leaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
		leaveKitty.setEmp_id(elp.getEmpId());
		leaveKitty.setAccumulated_date(new Date());

		leaveKittyRepository.save(leaveKitty);
		System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short() + " " + leaveKitty.getAccumulated_count());
		
	}

	private Double calculateClAccumulatedCountOfProbationary(int month, int day) {
		
		double accumalatedCount = 0;
		
		if (day >= 20) {
			for(int m=1; m<=month; m++) {
				accumalatedCount = accumalatedCount + 1.0;
			}		
		} 
		
		return accumalatedCount;

	}

}
