package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.AssignUserResponseDTO;
import com.au.model.ApproverCreation;


@Repository
@Transactional
public interface ApproverCreationRepository  extends JpaRepository<ApproverCreation, Integer>{
	
	@Query(value = "select h from ApproverCreation h where h.active=true")
    public List<ApproverCreation> findAll1();
	
	@Modifying
	@Query(value = "update ApproverCreation h set h.active=false where h.approver_creation_id=?1")
	public void updateApproverCreation(Integer id);

	@Modifying
	@Query(value = "update ApproverCreation h set h.active=true where h.approver_creation_id=?1")
	public void updateApproverCreation1(Integer id);
	
	@Query(value ="Select new map(ac.approver_creation_id as id,ac.food_approver as food_approver,ac.created_by as created_by,ac.travel_approver as travel_approver,"
			+ "ac.modified_by as modified_by,ac.bill_approver as bill_approver,ac.created_date as created_date,"
			+ "ac.modified_date as modified_date,ac.active as active,ac.purchase_approver as purchase_approver,"
			+ "ac.user_id as user_id,ua.username as username,ua.usertype as usertype,ua.usercode as usercode,"
			+ "ac.created_username as created_username,ac.modified_username as modified_username) From ApproverCreation ac "
			+ "left join UserAuthentication ua on ua.id = ac.user_id "
			+ "Where ac.bill_approver=true And CONCAT(IfNull(ac.approver_creation_id,''),'',IfNull(ua.usertype,''),'',IfNull(ac.created_date,''),'',IfNull(ac.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(ac.approver_creation_id as id,ac.food_approver as food_approver,ac.created_by as created_by,ac.travel_approver as travel_approver,"
			+ "ac.modified_by as modified_by,ac.bill_approver as bill_approver,ac.created_date as created_date,"
			+ "ac.modified_date as modified_date,ac.active as active,ac.purchase_approver as purchase_approver,"
			+ "ac.user_id as user_id,ua.username as username,ua.usertype as usertype,ua.usercode as usercode,"
			+ "ac.created_username as created_username,ac.modified_username as modified_username) From ApproverCreation ac "
			+ "left join UserAuthentication ua on ua.id = ac.user_id  where ac.bill_approver=true")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM ApproverCreation c where c.user_id=?1 and c.bill_approver=true and c.active=true")
	public int getCountBill_approver(Integer userId, Boolean billApprover);

	@Query(value = "SELECT count(*) FROM ApproverCreation c where c.user_id=?1 and c.food_approver=true and c.active=true")
	public int getCountFood_approver(Integer userId, Boolean foodApprover);

	@Query(value = "SELECT count(*) FROM ApproverCreation c where c.user_id=?1 and c.purchase_approver=true and c.active=true")
	public int getCountPurchase_approver(Integer userId, Boolean purchaseApprover);

	@Query(value = "SELECT count(*) FROM ApproverCreation c where c.user_id=?1 and c.travel_approver=true and c.active=true")
	public int getCountTravel_approver(Integer userId, Boolean travelApprover);

	@Query(value = "select case WHEN food_approver IS true then 'TRUE' "
			+ "ELSE 'False' end from approver_creation where user_id =?1 and active=true", nativeQuery = true)
	public Boolean checkUserIsFoodApproverOrNot(Integer userId);
	
	@Query(value="select new com.au.dto.AssignUserResponseDTO( u.username as userName, ac.user_id as userId , ac.food_approver As food_approver ,ac.travel_approver AS travel_approver,ac.bill_approver As bill_approver,ac.purchase_approver As purchase_approver ) from ApproverCreation ac left join UserAuthentication u on u.id=ac.user_id where ac.active=1 and ( ac.food_approver=1 or ac.travel_approver=1 or ac.bill_approver=1 or ac.purchase_approver=1  ) ", nativeQuery = false)
	public List<AssignUserResponseDTO> getApprovers();

}
