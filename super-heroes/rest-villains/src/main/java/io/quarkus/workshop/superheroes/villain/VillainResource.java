package io.quarkus.workshop.superheroes.villain;

import java.util.List;
import org.jboss.logging.Logger;
import java.util.logging.Level;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

@Path("/api/villains")
public class VillainResource {

    Logger logger;
    VillainService service;

    public VillainResource(Logger logger, VillainService service) {
        this.logger = logger;
        this.service = service;
    }

    @GET
    @Path("/random")
    public RestResponse<Villain> getRandomVillain() {
        Villain villan = service.findRandomVillain();

        logger.info("Found random villain " + villan);
        return RestResponse.ok(villan);
    }


    @GET
    public RestResponse<List<Villain>> getAllVillains() {
        List<Villain> villans = service.findAllVillains();

        logger.debug("Found all villains " + villans);
        return RestResponse.ok(villans);
    }

    @GET
    @Path("/{id}")
    public RestResponse<Villain> getVillain(Long id) {
        Villain villan = service.findVillainById(id);

        if (villan != null) {
            logger.debug("Found villain " + villan);
            return RestResponse.ok(villan);
        } else {
            logger.debug("No villain found with id " + id);
            return RestResponse.noContent();
        }
    }

    @POST
    public RestResponse<Void> createVillain(@Valid Villain villain, @Context UriInfo uriInfo) {
        villain = service.persistVillain(villain);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(villain.id));
        logger.debug("New villain created with URI " + builder.build().toString());
        return RestResponse.created(builder.build());
    }

    @PUT
    public RestResponse<Villain> updateVillain(@Valid Villain villain) {
        villain = service.updateVillain(villain);
        logger.debug("Villain updated with new valued " + villain);
        return RestResponse.ok(villain);
    }


    @DELETE
    @Path("/{id}")
    public RestResponse<Void> deleteVillain(@RestPath Long id) {
        service.deleteVillain(id);
        logger.debug("Villain deleted with " + id);
        return RestResponse.noContent();
    }


    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        
        // log statement printing 
        return "Hello from Villan City";

    }

}
