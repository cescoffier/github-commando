package me.escoffier.quarkus.github;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RegisterRestClient(baseUri = "https://api.github.com/repos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PrService {

    @GET
    @Path("{repo}/pulls")
    List<PullRequest> getPullRequests(@PathParam("repo") String repo,
            @QueryParam("state") String state,
            @QueryParam("head") String head, @QueryParam("base") String base,
            @QueryParam("per_page") int per_page);

    @GET
    @Path("{repo}/pulls/{pull_number}")
    PullRequest getPullRequest(@PathParam("repo") String repo, @PathParam("pull_number") int pull_number);
}
