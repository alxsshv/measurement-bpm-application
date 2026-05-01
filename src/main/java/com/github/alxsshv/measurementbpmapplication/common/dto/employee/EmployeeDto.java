package com.github.alxsshv.measurementbpmapplication.common.dto.employee;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmployeeDto(
        String id,
        @NotNull(message = "Укажите имя поверителя")
        String name,
        @NotNull(message = "Укажите фамилию поверителя")
        String surname,
        @NotNull(message = "Укажите отчество поверителя")
        String patronymic,
        @NotNull(message = "Укажите СНИЛС поверителя")
        @Size(min = 11,max = 11, message = "Неверный формат СНИЛС (11 чисел без пробелов и символов)")
        String snils
)
{}
