package com.au.repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

import com.au.model.ServiceType;

@Repository
@Transactional
public interface ServiceTypeRepo extends JpaRepository<ServiceType, Long> {
	
	ServiceType findByServiceTypeName(String serviceTypeName);

	ServiceType findByServiceTypeShortName(String serviceTypeShortName);

	@Modifying
	@Transactional
	@Query(value = "update ServiceType dt set dt.active=:active where dt.id=:id") 
	void update(@Param("active")Boolean active,@Param("id")Long id);

	@Query(value=" SELECT NEW map("
			+ "    st.id AS id,"
			+ "    st.serviceTypeName AS serviceTypeName,"
			+ "    st.serviceTypeShortName AS serviceTypeShortName,"
			+ "    st.created_date AS createdDate,"
			+ "    st.modified_date AS modifiedDate,"
			+ "    st.created_by AS createdBy,"
			+ "    st.modified_by AS modifiedBy,"
			+ "    st.active AS active,"
			+ "    st.showInEvent AS showInEvent,"
			+ "    st.hostelStatus AS hostelStatus,"
			+ "    st.is_attachment AS is_attachment,"
			+ "    st.created_username AS createdUsername,"
			+ "    d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "    st.modified_username AS modifiedUsername ) FROM ServiceType st "
			+ "    left join Department d on st.dept_id = d.dept_id "
			+ "    where CONCAT(IfNull(st.serviceTypeName,''),'',IfNull(st.serviceTypeShortName,''),'', "
			+ "IfNull(st.showInEvent,''),'',IfNull(st.hostelStatus,''),'' )LIKE %?1% ")
	Page<Object> findAll1(Pageable pageable, Object keyword);

	@Query(value=" SELECT NEW map("
			+ "    st.id AS id,"
			+ "    st.serviceTypeName AS serviceTypeName,"
			+ "    st.serviceTypeShortName AS serviceTypeShortName,"
			+ "    st.created_date AS createdDate,"
			+ "    st.modified_date AS modifiedDate,"
			+ "    st.created_by AS createdBy,"
			+ "    st.modified_by AS modifiedBy,"
			+ "    st.active AS active,"
			+ "    st.showInEvent AS showInEvent,"
			+ "    st.hostelStatus AS hostelStatus,"
			+ "    st.is_attachment AS is_attachment,"
			+ "    st.created_username AS createdUsername,"
			+ "    d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "    st.modified_username AS modifiedUsername ) FROM ServiceType st "
			+ "    left join Department d on st.dept_id = d.dept_id ")
	Page<Object> findAll2(Pageable pageable);

	@Query(value = "SELECT st from ServiceType st where st.active=true")
	public List<ServiceType> getAllActiveServiceType();
	
	@Query(value = "SELECT st from ServiceType st where st.active=true And st.showInEvent=true")
	public List<ServiceType> getAllActiveServiceTypeOnlyevent();

	
	@Query(value = "SELECT NEW map(stt.id AS id,st.serviceTypeName AS serviceTypeName,st.serviceTypeShortName AS serviceTypeShortName,"
			+ "stt.created_date AS createdDate,stt.created_by AS createdBy,stt.active AS active,st.showInEvent AS showInEvent,"
			+ "st.hostelStatus AS hostelStatus,d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "stt.complaintDetails as complaintDetails,stt.floorAndExtension as floorAndExtension,stt.ticketStatus as ticketStatus,"
			+ "ua.username as username,ua.usertype as usertype,stt.attendedBy as attendedBy,ua.id as userId,st.id as serviceTypeId) "
			+ "from ServiceTypeTicket stt "
			+ "left join ServiceType st on stt.serviceTypeId = st.id "
			+ "left join UserAuthentication ua on ua.id = stt.userId "
			+ "left join Department d on st.dept_id = d.dept_id "
			+ "where st.active=true And ua.id=?1")
	public List<Map<String, Object>> getAllServiceByUserId(Integer user_id);

	
	@Query(value=" SELECT NEW map("
			+ "    st.id AS id,st.dept_id as st_dept_id,"
			+ "    d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id) FROM ServiceType st "
			+ "    left join Department d on st.dept_id = d.dept_id where st.id=?1 ")
	public List<Map<String, Object>> getAllServiceTypeById(Long id);

	@Query(value=" SELECT st.id AS id,"
			+ "    st.service_type_name AS serviceTypeName,"
			+ "    st.service_type_short_name AS serviceTypeShortName,"
			+ "    st.created_date AS createdDate,"
			+ "    st.modified_date AS modifiedDate,"
			+ "    st.created_by AS createdBy,"
			+ "    st.modified_by AS modifiedBy,"
			+ "    st.active AS active,"
			+ "    st.show_in_event AS showInEvent,"
			+ "    st.hostel_status AS hostelStatus,"
			+ "    st.is_attachment AS is_attachment,"
			+ "    st.created_username AS createdUsername,"
			+ "    d.dept_name as dept_name,d.dept_name_short as dept_name_short,d.dept_id as dept_id,"
			+ "    st.modified_username AS modifiedUsername FROM service_type st "
			+ "    left join department d on st.dept_id = d.dept_id "
			+ "	   where st.dept_id=?1 And st.active=true",nativeQuery = true)
	List<Map<String, Object>> getAllByDeptId(Integer dept_id);



}
