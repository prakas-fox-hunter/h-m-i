package com.example.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 3, max = 50, message = "Username harus 3-50 karakter")
    private String username;

    // @NotBlank(message = "Nama tidak boleh kosong")
    // @Size(min = 3, max = 50, message = "Nama harus 3-50 karakter")
    // private String Nama;

    // @NotNull(message = "Tanggal lahir tidak boleh kosong")
    // @Past(message = "Tanggal lahir harus di masa lalu")
    // private LocalDate tanggalLahir;

    // @NotBlank(message = "Address tidak boleh kosong")
    // private String address;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    private String password;

}
