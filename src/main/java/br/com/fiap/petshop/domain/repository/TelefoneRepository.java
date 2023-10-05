package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.Documento;
import br.com.fiap.petshop.domain.entity.Telefone;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class TelefoneRepository implements Repository<Telefone, Long> {

    private static final AtomicReference<TelefoneRepository> instance = new AtomicReference<>();
    private EntityManager manager;

    private TelefoneRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static TelefoneRepository build(EntityManager manager) {
        TelefoneRepository result = instance.get();
        if (Objects.isNull( result )) {
            TelefoneRepository repo = new TelefoneRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Telefone> findAll() {
        List<Telefone> list = manager.createQuery("FROM Telefone").getResultList();
        manager.close();
        return list;
    }

    @Override
    public Telefone findById(Long id) {
        Telefone telefone = manager.find(Telefone.class, id);
        manager.close();
        return telefone;
    }

    @Override
    public List<Telefone> findByTexto(String texto) {
        Query query = manager.createQuery("FROM Telefone a  WHERE a.nome LIKE :texto");
        query.setParameter("texto", texto);
        List<Telefone> list = query.getResultList();
        return list;
    }

    @Override
    public Telefone persist(Telefone telefone) {
        manager.getTransaction().begin();
        manager.persist(telefone);
        manager.getTransaction().commit();
        return telefone;
    }

    @Override
    public Telefone update(Telefone telefone) {
        Telefone mergedTelefone = manager.merge(telefone);
        return mergedTelefone;
    }

    @Override
    public boolean delete(Telefone telefone) {
        try {
            manager.remove(telefone);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
