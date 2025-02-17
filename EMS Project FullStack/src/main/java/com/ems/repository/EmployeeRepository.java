package com.ems.repository;

import com.ems.entity.Employee;
/*import com.ems.entity.Employee.Status;*/

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	@Query("SELECT COUNT(e) FROM Employee e WHERE e.status = :status")
	int countEmployeesByStatus(@Param("status") String status);

	List<Employee> findByStatus(String status);

	Optional<Employee> findByEmail(String email);

	Optional<Employee> findByPhoneNumber(String phoneNumber);
}
