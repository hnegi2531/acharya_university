package com.au.dto;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.au.model.BankImportTransaction;
import com.au.repository.BankImportTransactionRepository;


@Component
public class CSVTransactionHelperDto {

	
	@Autowired
	private BankImportTransactionRepository bit_repo;
	
	
	public static String TYPE = "text/csv";
	static String[] HEADERs = { "transaction_no", "cheque_dd_no", "transaction_remarks", "transaction_date" };

	private static final List<String> DATE_FORMATS = Arrays.asList(
			"dd-MM-yyyy",
			"dd/MM/yyyy",
			"yyyy-MM-dd",
			"MM/dd/yyyy",
			"dd MMM yyyy",
			"dd.MM.yyyy",
			"ddMMMyyyy"  // Handles 12Apr2025
	);

	public static boolean hasCSVFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}
	
//	public List<BankImportTransaction> csvToTutorials(InputStream is, Integer user_id) throws Exception {
//		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//				CSVParser csvParser = new CSVParser(fileReader,
//						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
//
//			List<BankImportTransaction> bit = new ArrayList<BankImportTransaction>();
//			
//			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
//
//			for (CSVRecord csvRecord : csvRecords) {
//			
//				BankImportTransaction bits = new BankImportTransaction(csvRecord.get("Transaction_no"), csvRecord.get("cheque_dd_no"),
//						 csvRecord.get("transaction_remarks"), csvRecord.get("transaction_date"));
//				bit.add(bits);
//				}
//			
//			return bit;
//		} catch (IOException e) {
//			throw new RuntimeException("Combination already present for the given inputs! CSV upload failed!!");
//		}
//	}
	
	String strDate;
	String  bt;
	public List<BankImportTransactionDto> csvToTutorials(InputStream is, Integer user_id) throws Exception {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<BankImportTransactionDto> bit = new ArrayList<BankImportTransactionDto>();
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			for (CSVRecord csvRecord : csvRecords) {
			try {
				DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
				String  bt= csvRecord.get("transaction_date");
				System.out.println("*************************************" +bt);
				Date parsedDate = parseFlexibleDate(bt);

//				Date d1 = df.parse(bt);
				strDate= df.format(parsedDate);
			} catch(Exception e) {
				throw new RuntimeException("Invalid Date Format !!! " );
			}
			try {
				BankImportTransactionDto bits = new BankImportTransactionDto(csvRecord.get("Transaction_no"), csvRecord.get("cheque_dd_no"),
				Double.parseDouble(csvRecord.get("amount")), strDate);
				             
				bit.add(bits);
			} catch (Exception e) {
					throw new RuntimeException("Invalid amount !!!");
			}
		}
		return bit;
			
		} catch (IOException e) {
			throw new RuntimeException("Combination already present for the given inputs! CSV upload failed!!");
		}
	}
	
	public static ByteArrayInputStream tutorialsToCSV(List<BankImportTransaction> tutorials) {
		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
			for (BankImportTransaction tutorial : tutorials) {
				List<Object> data = Arrays.asList(tutorial.getBank_import_transaction_id(), tutorial.getTransaction_no(),
						tutorial.getCheque_dd_no(), tutorial.getTransaction_remarks(), tutorial.getTransaction_date(),
						tutorial.getActive(), tutorial.getCreated_by(), tutorial.getCreated_Date(),
						tutorial.getStart_row(),tutorial.getEnd_row(),tutorial.getSchool_id(),tutorial.getStudent_id(),
						tutorial.getReceipt_no(),tutorial.getAmount(),tutorial.getFc_year_id(),tutorial.getBalance(),
						tutorial.getBank_usd_amt(),tutorial.getBank_inr_amt(),tutorial.getPaid(),
						tutorial.getCreated_username(), tutorial.getModified_by(), tutorial.getModified_Date(),
						tutorial.getModified_username());
				csvPrinter.printRecord(data);
			}
			csvPrinter.flush();
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
		}
	}

	public static Date parseFlexibleDate(String dateStr) {
		for (String format : DATE_FORMATS) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
				sdf.setLenient(false);
				return sdf.parse(dateStr);
			} catch (Exception ignored) {
			}
		}
		throw new IllegalArgumentException("Unparseable date: " + dateStr);
	}
	
}
