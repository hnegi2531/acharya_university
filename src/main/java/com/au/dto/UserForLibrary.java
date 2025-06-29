package com.au.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserForLibrary {
  private Integer userId;
  private String userName;
  private String userCode;
  private String name;
  private String userType;
  private String academicYear;
  private Date dateOfAdmission;
  private String admissionCategory;
  private String admissionSubCategory;
  private String fatherName;
  private String dateOfJoining;
  private String designationName;
  private Date dateOfRelieving;
  private Float  salary;
  private String cancelAdmissionDate;
  
}
