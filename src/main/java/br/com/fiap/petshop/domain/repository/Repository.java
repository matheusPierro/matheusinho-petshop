package br.com.fiap.petshop.domain.repository;

import org.jvnet.hk2.annotations.Contract;

import java.util.List;

@Contract
public interface Repository<T, U> {

    List<T> findAll();

    T findById(U id);

    T persist(T t);

    T update(T t);

    boolean delete(T t);

}
