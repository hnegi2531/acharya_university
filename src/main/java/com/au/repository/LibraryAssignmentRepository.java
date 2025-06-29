package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.dto.AllLibraryBookWithAccessionNumberDTO;
import com.au.model.LibraryAssigment;



@Repository
public interface LibraryAssignmentRepository  extends JpaRepository<LibraryAssigment, Integer>{

	LibraryAssigment findByLibraryId(Integer libraryId);

	LibraryAssigment findFirstByLibraryId(Integer libraryId);

	@Query(value="select new  com.au.dto.AllLibraryBookWithAccessionNumberDTO( la.title as bookName, la.accessionNumber as accessionNumber, l.libraryAssigmentId as libraryAssigmentId ) from LibraryInventory la left join"
			+ " LibraryAssigment l on l.libraryId=la.itemId  where la.isIssued=0 ",nativeQuery = false)
	List<AllLibraryBookWithAccessionNumberDTO> getAllLiraryBooksWithAccessioNumber();

	LibraryAssigment findByLibraryAssigmentId(Integer libraryAssignmentId);

}
