package br.com.fiap.petshop.domain.resources;

import br.com.fiap.petshop.domain.entity.servico.Consulta;
import br.com.fiap.petshop.domain.service.ConsultaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;


@Path("/consulta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaResource implements Resource<Consulta, Long> {

    @Context
    private UriInfo uriInfo;

    private ConsultaService service = ConsultaService.build();

    @GET
    @Override
    public Response findAll() {
        List<Consulta> all = service.findAll();
        return Response.ok( all ).build();
    }

    @GET
    @Path("/{id}")
    @Override
    public Response findById(@PathParam("id") Long id) {
        var consulta = service.findById( id );
        if (Objects.isNull( consulta )) return Response.status( 404 ).build();
        return Response.ok( consulta ).build();

    }


    @POST
    @Override
    public Response persist(Consulta consulta) {
        var entity = service.persist( consulta );
        //Criando a URI da requisição
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI uri = ub.path( String.valueOf( entity.getId() ) ).build();
        return Response.created( uri ).entity( entity ).build();
    }

    @PUT
    @Path("/{id}")
    @Override
    public Response update(@PathParam("id") Long id, Consulta consulta) {
        Consulta updated = service.update( id, consulta );
        if (Objects.isNull( updated )) return Response.notModified().build();
        return Response.ok( updated ).build();
    }

    @DELETE
    @Override
    public Response delete(Consulta consulta) {
        var updated = service.delete( consulta );
        if (updated) return Response.notModified().build();
        return Response.ok( updated ).build();
    }
}
