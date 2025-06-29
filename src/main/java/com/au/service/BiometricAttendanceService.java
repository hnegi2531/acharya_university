package com.au.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.au.event.BioTransactionEvent;
import com.au.exception.ResourceNotFoundException;
import com.au.model.BiometricAttendance;
import com.au.model.BiometricTransaction;
import com.au.repository.BiometricAttendanceRepository;
import com.au.repository.BiometricTransactionRepository;
import com.au.repository.HolidayCalenderRepository;
import com.au.response.ResponseHandler;
import com.au.util.CsvUtil;

@Service
public class BiometricAttendanceService {

	@Autowired
	private BiometricAttendanceRepository bio_repo;

	@Autowired
	private HolidayCalenderRepository s_repo;

	@Autowired
	private CsvUtil csvUtil;

	@Autowired
	private BiometricTransactionRepository biometricTransactionRepository;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	
	public List<BiometricAttendance> listAll(){
		return bio_repo.findAll();
	}
	
	public BiometricAttendance saveBiometricAttendance(BiometricAttendance bio) throws ParseException {	
		String leave_type_short = s_repo.getleaveType(bio.getDate());
		System.out.println("((((((((((((((((((())))))))))))))))))))) "+leave_type_short);
		String str =	leave_type_short.toString();
		
		DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
		Date d = dateFormat.parse(bio.getStartTime());

		DateFormat dateFormat1 = new SimpleDateFormat("hh:mm:ss");
		Date d1 = dateFormat.parse(bio.getEndTime());
		
		long difference = d1.getTime() - d.getTime();
	        long diffHours = difference / (60 * 60 * 1000);
	        System.out.println("((((((((((((((((((())))))))))))))))))))) "+diffHours);
	        
		      long differenceSeconds = difference / 1000 % 60;
	          long differenceMinutes = difference / (60 * 1000) % 60;
	          long differenceHours = difference / (60 * 60 * 1000) % 24;
	          String timetaken = String.format("%d:%02d:%02d", differenceHours,differenceMinutes,differenceSeconds);
	          System.out.println("3333333333333333333333333333333333333333333 " +timetaken);

		if(str.equalsIgnoreCase("GH")||str.equalsIgnoreCase("WO") ||str.equalsIgnoreCase("DH")
				&& (diffHours>=4 && differenceMinutes>=10)) {
			
			BiometricAttendance ba = new BiometricAttendance();
			ba.setAttendanceStatus(bio.getAttendanceStatus());
			ba.setCompoffDate(bio.getDate());
			ba.setCompoffStatus(true);
			ba.setDate(bio.getDate());
			ba.setEmpId(bio.getEmpId());
			ba.setEndTime(bio.getEndTime());
			ba.setStartTime(bio.getStartTime());
			ba.setPresentStatus(bio.getPresentStatus());
			ba.setRemarks(bio.getRemarks());
			ba.setShiftId(bio.getShiftId());
			ba.setUpdateDate(bio.getUpdateDate());
			ba.setUser_id(bio.getUser_id());
			ba.setDuration(timetaken);

			return bio_repo.save(ba);

		}else {		
			  bio.setDuration(timetaken);
			  	return bio_repo.save(bio);
		}
	}
	
	public BiometricAttendance get(Integer id) {
        return bio_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("BiometricAttendance Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	BiometricAttendance ct =  bio_repo.findById(id)
    	.orElseThrow(()-> new ResourceNotFoundException("BiometricAttendance Not Found:"+id));    	
    	bio_repo.delete(ct);
    }
    
    @Transactional
	public ResponseEntity<Object> uploadBiometricTransactionDetails(MultipartFile file)
			throws ParseException, IOException {
		try {
			List<BiometricTransaction> biometricTransactions = CsvUtil
					.getBiometricTransactiondetailsFromCSV(file.getInputStream());
			biometricTransactionRepository.saveAll(biometricTransactions);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "File saved succssfully", null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}
    
    public List<BiometricAttendance> getBiometricAttendanceByEmp_id(Integer month, Integer year, Integer emp_id) {
		return bio_repo.findByEmpIdAndMonthAndYear(emp_id, month, year);
	}
    
    public BiometricAttendance getBiometricAttendanceDataByEmp_idAndDate(String date, Integer emp_id) {
		return bio_repo.getBiometricAttendanceDataByEmp_idAndDate(date, emp_id);
	}

    public ResponseEntity<Object> bioTransactionTrigger(String date, Integer empId) {
		BioTransactionEvent bioTransactionEvent = new BioTransactionEvent(date, empId);
		applicationEventPublisher.publishEvent(bioTransactionEvent);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "BioTransaction Trigger Started", null);

	}
    
    
}
