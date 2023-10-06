package br.com.fiap.petshop.infra.security.repository;

import br.com.fiap.petshop.infra.security.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;

public class UsuarioRepository implements Repository<Usuario, Long> {

    private static volatile UsuarioRepository instance;

    private final EntityManager manager;

    private UsuarioRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static UsuarioRepository build(EntityManager manager) {
        UsuarioRepository result = instance;
        if (Objects.nonNull(result)) return result;
        synchronized (UsuarioRepository.class) {
            if (Objects.isNull(instance)) {
                instance = new UsuarioRepository(manager);
            }
            return instance;
        }
    }

    public Usuario findByUsername(String username) {
        String jpql = "SELECT u FROM Usuario u  where u.username =:username";
        Query query = manager.createQuery(jpql);
        query.setParameter("username", username);
        Usuario usuario = (Usuario) query.getSingleResult();
        return usuario;
    }

    @Override
    public List<Usuario> findAll() {
        return null;
    }

    @Override
    public Usuario findById(Long id) {
        return null;
    }

    @Override
    public List<Usuario> findByName(String texto) {
        return null;
    }

    @Override
    public Usuario persist(Usuario usuario) {
        return null;
    }
}
