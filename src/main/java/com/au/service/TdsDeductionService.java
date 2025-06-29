package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.JwtDetails;
import com.au.dto.PaginationDTO;
import com.au.dto.TdsDeductionDTO;
import com.au.model.TdsDeduction;
import com.au.repository.TdsDeductionRepository;
import com.au.response.ResponseHandler;
import com.au.util.CsvUtil;





@Service
public class TdsDeductionService {
	
	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private TdsDeductionRepository tdsDeductionRepository;


	public ResponseEntity<Object> uploadTdsDeductionFile(MultipartFile file, Integer month, Integer year,
			String jwtToken) {
		try {
			if (!CsvUtil.hasCSVFormat(file)) {
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "File is not in CSV Format", null);
			}
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			List<TdsDeduction> tdsList = CsvUtil
					.getDataForTdsDeduction(file.getInputStream(), month, year, jwtDetails);
			
			tdsDeductionRepository.saveAll(tdsList);
		

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "File Saved", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}

	}


	public ResponseEntity<Object> saveTds(TdsDeductionDTO tdsDeductionDTO, String jwtToken) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			
			boolean isExists=tdsDeductionRepository.existsByEmpCodeAndMonthAndYear(tdsDeductionDTO.getEmpCode(), tdsDeductionDTO.getMonth(), tdsDeductionDTO.getYear());
			if(isExists) {
				return ResponseHandler.generateResponse(false, HttpStatus.OK, "Data Already exists", null);

			}
			
			TdsDeduction tdsDeduction=new TdsDeduction();
			
			tdsDeduction.setAmount(tdsDeductionDTO.getAmount());
			tdsDeduction.setEmpCode(tdsDeductionDTO.getEmpCode());
			tdsDeduction.setEmployeeName(tdsDeductionDTO.getEmployeeName());
			tdsDeduction.setMonth(tdsDeductionDTO.getMonth());
			tdsDeduction.setYear(tdsDeductionDTO.getYear());
			tdsDeduction.setCreatedBy(jwtDetails.getUserId());
			tdsDeductionRepository.save(tdsDeduction);
		

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "File Saved", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}

	}


	public ResponseEntity<Object> getTdsList(Integer page, Integer pageSize) {
		try {
			Pageable pageable=PageRequest.of(page, pageSize);
			Page<TdsDeduction> tdsPage=tdsDeductionRepository.getAllTds(pageable);
			PaginationDTO paginationDTO=new PaginationDTO();
		    paginationDTO.setContent(tdsPage.getContent());
		    paginationDTO.setPageNo(page);
		    paginationDTO.setPageSize(pageSize);
		    paginationDTO.setTotalElement(tdsPage.getTotalElements());
		    paginationDTO.setTotalPage(tdsPage.getTotalPages());
		    paginationDTO.setIslast(tdsPage.isLast());
		

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", paginationDTO);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

}
