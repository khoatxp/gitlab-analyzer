package com.eris.gitlabanalyzer.model.types;

import com.eris.gitlabanalyzer.model.GitManagementUser;

@lombok.Getter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Author {
    private Long id;
    private String name;
    private String username;

    public static Author fromGitManagementUser(GitManagementUser gitManagementUser) {
        return new Author(
                gitManagementUser.getGitLabUserId(),
                gitManagementUser.getName(),
                gitManagementUser.getUsername()
        );
    }
}
