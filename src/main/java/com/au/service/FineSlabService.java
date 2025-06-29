package com.au.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.FineSlabDTO;
import com.au.model.FineSlab;
import com.au.repository.FineSlabRepository;
import com.au.response.ResponseHandler;

@Service
public class FineSlabService {

	@Autowired
	private FineSlabRepository fineSlabRepository;

	public ResponseEntity<Object> createFineSlab(FineSlabDTO fineSlabDTO) {
		try {

			FineSlab fineSlab = fineSlabRepository.getFineSlabByWeek(fineSlabDTO.getWeek());
			FineSlab newFineSlab=null;
			if (ObjectUtils.isNotEmpty(fineSlab)) {
				Date dateForTillDate =new Date();
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, -1);
                calendar.setTime(dateForTillDate);
                String tillDate=simpleDateFormat.format(calendar.getTime());
                fineSlab.setTillDate(tillDate);
                fineSlabRepository.save(fineSlab);
                Date dateForFromDate =new Date();
				newFineSlab = new FineSlab();
				newFineSlab.setTillDate(null);
			    String formattedDate=simpleDateFormat.format(dateForFromDate);
				newFineSlab.setFromDate(formattedDate);
				newFineSlab.setSchoolId(fineSlabDTO.getSchoolId());
				newFineSlab.setWeek(fineSlabDTO.getWeek());
				newFineSlab.setPercentage(fineSlabDTO.getPercentage());
				fineSlabRepository.save(newFineSlab);
				
			}else {
				fineSlab = new FineSlab();
				fineSlab.setTillDate(null);
				Date date =new Date();
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
				String formattedDate=simpleDateFormat.format(date);
				fineSlab.setFromDate(formattedDate);
				fineSlab.setSchoolId(fineSlabDTO.getSchoolId());
				fineSlab.setWeek(fineSlabDTO.getWeek());
				fineSlab.setPercentage(fineSlabDTO.getPercentage());
				fineSlabRepository.save(fineSlab);
			}
			

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> getFineSlab() {
		try {

			List<FineSlabDTO> fineSlabDTO = fineSlabRepository.getAllFines();

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", fineSlabDTO);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> getFineSlabById(Integer fineSlabId) {
		try {

			FineSlabDTO fineSlabDTO = fineSlabRepository.getFineSlabById(fineSlabId);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", fineSlabDTO);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> updateFineSlab(FineSlabDTO fineSlabDTO) {
		try {

			FineSlab fineSlab=fineSlabRepository.findById(fineSlabDTO.getFineSlabId()).get();
			if(ObjectUtils.isNotEmpty(fineSlab)) {
				fineSlab.setSchoolId(fineSlabDTO.getSchoolId());
				fineSlab.setWeek(fineSlabDTO.getWeek());
				fineSlab.setPercentage(fineSlabDTO.getPercentage());
				fineSlabRepository.save(fineSlab);	
			}
			
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", fineSlabDTO);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

}
