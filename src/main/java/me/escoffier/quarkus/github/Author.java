package me.escoffier.quarkus.github;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Author {

    public String name;
    public String email;
}
