package br.com.fiap.petshop.infra.security.dto;

import br.com.fiap.petshop.infra.security.entity.PessoaJuridica;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.Objects;

public record PessoaJuridicaDTO(
        Long id,
        @NotNull String nome,
        @PastOrPresent LocalDate nascimento,
        @NotNull @Email String email,
        String password,
        @NotNull String cnpj
) {
    public static PessoaJuridicaDTO of(PessoaJuridica p) {
        if(Objects.isNull(p)) return null;
        return new PessoaJuridicaDTO( p.getId(), p.getNome(), p.getNascimento(), p.getEmail(), "", p.getCnpj() );
    }

    public static PessoaJuridica of(PessoaJuridicaDTO p) {
        if(Objects.isNull(p)) return null;
        return new PessoaJuridica( p.id, p.nome, p.nascimento, p.email, p.password, p.cnpj );
    }
}
