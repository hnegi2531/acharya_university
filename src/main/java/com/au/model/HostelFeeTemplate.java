package com.au.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hostel_fee_template")
public class HostelFeeTemplate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer	hostel_fee_template_id;
	@Column(updatable = false)
	private String template_name;
	private String  hostels_block_id;
	private Integer hostel_room_type_id;
	private Integer ac_year_id;
	private String fee_type;
	private Integer currency_type_id;
	private Integer common_acessories;
	private Integer template_amount;
	private Integer standardAccessoriesId;
	
	/** Voucher Head New Id is coming*/
	private Integer fee_head_id;
	private String remarks;
	private String  school_ids;  //institute_ids;
	private String school_name_short;
	private Integer minimum_amount;
	/** total Sum Of All Voucher Head wise Amount */
	private Integer total_amount;
	/** Voucher Head wise Amount */
	private Integer advance_amount;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;
	@Column(updatable = false)
	private Integer createdBy;
	private Integer modifiedBy;
	@Column(updatable = false)
	private String createdUsername;
	private String modifiedUsername;
	private Boolean active;
	private String hostel_block_short_name;
	private boolean offer_status;

}
