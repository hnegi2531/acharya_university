package com.au.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.JwtDetails;
import com.au.model.BiometricTransaction;
import com.au.model.TdsDeduction;
import com.au.model.TemporaryIncrementCreation;
import com.au.model.TemporaryInvPay;
import com.au.repository.BiometricTransactionRepository;

@Component
public class CsvUtil {
	
	private BiometricTransactionRepository biometricTransactionRepository;
	public static String TYPE = "text/csv";
	static String[] HEADERs = {"Employee Name","Employee Code", "Amount", "Type" };
	static String[] BIOMETRIC_TRANSACTION_HEADERs = { "Employee Code", "Transaction Date", "Transaction Time" };

	private static final String BATCH_ID = "BATCH-";
	private static int batchIdCounter = 0;
	Random random = new Random();

	
	public static boolean hasCSVFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public static List<TemporaryInvPay> getDateFromCSV(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			 CSVParser csvParser = new CSVParser(fileReader,
					 CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			System.out.println("AAAAAAAAAAAAAAAAA");
			List<TemporaryInvPay> tutorials = new ArrayList<TemporaryInvPay>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				System.out.println("CCCCCCCCCCCCCCCCC " + csvRecord.get("Employee Code"));
				TemporaryInvPay tutorial = new TemporaryInvPay(csvRecord.get("Employee Name"),
						csvRecord.get("Employee Code"),
						Double.valueOf(csvRecord.get("Amount")),
						csvRecord.get("Type"),
						csvRecord.get("Remarks")
				);

				tutorials.add(tutorial);
			}
			System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBB");
			return tutorials;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}

	public static List<BiometricTransaction> getBiometricTransactiondetailsFromCSV(InputStream is)
			throws ParseException {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<BiometricTransaction> biometricTransactions = new ArrayList<BiometricTransaction>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			for (CSVRecord csvRecord : csvRecords) {
				String empCode = csvRecord.get("Employee Code");
				String dateTimeStr = csvRecord.get("Transaction Date") + " " + csvRecord.get("Transaction Time");

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd[ H:mm[:ss]]");
				LocalDateTime trnDateTime = LocalDateTime.parse(dateTimeStr, formatter);
				LocalDateTime adjustedDateTime = trnDateTime.with(LocalTime.MIN);
				Date date = Date.from(adjustedDateTime.atZone(ZoneId.systemDefault()).toInstant());
				
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy"); 
				SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd"); 
				String trnDate=formatter2.format(date);
				Time time = Time.valueOf(trnDateTime.toLocalTime());
                LocalTime modifiedTime=time.toLocalTime().minusHours(5).minusMinutes(30);
                Time newTime=Time.valueOf(modifiedTime);
				BiometricTransaction bioTrans = new BiometricTransaction(empCode, trnDate, newTime);			bioTrans.setCardID(null);
				bioTrans.setCreatedAt(new Date());
				biometricTransactions.add(bioTrans);
			}

			return biometricTransactions;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}

	public static List<TemporaryIncrementCreation> getDataForIncrementCreation(InputStream inputStream, Integer month,
			Integer year, JwtDetails jwtDetails) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<TemporaryIncrementCreation> incrementCreations = new ArrayList<TemporaryIncrementCreation>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for (CSVRecord csvRecord : csvRecords) {

				TemporaryIncrementCreation incrementCreation = new TemporaryIncrementCreation(csvRecord.get("EmpCode"),
						csvRecord.get("Proposed Designation"), csvRecord.get("Proposed Department"),
						csvRecord.get("Proposed Salary Structure"), csvRecord.get("Proposed Basic"),
						csvRecord.get("Proposed Special Pay"), csvRecord.get("Proposed Gross pay"),
						csvRecord.get("Proposed CTC"), csvRecord.get("Remarks"), month, year);
				incrementCreation.setCreatedBy(jwtDetails.getUserId());
				incrementCreations.add(incrementCreation);
			}

			return incrementCreations;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}

	}

	public static List<TdsDeduction> getDataForTdsDeduction(InputStream inputStream, Integer month, Integer year,
			JwtDetails jwtDetails) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<TdsDeduction> tdsDeductions = new ArrayList<TdsDeduction>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for (CSVRecord csvRecord : csvRecords) {

				TdsDeduction tdsDeduction = new TdsDeduction(csvRecord.get("EmpCode"),
						csvRecord.get("EmployeeName"), csvRecord.get("Amount"), month, year);
				tdsDeduction.setCreatedBy(jwtDetails.getUserId());
				tdsDeductions.add(tdsDeduction);
			}

			return tdsDeductions;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}

}
