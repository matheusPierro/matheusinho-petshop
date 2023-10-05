package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.Main;
import br.com.fiap.petshop.domain.entity.animal.Animal;
import br.com.fiap.petshop.infra.database.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class AnimalRepository implements Repository<Animal, Long> {

    private static final AtomicReference<AnimalRepository> instance = new AtomicReference<>();
    private EntityManager manager;

    private AnimalRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static AnimalRepository build(EntityManager manager) {
        AnimalRepository result = instance.get();
        if (Objects.isNull(result)) {
            AnimalRepository repo = new AnimalRepository(manager);
            if (instance.compareAndSet(null, repo)) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Animal> findAll() {
        List<Animal> list = manager.createQuery("FROM Animal").getResultList();
        manager.close();
        return list;
    }

    @Override
    public Animal findById(Long id) {
        Animal animal = manager.find(Animal.class, id);
        manager.close();
        return animal;
    }

    @Override
    public List<Animal> findByTexto(String texto) {
        Query query = manager.createQuery("FROM Animal a  WHERE a.nome LIKE :texto");
        query.setParameter("texto", texto);
        return query.getResultList();
    }

    @Override
    public Animal persist(Animal animal) {
        manager.getTransaction().begin();
        manager.persist(animal);
        manager.getTransaction().commit();
        manager.close();
        return animal;
    }

    @Override
    public Animal update(Animal animal) {
        return manager.merge(animal);
    }

    @Override
    public boolean delete(Animal animal) {
        try {
            manager.remove(animal);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
