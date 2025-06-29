package com.au.dto;

import java.util.List;

import com.au.model.ApplicantDetails;
import com.au.model.BankImportTransaction;
import com.au.model.Candidate_Walkin;
import com.au.model.FeeReceipt;
import com.au.model.PGApplicable;
import com.au.model.ReportingStudents;
import com.au.model.StdEntranceExam;
import com.au.model.StdReportingStudentsHistory;
import com.au.model.StudentPaymentHistory;
import com.au.model.StudentTranscriptSubmission;
import com.au.model.Student_Details;
import com.au.model.TallyReceipt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeeReceiptDto {
	
	private FeeReceipt fee_rec;
	private List<StudentPaymentHistory> sph;
	private List<TallyReceipt> tr;
	private BankImportTransaction bit;
	private List<HostelFeeReceiptVoucherHeadWiseDto> hostelFeeReceiptVocherHead;
	private List<HostelBulkFeeRceiptVoucherHeadWiseDto> hostelBulkFeeReciptVocherHead;
	
}
