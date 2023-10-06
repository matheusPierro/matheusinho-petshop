package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.animal.Animal;
import br.com.fiap.petshop.domain.entity.servico.Tosa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class TosaRepository implements Repository<Tosa, Long> {

    private static final AtomicReference<TosaRepository> instance = new AtomicReference<>();
    private final EntityManager manager;

    private TosaRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static TosaRepository build(EntityManager manager) {
        TosaRepository result = instance.get();
        if (Objects.isNull( result )) {
            TosaRepository repo = new TosaRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Tosa> findAll() {
        return manager.createQuery( "From Tosa" ).getResultList();
    }

    @Override
    public Tosa findById(Long id) {
        return manager.find( Tosa.class, id );
    }


    @Override
    public Tosa persist(Tosa tosa) {
        //Não posso confiar no usuário preciso pegar os dados do Animal:
        tosa.setId( null );
        manager.getTransaction().begin();
        Query query = manager.createQuery( "From Animal a where a.id =:id" );
        query.setParameter( "id", tosa.getAnimal().getId() );
        List<Animal> list = query.getResultList();
        list.forEach( tosa::setAnimal );
        tosa = manager.merge( tosa );
        manager.getTransaction().commit();
        return tosa;
    }

    @Override
    public Tosa update(Tosa tosa) {
        //Será que existe banho com o número informado?
        Tosa b = manager.find( Tosa.class, tosa.getId() );
        if (Objects.isNull( b )) return null;

        //Não posso confiar no usuário preciso pegar os dados do Animal:
        manager.getTransaction().begin();

        //Animal
        if (Objects.nonNull( tosa.getAnimal() )) {
            Query query = manager.createQuery( "From Animal p where p.id =:id" );
            query.setParameter( "id", tosa.getAnimal().getId() );
            List<Animal> list = query.getResultList();
            list.forEach( b::setAnimal );
            //Descrição
            if (Objects.nonNull( tosa.getDescricao() ) && !tosa.getDescricao().equals( "" )) {
                b.setDescricao( tosa.getDescricao() );
            }
            //Observação
            if (Objects.nonNull( tosa.getObservacao() ) && !tosa.getObservacao().equals( "" )) {
                b.setObservacao( tosa.getObservacao() );
            }
            //Abertura
            if (Objects.nonNull( tosa.getAbertura() )) {
                b.setAbertura( tosa.getAbertura() );
            }
            //Autorização
            if (Objects.nonNull( tosa.getAutorizacao() )) {
                b.setAutorizacao( tosa.getAutorizacao() );
            }
            //Conclusão
            if (Objects.nonNull( tosa.getConclusao() )) {
                b.setConclusao( tosa.getConclusao() );
            }
            //Valor
            if (Objects.nonNull( tosa.getValor() )) {
                b.setValor( tosa.getValor() );
            }

        }

        tosa = manager.merge( b );
        manager.getTransaction().commit();
        return tosa;
    }

    @Override
    public boolean delete(Tosa tosa) {
        manager.remove( tosa );
        return true;
    }
}
