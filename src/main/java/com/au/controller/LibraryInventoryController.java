package com.au.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.LibraryInventoryDTO;
import com.au.service.LibraryInventoryService;
import com.au.service.UserAuthenticationService;




@RestController
@RequestMapping("/api/libraryInv")
@CrossOrigin
public class LibraryInventoryController {

	
	@Autowired
	private LibraryInventoryService  libraryInvService;
	
	@Autowired
	private UserAuthenticationService uservice;
	
	@PostMapping("/saveLibraryInventory")
	public ResponseEntity<Object> saveLibraryInventory(@RequestBody LibraryInventoryDTO libraryInventoryDto){
		return libraryInvService.saveLibraryInventory(libraryInventoryDto);
	}
	
	
	@GetMapping("/getAllLibraryBooksWithAccessionNumber")
	public ResponseEntity<Object> getAllLibraryBooksWithAccessionNumber(){
		return libraryInvService.getAllLibraryBooksWithAccessionNumber();
	}
	
	@GetMapping("/getAllUserListForLibrary")
	public ResponseEntity<Object> getAllUserListForLibrary(){
		return uservice.getAllUserListForLibrary();
	}
	
	
	@GetMapping("/getAllLibraryBooksIssue")
	public ResponseEntity<Object> getAllLibraryBooksIssue(@RequestParam("issuerId") Integer issuerId){
		return libraryInvService.getAllLibraryBooksIssue(issuerId);
	}
}
