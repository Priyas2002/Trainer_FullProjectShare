package com.ems.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ems.entity.ActivityLog;
import com.ems.entity.Employee;
import com.ems.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/employees")
	public String listEmployees(Model model, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("managerEmail") == null) {
			return "redirect:/loginManager"; // Redirect to login if session is null
		}

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		model.addAttribute("managerFirstName", session.getAttribute("managerFirstName"));
		model.addAttribute("managerLastName", session.getAttribute("managerLastName"));
		model.addAttribute("managerEmail", session.getAttribute("managerEmail"));

		List<Employee> employees = employeeService.getAllEmployees();
		model.addAttribute("totalEmployees", employees.size());
		model.addAttribute("employees", employees);

		model.addAttribute("activeEmployeeCount", employeeService.getEmployeeCountByStatus("ACTIVE"));
		model.addAttribute("inactiveEmployeeCount", employeeService.getEmployeeCountByStatus("INACTIVE"));
		model.addAttribute("onLeaveEmployeeCount", employeeService.getEmployeeCountByStatus("ON LEAVE"));

		return "manager/employees";
	}

	@GetMapping("/dashboard")
	public String showDashboard(Model model, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("managerEmail") == null) {
			return "redirect:/loginManager";
		}

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		model.addAttribute("managerFirstName", session.getAttribute("managerFirstName"));
		model.addAttribute("managerLastName", session.getAttribute("managerLastName"));
		model.addAttribute("managerEmail", session.getAttribute("managerEmail"));

		model.addAttribute("totalEmployees", employeeService.getEmployeeCount());
		model.addAttribute("activeEmployees", employeeService.getEmployeeCountByStatus("ACTIVE"));
		model.addAttribute("inactiveEmployees", employeeService.getEmployeeCountByStatus("INACTIVE"));
		model.addAttribute("onLeaveEmployees", employeeService.getEmployeeCountByStatus("ON LEAVE"));

		List<ActivityLog> activities = employeeService.getRecentActivities();
		model.addAttribute("recentActivities", activities);

		return "manager/dashboard";
	}

	@GetMapping("/employees/add")
	public String showAddEmployeeForm(Model model, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("managerEmail") == null) {
			return "redirect:/loginManager"; // Redirect to login if session is missing
		}

		model.addAttribute("managerFirstName", session.getAttribute("managerFirstName"));
		model.addAttribute("managerLastName", session.getAttribute("managerLastName"));
		model.addAttribute("managerEmail", session.getAttribute("managerEmail"));
		model.addAttribute("employee", new Employee());

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		return "manager/addEmployee";
	}

	@PostMapping("/employees/add")
	public String addEmployee(@RequestParam("first_name") String firstName, @RequestParam("last_name") String lastName,
			@RequestParam("email") String email, @RequestParam("phone_no") String phoneNumber,
			@RequestParam("position") String position, @RequestParam("status") String status,
			@RequestParam("joined_date") String joinedDate, Model model, HttpServletRequest request) {

		Employee employee = new Employee();
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setEmail(email);
		employee.setPhoneNumber(phoneNumber);
		employee.setPosition(position);
		employee.setStatus(status);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		employee.setJoinedDate(LocalDate.parse(joinedDate, formatter));

		try {
			employeeService.saveEmployee(employee);
			return "redirect:/manager/employees";
		} catch (IllegalArgumentException e) {
			model.addAttribute("errorMessage", e.getMessage());

			HttpSession session = request.getSession(false);
			if (session != null) {
				model.addAttribute("managerFirstName", session.getAttribute("managerFirstName"));
				model.addAttribute("managerLastName", session.getAttribute("managerLastName"));
				model.addAttribute("managerEmail", session.getAttribute("managerEmail"));
			}
			return "manager/addEmployee";
		}
	}

	@GetMapping("/update/{id}")
	public String showUpdateEmployeeForm(@PathVariable int id, Model model) {
		Employee employee = employeeService.getEmployeeById(id).orElse(null);
		if (employee == null) {
			return "redirect:/manager/employees";
		}
		model.addAttribute("employee", employee);
		return "manager/updateEmployee";
	}

	@PostMapping("/employees/update")
	public String updateEmployee(@RequestParam("id") int id, @RequestParam("first_name") String firstName,
			@RequestParam("last_name") String lastName, @RequestParam("email") String email,
			@RequestParam("phone_no") String phoneNumber, @RequestParam("position") String position,
			@RequestParam("status") String status, @RequestParam("joined_date") String joinedDate, Model model,
			HttpServletRequest request) {

		Employee employee = employeeService.getEmployeeById(id).orElse(null);

		if (employee == null) {
			return "redirect:/manager/employees";
		}

		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setEmail(email);
		employee.setPhoneNumber(phoneNumber);
		employee.setPosition(position);
		employee.setStatus(status);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		employee.setJoinedDate(LocalDate.parse(joinedDate, formatter));

		try {
			employeeService.saveEmployee(employee);
			return "redirect:/manager/employees";
		} catch (IllegalArgumentException e) {
			model.addAttribute("errorMessage", e.getMessage());

			model.addAttribute("employee", employee);

			HttpSession session = request.getSession(false);
			if (session != null) {
				model.addAttribute("managerFirstName", session.getAttribute("managerFirstName"));
				model.addAttribute("managerLastName", session.getAttribute("managerLastName"));
				model.addAttribute("managerEmail", session.getAttribute("managerEmail"));
			}
			return "manager/updateEmployee";
		}

	}

	@GetMapping("/employees/delete/{id}")
	public String deleteEmployee(@PathVariable int id) {
		employeeService.deleteEmployee(id);
		return "redirect:/manager/employees";
	}

}
