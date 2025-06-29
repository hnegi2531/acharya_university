//package com.au.event;
//
//import java.text.SimpleDateFormat;
//import java.time.Year;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import javax.persistence.criteria.Predicate.BooleanOperator;
//
//import org.apache.commons.lang3.ObjectUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import com.au.dto.TransactionSettlementDTO;
//import com.au.model.BankImportTransaction;
//import com.au.model.BulkFeeReceipt;
//import com.au.model.BulkTransaction;
//import com.au.model.CmaFeeReceipt;
//import com.au.model.FeeReceipt;
//import com.au.model.FeeTemplateSubAmount;
//import com.au.model.HostelDue;
//import com.au.model.RTGSFeeHistory;
//import com.au.model.RazorPayPaymentDetails;
//import com.au.model.RazorPaySecretKeys;
//import com.au.model.RazorPayTransaction;
//import com.au.model.ReceiptCounter;
//import com.au.model.RegistrationFeeTransaction;
//import com.au.model.StudentPaymentHistory;
//import com.au.model.Student_Details;
//import com.au.model.TallyReceipt;
//import com.au.model.UniformReceipt;
//import com.au.repository.BankImportTransactionRepository;
//import com.au.repository.BulkFeeReceiptRepository;
//import com.au.repository.BulkTransactionRepository;
//import com.au.repository.CmaFeeReceiptRepository;
//import com.au.repository.FeeReceiptRepository;
//import com.au.repository.FeeTemplateSubAmountRepository;
//import com.au.repository.FinancialYearRepository;
//import com.au.repository.RTGSFeeHistoryRepository;
//import com.au.repository.RazorPayPaymentDetailsRepository;
//import com.au.repository.RazorPaySecretKeyRepository;
//import com.au.repository.RazorPayTransactionRepository;
//import com.au.repository.ReceiptCounterRepository;
//import com.au.repository.RegistrationFeeTrsactionRepository;
//import com.au.repository.School_Repository;
//import com.au.repository.StudentDetailsRepository;
//import com.au.repository.StudentPaymentHistoryRepository;
//import com.au.repository.UniformReceiptRepository;
//
//@Component
//public class BankImportSettlementEventPublisher2 {
//
//	@Autowired
//	private RazorPayTransactionRepository razorPayTransactionRepository;
//
//	@Autowired
//	private RazorPayPaymentDetailsRepository razorPayPaymentDetailsRepository;
//
//	@Autowired
//	private BankImportTransactionRepository bankImportTransactionRepository;
//
//	@Autowired
//	private School_Repository schoolRepository;
//
//	@Autowired
//	private ReceiptCounterRepository receiptCounterRepository;
//
//	@Autowired
//	private RTGSFeeHistoryRepository rtgsFeeHistoryRepository;
//
//	@Autowired
//	private FeeReceiptRepository feeReceiptRepository;
//
//	@Autowired
//	private StudentPaymentHistoryRepository studentPaymentHistoryRepository;
//
//	@Autowired
//	private FinancialYearRepository financialYearRepository;
//
//	@Autowired
//	private StudentDetailsRepository studentDetailsRepository;
//
//	@Autowired
//	private BulkFeeReceiptRepository bulkFeeReceiptRepository;
//
//	@Autowired
//	private RazorPaySecretKeyRepository razorPaySecretKeyRepository;
//
//	@Autowired
//	private BulkTransactionRepository bulkTransactionRepository;
//
//	@Autowired
//	private FeeTemplateSubAmountRepository feeTemplateSubAmountRepository;
//
//	@Autowired
//	private ApplicationEventPublisher applicationEventPublisher;
//
//	@Autowired
//	private RegistrationFeeTrsactionRepository registrationFeeTransactionRepository;
//
//	@Async
//	@EventListener
//	public void handleAttendenceSheetEventPublisher(TransactionSettlementDTO transactionSettlementDTO) {
//		try {
//			transactionSettlementDTO.getItems().stream().filter(t -> t.isSettled() == true).forEach(settlement -> {
//				if (ObjectUtils.isNotEmpty(settlement) && ObjectUtils.isNotEmpty(settlement.getOrderId())) {
//
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//
//					BankImportTransaction bankImportTransaction = bankImportTransactionRepository
//							.getByOrderId(settlement.getOrderId());
//
//					RazorPayTransaction razorPayTransaction = razorPayTransactionRepository
//							.getByOrderIdAndTransactionType(settlement.getOrderId(), "College Fees");
//                    //Registration fee
//
//					RegistrationFeeTransaction registrationFeeTransaction = registrationFeeTransactionRepository.findByOrderId(settlement.getOrderId());
//					Student_Details studentDetails = studentDetailsRepository.getStudentByCandidateId(registrationFeeTransaction.getCandidateId());
//					if (ObjectUtils.isNotEmpty(registrationFeeTransaction)){
//						if(ObjectUtils.isNotEmpty(studentDetails.getAuid()))
//						{
//							if (ObjectUtils.isEmpty(bankImportTransaction)) {
//								bankImportTransaction = new BankImportTransaction();
//								bankImportTransaction.setReceipt_no(generateNextReceiptNumber());
//							}
//
//							bankImportTransaction.setSettlement_id(settlement.getSettlementId());
//							bankImportTransaction.setSettlement_utr(settlement.getSettlementUtr());
//							bankImportTransaction.setOrder_id(settlement.getOrderId());
//							bankImportTransaction.setPaid((double) settlement.getCredit() / 100);
//							bankImportTransaction.setActive(Boolean.TRUE);
//							bankImportTransaction.setCreated_by(1);
//							bankImportTransaction.setCreated_username("Amadmin");
//							bankImportTransaction.setFc_year_id(
//									financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
//
//							bankImportTransaction.setStudent_id(studentDetails.getStudent_id());
//
//							bankImportTransaction.setSchool_id(studentDetails.getSchool_id());
//							bankImportTransaction.setAmount((double) settlement.getAmount() / 100);
//							bankImportTransaction.setCheque_dd_no(registrationFeeTransaction.getOrderId() + "-"
//									+ settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
//							bankImportTransaction.setTransaction_date(sdf.format(registrationFeeTransaction.getTransactionDate()));
//							bankImportTransaction.setTransaction_no(registrationFeeTransaction.getPaymentId());
//							RazorPaySecretKeys razorPaySecretKeys = razorPaySecretKeyRepository
//									.getRazorPaySecretKeysBySchoolId(studentDetails.getSchool_id());
//							bankImportTransaction.setDeposited_bank_id(ObjectUtils.isNotEmpty(razorPaySecretKeys)
//									&& ObjectUtils.isNotEmpty(razorPaySecretKeys.getBankId())
//											? razorPaySecretKeys.getBankId()
//											: null);
//							bankImportTransaction.setBalance(bankImportTransaction.getAmount() - bankImportTransaction.getPaid());
//							bankImportTransactionRepository.save(bankImportTransaction);
//
//							//RTGS
//
//							List<RTGSFeeHistory> rtgsFeeHistories = rtgsFeeHistoryRepository
//									.getRtgsFeeHistories(bankImportTransaction.getBank_import_transaction_id());
//
//							if (ObjectUtils.isEmpty(rtgsFeeHistories)) {
//
//
//
//									RTGSFeeHistory rtgsFeeHistory = new RTGSFeeHistory();
//									rtgsFeeHistory.setActive(Boolean.TRUE);
//									rtgsFeeHistory.setBank_transaction_history_id(
//											bankImportTransaction.getBank_import_transaction_id());
//									rtgsFeeHistory.setPaid((double)registrationFeeTransaction.getAmount());
//									rtgsFeeHistory.setSchool_id(studentDetails.getSchool_id());
//									rtgsFeeHistory.setStudent_id(bankImportTransaction.getStudent_id());
//									rtgsFeeHistory.setRtgs_balance_amount(bankImportTransaction.getBalance());
//									rtgsFeeHistory.setRtgs_net_amount(bankImportTransaction.getAmount());
//
//									rtgsFeeHistory.setFc_year_id(financialYearRepository.getFinancialYearIdOnCurrentYear()
//											.getFinancial_year_id());
//
//									rtgsFeeHistory.setReceipt_no(bankImportTransaction.getReceipt_no());
//									rtgsFeeHistory.setReceipt_type("Registration Fee");
//
//
//
//
//								rtgsFeeHistoryRepository.save(rtgsFeeHistory);
//							}
//
//							// fee receipt
//
//							List<FeeReceipt> feeReceipts = feeReceiptRepository
//									.getFeeReceiptsByBankImportId(bankImportTransaction.getBank_import_transaction_id());
//							Integer fcYearId = financialYearRepository.getFinancialYearIdOnCurrentYear()
//									.getFinancial_year_id();
//
//
//							if (ObjectUtils.isEmpty(feeReceipts)) {
//								String number1;
//								FeeReceipt lastFeeReceipt = feeReceiptRepository
//										.getLastFeeReceiptByFinancialIdAndSchoolId(fcYearId, studentDetails.getSchool_id());
//
//								String number = lastFeeReceipt != null ? lastFeeReceipt.getFee_receipt() : null;
//
//								if (number == null) {
//									number1 = String.format("%01d", 1);
//
//								} else if (ObjectUtils.isNotEmpty(lastFeeReceipt)
//										&& fcYearId == lastFeeReceipt.getFinancial_year_id()
//										&& lastFeeReceipt.getSchool_id() == studentDetails.getSchool_id()) {
//
//
//									Integer count = Integer.valueOf(number) + 1;
//									number1 = String.format("%01d", count);
//
//								} else {
//									number1 = String.format("%01d", 1);
//								}
//
//								String fee_receipt1 = number1;
//
//
//									FeeReceipt feeReceipt = new FeeReceipt();
//									feeReceipt.setBank_transaction_history_id(
//											bankImportTransaction.getBank_import_transaction_id());
//									feeReceipt.setPaid_amount(bankImportTransaction.getPaid().floatValue());
//									feeReceipt.setFinancial_year_id(financialYearRepository
//											.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
//									feeReceipt.setActive(Boolean.TRUE);
//
//									feeReceipt.setReceipt_type("Registration Fee");
//									feeReceipt.setStudent_id(studentDetails.getStudent_id());
//
//
//									feeReceipt.setFee_receipt(fee_receipt1);
//									feeReceipt.setCreated_by(1);
//									feeReceipt.setCreated_username("Amadmin");
//									feeReceipt.setRemarks("AUTORECEIPT");
//									feeReceipt.setTransaction_type("ONLINE");
//									feeReceipt.setTransactionMode(settlement.getMethod().toUpperCase());
//									feeReceipt.setReceived_in("INR");
//									feeReceipt.setSchool_id(bankImportTransaction.getSchool_id());
//									feeReceipt.setHostel_status(0);
//
//							    feeReceiptRepository.save(feeReceipt);
//							}
//
//
//
//
//							List<StudentPaymentHistory> studentPaymentHistories = studentPaymentHistoryRepository
//									.getStudnentPaymentHistoryByBankImportId(
//											bankImportTransaction.getBank_import_transaction_id());
//
//							if (ObjectUtils.isEmpty(studentPaymentHistories)) {
//
//								       double paidAmount= bankImportTransaction.getPaid().doubleValue();
//								       Integer feeReceiptId  = feeReceiptRepository.getFeeReceiptId(studentDetails.getStudent_id(),bankImportTransaction.getBank_import_transaction_id(),fcYearId,studentDetails.getSchool_id());
//
//										StudentPaymentHistory studentPaymentHistory = new StudentPaymentHistory();
//										studentPaymentHistory.setActive(Boolean.TRUE);
//										studentPaymentHistory.setPaid_amount(paidAmount);
//
//										studentPaymentHistory.setStudent_id(bankImportTransaction.getStudent_id());
//
//										studentPaymentHistory.setSchool_id(studentDetails.getSchool_id());
//										studentPaymentHistory.setTranscation_type("ONLINE");
//										studentPaymentHistory.setTotal_amount(bankImportTransaction.getPaid().doubleValue());
//										studentPaymentHistory.setBankImportTransactionId(
//												bankImportTransaction.getBank_import_transaction_id());
//
//										studentPaymentHistory.setFee_receipt (bankImportTransaction.getReceipt_no());
//										studentPaymentHistory.setFee_receipt_id(feeReceiptId);
//										studentPaymentHistory.setVoucher_head_new_id(13);
//
//										studentPaymentHistory.setFinancial_year_id(fcYearId);
//
//											studentPaymentHistoryRepository.save(studentPaymentHistory);
//
//
//
//							}
//
//
//						}
//					}
//					 //end of registration
//					if (ObjectUtils.isNotEmpty(razorPayTransaction)) {
//
//						if (ObjectUtils.isEmpty(bankImportTransaction)) {
//							bankImportTransaction = new BankImportTransaction();
//							bankImportTransaction.setReceipt_no(generateNextReceiptNumber());
//						}
//
//						bankImportTransaction.setSettlement_id(settlement.getSettlementId());
//						bankImportTransaction.setSettlement_utr(settlement.getSettlementUtr());
//						bankImportTransaction.setOrder_id(settlement.getOrderId());
//						bankImportTransaction.setPaid((double) settlement.getCredit() / 100);
//						bankImportTransaction.setActive(Boolean.TRUE);
//						bankImportTransaction.setCreated_by(1);
//						bankImportTransaction.setCreated_username("Amadmin");
//						bankImportTransaction.setFc_year_id(
//								financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
//
//						bankImportTransaction.setStudent_id(razorPayTransaction.getStudentId());
//						bankImportTransaction.setTransaction_remarks(razorPayTransaction.getRemarks());
//						Integer schoolId = studentDetailsRepository
//								.getStudentSchoolId(razorPayTransaction.getStudentId());
//						Integer feetemplateId=studentDetailsRepository.getFeeTemplateIdByStudentId(razorPayTransaction.getStudentId());
//
//						bankImportTransaction.setSchool_id(schoolId);
//						bankImportTransaction.setAmount((double) settlement.getAmount() / 100);
//						bankImportTransaction.setCheque_dd_no(razorPayTransaction.getOrderId() + "-"
//								+ settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
//						bankImportTransaction.setTransaction_date(sdf.format(razorPayTransaction.getTransactionDate()));
//						bankImportTransaction.setTransaction_no(razorPayTransaction.getPaymentId());
//						RazorPaySecretKeys razorPaySecretKeys = razorPaySecretKeyRepository
//								.getRazorPaySecretKeysBySchoolId(schoolId);
//						bankImportTransaction.setDeposited_bank_id(ObjectUtils.isNotEmpty(razorPaySecretKeys)
//								&& ObjectUtils.isNotEmpty(razorPaySecretKeys.getBankId())
//										? razorPaySecretKeys.getBankId()
//										: null);
//						bankImportTransaction.setBalance(bankImportTransaction.getAmount() - bankImportTransaction.getPaid());
//						bankImportTransactionRepository.save(bankImportTransaction);
//
//						List<RazorPayPaymentDetails> razorPayPaymentDetails = razorPayPaymentDetailsRepository
//								.getByRazorPayTransactionIdAndReceiptType(
//										razorPayTransaction.getRazorPayTransactionId(), "College Fee");
//
//						List<RTGSFeeHistory> rtgsFeeHistories = rtgsFeeHistoryRepository
//								.getRtgsFeeHistories(bankImportTransaction.getBank_import_transaction_id());
//
//						if (ObjectUtils.isEmpty(rtgsFeeHistories)) {
//							rtgsFeeHistories = new ArrayList<>();
//							double deduceAmount = 0;
//							for (RazorPayPaymentDetails razorPay : razorPayPaymentDetails) {
//								RTGSFeeHistory rtgsFeeHistory = new RTGSFeeHistory();
//								rtgsFeeHistory.setActive(Boolean.TRUE);
//								rtgsFeeHistory.setBank_transaction_history_id(
//										bankImportTransaction.getBank_import_transaction_id());
//								rtgsFeeHistory.setPaid(razorPay.getAmount());
//								rtgsFeeHistory.setSchool_id(schoolId);
//								rtgsFeeHistory.setStudent_id(bankImportTransaction.getStudent_id());
//								rtgsFeeHistory.setRtgs_balance_amount(
//										(bankImportTransaction.getAmount() - razorPay.getAmount()));
//								rtgsFeeHistory.setRtgs_net_amount(bankImportTransaction.getAmount());
//								rtgsFeeHistory.setYear(razorPay.getYear());
//								rtgsFeeHistory.setSem(razorPay.getSem());
//								rtgsFeeHistory.setFc_year_id(financialYearRepository.getFinancialYearIdOnCurrentYear()
//										.getFinancial_year_id());
//								deduceAmount = rtgsFeeHistory.getRtgs_balance_amount();
//								rtgsFeeHistory.setReceipt_no(bankImportTransaction.getReceipt_no());
//								rtgsFeeHistory.setReceipt_type(razorPay.getReceiptType());
//								//rtgsFeeHistory.setRemarks(bankImportTransaction.getTransaction_remarks());
//								rtgsFeeHistories.add(rtgsFeeHistory);
//
//							}
//
//							rtgsFeeHistoryRepository.saveAll(rtgsFeeHistories);
//						}
//
//						List<FeeReceipt> feeReceipts = feeReceiptRepository
//								.getFeeReceiptsByBankImportId(bankImportTransaction.getBank_import_transaction_id());
//						Integer fcYearId = financialYearRepository.getFinancialYearIdOnCurrentYear()
//								.getFinancial_year_id();
//
//
//						if (ObjectUtils.isEmpty(feeReceipts)) {
//							String number1;
//							FeeReceipt lastFeeReceipt = feeReceiptRepository
//									.getLastFeeReceiptByFinancialIdAndSchoolId(fcYearId, schoolId);
//
//							String number = lastFeeReceipt != null ? lastFeeReceipt.getFee_receipt() : null;
//
//							if (number == null) {
//								number1 = String.format("%01d", 1);
//
//							} else if (ObjectUtils.isNotEmpty(lastFeeReceipt)
//									&& fcYearId == lastFeeReceipt.getFinancial_year_id()
//									&& lastFeeReceipt.getSchool_id() == schoolId) {
//
//								// String abc = number.substring(2);
//								Integer count = Integer.valueOf(number) + 1;
//								number1 = String.format("%01d", count);
//
//							} else {
//								number1 = String.format("%01d", 1);
//							}
//
//							String fee_receipt1 = number1;
//
//							feeReceipts = new ArrayList<>();
//							for (RTGSFeeHistory rtgsFeeHistory : rtgsFeeHistories) {
//								FeeReceipt feeReceipt = new FeeReceipt();
//								feeReceipt.setBank_transaction_history_id(
//										rtgsFeeHistory.getBank_transaction_history_id());
//								feeReceipt.setPaid_amount(rtgsFeeHistory.getPaid().floatValue());
//								feeReceipt.setFinancial_year_id(financialYearRepository
//										.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
//								feeReceipt.setActive(Boolean.TRUE);
//								//feeReceipt.setFee_receipt(rtgsFeeHistory.getReceipt_no());
//								feeReceipt.setReceipt_type("General");
//								feeReceipt.setStudent_id(rtgsFeeHistory.getStudent_id());
//								feeReceipt.setPaid_year(String.valueOf(rtgsFeeHistory.getSem()));
//								feeReceipt.setAc_year_id(razorPayTransaction.getAcYearId());
//								feeReceipt.setFee_receipt(fee_receipt1);
//								feeReceipt.setCreated_by(1);
//								feeReceipt.setCreated_username("Amadmin");
//								feeReceipt.setRemarks("AUTORECEIPT");
//								feeReceipt.setTransaction_type("ONLINE");
//								feeReceipt.setTransactionMode(settlement.getMethod().toUpperCase());
//								feeReceipt.setReceived_in("INR");
//								feeReceipt.setSchool_id(bankImportTransaction.getSchool_id());
//								feeReceipt.setHostel_status(0);
//								feeReceipts.add(feeReceipt);
//
//							}
//
//							feeReceiptRepository.saveAll(feeReceipts);
//						}
//
//						List<StudentPaymentHistory> studentPaymentHistories = studentPaymentHistoryRepository
//								.getStudnentPaymentHistoryByBankImportId(
//										bankImportTransaction.getBank_import_transaction_id());
//						List<FeeTemplateSubAmount> feeTemplateSubAmounts = feeTemplateSubAmountRepository
//								.getFeeTemplateSubAmounts(razorPayTransaction.getStudentId());
//
//						if (ObjectUtils.isEmpty(studentPaymentHistories) && ObjectUtils.isNotEmpty(feeTemplateSubAmounts)) {
//							studentPaymentHistories = new ArrayList<>();
//
//							for (FeeReceipt rtgsFeeHistory : feeReceipts) {
//							       float paidAmount=rtgsFeeHistory.getPaid_amount();
//								for(FeeTemplateSubAmount feeTemplateSubAmount:feeTemplateSubAmounts) {
//									StudentPaymentHistory studentPaymentHistory = new StudentPaymentHistory();
//									studentPaymentHistory.setActive(Boolean.TRUE);
//									studentPaymentHistory.setPaid_amount(getPaidAmount(feeTemplateSubAmount,rtgsFeeHistory,studentPaymentHistory,paidAmount));
//									paidAmount=(float) (paidAmount-studentPaymentHistory.getPaid_amount());
//									if(paidAmount<0) {
//										break;
//									}
//									studentPaymentHistory.setStudent_id(rtgsFeeHistory.getStudent_id());
//									studentPaymentHistory.setPaid_year(Integer.parseInt(rtgsFeeHistory.getPaid_year()));
//									studentPaymentHistory.setSchool_id(schoolId);
//									studentPaymentHistory.setTranscation_type("ONLINE");
//									studentPaymentHistory.setTotal_amount(rtgsFeeHistory.getPaid_amount().doubleValue());
//									studentPaymentHistory.setBankImportTransactionId(
//											bankImportTransaction.getBank_import_transaction_id());
//								//	studentPaymentHistory.setFee_receipt (String.valueOf(rtgsFeeHistory.getFee_receipt_id()));
//									studentPaymentHistory.setFee_receipt (bankImportTransaction.getReceipt_no());
//									studentPaymentHistory.setFee_receipt_id(rtgsFeeHistory.getFee_receipt_id());
//									studentPaymentHistory.setVoucher_head_new_id(feeTemplateSubAmount.getvoucher_head_new_id());
//									studentPaymentHistory.setFee_template_id(feetemplateId);
//									studentPaymentHistory.setFinancial_year_id(fcYearId);
//									if(ObjectUtils.isNotEmpty(studentPaymentHistory.getPaid_amount()) && studentPaymentHistory.getPaid_amount()>0) {
//										studentPaymentHistories.add(studentPaymentHistory);
//									}
//
//
//								}
//
//							}
//							studentPaymentHistoryRepository.saveAll(studentPaymentHistories);
//
//						}
//
//						List<RazorPayPaymentDetails> razorPayLatePaymentDetails = razorPayPaymentDetailsRepository
//								.getByRazorPayTransactionIdAndReceiptType(
//										razorPayTransaction.getRazorPayTransactionId(), "Bulk Fee");
//
//						List<BulkFeeReceipt> bulkFeeReceipts = bulkFeeReceiptRepository
//								.getBulkFeeReceiptByBankImportId(bankImportTransaction.getBank_import_transaction_id());
//						if (ObjectUtils.isEmpty(bulkFeeReceipts)) {
//							bulkFeeReceipts = new ArrayList<>();
//
//						Integer bulkreceiptNo = bulkFeeReceiptRepository.getMaxId() + 1;
//						for (RazorPayPaymentDetails latefee : razorPayLatePaymentDetails) {
//							BulkFeeReceipt bulkFeeReceipt = new BulkFeeReceipt();
//							bulkFeeReceipt.setActive(Boolean.TRUE);
//							bulkFeeReceipt.setAmount(latefee.getAmount());
//							bulkFeeReceipt.setStudent_id(razorPayTransaction.getStudentId());
//							bulkFeeReceipt
//									.setBankImportTransactionId(bankImportTransaction.getBank_import_transaction_id());
//							bulkFeeReceipt.setFinancial_year_id(
//									financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
//							bulkFeeReceipt.setSchool_id(schoolId);
//							bulkFeeReceipt.setTransaction_type("Late Fee");
//							bulkFeeReceipt.setBulk_fee_receipt(bulkreceiptNo);
//							bulkFeeReceipt.setOrderId(razorPayTransaction.getOrderId());
//							bulkFeeReceipts.add(bulkFeeReceipt);
//
//						}
//						bulkFeeReceiptRepository.saveAll(bulkFeeReceipts);
//
//						}
//
//
//						StudentDueEvent studentDueEvent=new StudentDueEvent(null,null,razorPayTransaction.getStudentId(),null);
//						applicationEventPublisher.publishEvent(studentDueEvent);
//					}
//
//					}
//
//				BulkTransaction bulkTransaction =bulkTransactionRepository.findByOrderId(settlement.getOrderId());
//				if(ObjectUtils.isNotEmpty(bulkTransaction)) {
//
//
//				BulkFeeReceipt bulkFeeReceipts = bulkFeeReceiptRepository
//						.findByOrderId(settlement.getOrderId());
//				if (ObjectUtils.isEmpty(bulkFeeReceipts)) {
//
//					BulkFeeReceipt bulkFeeReceipt = new BulkFeeReceipt();
//					bulkFeeReceipt.setActive(Boolean.TRUE);
//					bulkFeeReceipt.setAmount(bulkTransaction.getAmount().doubleValue());
//					bulkFeeReceipt.setFrom_name(bulkTransaction.getMobile());
//					bulkFeeReceipt.setFinancial_year_id(
//							financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
//					Integer latestReceiptNumber = bulkFeeReceiptRepository.getMaxId();
//					int receiptNo = (latestReceiptNumber != null) ? latestReceiptNumber : 0;
//					if (receiptNo == 0) {
//						receiptNo = 1;
//					} else {
//						receiptNo++;
//					}
//					bulkFeeReceipt.setOrderId(settlement.getOrderId());
//					bulkFeeReceipt.setBulk_fee_receipt(latestReceiptNumber);
//
//					bulkFeeReceiptRepository.save(bulkFeeReceipt);
//				}
//			}
//
//
//
//		});
//
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}
//
//	private Double getPaidAmount(FeeTemplateSubAmount feeTemplateSubAmount, FeeReceipt rtgsFeeHistory, StudentPaymentHistory studentPaymentHistory, float totalAmount) {
//		int paidYear=Integer.parseInt(rtgsFeeHistory.getPaid_year());
//
//		double balance=0;
//		double year1= ObjectUtils.isNotEmpty( feeTemplateSubAmount.getYear1_amt())?feeTemplateSubAmount.getYear1_amt():0;
//		double year2= ObjectUtils.isNotEmpty( feeTemplateSubAmount.getYear2_amt())?feeTemplateSubAmount.getYear2_amt():0;
//		double year3= ObjectUtils.isNotEmpty( feeTemplateSubAmount.getYear3_amt())?feeTemplateSubAmount.getYear3_amt():0;
//		double year4= ObjectUtils.isNotEmpty( feeTemplateSubAmount.getYear4_amt())?feeTemplateSubAmount.getYear4_amt():0;
//		double year5= ObjectUtils.isNotEmpty( feeTemplateSubAmount.getYear5_amt())?feeTemplateSubAmount.getYear5_amt():0;
//		double year6= ObjectUtils.isNotEmpty( feeTemplateSubAmount.getYear6_amt())?feeTemplateSubAmount.getYear6_amt():0;
//		double year7= ObjectUtils.isNotEmpty( feeTemplateSubAmount.getYear7_amt())?feeTemplateSubAmount.getYear7_amt():0;
//		double year8= ObjectUtils.isNotEmpty( feeTemplateSubAmount.getYear8_amt())?feeTemplateSubAmount.getYear8_amt():0;
//		double year9= ObjectUtils.isNotEmpty( feeTemplateSubAmount.getYear9_amt())?feeTemplateSubAmount.getYear9_amt():0;
//		double year10= ObjectUtils.isNotEmpty( feeTemplateSubAmount.getYear10_amt())?feeTemplateSubAmount.getYear10_amt():0;
//		double year11= ObjectUtils.isNotEmpty( feeTemplateSubAmount.getYear11_amt())?feeTemplateSubAmount.getYear11_amt():0;
//		double year12= ObjectUtils.isNotEmpty( feeTemplateSubAmount.getYear12_amt())?feeTemplateSubAmount.getYear12_amt():0;
//
//		if(paidYear==1 && totalAmount>0 && year1>0) {
//			studentPaymentHistory.setPaid_amount(totalAmount>=year1? year1:totalAmount);
//			totalAmount=(float) (totalAmount-year1);
//			studentPaymentHistory.setTo_pay(year1);
//
//		}
//
//		if(paidYear==2 && totalAmount>0 && year2>0 ) {
//			studentPaymentHistory.setPaid_amount(totalAmount>=year2? year2:totalAmount);
//			totalAmount=(float) (totalAmount-year2);
//			studentPaymentHistory.setTo_pay(year2);
//			}
//
//		if(paidYear==3 && totalAmount>0 & year3>0 ) {
//			studentPaymentHistory.setPaid_amount(totalAmount>=year3? year3:totalAmount);
//			totalAmount=(float) (totalAmount-year3);
//			studentPaymentHistory.setTo_pay(year3);
//		}
//
//		if(paidYear==4 && totalAmount>0 & year4>0) {
//			studentPaymentHistory.setPaid_amount(totalAmount>=year4? year4:totalAmount);
//			totalAmount=(float) (totalAmount-year4);
//			studentPaymentHistory.setTo_pay(year4);
//		}
//
//		if(paidYear==5 && totalAmount>0 & year5>0) {
//			studentPaymentHistory.setPaid_amount(totalAmount>=year5? year5:totalAmount);
//			totalAmount=(float) (totalAmount-year5);
//			studentPaymentHistory.setTo_pay(year5);
//		}
//
//		if(paidYear==6 && totalAmount>0 & year6>0 ) {
//			studentPaymentHistory.setPaid_amount(totalAmount>=year6? year6:totalAmount);
//			totalAmount=(float) (totalAmount-year6);
//			studentPaymentHistory.setTo_pay(year6);
//		}
//
//		if(paidYear==7 && totalAmount>0 & year7>0 ) {
//			studentPaymentHistory.setPaid_amount(totalAmount>=year7? year7:totalAmount);
//			totalAmount=(float) (totalAmount-year7);
//			studentPaymentHistory.setTo_pay(year7);
//		}
//
//		if(paidYear==8 && totalAmount>0  & year8>0) {
//			studentPaymentHistory.setPaid_amount(totalAmount>=year8? year8:totalAmount);
//			totalAmount=(float) (totalAmount-year8);
//			studentPaymentHistory.setTo_pay(year8);
//		}
//
//		if(paidYear==9 && totalAmount>0 & year9>0) {
//			studentPaymentHistory.setPaid_amount(totalAmount>=year9? year9:totalAmount);
//			totalAmount=(float) (totalAmount-year9);
//			studentPaymentHistory.setTo_pay(year9);
//		}
//
//		if(paidYear==10 && totalAmount>0 & year10>0) {
//			studentPaymentHistory.setPaid_amount(totalAmount>=year10? year10:totalAmount);
//			totalAmount=(float) (totalAmount-year10);
//			studentPaymentHistory.setTo_pay(year10);
//		}
//
//		if(paidYear==11 && totalAmount>0 & year11>0) {
//			studentPaymentHistory.setPaid_amount(totalAmount>=year11? year11:totalAmount);
//			totalAmount=(float) (totalAmount-year11);
//			studentPaymentHistory.setTo_pay(year11);
//		}
//
//		if(paidYear==12 && totalAmount>0 & year12>0) {
//			studentPaymentHistory.setPaid_amount(totalAmount>=year12? year12:totalAmount);
//			totalAmount=(float) (totalAmount-year12);
//			studentPaymentHistory.setTo_pay(year12);
//		}
//
//		if(ObjectUtils.isNotEmpty(studentPaymentHistory) &&    ObjectUtils.isNotEmpty(studentPaymentHistory.getTo_pay()) && ObjectUtils.isNotEmpty(studentPaymentHistory.getPaid_amount()) && studentPaymentHistory.getTo_pay()>0 && -studentPaymentHistory.getPaid_amount()>0) {
//			balance=studentPaymentHistory.getTo_pay()-studentPaymentHistory.getPaid_amount();
//		}
//		studentPaymentHistory.setBalance_amount(balance>0?balance:0);
//
//		return ObjectUtils.isNotEmpty(studentPaymentHistory.getPaid_amount())  && studentPaymentHistory.getPaid_amount()>0?studentPaymentHistory.getPaid_amount():0;
//	}
//
//	public String generateNextReceiptNumber() {
//		ReceiptCounter receiptCounter = receiptCounterRepository.findById(1L).orElse(new ReceiptCounter());
//
//		Integer nextReceiptNumber = receiptCounter.getLastReceiptNumber() == null ? 1
//				: receiptCounter.getLastReceiptNumber() + 1;
//		receiptCounter.setLastReceiptNumber(nextReceiptNumber);
//		receiptCounterRepository.save(receiptCounter);
//
//		return String.format("%05d", nextReceiptNumber);
//	}
//
//
//
//}
