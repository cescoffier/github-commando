package me.escoffier.quarkus.github;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient(baseUri = "https://api.github.com/repos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RunService {

    @GET
    @Path("{repo}/actions/workflows/{workflow_id}/runs")
    Response list(@PathParam("repo") String repo, @PathParam("workflow_id") int id, @QueryParam("event") String event,
            @QueryParam("status") String status, @QueryParam("per_page") int per_page);

    @GET
    @Path("{repo}/actions/runs/{run_id}/logs")
    Response getLog(@PathParam("repo") String repo, @HeaderParam("Authorization") String authorization, @PathParam("run_id") int runId);

}