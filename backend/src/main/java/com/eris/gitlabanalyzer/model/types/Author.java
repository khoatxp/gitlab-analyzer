package com.eris.gitlabanalyzer.model.types;

import com.eris.gitlabanalyzer.model.GitManagementUser;

@lombok.Getter
@lombok.AllArgsConstructor
public class Author {
    private final Long id;
    private final String name;
    private final String username;

    public static Author fromGitManagementUser(GitManagementUser gitManagementUser) {
        return new Author(
                gitManagementUser.getGitLabUserId(),
                gitManagementUser.getName(),
                gitManagementUser.getUsername()
        );
    }
}
