package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.Main;
import br.com.fiap.petshop.domain.entity.animal.Cachorro;
import br.com.fiap.petshop.domain.entity.animal.Gato;
import br.com.fiap.petshop.infra.security.entity.Pessoa;
import br.com.fiap.petshop.infra.security.service.PessoaFisicaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class GatoRepository implements Repository<Gato, Long> {

    private static final AtomicReference<GatoRepository> instance = new AtomicReference<>();
    private final EntityManager manager;

    private GatoRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static GatoRepository build(EntityManager manager) {
        GatoRepository result = instance.get();
        if (Objects.isNull( result )) {
            GatoRepository repo = new GatoRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Gato> findAll() {
        return manager.createQuery( "From Gato a" ).getResultList();
    }

    @Override
    public Gato findById(Long id) {
        return manager.find( Gato.class, id );
    }


    @Override
    public Gato persist(Gato animal) {
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
    public Gato update(Gato animal) {
        //Será que existe animal com o número informado?
        Gato cat = manager.find( Gato.class, animal.getId() );
        if (Objects.isNull( cat )) return null;

        //Não posso confiar no usuário preciso pegar os dados do Pessoa:
        manager.getTransaction().begin();

        //Pessoa
        if (Objects.nonNull( animal.getDono() )) {

            Query query = manager.createQuery( "From Pessoa p where p.id =:id" );
            query.setParameter( "id", animal.getDono().getId() );
            List<Pessoa> list = query.getResultList();
            list.forEach( cat::setDono );

            //Nome
            if (Objects.nonNull( animal.getNome() ) && !animal.getNome().equals( "" )) {
                cat.setNome(  animal.getNome() );
            }


            //Descrição
            if (Objects.nonNull( animal.getDescricao() ) && !animal.getDescricao().equals( "" )) {
                cat.setDescricao( animal.getDescricao() );
            }

            //Odogservação
            if (Objects.nonNull( animal.getObservacao() ) &&!animal.getObservacao().equals( "" )) {
                cat.setObservacao( animal.getObservacao() );
            }

            //Nascimento
            if (Objects.nonNull( animal.getNascimento() )) {
                cat.setNascimento( animal.getNascimento() );
            }

            //Raça
            if (Objects.nonNull( animal.getRaca() )) {
                cat.setRaca( animal.getRaca() );
            }

            //SEXO
            if (Objects.nonNull( animal.getSexo()) ) {
                cat.setSexo( animal.getSexo() );
            }

        }

        animal = manager.merge( cat );
        manager.getTransaction().commit();
        return animal;
    }

    @Override
    public boolean delete(Gato animal) {
        manager.remove( animal );
        return true;
    }
}
