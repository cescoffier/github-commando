package me.escoffier.quarkus.utils;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogReader {

    private final Scanner scanner;
    private final StringBuilder log;
    private boolean failed;

    enum State {
        INIT,
        IN_GROUP,
        IN_MAVEN,
        TERMINAL
    }

    private State state;

    public LogReader(Scanner scanner) {
        this.scanner = scanner;
        this.log = new StringBuilder();
        this.failed = false;
    }

    public void read() {
        state = State.INIT;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            switch (state) {
                case INIT: {
                    if (isNewGroup(line)) {
                        state = State.IN_GROUP;
                        resetLog();
                    } else if (isError(line)) {
                        state = State.TERMINAL;
                        failed = true;
                        // We add the line as it sounds suspicious.
                        log.append(line).append("\n");
                    }
                    break;
                }
                case IN_GROUP: {
                    if (isMavenStart(line)) {
                        state = State.IN_MAVEN;
                        resetLog();
                        addMavenLineToLog(line);
                    } else if (isError(line)) {
                        state = State.TERMINAL;
                        failed = true;
                    } else if (isNewGroup(line)) {
                        resetLog();
                    } else {
                        addLineToLog(line);
                    }
                    break;
                }
                case IN_MAVEN: {
                    if (isError(line)) {
                        state = State.TERMINAL;
                        failed = true;
                    } else if (isNewGroup(line)) {
                        resetLog();
                    } else {
                        addMavenLineToLog(line);
                    }
                    break;
                }
                case TERMINAL: {
                    return;
                }
            }
        }
    }

    private void addLineToLog(String line) {
        Matcher timed = TIMED_LINE.matcher(line);
        if (!timed.matches()) {
            log.append(line).append("\n");
        } else {
            log.append(timed.group(1)).append("\n");
        }
    }

    private void addMavenLineToLog(String line) {
        Matcher maven = MAVEN_LINE.matcher(line);
        if (!maven.matches()) {
            log.append(line).append("\n");
        } else {
            log.append("[").append(maven.group(1)).append("] ")
                    .append(maven.group(2))
                    .append("\n");
        }
    }

    private void resetLog() {
        log.delete(0, log.length());
    }

    public boolean hasFailed() {
        return failed;
    }

    public String getLog() {
        return log.toString();
    }

    private static Pattern MAVEN_LINE = Pattern.compile("^.* \\[(.*)\\] (.*)$");
    private static Pattern MACRO_LINE = Pattern.compile("^.* ##\\[(.*)\\](.*)$");
    private static Pattern TIMED_LINE = Pattern.compile("^.*Z (.*)$");

    private static boolean isNewGroup(String line) {
        return MACRO_LINE.matcher(line).matches() && line.contains("##[group]");
    }

    private static boolean isMavenStart(String line) {
        return MAVEN_LINE.matcher(line).matches() && line.contains("[INFO] Scanning for projects...");
    }

    private static boolean isError(String line) {
        return MACRO_LINE.matcher(line).matches() && line.contains("##[error]")
                // This is collected as an error, but should continue the collection.
                // Quarkus specific...
                && ! line.contains("Exception in thread \"Quarkus Test Cleanup Shutdown task\"");
    }

}
