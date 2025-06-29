package com.au.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDTO {

	private int pageNo;
	private int pageSize;
	private int totalPage;
	private long totalElement;
	private boolean islast;
	private List<?> content;
}
