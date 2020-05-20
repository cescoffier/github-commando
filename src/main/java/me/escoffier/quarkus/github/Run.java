package me.escoffier.quarkus.github;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Run extends GithubObject {

    public String head_branch;
    public String head_sha;
    public int run_number;
    public String event;
    public String status;
    public String conclusion;

    public String jobs_url;
    public String logs_url;
    public String check_suite_url;
    public String artifacts_url;
    public String cancel_url;
    public String rerun_url;
    public String workflow_url;
    public Commit head_commit;


}
