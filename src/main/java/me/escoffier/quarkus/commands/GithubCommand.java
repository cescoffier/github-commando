package me.escoffier.quarkus.commands;

import picocli.CommandLine;

public class GithubCommand {

    @CommandLine.Option(names = { "-r",
            "--repo" }, description = "Set the GitHub repository (owner/name)", required = true)
    String repo;

    @CommandLine.Option(names = { "-t", "--token"}, description = "The Github Token, using the `GITHUB_TOKEN` env variable if not set")
    String token;
}
