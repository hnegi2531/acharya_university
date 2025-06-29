package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.au.model.VendorAttachment;

@Transactional
@Repository
public interface VendorAttachmentRepository extends JpaRepository<VendorAttachment , Integer> {
	
	@Query(value = "select va from VendorAttachment va where va.vendor_id=?1")
	public VendorAttachment findById1(Integer job_id);

	@Query(value = "select vh.vendor_attachment_id as vendor_attachment_id,vh.vendor_id as vendor_id,vh.vendor_attachment_path as vendor_attachment_path,"
			+ "vh.vendor_attachment_file_name as vendor_attachment_file_name,vh.vendor_attachement_type as vendor_attachement_type "
			+ "from vendor_attachments vh where vh.vendor_id=?1 ",nativeQuery=true)
	public List<Map<String, Object>> vendorAttachmentDetails(Integer vendor_id);
	
	

}
