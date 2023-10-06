package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.Main;
import br.com.fiap.petshop.domain.entity.animal.Cachorro;
import br.com.fiap.petshop.infra.security.entity.Pessoa;
import br.com.fiap.petshop.infra.security.service.PessoaFisicaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class CachorroRepository implements Repository<Cachorro, Long> {

    private static final AtomicReference<CachorroRepository> instance = new AtomicReference<>();
    private final EntityManager manager;

    private CachorroRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static CachorroRepository build(EntityManager manager) {
        CachorroRepository result = instance.get();
        if (Objects.isNull( result )) {
            CachorroRepository repo = new CachorroRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Cachorro> findAll() {
        return manager.createQuery( "From Cachorro a" ).getResultList();
    }

    @Override
    public Cachorro findById(Long id) {
        return manager.find( Cachorro.class, id );
    }


    @Override
    public Cachorro persist(Cachorro animal) {
        //Não posso confiar no usuário:
        var pfService = PessoaFisicaService.of( Main.PERSISTENCE_UNIT );
        manager.getTransaction().begin();
        var dono = pfService.findById( animal.getDono().getId() );
        animal.setDono( dono );
        manager.merge( animal );
        manager.getTransaction().commit();
        return animal;
    }

    @Override
    public Cachorro update(Cachorro animal) {

        //Será que existe animal com o número informado?
        Cachorro dog = manager.find( Cachorro.class, animal.getId() );
        if (Objects.isNull( dog )) return null;

        //Não posso confiar no usuário preciso pegar os dados do Pessoa:
        manager.getTransaction().begin();

        //Pessoa
        if (Objects.nonNull( animal.getDono() )) {

            Query query = manager.createQuery( "From Pessoa p where p.id =:id" );
            query.setParameter( "id", animal.getDono().getId() );
            List<Pessoa> list = query.getResultList();
            list.forEach( dog::setDono );

            //Nome
            if (Objects.nonNull( animal.getNome() ) && !animal.getNome().equals( "" )) {
                dog.setNome(  animal.getNome() );
            }

            //Descrição
            if (Objects.nonNull( animal.getDescricao() ) && !animal.getDescricao().equals( "" )) {
                dog.setDescricao( animal.getDescricao() );
            }

            //Odogservação
            if (Objects.nonNull( animal.getObservacao() ) &&!animal.getObservacao().equals( "" )) {
                dog.setObservacao( animal.getObservacao() );
            }

            //Nascimento
            if (Objects.nonNull( animal.getNascimento() )) {
                dog.setNascimento( animal.getNascimento() );
            }

            //Raça
            if (Objects.nonNull( animal.getRaca() )) {
                dog.setRaca( animal.getRaca() );
            }

            //SEXO
            if (Objects.nonNull( animal.getSexo()) ) {
                dog.setSexo( animal.getSexo() );
            }



        }

        animal = manager.merge( dog );
        manager.getTransaction().commit();
        return animal;
    }

    @Override
    public boolean delete(Cachorro animal) {
        manager.remove( animal );
        return true;
    }
}
