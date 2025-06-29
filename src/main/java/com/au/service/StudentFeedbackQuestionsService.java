package com.au.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.ClassFeedbackAnswersWebDTO;
import com.au.dto.EligibleStudentFeedbackRequestDTO;
import com.au.dto.EmployeeFeedbackReport;
import com.au.dto.EmployeeFeedbackReportList;
import com.au.dto.FeedbackWindowDTO;
import com.au.dto.FeedbackWindowNewDto;
import com.au.dto.FreezeStudentAttendenceDTO;
import com.au.dto.GetStudentForFeedback;
import com.au.dto.StudentFeedbackAnswersDTO;
import com.au.dto.StudentFeedbackQuestionsDTO;
import com.au.model.ClassFeedbackAnswersWeb;
import com.au.model.FeedbackQuestions;
import com.au.model.FeedbackWindow;
import com.au.model.FreezeStudentAttendence;
import com.au.model.ReportingStudents;
import com.au.model.StudentAttendance;
import com.au.model.StudentFeedbackAnswers;
import com.au.model.StudentFeedbackQuestions;
import com.au.repository.ClassFeedbackAnswersRepositoryWeb;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.FeedbackAllowForStudentRepository;
import com.au.repository.FeedbackWindowRepository;
import com.au.repository.FreezeStudentAttendenceRepository;
import com.au.repository.ReportingStudentsRepository;
import com.au.repository.SectionAssignmentRepository;
import com.au.repository.StudentAttendanceRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.StudentFeedbackAnswersRepository;
import com.au.repository.StudentFeedbackQuestionsRepository;
import com.au.repository.TimeTableRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;


@Service
public class StudentFeedbackQuestionsService {

	@Autowired
	private StudentFeedbackQuestionsRepository studentFeedbackQuestionsRepository;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private StudentFeedbackAnswersRepository studentFeedbackAnswersRepository;
	
	@Autowired
	private FeedbackWindowRepository feedbackWindowRepository;
	
	@Autowired
	private FreezeStudentAttendenceRepository freezeStudentAttendenceRepository;
	
	@Autowired
	private StudentAttendanceRepository studentAttendanceRepository;
	
	@Autowired
	private ReportingStudentsRepository reportingStudentsRepository;
	
	@Autowired
	private TimeTableRepository timeTableRepository;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private ClassFeedbackAnswersRepositoryWeb classFeedbackAnswersRepository;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	@Autowired
	private FeedbackAllowForStudentRepository feedbackAllowForStudentRepository;
	
	@Autowired
	private SectionAssignmentRepository sectionAssignmentRepository;
	
	public ResponseEntity<Object> createFeedbackQuestionsForStudents(
			StudentFeedbackQuestionsDTO studentFeedbackQuestionsDTO) {
		try{
			StudentFeedbackQuestions studentFeedbackQuestions=new StudentFeedbackQuestions();
			studentFeedbackQuestions.setActive(true);
			studentFeedbackQuestions.setCourseId(studentFeedbackQuestionsDTO.getCourseId());
			studentFeedbackQuestions.setInstituteId(studentFeedbackQuestionsDTO.getInstituteId());
			studentFeedbackQuestions.setQuestions(studentFeedbackQuestionsDTO.getQuestions());
			studentFeedbackQuestionsRepository.save(studentFeedbackQuestions);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> getFeedbackQuestionsForStudentsList(Integer instituteId, Integer courseId) {
		try{
			List<FeedbackQuestions> studentFeedbackQuestions=studentFeedbackQuestionsRepository.findByInstituteIdAndCourseIdAndActive(instituteId,courseId,Boolean.TRUE);	
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", studentFeedbackQuestions);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> deactivateFeedbackQuestionsForStudents(Integer instituteId, Integer courseId, Long questionId) {
		try{
			StudentFeedbackQuestions studentFeedbackQuestions=studentFeedbackQuestionsRepository.findByInstituteIdAndCourseIdAndQuestionIdAndActive(instituteId,courseId,questionId,Boolean.TRUE);	
			studentFeedbackQuestions.setActive(false);
			studentFeedbackQuestionsRepository.save(studentFeedbackQuestions);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> getFeedbackQuestionsForStudentsListByQuestionId(Integer instituteId, Integer courseId,
			Long questionId) {
		try{
			StudentFeedbackQuestions studentFeedbackQuestions=studentFeedbackQuestionsRepository.findByInstituteIdAndCourseIdAndQuestionIdAndActive(instituteId,courseId,questionId,Boolean.TRUE);	
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", studentFeedbackQuestions);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> updateFeedbackQuestionsForStudents(
			StudentFeedbackQuestionsDTO studentFeedbackQuestions) {
		try{
			StudentFeedbackQuestions studentFeedbackQuestion=studentFeedbackQuestionsRepository.findByInstituteIdAndCourseIdAndQuestionIdAndActive(studentFeedbackQuestions.getInstituteId(),studentFeedbackQuestions.getCourseId(),studentFeedbackQuestions.getQuestionId(),Boolean.TRUE);	
			studentFeedbackQuestion.setQuestions(studentFeedbackQuestions.getQuestions());
			studentFeedbackQuestionsRepository.save(studentFeedbackQuestion);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> getStudentDetailsForFeedback(Integer studentId, Integer courseId) {
		try {
			Integer program_specialization_id = studentDetailsRepository.getProgramSpecializationId(studentId);
			if (classFeedbackAnswersRepository.getFeedbackAnswerCount(studentId, courseId) >= 1) {
				throw new RuntimeException("Feedback already given for this course !!!");
			}

			GetStudentForFeedback getStudentForFeedback = studentDetailsRepository.getStudentForFeedback(studentId,
					courseId);
			
			if (getStudentForFeedback == null) {
	            throw new RuntimeException("No data present for this combination !!!");
	        }

//			Map<String, Object> percentageDetails = freezeStudentAttendenceRepository.getAllPercentage(studentId,
//					courseId);
//
//			Integer actualPercentage = Integer.parseInt(percentageDetails.get("actualPercentage").toString());
//
//			Integer assignPercentage = (Integer) percentageDetails.get("assignPercentage");
//
//			if (assignPercentage > actualPercentage) {
//				throw new RuntimeException("You don't have enough attendance for giving feedback !!!");
//			}
			
	        Integer feedbackWindowId = feedbackWindowRepository.getFeedbackWindowId(
	                getStudentForFeedback.getProgram_specialization_id(),
	                getStudentForFeedback.getSchoolId(),
	                getStudentForFeedback.getAcYearId().toString());

	        Integer feedback_allowfor_student_id = feedbackAllowForStudentRepository.getCountFeedbackAllowForStudent(
	                feedbackWindowId, studentId, courseId);
	        
	        if (feedback_allowfor_student_id == null || feedback_allowfor_student_id == 0) {
	            Map<String, Object> percentageDetails = freezeStudentAttendenceRepository.getAllPercentage(studentId, courseId);

	            Integer actualPercentage = percentageDetails.get("actualPercentage") != null
	                    ? Integer.parseInt(percentageDetails.get("actualPercentage").toString())
	                    : 0;

	            Integer assignPercentage = percentageDetails.get("assignPercentage") != null
	                    ? (Integer) percentageDetails.get("assignPercentage")
	                    : 0;

	            if (assignPercentage > actualPercentage) {
	                throw new RuntimeException("You don't have enough attendance for giving feedback !!!");
	            }
	        }

			List<FeedbackQuestions> feedbackQuestions = studentFeedbackQuestionsRepository
					.getFeedbackQuestions(getStudentForFeedback.getSchoolId());
			getStudentForFeedback.setStudentFeedbackQuestions(feedbackQuestions);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", getStudentForFeedback);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}

	public ResponseEntity<Object> studentFeedbackAnswers(List<StudentFeedbackAnswersDTO>  studentFeedbackAnswersDTO) {
		try{
			List<StudentFeedbackAnswers> studentFeedbackAnswersList=new ArrayList<>();
			studentFeedbackAnswersDTO.stream().forEach(sfa->{
				StudentFeedbackAnswers studentFeedbackAnswers=new StudentFeedbackAnswers();
				studentFeedbackAnswers.setCourseId(sfa.getCourseId());
				studentFeedbackAnswers.setInstituteId(sfa.getInstituteId());
				studentFeedbackAnswers.setQuestionId(sfa.getQuestionId());
				studentFeedbackAnswers.setStudentId(sfa.getStudentId());
				studentFeedbackAnswers.setRatings(sfa.getRatings());
				studentFeedbackAnswersList.add(studentFeedbackAnswers);
			});
			
			studentFeedbackAnswersRepository.saveAll(studentFeedbackAnswersList);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}

//	public ResponseEntity<Object> createFeedbackWindow(FeedbackWindowDTO feedbackWindowDTO) {
//		try {
//			List<FeedbackWindow> feedbackWindowList = new ArrayList<>();
//
//
//				feedbackWindowDTO.getCourseAndBranch().stream().forEach(courseBranch -> {
//					FeedbackWindow feedbackWindow = new FeedbackWindow();
//
//					feedbackWindow.setAcademicYear(feedbackWindowDTO.getAcademicYear());
//					feedbackWindow.setBranch(feedbackWindowDTO.getBranch());
//					feedbackWindow.setCourseAndBranch(courseBranch);
//					feedbackWindow.setFromDate(feedbackWindowDTO.getFromDate());
//					feedbackWindow.setToDate(feedbackWindowDTO.getToDate());
//					feedbackWindow.setSemester(feedbackWindowDTO.getSemester());
//					feedbackWindow.setInstituteId(feedbackWindowDTO.getInstituteId());
//					feedbackWindow.setActive(Boolean.TRUE);
//
//					feedbackWindowList.add(feedbackWindow);
//				});
//
//
//			feedbackWindowRepository.saveAll(feedbackWindowList);
//
//			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
//
//		} catch (Exception e) {
//
//			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
//					e.getMessage());
//		}
//	}
	
	public ResponseEntity<Object> createFeedbackWindow(FeedbackWindowDTO feedbackWindowDTO) {
	    try {
	        List<FeedbackWindow> feedbackWindowList = new ArrayList<>();

	    

	            feedbackWindowDTO.getProgram_specialization_id().forEach(pId -> {

	                Optional<FeedbackWindow> existingFeedbackWindow = feedbackWindowRepository
	                    .findByAcademicYearAndCourseAndSemesterAndInstituteIdAndActiveTrue(
	                        feedbackWindowDTO.getAcademicYear(),
	                        pId,
	                        feedbackWindowDTO.getSemester(),
	                        feedbackWindowDTO.getInstituteId()
	                    ).stream().filter(feedbackWindow -> 
	                   
	                        (feedbackWindow.getFromDate().before(feedbackWindowDTO.getToDate()) 
	                        && feedbackWindow.getToDate().after(feedbackWindowDTO.getFromDate())))
	                    .findFirst();

	                if (existingFeedbackWindow.isPresent()) {
	                
	                    throw new IllegalArgumentException("A feedback window already exists for the same academic year, course, semester, and institute between the specified dates.");
	                }

	              
	                FeedbackWindow feedbackWindow = new FeedbackWindow();
	                feedbackWindow.setAcademicYear(feedbackWindowDTO.getAcademicYear());
	                feedbackWindow.setFromDate(feedbackWindowDTO.getFromDate());
	                feedbackWindow.setToDate(feedbackWindowDTO.getToDate());
	                feedbackWindow.setSemester(feedbackWindowDTO.getSemester());
	                feedbackWindow.setInstituteId(feedbackWindowDTO.getInstituteId());
	                feedbackWindow.setProgram_specialization_id(pId); // Set courseId from the list
	                feedbackWindow.setActive(Boolean.TRUE);

	                feedbackWindowList.add(feedbackWindow);
	            });

	     
	        feedbackWindowRepository.saveAll(feedbackWindowList);

	     
	        return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

	    } catch (IllegalArgumentException e) {
	      
	        return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getMessage());
	    } catch (Exception e) {
	       
	        return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getMessage());
	    }
	}


	public ResponseEntity<Object> feedbackWindowList() {
		try{
			List<Map<String, Object>> feedbackWindows=feedbackWindowRepository.getAllActiveFeedbackWindow();			
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", feedbackWindows);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> feedbackWindowById(Long feedbackWindowId) {
		try{
			FeedbackWindow feedbackWindows=feedbackWindowRepository.findByFeedbackWindowId(feedbackWindowId);			
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", feedbackWindows);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}

	public ResponseEntity<Object> updateFeedbackWindow(FeedbackWindowNewDto feedbackWindowDTO) {
		try {

			FeedbackWindow feedbackWindow = feedbackWindowRepository
					.findByFeedbackWindowId(feedbackWindowDTO.getFeedbackWindowId());
			
			

			if (feedbackWindow == null) {
				return ResponseHandler.generateResponse(false, HttpStatus.NOT_FOUND, "FeedbackWindow not found", null);
			}
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!! " +feedbackWindow.getFeedbackWindowId());
			
			feedbackWindow.setAcademicYear(feedbackWindowDTO.getAcademicYear());
			feedbackWindow.setBranch(feedbackWindowDTO.getBranch());
			feedbackWindow.setCourseAndBranch(feedbackWindowDTO.getCourseAndBranch());
			feedbackWindow.setFromDate(feedbackWindowDTO.getFromDate());
			feedbackWindow.setToDate(feedbackWindowDTO.getToDate());
			feedbackWindow.setSemester(feedbackWindowDTO.getSemester());
			feedbackWindow.setInstituteId(feedbackWindowDTO.getInstituteId());
			feedbackWindow.setProgram_specialization_id(feedbackWindowDTO.getProgram_specialization_id());
			feedbackWindow.setActive(feedbackWindowDTO.getActive());

			feedbackWindowRepository.save(feedbackWindow);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}

	public ResponseEntity<Object> deactivateFeedbackWindow(Long feedbackWindowId) {
		try{
			FeedbackWindow feedbackWindow=feedbackWindowRepository.findByFeedbackWindowId(feedbackWindowId);
			feedbackWindow.setActive(Boolean.FALSE);
			feedbackWindowRepository.save(feedbackWindow);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> freezeStudentAttendence(FreezeStudentAttendenceDTO freezeStudentAttendence) {
		try {
			List<FreezeStudentAttendence> freezeStudentAttendences = new ArrayList<>();

			for (Integer instituteId : freezeStudentAttendence.getInstituteIds()) {

				FreezeStudentAttendence existingRecord = freezeStudentAttendenceRepository
						.findByInstituteIdAndAcademicYear(instituteId, freezeStudentAttendence.getAcademicYear());

				if (existingRecord != null) {
					return ResponseHandler.generateResponse(false, HttpStatus.BAD_REQUEST,
							"A percentage is already assigned for the combination of instituteId and academicYear !!!",
							null);
				}

				FreezeStudentAttendence freezeAttendence = new FreezeStudentAttendence();
				freezeAttendence.setAcademicYear(freezeStudentAttendence.getAcademicYear());
				freezeAttendence.setInstituteId(instituteId);
				freezeAttendence.setPercentage(freezeStudentAttendence.getPercentage());
				freezeAttendence.setActive(Boolean.TRUE); // Set the active flag to true
				freezeStudentAttendences.add(freezeAttendence);
			}

			// Save all the FreezeStudentAttendence records to the repository
			freezeStudentAttendenceRepository.saveAll(freezeStudentAttendences);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			// Handle any exceptions that may occur and return an internal server error
			// response
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> freezeStudentAttendenceList() {
		try{
			List<FreezeStudentAttendenceDTO> freezeStudentAttendences=freezeStudentAttendenceRepository.getAllFreezeStudentList();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", freezeStudentAttendences);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> freezeStudentAttendenceById(Long freezeId) {
		try{
			FreezeStudentAttendenceDTO freezeStudentAttendences=freezeStudentAttendenceRepository.getfreezeStudentAttendenceById(freezeId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", freezeStudentAttendences);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> updatefreezeStudentAttendence(FreezeStudentAttendenceDTO freezeStudentAttendence) {
		try{
			FreezeStudentAttendence freezeStudentAttendences=freezeStudentAttendenceRepository.findByFreezeStudentAttendenceId(freezeStudentAttendence.getFreezeId());
			freezeStudentAttendences.setAcademicYear(freezeStudentAttendence.getAcademicYear());
			Integer instituteId= freezeStudentAttendence.getInstituteIds().stream().findFirst().get();
			freezeStudentAttendences.setInstituteId(instituteId);
			freezeStudentAttendences.setActive(Boolean.TRUE);
			freezeStudentAttendences.setPercentage(freezeStudentAttendence.getPercentage());
			freezeStudentAttendenceRepository.save(freezeStudentAttendences);	
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> deactivefreezeStudentAttendence(Long freezeId) {
		try{
			FreezeStudentAttendence freezeStudentAttendences=freezeStudentAttendenceRepository.findByFreezeStudentAttendenceId(freezeId);
			freezeStudentAttendences.setActive(Boolean.FALSE);;
			freezeStudentAttendenceRepository.save(freezeStudentAttendences);	
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> getStudentHaveLessAttendence(
			EligibleStudentFeedbackRequestDTO eligibleStudentFeedbackRequestDTO) {
		try {

			List<Map<String, Object>> eligibleStudentForFeedbackDTOs = studentAttendanceRepository
					.getStudentHaveLessAttendence(eligibleStudentFeedbackRequestDTO.getYear(),
							eligibleStudentFeedbackRequestDTO.getSem(),
							eligibleStudentFeedbackRequestDTO.getProgram_specialization_id(),
							eligibleStudentFeedbackRequestDTO.getCourseId());
			System.out.println("11111111111111111111111111111111111111111111111111111111111 " + eligibleStudentForFeedbackDTOs);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", eligibleStudentForFeedbackDTOs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> getEmployeeList(Integer studentId) {
		try {
			ReportingStudents reportingStudents = reportingStudentsRepository
					.getDetailsOfReportingStudentsByStudentId(studentId);
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " +reportingStudents.getCurrent_sem());
			
			String currentYearOrSem = timeTableRepository.getCurrentYearOrSem(studentId);
			System.out.println("############################### " +currentYearOrSem);
			List<Map<String, Object>> employeeList = null;
			if (StringUtils.equalsAnyIgnoreCase(currentYearOrSem, "Yearly")) {
				employeeList = studentAttendanceRepository.getEmployeeListByCuurentYear(studentId,
						reportingStudents.getCurrent_year());
			}
			if (StringUtils.equalsAnyIgnoreCase(currentYearOrSem, "Semester")) {
				employeeList = studentAttendanceRepository.getEmployeeListByCuurentSem(studentId,
						reportingStudents.getCurrent_sem());
				System.out.println("#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + employeeList);
			}
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", employeeList);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}

	public ResponseEntity<Object> getCourseList(Integer empId, Integer studentId) {
		try {
			List<Map<String, Object>> courseList = studentDetailsRepository.getCourseListFromTimeTableForStudent(empId,
					studentId);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", courseList);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}

	}

	public ResponseEntity<Object> employeeFeedbackReport(Integer empId) {
		try {
			EmployeeFeedbackReport employeeFeedbackReport = empDetail_repo
					.getEmployeeDetailsForFeedbackReport(empId);
			List<EmployeeFeedbackReportList> employeeFeedbackReportLists = classFeedbackAnswersRepository
					.getEmployeeForFeedbackReport(empId);
			employeeFeedbackReportLists.stream().forEach(t -> {
				Long studentCount = studentDetailsRepository.getStudentCountBySectionId(t.getSectionId());
				Float percentage = (float) (t.getRatings() / (studentCount * 100));
				t.setFeedbackPercentage(percentage);
				t.setStudentCount(studentCount);
			});
			employeeFeedbackReport.setFeedbackreport(employeeFeedbackReportLists);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", employeeFeedbackReport);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}

	public ResponseEntity<Object> manualAttendenceUpdateForStudent(
			List<EligibleStudentFeedbackRequestDTO> eligibleStudentFeedbackRequestDTO) {
		try {

			eligibleStudentFeedbackRequestDTO.stream().filter(t -> t.getIscheck().equals(Boolean.TRUE)).forEach(s -> {
				studentAttendanceRepository.updateStudentAttendenceForEligibleFeedback(s.getStudentAttendenceId());
			});

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}
	
	
	
	public ResponseEntity<Object> saveClassFeedbackAnswers(@Valid ClassFeedbackAnswersWebDTO cfa) {
		
		if (classFeedbackAnswersRepository.getFeedbackAnswerCount(cfa.getStudent_id(), cfa.getCourse_id()) >= 1) {
			throw new RuntimeException("Feedback already given for this course !!!");
		}
		
		try {
			List<ClassFeedbackAnswersWeb> feedback_list = new ArrayList<ClassFeedbackAnswersWeb>();
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = simpleDateFormat.format(date);
			System.out.println(formattedDate); 

			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " +formattedDate );
			Map<String, Object> institute = studentDetailsRepository.getStudentInstitute(cfa.getStudent_id());
			//String academicYear = String.valueOf(cfa.getAcYearId());
			String semester = String.valueOf(cfa.getSem());
			String schoolName = (String) institute.get("schoolName");
			Integer schoolId = (Integer) institute.get("schoolId");
			Integer acYearId = cfa.getAcYearId();
			String academicYear = String.valueOf(acYearId);
			FeedbackWindow isWindowExists = feedbackWindowRepository.checkSessionExistsOrNotForInstitute(schoolId,
					 semester, cfa.getProgram_specialization_id(), formattedDate,academicYear);
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " +isWindowExists.getInstitute());
			Integer sectionId = sectionAssignmentRepository.getSectionId(cfa.getStudent_id());
			if (ObjectUtils.isEmpty(isWindowExists)) {
				return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,
						"Window not already exists for this " + date, null);
			}

			if (classFeedbackAnswersRepository.getCountOfFeedback(cfa.getStudent_id(), cfa.getProgram_specialization_id(),
					cfa.getUser_id()) >= 1) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR,
						"Feedback Already given", null);
			} else {

				cfa.getAnswers().stream().forEach(rate -> {
					ClassFeedbackAnswersWeb feedback = new ClassFeedbackAnswersWeb();
					feedback.setActive(cfa.getActive());
					feedback.setClass_feedback_questions_id(rate.getQuestionId());
					feedback.setUser_id(cfa.getUser_id());
					feedback.setRatings(rate.getRating());
					feedback.setProgram_specialization_id(cfa.getProgram_specialization_id());
					feedback.setCreated_by(cfa.getCreated_by());
					feedback.setCreated_username(cfa.getCreated_username());
					feedback.setRemarks(cfa.getRemarks());
					feedback.setStudent_id(cfa.getStudent_id());
					feedback.setSectionId(sectionId);
					feedback.setAcYearId(cfa.getAcYearId());
					feedback.setYear(cfa.getYear());
					feedback.setSem(cfa.getSem());
					feedback.setFeedback_window_id(cfa.getFeedback_window_id());
					feedback.setWindow_count(cfa.getWindow_count());
					feedback.setCourse_id(cfa.getCourse_id());
					classFeedbackAnswersRepository.save(feedback);
					feedback_list.add(feedback);

				});

				return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

			}
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}
	
	
	public ResponseEntity<Object> activateFeedbackWindow(Long feedbackWindowId) {
		try {
			FeedbackWindow feedbackWindow = feedbackWindowRepository.findByFeedbackWindowId(feedbackWindowId);
			feedbackWindow.setActive(Boolean.TRUE);
			feedbackWindowRepository.save(feedbackWindow);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}

	public ResponseEntity<Object> activefreezeStudentAttendence(Long freezeId) {
		try {
			FreezeStudentAttendence freezeStudentAttendences = freezeStudentAttendenceRepository
					.findByFreezeStudentAttendenceId(freezeId);
			freezeStudentAttendences.setActive(Boolean.TRUE);
			freezeStudentAttendenceRepository.save(freezeStudentAttendences);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public List<Map<String, Object>> feedbackWindowListForDropDown(String academicYear, Integer program_specialization_id, String semester, Integer instituteId) {
		return feedbackWindowRepository.feedbackWindowListForDropDown(academicYear, program_specialization_id, semester, instituteId);
	}
	
	
	public ResponseEntity<Object> getStudentForFeedbackEmpId(Integer studentId, Integer courseId, Integer empId) {
	    try {
	        if (classFeedbackAnswersRepository.getFeedbackAnswerCount(studentId, courseId) >= 1) {
	            throw new RuntimeException("Feedback already given for this course !!!");
	        }

	        Map<String, Object> employeeDetails = empDetail_repo.getDetails(empId);
	        GetStudentForFeedback getStudentForFeedback = studentDetailsRepository.getStudentForFeedback(studentId, courseId);

	        if (getStudentForFeedback == null) {
	            throw new RuntimeException("No data present for this combination !!!");
	        }

	        String concateFeedbackWindow = feedbackWindowRepository.getConcateFeedbackWindow(
	                getStudentForFeedback.getProgram_specialization_id(),
	                getStudentForFeedback.getSchoolId(),
	                getStudentForFeedback.getAcYearId().toString());

	        Integer feedbackWindowId = feedbackWindowRepository.getFeedbackWindowId(
	                getStudentForFeedback.getProgram_specialization_id(),
	                getStudentForFeedback.getSchoolId(),
	                getStudentForFeedback.getAcYearId().toString());

	        String semester = feedbackWindowRepository.getFeedBackWindowSemester(feedbackWindowId);

	        Integer countFeedbackWindowId = feedbackWindowRepository.getCountFeedbackWindowId(
	                getStudentForFeedback.getProgram_specialization_id(),
	                getStudentForFeedback.getSchoolId(),
	                getStudentForFeedback.getAcYearId().toString(),
	                semester);

	        Integer feedback_allowfor_student_id = feedbackAllowForStudentRepository.getCountFeedbackAllowForStudent(
	                feedbackWindowId, studentId, courseId);
	        
	        // Check if feedback_allowfor_student_id is NULL, 0, or empty
	        if (feedback_allowfor_student_id == null || feedback_allowfor_student_id == 0) {
	            // If feedback_allowfor_student_id is missing, check attendance
	            Map<String, Object> percentageDetails = freezeStudentAttendenceRepository.getAllPercentage(studentId, courseId);

	            Integer actualPercentage = percentageDetails.get("actualPercentage") != null
	                    ? Integer.parseInt(percentageDetails.get("actualPercentage").toString())
	                    : 0;

	            Integer assignPercentage = percentageDetails.get("assignPercentage") != null
	                    ? (Integer) percentageDetails.get("assignPercentage")
	                    : 0;

	            if (assignPercentage > actualPercentage) {
	                throw new RuntimeException("You don't have enough attendance for giving feedback !!!");
	            }
	        }

	        List<FeedbackQuestions> feedbackQuestions = studentFeedbackQuestionsRepository
	                .getFeedbackQuestions(getStudentForFeedback.getSchoolId());
	        getStudentForFeedback.setStudentFeedbackQuestions(feedbackQuestions);

	        Map<String, Object> responseMap = new HashMap<>();
	        Map<String, Object> studentDetails = new HashMap<>();
	        studentDetails.put("studentId", getStudentForFeedback.getStudentId());
	        studentDetails.put("studentName", getStudentForFeedback.getStudentName());
	        studentDetails.put("auid", getStudentForFeedback.getAuid());
	        studentDetails.put("year", getStudentForFeedback.getYear());
	        studentDetails.put("sem", getStudentForFeedback.getSem());
	        studentDetails.put("section", getStudentForFeedback.getSection());
	        studentDetails.put("schoolId", getStudentForFeedback.getSchoolId());
	        studentDetails.put("courseName", getStudentForFeedback.getCourseName());
	        studentDetails.put("usn", getStudentForFeedback.getUsn());
	        studentDetails.put("acYear", getStudentForFeedback.getAcYearId());
	        studentDetails.put("course_code", getStudentForFeedback.getCourse_code());
	        studentDetails.put("ac_year", getStudentForFeedback.getAc_year());
	        studentDetails.put("concateFeedbackWindow", concateFeedbackWindow);
	        studentDetails.put("feedback_window_id", feedbackWindowId);
	        studentDetails.put("feedback_window_count", countFeedbackWindowId);
	        studentDetails.put("program_specialization_id", getStudentForFeedback.getProgram_specialization_id());
	        studentDetails.put("empcode", employeeDetails.get("empcode"));
	        studentDetails.put("facultyName", employeeDetails.get("employee_name"));
	        studentDetails.put("designation_name", employeeDetails.get("designation_name"));
	        studentDetails.put("designation_short_name", employeeDetails.get("designation_short_name"));
	        studentDetails.put("school_name", employeeDetails.get("school_name"));
	        studentDetails.put("school_name_short", employeeDetails.get("school_name_short"));
	        studentDetails.put("org_name", employeeDetails.get("org_name"));
	        studentDetails.put("emp_image_attachment_path", employeeDetails.get("emp_image_attachment_path"));
	        studentDetails.put("studentFeedbackQuestions", getStudentForFeedback.getStudentFeedbackQuestions());

	        responseMap.put("studentDetails", studentDetails);

	        return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", responseMap);

	    } catch (Exception e) {
	        return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getMessage());
	    }
	}



	public ResponseEntity<Object> checkFeedbackWindow(Integer userId) {
		
		Integer studentId = uar_repo.getStudentEmail(userId);
		Integer programSpecializationId = studentDetailsRepository.getProgramSpecializationId(studentId);
		Integer schoolId = studentDetailsRepository.getStudentSchoolId(studentId);
		
		System.out.println("studentId" +studentId);
		System.out.println("programSpecializationId" +programSpecializationId);
		System.out.println("schoolId" +schoolId);
		
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = simpleDateFormat.format(date);
		System.out.println(formattedDate); 
		
		Integer count = feedbackWindowRepository.checkCount(schoolId,programSpecializationId, formattedDate);
		System.out.println("count " + count);
		if (count < 1) {
			throw new RuntimeException("The feedback window is temporarily closed. Please try again later.");
		}
		return ResponseEntity.status(HttpStatus.OK).body("Feedback window is open.");
	}

}
