package com.myproject.firm.service;

import com.myproject.firm.dto.request.NewEmployeeRequestDto;
import com.myproject.firm.dto.request.UpdateEmployeeRequestDto;
import com.myproject.firm.dto.response.EmployeeDto;
import java.util.List;

public interface EmployeeService {

  List<EmployeeDto>  findAllEmployees(String sortBy);

  void addNewEmployees(NewEmployeeRequestDto newEmployeeRequestDto);

  void deleteEmployees(Long employeeId);

  EmployeeDto updateEmployees(Long employeeId, UpdateEmployeeRequestDto updateEmployeeRequestDto);
}
