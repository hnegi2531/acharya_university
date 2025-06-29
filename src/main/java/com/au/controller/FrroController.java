package com.au.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.FrroDTO;
import com.au.service.FrroService;

@RestController
@RequestMapping("/api/frro")
@CrossOrigin
public class FrroController {
	
	@Autowired
	private FrroService frroService;
	
	@PostMapping("/saveFrro")
	public ResponseEntity<Object> saveFrro(@RequestBody FrroDTO frroDTO,@RequestHeader("Authorization") String jwtToken){
		return frroService.saveFrro(frroDTO,jwtToken);
	}
	
	@GetMapping("/getStudentDetailsForFrro")
	public ResponseEntity<Object> getStudentDetailsForFrro(@RequestParam("auid") String auid){
		return frroService.getStudentDetailsForFrro(auid);
	}
	
	@GetMapping("/getStudentDetailsListForFrro")
	public ResponseEntity<Object> getStudentDetailsListForFrro(@RequestParam(required = false) String auid){
		return frroService.getStudentDetailsListForFrro(auid);
	}
 
	@PostMapping(value = "/uploadFrroFile",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> uploadFrroFile(@RequestPart("file") MultipartFile file,@RequestPart("fileType") String fileType,@RequestPart("studentId") String studentId) {
		return frroService.uploadFrroFile(file, fileType,studentId);	
	}
	
	@GetMapping("/checkFrroIsPresentOrNot")
	public ResponseEntity<Object> checkFrroIsPresentOrNot(@RequestParam("studentId") Integer studentId){
		return frroService.checkFrroIsPresentOrNot(studentId);
	}
	
	@GetMapping("/getFrroLists")
	public ResponseEntity<Object> getFrroList(){
		return frroService.getFrroList();
	}
	
	@PutMapping("/updateFrro")
	public ResponseEntity<Object> updateFrro(@RequestParam("studentId") Integer studentId,@RequestBody FrroDTO frroDTO,@RequestHeader("Authorization") String jwtToken){
		return frroService.updateFrro(studentId,frroDTO,jwtToken);
	}
	

	@GetMapping("/getFrroHistory")
	public ResponseEntity<Object> getFrroHistory(@RequestParam("studentId") Integer studentId){
		return frroService.getFrroHistory(studentId);
	}
	
	@GetMapping(path = "/frroFileDownload")
	public ResponseEntity<ByteArrayResource> purchaseOrderFileDownload(@RequestParam("pathName") final String pathName) {
		try {
			final byte[] data = frroService.downloadFile(pathName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type","application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (Exception e) {
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	@GetMapping("/getFrroStudentList")
	public ResponseEntity<Object> getFrroStudentList(@RequestParam(required = false) String auid){
		return frroService.getFrroStudentList(auid);
	}
}
