package com.au.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.AllLibraryBookWithAccessionNumberDTO;
import com.au.dto.LibraryBookIssueDTO;
import com.au.dto.LibraryInventoryDTO;
import com.au.model.LibraryInventory;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.LibraryAssignmentRepository;
import com.au.repository.LibraryBookIssueRepository;
import com.au.repository.LibraryInventoryRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class LibraryInventoryService {

	private final ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private LibraryInventoryRepository libraryInventoryRepository;
	
	@Autowired
	private LibraryAssignmentRepository libraryAssignmentRepository;
	
	@Autowired
	private LibraryBookIssueRepository libraryBookIssueRepository;

	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;
	
	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	
	public ResponseEntity<Object> saveLibraryInventory(LibraryInventoryDTO libraryInventoryDto) {
		try {
			Random random = new Random();

			int randomNumber = random.nextInt(9000) + 1000;
			LibraryInventory libraryInventory = modelMapper.map(libraryInventoryDto, LibraryInventory.class);
			libraryInventory.setUid(libraryInventory.getCategory() + "-" + randomNumber);
			libraryInventoryRepository.save(libraryInventory);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "Data saved Successfully", null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}
	
	
	public ResponseEntity<Object> getAllLibraryBooksWithAccessionNumber() {
		try {
			List<AllLibraryBookWithAccessionNumberDTO> allLibraryBookWithAccessionNumberDTO = libraryAssignmentRepository
					.getAllLiraryBooksWithAccessioNumber();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
					allLibraryBookWithAccessionNumberDTO);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}
	}


	public ResponseEntity<Object> getAllLibraryBooksIssue(Integer issuerId) {
		try {
			List<LibraryBookIssueDTO> libraryBookIssues = libraryBookIssueRepository.getAllLibraryBooksIssue(issuerId);
			libraryBookIssues.stream().forEach(t -> {
				int daysToIncrement = 0;
				Double fine = null;
				if (ObjectUtils.isNotEmpty(t.getDueDate())) {
					daysToIncrement = Integer.parseInt(t.getDueDate());
				}
				if (daysToIncrement > 0) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(t.getCheckOutTime());
					calendar.add(Calendar.DATE, daysToIncrement);
					Date currentDate = new Date();
					Date checkInDate = calendar.getTime();
					long difference_In_Time = currentDate.getTime() - calendar.getTime().getTime();
					long daysDifference = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
					t.setCheckInTime(checkInDate);

					if (daysDifference > 1) {
						fine = CalculateFine(daysDifference, t.getFinePerDay());
						libraryBookIssueRepository.updateFine(checkInDate, fine, t.getIssuerId(),
								t.getLibraryAssignmentId(), t.getAccessionNumber());
						t.setIssuerFine(fine);
					}

				}

			});
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", libraryBookIssues);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}
	}
	
	private double CalculateFine(long daysDifference, Double double1) {
		return daysDifference * double1;
	}
}
