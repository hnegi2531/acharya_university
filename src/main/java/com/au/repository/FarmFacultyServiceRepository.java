package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.FarmFacultyService;

@Repository
@Transactional
public interface FarmFacultyServiceRepository extends JpaRepository<FarmFacultyService, Integer> {
	
	@Query(value = "select ffs from FarmFacultyService ffs where ffs.active=true")
	public List<FarmFacultyService> findAll1();
	
	@Query("SELECT new map("
	        + "ffs.facultyServiceId AS id, "
	        + "ffs.empId AS empId, "
	        + "ffs.month AS month, "
	        + "ffs.year AS year, "
	        + "ffs.universityColgAchieved AS universityColgAchieved, "
	        + "ffs.universityColgProposed AS universityColgProposed, "
	        + "ffs.universityColgMaxMarks AS universityColgMaxMarks, "
	        + "ffs.universityColgSelfMarks AS universityColgSelfMarks, "
	        + "ffs.administrativeColgAchieved AS administrativeColgAchieved, "
	        + "ffs.administrativeColgProposed AS administrativeColgProposed, "
	        + "ffs.administrativeColgMaxMarks AS administrativeColgMaxMarks, "
	        + "ffs.administrativeColgSelfMarks AS administrativeColgSelfMarks, "
	        + "ffs.professionalServiceAchieved AS professionalServiceAchieved, "
	        + "ffs.professionalServiceProposed AS professionalServiceProposed, "
	        + "ffs.professionalServiceMaxMarks AS professionalServiceMaxMarks, "
	        + "ffs.professionalServiceSelfMarks AS professionalServiceSelfMarks, "
	        + "ffs.communityServiceAchieved AS communityServiceAchieved, "
	        + "ffs.communityServiceProposed AS communityServiceProposed, "
	        + "ffs.communityServiceMaxMarks AS communityServiceMaxMarks, "
	        + "ffs.communityServiceSelfMarks AS communityServiceSelfMarks, "
	        + "ffs.awardsAchieved AS awardsAchieved, "
	        + "ffs.awardsProposed AS awardsProposed, "
	        + "ffs.awardsMaxMarks AS awardsMaxMarks, "
	        + "ffs.awardsSelfMarks AS awardsSelfMarks, "
	        + "ffs.universityColgHodMarks AS universityColgHodMarks, "
	        + "ffs.administrativeColgHodMarks AS administrativeColgHodMarks, "
	        + "ffs.professionalServiceHodMarks AS professionalServiceHodMarks, "
	        + "ffs.communityServiceHodMarks AS communityServiceHodMarks, "
	        + "ffs.awardsHodMarks AS awardsHodMarks, "
	        + "ffs.attachmentPath AS attachmentPath, "
	        + "ffs.active AS active, "
	        + "ffs.createdBy AS createdBy, "
	        + "ffs.modifiedBy AS modifiedBy, "
	        + "ffs.createdDate AS createdDate, "
	        + "ffs.modifiedDate AS modifiedDate, "
	        + "ffs.createdUsername AS createdUsername, "
	        + "ffs.modifiedUsername AS modifiedUsername) "
	        + "FROM FarmFacultyService ffs "
		+ "Where CONCAT(IfNull(ffs.month,''),'',IfNull(ffs.year,''),'',IfNull(ffs.createdDate,''),'',IfNull(ffs.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query("SELECT new map("
	        + "ffs.facultyServiceId AS id, "
	        + "ffs.empId AS empId, "
	        + "ffs.month AS month, "
	        + "ffs.year AS year, "
	        + "ffs.universityColgAchieved AS universityColgAchieved, "
	        + "ffs.universityColgProposed AS universityColgProposed, "
	        + "ffs.universityColgMaxMarks AS universityColgMaxMarks, "
	        + "ffs.universityColgSelfMarks AS universityColgSelfMarks, "
	        + "ffs.administrativeColgAchieved AS administrativeColgAchieved, "
	        + "ffs.administrativeColgProposed AS administrativeColgProposed, "
	        + "ffs.administrativeColgMaxMarks AS administrativeColgMaxMarks, "
	        + "ffs.administrativeColgSelfMarks AS administrativeColgSelfMarks, "
	        + "ffs.professionalServiceAchieved AS professionalServiceAchieved, "
	        + "ffs.professionalServiceProposed AS professionalServiceProposed, "
	        + "ffs.professionalServiceMaxMarks AS professionalServiceMaxMarks, "
	        + "ffs.professionalServiceSelfMarks AS professionalServiceSelfMarks, "
	        + "ffs.communityServiceAchieved AS communityServiceAchieved, "
	        + "ffs.communityServiceProposed AS communityServiceProposed, "
	        + "ffs.communityServiceMaxMarks AS communityServiceMaxMarks, "
	        + "ffs.communityServiceSelfMarks AS communityServiceSelfMarks, "
	        + "ffs.awardsAchieved AS awardsAchieved, "
	        + "ffs.awardsProposed AS awardsProposed, "
	        + "ffs.awardsMaxMarks AS awardsMaxMarks, "
	        + "ffs.awardsSelfMarks AS awardsSelfMarks, "
	        + "ffs.universityColgHodMarks AS universityColgHodMarks, "
	        + "ffs.administrativeColgHodMarks AS administrativeColgHodMarks, "
	        + "ffs.professionalServiceHodMarks AS professionalServiceHodMarks, "
	        + "ffs.communityServiceHodMarks AS communityServiceHodMarks, "
	        + "ffs.awardsHodMarks AS awardsHodMarks, "
	        + "ffs.attachmentPath AS attachmentPath, "
	        + "ffs.active AS active, "
	        + "ffs.createdBy AS createdBy, "
	        + "ffs.modifiedBy AS modifiedBy, "
	        + "ffs.createdDate AS createdDate, "
	        + "ffs.modifiedDate AS modifiedDate, "
	        + "ffs.createdUsername AS createdUsername, "
	        + "ffs.modifiedUsername AS modifiedUsername) "
	        + "FROM FarmFacultyService ffs "
	        )
	Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update FarmFacultyService ffs set ffs.active=false where ffs.facultyServiceId=?1")
	public void updateFarmFacultyService(Integer facultyServiceId);

	@Modifying
	@Query(value = "update FarmFacultyService ffs set ffs.active=true where ffs.facultyServiceId=?1")
	public void updateFarmFacultyService1(Integer facultyServiceId);

	@Modifying
	@Query(value = "update FarmFacultyService ffs set ffs.attachmentPath=?2 where ffs.facultyServiceId=?1")
	public void updatePath(Integer facultyServiceId, String t2);

}
