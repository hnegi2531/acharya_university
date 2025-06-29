package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.au.model.PaymentVoucherAttachment;


@Repository
public interface PaymentVoucherAttachmentRepo extends JpaRepository<PaymentVoucherAttachment, Long>{

	public List<PaymentVoucherAttachment> findByPaymentVoucherNo(Integer paymenVouvherNo);
}
