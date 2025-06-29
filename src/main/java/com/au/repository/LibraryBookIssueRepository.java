package com.au.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.dto.LibraryBookIssueDTO;
import com.au.model.LibraryBookIssue;


@Transactional
@Repository
public interface LibraryBookIssueRepository extends JpaRepository<LibraryBookIssue, Integer> {

	
	
	@Query("SELECT new com.au.dto.LibraryBookIssueDTO(lbi.libraryAssignmentId, lbi.libraryItemName, lbi.uom, " +
	        "lbi.closingStock, lbi.issueQuantity, lbi.issuerId, lbi.accessionNumber, lbi.bookName, ua.username,la.finePerDay as finePerDay, la.dueDate as dueDate,lbi.checkOutTime,lbi.checkInTime,lbi.userCode) " +
	        "FROM LibraryBookIssue lbi left join UserAuthentication ua on ua.id=lbi.createdBy"
	        + " left join LibraryAssigment la on la.libraryAssigmentId=lbi.libraryAssignmentId WHERE lbi.issuerId=:issuerId")
	List<LibraryBookIssueDTO> getAllLibraryBooksIssue(Integer issuerId);


    @Query(value=" update LibraryBookIssue lb set lb.issuerFine=:issuerFine, lb.checkInTime=:checkInDate where lb.issuerId=:issuerId and lb.libraryAssignmentId=:libraryAssignmentId and lb.accessionNumber=:accessionNumber  ")
	void updateFine(Date checkInDate, Double issuerFine,Integer issuerId, Integer libraryAssignmentId, String accessionNumber);

	
}
