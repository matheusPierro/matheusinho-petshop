package br.com.fiap.petshop.domain.entity.animal;

import br.com.fiap.petshop.domain.entity.Sexo;
import br.com.fiap.petshop.domain.entity.servico.Servico;
import br.com.fiap.petshop.infra.security.entity.Pessoa;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "TB_GATO")
@DiscriminatorValue("GATO")
public class Gato extends Animal{
    public Gato() {
    }

    public Gato(Long id, String nome, Sexo sexo, LocalDate nascimento, String raca, String descricao, String observacao, Pessoa dono, Set<Servico> servicos) {
        super(id, nome, sexo, nascimento, raca, descricao, observacao, dono, servicos);
    }

    @Override
    public String toString() {
        return "Gato{} " + super.toString();
    }
}

