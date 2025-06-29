package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.HostelTypes;

@Repository
@Transactional
public interface HostelTypesRepository  extends JpaRepository<HostelTypes, Integer>{

	public Boolean existsByhostelType(String hostelType);
	
	@Query(value = "select h from HostelTypes h where h.active=true")
	public List<HostelTypes> findAll1();
	
	@Modifying
	@Query(value = "update HostelTypes h set h.active=false where h.hostelTypeId=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update HostelTypes h set h.active=true where h.hostelTypeId=?1")
	public void update1(Integer id);
	
	@Query(value = "Select new map(ht.hostelTypeId as id,ht.hostelType as hostelType,ht.createdBy as createdBy,"
			+ "ht.modifiedBy as modifiedBy,ht.createdDate as createdDate,ht.modifiedDate as modifiedDate,ht.createdUsername as createdUsername,"
			+ "ht.modifiedUsername as modifiedUsername,ht.active as active) From HostelTypes ht "
			+ "Where CONCAT(IfNull(ht.hostelTypeId,''),'',IfNull(ht.hostelType,''),'',IfNull(ht.createdDate,''),'',IfNull(ht.createdUsername,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select ht From HostelTypes ht")
	public Page<Object> findAll3(Pageable pageable);
}
