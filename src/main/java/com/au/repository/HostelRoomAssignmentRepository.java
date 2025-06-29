package com.au.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.HostelRoomAssignment;
import com.au.model.HostelRooms;

@Repository
@Transactional
public interface HostelRoomAssignmentRepository extends JpaRepository<HostelRoomAssignment,Integer> {

	HostelRoomAssignment findByHostelRoomAndActiveTrue(HostelRooms hostelRooms);

	@Query(value="Select hra.vacant From HostelRoomAssignment hra Where hra.hostelRoom=?1 And hra.active=true")
	Boolean vacantStatusOfRoom(HostelRooms hostelRoom);


}
