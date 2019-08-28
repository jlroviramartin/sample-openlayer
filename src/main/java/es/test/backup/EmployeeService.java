package es.test.backup;

import java.util.ArrayList;
import java.util.Collection;

public class EmployeeService {
	public static Collection<Employee> getAllEmployees() {
		return new ArrayList<Employee>();
	}

	public static Employee getEmployee(int empId) {
		return new Employee();
	}

	public static Employee createEmployee(Employee employee) {
		return employee;
	}

	public static void deleteEmployee(Employee employee) {
	}
}
