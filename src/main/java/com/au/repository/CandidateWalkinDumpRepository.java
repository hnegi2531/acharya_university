package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.CandidateWalkinDump;


@Repository
@Transactional
public interface CandidateWalkinDumpRepository extends JpaRepository<CandidateWalkinDump, Integer>{
	
	@Query(value="select cwd from CandidateWalkinDump cwd where cwd.active=true")
	public List<CandidateWalkinDump> findAll1();
	
	@Query(value = "select new map(cwd.candidate_id as id,cwd.username as username,"
			+ "cwd.created_date as created_date,cwd.created_by as created_by,cwd.created_username as created_username,"
			+ "cwd.modified_date as modified_date,cwd.modified_username as modified_username,cwd.active as active,cwd.modified_by as modified_by,"
			+ "cwd.father_mobile as father_mobile,cwd.mother_mobile as mother_mobile,cwd.photo as photo,"
			+ "concat(pr.program_short_name,'-',ps.program_specialization_short_name) as concatprogram,"
			+ "cwd.visitor_id as visitor_id,cwd.candidate_name as candidate_name,cwd.candidate_sex as candidate_sex,cwd.date_of_birth as date_of_birth,"
			+ "cwd.candidate_last_name as candidate_last_name,cwd.mobile_number as mobile_number,"
			+ "cwd.ac_year_id as ac_year_id,cwd.father_name as father_name,cwd.city_id as city_id,"
			+ "cwd.state_id as state_id,cwd.country_id as country_id,cwd.school_id as school_id,cwd.program_id as program_id,"
			+ "cwd.program_specilaization_id as program_specilaization_id,cwd.remarks as remarks,cwd.rep_name as rep_name,"
			+ "cwd.candidate_email as candidate_email,cwd.nationality as nationality,cwd.category as category,"
			+ "cwd.caste as caste,cwd.religion as religion,cwd.place_of_birth as place_of_birth, cwd.blood_group as blood_group,"
			+ "cwd.present_pincode as present_pincode,cwd.present_country as present_country,cwd.present_state as present_state,"
			+ "cwd.permanent_address as permanent_address,cwd.permanent_pincode as permanent_pincode,cwd.permanent_country as permanent_country,"
			+ "cwd.present_address as present_address,cwd.permanent_state as permanent_state,cwd.permanent_city as permanent_city,"
			+ "cwd.father_occupation as father_occupation,"
			+ "cwd.father_email as father_email,cwd.father_qualification as father_qualification,cwd.father_annual_income as father_annual_income,"
			+ "cwd.father_mobile as father_mobile, cwd.mother_name as mother_name,cwd.mother_occupation as mother_occupation) "
			+ "from CandidateWalkinDump cwd left join Program pr on cwd.program_id=pr.program_id "
			+ "left join ProgramSpecilization ps on cwd.program_specilaization_id = ps.program_specialization_id "
            + "where CONCAT(IfNull(cwd.created_date,''),'',IfNull(cwd.candidate_name,''),"
			+ "'',IfNull(cwd.program_assignment_id,''),'',IfNull(cwd.candidate_id,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(cwd.candidate_id as id,cwd.username as username,"
			+ "cwd.created_date as created_date,cwd.created_by as created_by,cwd.created_username as created_username,"
			+ "cwd.modified_date as modified_date,cwd.modified_username as modified_username,cwd.active as active,cwd.modified_by as modified_by,"
			+ "cwd.father_mobile as father_mobile,cwd.mother_mobile as mother_mobile,cwd.photo as photo,"
			+ "concat(pr.program_short_name,'-',ps.program_specialization_short_name) as concatprogram,"
			+ "cwd.visitor_id as visitor_id,cwd.candidate_name as candidate_name,cwd.candidate_sex as candidate_sex,cwd.date_of_birth as date_of_birth,"
			+ "cwd.candidate_last_name as candidate_last_name,cwd.mobile_number as mobile_number,"
			+ "cwd.ac_year_id as ac_year_id,cwd.father_name as father_name,cwd.city_id as city_id,"
			+ "cwd.state_id as state_id,cwd.country_id as country_id,cwd.school_id as school_id,cwd.program_id as program_id,"
			+ "cwd.program_specilaization_id as program_specilaization_id,cwd.remarks as remarks,cwd.rep_name as rep_name,"
			+ "cwd.candidate_email as candidate_email,cwd.nationality as nationality,cwd.category as category,"
			+ "cwd.caste as caste,cwd.religion as religion,cwd.place_of_birth as place_of_birth, cwd.blood_group as blood_group,"
			+ "cwd.present_pincode as present_pincode,cwd.present_country as present_country,cwd.present_state as present_state,"
			+ "cwd.permanent_address as permanent_address,cwd.permanent_pincode as permanent_pincode,cwd.permanent_country as permanent_country,"
			+ "cwd.present_address as present_address,cwd.permanent_state as permanent_state,cwd.permanent_city as permanent_city,"
			+ "cwd.father_occupation as father_occupation,"
			+ "cwd.father_email as father_email,cwd.father_qualification as father_qualification,cwd.father_annual_income as father_annual_income,"
			+ "cwd.father_mobile as father_mobile, cwd.mother_name as mother_name,cwd.mother_occupation as mother_occupation) "
			+ "from CandidateWalkinDump cwd left join Program pr on cwd.program_id=pr.program_id "
			+ "left join ProgramSpecilization ps on cwd.program_specilaization_id = ps.program_specialization_id")
   public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = "update candidate_walkin_dump cwd set cwd.active=false where candidate_id=?1",nativeQuery = true)
	public void updateCandidateWakin_dump(Integer candidate_id);
	
	@Modifying
	@Query(value = "update candidate_walkin_dump cwd set cwd.active=true where candidate_id=?1",nativeQuery = true)
	public void updateCandidateWakin_dump1(Integer candidate_id);
   

}
