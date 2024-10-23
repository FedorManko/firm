package com.myproject.firm.service;

import static com.myproject.firm.util.DtoCreator.buildEmployeeDto;
import static com.myproject.firm.util.DtoCreator.buildNUpdateEmployeeRequestDto;
import static com.myproject.firm.util.DtoCreator.buildNewEmployeeRequestDto;
import static com.myproject.firm.util.EntityCreator.buildEmployee;
import static com.myproject.firm.util.EntityCreator.buildManager;
import static com.myproject.firm.util.EntityCreator.buildOtherStaff;
import static com.myproject.firm.util.EntityCreator.buildWorker;
import static com.myproject.firm.util.ErrorCodeConstants.EMPLOYEE_RECORD_NOT_FOUND;
import static com.myproject.firm.util.TestConstants.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.myproject.firm.dto.request.NewEmployeeRequestDto;
import com.myproject.firm.dto.request.UpdateEmployeeRequestDto;
import com.myproject.firm.dto.response.EmployeeDto;
import com.myproject.firm.entity.Employee;
import com.myproject.firm.entity.Manager;
import com.myproject.firm.entity.OtherStaff;
import com.myproject.firm.entity.Worker;
import com.myproject.firm.mapper.EmployeeMapper;
import com.myproject.firm.repository.EmployeeRepository;
import com.myproject.firm.service.impl.EmployeeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

  @InjectMocks
  private EmployeeServiceImpl employeeService;

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private EmployeeMapper employeeMapper;

  @ParameterizedTest
  @MethodSource("provideRelevantSortDefineScenario")
  void findAllEmployees_whenThemPresentInDatabaseAndDefaultSort_thenReturnList(String sort) {
    Employee employee = buildEmployee();
    OtherStaff otherStaff = buildOtherStaff();
    doReturn(List.of(employee, otherStaff)).when(employeeRepository).findAll(any(Sort.class));
    doReturn(buildEmployeeDto()).when(employeeMapper).toEmployeeDto(any(Employee.class));
    doReturn(buildEmployeeDto()).when(employeeMapper).toEmployeeDto(any(OtherStaff.class));

    employeeService.findAllEmployees(sort);

    verify(employeeRepository).findAll(any(Sort.class));
    verify(employeeMapper).toEmployeeDto(any(Employee.class));
    verify(employeeMapper).toEmployeeDto(any(OtherStaff.class));
  }

  private static Stream<Arguments> provideRelevantSortDefineScenario() {
    return Stream.of(
        Arguments.of(""),
        Arguments.of("hireDate"),
        Arguments.of("lastName"));
  }

  @Test
  void addNewEmployees_thenSaveWorker() {
    NewEmployeeRequestDto newEmployeeRequestDto = buildNewEmployeeRequestDto();
    Worker worker = buildWorker();
    doReturn(worker).when(employeeRepository).save(any(Employee.class));

    employeeService.addNewEmployees(newEmployeeRequestDto);

    verify(employeeRepository).save(any(Employee.class));
  }

  @Test
  void addNewEmployees_thenSaveOtherStaff() {
    NewEmployeeRequestDto newEmployeeRequestDto = buildNewEmployeeRequestDto();
    newEmployeeRequestDto.setDescription("Boss");
    OtherStaff otherStaff = buildOtherStaff();
    doReturn(otherStaff).when(employeeRepository).save(any(Employee.class));

    employeeService.addNewEmployees(newEmployeeRequestDto);

    verify(employeeRepository).save(any(Employee.class));
  }

  @Test
  void addNewEmployees_thenSaveManager() {
    NewEmployeeRequestDto newEmployeeRequestDto = buildNewEmployeeRequestDto();
    newEmployeeRequestDto.setSubordinatesIds(List.of(ID));
    Manager manager = buildManager();
    doReturn(manager).when(employeeRepository).save(any(Employee.class));

    employeeService.addNewEmployees(newEmployeeRequestDto);

    verify(employeeRepository).save(any(Employee.class));
  }

  @Test
  void deleteEmployees_whenEmployeeIsPresent_theDeleteEmployee() {
    Employee employee = buildEmployee();
    doReturn(Optional.of(employee)).when(employeeRepository).findById(anyLong());
    doNothing().when(employeeRepository).deleteById(anyLong());

    employeeService.deleteEmployees(ID);

    verify(employeeRepository).findById(anyLong());
    verify(employeeRepository).deleteById(anyLong());
  }

  @Test
  void deleteEmployees_whenEmployeeIsPresent_theThrowEntityNotFoundException() {
    doReturn(Optional.empty()).when(employeeRepository).findById(anyLong());

    Executable actual = () -> employeeService.deleteEmployees(ID);
    EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, actual);
    assertThat(exception.getMessage()).isEqualTo(EMPLOYEE_RECORD_NOT_FOUND);

    verify(employeeRepository).findById(anyLong());
  }

  @Test
  void updateEmployees_whenNeedToUpdate_returnUpdatedEmployee() {
    Employee employee = buildEmployee();
    EmployeeDto employeeDto = buildEmployeeDto();
    doReturn(Optional.of(employee)).when(employeeRepository).findById(anyLong());
    doNothing().when(employeeRepository).deleteById(anyLong());
    doNothing().when(employeeRepository).flush();
    doReturn(employee).when(employeeRepository).save(any(Employee.class));
    doReturn(employeeDto).when(employeeMapper).toEmployeeDto(any(Employee.class));
    UpdateEmployeeRequestDto updateEmployeeRequestDto = buildNUpdateEmployeeRequestDto();
    employeeService.updateEmployees(ID, updateEmployeeRequestDto);

    verify(employeeRepository).findById(anyLong());
    verify(employeeRepository).deleteById(anyLong());
    verify(employeeRepository).flush();
    verify(employeeRepository).save(any(Employee.class));
    verify(employeeMapper).toEmployeeDto(any(Employee.class));
  }

}