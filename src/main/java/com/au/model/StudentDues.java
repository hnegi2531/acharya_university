package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student_dues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDues {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer studentDueId;



	private Float totalDue;

	private Integer studentId;

	private Float totalFix;

	private Float totalPaid;

	private Float totalGrant;

	private Float totalThirdFee;
	private Float totalAddOn;

	private Integer schoolId;
	private Integer programId;
	private Integer programSpecializationId;

	private Integer categoryId;

	private Float s10due;
	private Float s10fxd;
	private Float s10paid;
	private Float s10sch;
	private Float s10adondue;
	private Float s10waivr;
	private Float s11due;
	private Float s11fxd;
	private Float s11paid;
	private Float s11sch;
	private Float s11adondue;
	private Float s11waivr;
	private Float s12due;
	private Float s12fxd;
	private Float s12paid;
	private Float s12sch;
	private Float s12adondue;
	private Float s12waivr;
	private Float s1due;
	private Float s1fxd;
	private Float s1paid;
	private Float s1sch;
	private Float s1adondue;
	private Float s1waivr;
	private Float s2due;
	private Float s2fxd;
	private Float s2paid;
	private Float s2sch;
	private Float s2adondue;
	private Float s2waivr;
	private Float s3due;
	private Float s3fxd;
	private Float s3paid;
	private Float s3sch;
	private Float s3adondue;
	private Float s3waivr;
	private Float s4due;
	private Float s4fxd;
	private Float s4paid;
	private Float s4sch;
	private Float s4adondue;
	private Float s4waivr;
	private Float s5due;
	private Float s5fxd;
	private Float s5paid;
	private Float s5sch;
	private Float s5adondue;
	private Float s5waivr;
	private Float s6due;
	private Float s6fxd;
	private Float s6paid;
	private Float s6sch;
	private Float s6adondue;
	private Float s6waivr;
	private Float s7due;
	private Float s7fxd;
	private Float s7paid;
	private Float s7sch;
	private Float s7adondue;
	private Float s7waivr;
	private Float s8due;
	private Float s8fxd;
	private Float s8paid;
	private Float s8sch;
	private Float s8adondue;
	private Float s8waivr;
	private Float s9due;
	private Float s9fxd;
	private Float s9paid;
	private Float s9sch;
	private Float s9adondue;
	private Float s9waivr;

}
