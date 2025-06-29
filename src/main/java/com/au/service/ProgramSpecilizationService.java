package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ProgramSpecilization;
import com.au.repository.ProgramSpecilizationRepository;
import com.au.response.ResponseHandler;

@Service
public class ProgramSpecilizationService {

	@Autowired
	private ProgramSpecilizationRepository ps_repo;
	
	public List<Map<String,Object>> listAll() {
		return ps_repo.findAll11();
	}
	
	public ProgramSpecilization save_ProgramSpecilization(@Valid ProgramSpecilization ps) {
		
		if(ps_repo.countOfSpecilizationAuid(ps.getAuid_format())>=1) {
			throw new RuntimeException("AUID Formate Already Exist");
		}else if (ps_repo.getProgramSpecilizationWithName(ps.getSchool_id(), ps.getProgram_id(),ps.getProgram_specialization_name()) >= 1) {
			throw new RuntimeException("Program Specilization Name Already Exist with Combination Of School and Program and Specilization Name");
		}else if (ps_repo.getProgramSpecilizationWithShortName(ps.getSchool_id(), ps.getProgram_id(),ps.getProgram_specialization_short_name()) >= 1) {
			throw new RuntimeException("Program Specilization Name Already Exist with Combination Of School and Program and Specilization Short Name");
		}else if (ps_repo.getProgramSpecilizationWithBoth(ps.getSchool_id(), ps.getProgram_id(),ps.getProgram_specialization_name(),ps.getProgram_specialization_short_name()) >= 1) {
			throw new RuntimeException("Program Specilization Name Already Exist with Combination Of School,Program,Specilization Name and Specilization Short Name");
		}else {
			ps_repo.save(ps);
		}
		return ps;
	}
/*
	public List<ProgramSpecilization> save_ProgramSpecilization(ProgramSpecilizationDto psd, String jwtToken)
			throws Exception {
		List<ProgramSpecilization> list = new ArrayList<ProgramSpecilization>();
	//	if (getProgramSpecilization(psd.getSchool_id(), psd.getDept_id(), psd.getProgram_id(),psd.getAuid_format()) >= 1) {
	//		throw new RuntimeException("ProgramSpecilization already Exist");
	//	} else {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				psd.getProgram_type().keySet().stream().forEach(a -> {
					ProgramSpecilization programSpecilization = new ProgramSpecilization();
					programSpecilization.setProgram_specialization_name(psd.getProgram_specialization_name());
					programSpecilization.setProgram_specialization_short_name(psd.getProgram_specialization_short_name());
					programSpecilization.setAuid_format(psd.getAuid_format());
					programSpecilization.setAc_year_id(psd.getAc_year_id());
					programSpecilization.setSchool_id(psd.getSchool_id());
					programSpecilization.setProgram_id(psd.getProgram_id());
					programSpecilization.setDept_id(psd.getDept_id());
					programSpecilization.setProgram_type(a);
					programSpecilization.setMin_credit(psd.getProgram_type().get(a).getMin_credit());
					programSpecilization.setMax_credit(psd.getProgram_type().get(a).getMax_credit());
					programSpecilization.setActive(psd.getActive());
					programSpecilization.setCreated_by(jwtDetails.getUserId());
					programSpecilization.setCreated_username(jwtDetails.getUserName());
					save_ProgramSpecilization1(programSpecilization);
					list.add(programSpecilization);
				});
			} catch (Exception e) {
			//	if (e.getMessage().contains("auid_format_UNIQUE")) {
			//		throw new Exception("AUID should be unique");
			//	} else {
					e.printStackTrace();
				}
			//}
		return list;
	}
*/
	public ProgramSpecilization get(Integer id) {
		return ps_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProgramSpecilization Not Found:" + id));
	}

	public void delete(Integer id) {
		ProgramSpecilization ay = ps_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProgramSpecilization Not Found:" + id));
		ps_repo.update(id);
	}

	public void delete1(Integer id) {
		ProgramSpecilization ay = ps_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProgramSpecilization Not Found:" + id));
		ps_repo.update1(id);
	}

	public List<ProgramSpecilization> get1() {
		return ps_repo.findAll();
	}

	public List<ProgramSpecilization> findById(Integer id1, Integer id2) {
		return ps_repo.findById1(id1, id2);
	}

	public Integer countRecords(Integer id) {
		return ps_repo.findById2(id);
	}

//	public Integer getProgramSpecilization(Integer school_id, Integer program_id) {
//		return ps_repo.getProgramSpecilization(school_id, program_id);
//	}
	
	public Integer getProgramSpecilization1(Integer school_id,Integer dept_id, Integer program_id,String auid_format) {
		return ps_repo.getProgramSpecilization1(school_id, dept_id,program_id,auid_format);
	}

	public String getProgramAuid(Integer ps_id) {
		return ps_repo.getProgramAuid(ps_id);
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return ps_repo.findAll1();
		List<Map<String, Object>> response1 = ps_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		//return ps_repo.findAll2();
		List<Map<String, Object>> response = ps_repo.findAll2(pageable);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}

	public ProgramSpecilization save_ProgramSpecilization1(ProgramSpecilization p) {

		if(ps_repo.countOfSpecilizationAuidForUpdate(p.getProgram_specialization_id(),p.getAuid_format())>=1) {
			throw new RuntimeException("AUID Format Already Exist");
		}else if (ps_repo.countProgramSpecilizationNameForUpdate(p.getProgram_specialization_id(),p.getSchool_id(), p.getProgram_id(), p.getProgram_specialization_name()) >= 1) {
			throw new RuntimeException("ProgramSpecilization Name Already Exist with Combination Of School, Program and Specilization Name");
		} else if (ps_repo.countProgramSpecilizationShortNameForUpdate(p.getProgram_specialization_id(),p.getSchool_id(), p.getProgram_id(), p.getProgram_specialization_short_name()) >= 1) {
			throw new RuntimeException("ProgramSpecilization Name Already Exist with Combination Of School, Program And and Specilization Short Name");
		}else if (ps_repo.countProgramSpecilizationBothForUpdate(p.getProgram_specialization_id(),p.getSchool_id(), p.getProgram_id(), p.getProgram_specialization_name(), p.getProgram_specialization_short_name()) >= 1) {
			throw new RuntimeException("ProgramSpecilization Name Already Exist with Combination Of School, Program, Specilization Name and Specilization Short Name");
		}else {
			return ps_repo.save(p);
		}

	}

	public List<ProgramSpecilization> getAllCourseDetail(Integer school_id, Integer program_id, Integer dept_id) {
		return ps_repo.getAllCourseDetailss(school_id,program_id,dept_id);
	}

}
