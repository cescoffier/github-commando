package me.escoffier.quarkus.commands;

import me.escoffier.quarkus.github.GithubService;
import picocli.CommandLine;

import javax.inject.Inject;

@CommandLine.Command(name = "pr", description = "List open pull requests on the given repository")
public class ListPullRequests implements Runnable {

    @CommandLine.Option(names = {"-r", "--repo"}, description = "Set the GitHub repository (owner/name)", required = true)
    String repo;

    @Inject GithubService github;

    @Override
    public void run() {
        github.getPullRequests(repo)
                .forEach(p -> {
                    System.out.println(p.title + " [" + p.head.ref + "->" + p.base.ref + "] " + p.user.login + " : " + p.html_url);
                });
    }
}
