package com.myproject.firm.repository;

import com.myproject.firm.entity.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findByIdIn(List<Long> subordinatesIds);

}
