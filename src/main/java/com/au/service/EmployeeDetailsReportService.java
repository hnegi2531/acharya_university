package com.au.service;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.au.repository.EmployeeDetailsReportRepository;
import com.au.repository.EmployeeDetailsRepository;

@Service
public class EmployeeDetailsReportService {
	
	@Autowired
	private EmployeeDetailsReportRepository emp_detail_report_repo;	
	
	@Autowired
	private EmployeeDetailsRepository emp_details_repo;
	
	private Integer count=0;
	
	public List<Integer> getAllSchoolId(){
		return emp_detail_report_repo.getAllSchoolId();
	}

	
	public List<Map<String, Object>> getEmployeeDetailsForReportONGender() {
		List<Integer> school_id= getAllSchoolId();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		school_id.stream().forEach(sch_id ->{
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> gneder_report_of_school = emp_detail_report_repo.getEmployeeDetailsForReportONGender(sch_id);
			final_report.put("school_name_short", gneder_report_of_school.get(0).get("school_name_short"));
			
			if(gneder_report_of_school.size()==0) {
				throw new RuntimeException("Check Availability Of Data OF Employee");
			}else {
				final_report.put("school_name_short", gneder_report_of_school.get(0).get("school_name_short"));
				Map<String, Integer> gendarCounts = new HashMap<>();
				gneder_report_of_school.stream().forEach(r -> {
					String gen = r.get("gender").toString();
	            	 BigInteger bigIntCount = (BigInteger) r.get("gender_count");
	                    Integer count = bigIntCount.intValue(); 

	                    if ("M".equals(gen)) {
	                    	gendarCounts.put("Male", gendarCounts.getOrDefault("Male", 0) + count);
	                    } else if ("F".equals(gen)) {
	                    	gendarCounts.put("Female", gendarCounts.getOrDefault("Female", 0) + count);
	                    }
	                });
				 final_report.putAll(gendarCounts);
				
				list.add(final_report);
			}
			
		});
		return list;
	}
	
	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnDesignation() throws Exception{
		List<Integer> school_id= getAllSchoolId();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		school_id.stream().forEach(sch_id ->{
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> designatiom_report_of_school = emp_detail_report_repo.getEmployeeDetailsForReportOnDesignation(sch_id);
			if(designatiom_report_of_school.size()==0) {
				throw new RuntimeException("Check Availability Of Data in School and Designation");
			}else {
				final_report.put("school_name_short", designatiom_report_of_school.get(0).get("school_name_short"));

				designatiom_report_of_school.stream().forEach(r -> {
//					count=count+1;
					final_report.put(r.get("designation_short_name").toString() ,r.get("designation_count"));
//					final_report.put("designation_short_name "+count , r.get("designation_short_name"));
//					final_report.put("designation_count "+count, r.get("designation_count"));
				});
//				count=0;
				list.add(final_report);
			}
		});
		return list;
	}
	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnDepartment() throws Exception{
		List<Integer> school_id= getAllSchoolId();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		school_id.stream().forEach(sch_id ->{
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> department_report_of_school = emp_detail_report_repo.getEmployeeDetailsForReportOnDepartment(sch_id);

			if(department_report_of_school.size()==0) {
				throw new RuntimeException("Check Availability Of Data in School and Department " + sch_id);
			}else {
				final_report.put("school_name_short", department_report_of_school.get(0).get("school_name_short"));

				department_report_of_school.stream().forEach(r -> {
//					count=count+1;
					final_report.put(r.get("dept_name_short").toString() ,r.get("department_count"));
//					final_report.put("dept_name_short "+count , r.get("dept_name_short"));
//					final_report.put("department_count "+count, r.get("department_count"));
				});
//				count=0;
				list.add(final_report);
			}
		});
		return list;
	}
	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnEmployeeType() throws Exception{
		List<Integer> school_id= getAllSchoolId();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		school_id.stream().forEach(sch_id ->{
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> department_report_of_school = emp_detail_report_repo.getEmployeeDetailsForReportOnEmployeeType(sch_id);
			
			if(department_report_of_school.size()==0) {
				throw new RuntimeException("Check Availability Of Data in School and Employee Type");
			}else {
				final_report.put("school_name_short", department_report_of_school.get(0).get("school_name_short"));
				final_report.put("org_name", department_report_of_school.get(0).get("org_name"));
				final_report.put("org_type", department_report_of_school.get(0).get("org_type"));

				department_report_of_school.stream().forEach(r -> {
//					count=count+1;
					final_report.put(r.get("emp_type_short_name").toString() ,r.get("employee_type_count"));
//					final_report.put("emp_type_short_name "+count , r.get("emp_type_short_name"));
//					final_report.put("employee_type_count "+count, r.get("employee_type_count"));
				});
//				count=0;
				list.add(final_report);
			}
		});
		return list;
	}
	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnJobType() throws Exception{
		List<Integer> school_id= getAllSchoolId();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		school_id.stream().forEach(sch_id ->{
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> job_type_report_of_school = emp_detail_report_repo.getEmployeeDetailsForReportOnJobType(sch_id);

			if(job_type_report_of_school.size()==0) {
				throw new RuntimeException("Check Availability Of Data in School and Job Type");
			}else {
				final_report.put("school_name_short", job_type_report_of_school.get(0).get("school_name_short"));

				job_type_report_of_school.stream().forEach(r -> {
//					count=count+1;
					final_report.put(r.get("job_short_name").toString() ,r.get("job_type_count"));
//					final_report.put("job_short_name "+count , r.get("job_short_name"));
//					final_report.put("job_type_count "+count, r.get("job_type_count"));
				});
//				count=0;
				list.add(final_report);
			}
		});
		return list;
	}
	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnShift() throws Exception{
		List<Integer> school_id= getAllSchoolId();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		school_id.stream().forEach(sch_id ->{
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> shift_report_of_school = emp_detail_report_repo.getEmployeeDetailsForReportOnShift(sch_id);
			
			if(shift_report_of_school.size()==0) {
				throw new RuntimeException("Check Availability Of Data in School and Shift");
			}else {
				final_report.put("school_name_short", shift_report_of_school.get(0).get("school_name_short"));

				shift_report_of_school.stream().forEach(r -> {
//					count=count+1;
					final_report.put(r.get("shift_name").toString() ,r.get("shift_count"));
//					final_report.put("shift_name "+count , r.get("shift_name"));
//					final_report.put("shift_count "+count, r.get("shift_count"));
				});
//				count=0;
				list.add(final_report);
			}
		});
		return list;
	}
	
	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnMaritalStatus() throws Exception {
	    List<Integer> school_id = getAllSchoolId();
	    List<Map<String, Object>> list = new ArrayList<>();

	    for (Integer sch_id : school_id) {
	        HashMap<String, Object> final_report = new HashMap<>();
	        List<Map<String, Object>> marital_status_report_of_employee = emp_detail_report_repo.getEmployeeDetailsForReportOnMaritalStatus(sch_id);

	        if (marital_status_report_of_employee.isEmpty()) {
	            throw new RuntimeException("Check Availability Of Data OF Employee");
	        } else {
	            final_report.put("school_name_short", marital_status_report_of_employee.get(0).get("school_name_short"));

	            // Initialize a map to hold the marital status counts
	            Map<String, Integer> maritalCounts = new HashMap<>();

	            // Aggregate counts for each marital status
	            marital_status_report_of_employee.forEach(r -> {
	            	
	            	String marital = r.get("martial_status").toString();
	            	 BigInteger bigIntCount = (BigInteger) r.get("martial_status_count");
	                    Integer count = bigIntCount.intValue(); 

	                    if ("M".equals(marital)) {
	                        maritalCounts.put("Married", maritalCounts.getOrDefault("Married", 0) + count);
	                    } else if ("U".equals(marital)) {
	                        maritalCounts.put("Unmarried", maritalCounts.getOrDefault("Unmarried", 0) + count);
	                    }
	                });

	            // Add the aggregated data to the final report
	            final_report.putAll(maritalCounts);

	            // Add the final report for this school to the list
	            list.add(final_report);
	        }
	    }
	    return list;
	}

	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnExperienceInYear() throws Exception {
		List<Integer> school_id = getAllSchoolId();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		school_id.stream().forEach(sch_id -> {
			HashMap<String, Object> final_report = new HashMap<>();
			List<Integer> experience_group = Arrays.asList(0, 5, 10, 15, 20, 25, 30, 35);
			experience_group.stream().forEach(eg -> {
				Double last_group_number = eg + 4.9;
				int for_key = eg + 5;
				List<Map<String, Object>> exp_in_years_report_of_employee = emp_detail_report_repo
						.getEmployeeDetailsForReportOnExperienceInYear(sch_id, eg, last_group_number);

				if (exp_in_years_report_of_employee.size() != 0) {

					final_report.put("school_name_short",
							exp_in_years_report_of_employee.get(0).get("school_name_short"));

					exp_in_years_report_of_employee.stream().forEach(r -> {

						final_report.put(eg + " - " + for_key,
								(Integer) final_report.getOrDefault((eg + " - " + for_key), 0)
										+ ((BigInteger) r.get("exp_in_years_count")).intValue());

					});

				}
			});
			list.add(final_report);
		});
		return list;
	}
	
	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnExperienceInMonth() throws Exception{
		List<Integer> school_id= getAllSchoolId();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		school_id.stream().forEach(sch_id ->{
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> exp_in_months_report_of_employee = emp_detail_report_repo.getEmployeeDetailsForReportOnExperienceInMonth(sch_id);
			
			if(exp_in_months_report_of_employee.size()==0) {
				throw new RuntimeException("Check Availability Of Data OF Employee");
			}else {
				final_report.put("school_name_short", exp_in_months_report_of_employee.get(0).get("school_name_short"));

				exp_in_months_report_of_employee.stream().forEach(r -> {
//					count=count+1;
					final_report.put(r.get("exp_in_months").toString() ,r.get("exp_in_months_count"));
//					final_report.put("exp_in_months "+count , r.get("exp_in_months"));
//					final_report.put("exp_in_months_count "+count, r.get("exp_in_months_count"));
				});
//				count=0;
				list.add(final_report);
			}
		});
		return list;
	}
	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnSchools() throws Exception{
		
			List<Map<String, Object>> school_report_of_employee = emp_detail_report_repo.getEmployeeDetailsForReportOnSchools();
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
			if(school_report_of_employee.size()==0) {
				throw new RuntimeException("Check Availability Of Data OF Employee");
			}else {
				school_report_of_employee.stream().forEach(r -> {
				final_report.put(r.get("school_name_short").toString() ,r.get("school_count"));
				});
				list.add(final_report);
			}
			return list;
	}
	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnJoiningDate() throws Exception{
		List<Integer> school_id= getAllSchoolId();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		school_id.stream().forEach(sch_id ->{
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> joining_date_report_of_employee = emp_detail_report_repo.getEmployeeDetailsForReportOnJoiningDate(sch_id);
			
			if(joining_date_report_of_employee.size()==0) {
				throw new RuntimeException("Check Availability Of Data OF Employee");
			}else {
				final_report.put("school_name_short", joining_date_report_of_employee.get(0).get("school_name_short"));

				joining_date_report_of_employee.stream().forEach(r -> {
//					count=count+1;
					final_report.put(r.get("date_of_joining").toString() ,r.get("date_of_joining_count"));
//					final_report.put("date_of_joining "+count , r.get("date_of_joining"));
//					final_report.put("date_of_joining_count "+count, r.get("date_of_joining_count"));
				});
//				count=0;
				list.add(final_report);
			}
		});
		return list;
	}
	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnDateOfBirth() throws Exception {
	    List<Integer> schoolIds = getAllSchoolId();
	    List<Map<String, Object>> list = new ArrayList<>();

	    schoolIds.forEach(schoolId -> {
	        Map<String, Object> finalReport = new HashMap<>();
	        List<Integer> ageGroups = Arrays.asList(20, 30, 40, 50, 60, 70);

	        ageGroups.forEach(ag -> {
	            Integer firstAge = ag;
	            Integer secondAge;
	            String ageRangeKey;

	            if (ag == 70) {
	                secondAge = Integer.MAX_VALUE; // Special case for 70+
	                ageRangeKey = "70+";
	            } else {
	                secondAge = ag + 9;  // Inclusive of lower bound, exclusive of upper bound
	                ageRangeKey = firstAge + " - " + (secondAge + 1); // Adjust the range to display as "20-29"
	            }

	            // Fetch the data from the repository
	            List<Map<String, Object>> dobReport = emp_detail_report_repo.getEmployeeDetailsForReportOnDateOfBirth(schoolId, firstAge, secondAge);

	            if (!dobReport.isEmpty()) {
	                finalReport.put("school_name_short", dobReport.get(0).get("school_name_short"));
	            }

	            // Aggregate the counts
	            dobReport.forEach(r -> {
	                int count = ((BigInteger) r.get("age_count")).intValue();
	                finalReport.put(ageRangeKey, (Integer) finalReport.getOrDefault(ageRangeKey, 0) + count);
	            });
	        });

	        if (!finalReport.isEmpty()) {
	            list.add(finalReport);
	        }
	    });

	    return list;
	}



	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnMonthWiseOfJoiningYear(Integer year) throws Exception{
		List<Integer> school_id= getAllSchoolId();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		school_id.stream().forEach(sch_id ->{
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> joining_month_report_of_employee = emp_detail_report_repo.getEmployeeDetailsForReportOnMonthWiseOfJoiningYear(sch_id,year);

			if(joining_month_report_of_employee.size() !=0) {
				final_report.put("school_name_short", joining_month_report_of_employee.get(0).get("school_name_short"));
			}

			joining_month_report_of_employee.stream().forEach(r -> {
				final_report.put(r.get("month").toString() ,r.get("joining_in_month_count"));
			});
			if(final_report.size() != 0) {
				list.add(final_report);
			}
		});
		return list;
	}
	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnMonthWiseOfJoiningYearOnSchool(Integer year,Integer school_id) throws Exception{
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> joining_month_report_of_employee = emp_detail_report_repo.getEmployeeDetailsForReportOnMonthWiseOfJoiningYearOnSchool(year,school_id);
			
			if(joining_month_report_of_employee.size()==0) {
				throw new RuntimeException("Check Availability Of Data OF Employee");
			}else {
				final_report.put("school_name_short", joining_month_report_of_employee.get(0).get("school_name_short"));

				joining_month_report_of_employee.stream().forEach(r -> {
					final_report.put(r.get("month").toString() ,r.get("joining_in_month_count"));
				});
				list.add(final_report);
			}
		return list;
	}
	
	public List<Map<String, Object>> getEmployeeRelievingReportDataOnMonthWise(Integer year) throws Exception{
		List<Integer> school_id= getAllSchoolId();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		school_id.stream().forEach(sch_id ->{
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> joining_month_report_of_employee = emp_detail_report_repo.getEmployeeRelievingReportDataOnMonthWise(sch_id,year);
			
			if(joining_month_report_of_employee.size() != 0) {
				final_report.put("school_name_short", joining_month_report_of_employee.get(0).get("school_name_short"));
		

				joining_month_report_of_employee.stream().filter(r -> r.size() != 0).forEach(r -> {
					final_report.put(r.get("month").toString() ,r.get("relieving_in_month_count"));
					
				});
				list.add(final_report);
			}
		});
		return list;
	}
	
	public List<Map<String, Object>> getEmployeeDetailsForReportOnMonthWiseOfJoiningYearInactiveData(Integer year) throws Exception{
		List<Integer> school_id= getAllSchoolId();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		school_id.stream().forEach(sch_id ->{
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> joining_month_report_of_employee = emp_detail_report_repo.getEmployeeDetailsForReportOnMonthWiseOfJoiningYearInactiveData(sch_id,year);

			if(joining_month_report_of_employee.size() !=0) {
				final_report.put("school_name_short", joining_month_report_of_employee.get(0).get("school_name_short"));
			}

			joining_month_report_of_employee.stream().forEach(r -> {
				final_report.put(r.get("month").toString() ,r.get("joining_in_month_count"));
			});
			if(final_report.size() != 0) {
				list.add(final_report);
			}
		});
		return list;
	}
	
	public List<Map<String, Object>> getEmployeeRelievingReportDataOnMonthWiseInactiveData(Integer year) throws Exception{
		List<Integer> school_id= getAllSchoolId();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		school_id.stream().forEach(sch_id ->{
			HashMap<String, Object> final_report=new HashMap<>();
			List<Map<String, Object>> joining_month_report_of_employee = emp_detail_report_repo.getEmployeeRelievingReportDataOnMonthWiseInactiveData(sch_id,year);
			
			if(joining_month_report_of_employee.size() != 0) {
				final_report.put("school_name_short", joining_month_report_of_employee.get(0).get("school_name_short"));
		

				joining_month_report_of_employee.stream().filter(r -> r.size() != 0).forEach(r -> {
					final_report.put(r.get("month").toString() ,r.get("relieving_in_month_count"));
					
				});
				list.add(final_report);
			}
		});
		return list;
	}
	
	public HashMap<String,Object> getEmployeeDetailsForReportOnMonthWiseOfJoiningDateAndRelievingData(Integer year) throws Exception{
		List<Integer> school_id= getAllSchoolId();
		HashMap<String,Object> final_report=new HashMap<>();
		List<Map<String, Object>> list_of_joining_date=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list_of_relieving_date=new ArrayList<Map<String, Object>>();
		school_id.stream().parallel().forEach(sch_id ->{
			HashMap<String, Object> final_report_of_joining_data=new HashMap<>();
			List<Map<String, Object>> joining_month_report_of_employee = emp_detail_report_repo.getEmployeeDetailsForReportOnMonthWiseOfJoiningYear(sch_id,year);

			if(joining_month_report_of_employee.size() !=0) {
				final_report_of_joining_data.put("school_name_short", joining_month_report_of_employee.get(0).get("school_name_short"));
			}

			joining_month_report_of_employee.stream().parallel().forEach(r -> {
				final_report_of_joining_data.put(r.get("month").toString() ,r.get("joining_in_month_count"));
			});
			if(final_report_of_joining_data.size() != 0) {
				list_of_joining_date.add(final_report_of_joining_data);
			}
		});
		
		
		school_id.stream().parallel().forEach(sch_id ->{
			HashMap<String, Object> final_report_of_relieving_date=new HashMap<>();
			List<Map<String, Object>> joining_month_report_of_employee = emp_detail_report_repo.getEmployeeRelievingReportDataOnMonthWise(sch_id,year);
			
			if(joining_month_report_of_employee.size() != 0) {
				final_report_of_relieving_date.put("school_name_short", joining_month_report_of_employee.get(0).get("school_name_short"));
		

				joining_month_report_of_employee.stream().parallel().filter(r -> r.size() != 0).forEach(r -> {
					final_report_of_relieving_date.put(r.get("month").toString() ,r.get("relieving_in_month_count"));
					
				});
				list_of_relieving_date.add(final_report_of_relieving_date);
			}
		});
		final_report.put("joining_date_data", list_of_joining_date);
		final_report.put("relieving_date_data", list_of_relieving_date);
		return final_report;
		
	}

}
