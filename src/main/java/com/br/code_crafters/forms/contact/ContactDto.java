package com.br.code_crafters.forms.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactDto {

    @NotBlank(message = "{contact.nome.notblank}")
    private String nome;

    @Email(message = "{contact.email.field}")
    private String email;

    @NotBlank(message = "{contact.assunto.notblank}")
    private String assunto;

    @NotBlank(message = "{contact.message.notblank}")
    private String message;


}
