package br.com.fiap.petshop.domain.resources;

import br.com.fiap.petshop.domain.entity.servico.Banho;
import br.com.fiap.petshop.domain.service.BanhoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;


@Path("/banho")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BanhoResource implements Resource<Banho, Long> {

    @Context
    private UriInfo uriInfo;

    private BanhoService service = BanhoService.build();

    @GET
    @Override
    public Response findAll() {
        List<Banho> all = service.findAll();
        return Response.ok( all ).build();
    }

    @GET
    @Path("/{id}")
    @Override
    public Response findById(@PathParam("id") Long id) {
        var banho = service.findById( id );
        if (Objects.isNull( banho )) return Response.status( 404 ).build();
        return Response.ok( banho ).build();
    }

    @POST
    @Override
    public Response persist(Banho banho) {
        var entity = service.persist( banho );
        //Criando a URI da requisição
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI uri = ub.path( String.valueOf( entity.getId() ) ).build();
        return Response.created( uri ).entity( entity ).build();
    }

    @PUT
    @Path("/{id}")
    @Override
    public Response update(@PathParam("id") Long id, Banho banho) {
        Banho updated = service.update( id, banho );
        if (Objects.isNull( updated )) return Response.notModified().build();
        return Response.ok( updated ).build();
    }

    @DELETE
    @Override
    public Response delete(Banho banho) {
        var updated = service.delete( banho );
        if (updated) return Response.notModified().build();
        return Response.ok( updated ).build();
    }
}
