package com.au.repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.au.model.BiometricTransaction;

public interface BiometricTransactionRepository extends JpaRepository<BiometricTransaction, Long> {
	
	@Query(value = "select * from biometric_transaction bt where bt.trn_date=:trnDate ", nativeQuery = true)
	List<BiometricTransaction> getByTrnDate(@Param("trnDate") String trnDate);

	@Query(value = "select * from biometric_transaction bt where bt.trn_date=:trnDate and bt.empcode=:empcode ", nativeQuery = true)
	List<BiometricTransaction> getByTrnDateAndEmpcode(@Param("trnDate") String trnDate,
			@Param("empcode") String empcode);

	BiometricTransaction findByEmpcodeAndTrnDateAndTrnTime(String empcode, Date trnDate, Time trnTime);

	BiometricTransaction findFirstByEmpcodeAndTrnDateAndTrnTime(String employeeCode, String logdate, Time startTime);

	List<BiometricTransaction> findByTrnDate(Date trndate);

}
