package me.escoffier.quarkus.github;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class PullRequest extends GithubObject {

    public String diff_url;
    public String patch_url;
    public String issue_url;
    public String review_comments_url;
    public String review_comment_url;
    public String comments_url;
    public String statuses_url;
    public int number;
    public String state;
    public boolean locked;
    public String title;
    public User user;
    public String body;
    public List<Label> labels;
    public Milestone milestone;
    public List<User> assignees;
    public List<User> requested_reviewers;
    public Reference head;
    public Reference base;
    public boolean draft;

}
