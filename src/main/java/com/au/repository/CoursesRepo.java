package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.au.model.CoursesUndergone;

@Repository
public interface CoursesRepo extends JpaRepository<CoursesUndergone, Long>{

	Boolean existsByPriorityAndEmpId(Integer priority, Integer empId);

	List<CoursesUndergone> getByEmpId(Integer empId);

}
