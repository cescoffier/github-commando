package me.escoffier.quarkus.github;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Milestone extends GithubObject {

    public String labels_url;
    public int number;
    public String state;
    public String title;
    public String description;
    public User creator;
    public int open_issues;
    public int closed_issues;
    public String closed_at;
    public String due_on;

}
