package com.au.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
	private String status;

	private String razorpayPaymentId;
	private String razorpayOrderId;
	private String razorpaySignature;

	private String code;
	private String description;
	private String source;
	private String step;
	private String reason;
	private Map<String, Object> metadata;

}
