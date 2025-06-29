package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.dto.FrroResponseDTO;
import com.au.model.FRRO;

@Repository
public interface FrroRepository extends JpaRepository<FRRO, Integer>{

	@Query(value= " select s.student_id as studentId, s.auid as auid, s.student_name as studentName, "
			+ " s.acharya_email as email, p.program_short_name as branch,"
			+ " ps.program_specialization_short_name as course, s.dateofbirth as dateofbirth, rs.current_year as currentYear, rs.current_sem as currentSem "
			+ "  from Student_Details s "
			+ " left join Program p on p.program_id=s.program_id"
			+ " left join ProgramSpecilization ps on ps.program_specialization_id=s.program_specialization_id"
			+ " left join ReportingStudents rs on rs.student_id=s.student_id  "
			+ " where s.student_id=:studentId", nativeQuery = false)
	Map<String, Object> getStudentDetailsForFrro(Integer studentId);

	@Query(value= " select s.student_id as studentId, s.auid as auid, s.student_name as studentName from Student_Details s where s.fee_admission_category_id=2 ", nativeQuery = false)
	List<Map<String, Object>> getStudentDetailsListForFrro();
	
	FRRO getByStudentId(Integer studentId);

	@Query(value="select fr.frro_id as frroId, "
			+ "fr.name_as_per_passport as nameAsPerPassport, "
			+ "fr.passport_no as passportNo, "
			+ "fr.visa_no as visaNo, "
			+ "fr.fsis as fsis, "
			+ "fr.birth_place as birthPlace, "
			+ "fr.passport_issue_place as passportIssuePlace, "
			+ "fr.visa_type as visaType, "
			+ "fr.immigration_date as immigrationDate, "
			+ "fr.port_of_arrival as portOfArrival, "
			+ "fr.passport_issue_date as passportIssueDate, "
			+ "fr.type_of_entry as typeOfEntry, "
			+ "fr.issue_by as issueBy, "
			+ "fr.port_of_departure as portOfDeparture, "
			+ "fr.passport_expiry_date as passportExpiryDate, "
			+ "fr.place_of_visa_issue as placeOfVisaIssue, "
			+ "fr.rp_no as rpNo, "
			+ "fr.is_reported_to_india as isReportedToIndia, "
			+ "fr.reported_on as reportedOn, "
			+ "fr.visa_issue_date as visaIssueDate, "
			+ "fr.rp_issue_date as rpIssueDate, "
			+ "fr.remarks as remarks, "
			+ "fr.visa_expiry_date as visaExpiryDate, "
			+ "fr.rp_expiry_date as rpExpiryDate, "
			+ "fr.alu_equivalence_document as aluEquivalenceDocument, "
			+ "fr.passport_copy_document as passportCopyDocument, "
			+ "fr.visa_copy_document as visaCopyDocument, "
			+ "fr.residential_permit_copy_document as residentialPermitCopyDocument, "
			+ "fr.student_id as studentId, "
			+ "s.student_name as studentName, "
			+ "s.dateofbirth as dateOfBirth, "
			+ "s.candidate_sex as sex, "
			+ "s.father_name as fatherName, "
			+ "s.current_address as currentAddress, "
			+ "s.permanent_address as permanentAddress, "
			+ "s.local_adress1 As local_adress1,"
			+ "s.usn as usn, "
			+ "s.auid as auid, "
			+ "rs.current_year as currentYear, "
			+ "rs.current_sem as currentSem, "
			+ "pr.program_short_name as programShortName, "
			+ "ps.program_specialization_short_name as programSpecializationShortName, "
			+ "fr.visa_issued as visaIssued, "
			+ "fr.recognition as recognition, "
			+ "fr.affiliataion as affiliataion, "
			+ "n.nationality as nationality "
			+ " from frro fr "
			+ " left join student_details s on s.student_id=fr.student_id "
			+ " left join reporting_students rs on rs.student_id=fr.student_id"
			+ " left join program_assignment p on  p.program_assignment_id=s.program_assignment_id"
			+ " left join program pr on pr.program_id=p.program_id"
			+ " left join nationality n on n.nationality_id=s.nationality "
			+ " left join program_specialization ps on ps.program_specialization_id=s.program_specialization_id  ", nativeQuery = true)
        List<Map<String, Object>> getFrroDetails();
	
	@Query(value="select fr.frro_id as frroId, "
			+ "fr.name_as_per_passport as nameAsPerPassport, "
			+ "fr.passport_no as passportNo, "
			+ "fr.visa_no as visaNo, "
			+ "fr.fsis as fsis, "
			+ "fr.birth_place as birthPlace, "
			+ "fr.passport_issue_place as passportIssuePlace, "
			+ "fr.visa_type as visaType, "
			+ "fr.immigration_date as immigrationDate, "
			+ "fr.port_of_arrival as portOfArrival, "
			+ "fr.passport_issue_date as passportIssueDate, "
			+ "fr.type_of_entry as typeOfEntry, "
			+ "fr.issue_by as issueBy, "
			+ "fr.port_of_departure as portOfDeparture, "
			+ "fr.passport_expiry_date as passportExpiryDate, "
			+ "fr.place_of_visa_issue as placeOfVisaIssue, "
			+ "fr.rp_no as rpNo, "
			+ "fr.is_reported_to_india as isReportedToIndia, "
			+ "fr.reported_on as reportedOn, "
			+ "fr.visa_issue_date as visaIssueDate, "
			+ "fr.rp_issue_date as rpIssueDate, "
			+ "fr.remarks as remarks, "
			+ "fr.visa_expiry_date as visaExpiryDate, "
			+ "fr.rp_expiry_date as rpExpiryDate, "
			+ "fr.alu_equivalence_document as aluEquivalenceDocument, "
			+ "fr.passport_copy_document as passportCopyDocument, "
			+ "fr.visa_copy_document as visaCopyDocument, "
			+ "fr.residential_permit_copy_document as residentialPermitCopyDocument, "
			+ "fr.student_id as studentId, "
			+ "s.student_name as studentName, "
			+ "s.dateofbirth as dateOfBirth, "
			+ "s.candidate_sex as sex, "
			+ "s.father_name as fatherName, "
			+ "s.current_address as currentAddress, "
			+ "s.permanent_address as permanentAddress, "
			+ "s.local_adress1 As local_adress1,"
			+ "s.usn as usn, "
			+ "s.auid as auid, "
			+ "rs.current_year as currentYear, "
			+ "rs.current_sem as currentSem, "
			+ "pr.program_short_name as programShortName, "
			+ "ps.program_specialization_short_name as programSpecializationShortName, "
			+ "fr.visa_issued as visaIssued, "
			+ "fr.recognition as recognition, "
			+ "fr.affiliataion as affiliataion, "
			+ "n.nationality as nationality "
			+ " from frro fr "
			+ " left join student_details s on s.student_id=fr.student_id "
			+ " left join reporting_students rs on rs.student_id=fr.student_id"
			+ " left join program_assignment p on  p.program_assignment_id=s.program_assignment_id"
			+ " left join program pr on pr.program_id=p.program_id"
			+ " left join nationality n on n.nationality_id=s.nationality "
			+ " left join program_specialization ps on ps.program_specialization_id=s.program_specialization_id where s.auid=:auid ", nativeQuery = true)
	Map<String, Object> getFrroByStudentId(String auid);

	@Query(value= " select s.student_id as studentId, s.auid as auid, s.student_name as studentName from Student_Details s where s.fee_admission_category_id=2 and s.auid like %:auid% ", nativeQuery = false)
	List<Map<String, Object>> searchStudentDetailsListForFrro(String auid);

	@Query(value= " select s.student_id as studentId, s.auid as auid, s.student_name as studentName from FRRO fr  left join  Student_Details s on fr.studentId=s.student_id  ", nativeQuery = false)
	List<Map<String, Object>> getFrroStudentList();

	@Query(value= " select s.student_id as studentId, s.auid as auid, s.student_name as studentName from FRRO fr  left join  Student_Details s on fr.studentId=s.student_id where  s.auid like %:auid% ", nativeQuery = false)
	List<Map<String, Object>> getFrroStudentList(String auid);
	
}

