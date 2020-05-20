package me.escoffier.quarkus.commands;

import me.escoffier.quarkus.github.GithubService;
import me.escoffier.quarkus.github.PullRequest;
import me.escoffier.quarkus.github.Run;
import me.escoffier.quarkus.github.RunService;
import me.escoffier.quarkus.utils.LogReader;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import picocli.CommandLine;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This command downloads logs from the last run associated with a given PR.
 * The log are analyzed and the log of failed stages are dumped on disk.
 * <p>
 * This command requires the run to be completed. If the builds completes successfully, no files are downloaded.
 */
@CommandLine.Command(
        name = "log",
        descriptionHeading = "Retrieve the Github Actions logs",
        description = "- Retrieve the logs generated by the run of Github Actions on a specified pull requests"
)
public class RetrieveLogForPullRequest extends GithubCommand implements Runnable, CommandLine.IExitCodeGenerator {

    @CommandLine.Option(names = { "-p", "--pr" }, description = "Set the pull request (#<id>)", required = true)
    int pr;

    @CommandLine.Option(names = { "-w", "--workflow" }, description = "Set the workflow name", required = true)
    String workflow;

    @CommandLine.Option(names = { "-o",
            "--output" }, description = "Set the output directory in which logs are written, if not set, the current working directory is used.")
    String out;

    @Inject GithubService github;

    @Inject @RestClient RunService rs;

    private static final Logger LOGGER = Logger.getLogger(RetrieveLogForPullRequest.class.getName());

    private int exit;

    @Override
    public int getExitCode() {
        return exit;
    }

    @Override
    public void run() {

        String token = getToken();
        if (token == null) {
            LOGGER.error("The Github token must be set");
            exit = CommandLine.ExitCode.USAGE;
            return;
        }

        LOGGER.infof("Retrieving the pull request #%d from %s...", pr, repo);
        PullRequest pull = github.getPullRequest(repo, pr);
        if (pull == null) {
            LOGGER.errorf("Unable to find the pull request #%d from %s", pr, repo);
            exit = CommandLine.ExitCode.USAGE;
            return;
        }

        LOGGER.infof("Pull Request #%d found: %s", pull.number, pull.html_url);

        LOGGER.infof("Retrieving the last build for #%d...", pull.number);
        Run run = github.getLastRunForPr(repo, workflow, pull.head.ref);
        if (run == null) {
            LOGGER.errorf("Unable to find a build for pull request #%d of the workflow %s", pr, workflow);
            exit = CommandLine.ExitCode.USAGE;
        } else {
            LOGGER.infof("Last build retrieved. Status is '%s'", run.status);
            if (!"completed".equalsIgnoreCase(run.status)) {
                LOGGER.warn("The run is not yet completed, cannot retrieve the logs");
                exit = 3;
                return;
            }
            if ("success".equalsIgnoreCase(run.conclusion)) {
                LOGGER.infof("The last build succeeded, check %s", run.html_url);
                return;
            }
            LOGGER.infof("Retrieving logs of run %d", run.id);
            Response response = rs.getLog(repo, "token " + token, run.id);
            ZipFile zip = download(response);
            analyze(zip);
            File output = new File(".");
            if (out != null) {
                output = new File("output");
                if (!output.isDirectory()) {
                    output.mkdirs();
                }
            }
            LOGGER.infof("Number of failed stages: %d", failures.size());
            LOGGER.infof("Writing logs in %s", output.getAbsolutePath());
            write(output);
        }
    }

    private String getToken() {
        if (super.token != null) {
            return super.token;
        } else {
            return System.getenv("GITHUB_TOKEN");
        }
    }

    private void write(File output) {
        failures.forEach((name, log) -> {
            try {
                Files.write(new File(output, name).toPath(), log.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                LOGGER.errorf(e, "Unable to write log file %s", name);
            }
        });
    }

    private void analyze(ZipFile zip) {
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!skip(entry)) {
                try (InputStream is = zip.getInputStream(entry)) {
                    readLogAndCollect(entry, is);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }
    }

    private void readLogAndCollect(ZipEntry entry, InputStream is) {
        Scanner scanner = new Scanner(is);
        LOGGER.debugf("Reading %s", entry.getName());
        LogReader reader = new LogReader(scanner);
        reader.read();
        if (reader.hasFailed()) {
            LOGGER.debugf("Failure detected in %s", entry.getName());
            store(entry.getName(), reader.getLog());
        }
    }

    private Map<String, String> failures = new LinkedHashMap<>();

    private void store(String name, String log) {
        failures.put(name, log);
    }

    private boolean skip(ZipEntry entry) {
        return entry.isDirectory() || entry.getName().contains("/");
    }

    private ZipFile download(Response response) {
        try (InputStream in = response.readEntity(InputStream.class)) {
            File tmp = File.createTempFile(pr + "-", ".zip");
            tmp.delete();
            Files.copy(in, tmp.toPath());
            return new ZipFile(tmp);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}