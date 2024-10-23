package com.myproject.firm.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.myproject.firm.enums.EmployeeType;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class EmployeeDto {

  private Long id;
  private String firstName;
  private String lastName;
  private String patronymic;
  private LocalDate birthDate;
  private LocalDate hireDate;
  private EmployeeType employeeType;
  private String description;
  private List<EmployeeDto> subordinates;

}
