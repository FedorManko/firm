package com.myproject.firm.mapper;

import com.myproject.firm.dto.response.EmployeeDto;
import com.myproject.firm.entity.Employee;
import com.myproject.firm.entity.Manager;
import com.myproject.firm.entity.OtherStaff;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

  EmployeeDto toEmployeeDto(Employee employee);

  EmployeeDto toEmployeeDto(OtherStaff otherStaff);

  EmployeeDto toEmployeeDto(Manager manager);

}
