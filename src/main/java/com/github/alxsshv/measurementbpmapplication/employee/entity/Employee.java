package com.github.alxsshv.measurementbpmapplication.employee.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private String id;
    @NotNull(message = "Укажите имя поверителя")
    private String name;
    @NotNull(message = "Укажите фамилию поверителя")
    private String surname;
    @NotNull(message = "Укажите отчество поверителя")
    private String patronymic;
    @NotNull(message = "Укажите СНИЛС поверителя")
    @Size(min = 11,max = 11, message = "Неверный формат СНИЛС")
    private String snils;

    public void updateFrom(Employee employeeData){
        this.name = employeeData.getName();
        this.surname = employeeData.getSurname();
        this.patronymic =employeeData.getPatronymic();
        this.snils = employeeData.getSnils();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(name, employee.name) && Objects.equals(surname, employee.surname) && Objects.equals(patronymic, employee.patronymic) && Objects.equals(snils, employee.snils);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, snils);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", snils='" + "****" + '\'' +
                '}';
    }
}
