package com.myproject.firm.entity;

import com.myproject.firm.enums.EmployeeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@SuperBuilder
@Table(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @EqualsAndHashCode.Include
  private String firstName;

  @EqualsAndHashCode.Include
  private String lastName;

  @EqualsAndHashCode.Include
  private String patronymic;

  @EqualsAndHashCode.Include
  private LocalDate birthDate;

  @EqualsAndHashCode.Include
  private LocalDate hireDate;

  @EqualsAndHashCode.Include
  @Enumerated(EnumType.STRING)
  private EmployeeType employeeType;

  @ManyToOne
  @JoinColumn(name = "manager_id")
  private Manager manager;

  @CreationTimestamp
  private Instant created;

  @UpdateTimestamp
  private Instant updated;

}
