package com.au.service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.au.dto.JwtDetails;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Bank;
import com.au.model.FinancialYear;
import com.au.repository.BankRepository;
import com.au.repository.FeeReceiptRepository;
import com.au.repository.FinancialYearRepository;
import com.au.repository.School_Repository;
import com.au.repository.StudentPaymentHistoryRepository;
import com.au.repository.VoucherHeadRepository;
import com.au.response.ResponseHandler;


@Service
public class BankService {
	
	@Autowired
	private BankRepository bank_repository;
	
	@Autowired
	private FinancialYearRepository financial_year_repo;
	
	@Autowired
	private FeeReceiptRepository frc_repo;
	

	
	@Autowired
	private VoucherHeadRepository vou_repo;
	
	@Autowired
	private School_Repository sc_repo;
	

	@Autowired
	private StudentPaymentHistoryRepository sph_repo;
	
	public List<Bank> listAll() {
		return bank_repository.findAll1();
	}
	
	public List<Bank> listAll1() {
		return bank_repository.findAll();
	}

	public Bank saveBank(Bank b) throws Exception {
		if(bank_repository.getCountOfBankAccount(b.getAccount_number())>=1) {
			throw new Exception("Account Already Exist");
		} else {
			bank_repository.save(b);
		}
		return b;
		
	}
	
	public Bank updateBank(Bank bank, JwtDetails jwtDetails) {
		bank.setModified_by(jwtDetails.getUserId());
		bank.setModified_username(jwtDetails.getUserName());
		bank.setBalanceUpdatedBy(jwtDetails.getUserName());
		if(bank.getBalanceUpdatedOn() == null){
			bank.setPreviousUpdatedOn(new Date());
		}
		else bank.setPreviousUpdatedOn(bank.getBalanceUpdatedOn());
		bank.setBalanceUpdatedOn(new Date());
		return bank_repository.save(bank);
	}

	public Bank get(Integer id) {
		return bank_repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bank Not Found:" + id));
	}

	public void delete(Integer id) {
		Bank cc = bank_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bank Not Found:" + id));
		bank_repository.updateBank(id);
	}

	public void delete1(Integer id) {
		Bank cc = bank_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bank Not Found:" + id));
		bank_repository.updateBank1(id);

	}

	
	public Date increaseDateByOne(Date d) {
System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ"+d);
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, 1);
		d = c.getTime();
		System.out.println("JJJJJJJJJJJJJJJJJJJJJ1111111111111111JJJJJJJJJJJJJJJJJ"+d);
		return d;
	}	
//	public List<Bank> fetchAllBanknDetails() {
//		return bank_repository.findAll();
//	}
	
	public ResponseEntity<Object> fetchAllBanknDetails1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = bank_repository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllBanknDetails2(Pageable pageable) {
		
		Page<Object> response = bank_repository.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public List<HashMap<String, Object>> getTotalAmountBankWise(Integer financial_year_id) {
		List<Bank> banksData = bank_repository.findAll1();
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			banksData.stream().forEach(bank -> {
				Double book_balance =0.0;
				HashMap<String, Object> bank_amount_data = new HashMap<String, Object>();
	//			book_balance = bank_repository.getTotalAmountByBank(bank.getBank_id(), financial_year_id);
				Double opening_balance = bank_repository.getOutstandingBalance(bank.getBank_id())==null?0.0:bank_repository.getOutstandingBalance(bank.getBank_id());
				Double bank_balance = bank_repository.getBankBalance(bank.getBank_id())==null?0.0:bank_repository.getBankBalance(bank.getBank_id());
				
				System.out.println("::::::::::::::::::::::::::::::::::::: "+opening_balance);
				Double totalBankDebit = bank_repository.getBanksTotalDebit(bank.getBank_id(),financial_year_id)==null?0.0:bank_repository.getBanksTotalDebit(bank.getBank_id(),financial_year_id );
				System.out.println("FIIIIIIIIIIIINNNNNNNNNNNNNNAAAAAAALLLLLLLLLLL "+totalBankDebit);
				Double totalBankCredit = bank_repository.getBanksTotalCredit(bank.getBank_id(),financial_year_id)==null?0.0:bank_repository.getBanksTotalCredit(bank.getBank_id(),financial_year_id );
				System.out.println("FIIIIIIIIIIIINNNNNNNNNN2NNNNAAAAAAALLLLLLLLLLL "+totalBankCredit);
				
				book_balance = opening_balance - totalBankDebit + totalBankCredit; 
				
				bank_amount_data.put("bank_id", bank.getBank_id());
				bank_amount_data.put("bank_name", bank.getBank_name());
				bank_amount_data.put("book_balance", book_balance);
				bank_amount_data.put("opening_balance", opening_balance);
				bank_amount_data.put("bank_balance", bank_balance);
				
				System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK "+book_balance);
				data.add(bank_amount_data);
			
			});

		return data;
	}
	
	Date date;
	public List<HashMap<String, Object>> getCreditDebitTotalAmountMonthly(Integer school_id, Integer financial_year_id, Integer bank_id) {
			
		FinancialYear financialYear = financial_year_repo.getOne(financial_year_id);
		Bank bankData = bank_repository.getOne(bank_id);

		LocalDate fromDate = LocalDate.fromDateFields(increaseDateByOne(financialYear.getFrom_date()));
		LocalDate toDate = LocalDate.fromDateFields(increaseDateByOne(financialYear.getTo_date()));
		
			//get no of months between given dates
			Integer daysDiff = Months.monthsBetween(fromDate, toDate).getMonths();
			System.out.println("(((((((((((((((((()))))))))))))))))) "+daysDiff);
			
			date = financialYear.getFrom_date();
			 
			List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			
			System.out.println("++++++++++++++++++++++++++++++++ "+fromDate);
			System.out.println("++++++++++++++++++++++++++++++++ "+toDate);
			System.out.println("++++++++++++++++++++++++++++++++ "+date);
			Double new_ob = 0.0;
			for(int i = 0 ; i<=daysDiff ; i++) {
				
				HashMap<String, Object> data1 = new HashMap<String, Object>();
				 
				System.out.println("++++++++++++++++++++++++++++++++ "+fromDate);

				System.out.println("________________________________________________________________ "+fromDate.getMonthOfYear());

				if(i==0) {
					data1.put("opening_balance", bankData.getOpening_balance());
				} else {
					data1.put("opening_balance", new_ob);
				}
				
				Double totalDebitOfMonth = bank_repository.getTotalDebitOfMonth(bank_id,financial_year_id,fromDate.getMonthOfYear())==null?0.0:bank_repository.getTotalDebitOfMonth(bank_id,financial_year_id,fromDate.getMonthOfYear() );
				System.out.println("FIIIIIIIIIIIINNNNNNNNNNNNNNAAAAAAALLLLLLLLLLL "+totalDebitOfMonth);
				Double totalCreditOfMonth = bank_repository.getTotalCreditOfMonth(bank_id,financial_year_id,fromDate.getMonthOfYear())==null?0.0:bank_repository.getTotalCreditOfMonth(bank_id,financial_year_id,fromDate.getMonthOfYear() );
				System.out.println("FIIIIIIIIIIIINNNNNNNNNN2NNNNAAAAAAALLLLLLLLLLL "+totalCreditOfMonth);

//				new_ob = bankData.getOpening_balance() + (totalCreditOfMonth - totalDebitOfMonth );
				new_ob = bankData.getOpening_balance() - totalDebitOfMonth + totalCreditOfMonth; 
				
				data1.put("debit", totalDebitOfMonth);
				data1.put("credit", totalCreditOfMonth);
				data1.put("book_balance", new_ob);
				data1.put("month", fromDate.getMonthOfYear());
				data1.put("year", fromDate.getYear());
				
				data.add(data1);
				fromDate = fromDate.plusMonths(1); 
				System.out.println("INCREMENTEDDDDDDDDDDDDDDDDDDDDDDDDD "+fromDate);

				
			}
			new_ob=0.0;
			
		return data;
	}	
	
	Date d;
	public List<HashMap<String, Object>> getCreditDebitTotalAmountDayWise(Integer school_id, Integer financial_year_id,Integer bank_id, Date fDate, Date tDate) {
		
		//Date d1;
		FinancialYear financialYear = financial_year_repo.getOne(financial_year_id);
		Bank bankData = bank_repository.getOne(bank_id);
		
		LocalDate fromDate = LocalDate.fromDateFields(fDate);
		LocalDate toDate = LocalDate.fromDateFields(tDate);
		
			//get no of months between given dates
			Integer daysDiff = Days.daysBetween(fromDate, toDate).getDays();
			System.out.println("(((((((((((((((((()))))))))))))))))) "+daysDiff);
			
			d = fDate;
			 
			List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			
			System.out.println("++++++++++++++++++++++++++++++++ "+fromDate);
			System.out.println("++++++++++++++1++++++++++++++++++ "+fromDate.getDayOfMonth());
			System.out.println("++++++++++++++++++++++++++++++++ "+d);
			
			
			Double new_ob = 0.0;
			for(int i = 0 ; i<=daysDiff ; i++) {
				
				Format f = new SimpleDateFormat("EEEE");  
				String dayName = f.format(d); 
				System.out.println("++++++++++++++OOOOOOOOOOOOOOOOOOOO++++++++++++++++++ "+dayName);
				HashMap<String, Object> data1 = new HashMap<String, Object>();
				
				if(i==0) {
					data1.put("opening_balance", bankData.getOpening_balance());
				} else {
					data1.put("opening_balance", new_ob);
				}
				
				Double totalDebitOfMonth = bank_repository.getTotalDebitOfDay(bank_id,financial_year_id,d )==null?0.0:bank_repository.getTotalDebitOfDay(bank_id,financial_year_id,d);
				System.out.println("FIIIIIIIIIIIINNNNNNNNNNNNNNAAAAAAALLLLLLLLLLL "+totalDebitOfMonth);
				Double totalCreditOfMonth = bank_repository.getTotalCreditOfDay(bank_id,financial_year_id,d )==null?0.0:bank_repository.getTotalCreditOfDay(bank_id,financial_year_id,d );
				System.out.println("FIIIIIIIIIIIINNNNNNNNNN2NNNNAAAAAAALLLLLLLLLLL "+totalCreditOfMonth);
		
				
//				Double book_balance1 = bank_repository.getTotalAmountByBank1(bank_id, financial_year_id, date);

				new_ob = bankData.getOpening_balance() - totalDebitOfMonth + totalCreditOfMonth;
				
				data1.put("debit", totalDebitOfMonth);
				data1.put("credit", totalCreditOfMonth);
				data1.put("book_balance", new_ob);
				//data1.put("book_balance", new_ob);
				data1.put("day", dayName);
				data1.put("date", d);
				
				data.add(data1);

				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+new_ob);
				
				//increment date by 1
				Calendar c = Calendar.getInstance();
				c.setTime(d);
				c.add(Calendar.DATE, 1);
				d = c.getTime();
			//	increaseDateByOne(d);
				System.out.println("????????????????????????????????????????????? "+d);
			}
	//	date = null;
		return data;
	}
	
public List<HashMap<String, Object>> getCreditDebitTotalAmountMonthlyVoucherHeadWise(Integer school_id,Integer financial_year_id, Integer voucher_head_new_id) {
		
		Integer voucher_head_id = vou_repo.getVoucherHeadId(voucher_head_new_id);
		FinancialYear financialYear = financial_year_repo.getOne(financial_year_id);
//		List<Schools> schools = sc_repo.findAll12();
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> final_data = new ArrayList<HashMap<String, Object>>();

		LocalDate fromDate = LocalDate.fromDateFields(increaseDateByOne(financialYear.getFrom_date()));
		LocalDate toDate = LocalDate.fromDateFields(increaseDateByOne(financialYear.getTo_date()));
		
			//get no of months between given dates
			Integer daysDiff = Months.monthsBetween(fromDate, toDate).getMonths();
			System.out.println("(((((((((((((((((()))))))))))))))))) "+daysDiff);
			
			date = financialYear.getFrom_date();
			
			System.out.println("++++++++++++++++++++++++++++++++ "+fromDate);
			System.out.println("++++++++++++++++++++++++++++++++ "+toDate);
			System.out.println("++++++++++++++++++++++++++++++++ "+date);
//			Double new_ob = 0.0;
			
			HashMap<String, Object> data2 = new HashMap<String, Object>();
				
			for(int i = 0 ; i<=daysDiff ; i++) {
				
				
				HashMap<String, Object> data1 = new HashMap<String, Object>();
				 
				System.out.println("++++++++++++++++++++++++++++++++ "+fromDate);

				System.out.println("________________________________________________________________ "+fromDate.getMonthOfYear());
				
				Double new_ob = 0.0;

//				if(i==0) {
//					data1.put("opening_balance", 0.0);
//				} else {
//					data1.put("opening_balance", new_ob);
//				}
				
				Double totalDebitOfMonth = bank_repository.getTotalDebitOfMonthVoucherHeadWise(voucher_head_id,financial_year_id,school_id,fromDate.getMonthOfYear())==null?0.0:bank_repository.getTotalDebitOfMonthVoucherHeadWise(voucher_head_id,financial_year_id,school_id,fromDate.getMonthOfYear());
				System.out.println("FIIIIIIIIIIIINNNNNNNNNNNNNNAAAAAAALLLLLLLLLLL "+totalDebitOfMonth);
				Double totalCreditOfMonth = bank_repository.getTotalCreditOfMonthVoucherHeadWise(voucher_head_new_id,financial_year_id,school_id,fromDate.getMonthOfYear())==null?0.0:bank_repository.getTotalCreditOfMonthVoucherHeadWise(voucher_head_new_id,financial_year_id,school_id,fromDate.getMonthOfYear());
				System.out.println("FIIIIIIIIIIIINNNNNNNNNN2NNNNAAAAAAALLLLLLLLLLL "+totalCreditOfMonth);

				new_ob = new_ob - totalDebitOfMonth + totalCreditOfMonth; 
				
				data1.put("debit", totalDebitOfMonth);
				data1.put("credit", totalCreditOfMonth);
				data1.put("closing_balance", new_ob);
				data1.put("opening_balance", 0.0);
				data1.put("month", fromDate.getMonthOfYear());
				data1.put("year", fromDate.getYear());
				
				
				data.add(data1);
				
			
				fromDate = fromDate.plusMonths(1); 
				System.out.println("INCREMENTEDDDDDDDDDDDDDDDDDDDDDDDDD "+fromDate);

				
				}
//			data2.put(schools.get(k).getSchool_name_short(), data);
//
//			final_data.add(data2);
		return data;
	}

public List<HashMap<String, Object>> getCreditDebitTotalAmountDayWisebyVoucherHeadWise(Integer school_id,Integer financial_year_id,Integer voucher_head_new_id, Date fDate, Date tDate) {
	
	Integer voucher_head_id = vou_repo.getVoucherHeadId(voucher_head_new_id);
	List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	List<HashMap<String, Object>> final_data = new ArrayList<HashMap<String, Object>>();
	
	LocalDate fromDate = LocalDate.fromDateFields(fDate);
	LocalDate toDate = LocalDate.fromDateFields(tDate);
	
		//get no of months between given dates
		Integer daysDiff = Days.daysBetween(fromDate, toDate).getDays();
		System.out.println("(((((((((((((((((()))))))))))))))))) "+daysDiff);
		
		d = fDate;
		 
//		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		
		System.out.println("++++++++++++++++++++++++++++++++ "+fromDate);
		System.out.println("++++++++++++++1++++++++++++++++++ "+fromDate.getDayOfMonth());
		System.out.println("++++++++++++++++++++++++++++++++ "+d);
		
		
//		Double new_ob = 0.0;
		
		HashMap<String, Object> data2 = new HashMap<String, Object>();
			
		for(int i = 0 ; i<=daysDiff ; i++) {
			
			Format f = new SimpleDateFormat("EEEE");  
			String dayName = f.format(d); 
			System.out.println("++++++++++++++OOOOOOOOOOOOOOOOOOOO++++++++++++++++++ "+dayName);
			HashMap<String, Object> data1 = new HashMap<String, Object>();
			
//			if(i==0) {
//				data1.put("opening_balance", bankData.getOpening_balance());
//			} else {
//				data1.put("opening_balance", new_ob);
//			}
			
			Double totalDebitOfMonth = bank_repository.getTotalDebitOfDayVoucherHeadWise(voucher_head_id,school_id,financial_year_id,d )==null?0.0:bank_repository.getTotalDebitOfDayVoucherHeadWise(voucher_head_id,school_id,financial_year_id,d);
			System.out.println("FIIIIIIIIIIIINNNNNNNNNNNNNNAAAAAAALLLLLLLLLLL "+totalDebitOfMonth);
			Double totalCreditOfMonth = bank_repository.getTotalCreditOfDayVoucherHeadWise(voucher_head_new_id,school_id,financial_year_id,d )==null?0.0:bank_repository.getTotalCreditOfDayVoucherHeadWise(voucher_head_new_id,school_id,financial_year_id,d );
			System.out.println("FIIIIIIIIIIIINNNNNNNNNN2NNNNAAAAAAALLLLLLLLLLL "+totalCreditOfMonth);
	
			
//			Double book_balance1 = bank_repository.getTotalAmountByBank1(bank_id, financial_year_id, date);
			Double new_ob = 0.0;
			new_ob = new_ob - totalDebitOfMonth + totalCreditOfMonth;
			
			data1.put("debit", totalDebitOfMonth);
			data1.put("credit", totalCreditOfMonth);
			data1.put("closing_balance", new_ob);
			data1.put("opening_balance", 0.0);
			data1.put("day", dayName);
			data1.put("date", d);
			
			data.add(data1);

			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+new_ob);
			
			//increment date by 1
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.DATE, 1);
			d = c.getTime();
		//	increaseDateByOne(d);
			System.out.println("????????????????????????????????????????????? "+d);
		}
//		data2.put(schools.get(k).getSchool_name_short(), data);
//		final_data.add(data2);
		
	return data;

}

	public List<Map<String, Object>> bankDetailsBasedOnSchoolId(Integer school_id) {
		return bank_repository.bankDetailsBasedOnSchoolId(school_id);
	}

	
	public List<Map<String, Object>> getAllbankDetailsData() {
		return bank_repository.getAllbankDetailsData();
	}
}
