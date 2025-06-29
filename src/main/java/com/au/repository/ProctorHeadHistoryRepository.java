package com.au.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.ProctorHeadHistory;

@Transactional
@Repository
public interface ProctorHeadHistoryRepository extends JpaRepository<ProctorHeadHistory, Integer> {
	
	@Query(value = "select phh from ProctorHeadHistory phh where phh.student_id=?1")
	public List<ProctorHeadHistory> allHistoryDetails(Integer student_id);
	
//(Insert query is not a part of the JPA interface)
	@Modifying
	@Query(value ="Insert into proctor_head_history(proctor_id,student_id,student_name,from_date,to_date,"
			+ "modified_username)select ph.employee_name,psa.student_id,sd.student_name,psa.modified_date,now(),?2 "
			+ "from proctor_student_assignment psa "
			+ "left join employee_details ph on psa.proctor_id=ph.emp_id left join student_details sd "
			+ "on psa.student_id=sd.student_id where proctor_assign_id IN ?1",nativeQuery = true)
	public void insertData(List<Integer> proctor_assign_id, String userName);

}
