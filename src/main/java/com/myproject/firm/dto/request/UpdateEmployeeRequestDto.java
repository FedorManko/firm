package com.myproject.firm.dto.request;

import com.myproject.firm.enums.EmployeeType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequestDto {

  private EmployeeType employeeType;
  private List<Long> subordinatesIds;

}
