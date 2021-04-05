package com.eris.gitlabanalyzer.viewmodel;

import com.eris.gitlabanalyzer.model.GitManagementUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GitManagementUserView {
    private final Long id;
    private final String username;
    private final String name;

    public static GitManagementUserView fromGitManagementUser(GitManagementUser gitManagementUser){
        return new GitManagementUserView(
                gitManagementUser.getId(),
                gitManagementUser.getUsername(),
                gitManagementUser.getName()
        );
    }
}
