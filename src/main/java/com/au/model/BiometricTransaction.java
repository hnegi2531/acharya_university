package com.au.model;

import java.sql.Time;
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
@Table(name="biometric_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BiometricTransaction {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "empcode")
    private String empcode;

    @Column(name = "CardID")
    private String cardID;

    @Column(name = "trn_date")
    private String trnDate;

    @Column(name = "trn_time")
    private Time trnTime;

    @Column(name = "created_at")
    private Date createdAt;

    private String newEmpcode;

	public BiometricTransaction(String empcode, String trnDate, Time trnTime) {
		this.empcode = empcode;
		this.trnDate = trnDate;
		this.trnTime = trnTime;
	}

    public BiometricTransaction(String empcode, String trnDate, Time trnTime,String newEmpcode) {
        this.empcode = empcode;
        this.trnDate = trnDate;
        this.trnTime = trnTime;
        this.newEmpcode = newEmpcode;
    }
    
    

}
