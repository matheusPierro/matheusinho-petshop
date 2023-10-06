package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.animal.Animal;
import br.com.fiap.petshop.domain.entity.servico.Banho;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class BanhoRepository implements Repository<Banho, Long> {

    private static final AtomicReference<BanhoRepository> instance = new AtomicReference<>();
    private final EntityManager manager;

    private BanhoRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static BanhoRepository build(EntityManager manager) {
        BanhoRepository result = instance.get();
        if (Objects.isNull( result )) {
            BanhoRepository repo = new BanhoRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Banho> findAll() {
        return manager.createQuery( "From Banho" ).getResultList();
    }

    @Override
    public Banho findById(Long id) {
        return manager.find( Banho.class, id );
    }


    @Override
    public Banho persist(Banho banho) {
        //Não posso confiar no usuário preciso pegar os dados do Animal:
        banho.setId( null );
        manager.getTransaction().begin();
        Query query = manager.createQuery( "From Animal a where a.id =:id" );
        query.setParameter( "id", banho.getAnimal().getId() );
        List<Animal> list = query.getResultList();
        list.forEach( banho::setAnimal );
        banho = manager.merge( banho );
        manager.getTransaction().commit();
        return banho;
    }

    @Override
    public Banho update(Banho banho) {
        //Será que existe banho com o número informado?
        Banho b = manager.find( Banho.class, banho.getId() );
        if (Objects.isNull( b )) return null;

        //Não posso confiar no usuário preciso pegar os dados do Animal:
        manager.getTransaction().begin();

        //Animal
        if (Objects.nonNull( banho.getAnimal() )) {
            Query query = manager.createQuery( "From Animal p where p.id =:id" );
            query.setParameter( "id", banho.getAnimal().getId() );
            List<Animal> list = query.getResultList();
            list.forEach( b::setAnimal );
            //Descrição
            if (  Objects.nonNull( banho.getDescricao() ) && !banho.getDescricao().equals( "" )) {
                b.setDescricao( banho.getDescricao() );
            }
            //Observação
            if (Objects.nonNull( banho.getObservacao() ) && !banho.getObservacao().equals( "" )) {
                b.setObservacao( banho.getObservacao() );
            }
            //Abertura
            if (Objects.nonNull( banho.getAbertura() )) {
                b.setAbertura( banho.getAbertura() );
            }
            //Autorização
            if (Objects.nonNull( banho.getAutorizacao() )) {
                b.setAutorizacao( banho.getAutorizacao() );
            }
            //Conclusão
            if (Objects.nonNull( banho.getConclusao() )) {
                b.setConclusao( banho.getConclusao() );
            }
            //Valor
            if (Objects.nonNull( banho.getValor() )) {
                b.setValor( banho.getValor() );
            }

        }

        banho = manager.merge( b );
        manager.getTransaction().commit();
        return banho;
    }

    @Override
    public boolean delete(Banho banho) {
        manager.remove( banho );
        return false;
    }
}
