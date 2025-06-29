package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.MajorStruc;

@Repository
@Transactional
public interface MajorStrucRepository extends JpaRepository<MajorStruc, Integer> {

	@Query(value = "select h from MajorStruc h where h.active=true")
	public List<MajorStruc> findAll1();

	@Query(value = "select new map(m.major_id as id,m.major_name as major_name,m.dept_id as dept_id,"
			+ "m.created_by as created_by,m.modified_by as modified_by,"
			+ "m.created_date as created_date,m.modified_date as modified_date,m.active as active,"
			+ "m.created_username as created_username,m.modified_username as modified_username) "
			+ "from MajorStruc m "
			+ "where CONCAT(IfNull(m.major_id,''),'',IfNull(m.major_name,''),'',IfNull(m.dept_id,''),"
			+ "'',IfNull(m.created_by,''),'',IfNull(m.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(m.major_id as id,m.major_name as major_name,m.dept_id as dept_id,"
			+ "m.created_by as created_by,m.modified_by as modified_by,"
			+ "m.created_date as created_date,m.modified_date as modified_date,m.active as active,"
			+ "m.created_username as created_username,m.modified_username as modified_username) "
			+ "from MajorStruc m")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = "update MajorStruc h set h.active=false where h.major_id=?1")
	public void updateMajor_struc(Integer id);

	@Modifying
	@Query(value = "update MajorStruc h set h.active=true where h.major_id=?1")
	public void updateMajor_struc1(Integer id);

	@Query(value = "SELECT * FROM major_struc where dept_id=?1", nativeQuery = true)
	public List<MajorStruc> fetchMajorByDept(Integer dept_id);

}
