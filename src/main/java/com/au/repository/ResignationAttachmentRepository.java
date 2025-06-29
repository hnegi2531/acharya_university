package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ResignationAttachment;


@Transactional
@Repository
public interface ResignationAttachmentRepository extends JpaRepository<ResignationAttachment, Integer> {
	
	

	@Modifying
	@Query(value = "update ResignationAttachment ra set ra.attachment_path=?2 where ra.resignation_attachment_id in (?1)")
	public void updatePath(List<Integer> resignation_attachment_id, String t2);
	
	@Modifying
	@Query(value = "select resignation_attachment_id from resignation_attachment ra "
			+ " where ra.resignation_id in (?1) and ra.active=true",nativeQuery=true)
	public List<Integer> getResignationAttachment_ids(Integer resignation_id);

		
	@Modifying
	@Query(value = "update ResignationAttachment res set res.active=false where res.resignation_id=?1")
	public void deactivated(Integer id);
	
	@Modifying
	@Query(value = "update ResignationAttachment res set res.active=true where res.resignation_attachment_id=?1")
	public void activated(Integer id);

	
	@Query(value = "select rea.resignation_attachment_id as id,rea.resignation_id as resignation_id,rea.emp_id as emp_id,rea.attachment_name as attachment_name,"
			+ "rea.attachment_path as attachment_path,rea.attachment_type as attachment_type,rea.active as active from resignation_attachment rea "
			+ "where rea.emp_id=?1 And rea.active=true order by rea.created_date desc limit 1",nativeQuery=true)
	public Map<String, Object> getResignationAttachmentBasedOnEmployeeId(Integer emp_id);

}
