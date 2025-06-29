package com.au.dto;

import java.util.List;

public class MenuDTO {
	
	private List<Integer> module_id;
	private Integer menu_id;
	private String menu_name;
	private String menu_short_name;
	private String menu_desc;
	private Boolean active;
	private String menu_icon_name;
	
	public MenuDTO() {
		super();
	}

	public List<Integer> getModule_id() {
		return module_id;
	}

	public void setModule_id(List<Integer> module_id) {
		this.module_id = module_id;
	}

	public Integer getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public String getMenu_short_name() {
		return menu_short_name;
	}

	public void setMenu_short_name(String menu_short_name) {
		this.menu_short_name = menu_short_name;
	}

	public String getMenu_desc() {
		return menu_desc;
	}

	public void setMenu_desc(String menu_desc) {
		this.menu_desc = menu_desc;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getMenu_icon_name() {
		return menu_icon_name;
	}

	public void setMenu_icon_name(String menu_icon_name) {
		this.menu_icon_name = menu_icon_name;
	}
	
}
