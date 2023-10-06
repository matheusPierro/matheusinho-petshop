package br.com.fiap.petshop.domain.service;

import br.com.fiap.petshop.Main;
import br.com.fiap.petshop.domain.entity.servico.Consulta;
import br.com.fiap.petshop.domain.repository.ConsultaRepository;
import br.com.fiap.petshop.infra.database.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ConsultaService implements Service<Consulta, Long> {

    private static final AtomicReference<ConsultaService> instance = new AtomicReference<>();

    private final ConsultaRepository repository;

    private ConsultaService(ConsultaRepository repository) {

        this.repository = repository;
    }

    public static ConsultaService build() {
        ConsultaService result = instance.get();
        if (Objects.isNull( result )) {
            EntityManagerFactory factory = EntityManagerFactoryProvider.of( Main.PERSISTENCE_UNIT ).provide();
            EntityManager manager = factory.createEntityManager();
            ConsultaRepository repository = ConsultaRepository.build( manager );
            ConsultaService service = new ConsultaService( repository );
            if (instance.compareAndSet( null, service )) {
                result = service;
            } else {
                result = instance.get();
            }
        }
        return result;
    }

    @Override
    public List<Consulta> findAll() {
        return repository.findAll();
    }

    @Override
    public Consulta findById(Long id) {
        return repository.findById( id );
    }

    @Override
    public Consulta persist(Consulta consulta) {
        return repository.persist( consulta );
    }

    @Override
    public Consulta update(Long id, Consulta consulta) {
        var entidade = repository.findById( id );
        if (Objects.isNull( entidade )) return null;
        consulta.setId( id );
        return repository.update(consulta);
    }

    @Override
    public boolean delete(Consulta consulta) {
        return repository.delete(consulta);
    }
}
