package com.br.code_crafters.forms.register;

import com.br.code_crafters.user.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {


    private Long id;

    @NotBlank(message = "{register.username.notblank}")
    private String nmFull;

    @NotBlank(message = "{register.email.notblank}")
    private String email;

    @NotBlank(message = "{register.name.notblank}")
    private String username;


    @NotBlank(message = "{register.password.notblank}")
    private String password;

    @NotBlank(message = "{register.password.confirm.notblank}")
    private String passwordConfirm; // 💡 NOVO CAMPO

    @NotNull(message = "{register.user.role.notnull}")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

}
