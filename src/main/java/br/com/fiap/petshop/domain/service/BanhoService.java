package br.com.fiap.petshop.domain.service;

import br.com.fiap.petshop.Main;
import br.com.fiap.petshop.domain.entity.servico.Banho;
import br.com.fiap.petshop.domain.repository.BanhoRepository;
import br.com.fiap.petshop.infra.database.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class BanhoService implements Service<Banho, Long> {

    private static final AtomicReference<BanhoService> instance = new AtomicReference<>();

    private final BanhoRepository repository;

    private BanhoService(BanhoRepository repository) {

        this.repository = repository;
    }

    public static BanhoService build() {
        BanhoService result = instance.get();
        if (Objects.isNull( result )) {
            EntityManagerFactory factory = EntityManagerFactoryProvider.of( Main.PERSISTENCE_UNIT ).provide();
            EntityManager manager = factory.createEntityManager();
            BanhoRepository repository = BanhoRepository.build( manager );
            BanhoService service = new BanhoService( repository );
            if (instance.compareAndSet( null, service )) {
                result = service;
            } else {
                result = instance.get();
            }
        }
        return result;
    }

    @Override
    public List<Banho> findAll() {
        return repository.findAll();
    }

    @Override
    public Banho findById(Long id) {
        return repository.findById( id );
    }


    @Override
    public Banho persist(Banho banho) {
        return repository.persist( banho );
    }

    @Override
    public Banho update(Long id, Banho banho) {
        var entidade = repository.findById( id );
        if (Objects.isNull( entidade )) return null;
        banho.setId( id );
        return repository.update( banho );
    }

    @Override
    public boolean delete(Banho banho) {
        return repository.delete( banho );
    }
}
