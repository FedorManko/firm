package com.myproject.firm.service.impl;

import static com.myproject.firm.enums.EmployeeType.MANAGER;
import static com.myproject.firm.enums.EmployeeType.OTHER_STAFF;
import static com.myproject.firm.enums.EmployeeType.WORKER;
import static com.myproject.firm.util.ErrorCodeConstants.EMPLOYEE_RECORD_NOT_FOUND;

import com.myproject.firm.dto.request.NewEmployeeRequestDto;
import com.myproject.firm.dto.request.UpdateEmployeeRequestDto;
import com.myproject.firm.dto.response.EmployeeDto;
import com.myproject.firm.entity.Employee;
import com.myproject.firm.entity.Manager;
import com.myproject.firm.entity.OtherStaff;
import com.myproject.firm.entity.Worker;
import com.myproject.firm.enums.EmployeeType;
import com.myproject.firm.mapper.EmployeeMapper;
import com.myproject.firm.repository.EmployeeRepository;
import com.myproject.firm.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private static final String HIRE_DATE = "hireDate";
  private static final String LAST_NAME = "lastName";

  private final EmployeeRepository employeeRepository;
  private final EmployeeMapper employeeMapper;

  @Override
  public List<EmployeeDto> findAllEmployees(String sortBy) {
    List<Employee> employees;

    employees = getSortedEmployees(sortBy);

    List<EmployeeDto> employeeDtos = new ArrayList<>();

    mapEmployees(employees, employeeDtos);
    return employeeDtos;
  }

  private List<Employee> getSortedEmployees(String sortBy) {
    Sort sort = switch (sortBy) {
      case HIRE_DATE -> Sort.by(Sort.Direction.ASC, HIRE_DATE);
      case LAST_NAME -> Sort.by(Sort.Direction.ASC, LAST_NAME);
      default -> Sort.by(Sort.Direction.ASC, "id"); // Сортировка по умолчанию по id
    };

    return employeeRepository.findAll(sort);
  }

  @Override
  @Transactional
  public void addNewEmployees(NewEmployeeRequestDto newEmployeeRequestDto) {

    if (newEmployeeRequestDto.getDescription() == null
        && newEmployeeRequestDto.getSubordinatesIds() == null) {
      saveWorker(newEmployeeRequestDto);
    } else if (newEmployeeRequestDto.getSubordinatesIds() == null) {
      saveOtherStaff(newEmployeeRequestDto);
    } else {
      saveManager(newEmployeeRequestDto);
    }
  }

  @Override
  @Transactional
  public void deleteEmployees(Long employeeId) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_RECORD_NOT_FOUND));

    deleteEmpl(employeeId, employee);
  }

  private void deleteEmpl(Long employeeId, Employee employee) {
    if (employee instanceof Worker worker && worker.getManager() != null) {
      worker.getManager().getSubordinates().remove(worker);
      worker.setManager(null);
    }

    employeeRepository.deleteById(employeeId);
  }

  @Override
  @Transactional
  public EmployeeDto updateEmployees(Long employeeId,
      UpdateEmployeeRequestDto updateEmployeeRequestDto) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_RECORD_NOT_FOUND));

    EmployeeType newEmployeeType = updateEmployeeRequestDto.getEmployeeType();
    EmployeeType currentEmployeeType = employee.getEmployeeType();
    deleteEmpl(employeeId, employee);
    employeeRepository.flush();
    Employee updatedEmployee;

    if (newEmployeeType != null && !newEmployeeType.equals(MANAGER)) {
      updatedEmployee = updateEmployeeType(employee, currentEmployeeType, newEmployeeType);
    } else {
      updatedEmployee = updateManager(employee, updateEmployeeRequestDto.getSubordinatesIds());
    }

    return employeeMapper.toEmployeeDto(updatedEmployee);
  }

  private Employee updateEmployeeType(Employee employee, EmployeeType currentType,
      EmployeeType newType) {

    if (currentType.equals(MANAGER)) {
      Manager manager = (Manager) employee;
      manager.getSubordinates().forEach(subordinate -> subordinate.setManager(null));
    }

    return switch (newType) {
      case WORKER -> saveWorker(employee);
      case OTHER_STAFF -> saveOtherStaff(employee);
      default -> throw new IllegalArgumentException("Unknown employee type: " + newType);
    };
  }

  private Employee updateManager(Employee employee, List<Long> subordinatesIds) {

    if (subordinatesIds == null || subordinatesIds.isEmpty()) {
      return saveManager(employee);
    }

    return saveManagerWithSubordinates(employee, subordinatesIds);
  }

  private Employee saveManagerWithSubordinates(Employee employee, List<Long> subordinatesIds) {
    List<Employee> subordinates = employeeRepository.findByIdIn(subordinatesIds)
        .stream()
        .filter(subordinate -> subordinate.getEmployeeType().equals(WORKER))
        .toList();
    Manager manager = Manager.builder()
        .firstName(employee.getFirstName())
        .lastName(employee.getLastName())
        .patronymic(employee.getPatronymic())
        .birthDate(employee.getBirthDate())
        .hireDate(employee.getHireDate())
        .employeeType(MANAGER)
        .build();
    subordinates.forEach(subordinate -> subordinate.setManager(manager));
    return employeeRepository.save(manager);
  }

  private Employee saveManager(Employee employee) {
    Manager manager = Manager.builder()
        .firstName(employee.getFirstName())
        .lastName(employee.getLastName())
        .patronymic(employee.getPatronymic())
        .birthDate(employee.getBirthDate())
        .hireDate(employee.getHireDate())
        .employeeType(MANAGER)
        .build();
    return employeeRepository.save(manager);
  }

  private void saveManager(NewEmployeeRequestDto newEmployeeRequestDto) {
    List<Employee> subordinates = employeeRepository.findByIdIn(
            newEmployeeRequestDto.getSubordinatesIds())
        .stream()
        .filter(employee -> employee.getEmployeeType().equals(WORKER))
        .toList();
    Manager manager = Manager.builder()
        .firstName(newEmployeeRequestDto.getFirstName())
        .lastName(newEmployeeRequestDto.getLastName())
        .patronymic(newEmployeeRequestDto.getPatronymic())
        .birthDate(newEmployeeRequestDto.getBirthDate())
        .hireDate(newEmployeeRequestDto.getHireDate())
        .employeeType(MANAGER)
        .subordinates(subordinates)
        .build();
    subordinates.forEach(subordinate -> subordinate.setManager(manager));
    employeeRepository.save(manager);
  }

  private Employee saveOtherStaff(Employee employee) {
    OtherStaff worker = OtherStaff.builder()
        .firstName(employee.getFirstName())
        .lastName(employee.getLastName())
        .patronymic(employee.getPatronymic())
        .birthDate(employee.getBirthDate())
        .hireDate(employee.getHireDate())
        .employeeType(OTHER_STAFF)
        .build();
    return employeeRepository.save(worker);
  }

  private void saveOtherStaff(NewEmployeeRequestDto newEmployeeRequestDto) {
    OtherStaff worker = OtherStaff.builder()
        .firstName(newEmployeeRequestDto.getFirstName())
        .lastName(newEmployeeRequestDto.getLastName())
        .patronymic(newEmployeeRequestDto.getPatronymic())
        .birthDate(newEmployeeRequestDto.getBirthDate())
        .hireDate(newEmployeeRequestDto.getHireDate())
        .employeeType(OTHER_STAFF)
        .description(newEmployeeRequestDto.getDescription())
        .build();
    employeeRepository.save(worker);
  }

  private Employee saveWorker(Employee employee) {
    Worker worker = Worker.builder()
        .firstName(employee.getFirstName())
        .lastName(employee.getLastName())
        .patronymic(employee.getPatronymic())
        .birthDate(employee.getBirthDate())
        .hireDate(employee.getHireDate())
        .employeeType(WORKER)
        .build();
    return employeeRepository.save(worker);
  }

  private void saveWorker(NewEmployeeRequestDto newEmployeeRequestDto) {
    Worker worker = Worker.builder()
        .firstName(newEmployeeRequestDto.getFirstName())
        .lastName(newEmployeeRequestDto.getLastName())
        .patronymic(newEmployeeRequestDto.getPatronymic())
        .birthDate(newEmployeeRequestDto.getBirthDate())
        .hireDate(newEmployeeRequestDto.getHireDate())
        .employeeType(WORKER)
        .build();
    employeeRepository.save(worker);
  }

  private void mapEmployees(List<Employee> employees, List<EmployeeDto> employeeDtos) {
    for (Employee employee : employees) {

      EmployeeDto employeeDto;
      switch (employee) {
        case OtherStaff otherStaff -> employeeDto = employeeMapper.toEmployeeDto(otherStaff);
        case Manager manager -> employeeDto = employeeMapper.toEmployeeDto(manager);
        default -> employeeDto = employeeMapper.toEmployeeDto(employee);
      }
      employeeDtos.add(employeeDto);
    }
  }

}
