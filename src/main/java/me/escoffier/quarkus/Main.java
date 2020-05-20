package me.escoffier.quarkus;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import me.escoffier.quarkus.commands.RetrieveLogForPullRequest;
import me.escoffier.quarkus.commands.ListPullRequests;
import picocli.CommandLine;

import javax.inject.Inject;

@QuarkusMain
@TopCommand
@CommandLine.Command(mixinStandardHelpOptions = true, subcommands = { ListPullRequests.class,
        RetrieveLogForPullRequest.class })
public class Main implements QuarkusApplication {

    @Inject
    CommandLine.IFactory factory;

    @Override
    public int run(String... args) throws Exception {
        return new CommandLine(this, factory).execute(args);
    }

}
