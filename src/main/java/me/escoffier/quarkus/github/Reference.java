package me.escoffier.quarkus.github;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Reference {

    public String ref;
    public String sha;
    public User user;
    public Repository repo;
}
