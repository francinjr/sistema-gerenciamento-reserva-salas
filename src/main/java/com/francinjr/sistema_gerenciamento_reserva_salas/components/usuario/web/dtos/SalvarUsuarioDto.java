package com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.web.dtos;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.PapelUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; // ✅ Importar o Setter

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalvarUsuarioDto {

    @NotBlank(message = "O e-mail não pode ser vazio.")
    @Email(message = "O formato do e-mail é inválido.")
    private String email;

    @NotBlank(message = "A senha não pode ser vazia.")
    @Size(min = 6, max = 100)
    private String senha;
}