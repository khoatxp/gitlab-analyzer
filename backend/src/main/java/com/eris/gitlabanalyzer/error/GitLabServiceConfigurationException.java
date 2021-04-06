package com.eris.gitlabanalyzer.error;

public class GitLabServiceConfigurationException extends RuntimeException {
    public GitLabServiceConfigurationException(String errorMessage) {
        super(errorMessage);
    }
}
