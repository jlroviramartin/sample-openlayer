package es.test.backup;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//@Path("employees")
public class EmployeeResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Employee> getAllEmployees() {
		return new ArrayList<>(EmployeeService.getAllEmployees());
	}

	@GET
	@Path("/{employeeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Employee getEmployee(@PathParam("employeeId") int empId) {
		Employee emp = EmployeeService.getEmployee(empId);
		emp.setId(empId);
		emp.setFirstName("name1 " + empId);
		emp.setLastName("name2 " + empId);
		return emp;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Employee createEmployee(Employee emp) {
		Employee employee = EmployeeService.createEmployee(emp);
		return employee;
	}
}