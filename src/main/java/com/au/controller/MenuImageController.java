package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.MenuImageRequest;
import com.au.response.ResponseHandler;
import com.au.service.MenuImageService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MenuImageController {
	
	Logger log = LoggerFactory.getLogger(VendorAttachmentController.class);
	
	@Autowired
	private MenuImageService menu_ser;
	
	
	@PostMapping(value="/menuUploadImage")
	public ResponseEntity<Object> uploadImage(@ModelAttribute MenuImageRequest menu_image_request) throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			System.out.println("Hello-------" + auth.getDetails());
			log.debug("Message For MenuImage");
			menu_ser.uploadImage(menu_image_request.getFile() , menu_image_request.getMenu_id());
			ResponseEntity<Object> menu_image_response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return menu_image_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
		
	}
	
//	@PostMapping(value="/menuUploadImage")
//	public ResponseEntity<Object> uploadImage(@ModelAttribute MenuImageRequest menu_image_request) throws IOException{
//		if(RateLimitController.bucket.tryConsume(1)) {
//			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//			System.out.println("Hello-------" + auth.getDetails());
//			log.debug("Message For MenuImage");
//			MenuImage menu_img = menu_ser.uploadImage(menu_image_request.getFile() , menu_image_request.getMenu_id() );
//			ResponseEntity<Object> menu_image_response= ResponseHandler.generateResponse(true, HttpStatus.OK,menu_img);
//			return menu_image_response;
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}	
//		
//	}
	
	@GetMapping(path = "/menuImageDownload")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("menu_image_path") final String pathName) {
		
			try {
					final byte[] data = menu_ser.downloadFile(pathName);
					final ByteArrayResource resource = new ByteArrayResource(data);
					return ResponseEntity.ok().contentLength(data.length).header("Content-type","image/jpeg")
					.header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
			} catch (NoSuchFileException e) {
					return ResponseEntity.notFound().build();
			} catch (Exception e) {
					log.error(e.getMessage());
					return ResponseEntity.badRequest().contentLength(0).body(null);
			}
			
	}
	
	
//	@DeleteMapping("/deleteImage/{menu_image_id}")
//	public ResponseEntity<Object> deleteImage(@PathVariable Integer menu_image_id) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			menu_ser.delete(menu_image_id);
//			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
//			return response;
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
}

