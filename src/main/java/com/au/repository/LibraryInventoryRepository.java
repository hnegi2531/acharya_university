package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.au.dto.LibraryAccessionNumber;
import com.au.dto.LibraryBooks;
import com.au.dto.LibraryDetailsDTO;
import com.au.model.LibraryInventory;

public interface LibraryInventoryRepository extends JpaRepository<LibraryInventory, Integer> {

	LibraryInventory findByUid(String libraryInvUid);
	

	@Query(value="select new com.au.dto.LibraryBooks(l.env_item_id as libraryId,  CONCAT(l.title_of_book, ' - ', l.author) ) from EnvItemsInStores l "
			+ " left join Ledger le on l.ledger_id=le.ledger_id "
			+ "	left join ItemsCreation ic on l.item_id=ic.item_id  where ic.library_book_status=true ",nativeQuery = false)
	List<LibraryBooks> getAllLibraryBooks();
	
	@Query(value="select new com.au.dto.LibraryDetailsDTO(l.author as author , l.title_of_book as title) from EnvItemsInStores l "
			+ " left join Ledger le on l.ledger_id=le.ledger_id "
			+ "	left join ItemsCreation ic on l.item_id=ic.item_id  where ic.library_book_status=true and l.env_item_id=:itemId ",nativeQuery = false)
	LibraryDetailsDTO getLibraryBooksDetails(Integer itemId);
	
	@Query(value="select l.accession_number from library_inventory l order by l.created_date desc limit 1", nativeQuery = true)
	String getLatestAccessionNo();

	@Query(value="select new com.au.dto.LibraryAccessionNumber(l.author as author,l.title as title,l.accessionNumber as accessionNo,l.barcode as barcode ) from LibraryInventory l")
	Page<LibraryAccessionNumber> getAllLibraryAccessionIndex(Pageable pageable);

	LibraryInventory  findByAccessionNumber(String accessionNumber);


	LibraryInventory findFirstByAccessionNumber(String accessionNumber);


	
	
}
