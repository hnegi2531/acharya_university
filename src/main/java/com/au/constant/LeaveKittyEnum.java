package com.au.constant;

public enum LeaveKittyEnum {

	WEDDING_LEAVE ("WL"),
	RESEARCH_LEAVE("RL"),
	MATERNITY_LEAVE("ML"),
	PATERNITY_LEAVE("PL"),
	MALE("M"),
	FEMALE("F"),
	ORR("ORR"),
	UN_MARRIED("U"),
	TEACHING("Teaching"),
	TEACHING_ADMIN("Teaching Admin"),
	MARRIED("M"),
	RESTRICTED_LEAVE("RH"),
	CAUSAL_LEAVE("CL"),
	EARNED_LEAVE("EL"),
	VACATION_LEAVE("VL");
	private final String code;
	
	public static final Integer PERMANENT_STATUS = 2;
	public static final Integer TOTAL_RESEARCH_LEAVE = 12;
	// Constructor
    LeaveKittyEnum(String code) {
        this.code = code;
    }

    // Getter to retrieve the value
    public String getCode() {
        return code;
    }
}
