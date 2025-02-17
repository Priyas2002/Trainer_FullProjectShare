package com.ems.service;

import com.ems.entity.ActivityLog;
import com.ems.entity.Employee;
import com.ems.repository.ActivityLogRepository;
import com.ems.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    // Fetch All Employees
    public List<Employee> getAllEmployees() {
        try {
            List<Employee> employees = employeeRepository.findAll();
            logger.info("Total employees found: {}", employees.size());
            return employees;
        } catch (Exception e) {
            logger.error("Error fetching employees: {}", e.getMessage());
            throw new RuntimeException("Error fetching employees", e);
        }
    }

    // Save Employee (Handles Both Add & Update)
    public Employee saveEmployee(Employee employee) {
        try {
            boolean isNew = (employee.getId() == 0);
            String action = isNew ? "added" : "updated";

            // Check if an employee with the same email already exists
            Optional<Employee> existingEmployeeByEmail = employeeRepository.findByEmail(employee.getEmail());
            if (existingEmployeeByEmail.isPresent() && existingEmployeeByEmail.get().getId() != employee.getId()) {
                throw new IllegalArgumentException("Email already exists. Please use a different email.");
            }

            // Check if an employee with the same phone number already exists
            Optional<Employee> existingEmployeeByPhone = employeeRepository.findByPhoneNumber(employee.getPhoneNumber());
            if (existingEmployeeByPhone.isPresent() && existingEmployeeByPhone.get().getId() != employee.getId()) {
                throw new IllegalArgumentException("Phone number already exists. Please use a different phone number.");
            }

            // Save the employee
            Employee savedEmployee = employeeRepository.save(employee);
            logger.info("Employee {} {} successfully {}", employee.getFirstName(), employee.getLastName(), action);

            
            activityLogRepository.save(new ActivityLog(employee.getFirstName() + " " + employee.getLastName() + " was " + action));

            
            cleanOldActivities();

            return savedEmployee;

        } catch (IllegalArgumentException e) {
            
            logger.warn("Duplicate Entry: {}", e.getMessage());
            throw e; 
        } catch (Exception e) {
           
            logger.error("Error saving employee: {}", e.getMessage());
            throw new RuntimeException("Error saving employee", e);
        }
    }


    // Fetch Employee by ID
    public Optional<Employee> getEmployeeById(int id) {
        try {
            Optional<Employee> employee = employeeRepository.findById(id);
            if (employee.isPresent()) {
                logger.info("Fetching employee with ID: {}", id);
                return employee;
            } else {
                logger.warn("No employee found with ID: {}", id);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error fetching employee with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error fetching employee", e);
        }
    }

    // Delete Employee
    public void deleteEmployee(int id) {
        try {
            Optional<Employee> employee = employeeRepository.findById(id);
            if (employee.isPresent()) {
                employeeRepository.deleteById(id);
                logger.info("Employee with ID {} deleted successfully", id);

                
                activityLogRepository.save(new ActivityLog(employee.get().getFirstName() + " " + employee.get().getLastName() + " was deleted"));

                
                cleanOldActivities();
            } else {
                logger.warn("Attempted to delete non-existing employee with ID {}", id);
            }
        } catch (Exception e) {
            logger.error("Error deleting employee with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error deleting employee", e);
        }
    }

    // Fetch Total Employee Count
    public long getEmployeeCount() {
        try {
            long count = employeeRepository.count();
            logger.info("Total employee count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Error fetching employee count: {}", e.getMessage());
            throw new RuntimeException("Error fetching employee count", e);
        }
    }

    // Get Employee Count by Status
    public int getEmployeeCountByStatus(String status) {
        try {
            int count = employeeRepository.countEmployeesByStatus(status);
            logger.info("Total employees with status {}: {}", status, count);
            return count;
        } catch (Exception e) {
            logger.error("Error fetching employee count for status {}: {}", status, e.getMessage());
            throw new RuntimeException("Error fetching employee count by status", e);
        }
    }


    
    public List<ActivityLog> getRecentActivities() {
        try {
            return activityLogRepository.findLatestActivities();
        } catch (Exception e) {
            logger.error("Error fetching recent activities: {}", e.getMessage());
            throw new RuntimeException("Error fetching recent activities", e);
        }
    }

    // Automatically Keep Only Last 6 Activities
    private void cleanOldActivities() {
        try {
            List<ActivityLog> activities = activityLogRepository.findAll();
            if (activities.size() > 6) {
                activityLogRepository.delete(activities.get(0)); // Remove the oldest activity
                logger.info("Removed oldest activity to maintain last 6 logs.");
            }
        } catch (Exception e) {
            logger.error("Error cleaning old activities: {}", e.getMessage());
        }
    }
}