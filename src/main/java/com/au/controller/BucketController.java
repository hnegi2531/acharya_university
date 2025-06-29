package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.model.S3Object;
import com.au.dto.JwtDetails;
import com.au.dto.ScholarshipAttachmentDto;
import com.au.model.ScholarshipAttachment;
import com.au.service.AmazonClientService;
import com.au.service.JwtTokenService;

import lombok.var;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class BucketController {
	
	Logger log = LoggerFactory.getLogger(BucketController.class);

	@Autowired
	private AmazonClientService amazonClient;
	
	@Autowired
	private JwtTokenService jwt_service;

//	@PostMapping("/uploadFile")
//	public FileRequestDto uploadFile(@RequestPart(value = "file") MultipartFile file) throws IOException {
//		return this.amazonClient.uploadFile(file);
//	}
	
	@PostMapping("/uploadFile")
	public ScholarshipAttachment uploadFile(@ModelAttribute ScholarshipAttachmentDto sad,  @RequestHeader("Authorization") String jwtToken) throws IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Scholarship Attachment ");
		return this.amazonClient.uploadFile(sad.getFile(),sad.getScholarship_id(), sad.getCandidate_id(), jwtDetails);
	}
	
	@GetMapping("/readfile")
	public Collection<String> readFile(@RequestPart(value = "file_path")String file_path) throws IOException {
		return this.amazonClient.loadFileFromS3(file_path);
	}
	
	@DeleteMapping("/deleteFile")
	public String deleteFile(@RequestPart(value = "url") String fileUrl) {
		return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
	}
	
	
	  @GetMapping("/download")
	    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam(value= "path_name") final String pathName) {
	        final byte[] data = amazonClient.downloadFile(pathName);
	        final ByteArrayResource resource = new ByteArrayResource(data);
	        return ResponseEntity
	                .ok()
	                .contentLength(data.length)
	                .header("Content-type", "application/pdf")
	                .header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
	                .body(resource);
	    }
	  
	  @GetMapping(path = "/ScholarshipAttachmentFileviews")
			public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
				try {
					final byte[] data = amazonClient.viewFiles(fileName);
					final ByteArrayResource resource = new ByteArrayResource(data);
					return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/pdf")
							.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
							.header("Cache-Control", "no-cache").body(resource);
				} catch (NoSuchFileException e) {
					return ResponseEntity.notFound().build();
				} catch (Exception e) {
					log.error(e.getMessage());
					return ResponseEntity.badRequest().contentLength(0).body(null);
				}

			}
	  
//	  @PostMapping("/uploadVideoFile")
//		public ScholarshipAttachment uploadVideoFile(@ModelAttribute ScholarshipAttachmentDto sad) throws IOException, AmazonClientException, InterruptedException {
//			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//			System.out.println("Hello-------" + auth.getDetails());
//			log.debug("Message For Scholarship Attachment ");
//			return this.amazonClient.uploadVideoFile(sad.getFile(),sad.getScholarship_id());
//		}
	  
//	  @GetMapping("/downloadVideoFile")
//	    public HttpEntity<byte[]> downloadVideoFile(@RequestParam(value= "fileName") final String pathName) throws IOException {
//	        final S3Object s3Object = amazonClient.downloadVideoFile(pathName);
//	        String contentType = s3Object.getObjectMetadata().getContentType();
//	        System.out.println("Content-Type 1: "  + contentType);
//	        
//	        var bytes = s3Object.getObjectContent().readAllBytes();
//
//	        HttpHeaders header = new HttpHeaders();
//	        header.setContentType(MediaType.valueOf(contentType));
//	        header.setContentLength(bytes.length);
//
//	        return new HttpEntity<byte[]>(bytes, header);
////	        final ByteArrayResource resource = new ByteArrayResource(data);
////	        return ResponseEntity
////	                .ok()
////	                .contentLength(data.length)
////	                .header("Content-type", "video/mp4")
////	                .header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
////	                .body(resource);
//	    }
	
}