package com.au.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class MenuImageRequest {
	
	private MultipartFile file;
	private List<Integer> menu_id;
	
	
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public List<Integer> getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(List<Integer> menu_id) {
		this.menu_id = menu_id;
	}

}
