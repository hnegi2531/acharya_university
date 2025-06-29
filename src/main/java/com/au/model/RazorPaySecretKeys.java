package com.au.model;

import java.util.Date;
import java.util.List;

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
@Table(name = "razor_pay_secret_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RazorPaySecretKeys {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer schoolId;
	
	private String merchantId;
	
	private String razorPayKey;
	
	private String secretKey;
	
	private String apiKey;
	
	private Boolean active;
	
	private String status;
	
	private Integer bankId;
	
}
