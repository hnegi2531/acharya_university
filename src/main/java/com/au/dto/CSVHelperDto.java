package com.au.dto;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.au.model.CandidateResults;
import com.au.model.LessonPlanAssignment;
import com.au.repository.LessonPlanAssignmentRepository;

@Component
public class CSVHelperDto {
	
	@Autowired
	private LessonPlanAssignmentRepository lpat_repo;

	public static String TYPE = "text/csv";
	static String[] HEADERs = { "plan_date", "contents", "teaching_aid" };

	public static boolean hasCSVFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	String strDate;
	public List<LessonPlanAssignment> csvToTutorials(InputStream is, Integer user_id) throws Exception {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<LessonPlanAssignment> lessonPlans = new ArrayList<LessonPlanAssignment>();
			
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				
				try {
					 String  bt= csvRecord.get("plan_date");
					 SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy"); 
					 formatter2.setLenient(false);
					 Date date2=formatter2.parse(bt);
					 strDate= formatter2.format(date2);
				}catch (ParseException e) {
					throw new RuntimeException("Invalid Date Format !!! " );
				}
					
				LessonPlanAssignment lessonPlan = new LessonPlanAssignment(csvRecord.get("plan_date"),
						csvRecord.get("contents"), csvRecord.get("teaching_aid"), csvRecord.get("type"),
						csvRecord.get("teaching_mode"), csvRecord.get("learning_style"));
				lessonPlans.add(lessonPlan);
	//			}
			}
			return lessonPlans;
		} catch (IOException e) {
			throw new RuntimeException("Combination already present for the given inputs! CSV upload failed!!");
		}
	}
	
	public List<CandidateResults> csvToTutorialsForCandidateResults(InputStream is, Integer user_id) throws Exception {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<CandidateResults> candidate_res = new ArrayList<CandidateResults>();
			
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
					CandidateResults can_res = new CandidateResults(csvRecord.get("application_no"),
							csvRecord.get("result"),Double.parseDouble(csvRecord.get("percentage")),
							Integer.parseInt(csvRecord.get("exam_details_id")),csvRecord.get("type"),csvRecord.get("grade"),true );
					candidate_res.add(can_res);
					
					
//				}
			}
			return candidate_res;
		} catch (IOException e) {
			throw new RuntimeException("Combination already present for the given inputs! CSV upload failed!!");
		}
		
	}


	public static ByteArrayInputStream tutorialsToCSV(List<LessonPlanAssignment> tutorials) {
		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
			for (LessonPlanAssignment tutorial : tutorials) {
				List<Object> data = Arrays.asList(tutorial.getLesson_assignment_id(), tutorial.getContents(),
						tutorial.getLesson_id(), tutorial.getPlan_date(), tutorial.getTeaching_aid(),
						tutorial.getActive(), tutorial.getCreated_by(), tutorial.getCreated_date(),
						tutorial.getCreated_username(), tutorial.getModified_by(), tutorial.getModified_date(),
						tutorial.getModified_username());
				csvPrinter.printRecord(data);
			}
			csvPrinter.flush();
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
		}
	}
	
}
