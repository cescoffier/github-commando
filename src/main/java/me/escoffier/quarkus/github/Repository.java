package me.escoffier.quarkus.github;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.json.bind.annotation.JsonbProperty;
import java.util.List;

@RegisterForReflection
public class Repository extends GithubObject {

    public String name;
    public String full_name;
    public User owner;
    @JsonbProperty("private")
    public boolean isPrivate;
    public String description;
    public boolean fork;
    public String archive_url;
    public String assignees_url;
    public String blobs_url;
    public String branches_url;
    public String collaborators_url;
    public String comments_url;
    public String commits_url;
    public String compare_url;
    public String contents_url;
    public String contributors_url;
    public String deployments_url;
    public String downloads_url;
    public String events_url;
    public String forks_url;
    public String git_commits_url;
    public String git_refs_url;
    public String git_tags_url;
    public String git_url;
    public String issue_comment_url;
    public String issue_events_url;
    public String issues_url;
    public String keys_url;
    public String labels_url;
    public String languages_url;
    public String merges_url;
    public String milestones_url;
    public String notifications_url;
    public String pulls_url;
    public String releases_url;
    public String ssh_url;
    public String stargazers_url;
    public String statuses_url;
    public String subscribers_url;
    public String subscription_url;
    public String tags_url;
    public String teams_url;
    public String trees_url;
    public String clone_url;
    public String mirror_url;
    public String hooks_url;
    public String svn_url;
    public String homepage;

    public int forks_count;
    public int stargazers_count;
    public int watchers_count;
    public int size;
    public String default_branch;
    public int open_issues_count;
    public boolean is_template;
    public List<String> topics;
    public boolean has_issues;
    public boolean has_projects;
    public boolean has_wiki;
    public boolean has_pages;
    public boolean has_downloads;
    public boolean archived;
    public boolean disabled;
    public String visibility;
    public String pushed_at;
    public int subscribers_count;

}
