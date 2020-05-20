package me.escoffier.quarkus.github;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Commit {

    public String id;
    public String tree_id;
    public String message;
    public String timestamp;
    public Author author;
    public Author committer;
}
