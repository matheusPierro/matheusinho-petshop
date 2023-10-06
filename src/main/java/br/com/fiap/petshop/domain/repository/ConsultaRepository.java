package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.animal.Animal;
import br.com.fiap.petshop.domain.entity.servico.Banho;
import br.com.fiap.petshop.domain.entity.servico.Consulta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ConsultaRepository implements Repository<Consulta, Long> {

    private static final AtomicReference<ConsultaRepository> instance = new AtomicReference<>();
    private final EntityManager manager;

    private ConsultaRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static ConsultaRepository build(EntityManager manager) {
        ConsultaRepository result = instance.get();
        if (Objects.isNull( result )) {
            ConsultaRepository repo = new ConsultaRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Consulta> findAll() {
        return manager.createQuery( "From Consulta" ).getResultList();
    }

    @Override
    public Consulta findById(Long id) {
        return manager.find( Consulta.class, id );
    }


    @Override
    public Consulta persist(Consulta consulta) {
        //Não posso confiar no usuário preciso pegar os dados do Animal:
        consulta.setId( null );
        manager.getTransaction().begin();
        Query query = manager.createQuery( "From Animal a where a.id =:id" );
        query.setParameter( "id", consulta.getAnimal().getId() );
        List<Animal> list = query.getResultList();
        list.forEach( consulta::setAnimal );
        consulta = manager.merge( consulta );
        manager.getTransaction().commit();
        return consulta;
    }

    @Override
    public Consulta update(Consulta consulta) {
        //Será que existe banho com o número informado?
        Consulta c = manager.find( Consulta.class, consulta.getId() );
        if (Objects.isNull( c )) return null;

        //Não posso confiar no usuário preciso pegar os dados do Animal:
        manager.getTransaction().begin();

        //Animal
        if (Objects.nonNull( consulta.getAnimal() )) {
            Query query = manager.createQuery( "From Animal a where a.id =:id" );
            query.setParameter( "id", consulta.getAnimal().getId() );
            List<Animal> list = query.getResultList();
            list.forEach( c::setAnimal );
            //Descrição
            if (Objects.nonNull( consulta.getDescricao() ) && !consulta.getDescricao().equals( "" )) {
                c.setDescricao( consulta.getDescricao() );
            }
            //Observação
            if (Objects.nonNull( consulta.getObservacao() ) && !consulta.getObservacao().equals( "" )) {
                c.setObservacao( consulta.getObservacao() );
            }
            //Abertura
            if (Objects.nonNull( consulta.getAbertura() )) {
                c.setAbertura( consulta.getAbertura() );
            }
            //Autorização
            if (Objects.nonNull( consulta.getAutorizacao() )) {
                c.setAutorizacao( consulta.getAutorizacao() );
            }
            //Conclusão
            if (Objects.nonNull( consulta.getConclusao() )) {
                c.setConclusao( consulta.getConclusao() );
            }
            //Valor
            if (Objects.nonNull( consulta.getValor() )) {
                c.setValor( consulta.getValor() );
            }

        }

        consulta = manager.merge( c );
        manager.getTransaction().commit();
        return consulta;
    }

    @Override
    public boolean delete(Consulta consulta) {

        manager.remove( consulta );
        return true;
    }
}
