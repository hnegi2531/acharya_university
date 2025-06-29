package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.FeeTemplateHistory;

@Transactional
@Repository
public interface FeeTemplateHistoryRepository extends JpaRepository<FeeTemplateHistory, Integer> {

	@Query(value = "select new map(fth.fee_template_id as fee_template_id,fth.fee_template_name as fee_template_name,"
			+ "fth.ac_year_id as ac_year_id,fth.school_id as school_id,fth.program_id as program_id,"
			+ "fth.program_specialization_id as program_specialization_id,fth.currency_type_id as currency_type_id,"
			+ "fth.fee_admission_category_id as fee_admission_category_id,fth.fee_admission_sub_category_id as fee_admission_sub_category_id,"
			+ "fth.nationality as nationality,fth.Is_nri as Is_nri,fth.program_type_id as program_type_id,fth.remarks as remarks,"
			+ "fth.modified_date as modified_date,fth.active as active,fth.created_by as created_by,fth.created_username as created_username) "
			+ "from FeeTemplateHistory fth where fth.fee_template_id=?1")
	public List<HashMap<String, Object>> allHistoryDetail(Integer fee_template_id);
	
	@Query(value = "select new map(fth.fee_template_id as id,fth.fee_template_name as fee_template_name,"
			+ "fth.ac_year_id as ac_year_id,fth.school_id as school_id,fth.program_id as program_id,fth.program_specialization as program_specialization,"
			+ "fth.program_specialization_id as program_specialization_id,fth.currency_type_id as currency_type_id,"
			+ "fth.fee_admission_category_id as fee_admission_category_id,fth.fee_admission_sub_category_id as fee_admission_sub_category_id,"
			+ "fth.nationality as nationality,fth.Is_nri as Is_nri,fth.program_type_id as program_type_id,fth.remarks as remarks,"
			+ "fth.modified_date as modified_date,fth.active as active,fth.created_by as created_by,"
			+ "fth.created_username as created_username,sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "ac.ac_year as ac_year,ct.currency_type_name as currency_type_name,fac.fee_admission_category_short_name as fee_admission_category_short_name,"
			+ "ptn.program_type_name as program_type_name,fasc.fee_admission_sub_category_name as fee_admission_sub_category_name) from FeeTemplateHistory fth "
			+ "left join Schools sc on fth.school_id=sc.school_id "
			+ "left join Program pr on fth.program_id=pr.program_id "
			+ "left join Academic_year ac on fth.ac_year_id=ac.ac_year_id "
			+ "left join Currency_Type ct on fth.currency_type_id=ct.currency_type_id "
			+ "left join FeeAdmissionCategory fac on fth.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join ProgramType ptn on fth.program_type_id=ptn.program_type_id "
			+ "left join FeeAdmissionSubCategory fasc on fth.fee_admission_sub_category_id=fasc.fee_admission_sub_category_id where fth.fee_template_id=?1")
	public List<HashMap<String, Object>> allHistoryDetails(Integer fee_template_id);
	
	@Query(value = "select new map(fth.fee_template_id as id,fth.fee_template_name as fee_template_name,"
			+ "fth.ac_year_id as ac_year_id,fth.school_id as school_id,fth.program_id as program_id,"
			+ "fth.program_specialization_id as program_specialization_id,fth.currency_type_id as currency_type_id,"
			+ "fth.fee_admission_category_id as fee_admission_category_id,fth.fee_admission_sub_category_id as fee_admission_sub_category_id,"
			+ "fth.nationality as nationality,fth.Is_nri as Is_nri,fth.program_type_id as program_type_id,fth.remarks as remarks,"
			+ "fth.modified_date as modified_date,fth.active as active,fth.created_by as created_by,"
			+ "fth.created_username as created_username) from FeeTemplateHistory fth "
			+ "Where CONCAT(IfNull(fth.fee_template_id,''),'',IfNull(fth.fee_template_name,''),'',IfNull(fth.nationality,''),'',"
			+ "IfNull(fth.created_username,''),'',IfNull(fth.modified_date,''),'',IfNull(fth.school_id,''),'',"
			+ "IfNull(fth.fee_template_name,''),'',IfNull(fth.program_id,''),'',IfNull(fth.program_type_id,''),'',"
			+ "IfNull(fth.program_specialization_id,''),'',IfNull(fth.fee_admission_category_id,''),'',"
			+ "IfNull(fth.fee_admission_sub_category_id,''),'',IfNull(fth.currency_type_id,'')) LIKE %?1% ")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "select new map(fth.fee_template_id as id,fth.fee_template_name as fee_template_name,"
			+ "fth.ac_year_id as ac_year_id,fth.school_id as school_id,fth.program_id as program_id,"
			+ "fth.program_specialization_id as program_specialization_id,fth.currency_type_id as currency_type_id,"
			+ "fth.fee_admission_category_id as fee_admission_category_id,fth.fee_admission_sub_category_id as fee_admission_sub_category_id,"
			+ "fth.nationality as nationality,fth.Is_nri as Is_nri,fth.program_type_id as program_type_id,fth.remarks as remarks,"
			+ "fth.modified_date as modified_date,fth.active as active,fth.created_by as created_by,"
			+ "fth.created_username as created_username) from FeeTemplateHistory fth ")
	public Page<Object> findAll2(Pageable pageable);
	
	

}
