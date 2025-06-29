package com.au.service;

import java.time.Year;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.CustomTemplateDTO;
import com.au.dto.CustomTemplateListDTO;
import com.au.dto.PaginationDTO;
import com.au.model.CustomTemplate;
import com.au.model.CustomTemplateReferenceNumberConfiguration;
import com.au.repository.CustomTemplateReferenceNumberConfigurationRepository;
import com.au.repository.CustomTemplateRepository;
import com.au.response.ResponseHandler;



@Service
public class CustomTemplateService {

	private final ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private CustomTemplateRepository customTemplateRepository;

	@Autowired
	private CustomTemplateReferenceNumberConfigurationRepository customTemplateReferenceNumberConfigurationRepository;

	public ResponseEntity<Object> createCustomTemplate(CustomTemplateDTO customTemplateDTO) {
		try {
			CustomTemplate customTemplate = null;
			if(StringUtils.equals(customTemplateDTO.getTemplateType(), "CUSTOM")) {
			 customTemplate = customTemplateRepository.findByUserCodeAndCategoryTypeIdAndCategoryDetailId(
					customTemplateDTO.getUserCode(), customTemplateDTO.getCategoryTypeId(),
					customTemplateDTO.getCategoryDetailId());
			}
			if (ObjectUtils.isNotEmpty(customTemplate)) {
				return ResponseHandler.generateResponse(true, HttpStatus.BAD_REQUEST, "Template Already Exists", null);
			}

			customTemplate = modelMapper.map(customTemplateDTO, CustomTemplate.class);
			customTemplate.setCustomTemplateId(null);

			String referenceNo = "";
			String formattedNumber = "";
			Integer count = 1;

			CustomTemplateReferenceNumberConfiguration customTemplateReferenceNumberConfiguration = customTemplateReferenceNumberConfigurationRepository
					.findByCurrentYearAndCategoryShortName(Year.now().getValue(),
							customTemplateDTO.getCategoryShortName());
			if (ObjectUtils.isEmpty(customTemplateReferenceNumberConfiguration)) {
				CustomTemplateReferenceNumberConfiguration configuration = new CustomTemplateReferenceNumberConfiguration();
				formattedNumber = String.format("%04d", count);
				configuration.setCurrentYear(Year.now().getValue());
				configuration.setCounter(count);
				configuration.setCategoryShortName(customTemplateDTO.getCategoryShortName());
				customTemplateReferenceNumberConfigurationRepository.save(configuration);
			} else {
				count = customTemplateReferenceNumberConfiguration.getCounter();
				count++;
				formattedNumber = String.format("%04d", count);
				customTemplateReferenceNumberConfiguration.setCounter(count);
				customTemplateReferenceNumberConfiguration
						.setCategoryShortName(customTemplateDTO.getCategoryShortName());
				customTemplateReferenceNumberConfigurationRepository.save(customTemplateReferenceNumberConfiguration);
			}
			int lastTwoDigits = Year.now().getValue() % 100;
			referenceNo = "AU" + "/" + customTemplate.getCategoryShortName() + "/" + lastTwoDigits + "/"
					+ formattedNumber;
			customTemplate.setReferenceNo(referenceNo);

			customTemplateRepository.save(customTemplate);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "Data saved Successfully", null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> getCustomTemplateList(Integer pageNo, Integer pageSize) {
		try {
			Pageable pageable = PageRequest.of(pageNo, pageSize);

			Page<CustomTemplateListDTO> customTemplateListDto = customTemplateRepository
					.getCustomTemplateList(pageable);
			PaginationDTO paginationDTO = new PaginationDTO();
			paginationDTO.setContent(customTemplateListDto.getContent());
			paginationDTO.setPageNo(pageNo);
			paginationDTO.setPageSize(pageSize);
			paginationDTO.setTotalElement(customTemplateListDto.getTotalElements());
			paginationDTO.setTotalPage(customTemplateListDto.getTotalPages());
			paginationDTO.setIslast(customTemplateListDto.isLast());
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "Data Fetched Successfully", paginationDTO);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> getCustomTemplateByReferenceNo(String referenceNo) {
		try {
			CustomTemplate customTemplate = customTemplateRepository.findByReferenceNo(referenceNo);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "Data Fetched Successfully", customTemplate);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

}
