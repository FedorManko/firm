package com.myproject.firm.dto.request;

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
public class NewEmployeeRequestDto {

  private String firstName;
  private String lastName;
  private String patronymic;
  private LocalDate birthDate;
  private LocalDate hireDate;
  private String description;
  private List<Long> subordinatesIds;

}
