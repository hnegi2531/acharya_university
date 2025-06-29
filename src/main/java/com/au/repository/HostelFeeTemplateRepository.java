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

import com.au.model.HostelFeeTemplate;

@Repository
@Transactional
public interface HostelFeeTemplateRepository extends JpaRepository<HostelFeeTemplate, Integer> {
	@Query(value = "select hft.* from hostel_fee_template hft  " +
			"where hft.active=true and ac_year_id = (select (max(ac_year_id)) from hostel_fee_template ) ", nativeQuery = true)
	public List<HostelFeeTemplate> findAll1();

	@Modifying
	@Query(value = "update HostelFeeTemplate hft set hft.active=false where hft.template_name=?1")
	public void updateHostelFeeTemplate(String template_name);

	@Modifying
	@Query(value = "update HostelFeeTemplate hft set hft.active=true where hft.template_name=?1")
	public void updateHostelFeeTemplate1(String template_name);

	@Query(value = "SELECT hft.school_name_short,hft.created_username,hft.created_date,hft.created_by, hft.active,hft.hostel_fee_template_id,hrt.room_type,ay.ac_year, hft.template_name,hft.template_amount FROM hostel_fee_template hft\r\n"
			+ "left join academic_year ay on ay.ac_year_id=hft.ac_year_id\r\n"
			+ "left join au_std_hostel_room_type hrt on hrt.room_type_id=hft.hostel_room_type_id",nativeQuery = true)
	public List<Map<String, Object>> hostelFeeTemplateIndex();
	
	
	@Query(value = "SELECT ay.ac_year FROM hostel_fee_template hft  left join "
			+ "academic_year ay on hft.ac_year_id = ay.ac_year_id "
			+ "where hft.ac_year_id=?1",nativeQuery = true)
	public String fetchAcademicYear(Integer ac_year_id);	
	
	@Query(value = "SELECT c.currency_type_short_name FROM hostel_fee_template hft  left join "
			+ "currency_type c on hft.currency_type_id = c.currency_type_id "
			+ "where hft.currency_type_id=?1",nativeQuery = true)
	public String fetchCurrency(Integer currency_type_id);
	
	@Query(value = "Select new map(hft.hostel_fee_template_id as id,hft.template_name as template_name,hft.hostels_block_id as hostels_block_id,"
            + "hft.hostel_room_type_id as hostel_room_type_id,hft.total_amount as total_amount,hft.advance_amount as advance_amount,"
            + "hft.ac_year_id as ac_year_id,hft.fee_type as fee_type,hft.currency_type_id as currency_type_id,hft.standardAccessoriesId as standardAccessoriesId,"
            + "hft.template_amount as template_amount,hft.common_acessories as common_acessories,hft.fee_head_id as fee_head_id,"
            + "hft.remarks as remarks,hft.school_ids as school_ids,hft.school_name_short as school_name_short,hft.minimum_amount as minimum_amount,"
            + "hft.createdDate as createdDate,hft.modifiedDate as modifiedDate,hft.createdBy as createdBy,hft.modifiedBy as modifiedBy,"
            + "hft.createdUsername as createdUsername,hft.modifiedUsername as modifiedUsername,hft.active as active,"
            + "hbl.blockShortName as blockShortName,hrt.roomType as roomType,COALESCE(count(hba.hostelFeeTemplate.hostel_fee_template_id), 0) as count,"  
            + "ct.currency_type_short_name as currency_type_short_name,shda.standardAccessories as standardAccessories,fh.fee_head as fee_head,"
            + "ay.ac_year as ac_year,hft.hostel_block_short_name as hostel_block_short_name,hft.offer_status as offer_status) "
            + "From HostelFeeTemplate hft "
            + "Left Join Academic_year ay on ay.ac_year_id=hft.ac_year_id  "
            + "Left join HostelBedAssignment hba on hba.hostelFeeTemplate.hostel_fee_template_id= hft.hostel_fee_template_id "
            + "Left Join HostelBlocks hbl on hbl.hostelBlockId=hft.hostels_block_id "
            + "Left Join HostelRoomType hrt on hrt.roomTypeId=hft.hostel_room_type_id "
            + "Left Join Currency_Type ct on ct.currency_type_id=hft.currency_type_id "
            + "Left Join StdHostelStandardAccessories shda on shda.standardAccessoriesId=hft.standardAccessoriesId "
            + "Left Join FeeHead fh on fh.fee_head_id=hft.fee_head_id "
			+ "Where CONCAT(IfNull(hft.hostel_fee_template_id,''),'',IfNull(hft.template_name,''),'',IfNull(hbl.blockShortName,''),'',"
			+ "IfNull(hrt.roomType,''),'',IfNull(ct.currency_type_short_name,''),'',IfNull(shda.standardAccessories,''),'',"
			+ "IfNull(fh.fee_head,''),'',IfNull(hft.createdDate,''),'',IfNull(hft.createdBy,''),'',IfNull(hft.createdUsername,'')) LIKE %?1% group by hft.template_name")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(hft.hostel_fee_template_id as id,hft.template_name as template_name,hft.hostels_block_id as hostels_block_id,"
            + "hft.hostel_room_type_id as hostel_room_type_id,hft.total_amount as total_amount,hft.advance_amount as advance_amount,"
            + "hft.ac_year_id as ac_year_id,hft.fee_type as fee_type,hft.currency_type_id as currency_type_id,hft.standardAccessoriesId as standardAccessoriesId,"
            + "hft.template_amount as template_amount,hft.common_acessories as common_acessories,hft.fee_head_id as fee_head_id,"
            + "hft.remarks as remarks,hft.school_ids as school_ids,hft.school_name_short as school_name_short,hft.minimum_amount as minimum_amount,"
            + "hft.createdDate as createdDate,hft.modifiedDate as modifiedDate,hft.createdBy as createdBy,hft.modifiedBy as modifiedBy,"
            + "hft.createdUsername as createdUsername,hft.modifiedUsername as modifiedUsername,hft.active as active,"
            + "hbl.blockShortName as blockShortName,hrt.roomType as roomType,COALESCE(count(hba.hostelFeeTemplate.hostel_fee_template_id), 0) as count,"  
            + "ct.currency_type_short_name as currency_type_short_name,shda.standardAccessories as standardAccessories,fh.fee_head as fee_head,"
            + "ay.ac_year as ac_year,hft.hostel_block_short_name as hostel_block_short_name,hft.offer_status as offer_status) "
            + "From HostelFeeTemplate hft "
            + "Left Join Academic_year ay on ay.ac_year_id=hft.ac_year_id  "
            + "Left join HostelBedAssignment hba on hba.hostelFeeTemplate.hostel_fee_template_id= hft.hostel_fee_template_id "
            + "Left Join HostelBlocks hbl on hbl.hostelBlockId=hft.hostels_block_id "
            + "Left Join HostelRoomType hrt on hrt.roomTypeId=hft.hostel_room_type_id "
            + "Left Join Currency_Type ct on ct.currency_type_id=hft.currency_type_id "
            + "Left Join StdHostelStandardAccessories shda on shda.standardAccessoriesId=hft.standardAccessoriesId "
            + "Left Join FeeHead fh on fh.fee_head_id=hft.fee_head_id group by hft.template_name")
public Page<Object> findAll3(Pageable pageable);
	
	
//	@Query(value = "SELECT new map(hft.hostel_fee_template_id as id, hft.template_name as template_name, hft.hostels_block_id as hostels_block_id, " +
//	        "hft.hostel_room_type_id as hostel_room_type_id, hft.total_amount as total_amount, hft.advance_amount as advance_amount, " +
//	        "hft.ac_year_id as ac_year_id, hft.fee_type as fee_type, hft.currency_type_id as currency_type_id, hft.standardAccessoriesId as standardAccessoriesId, " +
//	        "hft.template_amount as template_amount, hft.common_acessories as common_acessories, hft.fee_head_id as fee_head_id, " +
//	        "hft.remarks as remarks, hft.school_ids as school_ids, hft.school_name_short as school_name_short, hft.minimum_amount as minimum_amount, " +
//	        "hft.createdDate as createdDate, hft.modifiedDate as modifiedDate, hft.createdBy as createdBy, hft.modifiedBy as modifiedBy, " +
//	        "hft.createdUsername as createdUsername, hft.modifiedUsername as modifiedUsername, hft.active as active, " +
//	        "hbl.blockShortName as blockShortName, hrt.roomType as roomType, " +
//	        "(SELECT COALESCE(COUNT(hba.student), 0) FROM HostelBedAssignment hba WHERE hba.hostelFeeTemplate = hft.hostel_fee_template_id) as count, " + 
//	        "ct.currency_type_short_name as currency_type_short_name, shda.standardAccessories as standardAccessories, fh.fee_head as fee_head, " +
//	        "ay.ac_year as ac_year, hft.hostel_block_short_name as hostel_block_short_name, hft.offer_status as offer_status) " +
//	        "FROM HostelFeeTemplate hft " +
//	        "LEFT JOIN Academic_year ay on ay.ac_year_id = hft.ac_year_id " +
//	        "LEFT JOIN HostelBlocks hbl on hbl.hostelBlockId = hft.hostels_block_id " +
//	        "LEFT JOIN HostelRoomType hrt on hrt.roomTypeId = hft.hostel_room_type_id " +
//	        "LEFT JOIN Currency_Type ct on ct.currency_type_id = hft.currency_type_id " +
//	        "LEFT JOIN StdHostelStandardAccessories shda on shda.standardAccessoriesId = hft.standardAccessoriesId " +
//	        "LEFT JOIN FeeHead fh on fh.fee_head_id = hft.fee_head_id " +
//	        "GROUP BY hft.template_name")
//	public Page<Object> findAll3(Pageable pageable);

	
	@Query(value = "SELECT count(distinct hft.template_name) FROM HostelFeeTemplate hft where hft.ac_year_id=?1 and hft.hostel_room_type_id=?2 And hft.active=true")
	public Integer countTemplateOnAcademicYear(Integer ac_year_id,Integer hostel_room_type_id);
	
	@Query(value = "Select new map(hft.hostel_fee_template_id as hostel_fee_template_id,hft.template_name as template_name,hft.hostels_block_id as hostels_block_id,"
			+ "hft.hostel_room_type_id as hostel_room_type_id,hft.total_amount as total_amount,hft.advance_amount as advance_amount,"
			+ "hft.ac_year_id as ac_year_id,hft.fee_type as fee_type,hft.currency_type_id as currency_type_id,hft.standardAccessoriesId as standardAccessoriesId,"
			+ "hft.template_amount as template_amount,hft.common_acessories as common_acessories,hft.fee_head_id as fee_head_id,"
			+ "hft.remarks as remarks,hft.school_ids as school_ids,hft.school_name_short as school_name_short,hft.minimum_amount as minimum_amount,"
			+ "hft.createdDate as createdDate,hft.modifiedDate as modifiedDate,hft.createdBy as createdBy,hft.modifiedBy as modifiedBy,"
			+ "hft.createdUsername as createdUsername,hft.modifiedUsername as modifiedUsername,hft.active as active,"
			+ "hbl.blockShortName as blockShortName,hrt.roomType as roomType,"
			+ "ct.currency_type_short_name as currency_type_short_name,shda.standardAccessories as standardAccessories,fh.fee_head as fee_head,"
			+ "ay.ac_year as ac_year,hft.hostel_block_short_name as hostel_block_short_name) From HostelFeeTemplate hft "
			+ "Left Join Academic_year ay on ay.ac_year_id=hft.ac_year_id  "
			+ "Left Join HostelBlocks hbl on hbl.hostelBlockId=hft.hostels_block_id "
			+ "Left Join HostelRoomType hrt on hrt.roomTypeId=hft.hostel_room_type_id "
			+ "Left Join Currency_Type ct on ct.currency_type_id=hft.currency_type_id "
			+ "Left Join StdHostelStandardAccessories shda on shda.standardAccessoriesId=hft.standardAccessoriesId "
			+ "Left Join FeeHead fh on fh.fee_head_id=hft.fee_head_id where hft.hostel_fee_template_id=?1 and hft.active=true")
	public List<HashMap<String , Object>> getHostelFeeTemplateDetails(Integer hostel_fee_template_id);
	
	@Query(value = "SELECT hft FROM HostelFeeTemplate hft where hft.ac_year_id=?1 and hft.hostel_room_type_id=?2 And hft.active=true")
	public List<HostelFeeTemplate> templateOnAcademicYearAndRoomTypeId(Integer ac_year_id, Integer hostel_room_type_id);

	@Query(value = "SELECT hft FROM HostelFeeTemplate hft where hft.ac_year_id=:academicYearId and FIND_IN_SET(:schoolId,hft.school_ids) > 0 And hft.active=true group by hft.template_name ")
	public List<HostelFeeTemplate> hostelFeeTemplateByAcademicYearAndSchool(Integer academicYearId, Integer schoolId);
	
	
	@Query(value = "Select new map(hft.template_name as template_name, hft.hostel_fee_template_id as hostel_fee_template_id, hft.template_amount as template_amount,"
			+ "vhn.voucher_head_new_id as voucher_head_new_id,hft.minimum_amount as minimum_amount,hft.total_amount as total_amount,hft.advance_amount as advance_amount,"
			+ "vhn.voucher_head as voucher_head) From HostelFeeTemplate hft "
			+ "Left Join VoucherHeadNew vhn on vhn.voucher_head_new_id=hft.fee_head_id where hft.template_name=?1 and hft.active=true")
	public List<HashMap<String, Object>> feeTemplateDeatilsByTemplateName(String template_name);

	@Query(value = "SELECT count(hba.student_id) FROM hostel_fee_template hft "
			+ "left join hostel_bed_assignment hba on hba.hostel_fee_template_id= hft.hostel_fee_template_id "
			+ "where hft.hostel_fee_template_id=?1 And hft.active=true",nativeQuery = true)
	public Integer countOfStudentBasedOnHostelFeeTemplateId(Integer hostel_fee_template_id);
	
	@Query(value = "SELECT hba.student_id FROM hostel_fee_template hft "
			+ "left join hostel_bed_assignment hba on hba.hostel_fee_template_id= hft.hostel_fee_template_id "
			+ "where hft.hostel_fee_template_id=?1 And hft.active=true",nativeQuery = true)
	public List<Integer> getStudentId(Integer hostel_fee_template_id);

	@Query(value = "SELECT hft FROM HostelFeeTemplate hft where hft.ac_year_id=:academicYearId and FIND_IN_SET(:schoolId,hft.school_ids) > 0 And hft.template_name=:template_name And hft.active=true")
    List<HostelFeeTemplate> hostelFeeTemplateByAcademicYearSchoolTemplateId(Integer academicYearId, Integer schoolId, String template_name);
}
