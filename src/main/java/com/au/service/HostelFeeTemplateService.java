package com.au.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.HostelFeeTemplateDto;
import com.au.dto.HostelFeeTemplateRequest;
import com.au.exception.ResourceNotFoundException;
import com.au.model.HostelFeeTemplate;
import com.au.model.HostelFeeTemplateSlots;
import com.au.model.HostelHeadWiseAmt;
import com.au.repository.Academic_year_repository;
import com.au.repository.CurrencyTypeRepository;
import com.au.repository.HostelBlocksRepository;
import com.au.repository.HostelFeeTemplateRepository;
import com.au.repository.HostelFeeTemplateSlotsRepository;
import com.au.repository.HostelFloorRepository;
import com.au.repository.HostelHeadWiseAmtRepository;
import com.au.repository.HostelRoomTypeRepository;
import com.au.repository.HostelRoomsRepository;
import com.au.repository.School_Repository;
import com.au.repository.StudentDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelFeeTemplateService {

	@Autowired
	private HostelFeeTemplateRepository hostelFeeTemplateRepository;

	@Autowired
	private Academic_year_repository aca_repo;

	@Autowired
	private CurrencyTypeRepository currency_type_repo;

	@Autowired
	private HostelRoomTypeRepository hostel_room_type_repo;

	@Autowired
	private HostelHeadWiseAmtRepository hostel_head_wise_amt_repo;
	
	@Autowired
	private HostelFeeTemplateSlotsRepository hsotel_fee_template_slot_repo;
	
	@Autowired
	private HostelBlocksRepository hostel_block_repo;
	
	@Autowired
	private HostelFloorRepository hostel_floor_repo;
	
	@Autowired
	private HostelRoomsRepository hostel_floor_room_repo;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private School_Repository sc_repo;
	
	public List<HostelFeeTemplate> listAll() {
		return hostelFeeTemplateRepository.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = hostelFeeTemplateRepository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = hostelFeeTemplateRepository.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public List<HostelFeeTemplate> saveHostelFeeTemplate(HostelFeeTemplateRequest s) {

		List<HostelFeeTemplate> list = new ArrayList<>();

		s.getFee_head_id().stream().forEach(s1 -> {
			HostelFeeTemplate hft = new HostelFeeTemplate();
			hft.setFee_head_id(s1);
			// hft.setTemplate_amount(s.getTemplate_amount().get(Integer.parseInt(s1)));
			// hft.setMinimum_amount(s.getMinimum_amount().get(Integer.parseInt(s1)));

			hft.setActive(s.getActive());
			hft.setTemplate_name(s.getTemplate_name());

			hft.setHostels_block_id(s.getHostels_block_id());
//			hft.setHostels_floor_id(s.getHostels_floor_id());
			hft.setHostel_room_type_id(s.getHostel_room_type_id());
//			hft.setHostels_room_id(s.getHostels_room_id());

			hft.setAc_year_id(s.getAc_year_id());
			hft.setCurrency_type_id(s.getCurrency_type_id());
			hft.setCreatedBy(s.getCreatedBy());
			hft.setCreatedDate(s.getCreatedDate());
			hft.setCreatedUsername(s.getCreatedUsername());
			hft.setSchool_ids(s.getSchool_ids());
			hft.setSchool_name_short(s.getSchool_name_short());
			hft.setRemarks(s.getRemarks());
			list.add(hft);
			hostelFeeTemplateRepository.save(hft);
		});

		/*
		 * 
		 * int fh = s.getFee_head_id().size(); for(int i=0;i<fh;i++) { HostelFeeTemplate
		 * hft = new HostelFeeTemplate(); hft.setFee_head_id(s.getFee_head_id().get(i));
		 * hft.setTemplate_amount(s.getTemplate_amount().get(i));
		 * hft.setMinimum_amount(s.getMinimum_amount().get(i));
		 * hft.setActive(s.getActive()); hft.setTemplate_name(s.getTemplate_name());
		 * 
		 * hft.setHostels_block_id(s.getHostels_block_id());
		 * hft.setHostel_block_shrot(s.getHostel_block_shrot());
		 * hft.setHostels_floor_id(s.getHostels_floor_id());
		 * hft.setHostel_room_type_id(s.getHostel_room_type_id());
		 * hft.setHostels_room_id(s.getHostels_room_id());
		 * 
		 * hft.setAcadamic_year(s.getAcadamic_year());
		 * hft.setCurrency_id(s.getCurrency_id()); hft.setCreatedBy(s.getCreatedBy());
		 * hft.setCreatedDate(s.getCreatedDate());
		 * hft.setCreatedUsername(s.getCreatedUsername());
		 * hft.setSchool_ids(s.getSchool_ids());
		 * hft.setSchool_name_short(s.getSchool_name_short());
		 * hft.setRemarks(s.getRemarks()); list.add(hft); hostelFeeTemplateRepository.save(hft); }
		 */
		return list;
	}

	public List<HostelFeeTemplate> get(Integer id) {
		HostelFeeTemplate hostelFeetemplate= hostelFeeTemplateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelFeeTemplate Not Found:" + id));
		List<HostelFeeTemplate> allFeeTemplate=hostelFeeTemplateRepository.findAll().stream().filter(e -> e.getTemplate_name().equals(hostelFeetemplate.getTemplate_name()) && e.getActive()).collect(Collectors.toList());
		return allFeeTemplate;
	}

	public void delete(Integer id) {
		HostelFeeTemplate hostelFeetemplate=hostelFeeTemplateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("HostelFeeTemplate Not Found:" + id));
		hostelFeeTemplateRepository.updateHostelFeeTemplate(hostelFeetemplate.getTemplate_name());
	}

	public void delete1(Integer id) {
		HostelFeeTemplate hostelFeetemplate=hostelFeeTemplateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("HostelFeeTemplate Not Found:" + id));
		hostelFeeTemplateRepository.updateHostelFeeTemplate1(hostelFeetemplate.getTemplate_name());

	}

	public List<Map<String, Object>> hostelFeeTemplateIndex() {
		return hostelFeeTemplateRepository.hostelFeeTemplateIndex();
	}

	public void saveHostelFeeTemplate1(@Valid List<HostelFeeTemplate> hft) {
		hft.stream().forEach(feeTemplate -> {
				feeTemplate .setSchool_name_short(sc_repo.getSchoolShortNameCommaSeperated(ResponseHandler.toConvertCommaSeperatedIdsAsList(feeTemplate.getSchool_ids())));
				feeTemplate .setHostel_block_short_name(hostel_block_repo.getBlockShortNameCommaSeperated(ResponseHandler.toConvertCommaSeperatedIdsAsList(feeTemplate.getHostels_block_id())));
//			hostel.getHft().setFloor_name(hostel_floor_repo.getFloorShortNameCommaSeperated(ResponseHandler.toConvertCommaSeperatedIdsAsList(hostel.getHft().getHostels_floor_id())));
//			hostel.getHft().setRoom_name(hostel_floor_room_repo.getRoomNameCommaSeperated(ResponseHandler.toConvertCommaSeperatedIdsAsList(hostel.getHft().getHostels_floor_id())));
			});
		hostelFeeTemplateRepository.saveAll(hft);
	}


	public List<HostelFeeTemplate> saveHostelFeeTemplate2(@Valid HostelFeeTemplateDto hostel) {

		 Integer count=hostelFeeTemplateRepository.countTemplateOnAcademicYear(hostel.getHft().get(0).getAc_year_id(),hostel.getHft().get(0).getHostel_room_type_id());
		String aid = aca_repo.fetchAcademicYear(hostel.getHft().get(0).getAc_year_id()).substring(2, 4);
		String currency_id = currency_type_repo.fetchCurrencyShortName(hostel.getHft().get(0).getCurrency_type_id());

		String room_type = null;

		if (hostel.getHft().get(0).getHostel_room_type_id() == 1)
			room_type = "1";
		else if (hostel.getHft().get(0).getHostel_room_type_id() == 2)
			room_type = "2";
		else if (hostel.getHft().get(0).getHostel_room_type_id() == 3)
			room_type = "3";
		else if (hostel.getHft().get(0).getHostel_room_type_id() == 4)
			room_type = "4";
		else if (hostel.getHft().get(0).getHostel_room_type_id() == 5)
			room_type = "5";
		else if (hostel.getHft().get(0).getHostel_room_type_id() == 6)
			room_type = "6";
		else if (hostel.getHft().get(0).getHostel_room_type_id() == 7)
			room_type = "7";
		else if (hostel.getHft().get(0).getHostel_room_type_id() == 8)
			room_type = "8";
		String hostelFeeTemplateName=aid + "OCPT" + room_type + currency_id.substring(0, 1) + (++count);
		hostel.getHft().stream().forEach(feeTemplate -> {
//		hostel.getHft().setTemplate_name(aid + "HOS" + currency_id + (count++) + "OT" + room_type);
			feeTemplate.setTemplate_name(hostelFeeTemplateName);
			feeTemplate .setSchool_name_short(sc_repo.getSchoolShortNameCommaSeperated(ResponseHandler.toConvertCommaSeperatedIdsAsList(feeTemplate.getSchool_ids())));
			feeTemplate .setHostel_block_short_name(hostel_block_repo.getBlockShortNameCommaSeperated(ResponseHandler.toConvertCommaSeperatedIdsAsList(feeTemplate.getHostels_block_id())));
//		hostel.getHft().setFloor_name(hostel_floor_repo.getFloorShortNameCommaSeperated(ResponseHandler.toConvertCommaSeperatedIdsAsList(hostel.getHft().getHostels_floor_id())));
//		hostel.getHft().setRoom_name(hostel_floor_room_repo.getRoomNameCommaSeperated(ResponseHandler.toConvertCommaSeperatedIdsAsList(hostel.getHft().getHostels_floor_id())));
		});
		List<HostelFeeTemplate> h =   hostelFeeTemplateRepository.saveAll(hostel.getHft());
		
//		hostel.getHhwa().stream().forEach(h1->{
//			HostelHeadWiseAmt hostel_amt = new HostelHeadWiseAmt();
//		//	HostelFeeTemplate hft = get(hostel.getHft().getHostel_fee_template_id());
//			System.out.println("ghghghghghghghghghghgh");
//			hostel_amt.setHostel_fee_template_id(h.getHostel_fee_template_id());
//			hostel_amt.setVoucher_head_new_id(h1.getVoucher_head_new_id());
//			hostel_amt.setAmount(h1.getAmount());
//			hostel_amt.setActive(h1.getActive());
//			hostel_amt.setCreatedBy(hostel.getHft().getCreatedBy());
//			hostel_amt.setCreatedUsername(hostel.getHft().getCreatedUsername());
//			hostel_amt.setCreatedBy(hostel.getHft().getCreatedBy());
//			hostel_amt.setCreatedUsername(hostel.getHft().getCreatedUsername());
//			hostel_amt.setActive(hostel.getHft().getActive());	
//			hostel_head_wise_amt_repo.save(hostel_amt);
//		});
//		
//		hostel.getHfts().stream().forEach(h2->{
//			HostelFeeTemplateSlots hfts = new HostelFeeTemplateSlots();
//			
//			hfts.setHostel_fee_template_id(hostel.getHft().getHostel_fee_template_id());
//			hfts.setMinimum_amount(h2.getMinimum_amount());
//			hfts.setDue_date(h2.getDue_date());
//			hfts.setCreatedBy(hostel.getHft().getCreatedBy());
//			hfts.setCreatedUsername(hostel.getHft().getCreatedUsername());
//			hfts.setActive(h2.getActive());	
//			hsotel_fee_template_slot_repo.save(hfts);
//		});
		
		
		return hostel.getHft();
	}
	
	public List<HashMap<String , Object>> getHostelFeeTemplateDetails(Integer hostel_fee_template_id) {
		return hostelFeeTemplateRepository.getHostelFeeTemplateDetails(hostel_fee_template_id);
	}

	public List<HostelFeeTemplate> hostelFeeTemplateByAcademicYearAndSchool(Integer academicYearId, Integer schoolId) {
		return hostelFeeTemplateRepository.hostelFeeTemplateByAcademicYearAndSchool(academicYearId,schoolId);
	}

	public HashMap<String, Object> countOfStudentBasedOnHostelFeeTemplateId(Integer hostel_fee_template_id) {
		Integer count =  hostelFeeTemplateRepository.countOfStudentBasedOnHostelFeeTemplateId(hostel_fee_template_id);
		List<Integer> StudentId = hostelFeeTemplateRepository.getStudentId(hostel_fee_template_id);
		
		
		List<Map<String, Object>> studentDataList = new ArrayList<>();
		HashMap<String, Object> result = new HashMap<>();
		
		StudentId.stream().forEach(sId ->{
			Map<String, Object> stuData = studentDetailsRepository.getData(sId);
			 studentDataList.add(stuData);
		});
		 result.put("count", count);
		 result.put("students", studentDataList);
		    return result;
	}

	public List<HostelFeeTemplate> hostelFeeTemplateByAcademicYearSchoolTemplateId(Integer academicYearId, Integer schoolId, Integer hostelFeeTemplateId) {
		Optional<HostelFeeTemplate> feeTemplate=hostelFeeTemplateRepository.findById(hostelFeeTemplateId);
		return hostelFeeTemplateRepository.hostelFeeTemplateByAcademicYearSchoolTemplateId(academicYearId,schoolId,feeTemplate.get().getTemplate_name());
	}
}
