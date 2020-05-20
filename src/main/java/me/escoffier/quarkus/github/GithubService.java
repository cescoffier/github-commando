package me.escoffier.quarkus.github;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.NoSuchElementException;

@ApplicationScoped
public class GithubService {

    private static final Logger LOGGER = Logger.getLogger(GithubService.class.getName());

    @Inject @RestClient WorkflowService workflow;

    @Inject @RestClient RunService run;

    @Inject @RestClient PrService pr;

    @Inject Jsonb jsonb;

    public List<Workflow> listWorkflowFromRepository(String repository) {
        Response response = workflow.list(repository);
        WorkflowListResponse res = response.readEntity(WorkflowListResponse.class);
        return res.workflows;
    }

    public List<Run> getRunsForWorkflow(String repository, String workflowName) {
        LOGGER.debugf("Searching for executions of %s", workflowName);
        List<Workflow> workflows = listWorkflowFromRepository(repository);
        Workflow wf = workflows.stream()
                .filter(w -> w.name.equalsIgnoreCase(workflowName)).findAny()
                .orElseThrow(() -> new NoSuchElementException("Unknown workflow " + workflowName));

        Response response = run.list(repository, wf.id, "pull_request", null, 500);
        RunListResponse res = response.readEntity(RunListResponse.class);
        return res.workflow_runs;
    }

    public List<PullRequest> getPullRequests(String repository) {
        return pr.getPullRequests(repository, "open", null, null, 500);
    }

    public PullRequest getPullRequest(String repo, int id) {
        return pr.getPullRequest(repo, id);
    }

    public Run getLastRunForPr(String repo, String workflow, String branch) {
        List<Run> runs = getRunsForWorkflow(repo, workflow);
        LOGGER.debugf("Searching for executions of %s using the branch %s", workflow, branch);
        return runs.stream()
                .filter(r -> r.head_branch.equalsIgnoreCase(branch)).findAny().orElse(null);
    }

    @RegisterForReflection
    public static class WorkflowListResponse {

        public int total_count;
        public List<Workflow> workflows;

    }

    @RegisterForReflection
    public static class RunListResponse {

        public int total_count;
        public List<Run> workflow_runs;

    }
}
