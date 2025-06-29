package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.HostelRoomType;

@Repository
@Transactional
public interface HostelRoomTypeRepository extends JpaRepository<HostelRoomType, Integer>{

	public Boolean existsByroomType(String roomType);
	public Boolean existsBynumberOfBeds(Integer numberOfBeds);
	
	@Query(value = "select hrt from HostelRoomType hrt where hrt.active=true")
	public List<HostelRoomType> findAll1();
	
	@Modifying
	@Query(value = "update HostelRoomType hrt set hrt.active=false where hrt.roomTypeId=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update HostelRoomType hrt set hrt.active=true where hrt.roomTypeId=?1")
	public void update1(Integer id);
	
	@Query(value = "select number_of_beds from hostel_room_type where room_type_id=?1 ",nativeQuery = true)
	public Integer fetchHostelRoomType(Integer room_type_id);
	
	@Query(value ="Select new map(hrt.roomTypeId as id,hrt.numberOfBeds as numberOfBeds,hrt.roomType as roomType,"
			+ "hrt.createdBy as createdBy,hrt.modifiedBy as modifiedBy,hrt.createdDate as createdDate,hrt.modifiedDate as modifiedDate,"
			+ "hrt.createdUsername as createdUsername,hrt.modifiedUsername as modifiedUsername,hrt.active as active,hrt.nomenclature as nomenclature) From HostelRoomType hrt "
			+ "Where CONCAT(IfNull(hrt.roomTypeId,''),'',IfNull(hrt.numberOfBeds,''),'',IfNull(hrt.roomType,''),'',IfNull(hrt.createdUsername,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(hrt.roomTypeId as id,hrt.numberOfBeds as numberOfBeds,hrt.roomType as roomType,"
			+ "hrt.createdBy as createdBy,hrt.modifiedBy as modifiedBy,hrt.createdDate as createdDate,hrt.modifiedDate as modifiedDate,"
			+ "hrt.createdUsername as createdUsername,hrt.modifiedUsername as modifiedUsername,hrt.active as active,hrt.nomenclature as nomenclature) From HostelRoomType hrt")
	public Page<Object> findAll3(Pageable pageable);
}
