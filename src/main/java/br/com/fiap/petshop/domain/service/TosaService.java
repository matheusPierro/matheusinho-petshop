package br.com.fiap.petshop.domain.service;

import br.com.fiap.petshop.Main;
import br.com.fiap.petshop.domain.entity.servico.Tosa;
import br.com.fiap.petshop.domain.repository.TosaRepository;
import br.com.fiap.petshop.infra.database.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class TosaService implements Service<Tosa, Long> {

    private static final AtomicReference<TosaService> instance = new AtomicReference<>();

    private final TosaRepository repository;

    private TosaService(TosaRepository repository) {
        this.repository = repository;
    }

    public static TosaService build() {
        TosaService result = instance.get();
        if (Objects.isNull( result )) {
            EntityManagerFactory factory = EntityManagerFactoryProvider.of( Main.PERSISTENCE_UNIT ).provide();
            EntityManager manager = factory.createEntityManager();
            TosaRepository repository = TosaRepository.build( manager );
            TosaService service = new TosaService( repository );
            if (instance.compareAndSet( null, service )) {
                result = service;
            } else {
                result = instance.get();
            }
        }
        return result;
    }

    @Override
    public List<Tosa> findAll() {
        return repository.findAll();
    }

    @Override
    public Tosa findById(Long id) {
        return repository.findById( id );
    }

    @Override
    public Tosa persist(Tosa tosa) {
        return repository.persist( tosa );
    }

    @Override
    public Tosa update(Long id, Tosa tosa) {
        var entidade = repository.findById( id );
        if (Objects.isNull( entidade )) return null;
        tosa.setId( id );
        return repository.update( tosa );
    }

    @Override
    public boolean delete(Tosa tosa) {
        return repository.delete( tosa );
    }
}
