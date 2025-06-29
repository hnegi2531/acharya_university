package com.au.model;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "uiform_fee_paid")
public class UniformFeePaid {


	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer uniformFeePaidId;

	    private Integer studentId;

	    private Integer paidYear;

	    private Double amount;

	    private Integer voucherHeadNewId;

	    private Integer otherFeeTemplateId;

	    private Integer uniformReceiptNo;

	    @Column(updatable = false)
	    private Integer createdBy;

	    private Integer modifiedBy;

	    @Column(updatable = false)
	    private String createdUsername;

	    private String modifiedUsername;

	    private Integer fcYearId;

	    @Temporal(TemporalType.TIMESTAMP)
	    private Date transactionDate;

	    @Column(updatable = false)
	    @Temporal(TemporalType.TIMESTAMP)
	    @CreationTimestamp
	    private Date createdDate;

	    @Column(name = "modified_date")
	    @Temporal(TemporalType.TIMESTAMP)
	    @UpdateTimestamp
	    private Date modifiedDate;
	    private Boolean active;


	    public UniformFeePaid() {
			super();
			// TODO Auto-generated constructor stub
		}

	    public Integer getPaidYear() {
	        return paidYear;
	    }


		public void setPaidYear(Integer paidYear) {
	        this.paidYear = paidYear;
	    }

	    public Double getAmount() {
	        return amount;
	    }

	    public void setAmount(Double amount) {
	        this.amount = amount;
	    }

	    public Integer getVoucherHeadNewId() {
	        return voucherHeadNewId;
	    }

	    public void setVoucherHeadNewId(Integer voucherHeadNewId) {
	        this.voucherHeadNewId = voucherHeadNewId;
	    }

	    

	    public Integer getOtherFeeTemplateId() {
			return otherFeeTemplateId;
		}

		public void setOtherFeeTemplateId(Integer otherFeeTemplateId) {
			this.otherFeeTemplateId = otherFeeTemplateId;
		}

		public Integer getUniformFeePaidId() {
			return uniformFeePaidId;
		}

		public void setUniformFeePaidId(Integer uniformFeePaidId) {
			this.uniformFeePaidId = uniformFeePaidId;
		}

		public Integer getStudentId() {
			return studentId;
		}

		public void setStudentId(Integer studentId) {
			this.studentId = studentId;
		}



		public Integer getUniformReceiptNo() {
			return uniformReceiptNo;
		}

		public void setUniformReceiptNo(Integer uniformReceiptNo) {
			this.uniformReceiptNo = uniformReceiptNo;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public void setModifiedDate(Date modifiedDate) {
			this.modifiedDate = modifiedDate;
		}

		public Integer getCreatedBy() {
	        return createdBy;
	    }

	    public void setCreatedBy(Integer createdBy) {
	        this.createdBy = createdBy;
	    }

	    public Integer getModifiedBy() {
	        return modifiedBy;
	    }

	    public void setModifiedBy(Integer modifiedBy) {
	        this.modifiedBy = modifiedBy;
	    }

	    public String getCreatedUsername() {
	        return createdUsername;
	    }

	    public void setCreatedUsername(String createdUsername) {
	        this.createdUsername = createdUsername;
	    }

	    public String getModifiedUsername() {
	        return modifiedUsername;
	    }

	    public void setModifiedUsername(String modifiedUsername) {
	        this.modifiedUsername = modifiedUsername;
	    }

	    public Integer getFcYearId() {
	        return fcYearId;
	    }

	    public void setFcYearId(Integer fcYearId) {
	        this.fcYearId = fcYearId;
	    }

	    public Date getTransactionDate() {
	        return transactionDate;
	    }

	    public void setTransactionDate(Date transactionDate) {
	        this.transactionDate = transactionDate;
	    }

	    public Date getCreatedDate() {
	        return createdDate;
	    }

	    public Date getModifiedDate() {
	        return modifiedDate;
	    }

		public Boolean getActive() {
			return active;
		}

		public void setActive(Boolean active) {
			this.active = active;
		}
	    
	    

}
