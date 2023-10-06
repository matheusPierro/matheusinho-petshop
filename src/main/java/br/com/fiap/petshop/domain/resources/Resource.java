package br.com.fiap.petshop.domain.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface Resource<T, U> {

    Response findAll();

    Response findById(U id);

    Response persist(T t);

    Response update(U id, T t);

    Response delete(T t);

}
