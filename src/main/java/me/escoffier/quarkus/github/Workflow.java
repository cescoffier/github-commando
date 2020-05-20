package me.escoffier.quarkus.github;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Workflow extends  GithubObject {

    public String name;
    public String path;
    public String state;
    public String badge_url;
}
