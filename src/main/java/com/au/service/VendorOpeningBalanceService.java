package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.au.dto.JwtDetails;
import com.au.dto.VendorOpeningBalanceDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.SubjectWorkLoadType;
import com.au.model.VendorOpeningBalance;
import com.au.repository.VendorOpeningBalanceRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class VendorOpeningBalanceService {
	
	@Autowired
	private VendorOpeningBalanceRepository vobr_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	public List<VendorOpeningBalance> saveVendorOpeningBalance(VendorOpeningBalanceDto vobd) {
		
		List<VendorOpeningBalance> list = new ArrayList<>();
		
		vobd.getSchool_id().entrySet().stream().forEach(a -> {
			if(vobr_repo.countSchoolIdAndVendorId(a.getKey(),vobd.getVoucher_head_new_id()) >= 1) {
				throw new RuntimeException("Data with this combination already exist");
			} else {
				
			VendorOpeningBalance vob = new VendorOpeningBalance();
			
			vob.setSchool_id(a.getKey());
			vob.setOpening_balance(a.getValue());
			vob.setVoucher_head_new_id(vobd.getVoucher_head_new_id());
			vob.setActive(vobd.getActive());
			vob.setCreated_username(vobd.getCreated_username());
			vob.setModified_username(vobd.getModified_username());
			vob.setCreated_date(vobd.getCreated_date());
			vob.setModified_date(vobd.getModified_date());
			vob.setCreated_by(vobd.getCreated_by());
			vob.setModified_by(vobd.getModified_by());
			save_VendorOpeningBalance(vob);
			list.add(vob);
			
			}
		});
		return list;
		
	}
	
	
	public VendorOpeningBalance save_VendorOpeningBalance(VendorOpeningBalance vob) {
		return vobr_repo.save(vob);
	}
	
	public List<VendorOpeningBalance> listAll1() {
		return vobr_repo.findAll11();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> vob_filtered_response = vobr_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, vob_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> vob_sorted_response = vobr_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, vob_sorted_response);
	}
	
	public VendorOpeningBalance get(Integer id) {
		return vobr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("VendorOpeningBalance Not Found:" + id));
	}
	
	public VendorOpeningBalance saveVendorOpeningBalance(VendorOpeningBalance vo) {
		return vobr_repo.save(vo);
	}
	
	public void delete(Integer id) {
		VendorOpeningBalance ay = vobr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("VendorOpeningBalance Not Found:" + id));
		vobr_repo.update(id);
	}

	public void delete1(Integer id) {
		VendorOpeningBalance ay = vobr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("VendorOpeningBalance Not Found:" + id));
		vobr_repo.update1(id);
	}
	
	
	public List<VendorOpeningBalance> update_VendorOpeningBalance(List<VendorOpeningBalance> dto,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

		dto.stream().forEach(l1->{
			if(l1.getOb_id() != null) {
				l1.setModified_by(jwtDetails.getUserId());
				l1.setModified_username(jwtDetails.getUserName());
			}else {
				if(vobr_repo.countSchoolIdAndVendorId(l1.getSchool_id(),l1.getVoucher_head_new_id()) >= 1) {
					throw new RuntimeException("Data with this combination already exist");
				} else {
					l1.setCreated_by(jwtDetails.getUserId());
					l1.setCreated_username(jwtDetails.getUserName());
				}
			}
		});
		return 	vobr_repo.saveAll(dto);
	}


		public List<VendorOpeningBalance> getVendorOpeningBalanceByVoucherHeadId(Integer voucher_head_new_id) {
		return vobr_repo.getVendorOpeningBalanceByVoucherHeadId(voucher_head_new_id);
	}

}
