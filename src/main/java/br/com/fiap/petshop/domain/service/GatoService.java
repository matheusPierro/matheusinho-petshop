package br.com.fiap.petshop.domain.service;

import br.com.fiap.petshop.Main;
import br.com.fiap.petshop.domain.entity.animal.Animal;
import br.com.fiap.petshop.domain.entity.animal.Gato;
import br.com.fiap.petshop.domain.repository.GatoRepository;
import br.com.fiap.petshop.infra.database.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class GatoService implements Service<Gato, Long> {

    private static final AtomicReference<GatoService> instance = new AtomicReference<>();

    private final GatoRepository repository;

    private GatoService(GatoRepository repository) {
        this.repository = repository;
    }

    public static GatoService build() {
        GatoService result = instance.get();
        if (Objects.isNull( result )) {
            EntityManagerFactory factory = EntityManagerFactoryProvider.of( Main.PERSISTENCE_UNIT ).provide();
            EntityManager manager = factory.createEntityManager();

            GatoRepository repository = GatoRepository.build( manager );

            GatoService service = new GatoService( repository );
            if (instance.compareAndSet( null, service )) {
                result = service;
            } else {
                result = instance.get();
            }
        }
        return result;
    }

    @Override
    public List<Gato> findAll() {
        return repository.findAll();
    }

    @Override
    public Gato findById(Long id) {
        return repository.findById( id );
    }


    @Override
    public Gato persist(Gato animal) {
        animal.setId( null );
        return repository.persist( animal );
    }


    @Override
    public Gato update(Long id, Gato animal) {
        var entidade = repository.findById( id );
        if (Objects.isNull( entidade )) return null;
        animal.setId( id );
        return repository.update( animal );
    }

    @Override
    public boolean delete(Gato animal) {
        return repository.delete( animal );
    }
}
