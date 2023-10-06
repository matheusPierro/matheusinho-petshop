package br.com.fiap.petshop.domain.service;

import br.com.fiap.petshop.Main;
import br.com.fiap.petshop.domain.entity.animal.Cachorro;
import br.com.fiap.petshop.domain.repository.CachorroRepository;
import br.com.fiap.petshop.infra.database.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class CachorroService implements Service<Cachorro, Long> {

    private static final AtomicReference<CachorroService> instance = new AtomicReference<>();

    private final CachorroRepository repository;

    private CachorroService(CachorroRepository repository) {

        this.repository = repository;
    }

    public static CachorroService build() {
        CachorroService result = instance.get();
        if (Objects.isNull( result )) {
            EntityManagerFactory factory = EntityManagerFactoryProvider.of( Main.PERSISTENCE_UNIT ).provide();
            EntityManager manager = factory.createEntityManager();

            CachorroRepository repository = CachorroRepository.build( manager );

            CachorroService service = new CachorroService( repository );
            if (instance.compareAndSet( null, service )) {
                result = service;
            } else {
                result = instance.get();
            }
        }
        return result;
    }

    @Override
    public List<Cachorro> findAll() {
        return repository.findAll();
    }

    @Override
    public Cachorro findById(Long id) {
        return repository.findById( id );
    }


    @Override
    public Cachorro persist(Cachorro animal) {
        animal.setId( null );
        return repository.persist( animal );
    }


    @Override
    public Cachorro update(Long id, Cachorro animal) {
        var entidade = repository.findById( id );
        if (Objects.isNull( entidade )) return null;
        animal.setId( id );
        return repository.update( animal );
    }

    @Override
    public boolean delete(Cachorro animal) {
        return repository.delete( animal );
    }
}
