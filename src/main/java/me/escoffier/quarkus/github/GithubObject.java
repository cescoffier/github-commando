package me.escoffier.quarkus.github;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class GithubObject {

    public int id;
    public String node_id;
    public String created_at;
    public String updated_at;
    public String url;
    public String html_url;
}
