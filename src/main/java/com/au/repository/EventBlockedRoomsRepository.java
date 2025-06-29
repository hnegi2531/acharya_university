package com.au.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.TemporalType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.EventBlockedRooms;

@Transactional
@Repository
public interface EventBlockedRoomsRepository extends JpaRepository<EventBlockedRooms , Integer>{
	
	
	@Query(value = "SELECT ebr FROM EventBlockedRooms ebr where ebr.active=true")
	public List<EventBlockedRooms> findAll1();
	
	@Query(value = "SELECT new map(ebr.event_blocked_room_id as id,ebr.block_id as block_id ) FROM EventBlockedRooms ebr")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "SELECT new map(ebr.event_blocked_room_id as id,ebr.block_id as block_id ) FROM EventBlockedRooms ebr")
	public Page<Object> findAll3(Pageable pageable);
	
	@Modifying
	@Query(value = "Update EventBlockedRooms ebr set ebr.active=false where ebr.event_blocked_room_id=?1")
	public void update(Integer event_blocked_room_id);
	
	@Modifying
	@Query(value = "Update EventBlockedRooms ebr set ebr.active=true where ebr.event_blocked_room_id=?1")
	public void update1(Integer event_blocked_room_id);
	
	@Query(value = "SELECT ir.room_id AS room_id, ir.block_id AS block_id, ift.tt_status AS tt_status, "
			+ "    CONCAT(ir.roomcode, '-', ib.block_short_name, '-', ift.facility_short_name) AS roomCodeWithBlocKAndFacilityType "
			+ "FROM infrastructure_rooms ir "
			+ "LEFT JOIN infrastructure_blocks ib ON ib.block_id = ir.block_id "
			+ "LEFT JOIN infrastructure_facility_type ift ON ift.facility_type_id = ir.facility_type_id "
			+ "WHERE ir.room_id NOT IN ( "
			+ "    SELECT ebr.room_id FROM event_blocked_rooms ebr "
			+ "    LEFT JOIN event_creation ec ON ebr.event_id = ec.event_id "
			+ "    WHERE NOT ( "
			+ "        CONVERT_TZ(ec.event_end_time, @@session.time_zone, '+00:00') <= CONVERT_TZ(:startTime, @@session.time_zone, '+00:00') "
			+ "        OR CONVERT_TZ(ec.event_start_time, @@session.time_zone, '+00:00') >= CONVERT_TZ(:endTime, @@session.time_zone, '+00:00')) "
			+ "    AND ec.approved_status IN ('Approved', 'Pending')) "
			+ "AND ir.show_in_event = true AND ift.tt_status = false ",nativeQuery = true)
		List<Map<String, Object>> getAvailableBlockAndRooms(
		    @Param("startTime") @Temporal(TemporalType.TIMESTAMP) Date eventStartTime,
		    @Param("endTime") @Temporal(TemporalType.TIMESTAMP) Date eventEndTime);

	@Query(value = "SELECT count(*) FROM event_blocked_rooms ebr "
			+ "Left Join event_creation ec On ebr.event_id=ec.event_id "
			+ "where ebr.room_id=?1 and ec.event_start_time=?2 And ec.event_end_time=?3 and ebr.active=true",nativeQuery = true)
	public Integer existsByRoomIdAndEventStartTimeAndEventEndTime(Integer roomId, String eventStartTime,String eventEndTime);

//	@Query(value = "SELECT count(*) FROM acharya_erp.event_blocked_rooms eb "
//			+ " LEFT JOIN event_creation ec ON eb.event_id = ec.event_id "
//			+ " where eb.room_id=:room_id And  not (CONVERT_TZ(ec.event_end_time, @@session.time_zone, '+00:00') <= CONVERT_TZ(:startTime, @@session.time_zone, '+00:00') "
//			+ "OR CONVERT_TZ(ec.event_start_time, @@session.time_zone, '+00:00') >= CONVERT_TZ(:endTime, @@session.time_zone, '+00:00')) ",nativeQuery = true)
//	public Integer checkingAvailableBlockAndRooms(Integer room_id,  @Param("startTime") @Temporal(TemporalType.TIMESTAMP) Date eventStartTime,
//		    @Param("endTime") @Temporal(TemporalType.TIMESTAMP) Date eventEndTime);
	
	@Query(value = "SELECT count(*) FROM event_blocked_rooms eb "
            + " LEFT JOIN event_creation ec ON eb.event_id = ec.event_id "
            + " WHERE eb.room_id = :roomId And ec.approved_status IN ('Approved', 'Pending') "
            + " AND NOT ( "
            + "   ec.event_end_time <= CONVERT_TZ(:eventStartTime, 'Asia/Kolkata', 'Asia/Kolkata') "
            + "   OR "
            + "   ec.event_start_time >= CONVERT_TZ(:eventEndTime, 'Asia/Kolkata', 'Asia/Kolkata') "
            + " ) ", nativeQuery = true)
Integer checkingAvailableBlockAndRooms(@Param("roomId") Integer roomId,
                                        @Param("eventStartTime") String eventStartTime,
                                        @Param("eventEndTime") String eventEndTime);


}
