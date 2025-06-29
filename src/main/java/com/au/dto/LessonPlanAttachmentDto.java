package com.au.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class LessonPlanAttachmentDto {

	private MultipartFile file;
	private Integer lesson_assignment_id;
	private String attachment_name;
 	private String attachment_path;
}
