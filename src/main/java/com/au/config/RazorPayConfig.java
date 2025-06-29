package com.au.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.au.model.RazorPaySecretKeys;
import com.au.service.RazorPaySecretKeyService;
import com.razorpay.RazorpayClient;

@Configuration
public class RazorPayConfig {

	@Autowired
	private RazorPaySecretKeyService razorPaySecretKeyService;

	public RazorpayClient getRazorpayClient(Integer schoolId) throws Exception {
		RazorPaySecretKeys secretKeys = razorPaySecretKeyService.getSecretKeysBySchoolId(schoolId);

		if (secretKeys != null && secretKeys.getActive()) {
			return new RazorpayClient(secretKeys.getRazorPayKey(), secretKeys.getSecretKey());
		} else {
			throw new IllegalArgumentException("Invalid or inactive RazorPay credentials for school ID: " + schoolId);
		}
	}
}
