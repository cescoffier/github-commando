# GitHub Commando

_Rationale_: Getting the logs of failed build in the GitHub Actions UI sucks...
 It's slow; you need to get the _raw_ log; it's generally large, the failure may be in the middle of the log, so you scroll up and down to find it. 
 It's inconvenient, time-consuming, open plenty of tabs in your browser. 
 
 This project provides a CLI to download the logs of the last build (run) of a given PR.
 It also analyzes the retrieved logs to extract the failed one and save them on disk.
 So you can analyze them on your machine.
 
 ## Build
 
 You need to build Quarkus first as it's a Quarkus application (command line mode).
 Then:
 
 ```bash
mvn clean package
```

To build the native executable:

```bash
mvn package -Pnative
```

## Usage

```bash
github-commando-1.0-SNAPSHOT-runner log -r smallrye/smallrye-reactive-messaging -p 553 -w 'Pull Request Build'
```

You need to set the repository (owner/repo) like `quarkusio/quarkus`.
The pull request is specified using `-p`.
Finally, you need to specify the workflow name with `-w`

```bash
github-commando-1.0-SNAPSHOT-runner log -r quarkusio/quarkus --pr 9462 --workflow='Quarkus CI'
```

Full usage:

```text
Retrieve the Github Actions logs - Retrieve the logs generated by the run of Github Actions on a specified pull
requests
  -o, --output=<out>    Set the output directory in which logs are written, if
                          not set, the current working directory is used.
  -p, --pr=<pr>         Set the pull request (#<id>)
  -r, --repo=<repo>     Set the GitHub repository (owner/name)
  -t, --token=<token>   The Github Token, using the `GITHUB_TOKEN` env variable
                          if not set
  -w, --workflow=<workflow>
                        Set the workflow name
```

**IMPORTANT**: To download the logs, the build must be **completed**. In case of success, no logs will be downloaded.
