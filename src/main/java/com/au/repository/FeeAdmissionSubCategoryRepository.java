package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.FeeAdmissionSubCategory;

@Transactional
@Repository
public interface FeeAdmissionSubCategoryRepository extends JpaRepository<FeeAdmissionSubCategory, Integer> {

	@Query(value = "SELECT * FROM fee_admission_sub_category where fee_admission_category_id=?1 and active=1", nativeQuery = true)
	public List<FeeAdmissionSubCategory> getFeeAdmissionByFeeAdmissionCategoryId(Integer fee_admission_category_id);

	@Query(value = "select new map(fasc.fee_admission_sub_category_id as id,fasc.approve_intake as approve_intake,"
			+ "fasc.fee_admission_sub_category_name as fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name,"
			+ "fasc.created_date as created_date,fasc.modified_date as modified_date,fasc.created_by as created_by,"
			+ "fasc.modified_by as modified_by,fasc.active as active,fasc.fee_admission_category_id as fee_admission_category_id,"
			+ "fasc.created_username as created_username,fasc.modified_username as modified_username,"
			+ "fac.fee_admission_category_type as fee_admission_category_type,"
			+ "boa.board_unique_short_name as board_unique_short_name) from FeeAdmissionSubCategory fasc "
			+ "left join FeeAdmissionCategory fac on fasc.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join Board boa on fasc.board_unique_id=boa.board_unique_id "
			+ "Where CONCAT(IfNull(fasc.fee_admission_sub_category_id,''),'',IfNull(fasc.fee_admission_sub_category_name,''),'',"
			+ "IfNull(fasc.fee_admission_sub_category_short_name,''),'',IfNull(fasc.created_date,''),'',IfNull(fasc.created_by,''),'',IfNull(fasc.created_username,''),'',IfNull(fac.fee_admission_category_type,''),'',IfNull(boa.board_unique_short_name,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	
	@Query(value = "select new map(fasc.fee_admission_sub_category_id as id,fasc.approve_intake as approve_intake,"
			+ "fasc.fee_admission_sub_category_name as fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name,"
			+ "fasc.created_date as created_date,fasc.modified_date as modified_date,fasc.created_by as created_by,"
			+ "fasc.modified_by as modified_by,fasc.active as active,fasc.fee_admission_category_id as fee_admission_category_id,"
			+ "fasc.created_username as created_username,fasc.modified_username as modified_username,"
			+ "fac.fee_admission_category_type as fee_admission_category_type,"
			+ "boa.board_unique_short_name as board_unique_short_name) from FeeAdmissionSubCategory fasc "
			+ "left join FeeAdmissionCategory fac on fasc.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join Board boa on fasc.board_unique_id=boa.board_unique_id")
	public Page<Object> findAll2(Pageable pageable);

	@Query(value = "select fasc from FeeAdmissionSubCategory fasc where fasc.active=true")
	public List<FeeAdmissionSubCategory> findAll11();

	@Modifying
	@Query(value = "update FeeAdmissionSubCategory fasc set fasc.active=false where fasc.fee_admission_sub_category_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update FeeAdmissionSubCategory fasc set fasc.active=true where fasc.fee_admission_sub_category_id=?1")
	public void update1(Integer id);

	@Query(value = "select count(*) FROM fee_admission_sub_category where fee_admission_sub_category_name=?1 and fee_admission_sub_category_short_name=?2 and fee_admission_category_id=?3 and board_unique_id=?4", nativeQuery = true)
	public Integer getProgramSpecilization(String fee_admission_sub_category_name, String fee_admission_sub_category_short_name,Integer fee_admission_category_id, Integer board_unique_id);

	@Query(value = "select new map(fasc.board_unique_id as board_unique_id,b.board_unique_name as board_unique_name,b.board_unique_short_name as board_unique_short_name) from FeeAdmissionSubCategory fasc left join Board b on fasc.board_unique_id = b.board_unique_id where fasc.fee_admission_sub_category_id=?1 and  fasc.active=true")
	public List<HashMap<String, Object>> getBoardName(Integer fee_admission_sub_category_id);
	
	@Query(value="SELECT COUNT(*) FROM fee_admission_sub_category where fee_admission_sub_category_name=?1 And fee_admission_category_id=?2 "
			+ "And active=true",nativeQuery = true)
	public Integer getFeeAdmissionSubCategoryNameCount(String fee_admission_sub_category_name, Integer fee_admission_category_id);
	
	@Query(value="SELECT COUNT(*) FROM fee_admission_sub_category where fee_admission_sub_category_short_name=?1 And fee_admission_category_id=?2 and active=true",nativeQuery = true)
	public Integer getFeeAdmissionSubCategoryShortNameCount(String fee_admission_sub_category_short_name, Integer fee_admission_category_id);
	
	@Query(value = "SELECT fasc.fee_admission_sub_category_short_name FROM fee_admission_sub_category fasc where fasc.fee_admission_sub_category_id=?1 and fasc.active=1", nativeQuery = true)
	public String getFeeAdmissionSubCategoryShortName(Integer fee_admission_sub_category_id);

	
	@Query(value = "select fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fac.fee_admission_category_id As fee_admission_category_id,"
			+ "fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "fac.fee_admission_category_short_name As fee_admission_category_short_name,fac.fee_admission_category_type As fee_admission_category_type,"
			+ "Concat(IfNull(fac.fee_admission_category_type,''),'-',IfNull(fasc.fee_admission_sub_category_name,'')) as concateFullName,"
			+ "Concat(IfNull(fac.fee_admission_category_short_name,''),'-',IfNull(fasc.fee_admission_sub_category_short_name,'')) as concateShortName "
			+ "from fee_admission_sub_category fasc "
			+ "left join fee_admission_category fac on fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "where fac.active=true And fasc.active=true",nativeQuery = true)
	public List<Map<String, Object>> concateFeeAdmissionSubCategoryDetail();

}
