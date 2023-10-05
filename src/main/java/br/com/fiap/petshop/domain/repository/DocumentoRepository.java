package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.Documento;
import br.com.fiap.petshop.domain.entity.animal.Animal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class DocumentoRepository implements Repository<Documento, Long> {

    private static final AtomicReference<DocumentoRepository> instance = new AtomicReference<>();
    private EntityManager manager;

    private DocumentoRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static DocumentoRepository build(EntityManager manager) {
        DocumentoRepository result = instance.get();
        if (Objects.isNull( result )) {
            DocumentoRepository repo = new DocumentoRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Documento> findAll() {
        List<Documento> list = manager.createQuery("FROM Documento").getResultList();
        manager.close();
        return list;
    }

    @Override
    public Documento findById(Long id) {
        Documento documento = manager.find(Documento.class, id);
        manager.close();
        return documento;
    }

    @Override
    public List<Documento> findByTexto(String texto) {
        Query query = manager.createQuery("FROM Documento a  WHERE a.nome LIKE :texto");
        query.setParameter("texto", texto);
        List<Documento> list = query.getResultList();
        return list;
    }

    @Override
    public Documento persist(Documento documento) {
        manager.getTransaction().begin();
        manager.persist(documento);
        manager.getTransaction().commit();
        return documento;
    }

    @Override
    public Documento update(Documento documento) {
        Documento mergedDocumento = manager.merge(documento);
        return mergedDocumento;
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
