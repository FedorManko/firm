package com.myproject.firm.controller;

import com.myproject.firm.dto.request.NewEmployeeRequestDto;
import com.myproject.firm.dto.request.UpdateEmployeeRequestDto;
import com.myproject.firm.dto.response.EmployeeDto;
import com.myproject.firm.service.EmployeeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

  private final EmployeeService employeeService;

  @GetMapping
  public ResponseEntity<List<EmployeeDto>> findAllEmployees(@RequestParam String sortBy) {
    List<EmployeeDto> response = employeeService.findAllEmployees(sortBy);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/new")
  @PreAuthorize("hasAnyRole('client_manager')")
  public ResponseEntity<Void> addNewEmployees(
      @RequestBody NewEmployeeRequestDto newEmployeeRequestDto) {
    employeeService.addNewEmployees(newEmployeeRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteEmployees(@RequestParam Long employeeId) {
    employeeService.deleteEmployees(employeeId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/update")
  public ResponseEntity<EmployeeDto> updateEmployees(@RequestParam Long employeeId,
      @RequestBody UpdateEmployeeRequestDto updateEmployeeRequestDto) {
    EmployeeDto response = employeeService.updateEmployees(employeeId, updateEmployeeRequestDto);
    return ResponseEntity.ok(response);
  }

}
