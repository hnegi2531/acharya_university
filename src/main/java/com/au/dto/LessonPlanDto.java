package com.au.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import com.au.model.LessonPlan;
import com.au.model.LessonPlanAssignment;

public class LessonPlanDto {


	LessonPlan lp;
	List<LessonPlanAssignment> lpa;

	public LessonPlan getLp() {
		return lp;
	}

	public void setLp(LessonPlan lp) {
		this.lp = lp;
	}

	public List<LessonPlanAssignment> getLpa() {
		return lpa;
	}

	public void setLpa(List<LessonPlanAssignment> lpa) {
		this.lpa = lpa;
	}

}
