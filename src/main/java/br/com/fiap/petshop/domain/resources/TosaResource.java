package br.com.fiap.petshop.domain.resources;

import br.com.fiap.petshop.domain.entity.servico.Tosa;
import br.com.fiap.petshop.domain.service.TosaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;


@Path("/tosa")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TosaResource implements Resource<Tosa, Long> {

    @Context
    private UriInfo uriInfo;

    private TosaService service = TosaService.build();

    @GET
    @Override
    public Response findAll() {
        List<Tosa> all = service.findAll();
        return Response.ok( all ).build();
    }

    @GET
    @Path("/{id}")
    @Override
    public Response findById(@PathParam("id") Long id) {
        var tosa = service.findById( id );
        if (Objects.isNull( tosa )) return Response.status( 404 ).build();
        return Response.ok( tosa ).build();
    }


    @POST
    @Override
    public Response persist(Tosa tosa) {
        var entity = service.persist( tosa );
        //Criando a URI da requisição
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI uri = ub.path( String.valueOf( entity.getId() ) ).build();
        return Response.created( uri ).entity( entity ).build();
    }

    @PUT
    @Path("/{id}")
    @Override
    public Response update(@PathParam("id") Long id, Tosa tosa) {
        Tosa updated = service.update( id, tosa );
        if (Objects.isNull( updated )) return Response.notModified().build();
        return Response.ok( updated ).build();
    }

    @DELETE
    @Override
    public Response delete(Tosa tosa) {
        var updated = service.delete( tosa );
        if (updated) return Response.notModified().build();
        return Response.ok( updated ).build();
    }
}
