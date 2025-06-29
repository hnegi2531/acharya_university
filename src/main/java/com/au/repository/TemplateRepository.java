package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.Template;

@Transactional
@Repository
public interface TemplateRepository extends JpaRepository<Template, Integer>{

	@Query(value = "select new map(t.template_name as template_name,t.template_short_name as template_short_name,"
			+ "t.template_id as template_id) from Template t where t.template_type_id=?1 and t.active=true")
	public List<HashMap<String, Object>> findTemplateDetails(Integer template_type_id);
	
	@Query(value ="Select new map(t.template_id as id,t.template_name as template_name,t.template_short_name as template_short_name,"
			+ "t.subject as subject,t.body as body,t.template_type_id as template_type_id,t.created_by as created_by,t.modified_by as modified_by,"
			+ "t.created_date as created_date,t.modified_date as modified_date,t.active as active,t.created_username as created_username,"
			+ "t.modified_username as modified_username,tt.template_type_name as template_type_name) From Template t "
			+ "Left join TemplateType tt on tt.template_type_id=t.template_type_id "
			+ "Where CONCAT(IfNull(t.template_id,''),'',IfNull(t.template_name,''),'',IfNull(t.template_short_name,''),'',IfNull(t.created_by,''),'',IfNull(t.created_date,''),'',IfNull(t.created_username,''),'',IfNull(tt.template_type_name,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(t.template_id as id,t.template_name as template_name,t.template_short_name as template_short_name,"
			+ "t.subject as subject,t.body as body,t.template_type_id as template_type_id,t.created_by as created_by,t.modified_by as modified_by,"
			+ "t.created_date as created_date,t.modified_date as modified_date,t.active as active,t.created_username as created_username,"
			+ "t.modified_username as modified_username,tt.template_type_name as template_type_name) From Template t "
			+ "Left join TemplateType tt on tt.template_type_id=t.template_type_id ")
	public Page<Object> findAll3(Pageable pageable);

}
