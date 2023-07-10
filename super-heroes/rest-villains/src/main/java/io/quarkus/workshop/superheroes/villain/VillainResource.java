package io.quarkus.workshop.superheroes.villain;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
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


import static jakarta.ws.rs.core.MediaType.*;

@Path("/api/villains")
public class VillainResource {

    Logger logger;
    VillainService service;

    public VillainResource(Logger logger, VillainService service) {
        this.logger = logger;
        this.service = service;
    }


    @Operation(summary = "Returns a random villain")
    @GET
    @Path("/random")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, required = true)))
    public RestResponse<Villain> getRandomVillain() {
        Villain villan = service.findRandomVillain();

        logger.info("Found random villain " + villan);
        return RestResponse.ok(villan);
    }

    @Operation(summary = "Returns all the villains from the database")
    @GET
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, required = true)))
    public RestResponse<List<Villain>> getAllVillains() {
        List<Villain> villans = service.findAllVillains();

        logger.debug("Found all villains " + villans);
        return RestResponse.ok(villans);
    }

    @Operation(summary = "Returns a villain for a given identifier")
    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, required = true)))
    @APIResponse(responseCode = "204", description = "The villain is not found for a given identifier")
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

    @Operation(summary = "Creates a valid villain")
    @RequestBody(
    name="Villain",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = Villain.class), 
      examples = @ExampleObject(name = "villain", value = "{\"name\":\"Dr. Evil\",\"otherName\":\"Dr. Evil\",\"level\":15,\"picture\":\"https://upload.wikimedia.org/wikipedia/en/5/53/Dr._Evil.jpg\",\"powers\":\"Evil\"}")))
    @APIResponse(responseCode = "201", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, required = true)))
    @POST
    public RestResponse<Void> createVillain(@Valid Villain villain, @Context UriInfo uriInfo) {
        villain = service.persistVillain(villain);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(villain.id));
        logger.debug("New villain created with URI " + builder.build().toString());
        return RestResponse.created(builder.build());
    }

    @Operation(summary = "Updates an exiting valid villain")
    @PUT
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, required = true)))
    public RestResponse<Villain> updateVillain(@Valid Villain villain) {
        villain = service.updateVillain(villain);
        logger.debug("Villain updated with new valued " + villain);
        return RestResponse.ok(villain);
    }


    @Operation(summary = "Deletes an exiting valid villain")
    @DELETE
    @Path("/{id}")
    @APIResponse(responseCode = "204")
    public RestResponse<Void> deleteVillain(@RestPath Long id) {
        service.deleteVillain(id);
        logger.debug("Villain deleted with " + id);
        return RestResponse.noContent();
    }

    @Operation(summary = "Returns a hello world message")
    @GET
    @Path("/hello")
    @APIResponse(responseCode = "200", content = @Content(mediaType = TEXT_PLAIN))
    @Produces(MediaType.TEXT_PLAIN)
    @Tag(name="hello")
    public String hello() {
        
        // log statement printing 
        return "Hello from Villan City";

    }

}
