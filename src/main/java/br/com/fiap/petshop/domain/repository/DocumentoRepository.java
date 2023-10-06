package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.Documento;
import br.com.fiap.petshop.domain.entity.animal.Animal;
import br.com.fiap.petshop.infra.security.entity.Pessoa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class DocumentoRepository implements Repository<Documento, Long> {

    private static final AtomicReference<DocumentoRepository> instance = new AtomicReference<>();
    private final EntityManager manager;

    private DocumentoRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static DocumentoRepository build(EntityManager manager) {
        DocumentoRepository result = instance.get();
        if (Objects.isNull(result)) {
            DocumentoRepository repo = new DocumentoRepository(manager);
            if (instance.compareAndSet(null, repo)) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }

    @Override
    public List<Documento> findAll() {
        return manager.createQuery("From Documento").getResultList();
    }

    @Override
    public Documento findById(Long id) {
        return manager.find(Documento.class, id);
    }

    @Override
    public Documento persist(Documento documento) {
        documento.setId(null);
        // Não posso confiar no usuário preciso pegar os dados do Animal:
        manager.getTransaction().begin();
        Query query = manager.createQuery("From Pessoa p where p.id =:id");
        query.setParameter("id", documento.getPessoa().getId());
        List<Pessoa> list = query.getResultList();
        list.forEach(documento::setPessoa);
        documento = manager.merge(documento);
        manager.getTransaction().commit();
        return documento;
    }

    @Override
    public Documento update(Documento documento) {

        // Será que existe documento com o número informado?
        Documento d = manager.find(Documento.class, documento.getId());
        if (Objects.isNull(d))
            return null;

        // Não posso confiar no usuário preciso pegar os dados do Dono:
        manager.getTransaction().begin();

        if (Objects.nonNull(documento.getPessoa())) {
            Query query = manager.createQuery("From Pessoa p where p.id =:id");
            query.setParameter("id", documento.getPessoa().getId());
            List<Pessoa> list = query.getResultList();
            list.forEach(d::setPessoa);

            if (Objects.nonNull(documento.getNumero()) && !documento.getNumero().equals("")) {
                d.setNumero(documento.getNumero());
            }
        }

        documento = manager.merge(d);
        manager.getTransaction().commit();
        return documento;
    }

    @Override
    public boolean delete(Documento documento) {
        try {
            manager.remove(documento);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
