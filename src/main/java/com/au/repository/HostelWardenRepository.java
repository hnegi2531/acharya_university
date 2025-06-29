package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.HostelWarden;

@Repository
@Transactional
public interface HostelWardenRepository extends JpaRepository<HostelWarden, Integer>
{
	@Query(value = "select h from HostelWarden h where h.active=true")
	public List<HostelWarden> findAll1();
	
	@Modifying
	@Query(value = "update HostelWarden h set h.active=false where h.wardenId=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update HostelWarden h set h.active=true where h.wardenId=?1")
	public void update1(Integer id);
	
	@Query(value = "select new map(hw.wardenId as id,hw.wardenName as wardenName,hw.address as address,"
			+ "hw.mobile as mobile,hw.createdBy as createdBy,hw.modifiedBy as modifiedBy,"
			+ "hw.createdDate as createdDate,hw.modifiedDate as modifiedDate,hw.active as active,"
			+ "hw.createdUsername as createdUsername,hw.modifiedUsername as modifiedUsername,hw.emailId as emailId) "
			+ "from HostelWarden hw "
			+ "where CONCAT(IfNull(hw.wardenId,''),'',IfNull(hw.wardenName,''),'',IfNull(hw.address,''),'',IfNull(hw.emailId,''),"
			+ "'',IfNull(hw.mobile,''),'',IfNull(hw.createdBy,''),'',IfNull(hw.createdDate,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(hw.wardenId as id,hw.wardenName as wardenName,hw.address as address,"
			+ "hw.mobile as mobile,hw.createdBy as createdBy,hw.modifiedBy as modifiedBy,"
			+ "hw.createdDate as createdDate,hw.modifiedDate as modifiedDate,hw.active as active,"
			+ "hw.createdUsername as createdUsername,hw.modifiedUsername as modifiedUsername,hw.emailId as emailId) "
			+ "from HostelWarden hw")
	public Page<Object> getAllSortedData(Pageable pageable);
	
}
