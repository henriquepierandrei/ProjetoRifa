package com.Rifa.v10.Dtos;

import jakarta.validation.constraints.NotEmpty;

public record RegisterDto(String name, String lastName, @NotEmpty String cpf, String email, String password, @NotEmpty String phone) {
}
