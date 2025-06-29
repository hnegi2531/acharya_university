package com.au.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.VoucherHeadRequest;
import com.au.exception.ResourceNotFoundException;
import com.au.model.FeeReceipt;
import com.au.model.VoucherHead;
import com.au.repository.AliasNameRepository;
import com.au.repository.FeeReceiptRepository;
import com.au.repository.JournalVoucherRepository;
import com.au.repository.VoucherHeadRepository;
import com.au.response.ResponseHandler;




@Service
public class VoucherHeadService {

	@Autowired
	private VoucherHeadRepository vou_repo;

	@Autowired
	AliasNameRepository a_repo;
	
	@Autowired
	private FeeReceiptRepository frc_repo;
	
	@Autowired
	private JournalVoucherRepository jv_repo;
	
//	public List<HashMap<String, Object>> listAll() {
//		return vou_repo.findAll1();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return vou_repo.findAll1();
		Page<Object> response1 = vou_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		//return vou_repo.findAll1();
		Page<Object> response = vou_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public List<VoucherHead> listAll1() {
		return vou_repo.findAll11();
	}

	public List<Integer> getSchoolByVHead(Integer voucher_head_new_id) {
		return vou_repo.getSchoolByVHead(voucher_head_new_id);
	}

	public List<VoucherHead> getAllVouchers(VoucherHeadRequest vou) {

		List<VoucherHead> list = new ArrayList<VoucherHead>();

		vou.getSchool_id().stream().forEach(a -> {
			List<Integer> schools = findByVouHeadSchoolId2(vou.getVoucher_head_new_id());
			if (schools.contains(a)) {

				System.out.println(a + "= is already exist");
			 throw new RuntimeException("Vocher head on this combination already exist");

			} else {
				VoucherHead vou1 = new VoucherHead();

				vou1.setVoucher_head_new_id(vou.getVoucher_head_new_id());
				vou1.setSchool_id(a);
				vou1.setTally_id(vou.getTally_id());
				vou1.setVoucher_type(vou.getVoucher_type());
				vou1.setBudget_head(vou.getBudget_head());
				vou1.setLedger_id(vou.getLedger_id());
				vou1.setCreated_by(vou.getCreated_by());
				vou1.setActive(vou.getActive());
				vou1.setCreated_username(vou.getCreated_username());
				vou1.setVoucher_priority(vou.getVoucher_priority());
				vou1.setSalary_structure_head_id(vou.getSalary_structure_head_id());
				vou1.setCash_or_bank(vou.getCash_or_bank());
				save_VoucherHead(vou1);
				list.add(vou1);
			}
		});
		return list;
	}

	public VoucherHead save_VoucherHead(VoucherHead voucher) {
		return vou_repo.save(voucher);
	}

	public VoucherHead get(Integer id) {
		return vou_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("VoucherHead Not Found:" + id));
	}

	public void delete(Integer id) {
		VoucherHead ay = vou_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("VoucherHead Not Found:" + id));
		vou_repo.update(id);
	}

	public void delete1(Integer id) {
		VoucherHead ay = vou_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("VoucherHead Not Found:" + id));
		vou_repo.update1(id);
	}

	public HashMap<String, Object> getVoucherBySchoolId(Integer school_id) {
		HashMap<String, Object> response = new HashMap<>();
		response.put("VoucherHeadDetails", vou_repo.getVoucherBySchoolId(school_id));
		response.put("AliasDetails", vou_repo.getAliasDetails());
		return response;
	}
	
	
	public HashMap<String, Object> FetchVoucherHeadBasedOnType() {
		HashMap<String, Object> response = new HashMap<>();
		response.put("VoucherHeadDetails", vou_repo.getVoucherTypeBySchoolId());
		response.put("AliasDetails", vou_repo.getAliasDetails());
		return response;
	}

	public List<Integer> findByVouHeadSchoolId(Integer voucher_head_new_id, String vt) {
		return vou_repo.findByVouHeadSchoolId(voucher_head_new_id, vt);
	}
	
	public List<Integer> findByVouHeadSchoolId2(Integer voucher_head_new_id) {
		return vou_repo.findByVouHeadSchoolId2(voucher_head_new_id);
	}

	public Integer findByVouHeadSchoolId1(Integer voucher_head_new_id, Integer sid) {
		return vou_repo.findByVouHeadSchoolId1(voucher_head_new_id, sid);
	}
	/*
	 * public List<HashMap<String, Object>> getSchoolByVHead123() { // TODO
	 * Auto-generated method stub return vou_repo.getSchoolByVHead123(); }
	 */

	public void update(Integer id, String voucher_type) {
		vou_repo.updatevoucher(id, voucher_type);

	}

	public List<VoucherHead> fetchHostelDetailsBySchoolHostel(Integer school_id) {
		return vou_repo.fetchHostelDetailsBySchoolHostel(school_id);
	}

	public List<HashMap<String, Object>> getAllJournalTypeExceptInflow() {
		return vou_repo.getAllJournalTypeExceptInflow();
	}
	int i =0;
	public List<HashMap<Object, Object>> getDataForDisplayingLedgerAmount(Integer financial_year_id) {
		List<HashMap<Object, Object>> response = new ArrayList<HashMap<Object, Object>>();
		List<VoucherHead> VoucherHeadDetail = vou_repo.getAllVoucherHeadDetail();
		for(i=financial_year_id;i>=financial_year_id-1;i--) {
		VoucherHeadDetail.stream().forEach(vh ->{
			HashMap<Object, Object> hs2 = new HashMap<Object, Object>();
			HashMap<Object, Object> hs3 = new HashMap<Object, Object>();
			HashMap<Object, Object> hs1 = new HashMap<Object, Object>();
			if(vh.getVoucher_type().equalsIgnoreCase("inflow")) {
				
				List<FeeReceipt> receipt_type =	frc_repo.getFeerecepitType(i);
				receipt_type.stream().forEach(ft ->{
					HashMap<Object, Object> hs = new HashMap<Object, Object>();
					
						Double sph_paid_amount =frc_repo.getPaidAmount(i,vh.getVoucher_head_id());
						String paidAmountAsString = (sph_paid_amount != null) ? sph_paid_amount.toString() : "null";
						hs.put(vh.getLedger_id(), paidAmountAsString);
						
						Double bfr_paid_amount =frc_repo.getbulkPaidAmount(i,vh.getVoucher_head_id());
						String bfrpaidAmount = (bfr_paid_amount != null) ? bfr_paid_amount.toString() : "null";
						hs.put(vh.getLedger_id(), bfrpaidAmount);
						hs3.put("inflow", hs);
				});
			
		}else if(vh.getVoucher_type().equalsIgnoreCase("journal")) {
			if(i !=0) {
			String total_amount = jv_repo.getTotalAmount(i);
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +total_amount);
			 BigDecimal totalAmountString = new BigDecimal(total_amount).setScale(2, RoundingMode.HALF_UP);
			 System.out.println("###########################" +totalAmountString);
			hs1.put(vh.getLedger_id(), totalAmountString);
			hs3.put("journal", hs1);
		}
		}
		if(hs3.size() !=0 && hs3 != null) {	 
			hs2.put(i, hs3);
			response.add(hs2);
		}
		});
		}
		financial_year_id=0;
	
		return response;
	}
	
	public List<Object> getInFlowVoucherHead() {
		List<Object> list = vou_repo.getInFlowVoucherHead();
		return list;
	}
	
}
