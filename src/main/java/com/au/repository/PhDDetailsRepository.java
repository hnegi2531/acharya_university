package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.PhD_Details;

@Transactional
@Repository
public interface PhDDetailsRepository extends JpaRepository<PhD_Details,Integer>{
	
	
	@Query(value = "Select pd from PhD_Details pd where pd.active=true")
	public List<PhD_Details> findAllActivePhdDetails();
	
	@Query(value = "select new map(p.phd_id as id,p.job_id as job_id,p.stream as stream,p.registered_year as registered_year,"
			+ "p.passedout_year as passedout_year,p.university as university,p.created_by as created_by,p.modified_by as modified_by,"
			+ "p.created_Date as created_Date,p.modified_Date as modified_Date,p.active as active,"
			+ "p.created_username as created_username,p.modified_username as modified_username,p.no_of_journals_published as no_of_journals_published,"
			+ "p.no_of_paper_presentations as no_of_paper_presentations,p.book_chapters as book_chapters,p.book_publications as book_publications,"
			+ "p.patents_owned as patents_owned,p.no_of_patents as no_of_patents,p.description as description,p.phd as phd,p.mphil as mphil) "
			+ "from PhD_Details p "
			+ "where CONCAT(IfNull(p.phd_id,''),'',IfNull(p.stream,''),'',IfNull(p.registered_year,''),'',IfNull(p.passedout_year,''),"
			+ "'',IfNull(p.university,''),'',IfNull(p.no_of_journals_published,''),'',IfNull(p.created_by,''),'',IfNull(p.created_Date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	@Query(value = "select new map(p.phd_id as id,p.job_id as job_id,p.stream as stream,p.registered_year as registered_year,"
			+ "p.passedout_year as passedout_year,p.university as university,p.created_by as created_by,p.modified_by as modified_by,"
			+ "p.created_Date as created_Date,p.modified_Date as modified_Date,p.active as active,"
			+ "p.created_username as created_username,p.modified_username as modified_username,p.no_of_journals_published as no_of_journals_published,"
			+ "p.no_of_paper_presentations as no_of_paper_presentations,p.book_chapters as book_chapters,p.book_publications as book_publications,"
			+ "p.patents_owned as patents_owned,p.no_of_patents as no_of_patents,p.description as description,p.phd as phd,p.mphil as mphil) "
			+ "from PhD_Details p")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = "update PhD_Details pd set pd.active=false where pd.phd_id=?1")
	public void updateDeactivatePhdDetails(Integer phd_id);
	
	@Modifying
	@Query(value = "update PhD_Details pd set pd.active=true where pd.phd_id=?1")
	public void updateActivatePhdDetails(Integer phd_id);
	
	@Query(value = "select new map(phd.stream as stream,phd.job_id as job_id, phd.registered_year as registered_year, phd.passedout_year as passedout_year,"
			+ "phd.university as university, phd.no_of_journals_published as no_of_journals_published, phd.no_of_paper_presentations as no_of_paper_presentations,"
			+ "phd.book_chapters as book_chapters, phd.book_publications as book_publications, phd.patents_owned as patents_owned,"
			+ "phd.no_of_patents as no_of_patents, phd.description as description, phd.phd as phd, phd.mphil as mphil,"
			+ "phd.created_by as created_by, phd.modified_by as modified_by, phd.active as active, phd.created_Date as created_Date,"
			+ "phd.modified_Date as modified_Date, phd.created_username as created_username, phd.modified_username as modified_username) "
			+ "from PhD_Details ed where ed.job_id=?1")
	public List<HashMap<String,Object>> findByJobId(Integer job_id);

}
