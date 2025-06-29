package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.SmsTemplateFormat;

@Transactional
@Repository
public interface SmsTemplateFormatRepository extends JpaRepository<SmsTemplateFormat ,Integer>{

	
	@Query(value = "SELECT count(*) FROM SmsTemplateFormat stf where stf.template_name=?1 and stf.active=true")
	public Integer countOfTemplateName(String template_name);
	
	@Query(value = "SELECT count(*) FROM SmsTemplateFormat stf where stf.template_type=?1 and stf.active=true")
	public Integer countOfTemplateType(String template_type);
	
	@Query(value="select stf from SmsTemplateFormat stf where stf.active=true")
	public List<SmsTemplateFormat> findAll1();
	

	@Query(value = "Select stf.sms_template_format_id as id,stf.template_name as template_name,"
			+ "stf.template_type as template_type,stf.created_date as created_date,stf.uzbek_content as uzbek_content,"
			+ "stf.modified_date as modified_date,stf.created_by as created_by,stf.modified_by as modified_by,"
			+ "stf.english_content as english_content,stf.created_username as created_username,stf.modified_username as modified_username,"
			+ "stf.active as active,stf.lead_satge as lead_satge,stf.role_id as role_id,stf.user_id as user_id,role.role_name as role_name,"
			+ "(Select GROUP_CONCAT(user_details.username) From user_details Where FIND_IN_SET(user_details.id,stf.user_id)) as user_names,"
			+ "(Select GROUP_CONCAT(program.program_short_name) From program Where FIND_IN_SET(program.program_id,stf.program_id)) as program_short_names From sms_template_format stf "
			+ "Left join roles role On role.role_id=stf.role_id "
			+ "Where CONCAT(IfNull(stf.sms_template_format_id,''),'',IfNull(stf.template_name,''),'',IfNull(stf.template_type,''),'',IfNull(stf.created_date,''),'',IfNull(stf.created_by,''),'',IfNull(stf.created_username,'')) LIKE %?1%",nativeQuery=true)
	public List<Map<String,Object>> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select stf.sms_template_format_id as id,stf.template_name as template_name,"
			+ "stf.template_type as template_type,stf.created_date as created_date,stf.uzbek_content as uzbek_content,"
			+ "stf.modified_date as modified_date,stf.created_by as created_by,stf.modified_by as modified_by,"
			+ "stf.english_content as english_content,stf.created_username as created_username,stf.modified_username as modified_username,"
			+ "stf.active as active,stf.lead_satge as lead_satge,stf.role_id as role_id,stf.user_id as user_id,role.role_name as role_name,"
			+ "(Select GROUP_CONCAT(user_details.username) From user_details Where FIND_IN_SET(user_details.id,stf.user_id)) as user_names,"
			+ "(Select GROUP_CONCAT(program.program_short_name) From program Where FIND_IN_SET(program.program_id,stf.program_id)) as program_short_names From sms_template_format stf "
			+ "Left join roles role On role.role_id=stf.role_id ",nativeQuery=true)
	public List<Map<String,Object>> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM SmsTemplateFormat stf where stf.sms_template_format_id !=?1 And stf.template_name=?2 and stf.active=true")
	public Integer countOfTemplateNameForUpdate(Integer sms_template_format_id,String template_name);
	
	@Query(value = "SELECT count(*) FROM SmsTemplateFormat stf where stf.sms_template_format_id !=?1 And stf.template_type=?2 and stf.active=true")
	public Integer countOfTemplateTypeForUpdate(Integer sms_template_format_id,String template_type);

	@Modifying
	@Query(value = "update SmsTemplateFormat stf set stf.active=false where stf.sms_template_format_id=?1")
	public void update(Integer sms_template_format_id);
	

	@Modifying
	@Query(value = "update SmsTemplateFormat stf set stf.active=true where stf.sms_template_format_id=?1")
	public void update1(Integer sms_template_format_id);
	
	
}
