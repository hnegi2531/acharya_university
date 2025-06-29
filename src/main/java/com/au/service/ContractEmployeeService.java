package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.EmployeeDetails;
import com.au.repository.EmployeeDetailsRepository;

@Service
public class ContractEmployeeService {

	@Autowired
	private EmployeeDetailsRepository empDetail_repo;

	public List<EmployeeDetails> listAll() {
		return empDetail_repo.findAll();
	}

	public EmployeeDetails saveContractEmployee(EmployeeDetails contractEmployee) {
		String val = getMasterCode();
		// String val1 = getContractMasterCode();
		// String[] exe = { val, val1 };
		// List<String> string = Arrays.asList(exe);
		// String max = Collections.max(string);
		if (val == null) {
			int num1 = 0;
			int add1 = 1;
			num1 = num1 + add1;
			String formattedStrr = String.format("%05d", num1);
			contractEmployee.setMaster_code("AU" + formattedStrr);
		} else {
			String[] arrOfStrr = val.split("U");
			String str2 = arrOfStrr[1];
			int num1 = Integer.parseInt(str2);
			int add1 = 1;
			num1 = num1 + add1;
			String formattedStr1 = String.format("%05d", num1);
			contractEmployee.setMaster_code("AU" + formattedStr1);
		}
		String str = getAllDetails();
		if (str == null) {
			if (contractEmployee.getContract_emp_type().equals("Regular")) {
				String str11 = "RC";
				int num = 0;
				int add = 1;
				num = num + add;
				String formattedStr = String.format("%03d", num);
				contractEmployee.setContract_empcode(contractEmployee.getSchool() + str11 + formattedStr);
			} else {
				if (contractEmployee.getContract_emp_type().equals("Non-Regular")) {
					String str2 = "NRC";
					int num = 0;
					int add = 1;
					num = num + add;
					String formattedStr = String.format("%03d", num);
					contractEmployee.setContract_empcode(contractEmployee.getSchool() + str2 + formattedStr);
				}
			}
		}

		else {
			if (contractEmployee.getContract_emp_type().equals("Regular")) {
				String str11 = "RC";
				String[] part = str.split("(?<=\\D)(?=\\d)");// D=decimal digit
				String str1 = part[1];
				int num = Integer.parseInt(str1);
				int add = 1;
				num = num + add;
				String formattedStr = String.format("%03d", num);
				contractEmployee.setContract_empcode(contractEmployee.getSchool() + str11 + formattedStr);
			} else {
				if (contractEmployee.getContract_emp_type().equals("Non-Regular")) {
					String str2 = "NRC";
					String[] part = str.split("(?<=\\D)(?=\\d)");// D=decimal digit
					String str1 = part[1];
					int num = Integer.parseInt(str1);
					int add = 1;
					num = num + add;
					String formattedStr = String.format("%03d", num);
					contractEmployee.setContract_empcode(contractEmployee.getSchool() + str2 + formattedStr);
				}
			}
		}
		return empDetail_repo.save(contractEmployee);
	}

	/*
	 * private String getContractMasterCode() { return
	 * conEmp_repo.fetchgetMaxMasterCode(); }
	 */
	private String getMasterCode() {
		return empDetail_repo.fetchgetMaxMasterCode();
	}

	private String getAllDetails() {
		return empDetail_repo.fetchContractEmployeeCodes();
	}

	public EmployeeDetails get(Integer id) {
		return empDetail_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contract Employee Not Found:" + id));
	}

	public void delete(Integer id) {
		EmployeeDetails cc = empDetail_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contract Employee Not Found:" + id));
		empDetail_repo.delete(cc);
	}

	public EmployeeDetails saveContractEmployees(EmployeeDetails cb) {
		return empDetail_repo.save(cb);
	}
}
