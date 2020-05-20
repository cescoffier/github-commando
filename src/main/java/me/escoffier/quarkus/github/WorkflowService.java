package me.escoffier.quarkus.github;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient(baseUri = "https://api.github.com/repos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface WorkflowService {


    @GET
    @Path("{repo}/actions/workflows")
    Response list(@PathParam("repo") String repo);

    @GET
    @Path("{repo}/actions/workflows/{id}")
    Workflow get(@PathParam("repo") String repo, @PathParam("id") String id);



}
