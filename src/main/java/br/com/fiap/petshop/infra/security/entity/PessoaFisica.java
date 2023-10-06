package br.com.fiap.petshop.infra.security.entity;

import br.com.fiap.petshop.domain.entity.Sexo;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "TB_PF", uniqueConstraints = {
        @UniqueConstraint(name = "UK_PF_CPF", columnNames = "NR_CPF")
})
@DiscriminatorValue("PF")
public class PessoaFisica extends Pessoa {

    //@CPF
    @Column(name = "NR_CPF", nullable = false)
    private String cpf;

//    @OneToOne(mappedBy = "pessoa", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    public PessoaFisica() {
    }

    public PessoaFisica(Long id, String nome, LocalDate nascimento, String email, String password, Sexo sexo, String cpf) {
        super( id, nome, nascimento, email, password );
        this.cpf = cpf;
        this.sexo = sexo;
    }

    public String getCpf() {
        return cpf;
    }

    public PessoaFisica setCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }


    public Sexo getSexo() {
        return sexo;
    }

    public PessoaFisica setSexo(Sexo sexo) {
        this.sexo = sexo;
        return this;
    }


    @Override
    public String toString() {
        return "PessoaFisica{" +
                "cpf='" + cpf + '\'' +
                ", sexo=" + sexo +
                "} " + super.toString();
    }
}
