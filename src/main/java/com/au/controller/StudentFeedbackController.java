package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.ClassFeedbackAnswersWebDTO;
import com.au.dto.EligibleStudentFeedbackRequestDTO;
import com.au.dto.FeedbackWindowDTO;
import com.au.dto.FeedbackWindowNewDto;
import com.au.dto.FreezeStudentAttendenceDTO;
import com.au.dto.StudentFeedbackAnswersDTO;
import com.au.dto.StudentFeedbackQuestionsDTO;
import com.au.model.FeedbackWindow;
import com.au.service.StudentFeedbackQuestionsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;




@RestController
@RequestMapping("/api/feedback")
@CrossOrigin
public class StudentFeedbackController {
	
	@Autowired
	private StudentFeedbackQuestionsService studentFeedbackQuestionsService;

	@PostMapping("/createFeedbackQuestionsForStudents")
	public ResponseEntity<Object> createFeedbackQuestionsForStudents(@RequestBody StudentFeedbackQuestionsDTO studentFeedbackQuestions){
		return studentFeedbackQuestionsService.createFeedbackQuestionsForStudents(studentFeedbackQuestions);
	}
	
	@GetMapping("/getFeedbackQuestionsForStudentsList")
	public ResponseEntity<Object> getFeedbackQuestionsForStudentsList(@RequestParam("instituteId") Integer instituteId, @RequestParam("courseId") Integer courseId){
		return studentFeedbackQuestionsService.getFeedbackQuestionsForStudentsList(instituteId,courseId);
	}
	
	@GetMapping("/getFeedbackQuestionsForStudentsListByQuestionId")
	public ResponseEntity<Object> getFeedbackQuestionsForStudentsListByQuestionId(@RequestParam("instituteId") Integer instituteId, @RequestParam("courseId") Integer courseId,@RequestParam("questionId") Long questionId){
		return studentFeedbackQuestionsService.getFeedbackQuestionsForStudentsListByQuestionId(instituteId,courseId,questionId);
	}
	
	
	@DeleteMapping("/deactivateFeedbackQuestionsForStudents")
	public ResponseEntity<Object> deactivateFeedbackQuestionsForStudents(@RequestParam("instituteId") Integer instituteId, @RequestParam("courseId") Integer courseId,@RequestParam("questionId") Long questionId){
		return studentFeedbackQuestionsService.deactivateFeedbackQuestionsForStudents(instituteId,courseId,questionId);
	}
	
	@PostMapping("/updateFeedbackQuestionsForStudents")
	public ResponseEntity<Object> updateFeedbackQuestionsForStudents(@RequestBody StudentFeedbackQuestionsDTO studentFeedbackQuestions){
		return studentFeedbackQuestionsService.updateFeedbackQuestionsForStudents(studentFeedbackQuestions);
	}
	
	@GetMapping("/getStudentForFeedback")
	public ResponseEntity<Object> getStudentForFeedback(@RequestParam("studentId") Integer studentId,@RequestParam("courseId") Integer courseId){
		return studentFeedbackQuestionsService.getStudentDetailsForFeedback(studentId,courseId);
	}
	
	@GetMapping("/getStudentForFeedbackEmpId")
	public ResponseEntity<Object> getStudentForFeedbackEmpId(@RequestParam("studentId") Integer studentId,@RequestParam("courseId") Integer courseId,@RequestParam("empId") Integer empId){
		return studentFeedbackQuestionsService.getStudentForFeedbackEmpId(studentId,courseId,empId);
	}
	
	@PostMapping("/studentFeedbackAnswers")
	public ResponseEntity<Object> studentFeedbackAnswers(@RequestBody List<StudentFeedbackAnswersDTO> studentFeedbackAnswersDTO){
		return studentFeedbackQuestionsService.studentFeedbackAnswers(studentFeedbackAnswersDTO);
	}
	
	@PostMapping("/createFeedbackWindow")
	public ResponseEntity<Object> createFeedbackWindow(@RequestBody FeedbackWindowDTO feedbackWindowDTO){
		return studentFeedbackQuestionsService.createFeedbackWindow(feedbackWindowDTO);
	}
	
	@GetMapping("/feedbackWindowList")
	public ResponseEntity<Object> feedbackWindowList(){
		return studentFeedbackQuestionsService.feedbackWindowList();
	}
	
	@GetMapping("/feedbackWindowById")
	public ResponseEntity<Object> feedbackWindowById(@RequestParam("feedbackWindowId") Long feedbackWindowId){
		return studentFeedbackQuestionsService.feedbackWindowById(feedbackWindowId);
	}
	
	@PostMapping("/updateFeedbackWindow")
	public ResponseEntity<Object> updateFeedbackWindow(@RequestBody FeedbackWindowNewDto feedbackWindow){
	    return studentFeedbackQuestionsService.updateFeedbackWindow(feedbackWindow);
	}
	
	
	@PostMapping("/freezeStudentAttendence")
	public ResponseEntity<Object> freezeStudentAttendence(@RequestBody FreezeStudentAttendenceDTO freezeStudentAttendence){
		return studentFeedbackQuestionsService.freezeStudentAttendence(freezeStudentAttendence);
	}
	
	@GetMapping("/freezeStudentAttendenceList")
	public ResponseEntity<Object> freezeStudentAttendenceList(){
		return studentFeedbackQuestionsService.freezeStudentAttendenceList();
	}
	
	@GetMapping("/freezeStudentAttendenceById")
	public ResponseEntity<Object> freezeStudentAttendenceById(@RequestParam("freezeId") Long freezeId){
		return studentFeedbackQuestionsService.freezeStudentAttendenceById(freezeId);
	}
	
	@PostMapping("/updatefreezeStudentAttendence")
	public ResponseEntity<Object> updatefreezeStudentAttendence(@RequestBody FreezeStudentAttendenceDTO freezeStudentAttendence){
		return studentFeedbackQuestionsService.updatefreezeStudentAttendence(freezeStudentAttendence);
	}
	
	@DeleteMapping("/deactivefreezeStudentAttendence")
	public ResponseEntity<Object> deactivefreezeStudentAttendence(@RequestParam("freezeId") Long freezeId){
		return studentFeedbackQuestionsService.deactivefreezeStudentAttendence(freezeId);
	}
	
	@PostMapping("/getStudentHaveLessAttendence")
	public ResponseEntity<Object> getStudentHaveLessAttendence(
			@RequestBody EligibleStudentFeedbackRequestDTO eligibleStudentFeedbackRequestDTO) {
		return studentFeedbackQuestionsService.getStudentHaveLessAttendence(eligibleStudentFeedbackRequestDTO);
	}
	
	
	@GetMapping("/getEmployeeList")
	public ResponseEntity<Object> getEmployeeList(@RequestParam("studentId") Integer studentId ) {
		return studentFeedbackQuestionsService.getEmployeeList(studentId);
	}
	
	@GetMapping("/getCourseList")
	public ResponseEntity<Object> getCourseList(@RequestParam("empId") Integer empId,@RequestParam("studentId") Integer studentId ) {
		return studentFeedbackQuestionsService.getCourseList(empId,studentId);
	}
	
	@GetMapping("/employeeFeedbackReport")
	public ResponseEntity<Object> employeeFeedbackReport(@RequestParam("empId") Integer empId) {
		return studentFeedbackQuestionsService.employeeFeedbackReport(empId);
	}
	
	

	@PostMapping("/manualAttendenceUpdateForStudent")
	public ResponseEntity<Object> manualAttendenceUpdateForStudent(
			@RequestBody List<EligibleStudentFeedbackRequestDTO> eligibleStudentFeedbackRequestDTO) {
		return studentFeedbackQuestionsService.manualAttendenceUpdateForStudent(eligibleStudentFeedbackRequestDTO);
	}

	@PostMapping("/classFeedbackAnswersWeb")
	public ResponseEntity<Object> saveClassFeedbackAnswers(@RequestBody @Valid ClassFeedbackAnswersWebDTO cfa,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {

		return studentFeedbackQuestionsService.saveClassFeedbackAnswers(cfa);

	}
	
	@GetMapping("/checkFeedbackWindow")
	public ResponseEntity<Object> checkFeedbackWindow(@RequestParam("userId") Integer userId ) {
		return studentFeedbackQuestionsService.checkFeedbackWindow(userId);
	}
	
	@DeleteMapping("/deactivateFeedbackWindow")
	public ResponseEntity<Object> deactivateFeedbackWindow(@RequestParam("feedbackWindowId") Long feedbackWindowId){
		return studentFeedbackQuestionsService.deactivateFeedbackWindow(feedbackWindowId);
	}
	
	@PutMapping("/activateFeedbackWindow")
	public ResponseEntity<Object> activateFeedbackWindow(@RequestParam("feedbackWindowId") Long feedbackWindowId) {
		return studentFeedbackQuestionsService.activateFeedbackWindow(feedbackWindowId);
	}
	
	@PutMapping("/activefreezeStudentAttendence")
	public ResponseEntity<Object> activefreezeStudentAttendence(@RequestParam("freezeId") Long freezeId) {
		return studentFeedbackQuestionsService.activefreezeStudentAttendence(freezeId);
	}
	
	@GetMapping("/feedbackWindowListForDropDown")
	public List<Map<String, Object>> feedbackWindowListForDropDown(@RequestParam("academicYear") String academicYear ,
			@RequestParam("program_specialization_id") Integer program_specialization_id,
			@RequestParam("semester") String semester,@RequestParam("instituteId") Integer instituteId){
		return studentFeedbackQuestionsService.feedbackWindowListForDropDown( academicYear, program_specialization_id, semester, instituteId);
	}
	
}
