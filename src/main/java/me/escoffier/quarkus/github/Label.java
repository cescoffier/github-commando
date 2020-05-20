package me.escoffier.quarkus.github;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Label {

    public String id;
    public String node_id;
    public String name;
    public String description;
}
