package com.ems.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ems.entity.ActivityLog;
import com.ems.entity.Employee;
import com.ems.repository.ActivityLogRepository;
/*import com.ems.entity.Employee.Status;*/
import com.ems.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private ActivityLogRepository activityLogRepository;

	@InjectMocks
	private EmployeeService employeeService;

	@Test
	public void testSaveEmployee() {

		// Arrange
		Employee employee = new Employee();
		employee.setId(10);
		employee.setFirstName("Ayush");
		employee.setLastName("Das");

		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

		// Act
		Employee created = employeeService.saveEmployee(employee);

		// Assert
		assertEquals("Ayush", created.getFirstName());
		verify(employeeRepository, times(1)).save(employee);
		verify(activityLogRepository, times(1)).save(any(ActivityLog.class));

	}

	@Test
	public void testGetEmployeeById() {
		// Arrange
		Employee employee = new Employee();
		employee.setId(1);
		employee.setFirstName("John");

		when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

		// Act
		Optional<Employee> foundEmployee = employeeService.getEmployeeById(1);

		// Assert
		assertTrue(foundEmployee.isPresent());
		assertEquals("John", foundEmployee.get().getFirstName());
		verify(employeeRepository, times(1)).findById(1);
	}

	@Test
	public void testDeleteEmployee() {
		// Arrange
		int employeeId = 1;
		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(new Employee()));
		doNothing().when(employeeRepository).deleteById(employeeId);

		// Act
		employeeService.deleteEmployee(employeeId);

		// Assert
		verify(employeeRepository, times(1)).deleteById(employeeId);
	}

}
