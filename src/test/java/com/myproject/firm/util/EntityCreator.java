package com.myproject.firm.util;

import static com.myproject.firm.enums.EmployeeType.WORKER;
import static com.myproject.firm.util.TestConstants.BIRTH_DATE;
import static com.myproject.firm.util.TestConstants.FIRST_NAME;
import static com.myproject.firm.util.TestConstants.HIRE_DATE;
import static com.myproject.firm.util.TestConstants.ID;
import static com.myproject.firm.util.TestConstants.LAST_NAME;
import static com.myproject.firm.util.TestConstants.PATRONYMIC;

import com.myproject.firm.entity.Employee;
import com.myproject.firm.entity.Manager;
import com.myproject.firm.entity.OtherStaff;
import com.myproject.firm.entity.Worker;
import java.time.LocalDate;
import java.util.List;

public class EntityCreator {

  public static Employee buildEmployee() {
    return Employee.builder()
        .id(ID)
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .patronymic(PATRONYMIC)
        .birthDate(LocalDate.parse(BIRTH_DATE))
        .hireDate(LocalDate.parse(HIRE_DATE))
        .employeeType(WORKER)
        .build();
  }

  public static OtherStaff buildOtherStaff() {
    return OtherStaff.builder()
        .id(ID)
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .patronymic(PATRONYMIC)
        .birthDate(LocalDate.parse(BIRTH_DATE))
        .hireDate(LocalDate.parse(HIRE_DATE))
        .employeeType(WORKER)
        .build();
  }

  public static Worker buildWorker() {
    return Worker.builder()
        .id(ID)
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .patronymic(PATRONYMIC)
        .birthDate(LocalDate.parse(BIRTH_DATE))
        .hireDate(LocalDate.parse(HIRE_DATE))
        .employeeType(WORKER)
        .build();
  }

  public static Manager buildManager() {
    return Manager.builder()
        .id(ID)
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .patronymic(PATRONYMIC)
        .birthDate(LocalDate.parse(BIRTH_DATE))
        .hireDate(LocalDate.parse(HIRE_DATE))
        .employeeType(WORKER)
        .subordinates(List.of(buildEmployee()))
        .build();
  }

}
