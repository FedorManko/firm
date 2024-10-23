package com.myproject.firm.util;

import static com.myproject.firm.enums.EmployeeType.OTHER_STAFF;
import static com.myproject.firm.enums.EmployeeType.WORKER;
import static com.myproject.firm.util.TestConstants.BIRTH_DATE;
import static com.myproject.firm.util.TestConstants.FIRST_NAME;
import static com.myproject.firm.util.TestConstants.HIRE_DATE;
import static com.myproject.firm.util.TestConstants.ID;
import static com.myproject.firm.util.TestConstants.LAST_NAME;
import static com.myproject.firm.util.TestConstants.PATRONYMIC;

import com.myproject.firm.dto.request.NewEmployeeRequestDto;
import com.myproject.firm.dto.request.UpdateEmployeeRequestDto;
import com.myproject.firm.dto.response.EmployeeDto;
import java.time.LocalDate;

public class DtoCreator {

  public static EmployeeDto buildEmployeeDto() {
    return EmployeeDto.builder()
        .id(ID)
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .patronymic(PATRONYMIC)
        .birthDate(LocalDate.parse(BIRTH_DATE))
        .hireDate(LocalDate.parse(HIRE_DATE))
        .employeeType(WORKER)
        .build();
  }

  public static NewEmployeeRequestDto buildNewEmployeeRequestDto() {
    return NewEmployeeRequestDto.builder()
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .patronymic(PATRONYMIC)
        .birthDate(LocalDate.parse(BIRTH_DATE))
        .hireDate(LocalDate.parse(HIRE_DATE))
        .build();
  }

  public static UpdateEmployeeRequestDto buildNUpdateEmployeeRequestDto() {
    return UpdateEmployeeRequestDto.builder()
        .employeeType(OTHER_STAFF)
        .build();
  }

}
