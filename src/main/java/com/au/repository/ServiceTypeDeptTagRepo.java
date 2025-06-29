package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.au.model.ServiceTypeDeptTags;

public interface ServiceTypeDeptTagRepo extends JpaRepository<ServiceTypeDeptTags, Long> {
	
	Boolean existsByDeptTagIdAndServiceTypeId(Integer dptId, Long serviceTypeId);

	@Query(value = "select new map(stdt.id as Id, stdt.active as Active, stdt.created_username as CreatedUserName, "
			+ " stdt.created_date as CreatedDate,st.serviceTypeName as ServiceTypeName,"
			+ "dt.tag_id as DepartmentTagId, stdt.serviceTypeId as ServiceTypeId, dt.tag_name as DepartmentTagName) "
			+ "from ServiceTypeDeptTags stdt "
			+ "left join ServiceType st on st.id = stdt.serviceTypeId "
			+ "left join DepartmentTags dt on dt.tag_id= stdt.deptTagId "
			+ "where CONCAT(IfNull(stdt.active,''),'',IfNull(stdt.created_username,''),'',IfNull(st.serviceTypeName,''),'',"
			+ "IfNull(dt.tag_name,''),'') LIKE %?1%")
	public Page<Object> findAllServiceDept1(Pageable pageable, Object keyword);

	@Query(value = "select new map(stdt.id as Id, stdt.active as Active, stdt.created_username as CreatedUserName,"
			+ "  stdt.created_date as CreatedDate,st.serviceTypeName as ServiceTypeName, "
			+ "dt.tag_id as DepartmentTagId, stdt.serviceTypeId as ServiceTypeId,  dt.tag_name as DepartmentTagName) "
			+ "from ServiceTypeDeptTags stdt "
			+ "left join ServiceType st on st.id = stdt.serviceTypeId "
			+ "left join DepartmentTags dt on dt.tag_id= stdt.deptTagId")
	public Page<Object> findAllServiceDept2(Pageable pageable);

	@Modifying
	@Transactional
	@Query(value = "update ServiceTypeDeptTags dt set dt.active=:active where dt.id=:id")
	void update(Boolean active, Long id);

	ServiceTypeDeptTags findByDeptTagIdAndServiceTypeId(Integer tagId, Long serviceTypeId);

	@Query(value = "SELECT count(*) FROM service_type_dept_tags where service_type_id=?1 And dept_tag_id in (?2)",nativeQuery = true)
	public Integer findByAcYearSchoolId(Long serviceTypeId, List<Integer> list);


}
