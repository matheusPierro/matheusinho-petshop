package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.Documento;
import br.com.fiap.petshop.domain.entity.servico.Servico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ServicoRepository implements Repository<Servico, Long>{

    private static final AtomicReference<ServicoRepository> instance = new AtomicReference<>();
    private EntityManager manager;

    private ServicoRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static ServicoRepository build(EntityManager manager) {
        ServicoRepository result = instance.get();
        if (Objects.isNull( result )) {
            ServicoRepository repo = new ServicoRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Servico> findAll() {
        List<Servico> list = manager.createQuery("FROM Servico").getResultList();
        manager.close();
        return list;
    }

    @Override
    public Servico findById(Long id) {
        Servico servico = manager.find(Servico.class, id);
        manager.close();
        return servico;
    }

    @Override
    public List<Servico> findByTexto(String texto) {
        Query query = manager.createQuery("FROM Servico a  WHERE a.nome LIKE :texto");
        query.setParameter("texto", texto);
        List<Servico> list = query.getResultList();
        return list;
    }

    @Override
    public Servico persist(Servico servico) {
        manager.getTransaction().begin();
        manager.persist(servico);
        manager.getTransaction().commit();
        return servico;
    }

    @Override
    public Servico update(Servico servico) {
        Servico mergedServico = manager.merge(servico);
        return mergedServico;
    }

    @Override
    public boolean delete(Servico servico) {
        try {
            manager.remove(servico);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
