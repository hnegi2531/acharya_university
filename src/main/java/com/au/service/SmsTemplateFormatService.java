package com.au.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;

import com.au.model.SmsTemplateFormat;
import com.au.repository.SmsTemplateFormatRepository;
import com.au.response.ResponseHandler;

@Service
public class SmsTemplateFormatService {
	
	
	@Autowired
	private SmsTemplateFormatRepository sms_template_format_repo;
	
	
	public SmsTemplateFormat saveSmsTemplateFormat(SmsTemplateFormat stf) throws Exception {

		if(sms_template_format_repo.countOfTemplateName(stf.getTemplate_name())>=1) {
			throw new Exception("Template Name Already exist");
		}else if(sms_template_format_repo.countOfTemplateType(stf.getTemplate_type())>=1) {
			throw new Exception("Template Type Already exist");
		}else {
			return	sms_template_format_repo.save(stf);
		}
	}

	

	public List<SmsTemplateFormat> listAll() {
		return sms_template_format_repo.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		List<Map<String,Object>> response1 = sms_template_format_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		List<Map<String,Object>> response =sms_template_format_repo.findAll3(pageable);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}
	
	public SmsTemplateFormat get(Integer id) {
		return sms_template_format_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Template Format Not Found: " + id));
	}

	

	public SmsTemplateFormat updateSmsTemplateFormat(SmsTemplateFormat stf) throws Exception {
		if(sms_template_format_repo.countOfTemplateNameForUpdate(stf.getSms_template_format_id(),stf.getTemplate_name())>=1) {
			throw new Exception("Template Name Already exist");
		}else if(sms_template_format_repo.countOfTemplateTypeForUpdate(stf.getSms_template_format_id(),stf.getTemplate_type())>=1) {
			throw new Exception("Template Type Already exist");
		}else {
			return	sms_template_format_repo.save(stf);
		}
	}

	public void delete(Integer id) {
		sms_template_format_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Template Format Not Found: " + id));
		sms_template_format_repo.update(id);
	}

	public void delete1(Integer id) {
		sms_template_format_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Template Format Not Found: " + id));
		sms_template_format_repo.update1(id);
	}

}
